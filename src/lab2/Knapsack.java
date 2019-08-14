package lab2;
/**
 * A class for representing implementations of the Knapsack problem.
 * Created for CITS3001 at the University of Western Australia.
 * @author Tim French.
 **/

public interface Knapsack
{

    /**
     * Implements the fractional knapsack problem.
     * The value returned should be the maximum
     * value of a combination of products with the given value and
     * weight that can fit ain a knapsack with the given capacity.
     * The products are divisible so a fraction of each maybe taken.
     * The returned value should be the greatest integer
     * less than or equal to the maximum value.
     * The arrays are assumed to be of equal size, and all non-negative values.
     * @param weights the array of weights of each type of product available.
     * @param values the array of values of each type of product available.
     * @param capacity the size of the knapsack
     * @return the greatest int less than or equal to the maximum possible value of the knapsack.
     **/
    public int fractionalKnapsack(int[] weights, int[] values, int capacity);


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
     * @param weights the array of weights of each type of product available.
     * @param values the array of values of each type of product available.
     * @param capacity the size of the knapsack
     * @return the maximum possible value of the knapsack.
     **/
    public int discreteKnapsack(int[] weights, int[] values, int capacity);

}