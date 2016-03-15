import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by zachw on 2/25/16.
 */
public class KenKenPuzzle {
    private int size;
    private int rows = 4;
    private int cols = 4;

    public void setObjectList(BoxObject[] objectList) {
        this.objectList = objectList;
    }

    private BoxObject objectList[];
    private List<Integer> domain[][];
    private List<Integer> constraint[][][];
    private int[][] assignments = {
            {-1, -1, -1, -1, -1, 4, -1, -1},
            {-1, -1, -1, -1, -1, -1, -1, -1},
            {-1, 4, -1, -1, -1, -1, -1, -1},
            {-1, -1, -1, -1, -1, 9, -1, -1},
            {-1, -1, 8, -1, -1, -1, -1, -1},
            {-1, -1, -1, -1, -1, -1, -1, -1},
            {6, -1, -1, -1, -1, -1, -1, -1},
            {-1, -1, -1, -1, -1, -1, -1, -1}};

    Random randGen;


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
                System.out.println(constraint[x][y][0].toString());
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
                            //System.out.println("from box: " + compBoxX + compBoxY + " and compbox domain size" + domain[compBoxX][compBoxY].toString());
                        }
                    }
                }

            }
        }
    }

    public void operatorArchConisitency() {
        for (int objectCounter = 0; objectCounter < objectList.length-1; objectCounter++) {
            if (objectList[objectCounter].getOpertor() == '+') {

            } else if (objectList[objectCounter].getOpertor() == '-') {
                archConisitencySubtract(objectCounter);
            } else if (objectList[objectCounter].getOpertor() == '/') {
                archConisitencyDivide(objectCounter);
            } else if (objectList[objectCounter].getOpertor() == 'x' || objectList[objectCounter].getOpertor() == 'X') {

            }
        }
    }

