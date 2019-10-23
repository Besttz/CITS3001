package agents;
import loveletter.*;
import java.util.*;

/**
 * An interface for representing an agent in the game Love Letter
 * All agent's must have a 0 parameter constructor
 * */
public class KBA implements Agent{

  private Random rand;
  private State current;
  private int myIndex;
  private ArrayList<World> KB; 
  private Map<Integer,Stack> aaa; // link the player and their discard
  private int turn;
  private float[] bbb; // for possibility of each card in current
  private Card[] unplayed;
  private double num_state;
  private int[] seat;
  private int i = 0;

  //0 place default constructor
  public KBA(){
	  rand =  new Random();
	  this.KB = new ArrayList<World>(); 
	  this.turn = 0;
	  this.aaa = new HashMap<Integer,Stack>();
	  this.bbb = new float[8];
	  this.num_state = 0;
	  this.seat = new int[3];
  }
  
  
  private class Player{
	  private int seat;
	  private int posCard;
	  public Player(int a, int b) {
		  this.seat = a;
		  this.posCard = b;
	  }
  }
  
  private class World{
	  private Player one;
	  private Player two;
	  private Player three;
	  private double possibility;
	  
	  public World(Player a, Player b, Player c, double d) {
		  this.one = a;
		  this.two = b;
		  this.three = c;
		  this.possibility = d;
	  }
  }
  
  public void Collect() {
	  unplayed = current.unseenCards();
	  int num = current.deckSize() + current.numPlayers();
	  for(int i = 0; i < unplayed.length; i++) {
		  bbb[unplayed[i].value()-1] ++;
	  }
	  for(int i = 0; i < bbb.length; i++) {
		  bbb[i] = bbb[i]/num;
	  }
  }
  
  public World getPossibleWorld() {
	  int a = rand.nextInt(unplayed.length);
	  int b = rand.nextInt(unplayed.length);
	  int c = rand.nextInt(unplayed.length);
	  while(a == b || a == c || b == c) {
		  a = rand.nextInt(unplayed.length);
		  b = rand.nextInt(unplayed.length);
		  c = rand.nextInt(unplayed.length);
	  }
	  
	  int one = unplayed[a].value();
	  int two = unplayed[b].value();
	  int three = unplayed[c].value();
	  
	  
	  
	  
	  Player pone = new Player(seat[0],one);
	  Player ptwo = new Player(seat[1],two);
	  Player pthree = new Player(seat[2],three);
	  
	  
	  int length = unplayed.length;
	  int size = (length*(length-1)*(length-2))/(3*2*1);
	  num_state = 100/size;
	  World world = new World(pone,ptwo,pthree,num_state);
	  
	  return world;
  }
  
  public void getList() {
	  for(int i = 0; i < 56; i++) {
		  World w = getPossibleWorld();
		  KB.add(w);
	  }
  }
  
  public double reward(Card c) {
	  double reward = 0;
	  for(int i = 0; i < KB.size(); i++) {
		  World w = KB.get(i);
		  double num = compare(c,w);
		  reward += num;
	  }
	  return reward;
  }
  
  
  public double compare(Card c, World w) {
	  double score = 0;
	  if(c.value() == 1 && (w.one.posCard != 1 || w.two.posCard != 1 || w.three.posCard != 1)) {
		  score += 1f;
	  }
	  
	  if(c.value() == 2) {
		  score += 0.7f;
	  }
	  
	  if(c.value() == 3 && ((w.one.posCard == 2 || w.one.posCard == 1) || (w.two.posCard == 2 || w.two.posCard == 1) || (w.three.posCard == 2 || w.three.posCard == 1))) {
		  score += 1;
	  }
	  
	  if(c.value() == 4 && (w.one.posCard != 1 || w.two.posCard != 1 || w.three.posCard != 1)) {
		  score += 1f;
	  }
	  else {
		  score += 0.4f;
	  }
	  
	  if(c.value() == 5 && (w.one.posCard == 8 || w.two.posCard == 8 || w.three.posCard == 8)) {
		  score += 1f;
	  }
	  
	  if(c.value() == 6 && (w.one.posCard == 4 || w.two.posCard == 4 || w.three.posCard == 4)) {
		  score += 1f;
	  }
	  
	  if(c.value() == 7 || c.value() == 8) {
		  // do nothing
	  }
	   score = score*w.possibility;
	   return score;
  }
  

  
  /**
   * Reports the agents name
   * */
  public String toString(){return "KBA";}


  /**
   * Method called at the start of a round
   * @param start the starting state of the round
   **/
  public void newRound(State start){
    current = start;
    myIndex = current.getPlayerIndex();
  }

