/*The dealer is a special type of player with slightly different fields. Dealer
play is automatized and plays according to a pre-determined set of rules.
*/

import java.util.concurrent.TimeUnit;

public class Dealer{
  Deck deck;
  int dealer_score;
  Hand dealer_hand;
  boolean live_action;

  public Dealer(Hand dealer_hand, Deck deck, boolean live_action){
    this.live_action = live_action;
    this.dealer_hand = dealer_hand;
    this.deck = deck;
    dealerPlay();
  }

  public void dealerPlay(){
    dealer_hand.getCard(1).setFaceUp();
    System.out.println("Dealer reveals bottom card: " + dealer_hand.getCard(1).getName() + ".");
    //try{TimeUnit.SECONDS.sleep(2);} catch (InterruptedException ex){}
    while(live_action){
      System.out.println("Dealer's hand: " + dealer_hand.readHand() + ".");
      dealer_score = dealer_hand.getScore();
      if (dealer_score < 17){
        System.out.println("Dealer hits!");
        //try{TimeUnit.SECONDS.sleep(2);} catch (InterruptedException ex){}
        dealer_hand.addCard(deck.draw());
      }
      else if (dealer_score >= 17 && dealer_score <= 21){
        System.out.println("Dealer stands! Score: " + Integer.toString(getScore()));
        //try{TimeUnit.SECONDS.sleep(2);} catch (InterruptedException ex){}
        break;
      }
      else if (dealer_score > 21){
        System.out.println("Dealer busts!");
        //try{TimeUnit.SECONDS.sleep(2);} catch (InterruptedException ex){}
        break;
      }
    }
  }

  public int getScore(){
    return dealer_score;
  }
}
