/*The Player class stores important player details including their identifier
(name), bankroll, bet size, and their hands.
*/

import java.util.ArrayList;
import java.util.Scanner;
import java.util.*;

public class Player{
  int player_number;
  double bankroll;
  int bet;
  int allowed_doubles;
  ArrayList<Hand> hands = new ArrayList<Hand>();
  Deck deck;
  boolean is_busted = false;
  String get_hand;

  public Player(int player_number){
    this.player_number = player_number;
  }

  public String getName(){
    return Integer.toString(player_number);
  }

  public void setBank(int buyin){
    bankroll = buyin;
  }

  public double getBank(){
    return bankroll;
  }

  public int getBet(){
    return bet;
  }

  public void setBet(int bet){
    this.bet = bet;
    this.allowed_doubles = (int) bankroll/bet - 1;
  }

  public void initHand(Hand hand){ //clears the hands arraylist and starts anew
    hands.removeAll(hands);
    hands.add(hand);
    readHand(hand);
  }

  public void addHand(Hand hand){
    hands.add(hand);
  }

  public void addHand(int index, Hand hand){
    hands.add(index, hand);
  }

  public ArrayList<Hand> getHand(){
    return hands;
  }

  public void readHand(Hand hand){
    get_hand = "";
    for (Card c : hand.getHand()){
      get_hand += c.getName() + ", ";
    }
    System.out.print("Player " + player_number + " hand: ");
    System.out.println(get_hand + " for a total of " + hand.getScore() + ".");
  }

  public int getAllowedDoubles(){
    return allowed_doubles;
  }

  public void useDouble(){
    allowed_doubles--;
  }

  public boolean isOut(){
    for(Hand h : hands){
      if(!h.isBusted()){
        return false;
      }
    }
    return true;
  }

  public void win(){
    bankroll += bet;
  }

  public void lose(){
    bankroll -= bet;
  }

  public void blackjack(){
    bankroll += 1.5 * bet;
  }
}
