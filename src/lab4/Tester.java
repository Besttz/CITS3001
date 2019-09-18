package lab4;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

//Download from GitHub
// https://github.com/TasSM/WordChess/blob/2a8881245c93fb8b1fc8684aa1d4e48fcfffdc38/WordChessTest.java
public class Tester {


    private static String[] importWords(String infile) throws IOException {

        FileReader fr = new FileReader(infile);
        BufferedReader bf = new BufferedReader(fr);
        List<String> lines = new ArrayList<String>();

        String line = bf.readLine();

        while (line != null) {
            lines.add(line);
            line = bf.readLine();
        }
        bf.close();

        // Convert to String[]
        String[] output = lines.toArray(new String[]{});

        // System.out.println(Arrays.toString(output));

        return output;

    }

    public static void main(String[] args) throws IOException {
        String startWord = "SICK";
        String endWord = "WELL";

        String startWord1 = "PEEPING";
        String endWord1 = "BUCKLED";

        String startWord2 = "TENTS";
        String endWord2 = "BADLY";

        String startWord3 = "VENDS";
        String endWord3 = "STONE";

        // String startWord3 = "ASTER";
        // String endWord3 = "WIRES";

        String infile = "/Users/Tommy/IdeaProjects/CITS3001/src/lab4/corncob_caps.txt";
        String[] dictionary = importWords(infile);


        WordChessImp WC = new WordChessImp();

        String[] output = WC.findPath(dictionary, startWord, endWord);
        System.out.println(Arrays.toString(output));

        String[] output1 = WC.findPath(dictionary, startWord1, endWord1);
        System.out.println(Arrays.toString(output1));

        String[] output2 = WC.findPath(dictionary, startWord2, endWord2);
        System.out.println(Arrays.toString(output2));

        String[] output3 = WC.findPath(dictionary, startWord3, endWord3);
        System.out.println(Arrays.toString(output3));



    }
}