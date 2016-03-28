import javax.swing.*;
import java.awt.*;
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
    BoxObject initialFileList[];
    boolean forzenBoard=true;

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
        JMenuItem loadGame = new JMenuItem("load");
        JMenuItem nodeCon = new JMenuItem("run node consistency");
        JMenuItem arcCon = new JMenuItem("run arch consistency");
        JMenuItem genCon = new JMenuItem("run general consistency");
        JMenuItem searchCon = new JMenuItem("run search");

        loadGame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {

                    changeSize();


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        arcCon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                puzzle.checkSingleDomain();
                puzzle.archConsistency();
                if(display.checkwin()){
                    JOptionPane.showMessageDialog(null,"AI finish the game!", "Arc result!!",1);
                }

            }
        });

        genCon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                puzzle.checkSingleDomain();
                puzzle.generalConsistency();
                if(display.checkwin()){
                    JOptionPane.showMessageDialog(null,"AI finish the game!", "Gen result!!",1);
                }

            }
        });

        nodeCon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                puzzle.nodeConsistency();
                puzzle.checkSingleDomain();
            }
        });
        
        searchCon.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt){
                puzzle.initSearch();
                display.win();
                JOptionPane.showMessageDialog(null,"AI finish the game!", "Search result!!",1);
            }
        });

        mBar.add(menu);
        menu.add(loadGame);
        menu.add(nodeCon);
        menu.add(arcCon);
        menu.add(genCon);
        menu.add(searchCon);

        this.setJMenuBar(mBar);

        if(forzenBoard)
        {
            nodeCon.setEnabled(false);
            arcCon.setEnabled(false);
            genCon.setEnabled(false);
            searchCon.setEnabled(false);
        }else{
            nodeCon.setEnabled(true);
            arcCon.setEnabled(true);
            genCon.setEnabled(true);
            searchCon.setEnabled(true);
        }

    }

    public void changeSize() throws FileNotFoundException {
//        String input = JOptionPane.showInputDialog(null,
//                "Enter number of rows (ex: 4)","row entry",1);
        ReadFile readFile;
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File("../KenKenCSIT441"));
        fc.showOpenDialog(new JFrame());

        if(fc.getSelectedFile()!=null)
        {
            forzenBoard=false;
            File x=  fc.getSelectedFile();
            readFile=new ReadFile(x);

            this.remove(display);
            initialFileList =readFile.getList();
            size = readFile.getSize();
            puzzle = new KenKenPuzzle(size,size);
            display = new KenKenDisplay(puzzle);
            display.loadBoxObjects(initialFileList);
            this.add(display);
            initMenu();
            repaint();
            puzzle.setObjectList(initialFileList);
            puzzle.SetDomain(size);
            puzzle.loadBoxObjects(initialFileList);
            display.loadBoxObjects(initialFileList);
        }



    }

    public static void main(String[] args){
        new KenKenDriver();
    }
}
