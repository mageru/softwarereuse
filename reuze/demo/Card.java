package reuze.demo;

/**
 * The card class defines what a card is for use in various games which might use cards.
 * @author Justin Joseph Miller
 *
 */
public class Card implements Comparable<Object>
{
    private int rank;
    private char suit;
 
    /**
     * Constructor takes the two params required for a card to exist.
     * @param theSuit	associated suite for card (dimaond,hearts,spades,clubs)
     * @param theRank
     */
    public Card(char theSuit, int theRank)
    {
        this.suit = theSuit;
        this.rank = theRank;
    }
 
    /**
     * Get the rank of the card.
     * @return rank 	the value of the card in comparison to other cards
     */
    public int getRank()
    {
        return rank;
    }
 
    /**
     * Get the suite of the card
     * @return suite 	the suite of the card (diamond, hearts, spades, or clubs)
     */
    public char getSuit()
    {
        return suit;
    }
 
    /**
     * Return the String representation of the object
     * @return String	Representation of card including suit and rank.
     * @see java.lang.Object#toString()
     */
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
 
    /**
     * Define how to compare two cards for the game of War.
     * @return int	the results of the comparison 1 for greater, -1 for less, 0 for equal
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
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