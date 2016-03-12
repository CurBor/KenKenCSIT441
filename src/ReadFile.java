import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Wei Zhou on 2016/3/11.
 */
public class ReadFile {

    private int size;
    private int numberOfProblem;

    public ReadFile(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        size=scanner.nextInt();
        numberOfProblem=0;
        while(scanner.hasNextLine())
        {
            numberOfProblem++;
            scanner.nextLine();
        }
        scanner.close();

        Questions list[]=new Questions[numberOfProblem];

        scanner=new Scanner(file);
        scanner.nextLine();
        int count=0;
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] splitLine = line.split(":");
            char opertar = splitLine[1].charAt(splitLine[1].length()-1);
            String x=splitLine[1].substring(0, splitLine[1].length() -1);
            int goal = Integer.parseInt(x);
            Questions questions = new Questions(goal, opertar);

            Scanner readLine = new Scanner(splitLine[0]);
            while (readLine.hasNextInt()) {
                int row = readLine.nextInt();
                int col = readLine.nextInt();
                questions.addCube(row, col);
            }

            list[count] = questions;
            count++;
        }
        int x=2;
        System.out.println(list[x].getGoal());
        System.out.println(list[x].getOpertor());
        
        System.out.println(list[x].getCubeStringList());
    }


}
