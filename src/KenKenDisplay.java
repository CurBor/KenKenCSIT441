import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class KenKenDisplay extends JPanel {

    KenKenPuzzle puzzle;
    private BoxObject objectList[];
    int cellSize = 125;
    int divWid = 8;
    int thinkWid = 3;
    int start_X = 50;
    int start_y = 50;
    int letterOffSet_Y = 30;
    int letterOffSet_X = 10;
    boolean lunchPuzzle = false;


    Color[] colors = {Color.white, Color.white};

    Font bigFont = new Font("Arial", 1, 25);

    public void loadBoxObjects(BoxObject[] boxObjects) {
        objectList = boxObjects;
        lunchPuzzle = true;
        repaint();
    }


    public void printObjectOutline(Graphics g) {
        g.setColor(Color.black);
        for (int countObject = 0; countObject < objectList.length - 1; countObject++) {
            //print operator and goal for each block

            int firstcube[]= objectList[countObject].indexOfCube(0);
//            returnValue[0] = start_y + ((loc2[1]) * (divWid + cellSize));
//            returnValue[1] = start_X + ((loc2[0]) * (divWid + cellSize));
            char getOpertor=objectList[countObject].getOpertor();
            if(getOpertor=='/')
            {
                getOpertor='÷';
            }
            String in=""+objectList[countObject].getGoal()+getOpertor;
            g.setFont(bigFont);
            g.drawString(in, start_X+
                            divWid+(cellSize+divWid)*firstcube[1]+letterOffSet_X,
                    start_y+divWid+(cellSize+divWid)*firstcube[0]+letterOffSet_Y);


            //printBoard
            for (int x = 0; x < objectList[countObject].getCubeList().size() / 2; x++) {
                int location[] = objectList[countObject].indexOfCube(x);
                if (location[0] - 1 >= 0) {
                    int testInlist[] = {location[0] - 1, location[1]};
                    boolean needPrintBorader = true;

                    for (int y = 0; y < objectList[countObject].getCubeList().size() / 2; y++) {
                        int cubelist[]= objectList[countObject].indexOfCube(y);
                        if (testInlist[1]==cubelist[1]&& testInlist[0]==cubelist[0]) {
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
                        int cubelist[]= objectList[countObject].indexOfCube(y);
                        if (testInlist[1]==cubelist[1]&& testInlist[0]==cubelist[0]) {
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
        lunchPuzzle = true;
        repaint();
    }

    public int[] getBetweenBoarder(int loc1[], int loc2[]) {
        int[] returnValue = new int[4];
        if (loc1[1] == loc2[1]) {
            if (loc1[0] > loc2[0]) {
                returnValue[0] = start_X + (loc1[1]) * (divWid + cellSize) ;
                returnValue[1] = start_y + (loc1[0]) * (divWid + cellSize) ;
                returnValue[2] = cellSize + divWid;
                returnValue[3] = divWid;
            }
            if (loc1[0] < loc2[0]) {
                returnValue[0] = start_X + ((loc2[1]) * (divWid + cellSize));
                returnValue[1] = start_y + ((loc2[0]) * (divWid + cellSize));
                returnValue[2] = cellSize + divWid;
                returnValue[3] = divWid;
            }
        }
        if (loc1[0] == loc2[0]) {
            if (loc1[1] > loc2[1]) {
                returnValue[0] = start_X + (loc2[1] + 1) * (divWid + cellSize);
                returnValue[1] = start_y + (loc2[0]) * (divWid + cellSize);
                returnValue[2] = divWid;
                returnValue[3] = cellSize + divWid;
            }
            if (loc1[1] < loc2[1]) {
                returnValue[0] = start_X + ((loc1[1] + 1) * (divWid + cellSize));
                returnValue[1] = start_y + ((loc1[0]) * (divWid + cellSize));
                returnValue[2] = divWid;
                returnValue[3] = cellSize + divWid;
            }
        }
        int f = 0;


        return returnValue;
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
        int x = me.getX();
        int y = me.getY();

        int selectedRow = (y - start_y - divWid) / (cellSize + divWid);
        int selectedCol = (x - start_X - divWid) / (cellSize + divWid);


        puzzle.generateMove(selectedRow, selectedCol);
        repaint();
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

}
