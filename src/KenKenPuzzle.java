import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by zachw on 2/25/16.
 */
public class KenKenPuzzle {
    private int rows = 8;
    private int cols= 8;
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

    public void SetDomain(int size){

        List<Integer> f[][]=new List[size][size];


        for(int x=0;x<3;x++)
        {
            for(int y=0;y<3;y++)
            {
                f[x][y]=new ArrayList();
            }
        }

        for(int x=0;x<size;x++)
        {
            for(int y=0;y<size;y++)
            {
                for(int z=1;z<=size;z++)
                    f[x][y].add(z);
            }
        }

//        System.out.println(f[1][2]);
//
//        for(int x=0;x<3;x++)
//        {
//            for(int y=0;y<3;y++)
//            {
//
//                System.out.println(f[x][y].toString());
//            }
//        }

    }

    public void SetConstraint()

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
