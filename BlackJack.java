/*
This runs a game of BlackJack. The program allows for six players, multiple
decks, and realistic gameplay (including splits and doubles). Dealer hits to 17,
and BlackJack pays 3:2. The game will continue until all players go bankrupt.

Various pauses are commented out throughout the code in the BlackJack and Dealer
java files. These can be uncommented to aid in fluidity when using this program
in the command line.
*/

import java.util.concurrent.TimeUnit;
import java.util.*;

public class BlackJack{
  Deck deck;
  int number_of_decks;
  Hand dealer_hand;
  boolean is_dealer_play; //returns false if all players bust
  int number_of_opponents;
  ArrayList<Hand> hands = new ArrayList<Hand>(); //useful place-holder
  LinkedList<Player> players;
  boolean split;
  boolean dealer_kill;
  boolean valid;
  boolean dealer_busted;
  int bank; //used to move money
  int next_bet;
  int handIterator;
  int dealer_score;
  String move; //hit, stand, split, or double
  Scanner input = new Scanner(System.in);

  public BlackJack(){
    setUpGame();
    shuffle();
    while (isgameOn()){
      playersPlay();
      //try{TimeUnit.SECONDS.sleep(1);} catch (InterruptedException iex){}
      Dealer dealer = new Dealer(dealer_hand, deck, is_dealer_play);
      //try{TimeUnit.SECONDS.sleep(1);} catch (InterruptedException iex){}
      winCondition();
    }
  }

/*
setUpGame sets up a deck, introduces players to the table with their initial
bankrolls, and initializes appropriate data structures for play. User input
is used to define parameters pertaining to the game structure.
*/
  public void setUpGame(){
    players = new LinkedList<Player>();

    do {
      System.out.println("How many decks in the shoe? (Enter 2-8) ");

      try{number_of_decks = input.nextInt();
      valid = number_of_decks < 9 && number_of_decks > 1;}
      catch(InputMismatchException immex){
        System.out.println("Error. Please enter valid values.");
        valid = false;
        //try{TimeUnit.SECONDS.sleep(1);} catch (InterruptedException iex){}
        input.nextLine();
        continue;}
      if(!valid){System.out.println("Error. Please enter valid values.");}
    }while(!valid);
    deck = new Deck(number_of_decks);

    do {
      System.out.println("How many opponents vs dealer? (Enter 1-6) ");
      try{number_of_opponents = input.nextInt();
        valid = number_of_opponents < 7 && number_of_opponents > 0;}
      catch(InputMismatchException immex){
        System.out.println("Error. Please enter valid values.");
        valid = false;
        //try{TimeUnit.SECONDS.sleep(1);} catch (InterruptedException iex){}
        input.nextLine();
        continue;}
      if (!valid){System.out.println("Error. Please enter valid values.");}
        //try{TimeUnit.SECONDS.sleep(1);} catch (InterruptedException iex){}
    }while(!valid);

    Player new_p; //players are added to the table
    for(int i = 0; i < number_of_opponents; i++){
      new_p = new Player(i+1);
      players.add(new_p);
    }

/* loops through players asking them to deposit an inital bankroll.
*/
    for (Player p : players){
      do{
        System.out.print("Player " + p.getName());
        System.out.print(" Please add to your bank. Amount: ");
        try{bank = input.nextInt();
        valid = bank > 0;}
        catch(InputMismatchException immex){
          System.out.println("Error. Please enter a valid value.");
          valid = false;
          //try{TimeUnit.SECONDS.sleep(1);} catch (InterruptedException iex){}
          input.nextLine();
          continue;
        }
        if(!valid){
          System.out.println("Error. Please enter a valid value.");
          //try{TimeUnit.SECONDS.sleep(1);} catch (InterruptedException iex){}
        }
        else{p.setBank(bank);}
      }while (!valid);
    }
  }