  /**
   * Method called when any agent performs an action. 
   * @param act the action an agent performs
   * @param results the state of play the agent is able to observe.
   * **/
  public void see(Action act, State results){
    current = results;
    turn++;
    
    if(turn <= 3) {
    	if(act.player() != myIndex) {
    		Stack pos = new Stack<Card>();
    		pos.add(act.card());
    		aaa.put(act.player(), pos);
    	}
    	if(turn%current.numPlayers() != myIndex) {
    		seat[i] = turn;
    		i++;
    	}
    }/*
    else {
    	Stack temp = aaa.get(act.player());
    	temp.add(act.card());
    	aaa.put(act.player(), temp);
    }*/
    
    
  }
  
  

  
  public int findTarget(Card a) {
	  int target = -1;
	  ArrayList list = new ArrayList<World>();
	  double max = 0;
	  for(int i = 0; i < KB.size(); i++) {
		  World w = KB.get(i);
		  if(w.possibility > max) {
			  max = w.possibility;
		  }
	  }
	  for(int i = 0; i < KB.size(); i++) {
		  if(KB.get(i).possibility == max) {
			  list.add(KB.get(i));
		  }
	  }
	  
      World random_world = (World)list.get(rand.nextInt(list.size()));
      int low = 3;
      int middle = 0;
      int high = 0;
      if(random_world.one.posCard <= random_world.two.posCard) {
    	  if(random_world.three.posCard <= random_world.one.posCard) {
    		  low = random_world.three.seat;
    		  high = random_world.two.seat;
    		  middle = random_world.one.seat;
    	  }
    	  if(random_world.three.posCard > random_world.two.posCard) {
    		  low = random_world.one.seat;
    		  high = random_world.three.seat;
    		  middle = random_world.two.seat;
    	  }
      }
      else {
    	  if(random_world.three.posCard <= random_world.two.posCard) {
    		  low = random_world.three.seat;
    		  high = random_world.one.seat;
    		  middle = random_world.two.seat;
    	  }
    	  if(random_world.three.posCard > random_world.one.posCard) {
    		  low = random_world.two.seat;
    		  high = random_world.three.seat;
    		  middle = random_world.one.seat;
    	  }
      }
      
      if(a.value() > 4) {
    	  if(!current.eliminated(low) && !current.handmaid(low)) {
    		  target = low;
    	  }
    	  else if(!current.eliminated(middle) && !current.handmaid(middle)) {
    		  target = middle;
    	  }
    	  else if(!current.eliminated(high) && !current.handmaid(high)){
    		  target = high;
    	  }
      }
      else {
    	  if(!current.eliminated(high)&& !current.handmaid(high)) {
    		  target = high;
    	  }
    	  else if(!current.eliminated(middle) && !current.handmaid(middle)) {
    		  target = middle;
    	  }
    	  else if(!current.eliminated(low) && !current.handmaid(low)){
    		  target = low;
    	  }
      }
      
	  
	  
	  /*
	  int[] score = new int[current.numPlayers()];
	  int max = -1;
	  int target = -1;
	  if(a.value() != 4) {
		  for(int i = 0; i < current.numPlayers(); i++) {
			  if(!current.eliminated(i) && !current.handmaid(i)) {
				  if(i != myIndex && current.score(i) > max) {
					  max = current.score(i);
					  target = i;  
				  }
			  }
		  }  
	  }
	  else {
		  target = myIndex;
	  }*/
	  
	  return target;
  }
  
  
  
  
  public Card PlayCard(Card a, Card b, double c, double d) {
	  if(a.value() == 8) {
		  return b;
	  }
	  else if(b.value() == 8) {
		  return a;
	  }
	  else if(a.value() == 7 && b.value() > 4) {
		  return a;
	  }
	  else if(b.value() == 7 && a.value() > 4) {
		  return b;
	  }
	  else {
		  if(c <= d) {
			  return a;
		  }
		  else {
			  return b;
		  }
	  }
  }
  

  public void clear() {
	  bbb = new float[8];
	  KB.clear();
  }

  /**
   * Perform an action after drawing a card from the deck
   * @param c the card drawn from the deck
   * @return the action the agent chooses to perform
   * @throws IllegalActionException when the Action produced is not legal.
   * */
  public Action playCard(Card c){
	  Collect();
	  getList();
    Action act = null;
    Card play;
    double c_reward = reward(c);
    double hand_reward = reward(current.getCard(myIndex));
    while(!current.legalAction(act, c)){
      play = PlayCard(c,current.getCard(myIndex),c_reward,hand_reward);
      int target = findTarget(play);
      if(target == -1) target = rand.nextInt(current.numPlayers());
      try{
        switch(play){
          case GUARD:
            act = Action.playGuard(myIndex, target, Card.values()[rand.nextInt(7)+1]);
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
      }catch(IllegalActionException e){/*do nothing*/}
    }
    clear();
    return act;
  }
}
