package lab1;
import java.util.Arrays;
import java.util.Random;

public class Sorter {

    /** Prints error message and terminates program */
    public static void pperr(String msg) {
        System.out.println("Error:");
        System.out.println(msg);
        System.exit(1);
    }

    private static int[] randomArray(int length){
        Random rand = new Random();
        int[] result = new int[length];
        for (int i = 0; i < length; i++) {
            result[i] = rand.nextInt(50);;
        }
        return result;
    }

    public static void main(String[] args) {
        //Generate new array
        int[] nums = randomArray(10);
        System.out.println("Generate New Array");
        //Print Array Elements
        for (int i = 0; i < nums.length; i++) {
            System.out.print(nums[i]+" ");
        }
        System.out.println();
        //Clone array for sorting
        int[] a1 = nums.clone();
        int[] a2 = nums.clone();
        int[] a3 = nums.clone();
        //Start to Insert Sort
        Sorter  test = new Sorter();
        long startTime = System.currentTimeMillis();
        test.insertSort(a1);
        long endTime = System.currentTimeMillis();
        System.out.println("Total execution time: " + (endTime - startTime));
        System.out.println("After insertSort");
        for (int i = 0; i < a1.length; i++) {
            System.out.print(a1[i]+" ");
        }
        //Start to Merge Sort
        test.mergeSort(a2);
        System.out.println("After mergeSort");
        for (int i = 0; i < a2.length; i++) {
            System.out.print(a2[i]+" ");
        }

        //Start to Radix Sort
        long startTime1 = System.currentTimeMillis();
        test.radixSort(a3);
        long endTime1 = System.currentTimeMillis();
        System.out.println("Total execution time: " + (endTime1 - startTime1));

        System.out.println("After radixSort");
        for (int i = 0; i < a3.length; i++) {
            System.out.print(a3[i]+" ");
        }
        //Test if it is in ascend order
        for (int i = 1; i < a1.length; i++) {
            if (a1[i-1]>a1[i]) pperr("insertSort Failed");
        }
        //Test if it has the same elements as the original array
        if (a1.length!=nums.length) pperr("insertSort Failed");
        for (int num:nums) {
            if(Arrays.binarySearch(a1,num)<0) pperr("insertSort Failed");
        }

    }

    private void insertSort(int[] nums) {
        for (int i = 1; i < nums.length; i++) {
            int key = nums[i];
            int j = i - 1;
            while (j > -1 && nums[j] > key) {
                nums[j + 1] = nums[j];
                j--;
            }
            nums[j + 1] = key;
        }
    }

    private void mergeSort(int[] a, int p, int r){
        if (p<r) {
            int q= (p+r)/2;
            mergeSort(a,p,q);
            mergeSort(a,q+1,r);
            merge(a,p,q,r);
        }
    }

    private void mergeSort(int[] a){
        mergeSort(a,0,a.length-1);
    }

    private void merge(int[] a, int p, int q, int r){
        int n = q-p+1;
        int m = r-q;
        int[] left = new int[n];
        int[] right = new int[m];
        for (int i = 0; i < n; i++) {
            left[i] = a[i+p];
        }
        for (int i = 0; i < m; i++) {
            right[i] = a[i+q+1];
        }
        int i = 0;
        int j = 0;
        for (int k = p; k <= r; k++) {
            if(i==n) a[k] = right[j++];
            else if(j==m || left[i]<right[j]) a[k] = left[i++];
            else a[k] = right[j++];
        }

    }

    private int maxBit(int[] a){
        //Find the max number
        int max = a[0];
        for (int i:a) if (i > max) max = i;
        int result = 1;
        //Find the bit
        while (max>=10){
            max/=10;
            result++;
        }
        return result;
    }
    private void radixSort(int[] aa) {
        int bit = maxBit(aa);
        //Start the Count Sort for every bit
        //Use CountSort for every bit
        int radix = 1;
        int[] a = aa;
        for (int i = 0; i < bit; i++) {
            int[] count = new int[11];
            Arrays.fill(count,0);
            int[] output = new int[a.length];
            Arrays.fill(output,0);

            //Count every key
            for (int j = 0; j < a.length; j++) {
                count[(a[j] / radix % 10)+1]++;
            }
            //Add the count to calculate position for every key
            for (int j = 1; j < 10; j++) {
                count[j] += count[j - 1];
            }
            //Put sorted nums to new array
            for (int j = 0; j < a.length; j++) {
                output[count[a[j] / radix % 10]] = a[j];
                count[a[j] / radix % 10]++;
            }
            radix *= 10;
            a = output;
        }
        // Use a new variable to solve Java parameter reference problem 这点好烦呀
        for (int i = 0; i < a.length; i++) {
            aa[i] = a[i];
        }

    }
}
