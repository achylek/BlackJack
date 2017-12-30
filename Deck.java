/* Constructs and works with the deck, which is a collection of card objects.
The deck can contain any multiple of 52-card decks, and each card will appear
[# of decks] times. There is only one deck; thus, it is static.

The deck object is only ever manipulated when it is shuffled. A single, static
integer index is increased by one as cards are drawn and selects which card
should be returned. When the index traverses 80% of the deck, a flag is thrown
indicating the deck should be reshuffled.
*/
import java.util.ArrayList;
import java.util.Random;

public class Deck{
  public static ArrayList<Card> deck;
  int number_of_decks;
  public static int top_card_index;
  public static boolean card_marker = false;
  int count;
  ArrayList<Card> used_cards = new ArrayList<Card>();
  Card top_card;

  public Deck(int multiplier){
    number_of_decks = multiplier;
    deck = new ArrayList<Card>();
    for(int i = 0; i < 52 * multiplier + 1; i++){
      this.deck.add(new Card(i));
    }
  }


/*The shuffle method makes use of Java's Random class. A count is kept
as cards are added into the deck. A used-card list keeps track of which cards
have been added, and if Random generates a card that already exists in the deck,
the loop is exited and the process is tried again. When all the existing cards
in used_cards have been checked, our random card is appended to the deck. The
top card index is reset at the end of the process.
*/
  public void shuffle(){
    count = 0;
    Random randomGenerator = new Random();

    while (count < 52 * number_of_decks){
      int randomInt = randomGenerator.nextInt(52 * number_of_decks);

      for (int j = 0; j < count + 1; j++){ //iterate through our used numbers
        if (j == count){
          used_cards.add(count, new Card(randomInt));
          deck.set(count, new Card(randomInt));
          count++;
          break;
        }
        else if (used_cards.get(j).getCardID() == randomInt) {
        break;
        }
      }
    }
    top_card_index = 0;
    card_marker = false;
    used_cards.clear();
  }

  public Hand dealerDeal(){
    Card card1 = draw();
    Card card2 = draw();
    card2.setFaceDown();
    Hand dealer_hand = new Hand(card1, card2);
    System.out.println("Dealer shows " + card1.getName());
    return dealer_hand;
  }

  public Hand playerDeal(){
    Card card1 = draw();
    Card card2 = draw();
    Hand player_hand = new Hand(card1, card2);
    return player_hand;
  }

  public Card draw(){
    top_card = deck.get(top_card_index);
    increaseTopCardIndex();
    return top_card;
  }

  public void increaseTopCardIndex(){
    top_card_index++;
    if (top_card_index >= 0.8 * deck.size() && card_marker == false){
      card_marker = true;
      System.out.println("Card marker has been reached.");
    }
  }

  public boolean getMarker(){
    return card_marker;
  }

  public Card getTopCard(){
    return deck.get(top_card_index);
  }
}
