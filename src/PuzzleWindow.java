import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by zachw on 2/25/16.
 */
public class PuzzleWindow extends JFrame{

    int win_wid = 600;
    int win_hei = 600;
    int size;

    int rows = 4;
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
        JMenu menu = new JMenu("Menu");
        JMenuItem sizeItem = new JMenuItem("load");
        JMenuItem colItem = new JMenuItem("change cols ...");

        sizeItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    changeSize();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        colItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeCols();
            }
        });


        mBar.add(menu);
        menu.add(sizeItem);
        menu.add(colItem);

        this.setJMenuBar(mBar);
    }

    public void changeSize() throws FileNotFoundException {
//        String input = JOptionPane.showInputDialog(null,
//                "Enter number of rows (ex: 4)","row entry",1);
        ReadFile readFile;
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File("../KenKenCSIT441"));
        fc.showOpenDialog(new JFrame());

        File x=  fc.getSelectedFile();

            readFile=new ReadFile(x);

        size = readFile.getSize();
        this.remove(display);
        puzzle = new Puzzle(size,size);
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
