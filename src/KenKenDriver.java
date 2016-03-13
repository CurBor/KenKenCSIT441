import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by zachw on 2/25/16.
 */
public class KenKenDriver extends JFrame{

    int win_wid = 600;
    int win_hei = 600;
    int size;

    int rows = 4;
    int cols = 4;

    KenKenPuzzle puzzle;
    KenKenDisplay display;
    BoxObject listOfConstraints[];

    public KenKenDriver()
    {
        this.setTitle("Puzzle Board");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(win_wid, win_hei);

        puzzle = new KenKenPuzzle(rows, cols);
        display = new KenKenDisplay(puzzle);


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
                    puzzle.SetDomain(size);
                    puzzle.loadBoxObjects(listOfConstraints);
                    display.loadBoxObjects(listOfConstraints);
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

        this.remove(display);
        listOfConstraints=readFile.getList();
        size = readFile.getSize();
        puzzle = new KenKenPuzzle(size,size);
        display = new KenKenDisplay(puzzle);
        display.loadBoxObjects(listOfConstraints);
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
        puzzle = new KenKenPuzzle(rows,cols);
        display = new KenKenDisplay(puzzle);
        this.add(display);
        repaint();

    }



    public static void main(String[] args){
        new KenKenDriver();
    }
}
