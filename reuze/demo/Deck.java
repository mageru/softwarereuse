package reuze.demo;

import java.util.*;

/**
 * The deck class represents a selection of cards to be used with the WAR game.
 * @author Justin Joseph Miller
 *
 */
public class Deck
{
private ArrayList<Card> deck;

/**
 * Default deck constructor
 */
public Deck()
  {
      deck = new ArrayList<Card>();
  }

/**
 * Add a card to the current deck.
 * @param aCard
 */
public void add(Card aCard)
  {
      deck.add(aCard);
  }

  //getTopCard - removes and returns the "top" card of the pile.  It just uses the ArrayList method
/**
 * Pull the top card off the deck and return it.
 * @return Card	current top card of deck
 */
public Card getTopCard()
  {
      return deck.remove(0);
  }

/**
 * toString - returns a String representation of the deck.
 * @return string object that represents the deck
 * @see java.lang.Object#toString()
 */
public String toString()
  {
      return deck.toString();
  }

/**
 * Get the number of cards in the deck.
 * @return	number of cards in deck
 */
public int size()
  {
      return deck.size();
  }

/**
 * Empties the current deck.
 */
public void clear()
  {
      deck.clear();
  }

/**
 * Shuffles the deck
 */
public void shuffle()
  {
      Random rand = new Random();

      for (int i=0; i<2000; i++)    //repeat 2000 times
      {
          if (size() > 0)           //if there is anything here to shuffle, then...
          {
        	  Card topCard = deck.remove(0);              //take off the top card...
              int newPosition = rand.nextInt(deck.size());    //...find a new spot for it in a random position
              deck.add(newPosition, topCard);                 //...and put it back there
          }
      }
  }
}