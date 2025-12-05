import java.util.ArrayList;
import java.util.HashMap;

public class Node {
    long value;
    Node left;
    Node right;
    int freq;
    Node (long value , Node left, Node right, int freq)
    {
        this.value=value;
        this.left=left;
        this.right=right;
        this.freq=freq;
    }
}
class Pair {
    int a;
    HashMap<Long, ArrayList<Boolean>> b;
    Pair(int a, HashMap<Long,ArrayList<Boolean>>  b) {
        this.a = a;
        this.b = b;
    }
}

