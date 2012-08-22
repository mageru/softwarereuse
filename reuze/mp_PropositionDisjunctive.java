package reuze;
//package aima.core.probability.proposition;

import java.util.Map;


//import aima.core.probability.RandomVariable;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Formula 13.4, page
 * 489.<br>
 * <br>
 * 
 * We can also derive the well-known formula for the probability of a
 * disjunction, sometimes called the <b>inclusion-exclusion principle:</b><br>
 * <br>
 * P(a OR b) = P(a) + P(b) - P(a AND b).<br>
 * 
 * @author Ciaran O'Reilly
 */
public class mp_PropositionDisjunctive extends mp_a_Proposition implements
		ml_i_SentencePropositionBinary {

	private mp_i_Proposition left = null;
	private mp_i_Proposition right = null;
	//
	private String toString = null;

	public mp_PropositionDisjunctive(mp_i_Proposition left, mp_i_Proposition right) {
		if (null == left) {
			throw new IllegalArgumentException(
					"Left side of disjunction must be specified.");
		}
		if (null == right) {
			throw new IllegalArgumentException(
					"Right side of disjunction must be specified.");
		}
		// Track nested scope
		addScope(left.getScope());
		addScope(right.getScope());
		addUnboundScope(left.getUnboundScope());
		addUnboundScope(right.getUnboundScope());

		this.left = left;
		this.right = right;
	}

	@Override
	public boolean holds(Map<mp_i_RandomVariable, Object> possibleWorld) {
		return left.holds(possibleWorld) || right.holds(possibleWorld);
	}

	@Override
	public String toString() {
		if (null == toString) {
			StringBuilder sb = new StringBuilder();
			sb.append("(");
			sb.append(left.toString());
			sb.append(" OR ");
			sb.append(right.toString());
			sb.append(")");

			toString = sb.toString();
		}

		return toString;
	}
}
