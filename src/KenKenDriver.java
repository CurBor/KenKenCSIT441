import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * KenKen Puzzle
 * Created by Zach Widger and Wei Zhou
 * For CSIT 441 Artificial Intelligence
 * Finished on 3/28/2016
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
        JMenuItem description = new JMenuItem("About Game");
        JMenuItem helpMenu = new JMenuItem("Help");
        JMenuItem loadGame = new JMenuItem("load");
        JMenuItem nodeCon = new JMenuItem("run node consistency");
        JMenuItem arcCon = new JMenuItem("run arch consistency");
        JMenuItem fullArcCon = new JMenuItem("run full arch consistency");
        JMenuItem genCon = new JMenuItem("run full general consistency");
        JMenuItem searchCon = new JMenuItem("run search");
        JMenuItem forwardCheckingCon = new JMenuItem("forward checking");

        loadGame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {

                    changeSize();


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        description.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt){
                JOptionPane.showMessageDialog(null,"As in sudoku, the goal of each puzzle is to fill a grid with digits\n" +
                        " –– 1 through 4 for a 4×4 grid, 1 through 5 for a 5×5, etc. –– \n" +
                        "so that no digit appears more than once in any row or any column (a Latin square). \n" +
                        "Grids range in size from 3×3 to 9×9. Additionally, KenKen grids are divided into heavily outlined groups of cells \n" +
                        "–– often called “cages” –– and the numbers in the cells of each cage must produce a certain “target” number when \n" +
                        "combined using a specified mathematical operation (either addition, subtraction, multiplication or division). \n" +
                        "For example, a linear three-cell cage specifying addition and a target number of 6 in a 4×4 puzzle must be \n" +
                        "satisfied with the digits 1, 2, and 3. Digits may be repeated within a cage, as long as they are not in the same row or column. \n" +
                        "No operation is relevant for a single-cell cage: placing the \"target\" in the cell is the only possibility (thus being a \"free space\").\n" +
                        " The target number and operation appear in the upper left-hand corner of the cage.\n" +
                        "This information was gathered from https://en.wikipedia.org/wiki/KenKen","About KenKen",1);
            }
        });

        helpMenu.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt){
                JOptionPane.showMessageDialog(null,"Welcome! This is a KenKen game designed by Zach Widger and Wei Zhou.\n" +
                        "To get started, load a problem from file.\n" +
                        "You are able to run node, arch, full arch, and general consistencies.\n" +
                        "It is suggested that you use node consistency before either arch consistencies, but this is not required.\n" +
                        "Full arch consistency will run arch consistency until no change is found\n" +
                        "General consistency will check all constraints including those above binary.\n" +
                        "General consistnecy will also run until no change is found in the domains of each box.\n" +
                        "You are able to view the domains inside of each box after any of the consistency types have been ran\n" +
                        "When using search for larger problems it is suggested that you run general consistency first\n" +
                        "as the 9x9 problems will take a long time without it.\n" +
                        "This game also supports user solver and will let you know when you have won","Help Menu",1);
            }
        });

        arcCon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                puzzle.checkSingleDomain();
                puzzle.archConsistency();
                if(display.checkwin()){
                    JOptionPane.showMessageDialog(null,"Arch Consistency has finished the game!", "KenKen result!!",1);
                }else {domainDialogBox("Arch");}

            }
        });

        fullArcCon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                puzzle.checkSingleDomain();
                puzzle.fullArchConsistency();
                if(display.checkwin()){
                    JOptionPane.showMessageDialog(null,"Full arch Consistency has finished the game!", "KenKen result!!",1);
                }else {domainDialogBox("Full Arch");}
            }
        });

        genCon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                puzzle.checkSingleDomain();
                puzzle.generalConsistency();
                if(display.checkwin()){
                    JOptionPane.showMessageDialog(null,"General Consistency has finished the game!", "KenKen result!!",1);
                }else{domainDialogBox("General");}
            }
        });

        nodeCon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                puzzle.nodeConsistency();
                puzzle.checkSingleDomain();
                domainDialogBox("Node");
            }
        });
        
        searchCon.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt){
                puzzle.initSearch();
                display.win();
                JOptionPane.showMessageDialog(null, "The AI has finish the game!", "Search result!!", 1);
            }
        });

        forwardCheckingCon.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt){
                boolean check=puzzle.forwardChecking();
                if(check)
                    JOptionPane.showMessageDialog(null,"You are on the right track!", "Forward Checking",1);
                else
                    JOptionPane.showMessageDialog(null,"You are on the wrong track!", "Forward Checking",0);

            }
        });

        mBar.add(menu);
        menu.add(description);
        menu.add(helpMenu);
        menu.add(loadGame);
        menu.add(nodeCon);
        menu.add(arcCon);
        menu.add(fullArcCon);
        menu.add(genCon);
        menu.add(searchCon);
        menu.add(forwardCheckingCon);

        this.setJMenuBar(mBar);

        if(forzenBoard)
        {
            nodeCon.setEnabled(false);
            arcCon.setEnabled(false);
            genCon.setEnabled(false);
            fullArcCon.setEnabled(false);
            searchCon.setEnabled(false);
            forwardCheckingCon.setEnabled(false);
        }else{
            nodeCon.setEnabled(true);
            arcCon.setEnabled(true);
            genCon.setEnabled(true);
            fullArcCon.setEnabled(true);
            searchCon.setEnabled(true);
            forwardCheckingCon.setEnabled(true);
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

    public void domainDialogBox(String type){
        String domains = "";
        domains = puzzle.domainsToString();
        int reply = JOptionPane.showConfirmDialog(null, type + " Consistency has finished running. \n" +
                "Would you like to see the domains?", "General Consistency", JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION){
            JTextArea textArea = new JTextArea(domains);
            JScrollPane scrollPane = new JScrollPane(textArea);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            scrollPane.setPreferredSize(new Dimension(500, 500 ) );
            JOptionPane.showMessageDialog(null, scrollPane, "List of all current domains",
                    JOptionPane.YES_NO_OPTION);
        }
    }

    public static void main(String[] args){
        new KenKenDriver();
    }
}
