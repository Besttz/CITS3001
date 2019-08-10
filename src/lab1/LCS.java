package lab1;

import java.util.Arrays;

public class LCS {
    private int[][] record;
    private char[][] arrows;
    private String X;
    private String Y;
    private int xLength;
    private int yLength;

    public LCS(String X, String Y) {
        this.X = X;
        this.Y = Y;
        xLength = X.length();
        yLength = Y.length();
        record = new int[xLength + 1][yLength + 1];
        arrows = new char[xLength + 1][yLength + 1];
        for (int i = 0; i < xLength + 1; i++) {
            Arrays.fill(record[i], -1);
        }
    }

    void dynamic() {
        dynamic(xLength, yLength);
        System.out.println("Length of LCS: " + record[xLength][yLength]);
        print();
    }

    private int dynamic(int i, int j) {
        //Return the value if it's not -1, otherwise calculate the value
        if (record[i][j] != -1) {
            return record[i][j];
        } else {
            if (i == 0 || j == 0) {
                record[i][j] = 0;
                return 0;
            } else if (X.charAt(i - 1) == Y.charAt(j - 1)) {
                record[i][j] = dynamic((i - 1), (j - 1)) + 1;
                arrows[i][j] = '/';
                return record[i][j];
            } else {
                if (dynamic((i - 1), (j))>dynamic((i), (j - 1))){
                    record[i][j] = record[i-1][j];
                    arrows[i][j] = '|';
                } else {
                    record[i][j] = record[i][j-1];
                    arrows[i][j] = '-';
                }
                return record[i][j];
            }
        }
    }

    private void print(){
        int length = record[xLength][yLength];
        String result = "";
        int i = xLength;
        int j = yLength;
        int count = 0;
        while (count<length){
            if (arrows[i][j]=='|') i--;
            else if (arrows[i][j]=='-') j--;
            else {
                result = X.charAt(i-1) + result;
                i--;j--; count++;
            }
        }
        System.out.println("LCS:"+result);
    }
}
