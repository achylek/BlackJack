/* Stewards the players' possession of cards obtained from the deck.
Players may hold more than one Hand.
*/
import java.util.ArrayList;
import java.util.Scanner;
import java.util.*;

public class Hand{
  ArrayList<Card> hand;
  boolean is_dealer = false;
  int score;
  int ace_bank;
  int number_of_aces;
  String get_hand;

  public Hand(Card card1, Card card2){

    hand = new ArrayList<Card>();
    hand.add(card1);
    hand.add(card2);
  }

  public ArrayList<Card> getHand(){
    return hand;
  }

  public int getScore(){
    score = 0;
    ace_bank = getNumberofAces();
    for (int i = 0; i < hand.size(); i++){
      score += hand.get(i).getScoreValue();
    }
    while (score > 21 && ace_bank > 0){
      score -=10;
      ace_bank--;
    }
    return score;
  }

  public int getNumberofAces(){
    number_of_aces = 0;
    for (int i = 0; i < hand.size(); i++){
      if (hand.get(i).getRank().equals("ACE")){
        number_of_aces++;
      }
    }
    return number_of_aces;
  }

  public String readHand(){
    get_hand = "";
    for (int i = 0; i < hand.size() - 1; i++){
      get_hand += hand.get(i).getName() + ", ";
    }
    get_hand += hand.get(hand.size() - 1).getName();
    return get_hand + " for a total of " + Integer.toString(getScore());
  }

  public Card getCard(int which_card){
    return this.hand.get(which_card);
  }

 public boolean splitPossible(){
   if (hand.size() == 2 && hand.get(0).getScoreValue() == hand.get(1).getScoreValue()){
     return true;
   }
   return false;
 }

 public void addCard(Card card){
   hand.add(card);
 }

 public void removeCard(int i){
   hand.remove(i);
 }

 public int getSize(){
   return hand.size();
 }

 public boolean isBlackJack(){
   boolean is_blackjack = getScore() == 21;
   return is_blackjack;
 }

 public boolean isBusted(){

   if (getScore() > 21){
     return true;
   }
   return false;
 }
}
