package target;

import java.util.*; 

/**
 *
 * @author 
 */
public class Sort {

    public static ArrayList sortHashSet(HashSet hs) {

        ArrayList<String> al = new ArrayList<String>();

        Iterator it = hs.iterator();
        while (it.hasNext()) {
            al.add((String)it.next());
        }
        Collections.sort(al);
        return al;
    }

    public static ArrayList sortHashMap1(HashMap hm) {

        ArrayList<QuerySequence> a = new ArrayList<QuerySequence>();

        Set keys = hm.keySet();
        for (Iterator it = keys.iterator(); it.hasNext();) {

            QuerySequence s = (QuerySequence) hm.get(it.next());

            boolean success = false;
            for (int i = 0; i < a.size(); ++i) {

                if (s.osHitCount > a.get(i).osHitCount) {
                    a.add(i, s);
                    success = true;
                    break;
                } 
                else if (s.osHitCount == a.get(i).osHitCount &&  s.hitList.size() < a.get(i).hitList.size()   ) {
                    a.add(i, s);
                    success = true;
                    break;
                }         
                else if (s.osHitCount == a.get(i).osHitCount &&
                         s.hitList.size() == a.get(i).hitList.size() &&
                         s.getMeanPercentIdentity() >= a.get(i).getMeanPercentIdentity())
                {
                    a.add(i, s);
                    success = true;
                    break;
                }
            }
            if (success == false) {
                a.add(a.size(), s);  // first time, or smallest
            }
        }
        return a;
    }

    public static ArrayList sortHashMap2(HashMap hm) {

        ArrayList<Pair> a = new ArrayList<Pair>();

        Set keys = hm.keySet();
        for (Iterator it = keys.iterator(); it.hasNext();) {
            String k = (String) it.next();
            int c = ((Integer) hm.get(k)).intValue();

            boolean success = false;
            for (int i = 0; i < a.size(); ++i) {
                Pair p2 = a.get(i);
                if (c > p2.count) {
                    a.add(i, new Pair(k, c));
                    success = true;
                    break;
                }
            }
            if (success == false) {
                a.add(a.size(), new Pair(k, c));  // first time, or smallest
            }
        }
        return a;
    }
}
