package lab4;

/**
 * An interface to wrap the word-chess search algorithm.
 * This class is for use in CITS3001 at the University of Western Australia
 * @author Tim French
 **/


public interface WordChess{

    /**
     * Finds a shortest sequence of words in the dictionary such that the first word is the startWord,
     * the last word is the endWord, and each word is equal to the previous word with one letter changed.
     * All words in the sequence are the same length. If no sequence is possible, an empty array is returned.
     * It is assumed that both startWord and endWord are elements of the dictionary.
     * @param dictionary The set of words that can be used in the sequence; all words in the dictionary are capitalised.
     * @param startWord the first word on the sequence.
     * @param endWord the last word in the sequence.
     * @return an array containing a shortest sequence from startWord to endWord, in order,
     * using only words from the dictionary that differ by a single character.
     * */
    public String[] findPath(String[] dictionary, String startWord, String endWord);


}