  public void shuffle(){
    System.out.println("Cupid Shuffling... ");
    deck.shuffle();
  }

/*
Takes players' bets and deals a hand. A dealer's hand is dealt and
players are taken through their hand.
*/
  public void playersPlay(){
    for (Player p : players) {
      valid = true;
      do{
          System.out.print("Player " + p.getName() + " Please place your bet (1-" + (int) p.getBank() + "): ");
          try{next_bet = input.nextInt();
          valid = next_bet > 0 && next_bet <= p.getBank();}
          catch(InputMismatchException immex){
            System.out.println("Error. Please enter a valid value.");
            //try{TimeUnit.SECONDS.sleep(1);} catch (InterruptedException iex){}
            valid = false;
            input.nextLine();
            continue;
          }
          if(!valid){
            System.out.println("Error. Please enter an appropriate bet.");
            //try{TimeUnit.SECONDS.sleep(1);} catch (InterruptedException iex){}
          }
          else{p.setBet(next_bet);}
      } while (!valid);
    }

    System.out.println("Dealing... ");
    //try{TimeUnit.SECONDS.sleep(1);} catch (InterruptedException iex){}
    dealer_hand = deck.dealerDeal();
    //try{TimeUnit.SECONDS.sleep(2);} catch (InterruptedException iex){}

    for (Player p : players){
      p.initHand(deck.playerDeal());
    }
    System.out.println();
    //try{TimeUnit.SECONDS.sleep(2);} catch (InterruptedException iex){}
    dealer_kill = false;
    if (dealer_hand.getCard(0).getRank().equals("ACE")){
      System.out.println("Checking bottom dealer card....");
      if (dealer_hand.getScore() == 21){
        dealer_kill = true;
        return;
      }
    }

/* Iteration through players, iteratiion through hands of eache player, and
gameplay.
*/
    for (Player p : players){
      hands = p.getHand();
      handIterator = 0;
      valid = true;
      while(valid){
        Hand h = hands.get(handIterator);
        if (h.isBlackJack()){
          if(h.getSize() == 2){
            p.readHand(h);
            System.out.println("Player " + p.getName() + ", you have Blackjack!");
            handIterator++;
          }
          else{
            p.readHand(h);
            handIterator++;
          }
          //try{TimeUnit.SECONDS.sleep(1);} catch (InterruptedException ex){}
        }
        split = false;
        while (!h.isBlackJack()){
          move = "";
          //try{TimeUnit.SECONDS.sleep(1);} catch (InterruptedException ex){}
          p.readHand(h);
          //try{TimeUnit.SECONDS.sleep(1);} catch (InterruptedException ex){}
          if (h.isBusted()) {
            System.out.println("You are busted!");
            handIterator++;
            break;
          }
          else if (h.splitPossible() && !(p.getAllowedDoubles() == 0)) {
            System.out.println("HIT, STAND, SPLIT, or DOUBLE? ");
            move = input.next().toUpperCase().replaceAll("//s","");
          }
          else if (h.getSize() < 3 && !(p.getAllowedDoubles() == 0)) {
            System.out.println("HIT, STAND, or DOUBLE? ");
            move = input.next().toUpperCase().replaceAll("//s","");
          }
          else {
            System.out.println("HIT or STAND?");
            move = input.next().toUpperCase().replaceAll("//s","");
          }
          if (move.equals("HIT")) {
            h.addCard(deck.draw());
          }
          else if (move.equals("STAND")) {
            handIterator++;
            break;
          }
          else if (move.equals("DOUBLE")) {
            h.addCard(deck.draw());
            p.useDouble();
            p.addHand(handIterator, h);
            handIterator += 2;
            System.out.println("Player " + p.getName() + " stakes another " + p.getBet());
            System.out.println("Player " + p.getName() + " hand: " + h.readHand());
            break;
          }

          else if (move.equals("SPLIT")) {
            Card split_card = h.getCard(1);
            h.removeCard(1);
            h.addCard(deck.draw());
            p.addHand(new Hand(split_card, deck.draw()));
            System.out.println("After size = " + hands.size());
            p.useDouble();
            split = true;
          }
        }
        valid = handIterator < hands.size();
      }
    }
  this.is_dealer_play = isDealerplay();
}

/*
Each player is found to win, lose, or draw.
*/
  public void winCondition(){
    dealer_score = dealer_hand.getScore();
    dealer_busted = dealer_score > 21;

    ListIterator litr = players.listIterator();
    while(litr.hasNext()){
      Object playerobj = litr.next();
      Player p = Player.class.cast(playerobj);

      hands = p.getHand();
      for (Hand h : hands){
        if (h.getScore() == 21 && h.getSize() == 2 && dealer_score != 21){
          p.blackjack();
          System.out.println("Player " + p.getName() + " wins " + p.getBet()*1.5 + ".");
        }
        else if (h.getScore() < dealer_score && !dealer_busted || h.isBusted()){
          p.lose();
          System.out.println("Player " + p.getName() + " loses " + p.getBet() + ".");
        }
        else if (h.getScore() == dealer_score && !dealer_busted){
          System.out.println("Player " + p.getName() + " draws.");
        }
        else if (h.getScore() > dealer_score && !h.isBusted() || dealer_busted && !h.isBusted()){
          p.win();
          System.out.println("Player " + p.getName() + " wins " + p.getBet() + ".");
        }
      }
      if (p.getBank() < 1){
        litr.remove();
        System.out.println("Player " + p.getName() + " is bankrupt.");
      }
    }
  }

/*
Checks the % deck usage each round and continues the game if players still have
a live bankroll.
*/
  public boolean isgameOn(){
    if (players.isEmpty()){
      System.out.println("Game Over!");
      return false;
    }
    if (deck.getMarker()){
      System.out.println("Deck has run out 80%. Reshuffling and replacing marker...");
      shuffle();
      //try {TimeUnit.SECONDS.sleep(1);} catch (InterruptedException iex){}
    }
    return true;
  }

/*
If everyone busts, the dealer does not draw.
*/
  public boolean isDealerplay(){
    for (Player p : players){
      if (!p.isOut()){
        return true;
      }
    }
    return false;
  }

  public static void main(String[] args){
    System.out.println("~*|*~ Welcome to BlackJack! Dealer hits to 17, BlackJack pays"
    + " 3:2. No insurance. ~*|*~");
    BlackJack game = new BlackJack();
    }
  }
