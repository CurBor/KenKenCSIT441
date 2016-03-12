import javax.swing.*;

/**
 * Created by zachw on 2/25/16.
 */
public class PuzzleWindow extends JFrame{

    int win_wid = 600;
    int win_hei = 600;

    int rows = 6;
    int cols = 4;

    Puzzle puzzle;
    PuzzleDisplay display;

    public PuzzleWindow()
    {
        this.setTitle("Puzzle Board");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(win_wid, win_hei);

        puzzle = new Puzzle(rows, cols);
        display = new PuzzleDisplay(puzzle);

        initMenu();

        this.add(display);

        this.setVisible(true);
    }

    public void initMenu()
    {
        JMenuBar mBar = new JMenuBar();
        JMenu menu = new JMenu("Size");
        JMenuItem rowItem = new JMenuItem("change rows ...");
        JMenuItem colItem = new JMenuItem("change cols ...");

        rowItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeRows();
            }
        });

        colItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeCols();
            }
        });


        mBar.add(menu);
        menu.add(rowItem);
        menu.add(colItem);

        this.setJMenuBar(mBar);
    }

    public void changeRows()
    {
        String input = JOptionPane.showInputDialog(null,
                "Enter number of rows (ex: 4)","row entry",1);
        rows = Integer.parseInt(input);
        this.remove(display);
        puzzle = new Puzzle(rows,cols);
        display = new PuzzleDisplay(puzzle);
        this.add(display);
        repaint();

    }

    public void changeCols()
    {
        System.err.print("menu clicked\n");
        String input = JOptionPane.showInputDialog(null,
                "Enter number of rows (ex: 4)","col entry",1);
        cols = Integer.parseInt(input);
        this.remove(display);
        puzzle = new Puzzle(rows,cols);
        display = new PuzzleDisplay(puzzle);
        this.add(display);
        repaint();

    }



    public static void main(String[] args){
        new PuzzleWindow();
    }
}
