package lab2;

public class Tester {

    public static void main(String[] args) {
        KnapsackImp test = new KnapsackImp();
        int[] w = {2, 3, 5, 7, 1, 4, 1};
        int[] v = {10, 5, 15, 7, 6, 18, 3};
        System.out.println(test.fractionalKnapsack(w, v, 15));
        //The right answer should be 55
        int[] vv ={1,2,5,6};
        int[] ww = {2,3,4,5};
        System.out.println(test.discreteKnapsack(ww,vv,8));
        //The right answer should be 8
    }
}
