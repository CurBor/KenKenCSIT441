import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * KenKen Puzzle
 * Created by Zach Widger and Wei Zhou
 * For CSIT 441 Artificial Intelligence
 * Finished on 3/28/2016
 */

public class ReadFile {

    public int getSize() {
        return size;
    }

    private int size;

    public BoxObject[] getList() {
        return list;
    }

    private BoxObject list[];

    public ReadFile(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        size=scanner.nextInt();
        int numberOfProblem=0;
        while(scanner.hasNextLine())
        {
            numberOfProblem++;
            scanner.nextLine();
        }
        scanner.close();

        list=new BoxObject[numberOfProblem];

        scanner=new Scanner(file);
        scanner.nextLine();
        int count=0;
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] splitLine = line.split(":");
            char opertar = splitLine[1].charAt(splitLine[1].length()-1);
            String x=splitLine[1].substring(0, splitLine[1].length() -1);
            int goal = Integer.parseInt(x);
            BoxObject questions = new BoxObject(goal, opertar);

            Scanner readLine = new Scanner(splitLine[0]);
            while (readLine.hasNextInt()) {
                int row = readLine.nextInt();
                int col = readLine.nextInt();
                questions.addCube(row, col);
            }

            list[count] = questions;
            count++;
        }
    }
}
