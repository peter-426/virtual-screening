package target;

import java.util.*;
import java.io.*;

/**
 *  Build table from pdbtosp.txt
 * 
---------------------------------------------------------------------------
UniProt - Swiss-Prot Protein Knowledgebase
Swiss Institute of Bioinformatics (SIB); Geneva, Switzerland
European Bioinformatics Institute (EBI); Hinxton, United Kingdom
Protein Information Resource (PIR); Washington DC, USA
----------------------------------------------------------------------------

Description: PDB cross-references in UniProtKB/Swiss-Prot
Name:        pdbtosp.txt
Release:     2010_07 of 15-Jun-2010

----------------------------------------------------------------------------

The PDB database is available at the following URL:

http://www.pdb.org/
http://www.ebi.ac.uk/msd/


- Number of PDB entries referenced in Swiss-Prot: 51760
- Number of Swiss-Prot entries with one or more pointers to PDB: 15818

PDB
code  Method    Resolution  Swiss-Prot entry name(s)
____  ________  __________  __________________________________________
101M  X-ray     2.07 A      MYG_PHYCA   (P02185)
102L  X-ray     1.74 A      LYS_BPT4    (P00720)
102M  X-ray     1.84 A      MYG_PHYCA   (P02185)
103L  X-ray     1.90 A      LYS_BPT4    (P00720) 
...
1FJG  X-ray     3.00 A      RS10_THET8  (Q5SHN7), RS11_THET8  (P80376),
                            RS12_THET8  (Q5SHN3), RS13_THET8  (P80377),
                            RS14Z_THET8 (Q5SHQ1), RS15_THET8  (Q5SJ76),
                            RS16_THET8  (Q5SJH3), RS17_THET8  (Q5SHP7),
                            RS18_THET8  (Q5SLQ0), RS19_THET8  (Q5SHP2),
                            RS20_THET8  (P80380), RS2_THET8   (P80371),
                            RS3_THET8   (P80372), RS4_THET8   (P80373),
                            RS5_THET8   (Q5SHQ5), RS6_THET8   (Q5SLP8),
                            RS7_THET8   (P17291), RS8_THET8   (Q5SHQ2),
                            RS9_THET8   (P80374), RSHX_THET8  (Q5SIH3)
1FJI  Model     -           HEMH_BACSU  (P32396)
 *
 *
 *
 *
 */
public class PDB {

    static HashMap pdbToSP;
    static HashMap spToPDB;
    static HashSet protein3dSet;

    public static void buildMaps(File pdbFile) {

        pdbToSP = new HashMap();
        spToPDB = new HashMap();

        protein3dSet = new HashSet();


        try {
            BufferedReader br = new BufferedReader(new FileReader(pdbFile));

            String line = br.readLine();

            int count = 0;

            while (line != null) {

                if (line.matches("^[1-9].*")) {

                    // Main.jta.append(line + "\n");

                    int start = 0;
                    int end = line.indexOf(" ");
                    String pdbID = line.substring(0, end);
                    //Main.jta.append(pdbID + "=> ");

                    do {
                        start = line.indexOf("(") + 1;
                        end = line.indexOf(")");

                        String acc = line.substring(start, end);
                        protein3dSet.add(acc);
                        //Main.jta.append(acc + ", ");

                        start = line.indexOf("(", end + 1) + 1;
                        end = line.indexOf(")", start);
                        if (start > 0 && end > start) {
                            acc = line.substring(start, end);
                            protein3dSet.add(acc);
                            // Main.jta.append(acc + ", ");
                        }
                        line = br.readLine();

                    } while (line.startsWith("  "));

                    //Main.jta.append("\n");
                } else {
                    line = br.readLine();
                }

                // if (++count > 255) { break; }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
