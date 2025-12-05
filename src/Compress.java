import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Compress {
    File f;
    int n;
    Compress(File f,int n) {
        this.f=f;
        this.n=n;
    }
    public Map<Long, Integer> readFileInChunks(File file, int n) {
        if (n < 1 || n > 8) {
            throw new IllegalArgumentException("Chunk size must be between 1 and 8 bytes.");
        }
        Map<Long, Integer> freq = new HashMap<>();

        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[1];
            int bytesRead=0;
            while ((bytesRead = fis.read(buffer)) != -1) {
                long value = 0;
                for (int i = 0; i < bytesRead; i++) {
                    value = (value << 8) | (buffer[i] & 0xFF);
                }
                freq.put(value, freq.getOrDefault(value, 0) + 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return freq;
    }
    void compressFile()
    {
        Map<Long,Integer> freq= readFileInChunks(f  ,n);
        huffman t= new huffman();
        Pair p=t.buildtree(freq);
        HashMap<Long, ArrayList<Boolean>> mp=p.b ;
        fileconverter fl=new fileconverter(f,n,mp,p.a);
        fl.convert();
    }
}
