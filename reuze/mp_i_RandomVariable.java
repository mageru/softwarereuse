package reuze;
import reuze.mp_i_Domain;

//package aima.core.probability;

//import aima.core.probability.domain.Domain;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 486.<br>
 * <br>
 * Variables in probability theory are called random variables and their names
 * begin with an upper-case letter. Every random variable has a domain - the set
 * of possible values it can take on.
 * 
 * @author Ciaran O'Reilly
 */
public interface mp_i_RandomVariable {
	/**
	 * 
	 * @return the name used to uniquely identify this variable.
	 */
	String getName();

	/**
	 * 
	 * @return the Set of possible values the Random Variable can take on.
	 */
	mp_i_Domain getDomain();
}