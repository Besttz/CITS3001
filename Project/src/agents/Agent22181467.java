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
        rand = new Random(System.nanoTime());    //  RANDOM SUPPORT
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
        rand = new Random(System.nanoTime());    //  RANDOM SUPPORT
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
            if (act.card().value() == 2&&current.getCard(act.target()).value()!=-1){
                priestSeen = new int[]{act.target(),current.getCard(act.target()).value() };
            }
        } else if (act.player() == priestSeen[0]) {//  RECORD IF THE PLAYER IS RECORDED BY OUR PRIEST
            //  IF HE PLAYS THIS CARD, DELETE THE RECORD
            if (act.card().value() == priestSeen[1]) {
                Arrays.fill(priestSeen, -1);
                priestFound = false;
            } else priestFound = true;
        } else if (act.target() == priestSeen[0]) {//  RECORD IF THE TARGET IS RECORDED BY OUR PRIEST
            if (act.card().value() == 5) {
                Arrays.fill(priestSeen, -1);
                priestFound = false;
            } else if (act.card().value() == 6){// S12 THE CARD SEEN BY US IS EXCHANGED
                priestSeen[0] = act.player();
                priestFound = true;
            }
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
            //  GENERATE RANDOM CARD TO USE
            if (rand.nextDouble() < 0.5) play = Card.values()[hand[0] - 1];
            else play = Card.values()[hand[1] - 1];

            int target = rand.nextInt(current.numPlayers());
            //  PRIORITY 0 SKIP PRINCESS
            if (play.value() == 8) continue;

            //  PRIORITY 2 PRIEST FOUND
            //  IF THE USER STILL HOLD THE CARD SEEN BY US
            if (priestFound && !current.eliminated(priestSeen[0])&&!current.handmaid(priestSeen[0])) {
                if (hand[0] == 1 || hand[1] == 1)
                    if (priestSeen[1] > 1)
                        return Action.playGuard(myIndex, priestSeen[0], Card.values()[priestSeen[1] - 1]);
                if (hand[0] == 3) {
                    if (hand[1] > priestSeen[1]) return Action.playBaron(myIndex, priestSeen[0]);
                } else if (hand[1] == 3) {
                    if (hand[0] > priestSeen[1]) return Action.playBaron(myIndex, priestSeen[0]);
                }
            }

            //  S9 IF THE GAME IS ENDING SOON, USE GUARD IF WE HAVE
            if (current.deckSize()/current.numPlayers()<=2){
                if (hand[0]==1||hand[1]==1) play=Card.values()[0];
            }

            //  PRIORITY 4 USE HANDMAID
            if (hand[0] == 4) play = Card.values()[hand[0] - 1];
            if (hand[1] == 4) play = Card.values()[hand[1] - 1];

            //  S08

            //  PRIORITY 5 75% DON'T USE GUARD IN THE FIRST TWO ROUND
            if (round <= 2 && play.value() == 1 && rand.nextDouble() < 0.8)
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
                        //  S6 S7 GUESS FROM THE CARDS WHICH HAS TWO
                        int guessCard = 0;
                        if (cards_new[4] == 2 && hand_new[target][4] == 1) guessCard = 4;
                        if (cards_new[2] == 2 && hand_new[target][2] == 1) guessCard = 2;
                        if (cards_new[5] == 2 && hand_new[target][5] == 1) guessCard = 5;
                        if (cards_new[3] == 2 && hand_new[target][3] == 1) guessCard = 3;

                        if (guessCard == 0) { // NO CARD STILL HAVE 2 LEFT
                            if (cards_new[4] == 1 && hand_new[target][4] == 1) guessCard = 4;
                            if (cards_new[2] == 1 && hand_new[target][2] == 1) guessCard = 2;
                            if (cards_new[5] == 1 && hand_new[target][5] == 1) guessCard = 5;
                            if (cards_new[6] == 1 && hand_new[target][6] == 1) guessCard = 6;
                            if (cards_new[7] == 1 && hand_new[target][7] == 1) guessCard = 7;
                            if (cards_new[3] == 1 && hand_new[target][3] == 1) guessCard = 3;
                            if (cards_new[8] == 1 && hand_new[target][8] == 1) guessCard = 8;
                        }
                        act = Action.playGuard(myIndex, target, Card.values()[guessCard - 1]);

                        break;
                    case PRIEST:
                        act = Action.playPriest(myIndex, target);
                        break;
                    case BARON:
                        //  S13 Don't use Baron if we're hoding 1
                        if (hand[0]==1||hand[1]==1) continue;
                        act = Action.playBaron(myIndex, target);
                        break;
                    case HANDMAID:
                        act = Action.playHandmaid(myIndex);
                        break;
                    case PRINCE:
                        //  S14 Don't play this to myself if I'm holding princess
                        if ((hand[0]==8||hand[1]==8)&&target==myIndex) continue;
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
