package agents;

import loveletter.Action;
import loveletter.Card;
import loveletter.IllegalActionException;
import loveletter.State;

import java.util.Arrays;
import java.util.Random;

public class Agent22374571 implements loveletter.Agent {
    /**
     * The cards left for all kinds of cards
     */
    private int[] deck; //{0, 5, 2, 2, 2, 2, 1, 1, 1}
    /**
     * The times of our agent to play cards
     */
    private int round;
    /**
     * Index of Player in this round
     */
    private int myIndex;
    /**
     * Saved Game State to Calculate
     */
    private State current;
    /**
     * Array saving the possible card in users hand
     */
    private int[][] hands;
    /**
     * Record the card we seen by acts(like Priest, King)
     */
    private int[] seen; //{SeenWho,SeenWhat}
    private boolean ifSeen; // IF THE CARD SEEN IS STILL ON HIS HAND
    private int seenBaron; // IF WE SEEN BARON

    private Random rand;


    /**
     * Method called at the start of a round
     *
     * @param start the initial state of the round
     **/
    public void newRound(State start) {
        rand = new Random(System.nanoTime());    //  RANDOM SUPPORT
        seen = new int[2];

        current = start;
        //  GET CURRENT PLAYER ID
        myIndex = start.getPlayerIndex();
        round = 0;
        //  RECORD THE REMAIN NUMBER OF EVERY CARDS
        deck = new int[]{0, 5, 2, 2, 2, 2, 1, 1, 1};
        //  EVERYONE CAN HAVE EVERY KIND OF CARDS NOW
        //  ADD ALL CARDS TO THE ESTIMATE HANDS
        int totalPlayer = current.numPlayers();
        hands = new int[totalPlayer][9];
        for (int i = 0; i < totalPlayer; i++) {
            Arrays.fill(hands[i], 1);
            hands[i][0] = 0;
        }
        //  CLEAR VARIABLE
        Arrays.fill(seen, -1);
        ifSeen = false;
        seenBaron = -1;

    }

    /**
     * Reports the agent's name
     */
    public String toString() {
        return "Zhenyu2";
    }

