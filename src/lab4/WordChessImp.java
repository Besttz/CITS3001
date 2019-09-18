package lab4;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * The class for implement of WordChess
 */
public class WordChessImp implements WordChess {

    /**
     * Check if a word is inside the dictionary
     * @param word the word to check
     * @param dict the dictionary to search
     * @return true if the word is in the dictionary
     */
    private boolean checkDict(String word, String[] dict){
        int a = 0;
        int b = dict.length-1;
        while (a<=b) {
            int m = (a+b)/2;
            if (word.compareTo(dict[m]) == 0) return true;
            else if (word.compareTo(dict[m])>0) a = m+1;
            else b = m-1;
        }
        return false;
    }
    /**
     * Finds a shortest sequence of words in the dictionary such that the first word is the startWord,
     * the last word is the endWord, and each word is equal to the previous word with one letter changed.
     * All words in the sequence are the same length. If no sequence is possible, an empty array is returned.
     * It is assumed that both startWord and endWord are elements of the dictionary.
     *
     * @param dictionary The set of words that can be used in the sequence; all words in the dictionary are capitalised.
     * @param startWord  the first word on the sequence.
     * @param endWord    the last word in the sequence.
     * @return an array containing a shortest sequence from startWord to endWord, in order,
     * using only words from the dictionary that differ by a single character.
     */
    public String[] findPath(String[] dictionary, String startWord, String endWord) {
        // Use A queue to save all the words to check and use BFS
        LinkedList<String> queue = new LinkedList<>();
        queue.addFirst(startWord);
        // Use an ArrayList to save the words visited
        ArrayList<String> visited = new ArrayList<>();
        // Use an ArrayList to save the parents of the words (in visited)
        ArrayList<Integer> parent = new ArrayList<>();
        parent.add(-1);
        int index = 0;
        String current;
        boolean founded = false;
        while (!queue.isEmpty()){
            current = queue.removeFirst(); //Remove the first word
            int currentI = visited.size(); // Get index for saving parent
            visited.add(current);
            //Check all available words and save to the arrayList
            for (int i = 0; i < current.length(); i++) {
                //Replace from the first letter
                for (char j = 'A'; j <= 'Z'; j++) {
                    char[] neww = current.toCharArray();
                    neww[i] = j;
                    String newWord = String.valueOf(neww);
                    //Check if the new word is a valid word
                    if (checkDict(newWord,dictionary)){
                        //Check if visited or in queue
                        if (visited.contains(newWord)||queue.contains(newWord)) continue;
                        //Add into queue
                        queue.addLast(newWord);
                        //Record parents
                        parent.add(currentI);
                        //Check if equals to end
                        if (newWord.compareTo(endWord)==0) {
                            founded = true;
                            break;
                        }
                    }
                }
                if (founded) break;
            }
            if (founded) break;
        }
        if (founded){
            LinkedList<Integer> stack = new LinkedList<>();
            stack.addFirst(parent.get(parent.size()-1));
            while (parent.get(stack.getFirst())!=-1){
                stack.addFirst(parent.get(stack.getFirst()));
            }
            int size = stack.size();
            String result[] = new String[size+1];
            result[0] = startWord;
            stack.removeFirst();
            for (int i = 1; i <size; i++) {
                result[i] = visited.get(stack.removeFirst());
            }
            result[size] = endWord;
            return result;
        }
        return new String[0];
    }
}
