import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class BitWriter {
    private BufferedOutputStream out;
    private char currentByte = 0;
    private int bitCount = 0;
    public int gb=0;
    public BitWriter(File f) throws IOException {
        out  = new BufferedOutputStream(new FileOutputStream(f));
    }
    public void writeBit(boolean bit) throws IOException {
        currentByte <<= 1;
        if (bit) currentByte |= 1;

        bitCount++;


        if (bitCount == 8) {
            out.write(currentByte);
            currentByte = 0;
            bitCount = 0;
            gb++;
        }
    }

    public void writeBits(ArrayList<Boolean> bits) throws IOException {
        for (boolean b : bits) writeBit(b);
    }

    public void close() throws IOException {
        if (bitCount > 0) {
            currentByte <<= (8 - bitCount);
            gb++;
            out.write(currentByte);
        }
        out.close();
    }
}
