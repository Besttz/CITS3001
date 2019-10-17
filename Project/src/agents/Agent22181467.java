package agents;

import loveletter.Action;
import loveletter.Card;
import loveletter.IllegalActionException;
import loveletter.State;

import java.util.Arrays;
import java.util.Random;

public class Agent22181467 implements loveletter.Agent {

    /**
     * This is the array saving the possible card in users hand
     */
    private int[][] hand;
    /**
     * The cards left for all kinds of cards
     */
    private int[] cards;
    /**
     * The times of our agent to play cards
     */
    private int round;
    private int myIndex;
    private State current;

    //  RANDOM SUPPORT
    private Random rand;


    public Agent22181467() {
        rand = new Random();    //  RANDOM SUPPORT
    }

    /**
     * Reports the agent's name
     */
    public String toString() {
        return "Zhenyu";
    }

    /**
     * Method called at the start of a round
     *
     * @param start the initial state of the round
     **/
    public void newRound(State start) {
        current = start;
        //  GET CURRENT PLAYER ID
        myIndex = start.getPlayerIndex();
        round = 0;
        //  RECORD THE REMAIN NUMBER OF EVERY CARDS
        cards = new int[]{0, 5, 2, 2, 2, 2, 1, 1, 1};
        //  EVERYONE CAN HAVE EVERY KIND OF CARDS NOW
        //  ADD ALL CARDS TO THE ESTIMATE HANDS
        int totalPlayer = current.numPlayers();
        hand = new int[totalPlayer][9];
        for (int i = 0; i < totalPlayer; i++) {
            Arrays.fill(hand[i], 1);
            hand[i][0] = 0;
        }
    }

    /**
     * Method called when any agent performs an action.
     *
     * @param act     the action an agent performs
     * @param results the state of play the agent is able to observe.
     **/
    public void see(Action act, State results) {
        if (results.eliminated(results.getPlayerIndex())) return;
        current = results;
        //  REDUCE THE NUMBER OF CARD IN THE cards ARRAY
        int cardValue = act.card().value();
        cards[cardValue]--;
        //  REMOVE THIS CARD FROM THE ESTIMATE HANDS IF THERE'S NO MORE
        if (cards[act.card().value()] == 0)
            for (int i = 0; i < current.numPlayers(); i++)
                hand[i][cardValue] = 0;

    }

    /**
     * Perform an action after drawing a card from the deck
     *
     * @param c the card drawn from the deck
     * @return the action the agent chooses to perform
     * @throws IllegalActionException when the Action produced is not legal.
     */
    public Action playCard(Card c) {
        //  UPDATE THE GAME TIMES
        round++;
        //  GET A COPY AFTER GET CURRENT CARD
        int[][] hand_new = hand.clone();
        int[] cards_new = cards.clone();
        cards_new[c.value()]--;
        if (cards_new[c.value()] == 0)
            for (int i = 0; i < current.numPlayers(); i++)
                hand_new[i][c.value()] = 0;


        Action act = null;

        Card play;


        while (!current.legalAction(act, c)) {
            if (rand.nextDouble() < 0.5) play = c;
            else play = current.getCard(myIndex);
            if (play.value() == 8) continue;
            int target = rand.nextInt(current.numPlayers());
            try {
                switch (play) {
                    case GUARD:
                        //  CHOOSE THE TARGET AS THE HIGHEST SCORE
                        int highmark = -1;
                        for (int i = 0; i < current.numPlayers(); i++) {
                            if (i == myIndex) continue;
                            if (current.eliminated(i)) continue;
                            if (current.handmaid(i)) continue;
                            if (current.score(i) >= highmark) target = i;
                        }
                        //  GUESS FROM THE CARDS WHICH HAS TWO
                        int guessCard = 0;
                        for (int i = 2; i < 6; i++) {
                            if (cards_new[i]==2) guessCard =i;
                        }
                        while (hand_new[target][guessCard]==0) guessCard = rand.nextInt(7) + 1;
                        act = Action.playGuard(myIndex, target, Card.values()[guessCard-1]);

                        break;
                    case PRIEST:
                        act = Action.playPriest(myIndex, target);
                        break;
                    case BARON:
                        act = Action.playBaron(myIndex, target);
                        break;
                    case HANDMAID:
                        act = Action.playHandmaid(myIndex);
                        break;
                    case PRINCE:
                        act = Action.playPrince(myIndex, target);
                        break;
                    case KING:
                        act = Action.playKing(myIndex, target);
                        break;
                    case COUNTESS:
                        act = Action.playCountess(myIndex);
                        break;
                    default:
                        act = null;//never play princess
                }
            } catch (IllegalActionException e) {/*do nothing*/}
        }
        return act;
    }
}