    /**
     * Method called when any agent performs an action.
     *
     * @param act     the action an agent performs
     * @param results the state of play the agent is able to observe.
     **/
    public void see(Action act, State results) {
        if (results.eliminated(results.getPlayerIndex())) return;
        State old = current;
        current = results;
        //  REDUCE THE NUMBER OF CARD IN THE cards ARRAY
        int cardValue = act.card().value();
        seeCard(cardValue);
        //  RECORD THE CARD WE SEEN
        if (cardValue == 1) {
            // DO THINGS IF WE SEE A GUARD PERFORMING
        } else if (cardValue == 3) {

        } else if (cardValue == 5) {
            seeCard(current.getDiscards(act.target()).next().value());
            //  RECORD THE CARD FROM THE TARGET
        }


        //  RECORD MORE INFORMATION IF ACTION DID BY US
        if (act.player() == myIndex) {
            //  RECORD OUR PRIEST SEEN
            if (act.card().value() == 2 && current.getCard(act.target()) != null) {
                seen = new int[]{act.target(), current.getCard(act.target()).value()};
            }
        } else if (act.player() == seen[0]) {//  RECORD IF THE PLAYER IS RECORDED BY OUR PRIEST
            //  IF HE PLAYS THIS CARD, DELETE THE RECORD
            if (act.card().value() == seen[1]) {
                Arrays.fill(seen, -1);
                ifSeen = false;
            } else ifSeen = true;
        } else if (act.target() == seen[0]) {//  RECORD IF THE TARGET IS RECORDED BY OUR PRIEST
            if (act.card().value() == 5) {
                Arrays.fill(seen, -1);
                ifSeen = false;
            } else if (act.card().value() == 6) {// S12 THE CARD SEEN BY US IS EXCHANGED
                seen[0] = act.player();
                ifSeen = true;
            }
        } else if (act.target() == myIndex) { // S23 SEEN AS PRIESTSEEN IF WE'RE TARGET OF KNG
            if (act.card().value() == 6) {
                ifSeen = true;
                seen[0] = act.player();
                seen[1] = old.getCard(myIndex).value();

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
        round++;
        //  GET CARD CURRENTLY HAND
        int hand[] = {c.value(), current.getCard(myIndex).value()};

        //  GET A COPY AFTER GET CURRENT CARD
        int[][] hand_new = hands.clone();
        int[] cards_new = deck.clone();
        //  REMOVE THE CARD WE JUST GET
        cards_new[c.value()]--;
        if (cards_new[c.value()] == 0)
            for (int i = 0; i < current.numPlayers(); i++)
                hand_new[i][c.value()] = 0;

        //  START TO DETERMINE BY STRATEGY
        Action act = null;
        Card play = null;
        //  THE FIRST ROUND STRATEGIES
        //  FOR ROUND ONW
        if (round == 1) {
            if (hand[0] == 4 || hand[1] == 4) return Action.playHandmaid(myIndex);
            else if (hand[0] == 1) {
                if (hand[1] == 5) {
                    return Action.playPrince(myIndex, generateTarget());
                } else if (hand[1] == 4) {
                    return Action.playHandmaid(myIndex);
                } else if (hand[1] == 2) {
                    return Action.playPriest(myIndex, generateTarget());
                } else if (hand[1] == 3) {
                    play = Card.values()[0];
                } else if (hand[1] == 6) {
                    play = Card.values()[0];
                } else if (hand[1] == 7) {
                    play = Card.values()[0];
                }
            } else if (hand[1] == 1) {
                if (hand[0] == 5) {
                    return Action.playPrince(myIndex, generateTarget());
                } else if (hand[0] == 4) {
                    return Action.playHandmaid(myIndex);
                } else if (hand[0] == 2) {
                    return Action.playPriest(myIndex, generateTarget());
                } else if (hand[0] == 3) {
                    play = Card.values()[0];
                } else if (hand[0] == 6) {
                    play = Card.values()[0];
                } else if (hand[0] == 7) {
                    play = Card.values()[0];
                }
            } else if (hand[0] == 3) {
                if (hand[1] >= 5) return Action.playBaron(myIndex, generateTarget());

                else if (hand[1] == 2)
                    return Action.playPriest(myIndex, generateTarget());

            } else if (hand[1] == 3) {
                if (hand[0] >= 5) return Action.playBaron(myIndex, generateTarget());
                else if (hand[0] == 2)
                    return Action.playPriest(myIndex, generateTarget());
            }
        }

        int target = generateTarget();
        int guardTarget = 0;
        int baronTarget = 0;
        int kingPrinceTarget = 0;

        double cardPoint[] = {0.0, 0.0};

        for (int i = 0; i < 2; i++) {
            //  S0 SKIP PRINCESS
            if (hand[i] == 8) {
                cardPoint[i] = -1; // NOT CONSIDERABLE
                continue;
            }
            //  S2 SEEN
            //  IF THE USER STILL HOLD THE CARD SEEN BY US
            if (ifSeen && !current.eliminated(seen[0]) && !current.handmaid(seen[0])) {
                if (hand[i] == 1)
                    if (seen[1] > 1)
                        return Action.playGuard(myIndex, seen[0], Card.values()[seen[1] - 1]);
                if (hand[i] == 3) {
                    if (hand[1 - i] > seen[1]) return Action.playBaron(myIndex, seen[0]);
                } else if (hand[i] == 6) {//  S18 S19 EXCHANGE PRINCESS AND COUNTESS
                    if (seen[1] == 7 && (cards_new[1] / (getSurvive() - 1) < 1))
                        return Action.playKing(myIndex, seen[0]);
                    if (seen[1] == 8 && ((cards_new[1] + cards_new[5]) / (getSurvive() - 1) < 1))
                        return Action.playKing(myIndex, seen[0]);
                } else if (hand[i] == 5) { // S19 PRINCE TO PRINCESS
                    if (seen[1] == 8) return Action.playPrince(myIndex, seen[0]);
                }
                if (seen[1] == 5 & (hand[0] == 8 || hand[1] == 8)) target = seen[0];
            }

            //  S3 HANDMAID FIRST
            if (hand[i] == 4) return Action.playHandmaid(myIndex);

            //  S4 IF HOLDING COUNTESS IN THE FIRST TWO ROUND, PLAY IT
            if (round <= 2 && hand[i] == 7) return Action.playCountess(myIndex);

            //  S5 80% DON'T PLAY GUARD IN THE FIRST TWO ROUNDS
            if (round <= 2 && rand.nextDouble() < 0.8) cardPoint[i] = 0;

            //  S6 THE CARD POINT OF GUARD
            if (hand[i] == 1) {
                double[] cardPossiblity = new double[current.numPlayers()];
                for (int j = 0; j < current.numPlayers(); j++) {
                    if (j == myIndex) continue;
                    if (current.handmaid(j)||current.eliminated(j)) continue;
                    for (int k = 0; k < 9; k++)
                        if (hand_new[j][k] == 1) cardPossiblity[j]++;
                }
                int minPlayer = -1;
                double minHand = 10;
                int times = 0;
                for (int j = 0; j < current.numPlayers(); j++) {
                    if (cardPossiblity[j] == 0) continue;
                    if (cardPossiblity[j] < minHand) {
                        minPlayer = j;
                        minHand = cardPossiblity[j];
                        times++;
                    }
                }
                guardTarget = minPlayer;
                if (times == current.numPlayers() - 1||guardTarget== -1)
                    guardTarget = generateTarget();
                cardPoint[i] = 10 / minHand;
            } else if (hand[i] == 2) {
                if (hand[1 - i] == 1) cardPoint[i] = 9;
                else if (hand[1 - i] == 3) return Action.playPriest(myIndex, generateTarget());
                else if (hand[1 - i] == 2) return Action.playPriest(myIndex, generateTarget());
                else if (hand[1 - i] == 5) cardPoint[i] = 7;
                else if (hand[1 - i] == 6) cardPoint[i] = 8;
                else if (hand[1 - i] == 7) cardPoint[i] = 5;
            } else if (hand[i] == 3) {
                int our = hand[1 - i];
                if (our==1) cardPoint[i] = -1;
                double[] winPossiblity = new double[current.numPlayers()];
                for (int j = 0; j < current.numPlayers(); j++) {
                    if (j == myIndex) continue;
                    if (current.handmaid(j)||current.eliminated(j)) continue;
                    for (int k = 0; k < 9; k++) {
                        if (k > our) break;
                        if (hand_new[j][k] == 1) winPossiblity[j] += cards_new[k];
                    }
                    winPossiblity[j] /= current.deckSize();
                }
                int maxPlayer = -1;
                double maxWin = -1;
                int times = 0;
                for (int j = 0; j < current.numPlayers(); j++) {
                    if (winPossiblity[j] == 0) continue;
                    if (winPossiblity[j] > maxWin) {
                        maxPlayer = j;
                        maxWin = winPossiblity[j];
                        times++;
                    }
                }
                baronTarget = maxPlayer;
                if (times == current.numPlayers() - 1||baronTarget==-1)
                    baronTarget = generateTarget();
                cardPoint[i] = maxWin;

            } else if (hand[i] == 5) {
                if (hand[1-i]==8){
                    kingPrinceTarget = generateTarget();
                    if (current.handmaid(kingPrinceTarget))
                        return Action.playPrince(myIndex,myIndex);
                }
                if (seenBaron != -1){
                    kingPrinceTarget = seenBaron;
                } else kingPrinceTarget = generateTarget();

                cardPoint[i] = 3;

            } else if (hand[i]==6){
                if (seenBaron != -1){
                    kingPrinceTarget = seenBaron;
                } else kingPrinceTarget = generateTarget();
                if (hand[1-i]==1) cardPoint[i] = 0;
                else cardPoint[i] = 2;
                if (round>2&&hand[1-i]==2)  cardPoint[i] = 9;

            } else if (hand[i]==7){
                if (hand[1-i]==5||hand[1-i]==6) return Action.playCountess(myIndex);
                else if (round<=2) cardPoint[i] = 6;
                else cardPoint[i] = 2;
            }

        }

        //  PLAY THE CARD WITH HIGHEST POINT
        if (play==null) {
            if (cardPoint[0] > cardPoint[1]) play = Card.values()[hand[0] - 1];
            else play = Card.values()[hand[1] - 1];
        }

        try {
            switch (play) {
                case GUARD:
                    target = guardTarget;
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
                    if (vaildPlayer(seenBaron)) act = Action.playPriest(myIndex, seenBaron);
                    else act = Action.playPriest(myIndex, generateTarget());
                    break;
                case BARON:
                    act = Action.playBaron(myIndex, baronTarget);
                    break;
                case HANDMAID:
                    act = Action.playHandmaid(myIndex);
                    break;
                case PRINCE:
                    act = Action.playPrince(myIndex, kingPrinceTarget);
                    break;
                case KING:
                    act = Action.playKing(myIndex, kingPrinceTarget);
                    break;
                case COUNTESS:
                    act = Action.playCountess(myIndex);
                    break;
                default:
                    act = null;//never play princess
            }

        } catch (IllegalActionException e) {/*do nothing*/}


        return act;
    }

    /**
     * To calculate a target who is not dead or handmaided
     *
     * @return the target player ID
     */
    private int generateTarget() {
        int target = 0;
        //  CHECK IF EVERYONE IS DIED OR HANDMAIDED
        int number = 0;
        for (int i = 0; i < current.numPlayers(); i++) {
            if (i == myIndex) continue;
            if (current.eliminated(i)) number++;
            else if (current.handmaid(i)) number++;
        }
        if (number == current.numPlayers() - 1) {
            target = myIndex;
            while (target == myIndex||current.eliminated(target))
                target = rand.nextInt(current.numPlayers());

        } else {
            //  CHOOSE THE TARGET AS THE HIGHEST SCORE
            int highmark = -1;
            for (int j = 0; j < current.numPlayers(); j++) {
                if (j == myIndex) continue;
                if (current.eliminated(j)) continue;
                if (current.handmaid(j)) continue;
                if (current.score(j) >= highmark) {
                    target = j;
                    highmark = current.score(j);
                }
            }
        }
        return target;
    }

    private int getSurvive() {
        int result = current.numPlayers();
        for (int i = 0; i < current.numPlayers(); i++)
            if (current.eliminated(i)) result--;
        return result;
    }

    private void seeCard(int cardValue) {
        deck[cardValue]--;
        //  REMOVE THIS CARD FROM THE ESTIMATE HANDS IF THERE'S NO MORE
        if (deck[cardValue] == 0)
            for (int i = 0; i < current.numPlayers(); i++)
                hands[i][cardValue] = 0;
    }

    private boolean vaildPlayer(int i){
        if (i<0||i>=current.numPlayers()) return false;
        if (current.eliminated(i)||current.handmaid(i)) return false;
        return true;
    }
}

