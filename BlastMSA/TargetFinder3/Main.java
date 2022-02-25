package target;
 
import javax.swing.*;
import java.net.*;
import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

/**
 *
 * 
 */
public class Main extends JFrame implements ActionListener {

    static int minOrganisms = 10;  // ***************************
    static JTabbedPane jtp = new JTabbedPane();
    public static JTextArea jta1 = new JTextArea();
    public static JTextArea jta2 = new JTextArea();
    public static JTextArea jta3 = new JTextArea();
    
    static JToolBar toolBar = new JToolBar();
    static JButton runButton, clearButton;
    static JTextField jtf_e1 = new JTextField("0.00001", 5);
    static JTextField jtf_percent = new JTextField("30", 5);
    static File targetListFile;
    static volatile boolean pause = false;
    static volatile boolean skip = false;
    static JScrollPane jsp_ta1;
    static JScrollPane jsp_ta2;
    static JScrollPane jsp_ta3;
    static public File queryDir;
    static public File targetDir;
    static public File msaDir;

    static public File targetFile;
    static public File queryFile;
    static public File pdbResourcesDir;
    static public File pdbFile;
    
    //static public File outputFolder;
    static public String blastHome;
    static public String muscleHome;
    static public String jalviewHome;
    public static JToolBar toolBar2 = new JToolBar();
    public static JTextField jtf_queryFile = new JTextField("B_sub_123.fasta", 7);
    public static JTextField jtf_targetFile = new JTextField("40", 7);
    public static JCheckBox formatDbCheckBox = new JCheckBox("formatDB");
    public static JCheckBox blastCheckBox = new JCheckBox("blast");
    public static JCheckBox verboseCheckBox = new JCheckBox("verbose");
    public static JCheckBox htmlCheckBox = new JCheckBox("html");
    public static JCheckBox msaCheckBox = new JCheckBox("msa");
    public static JCheckBox msaImageCheckBox = new JCheckBox("msa image");
    public static JCheckBox testCheckBox = new JCheckBox("test");
    public static JTextField jtf_minOrganisms = new JTextField("20", 3);

    /**
     *
     */
    public Main() {

        setTitle("Target Finder 3");

        toolBar.setBackground(new Color(255, 255, 174));

        runButton = new JButton("Run");
        runButton.setToolTipText("Blast query sequences against target sequences and/or do MSA.");
        runButton.addActionListener(this);
        toolBar.add(runButton);
        toolBar.addSeparator();

        toolBar.add(formatDbCheckBox);
        toolBar.addSeparator();
        toolBar.add(blastCheckBox);
        toolBar.add(verboseCheckBox);
        toolBar.add(htmlCheckBox);
        //toolBar.addSeparator();
        toolBar.add(new JLabel("min orgs:"));
        toolBar.add(jtf_minOrganisms);
        toolBar.addSeparator();

        toolBar.add(msaCheckBox);
        toolBar.addSeparator();
        toolBar.add(msaImageCheckBox);
        toolBar.addSeparator();

        toolBar.add(testCheckBox);
        toolBar.addSeparator();

        toolBar.add(new JLabel("query:"));
        jtf_queryFile.setToolTipText("Query fasta file (B subtilis).");
        toolBar.add(jtf_queryFile);
        toolBar.addSeparator();

        toolBar.add(new JLabel("target:"));
        jtf_targetFile.setToolTipText("All proteomes fasta file.");
        toolBar.add(jtf_targetFile);
        toolBar.addSeparator();


        toolBar.add(new JLabel("e values:"));
        jtf_e1.setToolTipText("e value for source versus target.");
        toolBar.add(jtf_e1);
        toolBar.addSeparator();

        toolBar.add(new JLabel("percent identity:"));
        jtf_percent.setToolTipText("percent identity threshold for hits.");
        toolBar.add(jtf_percent);
        toolBar.addSeparator();

        clearButton = new JButton("Clear");
        clearButton.setToolTipText("Clears text area.");
        clearButton.addActionListener(this);
        toolBar.add(clearButton);
        toolBar.addSeparator();

        setLayout(new BorderLayout());
        getContentPane().add(toolBar, BorderLayout.NORTH);
        getContentPane().add(jtp, BorderLayout.CENTER);

        jsp_ta1 = new JScrollPane(jta1,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jtp.addTab("query proteins", jsp_ta1);

        jsp_ta2 = new JScrollPane(jta2,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jtp.addTab("subject proteomes", jsp_ta2);

        jsp_ta3 = new JScrollPane(jta3,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jtp.addTab("output", jsp_ta3);

        setSize(1200, 800);
        setVisible(true);
        WindowCloser wc = new WindowCloser();
        addWindowListener(wc);
    }

    /**
     *
     * @param b
     */
    public void enableButtons(boolean b) {

        runButton.setEnabled(b);

    }

    /**
     *
     * @param pw
     * @param s2
     */
    public void show(String pw, String s2) {
        try {
            JPanel panel = new JPanel();
            ImageIcon icon = new ImageIcon(new URL(s2));
            JLabel label = new JLabel();
            label.setIcon(icon);
            panel.add(label);
            JScrollPane jsp = new JScrollPane(panel,
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            jtp.addTab(pw, jsp);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     *
     */
    class BlastThread extends Thread {

        @Override
        public void run() {
            enableButtons(false);
            Target t = new Target();
            String e1 = jtf_e1.getText();
            String percent = jtf_percent.getText();

            String d = "D:";

            // http://www.expasy.ch/cgi-bin/lists?pdbtosp.txt

            pdbFile = new File(pdbResourcesDir + "/" + "pdbtosp.txt");

            if (testCheckBox.isSelected()) {
                minOrganisms = 1;
                msaDir = new File(d + "/b-project/data/msaTest/");

                targetFile = new File(Main.targetDir + "/" + "S_aureus.fasta");
                queryFile = new File(Main.queryDir + "/" + Main.jtf_queryFile.getText());

            } else {
                minOrganisms = Integer.parseInt(jtf_minOrganisms.getText());
                msaDir = new File(d + "/b-project/data/msa" + jtf_targetFile.getText() + "/");

                targetFile = new File(Main.targetDir + "/" + Main.jtf_targetFile.getText() + ".fasta");
                queryFile = new File(Main.queryDir + "/" + Main.jtf_queryFile.getText());
            }

            try {
                t.findTargets(e1, percent);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            enableButtons(true);
        }
    }

    /**
     * 
     * @param ae the action event that was performed
     */
    public void actionPerformed(ActionEvent ae) {
        Object ob = ae.getSource();
        //Main.jta3.append("name " + ob.getClass().getName() );

        if ((ob.getClass().getName()).equals("javax.swing.JButton") == false) {
            return;
        }
        JButton btnItem = (JButton) ae.getSource();

        if (btnItem == runButton) {
            new BlastThread().start();

        } else if (btnItem == clearButton) {
            jta1.setText("");
            jta2.setText("");
            jta3.setText("");
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        //dataHome = System.getProperty("user.home") + "/BlastMaster/data/demo/";
        //blastHome = "/usr/bin/";

        String d = "D:";

        queryDir = new File(d + "/b-project/data/B_subtilis/");
        targetDir = new File(d + "/b-project/data/proteomes/");

        pdbResourcesDir = new File(d + "/b-project/data/pdbResources/");

        blastHome = d + "/b-tools/blast/bin/";
        muscleHome = d + "/b-tools/muscle/";
        jalviewHome = d + "/b-tools/jalview/";

        // String userDir = System.getProperty("user.dir");
        // System.out.println(userDir);

        Main m = new Main();
    }
}
