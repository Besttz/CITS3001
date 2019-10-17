package agents;

import loveletter.Action;
import loveletter.Card;
import loveletter.IllegalActionException;
import loveletter.State;

import java.util.ArrayList;

public class Agent22181467 implements loveletter.Agent {

    /**
     * This is the array saving the possible card in users hand
     */
    private ArrayList<Integer>[] hand;
    /**
     * The cards left for all kinds of cards
     */
    private int[] cards;
    /**
     * The times of our agent to play cards
     */
    private int round;
    private int index;
    private State current;


    public Agent22181467() {
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
        index = start.getPlayerIndex();
        hand = new ArrayList[current.numPlayers()];
        index = start.getPlayerIndex();
        round = 0;
        cards = new int[]{5, 2, 2, 2, 2, 1, 1, 1};
        for (int i = 1; i <= 8; i++) hand[0].add(i);
        for (int i = 1; i < current.numPlayers(); i++)
            hand[i] = (ArrayList<Integer>) hand[0].clone();
    }

    /**
     * Method called when any agent performs an action.
     *
     * @param act     the action an agent performs
     * @param results the state of play the agent is able to observe.
     **/
    public void see(Action act, State results) {
        if (results.eliminated(results.getPlayerIndex())) return;

    }

    /**
     * Perform an action after drawing a card from the deck
     *
     * @param c the card drawn from the deck
     * @return the action the agent chooses to perform
     * @throws IllegalActionException when the Action produced is not legal.
     */
    public Action playCard(Card c) {
        return null;
    }
}
