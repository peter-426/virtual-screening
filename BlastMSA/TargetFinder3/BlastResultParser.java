package target;

import java.util.*;
import java.io.*;
 
class BlastResultParser {

    /**
     * parse Blast output, store data from hits above threshold in Hit object.
     * 
     * @param blast_results a multi-line file of blast results.
     * @param minP the minimum percent identity; threshold for a sequence
     *        similarity that implies structural similarity.
     * @param maxE the maximum E value; threshold for significant alignments.
     * @param hitFile the "hit" file where uniprot accession numbers are saved.
     * @return
     */
    public static void parse(Proteome queryProteome, Proteome targetProteome, String blasted, String minP, String maxE) {

        double maxEvalue = Double.parseDouble(maxE);
        double minPercent = Double.parseDouble(minP);
        int count = 0;

        String temp;

        try {
            BufferedReader br = new BufferedReader(new FileReader(blasted));
            String row = br.readLine();

            while (row != null) {

                //System.out.println(row);

                String[] cols = row.split("\t");
                if (cols != null && cols.length > 9) {

                    String[] queryId = cols[0].split("[|]");
                    String[] targetId = cols[1].split("[|]");


                    double percent = Double.parseDouble(cols[2].trim());
                    double e_val = Double.parseDouble(cols[10].trim());

                    if (e_val <= maxEvalue && percent >= minPercent) {
                        ++count;

                        temp = targetId[1];
                        Sequence targetSeq = (Sequence) targetProteome.sequences.get(targetId[1]);

                        Hit hit = new Hit(queryId[1], queryId[2], targetSeq.OS, targetId[1], targetId[2], e_val, percent);

                        QuerySequence querySeq = (QuerySequence) queryProteome.sequences.get(queryId[1]);

                        if (querySeq == null) {
                            System.out.println("BlastResultParser.parse:  seq is null");
                        } else {
                            querySeq.add(hit);
                        }

                        if (Main.testCheckBox.isSelected()) {
                            StringBuffer sb = new StringBuffer();

                            sb.append(">>> query uniprot ACC=" + queryId[1]);
                            sb.append(", query name=" + queryId[2]);

                            sb.append(", target uniProt ACC=" + targetId[1]);
                            sb.append(", target name=" + targetId[2]);

                            sb.append(", identity=" + percent);
                            sb.append("%, e value=" + e_val + "\n");
                            Main.jta3.append(sb.toString());
                        }
                    }
                }
                row = br.readLine();
            }
            br.close();

        } catch (Exception ex) {

            ex.printStackTrace();
        }

    }
}
