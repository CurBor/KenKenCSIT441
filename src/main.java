import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by Wei Zhou on 2016/3/11.
 */
public class main {
    public static void main(String[] args) throws FileNotFoundException {

        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File("../KenKenCSIT441"));
        fc.showOpenDialog(new JFrame());

        File x=  fc.getSelectedFile();
        new ReadFile(x);
    }



}
