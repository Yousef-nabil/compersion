import javax.swing.*;
import java.io.File;
public class Main {
    public static File openFileDialog() {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile();
        } else {
            return null;
        }
    }


    public static void main(String[] args) {
        File selected = openFileDialog();
        if (selected != null) {
            int option =2;

            if(option == 1) {
                Compress compress = new Compress(selected, 1);
                compress.compressFile();
            } else if(option == 2) {
                Decompress decompress = new Decompress(selected, 1);
                decompress.decompress();
            } else {
                System.out.println("Invalid option");
            }


        } else {
            System.out.println("No file selected.");
        }
    }
}
