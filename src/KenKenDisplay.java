import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * KenKen Puzzle
 * Created by Zach Widger and Wei Zhou
 * For CSIT 441 Artificial Intelligence
 * Finished on 3/28/2016
 */

public class KenKenDisplay extends JPanel {

    KenKenPuzzle puzzle;
    private BoxObject objectList[];
    int cellSize = 75;
    int divWid = 7;
    int thinkWid = 3;
    int start_X = 50;
    int start_y = 50;
    int letterOffSet_Y = 20;
    int letterOffSet_X = 10;
    int letterOffSetAnswer_Y = 70;
    int letterOffSetAnswer_X = 50;
    boolean freezeboard = false;

    boolean lunchPuzzle = false;
    Font bigFont = new Font("Arial", 1, 25);
    Font answerFont = new Font("Arial", 1, 40);
    Color boardColor = Color.black;

    public void loadBoxObjects(BoxObject[] boxObjects) {
        objectList = boxObjects;
        lunchPuzzle = true;
        boardColor = Color.black;
        repaint();
    }

    public void printObjectOutline(Graphics g) {
        g.setColor(Color.black);
        for (int countObject = 0; countObject < objectList.length - 1; countObject++) {
            //print operator and goal for each block

            int firstcube[] = objectList[countObject].indexOfCube(0);
            char getOpertor = objectList[countObject].getOpertor();
            if (getOpertor == '/') {
                getOpertor = '÷';
            }
            if (getOpertor == '=') {
                getOpertor = ' ';
            }
            String in = "" + objectList[countObject].getGoal() + getOpertor;
            g.setFont(bigFont);
            g.drawString(in, start_X +
                            divWid + (cellSize + divWid) * firstcube[1] + letterOffSet_X,
                    start_y + divWid + (cellSize + divWid) * firstcube[0] + letterOffSet_Y);


            //printBoard
            for (int x = 0; x < objectList[countObject].getCubeList().size() / 2; x++) {
                int location[] = objectList[countObject].indexOfCube(x);
                if (location[0] - 1 >= 0) {
                    int testInlist[] = {location[0] - 1, location[1]};
                    boolean needPrintBorader = true;

                    for (int y = 0; y < objectList[countObject].getCubeList().size() / 2; y++) {
                        int cubelist[] = objectList[countObject].indexOfCube(y);
                        if (testInlist[1] == cubelist[1] && testInlist[0] == cubelist[0]) {
                            needPrintBorader = false;
                        }
                    }
                    if (needPrintBorader == true) {
                        int[] board = getBetweenBoarder(testInlist, location);
                        g.fillRect(board[0], board[1], board[2], board[3]);
                    }
                }
                if (location[1] - 1 >= 0) {
                    int testInlist[] = {location[0], location[1] - 1};
                    boolean needPrintBorader = true;

                    for (int y = 0; y < objectList[countObject].getCubeList().size() / 2; y++) {
                        int cubelist[] = objectList[countObject].indexOfCube(y);
                        if (testInlist[1] == cubelist[1] && testInlist[0] == cubelist[0]) {
                            needPrintBorader = false;
                        }
                    }

                    if (needPrintBorader == true) {
                        int[] board = getBetweenBoarder(testInlist, location);
                        g.fillRect(board[0], board[1], board[2], board[3]);
                    }
                }

            }
        }

        int[][] assignments = puzzle.getAssignments();
        g.setColor(boardColor);
        g.setFont(answerFont);
        for (int x = 0; x < puzzle.getCols(); x++) {
            for (int y = 0; y < puzzle.getCols(); y++) {


                if (assignments[x][y] > 0) {
                    int z[] = printCubeSolver(x, y);
                    g.drawString("" + assignments[x][y], z[0], z[1]);
                }
            }
        }

        lunchPuzzle = true;
        repaint();
    }

