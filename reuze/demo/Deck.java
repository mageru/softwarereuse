package reuze.demo;

//Deck - holds a "pile" of Cards and has methods to access them

import java.util.*;

public class Deck
{
  //data - uses an ArrayList of Cards to actually store them
  private ArrayList<Card> pile;

  //constructor - creates the ArrayList that will be used
  public Deck()
  {
      pile = new ArrayList<Card>();
  }

  //methods

  //add - puts a Card at the end ("bottom") of the pile.  It just uses the ArrayList method
  public void add(Card aCard)
  {
      pile.add(aCard);
  }

  //getTopCard - removes and returns the "top" card of the pile.  It just uses the ArrayList method
  public Card getTopCard()
  {
      return pile.remove(0);
  }

  //toString - returns a String representation of the pile.  It just uses the ArrayList method
  public String toString()
  {
      return pile.toString();
  }

  //size - returns the size of the pile.  It just uses the ArrayList method
  public int size()
  {
      return pile.size();
  }

  //clear - empties the pile.  It just uses the ArrayList method
  public void clear()
  {
      pile.clear();
  }

  //shuffle - randomly reorders the pile.
  public void shuffle()
  {
      Random rand = new Random();

      for (int i=0; i<2000; i++)    //repeat 2000 times
      {
          if (size() > 0)           //if there is anything here to shuffle, then...
          {
        	  Card topCard = pile.remove(0);              //take off the top card...
              int newPosition = rand.nextInt(pile.size());    //...find a new spot for it in a random position
              pile.add(newPosition, topCard);                 //...and put it back there
          }
      }
  }
}