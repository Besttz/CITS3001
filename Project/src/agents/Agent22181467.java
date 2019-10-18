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
    private int[][] hands;
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

    //  RECORD PRIEST
    private int[] priestSeen; //{SeenWho,SeenWhat}
    boolean priestFound; // IF THE CARD PRIEST FOUND IS STILL ON HIS HAND


    public Agent22181467() {
        rand = new Random();    //  RANDOM SUPPORT
        priestSeen = new int[2];
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
        hands = new int[totalPlayer][9];
        for (int i = 0; i < totalPlayer; i++) {
            Arrays.fill(hands[i], 1);
            hands[i][0] = 0;
        }
        //  CLEAR VARIABLE
        Arrays.fill(priestSeen, -1);
        priestFound = false;
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
                hands[i][cardValue] = 0;
        //  RECORD MORE INFORMATION IF ACTION DID BY US
        if (act.player() == myIndex) {
            //  RECORD OUR PRIEST SEEN
            if (act.card().value() == 2)
                priestSeen = new int[]{act.target(), current.getCard(act.target()).value()};
        } else if (act.player() == priestSeen[0]) {//  RECORD IF THE PLAYER IS RECORDED BY OUR PRIEST
            //  IF HE PLAYS THIS CARD, DELETE THE RECORD
            if (act.card().value() == priestSeen[1]) Arrays.fill(priestSeen, -1);
            else priestFound = true;
        } else if (act.target()== priestSeen[0]){//  RECORD IF THE TARGET IS RECORDED BY OUR PRIEST
            if (act.card().value() ==5 ||act.card().value() == 6) Arrays.fill(priestSeen, -1);
        }


    }

    /**
     * Perform an action after drawing a card from the deck
     *
     * @param c the card drawn from the deck
     * @return the action the agent chooses to perform
     * @throws IllegalActionException when the Action produced is not legal.
     */
    public Action playCard(Card c) throws IllegalActionException {
        //  UPDATE THE GAME TIMES
        round++;
        //  GET A COPY AFTER GET CURRENT CARD
        int[][] hand_new = hands.clone();
        int[] cards_new = cards.clone();
        cards_new[c.value()]--;
        if (cards_new[c.value()] == 0)
            for (int i = 0; i < current.numPlayers(); i++)
                hand_new[i][c.value()] = 0;

        Action act = null;
        Card play;
        int hand[] = {c.value(), current.getCard(myIndex).value()};

        while (!current.legalAction(act, c)) {
            if (rand.nextDouble() < 0.5) play = Card.values()[hand[0] - 1];
            else play = Card.values()[hand[1] - 1];
            int target = rand.nextInt(current.numPlayers());
            //  PRIORITY 0 SKIP PRINCESS
            if (play.value() == 8) continue;
            //  PRIORITY 2 PRIEST FOUND
            //  IF THE USER STILL HOLD THE CARD SEEN BY US
            if (priestFound&&!current.eliminated(priestSeen[0])) {
                if (hand[0] == 1 || hand[1] == 1)
                    if (priestSeen[1]>1)
                        return Action.playGuard(myIndex, priestSeen[0], Card.values()[priestSeen[1] - 1]);
                if (hand[0] == 3) {
                    if (hand[1] > priestSeen[1]) return Action.playBaron(myIndex, priestSeen[0]);
                } else if (hand[1] == 3) {
                    if (hand[0] > priestSeen[1]) return Action.playBaron(myIndex, priestSeen[0]);
                }
            }
            //  PRIORITY 4 USE HANDMAID
            if (hand[0] == 4) play = Card.values()[hand[0] - 1];
            if (hand[1] == 4) play = Card.values()[hand[1] - 1];
            //  PRIORITY 5 75% DON'T USE GUARD IN THE FIRST TWO ROUND
            if (round<=2&&play.value()==1&&rand.nextDouble() < 0.75)
                continue;
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
                            if (cards_new[i] == 2) guessCard = i;
                        }
                        while (hand_new[target][guessCard] == 0) guessCard = rand.nextInt(7) + 1;
                        act = Action.playGuard(myIndex, target, Card.values()[guessCard - 1]);

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
