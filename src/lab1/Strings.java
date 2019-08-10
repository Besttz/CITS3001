package lab1;

public class Strings {

    private static void naive(String T, String P, int startPoint) {
        int t = T.length();
        int p = P.length();
        for (int i = 0; i < (t - p + 1); i++) {
            if (T.charAt(i) == P.charAt(0)) {
                boolean found = true;
                for (int j = 1; j < p; j++) {
                    if (T.charAt(i + j) != P.charAt(j)) {
                        found = false;
                        break;
                    }
                }
                if (found) {
                    int index = i + startPoint;
                    System.out.print("Find a match at " + index + "\n");
                }
            }
        }

    }

    private static void rabinKarp(String T, String P) {
        int t = T.length();
        int p = P.length();
        //Calculate the hashcode of P first
        int hcP = 0;
        int bit = 1;
        for (int i = p - 1; i >= 0; i--) {
            hcP += Character.getNumericValue(P.charAt(i)) * bit;
            bit *= 10;
        }
        int hcT = 0;
        //Calculate the hashcode of first pattern of T
        bit = 1;
        for (int i = p - 1; i >= 0; i--) {
            hcT += Character.getNumericValue(T.charAt(i)) * bit;
            bit *= 10;
        }
        //Start the loop of checking
        for (int i = 1; i < (t - p + 1); i++) {
            //Determine if a hashcode is matched
            if (hcT == hcP) {
                naive(T.substring(i - 1, i - 1 + p+1), P, i - 1);
            }
            // Remove the first char
            hcT -= Character.getNumericValue(T.charAt(i - 1)) * (bit / 10);
            // Plus the new value
            hcT *= 10;
            hcT += Character.getNumericValue(T.charAt(i - 1 + p));
        }
        if (hcT == hcP) {
            naive(T.substring(t - p, t - 1+1), P, t - p);
        }
    }

    private static int[] maxMatch(String P) {
        //Generate a new array for the MaxMatch
        int[] result = new int[P.length()];
        //Set the MaxMatch value of the first sub string
        result[0] = 0;
        //Loop to calculate the MaxMatch values
        for (int i = 1; i < result.length; i++) {
            String sub = P.substring(0, i+1);
            int len = sub.length() - 1;
            //Save all the prefix
            String[] pre = new String[len];
            for (int j = 0; j < len; j++) {
                pre[j] = sub.substring(0, j+1);
            }
            //Save all the suffix
            String[] suf = new String[len];
            for (int j = 0; j < len; j++) {
                suf[j] = sub.substring(len - j, len+1);
            }
            //Check the count of equal items
            int count = 0;
            for (int j = 0; j < len; j++) {
                if (pre[j].equals(suf[j])) {
                    if (pre[j].length()>count) count = pre[j].length();
                }
            }
            //Save the count to result
            result[i] = count;
        }
        return result;
    }

    private static void KMP(String T, String P) {
        int[] match = maxMatch(P);
        int p = P.length();
        int j = -1;
        for (int i = 0; i < T.length(); i++) {
            if (T.charAt(i)==P.charAt(j+1)){
                j++;
                if (j == p-1){
                    System.out.print("Find a match at " + (i-p+1) + "\n");
                    j=match[p-1]-1;
                }
            } else if (j != -1) j = match[j] - 1;
        }
    }

    private static void BM(String T, String P) {

    }
    public static void main(String[] args) {
        String T = "mississippi";
        String P = "iss";
        System.out.print("Naive \n");
        naive(T, P, 0);
        System.out.print("RabinKarp \n");
        rabinKarp(T, P);
        System.out.print("KMP \n");
        P = "i";
        KMP(T,P);
    }


}
