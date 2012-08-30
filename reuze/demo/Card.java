package reuze.demo;

public class Card implements Comparable<Object>
{
    //Data
    private int rank;
    private char suit;
 
    //default constructor
    public Card()
    {
        suit = '\u0003';
        rank = 0;
    }
 
    //Constructor
    public Card(char theSuit, int theRank)
    {
        this.suit = theSuit;
        this.rank = theRank;
    }
 
    //get Rank of the card
    public int getRank()
    {
        return rank;
    }
 
    //get Suite of the card
    public char getSuit()
    {
        return suit;
    }
 
    //toString - returns its representation as a String
    public String toString()
    {
        //Checks the rank of the cards and Changes them to the correct character
        if(rank == 11)
        {
            return suit + "" + 'J';
        }
        else if(rank == 12)
        {
            return suit + "" + 'Q';
        }
        else if(rank == 13)
        {
            return suit + "" + 'K';
        }
        else if(rank == 14)
        {
            return suit + "" + 'A';
        }
        else
        return suit + "" + rank;
    }
 
    //Compares 2 cards Rank and Suite
    public int compareTo(Object anotherCard)
    {
        Card another = (Card)anotherCard;
        if(this.rank > another.rank)
        {
            return 1;
        }
        else if(this.rank < another.rank)
        {
            return -1;
        }
        else
            return 0;
    }
 
 
}