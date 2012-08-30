package reuze.demo;
//import needed classes and packages
import java.util.Scanner;
import java.text.NumberFormat;
import java.io.IOException;
import java.util.Locale;
import java.text.DecimalFormat;


public class demoWar
{public static void main(String[] args) throws IOException
 {
 //Declare Variables
 //Creates the demoCards based on their suite
 demoCard heartdemoCard;
 demoCard diamonddemoCard;
 demoCard spadedemoCard;
 demoCard clubdemoCard;
 int countingPlays = 0;


 Scanner keyboard = new Scanner(System.in); //Allows Input

 //creates the CardPile array called DeckOfdemoCards
 CardPile deckOfdemoCards = new CardPile();

 //Creates Player1's demoCard Pile
 CardPile player1Pile = new CardPile();

 //Creates Player2's demoCard Pile
 CardPile player2Pile = new CardPile();

 //Creates the demoCards to fill the array (1-14 of hearts/diamonds/spades/clubs).
 for(int i = 2; i < 15; i++)
 {
     char heart = 'H';
     char diamond ='D';
     char spade = 'S';
     char club = 'C';

     deckOfdemoCards.add(heartdemoCard = new demoCard(heart, i));
     deckOfdemoCards.add(diamonddemoCard = new demoCard(diamond, i));
     deckOfdemoCards.add(spadedemoCard = new demoCard(spade, i));
     deckOfdemoCards.add(clubdemoCard = new demoCard(club, i));
 }

 //prints out the deck of demoCards
 System.out.println("Deck Of demoCards: " + deckOfdemoCards);

 //shuffles the demoCards
 deckOfdemoCards.shuffle();

 //Prints out the deck of demoCards after they are shuffled
 System.out.println("Deck of demoCards after shuffled: " + deckOfdemoCards);

 //Checking the size of the Deck
 System.out.println("" + deckOfdemoCards.size());

 //Takes the deckOfdemoCards and splits them up into 2 piles for Player1 and Player2
 for(int i = 0; i < 26; i++)
 {
     player1Pile.add(deckOfdemoCards.getTopCard());
     player2Pile.add(deckOfdemoCards.getTopCard());
 }

 //Prints out the deck of demoCards and then the player 1's pile and player 2's pile
 System.out.println("Deck of demoCards after removing demoCards into two piles: " + deckOfdemoCards);
 System.out.println("Player 1's demoCards: " + player1Pile);
 System.out.println("Player 2's demoCards: " + player2Pile);

 //checking the size of each players Pile
 System.out.println("" + player1Pile.size());
 System.out.println("" + player2Pile.size());

 //Prints the header for the game
 System.out.println("Lets have a war!!!");
 System.out.println("\n\tPlayer 1                                         Player 2");
 System.out.println("\n\t--------                                         --------");

 //Testing tricky spot where the getTopCard removes a the topdemoCard
 /*
 demoCard removedTopdemoCard = player1Pile.getTopCard();
 System.out.println("Getting player1Pile: " + removedTopdemoCard);
 player1Pile.add(removedTopdemoCard);
 System.out.println("Player1Pile is " + player1Pile);
 System.out.println("Player1Pile size is " +player1Pile.size());
 */

 //Starts the game of War
 try
 {   //do while the sizes of the player piles are greater than 0.
     do
     {
         //gets the top demoCards of each players Pile
         demoCard player1RemovedTopdemoCard = player1Pile.getTopCard();
         demoCard player2RemovedTopdemoCard = player2Pile.getTopCard();

         //Compares the 2 demoCards to test which is bigger. If player 1's demoCard is smaller than player 2 is the winner
         if(player1RemovedTopdemoCard.compareTo(player2RemovedTopdemoCard) < player2RemovedTopdemoCard.compareTo(player1RemovedTopdemoCard))
         {
             System.out.println("Player 1: " + player1RemovedTopdemoCard + " Player 2: " + player2RemovedTopdemoCard);
             System.out.println("Player 2 is the Winner");
             player2Pile.add(player1RemovedTopdemoCard);
             player2Pile.add(player2RemovedTopdemoCard);
             System.out.println("Player 1 has: " + player1Pile.size() + " demoCards left.");
             System.out.println("Player 2 has:" + player2Pile.size() + " demoCards left.");
             System.out.println("\n");
             keyboard.nextLine();
         }
         //Compares the 2 demoCards to test which is bigger. If player 2's demoCard is smaller than player 1 is the winner.
         else if(player1RemovedTopdemoCard.compareTo(player2RemovedTopdemoCard) > player2RemovedTopdemoCard.compareTo(player1RemovedTopdemoCard))
         {
             System.out.println("Player 1: " + player1RemovedTopdemoCard + " Player 2: " + player2RemovedTopdemoCard);
             System.out.println("Player 1 is the Winner");
             player1Pile.add(player1RemovedTopdemoCard);
             player1Pile.add(player2RemovedTopdemoCard);
             System.out.println("Player 1 has: " + player1Pile.size() + " demoCards left.");
             System.out.println("Player 2 has:" + player2Pile.size() + " demoCards left.");
             System.out.println("\n");
             keyboard.nextLine();
         }
         //Else it is a war
         else
         {
             System.out.println("Player 1: " + player1RemovedTopdemoCard + " Player 2: " + player2RemovedTopdemoCard);
             System.out.println("WAR!!!!!!!");
             //War if the player has only 4 demoCards left.
             if(player1Pile.size() == 1 || player2Pile.size() == 1)
             {
                 demoCard player1RemovedTopdemoCard2 = player1Pile.getTopCard();

                 demoCard player2RemovedTopdemoCard2 = player2Pile.getTopCard();
                 System.out.println("Player1's 2nd demoCard is: " + player1RemovedTopdemoCard2 + " Player2's 2nd demoCard is: " + player2RemovedTopdemoCard2);
                 if(player1RemovedTopdemoCard2.compareTo(player2RemovedTopdemoCard2) > player2RemovedTopdemoCard2.compareTo(player1RemovedTopdemoCard2))
                 {
                     System.out.println("Player 1 is the winner of the War! ");
                     player1Pile.add(player1RemovedTopdemoCard);
                     player1Pile.add(player1RemovedTopdemoCard2);
                     player1Pile.add(player2RemovedTopdemoCard);
                     player1Pile.add(player2RemovedTopdemoCard2);
                     System.out.println("Player 1 has: " + player1Pile.size() + " demoCards left.");
                     System.out.println("Player 2 has: " + player2Pile.size() + " demoCards left.");
                     System.out.println("\n");
                     keyboard.nextLine();
                 }
                 else if(player1RemovedTopdemoCard2.compareTo(player2RemovedTopdemoCard2) < player2RemovedTopdemoCard2.compareTo(player1RemovedTopdemoCard2))
                 {
                     System.out.println("Player 2 is the winner of the War! ");
                     player2Pile.add(player1RemovedTopdemoCard);
                     player2Pile.add(player1RemovedTopdemoCard2);
                     player2Pile.add(player2RemovedTopdemoCard);
                     player2Pile.add(player2RemovedTopdemoCard2);
                     System.out.println("Player 1 has: " + player1Pile.size() + " demoCards left.");
                     System.out.println("Player 2 has: " + player2Pile.size() + " demoCards left.");
                     System.out.println("\n");
                     keyboard.nextLine();
                 }
                 else
                 {
                     if(player2Pile.size() == 0)
                     {
                         player1Pile.add(player2RemovedTopdemoCard2);
                         player1Pile.add(player2RemovedTopdemoCard);
                         player1Pile.add(player1RemovedTopdemoCard2);
                         player1Pile.add(player1RemovedTopdemoCard);
                     }
                     else
                     {
                         player2Pile.add(player2RemovedTopdemoCard2);
                         player2Pile.add(player2RemovedTopdemoCard);
                         player2Pile.add(player1RemovedTopdemoCard2);
                         player2Pile.add(player1RemovedTopdemoCard);
                     }
                 }
             }
             //War if the player has only 2 demoCards left.
             else if(player1Pile.size() == 2 || player2Pile.size() == 2)
             {
                 demoCard player1RemovedTopdemoCard2 = player1Pile.getTopCard();
                 demoCard player1RemovedTopdemoCard3 = player1Pile.getTopCard();

                 demoCard player2RemovedTopdemoCard2 = player2Pile.getTopCard();
                 demoCard player2RemovedTopdemoCard3 = player2Pile.getTopCard();

                 do
                 {
                     System.out.println("Player1's 3rd demoCard is: " + player1RemovedTopdemoCard3 + " Player2's 3rd demoCard is: " + player2RemovedTopdemoCard3);
                     if(player1RemovedTopdemoCard3.compareTo(player2RemovedTopdemoCard3) > player2RemovedTopdemoCard3.compareTo(player1RemovedTopdemoCard3))
                     {
                         System.out.println("Player 1 is the winner of the War! ");
                         player1Pile.add(player1RemovedTopdemoCard);
                         player1Pile.add(player1RemovedTopdemoCard2);
                         player1Pile.add(player1RemovedTopdemoCard3);
                         player1Pile.add(player2RemovedTopdemoCard);
                         player1Pile.add(player2RemovedTopdemoCard2);
                         player1Pile.add(player2RemovedTopdemoCard3);
                         System.out.println("Player 1 has: " + player1Pile.size() + " demoCards left.");
                         System.out.println("Player 2 has: " + player2Pile.size() + " demoCards left.");
                         System.out.println("\n");
                         keyboard.nextLine();
                     }
                     else if(player1RemovedTopdemoCard3.compareTo(player2RemovedTopdemoCard3) < player2RemovedTopdemoCard3.compareTo(player1RemovedTopdemoCard3))
                     {
                         System.out.println("Player 2 is the winner of the War! ");
                         player2Pile.add(player1RemovedTopdemoCard);
                         player2Pile.add(player1RemovedTopdemoCard2);
                         player2Pile.add(player1RemovedTopdemoCard3);
                         player2Pile.add(player2RemovedTopdemoCard);
                         player2Pile.add(player2RemovedTopdemoCard2);
                         player2Pile.add(player2RemovedTopdemoCard3);
                         System.out.println("Player 1 has: " + player1Pile.size() + " demoCards left.");
                         System.out.println("Player 2 has: " + player2Pile.size() + " demoCards left.");
                         System.out.println("\n");
                         keyboard.nextLine();
                     }
                 //Continues the war if the top demoCard at the end of the war are still equal
                 }while(player1RemovedTopdemoCard3.compareTo(player2RemovedTopdemoCard3) == player2RemovedTopdemoCard3.compareTo(player1RemovedTopdemoCard3));
             }
             //War if the player has only 3 demoCards left.
             else if(player1Pile.size() == 3 || player2Pile.size() == 3)
             {
                 demoCard player1RemovedTopdemoCard2 = player1Pile.getTopCard();
                 demoCard player1RemovedTopdemoCard3 = player1Pile.getTopCard();
                 demoCard player1RemovedTopdemoCard4 = player1Pile.getTopCard();

                 demoCard player2RemovedTopdemoCard2 = player2Pile.getTopCard();
                 demoCard player2RemovedTopdemoCard3 = player2Pile.getTopCard();
                 demoCard player2RemovedTopdemoCard4 = player2Pile.getTopCard();

                 do
                 {
                     System.out.println("Player1's fourth demoCard is: " + player1RemovedTopdemoCard4 + " Player2's fourth demoCard is: " + player2RemovedTopdemoCard4);
                     if(player1RemovedTopdemoCard4.compareTo(player2RemovedTopdemoCard4) > player2RemovedTopdemoCard4.compareTo(player1RemovedTopdemoCard4))
                     {
                         System.out.println("Player 1 is the winner of the War! ");
                         player1Pile.add(player1RemovedTopdemoCard);
                         player1Pile.add(player1RemovedTopdemoCard2);
                         player1Pile.add(player1RemovedTopdemoCard3);
                         player1Pile.add(player1RemovedTopdemoCard4);
                         player1Pile.add(player2RemovedTopdemoCard);
                         player1Pile.add(player2RemovedTopdemoCard2);
                         player1Pile.add(player2RemovedTopdemoCard3);
                         player1Pile.add(player2RemovedTopdemoCard4);
                         System.out.println("Player 1 has: " + player1Pile.size() + " demoCards left.");
                         System.out.println("Player 2 has: " + player2Pile.size() + " demoCards left.");
                         System.out.println("\n");
                         keyboard.nextLine();
                     }
                     else if(player1RemovedTopdemoCard4.compareTo(player2RemovedTopdemoCard4) < player2RemovedTopdemoCard4.compareTo(player1RemovedTopdemoCard4))
                     {
                         System.out.println("Player 2 is the winner of the War! ");
                         player2Pile.add(player1RemovedTopdemoCard);
                         player2Pile.add(player1RemovedTopdemoCard2);
                         player2Pile.add(player1RemovedTopdemoCard3);
                         player2Pile.add(player1RemovedTopdemoCard4);
                         player2Pile.add(player2RemovedTopdemoCard);
                         player2Pile.add(player2RemovedTopdemoCard2);
                         player2Pile.add(player2RemovedTopdemoCard3);
                         player2Pile.add(player2RemovedTopdemoCard4);
                         System.out.println("Player 1 has: " + player1Pile.size() + " demoCards left.");
                         System.out.println("Player 2 has: " + player2Pile.size() + " demoCards left.");
                         System.out.println("\n");
                         keyboard.nextLine();
                     }
                 //Continues the war if the top demoCard at the end of the war are still equal
                 }while(player1RemovedTopdemoCard4.compareTo(player2RemovedTopdemoCard4) == player2RemovedTopdemoCard4.compareTo(player1RemovedTopdemoCard4));
             }
             //war if player has more than 4 demoCards
             else if(player1Pile.size() >= 4 || player2Pile.size() >= 4)
             {
                 demoCard player1RemovedTopdemoCard2 = player1Pile.getTopCard();
                 demoCard player1RemovedTopdemoCard3 = player1Pile.getTopCard();
                 demoCard player1RemovedTopdemoCard4 = player1Pile.getTopCard();
                 demoCard player1RemovedTopdemoCard5 = player1Pile.getTopCard();

                 demoCard player2RemovedTopdemoCard2 = player2Pile.getTopCard();
                 demoCard player2RemovedTopdemoCard3 = player2Pile.getTopCard();
                 demoCard player2RemovedTopdemoCard4 = player2Pile.getTopCard();
                 demoCard player2RemovedTopdemoCard5 = player2Pile.getTopCard();
                 do
                 {
                     System.out.println("Player1's 5th demoCard is: " + player1RemovedTopdemoCard5 + " Player2's 5th demoCard is: " + player2RemovedTopdemoCard5);
                     if(player1RemovedTopdemoCard5.compareTo(player2RemovedTopdemoCard5) > player2RemovedTopdemoCard5.compareTo(player1RemovedTopdemoCard5))
                     {
                         System.out.println("Player 1 is the winner of the War! ");
                         player1Pile.add(player1RemovedTopdemoCard);
                         player1Pile.add(player1RemovedTopdemoCard2);
                         player1Pile.add(player1RemovedTopdemoCard3);
                         player1Pile.add(player1RemovedTopdemoCard4);
                         player1Pile.add(player1RemovedTopdemoCard5);
                         player1Pile.add(player2RemovedTopdemoCard);
                         player1Pile.add(player2RemovedTopdemoCard2);
                         player1Pile.add(player2RemovedTopdemoCard3);
                         player1Pile.add(player2RemovedTopdemoCard4);
                         player1Pile.add(player2RemovedTopdemoCard5);
                         System.out.println("Player 1 has: " + player1Pile.size() + " demoCards left.");
                         System.out.println("Player 2 has: " + player2Pile.size() + " demoCards left.");
                         System.out.println("\n");
                         keyboard.nextLine();
                     }
                     else if(player1RemovedTopdemoCard5.compareTo(player2RemovedTopdemoCard5) < player2RemovedTopdemoCard5.compareTo(player1RemovedTopdemoCard5))
                     {
                         System.out.println("Player 2 is the winner of the War! ");
                         player2Pile.add(player1RemovedTopdemoCard);
                         player2Pile.add(player1RemovedTopdemoCard2);
                         player2Pile.add(player1RemovedTopdemoCard3);
                         player2Pile.add(player1RemovedTopdemoCard4);
                         player2Pile.add(player1RemovedTopdemoCard5);
                         player2Pile.add(player2RemovedTopdemoCard);
                         player2Pile.add(player2RemovedTopdemoCard2);
                         player2Pile.add(player2RemovedTopdemoCard3);
                         player2Pile.add(player2RemovedTopdemoCard4);
                         player2Pile.add(player1RemovedTopdemoCard5);
                         System.out.println("Player 1 has: " + player1Pile.size() + " demoCards left.");
                         System.out.println("Player 2 has: " + player2Pile.size() + " demoCards left.");
                         System.out.println("\n");
                         keyboard.nextLine();
                     }
                 //Continues the war if the top demoCard at the end of the war are still equal
                 }while(player1RemovedTopdemoCard5.compareTo(player2RemovedTopdemoCard5) == player2RemovedTopdemoCard5.compareTo(player1RemovedTopdemoCard5));
             }
         }
         //Adds to the plays that keep track of how many plays have been made
         countingPlays++;
         //. If there are 10 plays it shuffles and prints out a message that the demoCards have been shuffled.
         if(countingPlays >= 10)
         {
             player1Pile.shuffle();
             player2Pile.shuffle();
             System.out.println("Cards Shuffled");
             //resets the counter to 0
             countingPlays = 0;
         }
     //Continues the game of war while the players piles are bigger than 0
     }while(player1Pile.size() > 0 || player2Pile.size() > 0);
 }

 //Catches the Array 0 error and prints out who is the winner based on who has zero demoCards.
 catch (IndexOutOfBoundsException theException)  //tries to catch this type...
     {
         if(player1Pile.size() == 0)
         {
             System.out.println("Winner is Player 2" );
         }
         else
         System.out.println("Winner is Player 1" );

     }

 } //end of main
}//end of class