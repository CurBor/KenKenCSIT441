import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by zachw on 2/25/16.
 */
public class KenKenPuzzle {
    private int size;
    private int rows = 8;
    private int cols= 8;

    public void setObjectList(BoxObject[] objectList) {
        this.objectList = objectList;
    }

    private BoxObject objectList[];
    private List<Integer> domain[][];
    private List<Integer> constraint[][][];
    private int[][] assignments = {
            {-1,-1,-1,-1,-1,4,-1,-1},
            {-1,-1,-1,-1,-1,-1,-1,-1},
            {-1,4,-1,-1,-1,-1,-1,-1},
            {-1,-1,-1,-1,-1,9,-1,-1},
            {-1,-1,8,-1,-1,-1,-1,-1},
            {-1,-1,-1,-1,-1,-1,-1,-1},
            {6,-1,-1,-1,-1,-1,-1,-1},
            {-1,-1,-1,-1,-1,-1,-1,-1}};

    Random randGen;


    public BoxObject[] getObjectList() {
        return objectList;
    }
    public void SetDomain(int size){
        this.size=size;
        domain=new List[size][size];


        for(int x=0;x<size;x++)
        {
            for(int y=0;y<size;y++)
            {
                domain[x][y]=new ArrayList();
            }
        }

        for(int x=0;x<size;x++)
        {
            for(int y=0;y<size;y++)
            {
                for(int z=1;z<=size;z++)
                    domain[x][y].add(z);
            }
        }
        SetConstraints();

    }

    public void loadBoxObjects(BoxObject[] list){
        this.objectList=list;
    }

    public void SetConstraints(){
        constraint = new List[size][size][2];//0 for cubes that not equal, 1 for relationship with List of Box Object
        for(int x=0;x<size;x++)
        {
            for(int y=0;y<size;y++)
            {
                constraint[x][y][0]=setNotEqual(x,y);
                System.out.println(constraint[x][y][0].toString());
                constraint[x][y][1]=setRelationshipConstaint(x,y);
                System.out.println(constraint[x][y][1].toString());
            }
        }


    }


    public List<Integer> setRelationshipConstaint(int row,int col)
    {
        List<Integer> returnList=new ArrayList();


        for(int x=0;x< objectList.length-1;x++)
        {
            for(int y=0;y<objectList[x].getCubeList().size()/2;y++)
            {
                int[]list=objectList[x].indexOfCube(y);
                if(row==list[0]&& col==list[1])
                {
                    returnList.add(x);
                    return returnList;
                }
            }
        }

        return null;
    }
    public List<Integer> setNotEqual (int row,int col)
    {
        List<Integer> returnList=new ArrayList();
        for(int x=0;x<size;x++)
        {
            if(x!=row)
            {
                returnList.add(x);
                returnList.add(col);
            }

        }
        for(int x=0;x<size;x++)
        {
            if(x!=col) {
                returnList.add(row);
                returnList.add(x);
            }
        }

        return returnList;
    }

    public void nodeConsistency(){
        System.out.println("test");
    }


    public KenKenPuzzle(int rs, int cs)
    {
        randGen = new Random();
        rows = rs;
        cols = cs;
        initAssignment();
    }

    public void initAssignment()
    {
        assignments = new int[rows][cols];
        for(int row = 0; row < getRows(); row++)
        {
            for(int col = 0; col < getCols(); col++)
            {
                int prob = randGen.nextInt(100);
                if(prob > 75)
                {
                    assignments[row][col] = randGen.nextInt(8)+1;
                }
                else
                {
                    assignments[row][col] = -1;
                }
            }
        }
    }

    public void generateMove(int r, int c)
    {
        int assign = randGen.nextInt(9)+1;
        assignments[r][c] = assign;
//                System.out.println(domain[1][2]);
//
//        for(int x=0;x<3;x++)
//        {
//            for(int y=0;y<3;y++)
//            {
//
//                System.out.println(domain[x][y].toString());
//            }
//        }
    }


    public int getAssignment(int r, int c)
    {
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
