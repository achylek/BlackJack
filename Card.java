/* The class to define a card: Cards are defined by their index, one card for
each number. Each deck will contain 52 numbers.

The technique used to organize and arrive at card properties follows:
Card numbers mod 13 will return card ranks, and card #'s partitioned in groups
of size 13 will maintain the same suit, and the pattern can continue to
infiniti.
*/

public class Card{
  int card_identity;
  boolean is_face_up;
  int simple_index;
  final static String[] VALUES = new String[]{"ACE", "2", "3", "4", "5", "6",
                                "7", "8", "9", "10", "JACK", "QUEEN", "KING"};
  final static String[] SUITS = new String[]{"CLUBS", "DIAMONDS", "HEARTS",
                                "SPADES"};
  public Card(int index){
    this.card_identity = index;
    this.is_face_up = true;
  }

  public int getCardID(){
    return card_identity;
  }

  public String getRank(){
    return VALUES[card_identity % 13];
  }

  public String getName(){
    if (is_face_up == true){
      return VALUES[card_identity % 13] + " of " + SUITS[card_identity / 13 % 4];
    }
    else {return "<face down>";}
  }

  public int getScoreValue(){
    simple_index = card_identity % 13;
    if (simple_index == 0) {//we have an ace
      return 11;
    }
    else if (simple_index == 10 || simple_index == 11 || simple_index == 12) {
      return 10;
    }
    else {
      return Integer.parseInt(VALUES[simple_index]);
    }
  }

  public void setFaceDown(){
    is_face_up = false;
  }

  public void setFaceUp(){
    is_face_up = true;
  }
}
