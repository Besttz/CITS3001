package lab1;

public class Main {

    public static void main(String[] args){
        String T = "01101001";
        String P = "110110";
        System.out.print("Finding LCS \n");
        LCS test = new LCS(T,P);
        test.dynamic();
    }

}
