//package lab6;

public class MancalaImp implements MancalaAgent {

    /**
     * Allows the agent to nominate the house the agent would like to move seeds from.
     * The agent will always have control of houses 0-5 with store at 6.
     * Any move other than 0-5 will result in a forfeit.
     * An move from an empty house will result in a forfeit.
     * A legal move will always be available.
     * Assume your agent has 0.5 seconds to make a move.
     *
     * @param board the current state of the game.
     *              The board is an int array of length 14, indicating the 12 houses and 2 stores.
     *              The agent's house are 0-5 and their store is 6. The opponents houses are 7-12 and their store is 13. Board[i] is the number of seeds in house (store) i.
     *              board[(i+1}%14] is the next house (store) anticlockwise from board[i].
     *              This will be consistent between moves of a normal game so the agent can maintain a strategy space.
     * @return the house the agent would like to move the seeds from this turn.
     */
    public int move(int[] board) {
        return findBest(board, 0, true)[0];
    }

    /**
     * This function returns the best move and its unity from a board
     *
     * @param board the current game board
     * @return a array which the first element is the best move and the
     * second one is the unity for best move
     */
    public int[] findBest(int[] board, int recursiveTime, boolean isMe) {
//        if (recursiveTime>5) return new int[]{3, board[6]};
        int[] marks = {0, 0, 0, 0, 0, 0};
//        int[] maxMarks = {0, 0, 0, 0, 0, 0};
        int best = -99; // FOR A-B TRIMMING
        int bestMove = -1;

        //CHECK GAME STATUS
        //FIRST I NEED TO DECIDE WHICH MOVE IS LEGAL TO AVOID USELESS CALCULATION
        int unMove = 0;
        int moveable = -1;
        for (int i = 0; i < 6; i++) {
            if (board[i] == 0) {
                marks[i] = -100;

                unMove++;

            } else {
                moveable = i;
            }
        }
        if (unMove == 6) {
            if (isMe) return new int[]{0, board[6]};
            else return new int[]{0, board[13]};
        }
        if (isMe && recursiveTime == 0 && unMove >= 4) {
            for (int i = 0; i < 6; i++) {
                if (board[i] != 0) {
                    return new int[]{i, 0};
                }
            }
        }
//        if (unMove == 5) {
//            for (int i = 0; i < 6; i++) {
//                if (board[i] != 0) {
//                    if (isMe)return new int[]{i, board[6]};
//                    else return new int[]{i, board[13]};
//                }
//            }
//        }
        if (board[3] == 3 && board[6] == 0) return new int[]{3, 3}; //FOR THE FIRST ROUND
        if (board[3] == 0 && board[6] == 1 && board[9] != 0) return new int[]{1, 3}; //FOR THE FIRST ROUND


        //THEN CALCULATE IF THIS MOVE WILL FALLEN INTO THE STORAGE PLACE
        //IF NOT, CALCULATE HOW MANY POINTS CAN BE GET THIS ROUND
        //THEN SIMULATE THE NEXT PLAYER
        //UNTIL FEW SIMULATES RETURN THE BEST MOVE AND THE STORAGE AT THAT TIME
        //CHOOSE THE BEST STORAGE AND RETURN
        //GET THE FINAL UNITY OF THIS MOVE
        //HOWEVER IF THIS ONE FALLS INTO THE STORAGE PLACE REPEAT THE CALCULATION
        //BASED ON THE NEW BOARD INFORMATION TO GET THE UNITY

        //AFTER CALCULATING THE UNITY OF ONE MOVE
        //IF IT'S LARGER THAN BEST, RECORDED
        //THEN IN THE NEXT TIME IF THE MARKS GET IS SMALLER THEN JUST SKIP IT
        int[] finalScore = {0, 0, 0, 0, 0, 0};
        int meScore = 0;
        for (int i = 0; i < 6; i++) {
            if (unMove == 5 & moveable != i) continue;
            if (marks[i] == -100) continue; //SKIP UNNECESSARY CALCULATION
//            marks[i] = 0;
            if (board[i] == 6 - i) {

                int[] newBoard = board.clone();
                newBoard[i] = 0;
                for (int j = 1; j <= board[i]; j++) newBoard[(i + j) % 14]++;
                finalScore[i] = findBest(newBoard, recursiveTime++, isMe)[1];
//                maxMarks[i] = marks[i];

            } else { //CALCULATE HOW MANY POINTS GET
                //Get the new board and
                int[] newBoard = board.clone();
                newBoard[i] = 0;
                for (int j = 1; j <= board[i]; j++) {
                    //check if it past the storage (+1)
//                    if ((i + j) == 6) marks[i]++;
                    newBoard[(i + j) % 14]++;
                }
                //and if the final destination is empty
                int dest = (i + board[i]) % 14;
                if (dest < 6 && board[dest] == 0) {
                    //Get the marks from the opposite hole
//                    marks[i] += board[12 - i];
                    //Clear the opposite hole
                    newBoard[12 - dest] = 0;
                    newBoard[6] += board[12 - i];
                }
//                maxMarks[i] = marks[i];
                //CHECK IF IT'S LARGER THAN CURRENT BEST
                //CONTINUE CALCULATE IF SO, OTHERWISE SKIP
//                if (maxMarks[i] > best) {

                if (isMe) {
                    //change the board into the next play's view
                    int[] nextBoard = new int[14];
                    for (int j = 0; j < 7; j++) nextBoard[j] = newBoard[j + 7];
                    for (int j = 7; j < 14; j++) nextBoard[j] = newBoard[j - 7];
                    //calculate the best unity of him
                    //Our unity minus other unity is the final unity
                    finalScore[i] = findBest(nextBoard, recursiveTime++, false)[1];
                } else {
                    finalScore[i] = newBoard[6];
                }
                //CHECK IF THE FINAL MARK IS THE BEST
                if (finalScore[i] > best) {
                    meScore = newBoard[13];
                    best = finalScore[i];
                    bestMove = i;
                }

//                } else if () {

//                } else {
////                    continue;
//                    marks[i] = -90+maxMarks[i];
//                }
            }

        }

//
//        if (recursiveTime == 6){
//            return new int[]{bestMove, best};
//        }
//        if (bestMove == -1) {
//            best = 0;
//            for (int i = 0; i < 6; i++) {
//                if (board[i] != 0 && finalScore[i] > best) {
//                    best = finalScore[i];
//                    meScore = board[13];
//                    bestMove = i;
//                }
//            }
//        }
//        CHECK IF IT'S ILLEGAL
//        if (bestMove==-1 || board[bestMove] == 0) {
//            for (int i = 0; i < 6; i++) {
//                if (board[i]!=0) return new int[]{i,marks[i]};
//            }
//        }
        if (isMe) return new int[]{bestMove, best};
        else return new int[]{bestMove, meScore};
    }

    /**
     * The agents name.
     *
     * @return a hardcoded string, the name of the agent.
     */
    public String name() {
        return "Tommy";
    }

    /**
     * A method to reset the agent for a new game.
     */
    public void reset() {

    }
}
