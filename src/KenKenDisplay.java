import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class KenKenDisplay extends JPanel{

    KenKenPuzzle puzzle;
    int cellSize = 125;
    int divWid = 6;
    int thinkWid=3;
    int start_X = 50;
    int start_y = 50;
    int letterOffSet_Y = 40;
    int letterOffSet_X = 10;

    Color[] colors = {Color.white, Color.white};

    Font bigFont = new Font("Arial", 1, 40);


    public KenKenDisplay(KenKenPuzzle p)
    {
        puzzle = p;

        this.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent me)
            {
                processClick(me);
            }
        });

    }

    public void processClick(MouseEvent me)
    {
        int x = me.getX();
        int y = me.getY();

        int selectedRow = (y - start_y-divWid)/(cellSize+divWid);
        int selectedCol = (x - start_X-divWid)/(cellSize+divWid);


        puzzle.generateMove(selectedRow, selectedCol);
        repaint();
    }


    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.setColor(Color.black);
        g.fillRect(start_X, start_y, (cellSize+divWid)*puzzle.getCols()+divWid , (cellSize+divWid)*puzzle.getRows()+divWid);
        g.setColor(Color.white);
        g.fillRect(start_X+divWid, start_y+divWid, (cellSize+divWid)*puzzle.getCols()-divWid , (cellSize+divWid)*puzzle.getRows()-divWid);

        g.setColor(Color.gray);

        for(int size=1; size<puzzle.getCols();size++)
        {

            g.fillRect(start_X+divWid,start_y+(divWid+cellSize)*size,(cellSize+divWid)*puzzle.getCols()-divWid, thinkWid );
            g.fillRect(start_X+(divWid+cellSize)*size,start_y+divWid,thinkWid, (cellSize+divWid)*puzzle.getCols()-divWid);
        }


//        for(int row =0; row < puzzle.getRows(); row++)
//        {
//            for(int col = 0; col < puzzle.getCols(); col++)
//            {
//                int colorNum = (row+col)%2;
//                g.setColor(colors[colorNum]);
//                g.fillRect(start_X+divWid+(cellSize+divWid)*col, start_y+divWid+
//                        (cellSize+divWid)*row, cellSize ,cellSize);
//
//                if( puzzle.getAssignment(row, col) > -1)
//                {
//                    g.setColor(Color.black);
//                    g.setFont(bigFont);
//                    g.drawString(""+puzzle.getAssignment(row, col), start_X+
//                                    divWid+(cellSize+divWid)*col+letterOffSet_X,
//                            start_y+divWid+(cellSize+divWid)*row+letterOffSet_Y);
//
//                }
//
//
////            }
//        }
    }

}