    public int[] getBetweenBoarder(int loc1[], int loc2[]) {
        int[] returnValue = new int[4];
        if (loc1[1] == loc2[1]) {
            if (loc1[0] > loc2[0]) {
                returnValue[0] = start_X + (loc1[1]) * (divWid + cellSize);
                returnValue[1] = start_y + (loc1[0]) * (divWid + cellSize);
                returnValue[2] = cellSize + divWid * 2;
                returnValue[3] = divWid;
            }
            if (loc1[0] < loc2[0]) {
                returnValue[0] = start_X + ((loc2[1]) * (divWid + cellSize));
                returnValue[1] = start_y + ((loc2[0]) * (divWid + cellSize));
                returnValue[2] = cellSize + divWid * 2;
                returnValue[3] = divWid;
            }
        }
        if (loc1[0] == loc2[0]) {
            if (loc1[1] > loc2[1]) {
                returnValue[0] = start_X + (loc2[1] + 1) * (divWid + cellSize);
                returnValue[1] = start_y + (loc2[0]) * (divWid + cellSize);
                returnValue[2] = divWid;
                returnValue[3] = cellSize + divWid * 2;
            }
            if (loc1[1] < loc2[1]) {
                returnValue[0] = start_X + ((loc1[1] + 1) * (divWid + cellSize));
                returnValue[1] = start_y + ((loc1[0]) * (divWid + cellSize));
                returnValue[2] = divWid;
                returnValue[3] = cellSize + divWid * 2;
            }
        }

        return returnValue;
    }

    public int[] printCubeSolver(int row, int col) {
        int[] returnValue = new int[2];

        returnValue[0] = start_X + col * (divWid + cellSize) + letterOffSetAnswer_X;
        returnValue[1] = start_y + row * (divWid + cellSize) + letterOffSetAnswer_Y;


        return returnValue;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.black);
        g.fillRect(start_X, start_y, (cellSize + divWid) * puzzle.getCols() + divWid, (cellSize + divWid) * puzzle.getRows() + divWid);
        g.setColor(Color.white);
        g.fillRect(start_X + divWid, start_y + divWid, (cellSize + divWid) * puzzle.getCols() - divWid, (cellSize + divWid) * puzzle.getRows() - divWid);

        g.setColor(Color.gray);

        for (int size = 1; size < puzzle.getCols(); size++) {

            g.fillRect(start_X + divWid, start_y + (divWid + cellSize) * size, (cellSize + divWid) * puzzle.getCols() - divWid, thinkWid);
            g.fillRect(start_y + (divWid + cellSize) * size, start_y + divWid, thinkWid, (cellSize + divWid) * puzzle.getCols() - divWid);
        }

        if (lunchPuzzle) {
            printObjectOutline(g);
        }
    }

    public KenKenDisplay(KenKenPuzzle p) {
        puzzle = p;

        this.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                processClick(me);
            }
        });

    }

    public void processClick(MouseEvent me) {
        if (!lunchPuzzle || freezeboard) {
            return;
        }
        int x = me.getX();
        int y = me.getY();

        if ((y - start_y - divWid) > 0 && (x - start_X - divWid) > 0) {

            int selectedRow = (y - start_y - divWid) / (cellSize + divWid);
            int selectedCol = (x - start_X - divWid) / (cellSize + divWid);

            if (selectedRow < puzzle.getRows() && selectedCol < puzzle.getCols()) {
                String input = JOptionPane.showInputDialog(null, "Please enter the number you want to fill in (" + (selectedRow + 1) + " , " + (selectedCol + 1) + ")\n" +
                        "In the range 1-" + puzzle.getCols() + ".\nenter 0 to clear the field.", "Input Dialog", 1);
                int inputInteger = 0;

                try {
                    inputInteger = Integer.parseInt(input);
                } catch (java.lang.NumberFormatException e) {
                    if (input != null)
                        JOptionPane.showMessageDialog(null, "You have entered an invalid input", "Wrong Input!", 2);
                    return;
                }
                if (inputInteger >= 0 && inputInteger <= puzzle.getCols()) {

                    int win = puzzle.generateMove(selectedRow, selectedCol, inputInteger);
                    if (win==3) {
                        win();
                        JOptionPane.showMessageDialog(null,"You have finished the game!", "Win!!",1);
                    } else if(win==1) {
                        JOptionPane.showMessageDialog(null, "Logic Error", "Error!!", 0);
                    }
                    repaint();
                } else {
                    JOptionPane.showMessageDialog(null, "You have entered an out of range input", "Wrong Input!", 2);
                }
            }
        }
    }

    public void win() {
        boardColor = Color.green;
        freezeboard = true;

    }

    public boolean checkwin() {
        boolean win = true;
        for (int row = 0; row < puzzle.getSize(); row++) {
            for (int col = 0; col < puzzle.getSize(); col++) {
                if (puzzle.getAssignment(row, col) <= 0)
                    win = false;
            }
        }

        if (win) {
            win();
            return true;
        } else
            return false;
    }
}
