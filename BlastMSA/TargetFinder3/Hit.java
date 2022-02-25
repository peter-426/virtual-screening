package target;

/**
 *
 * @author peter-426
 */
public class Hit {

    String queryACC;  // this data is from Blast output
    String queryName;

    String targetACC;
    String targetName;
    String targetOS;
    double identity;
    double e_val;

    


    public Hit(String q1, String q2, String OS, String s1, String s2, double e, double p){

        queryACC    = q1;
        queryName   = q2;

        targetOS   = OS;
      
        targetACC  = s1;
        targetName = s2;

        e_val  = e;
        identity = p;
    }

}
