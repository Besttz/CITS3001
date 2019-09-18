package lab2;

import java.util.Arrays;

/**
 * A class for the Knapsack problem.
 *
 * @author Tommy Zhang 22181467
 **/
public class KnapsackImp implements Knapsack {
    /**
     * Implements the fractional knapsack problem.
     * The value returned should be the maximum
     * value of a combination of products with the given value and
     * weight that can fit ain a knapsack with the given capacity.
     * The products are divisible so a fraction of each maybe taken.
     * The returned value should be the greatest integer
     * less than or equal to the maximum value.
     * The arrays are assumed to be of equal size, and all non-negative values.
     *
     * @param weights  the array of weights of each type of product available.
     * @param values   the array of values of each type of product available.
     * @param capacity the size of the knapsack
     * @return the greatest int less than or equal to the maximum possible value of the knapsack.
     **/
    public int fractionalKnapsack(int[] weights, int[] values, int capacity) {
        //Calculate the Value-Weight Ratio
        int items = weights.length;
        double result = 0.0;
        double[] ratio = new double[items];
        int remainedCapa = capacity;
        for (int i = 0; i < items; i++) {
            ratio[i] = (double) values[i] / (double) weights[i];
        }
        //Make the ratio sorted
        double[] sorted = ratio.clone();
        mergeSort(sorted); // From small to great
        //Start to pack
        for (int i = 0; i < items; i++) {
            //Find out the most valuable item
            double currentValue = sorted[items - 1 - i];//Because sorted is from small to big
            int indexOfItem = -1;
            for (int j = 0; j < items; j++) {
                if (ratio[j] == currentValue) {
                    indexOfItem = j;
                    break;
                }
            }
            //Check the rest capacity
            int weight = weights[indexOfItem];
            if (remainedCapa > weight) {
                //If the remained capacity is greater, then put this item in
                remainedCapa -= weight;
                result += (double) values[indexOfItem];
                ratio[indexOfItem] = 0; //Set the value of it equals 0
            } else {
                result += ratio[indexOfItem] * remainedCapa;
                return (int) result;
            }
        }
        return 0;
    }

    /**
     * Implements the 0-1 knapsack problem.
     * The value returned should be the maximum
     * value of a combination of products with the given value and
     * weight that can fit ain a knapsack with the given capacity.
     * The products are not divisible so each must be wholly included,
     * or entirely left out.
     * The returned value should be the maximum value it is possible
     * to include in the knapsack.
     * The arrays are assumed to be of equal size, and all non-negative values.
     *
     * @param weights  the array of weights of each type of product available.
     * @param values   the array of values of each type of product available.
     * @param capacity the size of the knapsack
     * @return the maximum possible value of the knapsack.
     **/
    public int discreteKnapsack(int[] weights, int[] values, int capacity) {
        int items = weights.length;
        int[][] results = new int[items + 1][capacity + 1];
        int result = 0;
        Arrays.fill(results[0], 0);
        for (int i = 1; i <= items; i++) {
            results[i][0] = 0;
            for (int j = 1; j <= capacity; j++) {
                int last = results[i - 1][j];
                int neww = 0;
                if (j - weights[i - 1] >= 0) neww += values[i - 1] + results[i - 1][j - weights[i - 1]];
                if (last > neww) {
                    results[i][j] = last;
                } else {
                    results[i][j] = neww;
                }
                if (results[i][j] > result) result = results[i][j];
            }
        }
        return result;
    }

    /**
     * mergeSort algorithm to cut the array into pieces then merge them
     *
     * @param a array to sort
     * @param p start point to sort
     * @param r end point to sort
     */
    private void mergeSort(double[] a, int p, int r) {
        if (p < r) {
            int q = (p + r) / 2;
            mergeSort(a, p, q);
            mergeSort(a, q + 1, r);
            merge(a, p, q, r);
        }
    }

    /**
     * mergeSort algorithm written in lab 0
     *
     * @param a the array to sort
     */
    private void mergeSort(double[] a) {
        mergeSort(a, 0, a.length - 1);
    }

    /**
     * mergeSort algorithm to merge the cut sub arrays
     *
     * @param a array to sort
     * @param p start point to sort
     * @param q middle point of two suarray
     * @param r end point to sort
     */
    private void merge(double[] a, int p, int q, int r) {
        int n = q - p + 1;
        int m = r - q;
        double[] left = new double[n];
        double[] right = new double[m];
        for (int i = 0; i < n; i++) {
            left[i] = a[i + p];
        }
        for (int i = 0; i < m; i++) {
            right[i] = a[i + q + 1];
        }
        int i = 0;
        int j = 0;
        for (int k = p; k <= r; k++) {
            if (i == n) a[k] = right[j++];
            else if (j == m || left[i] < right[j]) a[k] = left[i++];
            else a[k] = right[j++];
        }

    }
}
