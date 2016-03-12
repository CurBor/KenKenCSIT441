import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wei Zhou on 2016/3/11.
 */
public class Questions {


    int goal;
    char opertor;
    List<Integer> cubeList;

    public  Questions(int goal, char opertor)
    {
        this.goal=goal;
        this.opertor=opertor;
        cubeList=new ArrayList<Integer>();
    }

    public void addCube (int row,int col)
    {
        cubeList.add(row);
        cubeList.add(col);
    }

    public int getGoal() {
        return goal;
    }

    public int[] indexOfCube(int input)
    {
    	int row=cubeList.get(input*2);
    	int col=cubeList.get(input*2+1);
    	
    	int[] xAndY={row,col};
    	
    	
		return xAndY;
    	
    }
    
    
    public List getCubeList() {
        return cubeList;
    }
    public String getCubeStringList(){
    	int counter=0;
    	String value="";
    	
    	while(counter<cubeList.size())
    	{
    		value+=cubeList.get(counter)+" ";
    		if(counter%2==1)
    		{
    			value+="||";
    		}
    		counter++;
    	}
    	
		return value;
    	
    }
    
    
    public char getOpertor() {
        return opertor;
    }

}