private void archConisitencyDivide(int counter) {

        int fisrtX = objectList[counter].cubeList.get(0);
        int fisrtY = objectList[counter].cubeList.get(1);
        int secondX = objectList[counter].cubeList.get(2);
        int secondY = objectList[counter].cubeList.get(3);
        int goal=objectList[counter].getGoal();
        if (domain[fisrtX][fisrtY].size() == 1) {
            int vault=domain[fisrtX][fisrtY].get(0);
            int checklistY[] = new int[size];
            for (int x = 0; x < size; x++) {
                checklistY[x] = 0;
            }
            if(goal*vault<=size&&goal*vault>0)
            {
                checklistY[goal+vault-1]=1;
            }
            if(vault/goal<=size&&vault/goal>0)
            {
                checklistY[vault-goal-1]=1;
            }
            for (int x = 0; x < size; x++) {
                if(checklistY[x]==0)
                {
                    domain[secondX][secondY].remove(new Integer(x+1));
                }
            }

        } else if (domain[secondX][secondY].size() == 1) {
            int vault=domain[secondX][secondY].get(0);
            int checklistX[] = new int[size];
            for (int x = 0; x < size; x++) {
                checklistX[x] = 0;
            }
            if(goal*vault<=size&&goal*vault>0)
            {
                checklistX[goal+vault-1]=1;
            }
            if(vault/goal<=size&&vault/goal>0)
            {
                checklistX[vault-goal-1]=1;
            }
            for (int x = 0; x < size; x++) {
                if(checklistX[x]==0)
                {
                    domain[fisrtX][fisrtY].remove(new Integer(x+1));
                }
            }
        } else {
            int checklistX[] = new int[size];
            int checklistY[] = new int[size];
            for (int x = 0; x < size; x++) {
                checklistX[x] = 0;
                checklistY[x] = 0;
            }
            for (int x = 1; x<= size; x++) {
                for (int y = 1; y <= size; y++)
                {
                    if(goal*x==y)
                    {
                        if(checkdomain(fisrtX,fisrtY,x)&&checkdomain(secondX,secondY,y))
                        {
                            checklistX[x-1]=1;
                            checklistY[y-1]=1;
                        }

                        if(checkdomain(secondX,secondY,x)&&checkdomain(fisrtX,fisrtY,y))
                        {
                            checklistX[y-1]=1;
                            checklistY[x-1]=1;
                        }



                    }
                }
            }

            System.out.println("domain:"+fisrtX+","+fisrtY);
            System.out.println(domain[fisrtX][fisrtY].toString());
            System.out.println("domain:"+secondX+","+secondY);
            System.out.println(domain[secondX][secondY].toString());
            for (int x = 0; x < size; x++) {
                if(checklistX[x]==0)
                {
                    domain[fisrtX][fisrtY].remove(new Integer(x+1));
                }
                if(checklistY[x]==0)
                {
                    domain[secondX][secondY].remove(new Integer(x+1));
                }

            }
            System.out.println("domain:"+fisrtX+","+fisrtY);
            System.out.println(domain[fisrtX][fisrtY].toString());
            System.out.println("domain:"+secondX+","+secondY);
            System.out.println(domain[secondX][secondY].toString());
        }
    }


    public void archConisitencySubtract(int counter) {
        int fisrtX = objectList[counter].cubeList.get(0);
        int fisrtY = objectList[counter].cubeList.get(1);
        int secondX = objectList[counter].cubeList.get(2);
        int secondY = objectList[counter].cubeList.get(3);
        int goal=objectList[counter].getGoal();
        if (domain[fisrtX][fisrtY].size() == 1) {
            int vault=domain[fisrtX][fisrtY].get(0);
            int checklistY[] = new int[size];
            for (int x = 0; x < size; x++) {
                checklistY[x] = 0;
            }
            if(goal+vault<=size&&goal+vault>0)
            {
                checklistY[goal+vault-1]=1;
            }
            if(vault-goal<=size&&vault-goal>0)
            {
                checklistY[vault-goal-1]=1;
            }
            for (int x = 0; x < size; x++) {
                if(checklistY[x]==0)
                {
                    domain[secondX][secondY].remove(new Integer(x+1));
                }
            }

        } else if (domain[secondX][secondY].size() == 1) {
            int vault=domain[secondX][secondY].get(0);
            int checklistX[] = new int[size];
            for (int x = 0; x < size; x++) {
                checklistX[x] = 0;
            }
            if(goal+vault<=size&&goal+vault>0)
            {
                checklistX[goal+vault-1]=1;
            }
            if(vault-goal<=size&&vault-goal>0)
            {
                checklistX[vault-goal-1]=1;
            }
            for (int x = 0; x < size; x++) {
                if(checklistX[x]==0)
                {
                    domain[fisrtX][fisrtY].remove(new Integer(x+1));
                }
            }
        } else {
            int checklistX[] = new int[size];
            int checklistY[] = new int[size];
            for (int x = 0; x < size; x++) {
                checklistX[x] = 0;
                checklistY[x] = 0;
            }
            for (int x = 1; x<= size; x++) {
                for (int y = 1; y <= size; y++)
                {
                    if(goal+x==y)
                    {
                        if(checkdomain(fisrtX,fisrtY,x)&&checkdomain(secondX,secondY,y))
                        {
                            checklistX[x-1]=1;
                            checklistY[y-1]=1;
                        }

                        if(checkdomain(secondX,secondY,x)&&checkdomain(fisrtX,fisrtY,y))
                        {
                            checklistX[y-1]=1;
                            checklistY[x-1]=1;
                        }



                    }
                }
            }

            System.out.println("domain:"+fisrtX+","+fisrtY);
            System.out.println(domain[fisrtX][fisrtY].toString());
            System.out.println("domain:"+secondX+","+secondY);
            System.out.println(domain[secondX][secondY].toString());
            for (int x = 0; x < size; x++) {
                if(checklistX[x]==0)
                {
                    domain[fisrtX][fisrtY].remove(new Integer(x+1));
                }
                if(checklistY[x]==0)
                {
                    domain[secondX][secondY].remove(new Integer(x+1));
                }

            }
            System.out.println("domain:"+fisrtX+","+fisrtY);
            System.out.println(domain[fisrtX][fisrtY].toString());
            System.out.println("domain:"+secondX+","+secondY);
            System.out.println(domain[secondX][secondY].toString());
        }
    }
    public boolean checkdomain(int x, int y, int check)
    {
        for(int countX=0;countX<domain[x][y].size();countX++)
        {
            if(check==domain[x][y].get(countX))
            {
                return true;
            }
        }
        return false;
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

    public void generateMove(int r, int c, int input) {

        System.out.println(r + "  " + c);
        assignments[r][c] = input;
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
}
