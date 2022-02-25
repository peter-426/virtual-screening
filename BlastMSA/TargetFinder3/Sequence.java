package target;

import java.io.*;

/**
 *  >sp|Q2FHK6|ACP_STAA3 Acyl carrier protein OS=Staphylococcus aureus (strain USA300) GN=acpP
 *
 * @author peter-426
 */
public class Sequence {

    String accession;
    String OS;
    String GN;

    public Sequence(String line) {

        if (line.startsWith(">")) {
            //System.out.println("line" + line + "\n\n");
        } else {
            System.out.println("Not a fasta line >> " + line + "\n\n");
            return;
        }

        try {
            int startIndex = line.indexOf("|") + 1;
            int endIndex = line.indexOf("|", startIndex+1);

            accession = line.substring(startIndex, endIndex);

            startIndex = line.indexOf("OS=") + 3;
            endIndex = line.indexOf("GN=");

            OS = line.substring(startIndex, endIndex).trim();

            startIndex = line.indexOf("GN=") + 3;
            endIndex = line.indexOf(" ", startIndex);
            if(endIndex < startIndex)
                    endIndex = line.length();

            GN = line.substring(startIndex, endIndex).trim();

            //System.out.println(">>>" + accession + ", " + OS + ", " + GN + "<<<");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Get the complete fasta record for specified protein.
     *
     * @param accession
     * @param file
     * @return
     */
    public static String getFastaQuery(String accession, File file) {

        StringBuffer sb = new StringBuffer();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));

            String line = br.readLine();

            while (line != null && line.contains(accession) == false) {
                line = br.readLine();
            }
            if (line != null && line.contains(accession) == true) {

                sb.append(line + "\n");
                line = br.readLine();
                while (line != null && line.startsWith(">") != true) {
                    sb.append(line + "\n");
                    line = br.readLine();
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return sb.toString();
    }

    public static String getFasta(String accession, File folder) {

        return Blast.fastaCMD(accession, Main.msaDir);

    }
}
