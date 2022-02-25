package target;

import java.io.*;
import java.util.*;

public class Target {

     Proteome queryProteome;
     Proteome targetProteome;

    /**
     *
     * @param e1 the e-value used by blast: target versus source organism.
     * @param percent the percent identity threshold used to parse blast results.
     */
    public void findTargets(String e1, String percent) {


        //Main.jta1.append("Building pdb maps from: " + Main.pdbFile + "\n");

        PDB.buildMaps(Main.pdbFile);

        Main.jta1.append("\n query: "  + Main.queryFile + "\n\n");
        Main.jta2.append("\n target: " + Main.targetFile + "\n\n" );

        if (Main.formatDbCheckBox.isSelected()) {
            Main.jta3.append("format DB: " + Main.targetFile);
            String s = Blast.formatDB(Main.targetFile, Main.msaDir);
            Main.jta3.append(s + "\n\n");
        }
        if (Main.blastCheckBox.isSelected()) {
           
            blast(e1, percent);
        }
        if (Main.msaCheckBox.isSelected()) {
            msa();
        }
        if (Main.msaImageCheckBox.isSelected()) {
            msaImage();
        }
    }


    public void listProteomes() {

        Set set = queryProteome.sequences.keySet();
        Iterator it = set.iterator();
        HashSet hs = new HashSet();
        while( it.hasNext() ) {
            
            String str= (String) it.next();
            String GN = ((Sequence) queryProteome.sequences.get(str)).GN;
            //sprintf();
            hs.add("GN=" + GN + "\tID =" + str);
        }

        ArrayList<String> al = Sort.sortHashSet(hs);
        for(String s:  al) {
            Main.jta1.append( s + "\n");
        }


        al = Sort.sortHashSet(targetProteome.speciesSet);
        for(String s:  al) {
            Main.jta2.append("OS=" + s + "\n");
        }
    }



    public void buildProteomes() {

        boolean isQueryProteome = true;
        queryProteome = new Proteome(Main.queryFile, isQueryProteome); // some B subtilis seq's in fasta
        System.gc();
        isQueryProteome = false;
        targetProteome = new Proteome(Main.targetFile, isQueryProteome); // all proteome seq's in fasta
        System.gc();
    }

 
    /**
     *
     * @param e1
     * @param percent
     */
    public void blast(String e1, String percent) {

        if (Main.testCheckBox.isSelected()) {

            String headings = "\n\nquery id, target id, % identity, alignment length, mismatches, gap openings, q. start, q. end, s. start, s. end, e-value, bit score\n\n";
            Main.jta3.append(headings);
            buildProteomes();
            listProteomes();
            return;
        }

        buildProteomes();
       // listProteomes();
        System.gc();

        long startTime = System.currentTimeMillis();

        Main.jta3.append("\n\nblasting query against database \n");
        Blast.blast(Main.queryFile, e1, Main.msaDir);
        Main.jta3.append("\nparsing blast results \n\n");
        
        String blast_results = Main.msaDir + "/blast_results.txt";
        BlastResultParser.parse(queryProteome, targetProteome, blast_results, percent, e1);

        IO.save(queryProteome, Main.msaDir, Main.queryFile, Main.targetFile);

        IO.stats(queryProteome, targetProteome, Main.msaDir);                // show stats

        Main.jta3.append("\n\n << blast hits saved to " + Main.msaDir + " >> \n\n");

        long endTime = System.currentTimeMillis();
        long averageTime = (endTime - startTime) / 1000;

        Main.jta3.append("\n\n time in seconds " + averageTime + "\n\n");
    }

    public void msa() {

        long startTime = System.currentTimeMillis();

        File[] files = Main.msaDir.listFiles();
        int count = 0;

        for (File file : files) {
            String f = file.toString();

            if (f.endsWith(".fasta")) {
                Main.jta3.append(++count + ") Now performing MSA on " + file + "\n\n");
                String temp = MSA.msa(file);

                //Main.jta3.append(temp);
                Main.jta3.append(" >>> MSA complete \n\n");
            }

            if (count > 1 && Main.testCheckBox.isSelected()) {
                Main.jta3.append(" testing ... just did 3 files \n\n");
                break;
            }
        }
        if (count > 0) {
            long endTime = System.currentTimeMillis();
            long averageTime = ((endTime - startTime) / count) / 1000;
            Main.jta3.append("\n\n" + count + " Mutiple Sequence Alignments, avg time in seconds " + averageTime + "\n\n");
        }
    }

    public void msaImage() {

        long startTime = System.currentTimeMillis();

        File[] files = Main.msaDir.listFiles();
        int count = 0;

        for (File file : files) {
            String f = file.toString();

            if (f.endsWith(".fastaA")) {
                Main.jta3.append(++count + ") Now generating MSA image of " + file + "\n\n");
                String temp = MSA.msaImage(file);

                //Main.jta3.append(temp);

                Main.jta3.append(" >>> MSA complete \n\n");
            }

            if (count > 1 && Main.testCheckBox.isSelected()) {
                Main.jta3.append(" testing ... just did 3 files \n\n");
                break;
            }
        }
        if (count > 0) {
            long endTime = System.currentTimeMillis();
            long averageTime = ((endTime - startTime) / count) / 1000;
            Main.jta3.append("\n\n" + count + " Mutiple Sequence Alignments, avg time in seconds " + averageTime + "\n\n");
        }
    }
}



