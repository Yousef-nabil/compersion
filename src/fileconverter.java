import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class fileconverter {
    File f;
    int n;
    HashMap<Long, ArrayList<Boolean>> mp;
    ArrayList<Boolean> list = new ArrayList<Boolean>();
    BitWriter b;
    Integer lenghtofmeta;
    fileconverter(File file, int n, HashMap<Long, ArrayList<Boolean>> mp,int lenghtofmeta) {
        f = file;
        this.n = n;
        this.mp = mp;
        this.lenghtofmeta = lenghtofmeta;
    }
    void meta()
    {
        int metasize = mp.size() + mp.size()+(lenghtofmeta + 7) / 8;
        for (int i=0;i<32;i++)
        {
            list.add((metasize&(1<<(31-i)))!=0);
        }
        for(HashMap.Entry<Long, ArrayList<Boolean>> entry : mp.entrySet())
        {
            for(int i=0;i<8;i++)
            {
                list.add((entry.getKey() & (1 << (7 - i))) != 0);
            }
            for(int i=0;i<8;i++)
            {
                list.add((entry.getValue().size()&(1<<(7-i)))!=0);
            }
            list.addAll(entry.getValue());
        }
        while (list.size()%8!=0)
        {
            list.add(false);
        }
    }
    void convert() {
        meta();
        try (FileInputStream fis = new FileInputStream(f)) {
            byte[] buffer = new byte[1];
            int bytesRead=0;
            long totalsize = 0;
            while ((bytesRead = fis.read(buffer)) != -1) {
                long value = 0;
                value = (value) | (buffer[0] & 0xFF);
                ArrayList<Boolean> x=mp.get(value);
                totalsize+=x.size();
                list.addAll(x);
            }
            totalsize=8-(totalsize%8);
            System.out.println("total size:"+totalsize);
            for (int i=0;i<8;i++) {
                list.addFirst((totalsize & (1<<i))!=0) ;
            }
            try {
                b = new BitWriter(f);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            b.writeBits(list);
            b.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    void convertback() {
        HashMap< ArrayList<Boolean>, Long> map= new HashMap<>();
        try (FileInputStream fis = new FileInputStream(f)) {
            byte[] buffer = new byte[1];
            int bytesRead=0;
            int bitsatend=0;
            int metadatasize=0;
            int metacounter=0;
            list=new ArrayList<>();
            ArrayList<Boolean> listofcodes=new ArrayList<Boolean>();
            fis.read(buffer);
            bitsatend = buffer[0] & 0xFF;
            for(int i=0;i<4;i++)
            {
                fis.read(buffer);
                metadatasize = (metadatasize<<8) | (buffer[0] & 0xFF);
            }
            while ((bytesRead = fis.read(buffer) ) != -1) {
                if (bytesRead < 1) {
                    break;
                }
                if (metacounter<metadatasize)
                {
                    metacounter++;
                    for (int i = 0; i < 8; i++) {
                        listofcodes.add((buffer[0] & (1 << (7-i))) != 0);
                    }
                    continue;
                }
                for (int i = 0; i < 8; i++) {
                    list.add((buffer[0] & (1 << (7-i))) != 0);
                }
            }
            System.out.println("final bits"+bitsatend);
            for (int i=0;i<bitsatend-1;i++) {
                list.removeLast();
            }
            int j=0;
            while(j<(long)listofcodes.size())
            {
                if(((long)listofcodes.size()-j) < 16) break;
                long v=0;
                for (int i = 0; i < 8; i++) {
                    v |= (listofcodes.get(i+j) ? 1L : 0L) << (7 - i);
                }
                int count=0;
                for (int i = 0; i < 8; i++) {
                    count |= (listofcodes.get(i+j+8) ? 1 : 0) << (7-i);
                }
                ArrayList<Boolean> x=new ArrayList<>();
                for (int i = 0; i < count; i++) {
                    x.add(listofcodes.get(i+j+16));
                }
                map.put(x,v);
                j+=16+count;
            }
            ArrayList<Boolean> element=new ArrayList<>();
            ArrayList<Long> ans=new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                System.out.println(list.get(i));
                element.add(list.get(i));
                if(map.containsKey(element)) {
                    long temp=map.get(element);
                    ans.add(temp);

                    element.clear();

                }
            }
            System.out.println(element.size());

            for(int i=0;i<ans.size();i++) {
                System.out.println(ans.get(i));
            }
            try {
                FileOutputStream out = new FileOutputStream(f);
                for (long value : ans) {
                    out.write((byte) value);
                }
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
