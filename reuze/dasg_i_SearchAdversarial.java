package reuze;

import reuze.d_Metrics;

/**
 * Variant of the search interface. Since players can only control the next
 * move, method <code>makeDecision</code> returns only one action, not a
 * sequence of actions.
 * 
 * @author Ruediger Lunde
 */
public interface dasg_i_SearchAdversarial<STATE, ACTION> {

	/** Returns the action which appears to be the best at the given state. */
	ACTION makeDecision(STATE state);

	/**
	 * Returns all the metrics of the search.
	 * 
	 * @return all the metrics of the search.
	 */
	d_Metrics getMetrics();
}
