package reuze;
//package aima.core.logic.fol;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class mf_Quantifiers {
	public static final String FORALL = "FORALL";
	public static final String EXISTS = "EXISTS";

	public static boolean isFORALL(String quantifier) {
		return FORALL.equals(quantifier);
	}

	public static boolean isEXISTS(String quantifier) {
		return EXISTS.equals(quantifier);
	}
}
