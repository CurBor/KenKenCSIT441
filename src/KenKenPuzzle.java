import java.util.ArrayList;
import java.util.List;

/**
 * KenKen Puzzle
 * Created by Zach Widger and Wei Zhou
 * For CSIT 441 Artificial Intelligence
 * Finished on 3/28/2016
 */

public class KenKenPuzzle {

    private int size;
    private int rows = 4;
    private int cols = 4;
    private boolean change = false; // for general consistency looping

    public void setObjectList(BoxObject[] objectList) {
        this.objectList = objectList;
    }

    private BoxObject objectList[];
    private List<Integer> domain[][];
    private List<Integer> constraint[][][];
    private int[][] assignments;

    private int searchAssignmentsStore[][];


    public BoxObject[] getObjectList() {
        return objectList;
    }

    public void SetDomain(int size) {
        this.size = size;
        domain = new List[size][size];


        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                domain[x][y] = new ArrayList();
                for (int z = 1; z <= size; z++)
                    domain[x][y].add(z);
            }
        }

        SetConstraints();

    }

    public void loadBoxObjects(BoxObject[] list) {
        this.objectList = list;
    }

    public void SetConstraints() {
        constraint = new List[size][size][2];//0 for cubes that not equal, 1 for relationship with List of Box Object
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                constraint[x][y][0] = setNotEqual(x, y);
                //System.out.println(constraint[x][y][0].toString());
                constraint[x][y][1] = setRelationshipConstaint(x, y);
                //System.out.println(constraint[x][y][1].toString());
            }
        }


    }


    public List<Integer> setRelationshipConstaint(int row, int col) {
        List<Integer> returnList = new ArrayList();


        for (int x = 0; x < objectList.length - 1; x++) {
            for (int y = 0; y < objectList[x].getCubeList().size() / 2; y++) {
                int[] list = objectList[x].indexOfCube(y);
                if (row == list[0] && col == list[1]) {
                    returnList.add(x);
                    return returnList;
                }
            }
        }

        return null;
    }

    public List<Integer> setNotEqual(int row, int col) {
        List<Integer> returnList = new ArrayList();
        for (int x = 0; x < size; x++) {
            if (x != row) {
                returnList.add(x);
                returnList.add(col);
            }

        }
        for (int x = 0; x < size; x++) {
            if (x != col) {
                returnList.add(row);
                returnList.add(x);
            }
        }

        return returnList;
    }

    public void nodeConsistency() {
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                int pointer = constraint[x][y][1].get(0);
                if (objectList[pointer].cubeList.size() == 2) {
                    int goal = objectList[pointer].getGoal();
                    for (int z = 0; z < domain[x][y].size(); z++) {
                        if (domain[x][y].get(z) != goal) {

                            domain[x][y].remove(z);
                            //System.out.println("box : "+ x + y +domain[x][y].toString() + " and number "+ z);
                            z--;
                        }
                    }
                }
            }
        }
    }

    public void archConsistency() {
        notEqualsArchConsistency();
        operatorArchConisitency();
        checkSingleDomain();

    }

    public void fullArchConsistency(){
        do {
            change = false;
            notEqualsArchConsistency();
            operatorArchConisitency();
            checkSingleDomain();
        } while (change);
    }

    public void generalConsistency() {
        do {
            change = false;
            nodeConsistency();
            notEqualsArchConsistency();
            generalOperatorConisitency();
            checkSingleDomain();
        } while (change);
    }


    /*
        this will go through all of the consistencies we have set up that are not equals statements.
        If any domain is of size 1 than it will go through and remove that value from all box domains
        that cannot equal the initial box.
     */
    public void notEqualsArchConsistency() {
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                for (int c = 0; c < constraint[x][y][0].size(); c += 2) {
                    int compBoxX = constraint[x][y][0].get(c);
                    int compBoxY = constraint[x][y][0].get(c + 1);
                    if (domain[x][y].size() == 1) {
                        if (domain[compBoxX][compBoxY].contains(domain[x][y].get(0))) {
                            domain[compBoxX][compBoxY].remove(new Integer(domain[x][y].get(0)));
                            change = true;
                            //System.out.println("from box: " + compBoxX + compBoxY + " and compbox domain size" + domain[compBoxX][compBoxY].toString());
                        }
                    }
                }

            }
        }
    }

    public void operatorArchConisitency() {
        for (int objectCounter = 0; objectCounter < objectList.length - 1; objectCounter++) {
            if (objectList[objectCounter].getOpertor() == '+') {
                archConisitencyPlus(objectCounter);
            } else if (objectList[objectCounter].getOpertor() == '-') {
                archConisitencySubtract(objectCounter);
            } else if (objectList[objectCounter].getOpertor() == '/') {
                archConisitencyDivide(objectCounter);
            } else if (objectList[objectCounter].getOpertor() == 'x' || objectList[objectCounter].getOpertor() == 'X') {
                archConisitencyMultiply(objectCounter);
            }
        }
    }

    private void archConisitencyPlus(int counter) {
        int goal = objectList[counter].getGoal();
        List<Integer> list = new ArrayList<Integer>();
        for (int x = 0; x < objectList[counter].getCubeList().size(); x++) {
            list.add(objectList[counter].cubeList.get(x));
        }


        for (int x = 0; x < list.size() / 2; x++) {
            if (domain[list.get(2 * x)][list.get(2 * x + 1)].size() == 1 && list.size() <= 2) {
                goal = goal - domain[list.get(2 * x)][list.get(2 * x + 1)].get(0);
                list.remove(2 * x);
                list.remove(2 * x + 1);
                //System.out.println(list.size());
                x--;
            }
        }
        //System.out.println(list.size() / 2);

        if (list.size() / 2 == 1) {
            if (goal > 0 && goal <= size && checkdomain(list.get(0), list.get(1), goal)) {
                domain[list.get(0)][list.get(1)] = new ArrayList<Integer>();
                domain[list.get(0)][list.get(1)].add(goal);
            }
        } else if (list.size() / 2 == 2) {
            int fisrtX = list.get(0);
            int fisrtY = list.get(1);
            int secondX = list.get(2);
            int secondY = list.get(3);

            int checklistX[] = new int[size];
            int checklistY[] = new int[size];
            for (int x = 0; x < size; x++) {
                checklistX[x] = 0;
                checklistY[x] = 0;
            }
            for (int x = 1; x <= size; x++) {
                for (int y = 1; y <= size; y++) {
                    if (goal == x + y) {
                        if (checkdomain(fisrtX, fisrtY, x) && checkdomain(secondX, secondY, y)) {
                            checklistX[x - 1] = 1;
                            checklistY[y - 1] = 1;
                        }
                    }
                }
            }

            System.out.println("domain +:" + fisrtX + "," + fisrtY);
            System.out.println(domain[fisrtX][fisrtY].toString());
            System.out.println("domain +:" + secondX + "," + secondY);
            System.out.println(domain[secondX][secondY].toString());
            for (int x = 0; x < size; x++) {
                if (checklistX[x] == 0) {
                    if(domain[fisrtX][fisrtY].contains(new Integer(x+1))){
                        change = true;
                    }
                    domain[fisrtX][fisrtY].remove(new Integer(x + 1));
                }
                if (checklistY[x] == 0) {
                    if(domain[secondX][secondY].contains(new Integer(x+1))){
                        change = true;
                    }
                    domain[secondX][secondY].remove(new Integer(x + 1));
                }

            }
            System.out.println("domain +:" + fisrtX + "," + fisrtY);
            System.out.println(domain[fisrtX][fisrtY].toString());
            System.out.println("domain +:" + secondX + "," + secondY);
            System.out.println(domain[secondX][secondY].toString());

        }
    }

    private void archConisitencyMultiply(int counter) {
        int goal = objectList[counter].getGoal();
        List<Integer> list = new ArrayList<Integer>();
        for (int x = 0; x < objectList[counter].getCubeList().size(); x++) {
            list.add(objectList[counter].cubeList.get(x));
        }


        for (int x = 0; x < list.size() / 2; x++) {
            if (domain[list.get(2 * x)][list.get(2 * x + 1)].size() == 1 && list.size() > 2) {
                goal = goal / domain[list.get(2 * x)][list.get(2 * x + 1)].get(0);
                list.remove(2 * x);
                list.remove(2 * x + 1);
                x--;
            }
        }

        if (list.size() / 2 == 1) {
            if (goal > 0 && goal <= size && checkdomain(list.get(0), list.get(1), goal)) {
                domain[list.get(0)][list.get(1)] = new ArrayList<Integer>();
                domain[list.get(0)][list.get(1)].add(goal);
            }
        } else if (list.size() / 2 == 2) {
            int fisrtX = list.get(0);
            int fisrtY = list.get(1);
            int secondX = list.get(2);
            int secondY = list.get(3);

            int checklistX[] = new int[size];
            int checklistY[] = new int[size];
            for (int x = 0; x < size; x++) {
                checklistX[x] = 0;
                checklistY[x] = 0;
            }
            for (int x = 1; x <= size; x++) {
                for (int y = 1; y <= size; y++) {
                    if (goal == x * y) {
                        if (checkdomain(fisrtX, fisrtY, x) && checkdomain(secondX, secondY, y)) {
                            checklistX[x - 1] = 1;
                            checklistY[y - 1] = 1;
                        }
                    }
                }
            }
            for (int x = 0; x < size; x++) {
                if (checklistX[x] == 0) {
                    if(domain[fisrtX][fisrtY].contains(new Integer(x+1))){
                        change = true;
                    }
                    domain[fisrtX][fisrtY].remove(new Integer(x + 1));
                }
                if (checklistY[x] == 0) {
                    if(domain[secondX][secondY].contains(new Integer(x+1))){
                        change = true;
                    }
                    domain[secondX][secondY].remove(new Integer(x + 1));
                }
            }
        }
    }

    private void archConisitencyDivide(int counter) {

        int fisrtX = objectList[counter].cubeList.get(0);
        int fisrtY = objectList[counter].cubeList.get(1);
        int secondX = objectList[counter].cubeList.get(2);
        int secondY = objectList[counter].cubeList.get(3);
        int goal = objectList[counter].getGoal();
        if (domain[fisrtX][fisrtY].size() == 1) {
            int vault = domain[fisrtX][fisrtY].get(0);
            int checklistY[] = new int[size];
            for (int x = 0; x < size; x++) {
                checklistY[x] = 0;
            }
            if (goal * vault <= size && goal * vault > 0) {
                checklistY[(goal * vault) - 1] = 1;
            }
            if (vault / goal <= size && vault / goal > 0 && vault % goal == 0) {
                checklistY[(vault / goal) - 1] = 1;
            }
            for (int x = 0; x < size; x++) {
                if (checklistY[x] == 0) {
                    if(domain[secondX][secondY].contains(new Integer(x+1))){
                        change = true;
                    }
                    domain[secondX][secondY].remove(new Integer(x + 1));
                }
            }
        } else if (domain[secondX][secondY].size() == 1) {
            int vault = domain[secondX][secondY].get(0);
            int checklistX[] = new int[size];
            for (int x = 0; x < size; x++) {
                checklistX[x] = 0;
            }
            if (goal * vault <= size && goal * vault > 0) {
                checklistX[(goal * vault) - 1] = 1;
            }
            if (vault / goal <= size && vault / goal > 0 && vault % goal == 0) {
                checklistX[(vault / goal) - 1] = 1;
            }
            for (int x = 0; x < size; x++) {
                if (checklistX[x] == 0) {
                    if(domain[fisrtX][fisrtY].contains(new Integer(x+1))){
                        change = true;
                    }
                    domain[fisrtX][fisrtY].remove(new Integer(x + 1));
                }
            }
        } else {
            int checklistX[] = new int[size];
            int checklistY[] = new int[size];
            for (int x = 0; x < size; x++) {
                checklistX[x] = 0;
                checklistY[x] = 0;
            }
            for (int x = 1; x <= size; x++) {
                for (int y = 1; y <= size; y++) {
                    if (goal * x == y) {
                        if (checkdomain(fisrtX, fisrtY, x) && checkdomain(secondX, secondY, y)) {
                            checklistX[x - 1] = 1;
                            checklistY[y - 1] = 1;
                        }
                        if (checkdomain(secondX, secondY, x) && checkdomain(fisrtX, fisrtY, y)) {
                            checklistX[y - 1] = 1;
                            checklistY[x - 1] = 1;
                        }
                    }
                }
            }

            System.out.println("domain:" + fisrtX + "," + fisrtY);
            System.out.println(domain[fisrtX][fisrtY].toString());
            System.out.println("domain:" + secondX + "," + secondY);
            System.out.println(domain[secondX][secondY].toString());
            for (int x = 0; x < size; x++) {
                if (checklistX[x] == 0) {
                    if(domain[fisrtX][fisrtY].contains(new Integer(x+1))){
                        change = true;
                    }
                    domain[fisrtX][fisrtY].remove(new Integer(x + 1));
                }
                if (checklistY[x] == 0) {
                    if(domain[secondX][secondY].contains(new Integer(x+1))){
                        change = true;
                    }
                    domain[secondX][secondY].remove(new Integer(x + 1));
                }
            }
            System.out.println("domain:" + fisrtX + "," + fisrtY);
            System.out.println(domain[fisrtX][fisrtY].toString());
            System.out.println("domain:" + secondX + "," + secondY);
            System.out.println(domain[secondX][secondY].toString());
        }
    }

    public void archConisitencySubtract(int counter) {
        int fisrtX = objectList[counter].cubeList.get(0);
        int fisrtY = objectList[counter].cubeList.get(1);
        int secondX = objectList[counter].cubeList.get(2);
        int secondY = objectList[counter].cubeList.get(3);
        int goal = objectList[counter].getGoal();
        if (domain[fisrtX][fisrtY].size() == 1) {
            int vault = domain[fisrtX][fisrtY].get(0);
            int checklistY[] = new int[size];
            for (int x = 0; x < size; x++) {
                checklistY[x] = 0;
            }
            if (goal + vault <= size && goal + vault > 0) {
                checklistY[goal + vault - 1] = 1;
            }
            if (vault - goal <= size && vault - goal > 0) {
                checklistY[vault - goal - 1] = 1;
            }
            for (int x = 0; x < size; x++) {
                if (checklistY[x] == 0) {
                    if(domain[secondX][secondY].contains(new Integer(x+1))){
                        change = true;
                    }
                    domain[secondX][secondY].remove(new Integer(x + 1));
                }
            }

        } else if (domain[secondX][secondY].size() == 1) {
            int vault = domain[secondX][secondY].get(0);
            int checklistX[] = new int[size];
            for (int x = 0; x < size; x++) {
                checklistX[x] = 0;
            }
            if (goal + vault <= size && goal + vault > 0) {
                checklistX[goal + vault - 1] = 1;
            }
            if (vault - goal <= size && vault - goal > 0) {
                checklistX[vault - goal - 1] = 1;
            }
            for (int x = 0; x < size; x++) {
                if (checklistX[x] == 0) {
                    if(domain[fisrtX][fisrtY].contains(new Integer(x+1))){
                        change = true;
                    }
                    domain[fisrtX][fisrtY].remove(new Integer(x + 1));
                }
            }
        } else {
            int checklistX[] = new int[size];
            int checklistY[] = new int[size];
            for (int x = 0; x < size; x++) {
                checklistX[x] = 0;
                checklistY[x] = 0;
            }
            for (int x = 1; x <= size; x++) {
                for (int y = 1; y <= size; y++) {
                    if (goal + x == y) {
                        if (checkdomain(fisrtX, fisrtY, x) && checkdomain(secondX, secondY, y)) {
                            checklistX[x - 1] = 1;
                            checklistY[y - 1] = 1;
                        }
                        if (checkdomain(secondX, secondY, x) && checkdomain(fisrtX, fisrtY, y)) {
                            checklistX[y - 1] = 1;
                            checklistY[x - 1] = 1;
                        }
                    }
                }
            }
            for (int x = 0; x < size; x++) {
                if (checklistX[x] == 0) {
                    if(domain[fisrtX][fisrtY].contains(new Integer(x+1))){
                        change = true;
                    }
                    domain[fisrtX][fisrtY].remove(new Integer(x + 1));

                }
                if (checklistY[x] == 0) {
                    if(domain[secondX][secondY].contains(new Integer(x+1))){
                        change = true;
                    }
                    domain[secondX][secondY].remove(new Integer(x + 1));
                }
            }
        }
    }

    public void generalOperatorConisitency() {
        for (int objectCounter = 0; objectCounter < objectList.length - 1; objectCounter++) {
            if (objectList[objectCounter].getOpertor() == '+') {
                generalConisitencyPlus(objectCounter);
            } else if (objectList[objectCounter].getOpertor() == '-') {
                archConisitencySubtract(objectCounter);
            } else if (objectList[objectCounter].getOpertor() == '/') {
                archConisitencyDivide(objectCounter);
            } else if (objectList[objectCounter].getOpertor() == 'x' || objectList[objectCounter].getOpertor() == 'X') {
                generalConisitencyMultiply(objectCounter);
            }
        }
    }

    private void generalConisitencyPlus(int counter) {
        int goal = objectList[counter].getGoal();
        List<Integer> list = new ArrayList<Integer>();
        for (int x = 0; x < objectList[counter].getCubeList().size(); x++) {
            list.add(objectList[counter].cubeList.get(x));
        }


        for (int x = 0; x < list.size() / 2; x++) {
            if (domain[list.get(2 * x)][list.get(2 * x + 1)].size() == 1 && list.size() <= 2) {
                goal = goal - domain[list.get(2 * x)][list.get(2 * x + 1)].get(0);
                list.remove(2 * x);
                list.remove(2 * x + 1);
                System.out.println(list.size());
                x--;
            }
        }
        System.out.println(list.size() / 2);

        if (list.size() / 2 == 1) {
            if (goal > 0 && goal <= size && checkdomain(list.get(0), list.get(1), goal)) {
                domain[list.get(0)][list.get(1)] = new ArrayList<Integer>();
                domain[list.get(0)][list.get(1)].add(goal);
            }
        } else if (list.size() / 2 == 2) {
            int fisrtX = list.get(0);
            int fisrtY = list.get(1);
            int secondX = list.get(2);
            int secondY = list.get(3);

            int checklistX[] = new int[size];
            int checklistY[] = new int[size];
            for (int x = 0; x < size; x++) {
                checklistX[x] = 0;
                checklistY[x] = 0;
            }
            for (int x = 1; x <= size; x++) {
                for (int y = 1; y <= size; y++) {
                    if (goal == x + y) {
                        if (checkdomain(fisrtX, fisrtY, x) && checkdomain(secondX, secondY, y)) {
                            checklistX[x - 1] = 1;
                            checklistY[y - 1] = 1;
                        }
                    }
                }
            }

            System.out.println("domain +:" + fisrtX + "," + fisrtY);
            System.out.println(domain[fisrtX][fisrtY].toString());
            System.out.println("domain +:" + secondX + "," + secondY);
            System.out.println(domain[secondX][secondY].toString());
            for (int x = 0; x < size; x++) {
                if (checklistX[x] == 0) {
                    if(domain[fisrtX][fisrtY].contains(new Integer(x+1))){
                        change = true;
                    }
                    domain[fisrtX][fisrtY].remove(new Integer(x + 1));
                }
                if (checklistY[x] == 0) {
                    if(domain[secondX][secondY].contains(new Integer(x+1))){
                        change = true;
                    }
                    domain[secondX][secondY].remove(new Integer(x + 1));
                }

            }
            System.out.println("domain +:" + fisrtX + "," + fisrtY);
            System.out.println(domain[fisrtX][fisrtY].toString());
            System.out.println("domain +:" + secondX + "," + secondY);
            System.out.println(domain[secondX][secondY].toString());

        } else if (list.size() / 2 == 3) {
            int fisrtX = list.get(0);
            int fisrtY = list.get(1);
            int secondX = list.get(2);
            int secondY = list.get(3);
            int thirdX = list.get(4);
            int thirdY = list.get(5);

            int checklist1[] = new int[size];
            int checklist2[] = new int[size];
            int checklist3[] = new int[size];
            for (int x = 0; x < size; x++) {
                checklist1[x] = 0;
                checklist2[x] = 0;
                checklist3[x] = 0;
            }
            for (int x = 1; x <= size; x++) {
                for (int y = 1; y <= size; y++)
                    for (int z = 1; z <= size; z++)
                        if (goal == x + y + z) {
                            if (checkdomain(fisrtX, fisrtY, x) && checkdomain(secondX, secondY, y) && checkdomain(thirdX, thirdY, z)) {
                                checklist1[x - 1] = 1;
                                checklist2[y - 1] = 1;
                                checklist3[z - 1] = 1;
                            }
                        }
            }


            System.out.println("domain 3+:" + fisrtX + "," + fisrtY);
            System.out.println(domain[fisrtX][fisrtY].toString());
            System.out.println("domain 3+:" + secondX + "," + secondY);
            System.out.println(domain[secondX][secondY].toString());
            System.out.println("domain 3+:" + thirdX + "," + thirdY);
            System.out.println(domain[thirdX][thirdY].toString());
            for (int x = 0; x < size; x++) {
                if (checklist1[x] == 0) {
                    if(domain[fisrtX][fisrtY].contains(new Integer(x+1))){
                        change = true;
                    }
                    domain[fisrtX][fisrtY].remove(new Integer(x + 1));
                }
                if (checklist2[x] == 0) {
                    if(domain[secondX][secondY].contains(new Integer(x+1))){
                        change = true;
                    }
                    domain[secondX][secondY].remove(new Integer(x + 1));
                }
                if (checklist3[x] == 0) {
                    if(domain[thirdX][thirdY].contains(new Integer(x+1))){
                        change = true;
                    }
                    domain[thirdX][thirdY].remove(new Integer(x + 1));
                }
            }
            System.out.println("domain 3+:" + fisrtX + "," + fisrtY);
            System.out.println(domain[fisrtX][fisrtY].toString());
            System.out.println("domain 3+:" + secondX + "," + secondY);
            System.out.println(domain[secondX][secondY].toString());
            System.out.println("domain 3+:" + thirdX + "," + thirdY);
            System.out.println(domain[thirdX][thirdY].toString());

        } else if (list.size() / 2 == 4) {
            int firstX = list.get(0);
            int firstY = list.get(1);
            int secondX = list.get(2);
            int secondY = list.get(3);
            int thirdX = list.get(4);
            int thirdY = list.get(5);
            int fourthX = list.get(6);
            int fourthY = list.get(7);

            int checklist1[] = new int[size];
            int checklist2[] = new int[size];
            int checklist3[] = new int[size];
            int checklist4[] = new int[size];
            for (int x = 0; x < size; x++) {
                checklist1[x] = 0;
                checklist2[x] = 0;
                checklist3[x] = 0;
                checklist4[x] = 0;
            }
            for (int x = 1; x <= size; x++) {
                for (int y = 1; y <= size; y++)
                    for (int z = 1; z <= size; z++)
                        for (int f = 1; f <= size; f++)
                            if (goal == x + y + z + f) {
                                if (checkdomain(firstX, firstY, x) && checkdomain(secondX, secondY, y) && checkdomain(thirdX, thirdY, z) && checkdomain(fourthX, firstY, f)) {
                                    checklist1[x - 1] = 1;
                                    checklist2[y - 1] = 1;
                                    checklist3[z - 1] = 1;
                                    checklist4[f - 1] = 1;
                                }
                            }
            }

            for (int x = 0; x < size; x++) {
                if (checklist1[x] == 0) {
                    if(domain[firstX][firstY].contains(new Integer(x+1))){
                        change = true;
                    }
                    domain[firstX][firstY].remove(new Integer(x + 1));
                }
                if (checklist2[x] == 0) {
                    if(domain[secondX][secondY].contains(new Integer(x+1))){
                        change = true;
                    }
                    domain[secondX][secondY].remove(new Integer(x + 1));
                }
                if (checklist3[x] == 0) {
                    if(domain[thirdX][thirdY].contains(new Integer(x+1))){
                        change = true;
                    }
                    domain[thirdX][thirdY].remove(new Integer(x + 1));
                }
                if (checklist4[x] == 0) {
                    if(domain[fourthX][fourthY].contains(new Integer(x+1))){
                        change = true;
                    }
                    domain[fourthX][fourthY].remove(new Integer(x + 1));
                }
            }

        } else if (list.size() / 2 == 5) {
            int firstX = list.get(0);
            int firstY = list.get(1);
            int secondX = list.get(2);
            int secondY = list.get(3);
            int thirdX = list.get(4);
            int thirdY = list.get(5);
            int fourthX = list.get(6);
            int fourthY = list.get(7);
            int fivethX = list.get(8);
            int fivethY = list.get(9);

            int checklist1[] = new int[size];
            int checklist2[] = new int[size];
            int checklist3[] = new int[size];
            int checklist4[] = new int[size];
            int checklist5[] = new int[size];
            for (int x = 0; x < size; x++) {
                checklist1[x] = 0;
                checklist2[x] = 0;
                checklist3[x] = 0;
                checklist4[x] = 0;
                checklist5[x] = 0;
            }
            for (int x = 1; x <= size; x++) {
                for (int y = 1; y <= size; y++)
                    for (int z = 1; z <= size; z++)
                        for (int f = 1; f <= size; f++)
                            for (int g = 1; g <= size; g++)
                                if (goal == x + y + z + f + g) {
                                    if (checkdomain(firstX, firstY, x) && checkdomain(secondX, secondY, y) && checkdomain(thirdX, thirdY, z) && checkdomain(fourthX, firstY, f) && checkdomain(fivethX, fivethY, g)) {
                                        checklist1[x - 1] = 1;
                                        checklist2[y - 1] = 1;
                                        checklist3[z - 1] = 1;
                                        checklist4[f - 1] = 1;
                                        checklist5[g - 1] = 1;
                                    }
                                }
            }

            for (int x = 0; x < size; x++) {
                if (checklist1[x] == 0) {
                    if(domain[firstX][firstY].contains(new Integer(x+1))){
                        change = true;
                    }
                    domain[firstX][firstY].remove(new Integer(x + 1));
                }
                if (checklist2[x] == 0) {
                    if(domain[secondX][secondY].contains(new Integer(x+1))){
                        change = true;
                    }
                    domain[secondX][secondY].remove(new Integer(x + 1));
                }
                if (checklist3[x] == 0) {
                    if(domain[thirdX][thirdY].contains(new Integer(x+1))){
                        change = true;
                    }
                    domain[thirdX][thirdY].remove(new Integer(x + 1));
                }
                if (checklist4[x] == 0) {
                    if(domain[fourthX][fourthY].contains(new Integer(x+1))){
                        change = true;
                    }
                    domain[fourthX][fourthY].remove(new Integer(x + 1));
                }
                if (checklist5[x] == 0) {
                    if(domain[fivethX][fivethY].contains(new Integer(x+1))){
                        change = true;
                    }
                    domain[fivethX][fivethY].remove(new Integer(x + 1));
                }
            }

        }
    }

    private void generalConisitencyMultiply(int counter) {
        int goal = objectList[counter].getGoal();
        List<Integer> list = new ArrayList<Integer>();
        for (int x = 0; x < objectList[counter].getCubeList().size(); x++) {
            list.add(objectList[counter].cubeList.get(x));
        }


        for (int x = 0; x < list.size() / 2; x++) {
            if (domain[list.get(2 * x)][list.get(2 * x + 1)].size() == 1 && list.size() > 2) {
                goal = goal / domain[list.get(2 * x)][list.get(2 * x + 1)].get(0);
                list.remove(2 * x);
                list.remove(2 * x + 1);
                x--;
            }
        }

        if (list.size() / 2 == 1) {
            if (goal > 0 && goal <= size && checkdomain(list.get(0), list.get(1), goal)) {
                domain[list.get(0)][list.get(1)] = new ArrayList<Integer>();
                domain[list.get(0)][list.get(1)].add(goal);
            }
        } else if (list.size() / 2 == 2) {
            int fisrtX = list.get(0);
            int fisrtY = list.get(1);
            int secondX = list.get(2);
            int secondY = list.get(3);

            int checklistX[] = new int[size];
            int checklistY[] = new int[size];
            for (int x = 0; x < size; x++) {
                checklistX[x] = 0;
                checklistY[x] = 0;
            }
            for (int x = 1; x <= size; x++) {
                for (int y = 1; y <= size; y++) {
                    if (goal == x * y) {
                        if (checkdomain(fisrtX, fisrtY, x) && checkdomain(secondX, secondY, y)) {
                            checklistX[x - 1] = 1;
                            checklistY[y - 1] = 1;
                        }
                    }
                }
            }

            for (int x = 0; x < size; x++) {
                if (checklistX[x] == 0) {
                    if(domain[fisrtX][fisrtY].contains(new Integer(x+1))){
                        change = true;
                    }
                    domain[fisrtX][fisrtY].remove(new Integer(x + 1));
                }
                if (checklistY[x] == 0) {
                    if(domain[secondX][secondY].contains(new Integer(x+1))){
                        change = true;
                    }
                    domain[secondX][secondY].remove(new Integer(x + 1));
                }

            }

        } else if (list.size() / 2 == 3) {
            int fisrtX = list.get(0);
            int fisrtY = list.get(1);
            int secondX = list.get(2);
            int secondY = list.get(3);
            int thirdX = list.get(4);
            int thirdY = list.get(5);

            int checklist1[] = new int[size];
            int checklist2[] = new int[size];
            int checklist3[] = new int[size];
            for (int x = 0; x < size; x++) {
                checklist1[x] = 0;
                checklist2[x] = 0;
                checklist3[x] = 0;
            }
            for (int x = 1; x <= size; x++) {
                for (int y = 1; y <= size; y++)
                    for (int z = 1; z <= size; z++)
                        if (goal == x * y * z) {
                            if (checkdomain(fisrtX, fisrtY, x) && checkdomain(secondX, secondY, y) && checkdomain(thirdX, thirdY, z)) {
                                checklist1[x - 1] = 1;
                                checklist2[y - 1] = 1;
                                checklist3[z - 1] = 1;
                            }
                        }
            }
            for (int x = 0; x < size; x++) {
                if (checklist1[x] == 0) {
                    if(domain[fisrtX][fisrtY].contains(new Integer(x+1))){
                        change = true;
                    }
                    domain[fisrtX][fisrtY].remove(new Integer(x + 1));
                }
                if (checklist2[x] == 0) {
                    if(domain[secondX][secondY].contains(new Integer(x+1))){
                        change = true;
                    }
                    domain[secondX][secondY].remove(new Integer(x + 1));
                }
                if (checklist3[x] == 0) {
                    if(domain[thirdX][thirdY].contains(new Integer(x+1))){
                        change = true;
                    }
                    domain[thirdX][thirdY].remove(new Integer(x + 1));
                }
            }
        }else if (list.size() / 2 == 4) {
            int fisrtX = list.get(0);
            int fisrtY = list.get(1);
            int secondX = list.get(2);
            int secondY = list.get(3);
            int thirdX = list.get(4);
            int thirdY = list.get(5);
            int fourthX = list.get(6);
            int fourthY = list.get(7);

            int checklist1[] = new int[size];
            int checklist2[] = new int[size];
            int checklist3[] = new int[size];
            int checklist4[] = new int[size];
            for (int x = 0; x < size; x++) {
                checklist1[x] = 0;
                checklist2[x] = 0;
                checklist3[x] = 0;
                checklist4[x] = 0;
            }
            for (int x = 1; x <= size; x++) {
                for (int y = 1; y <= size; y++)
                    for (int z = 1; z <= size; z++)
                        for (int f = 1; f <= size; f++){
                            if (goal == x * y * z * f) {
                                if (checkdomain(fisrtX, fisrtY, x) && checkdomain(secondX, secondY, y) && checkdomain(thirdX, thirdY, z) && checkdomain(fourthX, fourthY, f)) {
                                    checklist1[x - 1] = 1;
                                    checklist2[y - 1] = 1;
                                    checklist3[z - 1] = 1;
                                    checklist4[f - 1] = 1;
                                }
                            }
                        }
            }


            System.out.println("domain 4+:" + fisrtX + "," + fisrtY);
            System.out.println(domain[fisrtX][fisrtY].toString());
            System.out.println("domain 4+:" + secondX + "," + secondY);
            System.out.println(domain[secondX][secondY].toString());
            System.out.println("domain 4+:" + thirdX + "," + thirdY);
            System.out.println(domain[thirdX][thirdY].toString());
            System.out.println("domain 4+:" + fourthX + "," + fourthY);
            System.out.println(domain[fourthX][fourthY].toString());
            for (int x = 0; x < size; x++) {
                if (checklist1[x] == 0) {
                    if(domain[fisrtX][fisrtY].contains(new Integer(x+1))){
                        change = true;
                    }
                    domain[fisrtX][fisrtY].remove(new Integer(x + 1));
                }
                if (checklist2[x] == 0) {
                    if(domain[secondX][secondY].contains(new Integer(x+1))){
                        change = true;
                    }
                    domain[secondX][secondY].remove(new Integer(x + 1));
                }
                if (checklist3[x] == 0) {
                    if(domain[thirdX][thirdY].contains(new Integer(x+1))){
                        change = true;
                    }
                    domain[thirdX][thirdY].remove(new Integer(x + 1));
                }
                if (checklist4[x] == 0) {
                    if(domain[fourthX][fourthY].contains(new Integer(x+1))){
                        change = true;
                    }
                    domain[fourthX][fourthY].remove(new Integer(x + 1));
                }
            }
            System.out.println("domain 4+:" + fisrtX + "," + fisrtY);
            System.out.println(domain[fisrtX][fisrtY].toString());
            System.out.println("domain 4+:" + secondX + "," + secondY);
            System.out.println(domain[secondX][secondY].toString());
            System.out.println("domain 4+:" + thirdX + "," + thirdY);
            System.out.println(domain[thirdX][thirdY].toString());
            System.out.println("domain 4+:" + fourthX + "," + fourthY);
            System.out.println(domain[fourthX][fourthY].toString());

        }else if (list.size() / 2 == 5) {
            int fisrtX = list.get(0);
            int fisrtY = list.get(1);
            int secondX = list.get(2);
            int secondY = list.get(3);
            int thirdX = list.get(4);
            int thirdY = list.get(5);
            int fourthX = list.get(6);
            int fourthY = list.get(7);
            int fifthX = list.get(8);
            int fifthY = list.get(9);

            int checklist1[] = new int[size];
            int checklist2[] = new int[size];
            int checklist3[] = new int[size];
            int checklist4[] = new int[size];
            int checklist5[] = new int[size];
            for (int x = 0; x < size; x++) {
                checklist1[x] = 0;
                checklist2[x] = 0;
                checklist3[x] = 0;
                checklist4[x] = 0;
                checklist5[x] = 0;
            }
            for (int x = 1; x <= size; x++) {
                for (int y = 1; y <= size; y++)
                    for (int z = 1; z <= size; z++)
                        for (int f = 1; f <= size; f++){
                            for (int g = 1; g <= size; g++) {
                                if (goal == x * y * z * f * g) {
                                    if (checkdomain(fisrtX, fisrtY, x) && checkdomain(secondX, secondY, y) && checkdomain(thirdX, thirdY, z) && checkdomain(fourthX, fourthY, f) && checkdomain(fifthX, fifthY, g)) {
                                        checklist1[x - 1] = 1;
                                        checklist2[y - 1] = 1;
                                        checklist3[z - 1] = 1;
                                        checklist4[f - 1] = 1;
                                        checklist5[g - 1] = 1;
                                    }
                                }
                            }
                        }
            }

            System.out.println("domain 5+:" + fisrtX + "," + fisrtY);
            System.out.println(domain[fisrtX][fisrtY].toString());
            System.out.println("domain 5+:" + secondX + "," + secondY);
            System.out.println(domain[secondX][secondY].toString());
            System.out.println("domain 5+:" + thirdX + "," + thirdY);
            System.out.println(domain[thirdX][thirdY].toString());
            System.out.println("domain 5+:" + fourthX + "," + fourthY);
            System.out.println(domain[fourthX][fourthY].toString());
            System.out.println("domain 5+:" + fifthX + "," + fifthY);
            System.out.println(domain[fifthX][fifthY].toString());
            for (int x = 0; x < size; x++) {
                if (checklist1[x] == 0) {
                    if(domain[fisrtX][fisrtY].contains(new Integer(x+1))){
                        change = true;
                    }
                    domain[fisrtX][fisrtY].remove(new Integer(x + 1));
                }
                if (checklist2[x] == 0) {
                    if(domain[secondX][secondY].contains(new Integer(x+1))){
                        change = true;
                    }
                    domain[secondX][secondY].remove(new Integer(x + 1));
                }
                if (checklist3[x] == 0) {
                    if(domain[thirdX][thirdY].contains(new Integer(x+1))){
                        change = true;
                    }
                    domain[thirdX][thirdY].remove(new Integer(x + 1));
                }
                if (checklist4[x] == 0) {
                    if(domain[fourthX][fourthY].contains(new Integer(x+1))){
                        change = true;
                    }
                    domain[fourthX][fourthY].remove(new Integer(x + 1));
                }
                if (checklist5[x] == 0) {
                    if(domain[fifthX][fifthY].contains(new Integer(x+1))){
                        change = true;
                    }
                    domain[fifthX][fifthY].remove(new Integer(x + 1));
                }
            }
            System.out.println("domain 5+:" + fisrtX + "," + fisrtY);
            System.out.println(domain[fisrtX][fisrtY].toString());
            System.out.println("domain 5+:" + secondX + "," + secondY);
            System.out.println(domain[secondX][secondY].toString());
            System.out.println("domain 5+:" + thirdX + "," + thirdY);
            System.out.println(domain[thirdX][thirdY].toString());
            System.out.println("domain 5+:" + fourthX + "," + fourthY);
            System.out.println(domain[fourthX][fourthY].toString());
            System.out.println("domain 5+:" + fifthX + "," + fifthY);
            System.out.println(domain[fifthX][fifthY].toString());
        }

    }

    public boolean checkdomain(int x, int y, int check) {
        for (int countX = 0; countX < domain[x][y].size(); countX++) {
            if (check == domain[x][y].get(countX)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkDomainPossibly(int testDomain[][]) {

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (testDomain[x][y] > 0) {
                    int checkNumber = testDomain[x][y];
                    //check not equal
                    for (int checkX = 0; checkX < size; checkX++) {
                        if (testDomain[checkX][y] == checkNumber && checkX != x)
                            return false;
                    }
                    for (int checkY = 0; checkY < size; checkY++) {
                        if (testDomain[x][checkY] == checkNumber && checkY != y)
                            return false;
                    }
                }
            }
        }

        for (int checkBox = 0; checkBox < objectList.length - 1; checkBox++) {
            if (objectList[checkBox].getOpertor() != '=') {
                boolean test = true;
                for (int count = 0; count < objectList[checkBox].getCubeList().size() / 2; count++) {
                    int cube[] = objectList[checkBox].indexOfCube(count);
                    if (testDomain[cube[0]][cube[1]] < 1)
                        test = false;
                }
                if (test) {
                    if (objectList[checkBox].getOpertor() == '+') {
                        int sum = 0;
                        for (int count = 0; count < objectList[checkBox].getCubeList().size() / 2; count++) {
                            int cube[] = objectList[checkBox].indexOfCube(count);
                            sum += testDomain[cube[0]][cube[1]];
                        }
                        if (sum != objectList[checkBox].getGoal()) {
                            return false;
                        }
                    } else if (objectList[checkBox].getOpertor() == 'x'||objectList[checkBox].getOpertor() == 'X') {
                        int sum = 1;
                        for (int count = 0; count < objectList[checkBox].getCubeList().size() / 2; count++) {
                            int cube[] = objectList[checkBox].indexOfCube(count);
                            sum *= testDomain[cube[0]][cube[1]];
                        }
                        if (sum != objectList[checkBox].getGoal()) {
                            return false;
                        }
                    } else if (objectList[checkBox].getOpertor() == '-') {
                        int cube[] = objectList[checkBox].indexOfCube(0);
                        int first = testDomain[cube[0]][cube[1]];
                        cube = objectList[checkBox].indexOfCube(1);
                        int second = testDomain[cube[0]][cube[1]];

                        if (second > first) {
                            int store = first;
                            first = second;
                            second = store;
                        }
                        if (first - second != objectList[checkBox].getGoal()) {
                            return false;
                        }
                    } else if (objectList[checkBox].getOpertor() == '/') {
                        int cube[] = objectList[checkBox].indexOfCube(0);
                        int first = testDomain[cube[0]][cube[1]];
                        cube = objectList[checkBox].indexOfCube(1);
                        int second = testDomain[cube[0]][cube[1]];

                        if (second > first) {
                            int store = first;
                            first = second;
                            second = store;
                        }
//                        System.out.println(objectList[checkBox].getGoal());
//                        System.out.println(first / second);
//                        System.out.println(first % second);
                        if (first / second != objectList[checkBox].getGoal() || first % second > 0) {
                            return false;
                        }
                    }
                }

            } else {
                int cube[] = objectList[checkBox].indexOfCube(0);
                if (testDomain[cube[0]][cube[1]] != objectList[checkBox].getGoal() && testDomain[cube[0]][cube[1]] > 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public void checkSingleDomain() {
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (domain[x][y].size() == 1) {
                    assignments[x][y] = domain[x][y].get(0);

                }
            }
        }
    }

    public void initSearch() {
        searchAssignmentsStore = new int[size][size];
        int[][] testAssignments = new int[size][size];
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                testAssignments[row][col] = 0;
            }
        }
        search(testAssignments, 0);
        printAllSearchAssainment();
    }

    public void search(int testAssainment[][], int cubeLocate) {
        if (cubeLocate == size * size) {
            if (checkDomainPossibly(testAssainment)) {
                for (int row = 0; row < size; row++) {
                    for (int col = 0; col < size; col++) {
                        searchAssignmentsStore[row][col] = testAssainment[row][col];
                    }
                }
            }
            return;
        }

        for (int x = 0; x < domain[cubeLocate / size][cubeLocate % size].size(); x++) {
            testAssainment[cubeLocate / size][cubeLocate % size] = domain[cubeLocate / size][cubeLocate % size].get(x);
            if (checkDomainPossibly(testAssainment)) {
                search(testAssainment, cubeLocate + 1);
                testAssainment[cubeLocate / size][cubeLocate % size] = 0;
            } else {
                testAssainment[cubeLocate / size][cubeLocate % size] = 0;
            }
        }
    }

    public void printAllSearchAssainment() {
        for (int countx = 0; countx < size; countx++) {
            for (int county = 0; county < size; county++) {
                System.out.print(searchAssignmentsStore[countx][county] + " ");
                assignments[countx][county]=searchAssignmentsStore[countx][county];
            }
            System.out.println();
        }

    }

    public KenKenPuzzle(int rs, int cs) {
        rows = rs;
        cols = cs;
        size = rows;
        initAssignment();
    }

    public void initAssignment() {
        assignments = new int[size][size];
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                assignments[row][col] = 0;
            }
        }
    }

    public boolean generateMove(int r, int c, int input) {
        int testAssignments[][] = new int[size][size];
        for (int x = 0; x < size; x++)
            for (int y = 0; y < size; y++)
                testAssignments[x][y] = assignments[x][y];

        testAssignments[r][c] = input;

        if (checkDomainPossibly(testAssignments)) {
            System.out.println(r + "  " + c);
            assignments[r][c] = input;

            boolean win=true;
            for (int row = 0; row < size; row++)
                for (int col = 0; col < size; col++)
                   if(assignments[row][col]<=0)
                       win=false;

            if(win)
                return true;
            else
                return false;

        } else {
            return false;
        }
    }


    public int getAssignment(int r, int c) {
        return assignments[r][c];
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public int[][] getAssignments() {
        return assignments;
    }

    public void setAssignments(int[][] assignments) {
        this.assignments = assignments;
    }

    public String domainsToString() {
        String stDomains = "";
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                stDomains += "The domain for box " + x + " " + y + " Contains: " +domain[x][y].toString() + "\n";
            }
        }
        return stDomains;
    }
    public int getSize() {
        return size;
    }

}
