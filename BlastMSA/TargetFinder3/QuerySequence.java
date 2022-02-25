package target;

import java.util.*;


/**
 *  >sp|Q2FHK6|ACP_STAA3 Acyl carrier protein OS=Staphylococcus aureus (strain USA300) GN=acpP
 *
 * @author peter-425
 */
public class QuerySequence extends Sequence{

    ArrayList<Hit> hitList;     // list of hit objects
    HashSet<String> targetAccNumberSet;    // set of target uniprot accession numbers
    HashSet<String> speciesSet;  // set of target species with at least 1 hit

    int osHitCount;

    public QuerySequence(String fastaLine) {

        super(fastaLine);

        hitList = new ArrayList<Hit>();
        targetAccNumberSet = new HashSet<String>();
        speciesSet = new HashSet<String>();

    }

    public void add(Hit h) {

        hitList.add(h);
        targetAccNumberSet.add(h.targetACC);

        if (!speciesSet.contains(h.targetOS)) {

            ++osHitCount;
             speciesSet.add(h.targetOS);
        }
    }

    public double getMeanPercentIdentity() {

        double sum = 0;

        for (Hit h : hitList) {
            sum += h.identity;
            //sb.append(h.queryACC + ", " + h.queryName + ", " + h.targetACC +
            // ", " + h.targetName + ", " + h.e_val + ", " + h.identity + "\n");
        }
        if (hitList.size() > 0) {
            return sum / hitList.size();
        } else {
            return 0;
        }
    }
    public String getHits() {

        StringBuffer sb = new StringBuffer();

        for (Hit h : hitList) {
            sb.append(h.queryACC + ", " + h.queryName + ", " + h.targetACC + ", " + h.targetName + ", " + h.e_val + ", " + h.identity + "\n");
        }
        return sb.toString();
    }
}
