import java.io.File;

public class Decompress {
    File f;
    int n;
    Decompress(File f,int n) {
        this.f=f;
        this.n=n;
    }
    void decompress() {
        fileconverter fl=new fileconverter(f,n,null,0);
        fl.convertback();
    }
}
