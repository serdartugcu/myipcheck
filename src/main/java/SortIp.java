import org.apache.commons.net.util.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SortIp {
    public static void main(String[] args) {

        List<String> ipList = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader("unsortedIp.txt"))) {
            for (String line; (line = br.readLine()) != null; ) {
                ipList.add(line);
            }
        }
            catch (IOException e) {
                e.printStackTrace();
            }
        Collections.sort(ipList, (a, b) -> {
            int[] aOct = Arrays.stream(a.split("\\.")).mapToInt(Integer::parseInt).toArray();
            int[] bOct = Arrays.stream(b.split("\\.")).mapToInt(Integer::parseInt).toArray();
            int r = 0;
            for (int i = 0; i < aOct.length && i < bOct.length; i++) {
                r = Integer.compare(aOct[i], bOct[i]);
                if (r != 0) {
                    return r;
                }
            }
            return r;
        });

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter("sortedIp.txt", true));
            for(String ip:ipList) {
                writer.write(ip + "\n");

            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }



        }

    }
