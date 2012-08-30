package reuze.demo;

import java.util.Scanner;
import java.io.IOException;


public class demoWar {
	public static void main(String[] args) throws IOException {
		// Declare Variables
		// Creates the cards based on their suite
		Card heartCard;
		Card diamondCard;
		Card spadeCard;
		Card clubCard;
		int countingPlays = 0;

		Scanner keyboard = new Scanner(System.in); // Allows Input

		// creates the Deck array called DeckOfcards
		Deck deckOfCards = new Deck();

		// Creates Player1's Card Pile
		Deck player1Hand = new Deck();

		// Creates Player2's Card Pile
		Deck player2Hand = new Deck();

		// Creates the cards to fill the array (1-14 of
		// hearts/diamonds/spades/clubs).
		for (int i = 2; i < 15; i++) {
			char heart = 'H';
			char diamond = 'D';
			char spade = 'S';
			char club = 'C';

			deckOfCards.add(heartCard = new Card(heart, i));
			deckOfCards.add(diamondCard = new Card(diamond, i));
			deckOfCards.add(spadeCard = new Card(spade, i));
			deckOfCards.add(clubCard = new Card(club, i));
		}

		System.out.println("Deck Of cards: " + deckOfCards);
		deckOfCards.shuffle();

		// Prints out the deck of cards after they are shuffled
		System.out.println("Deck of cards after shuffled: " + deckOfCards);

		// Checking the size of the Deck
		System.out.println("" + deckOfCards.size());

		// Takes the deckOfcards and splits them up into 2 piles for Player1 and
		// Player2
		for (int i = 0; i < 26; i++) {
			player1Hand.add(deckOfCards.getTopCard());
			player2Hand.add(deckOfCards.getTopCard());
		}

		// Prints out the deck of cards and then the player 1's pile and player
		// 2's pile
		System.out
				.println("Deck of cards after removing cards into two piles: "
						+ deckOfCards);
		System.out.println("Player 1's cards: " + player1Hand);
		System.out.println("Player 2's cards: " + player2Hand);

		// checking the size of each players Pile
		System.out.println("" + player1Hand.size());
		System.out.println("" + player2Hand.size());

		// Prints the header for the game
		System.out.println("-------------Begin the game------------");

		// Testing tricky spot where the getTopCard removes a the topCard
		/*
		 * Card removedTopCard = player1Pile.getTopCard();
		 * System.out.println("Getting player1Pile: " + removedTopCard);
		 * player1Pile.add(removedTopCard); System.out.println("Player1Pile is "
		 * + player1Pile); System.out.println("Player1Pile size is "
		 * +player1Pile.size());
		 */

		// Starts the game of War
		try { // do while the sizes of the player piles are greater than 0.
			do {
				// gets the top cards of each players Pile
				Card player1RemovedTopCard = player1Hand.getTopCard();
				Card player2RemovedTopCard = player2Hand.getTopCard();

				// Compares the 2 cards to test which is bigger. If player 1's
				// Card is smaller than player 2 is the winner
				if (player1RemovedTopCard.compareTo(player2RemovedTopCard) < player2RemovedTopCard
						.compareTo(player1RemovedTopCard)) {
					System.out.println("Player 1: " + player1RemovedTopCard
							+ " Player 2: " + player2RemovedTopCard);
					System.out.println("Player 2 is the Winner");
					player2Hand.add(player1RemovedTopCard);
					player2Hand.add(player2RemovedTopCard);
					System.out.println("Player 1 has: " + player1Hand.size()
							+ " cards left.");
					System.out.println("Player 2 has:" + player2Hand.size()
							+ " cards left.");
					System.out.println("\n");
					keyboard.nextLine();
				}
				// Compares the 2 cards to test which is bigger. If player 2's
				// Card is smaller than player 1 is the winner.
				else if (player1RemovedTopCard.compareTo(player2RemovedTopCard) > player2RemovedTopCard
						.compareTo(player1RemovedTopCard)) {
					System.out.println("Player 1: " + player1RemovedTopCard
							+ " Player 2: " + player2RemovedTopCard);
					System.out.println("Player 1 is the Winner");
					player1Hand.add(player1RemovedTopCard);
					player1Hand.add(player2RemovedTopCard);
					System.out.println("Player 1 has: " + player1Hand.size()
							+ " cards left.");
					System.out.println("Player 2 has:" + player2Hand.size()
							+ " cards left.");
					System.out.println("\n");
					keyboard.nextLine();
				}
				// Else it is a war
				else {
					System.out.println("Player 1: " + player1RemovedTopCard
							+ " Player 2: " + player2RemovedTopCard);
					System.out.println("WAR!!!!!!!");
					// War if the player has only 4 cards left.
					if (player1Hand.size() == 1 || player2Hand.size() == 1) {
						Card player1RemovedTopCard2 = player1Hand.getTopCard();

						Card player2RemovedTopCard2 = player2Hand.getTopCard();
						System.out.println("Player1's 2nd Card is: "
								+ player1RemovedTopCard2
								+ " Player2's 2nd Card is: "
								+ player2RemovedTopCard2);
						if (player1RemovedTopCard2
								.compareTo(player2RemovedTopCard2) > player2RemovedTopCard2
								.compareTo(player1RemovedTopCard2)) {
							System.out
									.println("Player 1 is the winner of the War! ");
							player1Hand.add(player1RemovedTopCard);
							player1Hand.add(player1RemovedTopCard2);
							player1Hand.add(player2RemovedTopCard);
							player1Hand.add(player2RemovedTopCard2);
							System.out.println("Player 1 has: "
									+ player1Hand.size() + " cards left.");
							System.out.println("Player 2 has: "
									+ player2Hand.size() + " cards left.");
							System.out.println("\n");
							keyboard.nextLine();
						} 
						else if (player1RemovedTopCard2
								.compareTo(player2RemovedTopCard2) < player2RemovedTopCard2
								.compareTo(player1RemovedTopCard2)) {
							System.out
									.println("Player 2 is the winner of the War! ");
							player2Hand.add(player1RemovedTopCard);
							player2Hand.add(player1RemovedTopCard2);
							player2Hand.add(player2RemovedTopCard);
							player2Hand.add(player2RemovedTopCard2);
							System.out.println("Player 1 has: "
									+ player1Hand.size() + " cards left.");
							System.out.println("Player 2 has: "
									+ player2Hand.size() + " cards left.");
							System.out.println("\n");
							keyboard.nextLine();
						} 
						else {
							if (player2Hand.size() == 0) {
								player1Hand.add(player2RemovedTopCard2);
								player1Hand.add(player2RemovedTopCard);
								player1Hand.add(player1RemovedTopCard2);
								player1Hand.add(player1RemovedTopCard);
							} 
							else {
								player2Hand.add(player2RemovedTopCard2);
								player2Hand.add(player2RemovedTopCard);
								player2Hand.add(player1RemovedTopCard2);
								player2Hand.add(player1RemovedTopCard);
							}
						}
					}
					// War if the player has only 2 cards left.
					else if (player1Hand.size() == 2 || player2Hand.size() == 2) {
						Card player1RemovedTopCard2 = player1Hand.getTopCard();
						Card player1RemovedTopCard3 = player1Hand.getTopCard();

						Card player2RemovedTopCard2 = player2Hand.getTopCard();
						Card player2RemovedTopCard3 = player2Hand.getTopCard();

						do {
							System.out.println("Player1's 3rd Card is: "
									+ player1RemovedTopCard3
									+ " Player2's 3rd Card is: "
									+ player2RemovedTopCard3);
							if (player1RemovedTopCard3
									.compareTo(player2RemovedTopCard3) > player2RemovedTopCard3
									.compareTo(player1RemovedTopCard3)) {
								System.out
										.println("Player 1 is the winner of the War! ");
								player1Hand.add(player1RemovedTopCard);
								player1Hand.add(player1RemovedTopCard2);
								player1Hand.add(player1RemovedTopCard3);
								player1Hand.add(player2RemovedTopCard);
								player1Hand.add(player2RemovedTopCard2);
								player1Hand.add(player2RemovedTopCard3);
								System.out.println("Player 1 has: "
										+ player1Hand.size() + " cards left.");
								System.out.println("Player 2 has: "
										+ player2Hand.size() + " cards left.");
								System.out.println("\n");
								keyboard.nextLine();
							} 
							else if (player1RemovedTopCard3
									.compareTo(player2RemovedTopCard3) < player2RemovedTopCard3
									.compareTo(player1RemovedTopCard3)) {
								System.out
										.println("Player 2 is the winner of the War! ");
								player2Hand.add(player1RemovedTopCard);
								player2Hand.add(player1RemovedTopCard2);
								player2Hand.add(player1RemovedTopCard3);
								player2Hand.add(player2RemovedTopCard);
								player2Hand.add(player2RemovedTopCard2);
								player2Hand.add(player2RemovedTopCard3);
								System.out.println("Player 1 has: "
										+ player1Hand.size() + " cards left.");
								System.out.println("Player 2 has: "
										+ player2Hand.size() + " cards left.");
								System.out.println("\n");
								keyboard.nextLine();
							}
							// Continues the war if the top Card at the end of
							// the war are still equal
						} while (player1RemovedTopCard3
								.compareTo(player2RemovedTopCard3) == player2RemovedTopCard3
								.compareTo(player1RemovedTopCard3));
					}
					// War if the player has only 3 cards left.
					else if (player1Hand.size() == 3 || player2Hand.size() == 3) {
						Card player1RemovedTopCard2 = player1Hand.getTopCard();
						Card player1RemovedTopCard3 = player1Hand.getTopCard();
						Card player1RemovedTopCard4 = player1Hand.getTopCard();

						Card player2RemovedTopCard2 = player2Hand.getTopCard();
						Card player2RemovedTopCard3 = player2Hand.getTopCard();
						Card player2RemovedTopCard4 = player2Hand.getTopCard();

						do {
							System.out.println("Player1's fourth Card is: "
									+ player1RemovedTopCard4
									+ " Player2's fourth Card is: "
									+ player2RemovedTopCard4);
							if (player1RemovedTopCard4
									.compareTo(player2RemovedTopCard4) > player2RemovedTopCard4
									.compareTo(player1RemovedTopCard4)) {
								System.out
										.println("Player 1 is the winner of the War! ");
								player1Hand.add(player1RemovedTopCard);
								player1Hand.add(player1RemovedTopCard2);
								player1Hand.add(player1RemovedTopCard3);
								player1Hand.add(player1RemovedTopCard4);
								player1Hand.add(player2RemovedTopCard);
								player1Hand.add(player2RemovedTopCard2);
								player1Hand.add(player2RemovedTopCard3);
								player1Hand.add(player2RemovedTopCard4);
								System.out.println("Player 1 has: "
										+ player1Hand.size() + " cards left.");
								System.out.println("Player 2 has: "
										+ player2Hand.size() + " cards left.");
								System.out.println("\n");
								keyboard.nextLine();
							} else if (player1RemovedTopCard4
									.compareTo(player2RemovedTopCard4) < player2RemovedTopCard4
									.compareTo(player1RemovedTopCard4)) {
								System.out
										.println("Player 2 is the winner of the War! ");
								player2Hand.add(player1RemovedTopCard);
								player2Hand.add(player1RemovedTopCard2);
								player2Hand.add(player1RemovedTopCard3);
								player2Hand.add(player1RemovedTopCard4);
								player2Hand.add(player2RemovedTopCard);
								player2Hand.add(player2RemovedTopCard2);
								player2Hand.add(player2RemovedTopCard3);
								player2Hand.add(player2RemovedTopCard4);
								System.out.println("Player 1 has: "
										+ player1Hand.size() + " cards left.");
								System.out.println("Player 2 has: "
										+ player2Hand.size() + " cards left.");
								System.out.println("\n");
								keyboard.nextLine();
							}
							// Continues the war if the top Card at the end of
							// the war are still equal
						} while (player1RemovedTopCard4
								.compareTo(player2RemovedTopCard4) == player2RemovedTopCard4
								.compareTo(player1RemovedTopCard4));
					}
					// war if player has more than 4 cards
					else if (player1Hand.size() >= 4 || player2Hand.size() >= 4) {
						Card player1RemovedTopCard2 = player1Hand.getTopCard();
						Card player1RemovedTopCard3 = player1Hand.getTopCard();
						Card player1RemovedTopCard4 = player1Hand.getTopCard();
						Card player1RemovedTopCard5 = player1Hand.getTopCard();

						Card player2RemovedTopCard2 = player2Hand.getTopCard();
						Card player2RemovedTopCard3 = player2Hand.getTopCard();
						Card player2RemovedTopCard4 = player2Hand.getTopCard();
						Card player2RemovedTopCard5 = player2Hand.getTopCard();
						do {
							System.out.println("Player1's 5th Card is: "
									+ player1RemovedTopCard5
									+ " Player2's 5th Card is: "
									+ player2RemovedTopCard5);
							if (player1RemovedTopCard5
									.compareTo(player2RemovedTopCard5) > player2RemovedTopCard5
									.compareTo(player1RemovedTopCard5)) {
								System.out
										.println("Player 1 is the winner of the War! ");
								player1Hand.add(player1RemovedTopCard);
								player1Hand.add(player1RemovedTopCard2);
								player1Hand.add(player1RemovedTopCard3);
								player1Hand.add(player1RemovedTopCard4);
								player1Hand.add(player1RemovedTopCard5);
								player1Hand.add(player2RemovedTopCard);
								player1Hand.add(player2RemovedTopCard2);
								player1Hand.add(player2RemovedTopCard3);
								player1Hand.add(player2RemovedTopCard4);
								player1Hand.add(player2RemovedTopCard5);
								System.out.println("Player 1 has: "
										+ player1Hand.size() + " cards left.");
								System.out.println("Player 2 has: "
										+ player2Hand.size() + " cards left.");
								System.out.println("\n");
								keyboard.nextLine();
							} else if (player1RemovedTopCard5
									.compareTo(player2RemovedTopCard5) < player2RemovedTopCard5
									.compareTo(player1RemovedTopCard5)) {
								System.out
										.println("Player 2 is the winner of the War! ");
								player2Hand.add(player1RemovedTopCard);
								player2Hand.add(player1RemovedTopCard2);
								player2Hand.add(player1RemovedTopCard3);
								player2Hand.add(player1RemovedTopCard4);
								player2Hand.add(player1RemovedTopCard5);
								player2Hand.add(player2RemovedTopCard);
								player2Hand.add(player2RemovedTopCard2);
								player2Hand.add(player2RemovedTopCard3);
								player2Hand.add(player2RemovedTopCard4);
								player2Hand.add(player1RemovedTopCard5);
								System.out.println("Player 1 has: "
										+ player1Hand.size() + " cards left.");
								System.out.println("Player 2 has: "
										+ player2Hand.size() + " cards left.");
								System.out.println("\n");
								keyboard.nextLine();
							}
							// Continues the war if the top Card at the end of
							// the war are still equal
						} while (player1RemovedTopCard5
								.compareTo(player2RemovedTopCard5) == player2RemovedTopCard5
								.compareTo(player1RemovedTopCard5));
					}
				}
				// Adds to the plays that keep track of how many plays have been
				// made
				countingPlays++;
				// . If there are 10 plays it shuffles and prints out a message
				// that the cards have been shuffled.
				if (countingPlays >= 10) {
					player1Hand.shuffle();
					player2Hand.shuffle();
					System.out.println("Cards Shuffled");
					// resets the counter to 0
					countingPlays = 0;
				}
				// Continues the game of war while the players piles are bigger
				// than 0
			} while (player1Hand.size() > 0 || player2Hand.size() > 0);
		}

		// Catches the Array 0 error and prints out who is the winner based on
		// who has zero cards.
		catch (IndexOutOfBoundsException theException) // tries to catch this
														// type...
		{
			if (player1Hand.size() == 0) {
				System.out.println("Winner is Player 2");
			} else
				System.out.println("Winner is Player 1");

		}

	} // end of main
}// end of class