package target;

import java.util.*;
import java.io.*;

/**
 *
 * @author peter-426
 */
public class Proteome {

    HashMap sequences;
    HashSet speciesSet;

    public Proteome(File file, boolean isQueryProteome) {

        sequences  = new HashMap();
        speciesSet = new HashSet<String>();

        int count = 0;

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = br.readLine();

            while (line != null) {

                if (line.startsWith(">")) {
                    //System.out.println("Proteome: " + line);
                    ++count;
                    if (isQueryProteome) {
                        QuerySequence seq = new QuerySequence(line);
                        sequences.put(seq.accession, seq);
                    } else {
                        Sequence seq = new Sequence(line);
                        sequences.put(seq.accession, seq);
                        speciesSet.add(seq.OS);
                    }
                }
                line = br.readLine();
            }
            //Main.jta.append("\n" + count + " target sequences in file named " + file + "\n\n");
            br.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getHits() {

        StringBuffer sb = new StringBuffer();

        Set keys = sequences.keySet();
        for (Iterator it = keys.iterator(); it.hasNext();) {

            QuerySequence s = (QuerySequence) sequences.get(it.next());
            sb.append(s.getHits());
        }
        return sb.toString();
    }
}
