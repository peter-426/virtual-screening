package target;

import java.util.*;
import java.io.*;
 
/**
 *
 * @author
 */
public class Blast {

    /**
     * http://www.ncbi.nlm.nih.gov/staff/tao/URLAPI/formatdb_fastacmd.html
     *
     * @param dbseq
     * @return
     */
    public static String formatDB(File dbseq, File outputFolder) {

        if (!outputFolder.exists()) {
            outputFolder.mkdir();
        }

        String cmd = Main.blastHome + "formatdb";

        List cmdArgsList = new ArrayList<String>();
        cmdArgsList.add("-i");
        cmdArgsList.add(dbseq.getPath());

        //cmdArgsList.add(fasta);
        cmdArgsList.add("-p");
        cmdArgsList.add("T");

        cmdArgsList.add("-o");
        cmdArgsList.add("T");

        cmdArgsList.add("-n");
        cmdArgsList.add("BlastDB");

        String s = runBlast(cmd, cmdArgsList, outputFolder);
        return s;
    }

    // D:\b-tools\blast\bin\fastacmd -d BlastDB -p T -s Q5HJZ7
    public static String fastaCMD(String term, File folder) {


        if ( ! folder.exists() ) {
            folder.mkdir();
        }

        String cmd = Main.blastHome + "fastacmd";

        List cmdArgsList = new ArrayList<String>();
        // cmdArgsList.add("-i");
        //  cmdArgsList.add(dbFolder.getPath());

        //cmdArgsList.add(fasta);

        cmdArgsList.add("-d");
        cmdArgsList.add("BlastDB");

        cmdArgsList.add("-p");
        cmdArgsList.add("T");

        cmdArgsList.add("-s");
        cmdArgsList.add(term);

        String s = runBlast(cmd, cmdArgsList, folder);

        return s;
    }

    /**
     *
     * @param command
     * @param args
     * @return
     */
    public static String runBlast(String command, List<String> args, File outputFolder) {

        if (!outputFolder.exists()) {
            outputFolder.mkdir();
        }

        StringBuffer sb = new StringBuffer();
        StringBuffer cmd = new StringBuffer();

        if (command == null || command.equals("")) {
            throw new RuntimeException("The 'command' port cannot be null.");
        }
        Process proc = null;
        Runtime rt = Runtime.getRuntime();
        cmd.append(command + " ");

        for (String s : args) {
            cmd.append(s + " ");
        }

        try {
            proc = rt.exec(cmd.toString(), null, outputFolder);
            // Get the input stream and read from it
            InputStream in = proc.getInputStream();
            int c;

            while ((c = in.read()) != -1) {
                sb.append((char) c);
            }
            in.close();


        } catch (Exception ex) {
            System.err.println(ex);
        }
        return sb.toString();
    }

    /**
     *
     * @param fasta the sequences that will be blasted against the DB
     * @param evalue
     * @return blast output in tabular format
     */
    public static void blast(File fasta, String evalue, File outputFolder) {

        if (!outputFolder.exists()) {
            outputFolder.mkdir();
        }

        String cmd = Main.blastHome + "blastall";

        // BlastDB: written to homeData as BlastDB.phr  .pin  .psq

        List cmdArgsList = new ArrayList<String>();
        cmdArgsList.add("-p");
        cmdArgsList.add("blastp");
        cmdArgsList.add("-d");
        cmdArgsList.add("BlastDB");
        cmdArgsList.add("-i");

        cmdArgsList.add(fasta.getPath());

        cmdArgsList.add("-e");
        cmdArgsList.add(evalue);
        cmdArgsList.add("-m");
        cmdArgsList.add("8");
        cmdArgsList.add("-a");
        cmdArgsList.add("2");

        runBlastProteomes(cmd, cmdArgsList, outputFolder);
    }

    /**
     *
     * @param command
     * @param args
     * @return
     */
    public static void runBlastProteomes(String command, List<String> args, File outputFolder) {

        if (!outputFolder.exists()) {
            outputFolder.mkdir();
        }

        StringBuffer cmd = new StringBuffer();

        if (command == null || command.equals("")) {
            throw new RuntimeException("The 'command' port cannot be null.");
        }
        Process proc = null;
        Runtime rt = Runtime.getRuntime();
        cmd.append(command + " ");

        for (String s : args) {
            cmd.append(s + " ");
        }

        try {
            proc = rt.exec(cmd.toString(), null, outputFolder);
            // Get the input stream and read from it
            InputStream in = proc.getInputStream();
            int c;


            String blast_out = outputFolder + "/blast_results.txt";
            FileWriter fw = new FileWriter(blast_out);

            while ((c = in.read()) != -1) {
                fw.write((char) c);
            }
            in.close();
            fw.close();
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }
}
