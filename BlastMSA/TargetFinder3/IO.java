package target;

import java.io.*;
import java.util.*;

/**
 *
 * @author peter-426
 */
public class IO {

    /**
     * Save subject sequence and hit query sequences to file for
     * Multiple Sequence Alignment
     *
     * @param msaPath
     */
    public static void save(Proteome queryProteome, File msaPath, File queryFile, File targetFile) {

        HashMap sequences = queryProteome.sequences;

        // System.out.println( msaPath.toString() + "\n" + queryFile.toString() + "\n" + targetFile.toString() + "\n" );

        try {
            FileWriter fw = null;

            Set keys = sequences.keySet();
            for (Iterator it = keys.iterator(); it.hasNext();) {

                QuerySequence s = (QuerySequence) sequences.get(it.next());

                if (s.osHitCount >= Main.minOrganisms) {

                    File file = new File(msaPath + "/" + s.accession + "-hits.fasta");
                    fw = new FileWriter(file);

                    //System.out.println(file.toString() + "\n");

                    fw.write(Sequence.getFastaQuery(s.accession, queryFile));

                    for (String targetACC : s.targetAccNumberSet) {
                        fw.write(Sequence.getFasta(targetACC, targetFile));   // write hits from other bacteria
                    }
                    fw.close();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     * Save subject sequence and hit query sequences to file for
     * Multiple Sequence Alignment
     *
     * @param msaPath
     */
    public static void stats(Proteome queryProteome, Proteome targetProteome, File msaPath) {

        HashMap sequences = queryProteome.sequences;
        ArrayList<QuerySequence> temp = new ArrayList<QuerySequence>();

        temp = Sort.sortHashMap1(sequences);

        if (Main.htmlCheckBox.isSelected()) {

            Main.jta3.append("<table>");
            Main.jta3.append("<th>Proteomes</th> <th>Hits</th> <th>mean id.</th> <th> uniprot </th> <th>GN</th> <th>3d</th> <th>other 3D</th>\n");
        } else {
            Main.jta3.append(" Prot. & Hits & mean id. & uniprot & GN & 3d & other 3D \\\\ \n");
            System.out.println("IO stats, seq's size=" + temp.size() + "  min orgs" + Main.minOrganisms + "\n");
        }

        try {


            for (QuerySequence querySeq : temp) {

                // System.out.println("IO stats, size=" + querySeq.osCountHashMap.size());

                if (querySeq.osHitCount >= Main.minOrganisms) {

                    boolean hasThreeDStructure = false;

                    StringBuffer missingOS = new StringBuffer();
                    StringBuffer threeD = new StringBuffer();

                    String line;

                    if (Main.htmlCheckBox.isSelected()) {

                        line = querySeq.osHitCount
                                + " </td><td>   " + querySeq.hitList.size()
                                + " </td><td>   " + Math.round(querySeq.getMeanPercentIdentity()) + "%"
                                + " </td><td>   " + querySeq.accession + "  </td><td>  " + querySeq.GN;

                    } else {
                        line = querySeq.osHitCount
                                + "  &  " + querySeq.hitList.size()
                                + "  &  " + Math.round(querySeq.getMeanPercentIdentity()) + "%"
                                + "  &  " + querySeq.accession + "  &  " + querySeq.GN;
                    }

                    if (PDB.protein3dSet.contains(querySeq.accession)) {
                        threeD.append(" </td><td> yes </td><td>");
                        hasThreeDStructure = true;
                    } else {
                        threeD.append("  </td><td> no </td><td>  ");
                    }
                    for (Hit h : querySeq.hitList) {
                        if (querySeq.accession.equals(h.targetACC) == false) {
                            //System.out.println("query id == subject id \n");

                            if (PDB.protein3dSet.contains(h.targetACC)) {
                                threeD.append(h.targetACC + " ");
                                hasThreeDStructure = true;
                            }
                        }
                    }
                    threeD.append(" </td> <tr> \n");
                    // if( querySeq.speciesSet.size()  < targetProteome.speciesSet.size()   )
                    //     sb.append( ", missing=");

                    Iterator it = targetProteome.speciesSet.iterator();
                    while (it.hasNext()) {
                        String os = (String) it.next();
                        if (!querySeq.speciesSet.contains(os)) {
                            missingOS.append("\n    " + os);
                        }
                    }
                    missingOS.append("\n");

                    if (Main.verboseCheckBox.isSelected()) {
                        Main.jta3.append(line + threeD.toString() + missingOS.toString());
                    } else if (Main.htmlCheckBox.isSelected()) {
                        Main.jta3.append(line + threeD.toString());    // want to see all hits, not just those with 3D
                    } else if (hasThreeDStructure) {
                        Main.jta3.append(line + threeD.toString());    // has 
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (Main.htmlCheckBox.isSelected()) {
              Main.jta3.append("</table>");
        }
    }

    public static void show(QuerySequence s, File proteomeFile) {

        System.out.println("Hit Count= " + s.hitList.size() + " == ");
        System.out.println(Sequence.getFasta(s.accession, proteomeFile));
    }

    public static void listSequenceAccessionNumbers(HashMap sequences) {

        Set keys = sequences.keySet();
        for (Iterator it = keys.iterator(); it.hasNext();) {

            Sequence s = (Sequence) sequences.get(it.next());
            System.out.println(s.accession);
        }
    }

    public static void saveXXX(String s, File f) {
        try {
            FileWriter fw = new FileWriter(f);
            fw.write(s);
            fw.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
