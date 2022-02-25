package target;
 
import java.util.*;
import java.io.*;

/**
 *
 * @author
 */
public class MSA {

    /**
     *
     * @param fasta the sequences that will be blasted against the DB
     * @param evalue
     * @return blast output in tabular format
     */
    public static String msa(File in) {

        String cmd = Main.muscleHome + "muscle";
        List cmdArgsList = new ArrayList<String>();
        File out = new File(in.toString() + "A");

        cmdArgsList.add("-in");
        cmdArgsList.add(in.toString());
        cmdArgsList.add("-out");
        cmdArgsList.add(out.toString());

        String s = run(cmd, cmdArgsList);
        return s;
    }

    public static String msaImage(File in) {

        File out = new File(in.toString() + ".png");
        String cmd = Main.jalviewHome + "jalview.exe";

        // jalview -nodisplay  -open test.fastaA -colour Zappo  -png my.png -imgMap my.html

        List cmdArgsList = new ArrayList<String>();
        cmdArgsList.add("-nodisplay");

        cmdArgsList.add("-open");
        cmdArgsList.add(in.toString());

        cmdArgsList.add("-colour");
        cmdArgsList.add("-Zappo");


        cmdArgsList.add("-png");
        cmdArgsList.add(out.toString());


        String s = run(cmd, cmdArgsList);
        return s;
    }

    /**
     *
     * @param command
     * @param args
     * @return
     */
    public static String run(String command, List<String> args) {

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

        Main.jta3.append(cmd.toString());

        try {
            proc = rt.exec(cmd.toString(), null, Main.msaDir);

            Main.jta3.append(" ... executed \n");

            //Get the input stream and read from it

            InputStream in = proc.getErrorStream();  // InputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String line = null;

            while ((line = br.readLine()) != null) {
                // Muscle sends info to stderr,
                // the alignment goes to stdout or the -out file
                sb.append(line + "\n");
            }
            in.close();

        } catch (Exception ex) {
            System.err.println(ex);
        }
        return sb.toString();
    }
}

