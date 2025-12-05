import java.util.*;
class NodeComparator implements Comparator<Node> {
    @Override
    public int compare(Node a, Node b) {
        return Integer.compare(a.freq, b.freq);
    }
}
public class huffman {
    HashMap<Long,ArrayList<Boolean>> mp =new HashMap<>();
    void explore(Node top, ArrayList<Boolean> code) {
        if (top==null) return;
        if (top.value!=-1)
            mp.put(top.value,code);
        code.addLast(false);
        explore(top.left,new ArrayList<>(code));
        code.removeLast();
        code.addLast(true);
        explore(top.right,new ArrayList<>(code));
        code.removeLast();
    }
    Pair buildtree(Map<Long,Integer> freq) {
        PriorityQueue<Node> p = new PriorityQueue<>(new NodeComparator());
        for (Map.Entry<Long, Integer> entry : freq.entrySet()) {
            Node temp = new Node(entry.getKey(), null, null, entry.getValue());
            p.add(temp);
        }
        while (p.size() > 1) {
            Node n1 = p.poll();
            Node n2 = p.poll();
            Node n = new Node(-1, n1, n2, n1.freq + n2.freq);
            p.add(n);
        }
        ArrayList<Boolean> temp = new ArrayList<>();
        explore(p.poll(), temp);
        int value=0;
        for (Map.Entry<Long, ArrayList<Boolean>> entry : mp.entrySet()) {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
            value+=entry.getValue().size();
        }
        Pair combined=new Pair(value,mp);
        return combined;
    }
}
