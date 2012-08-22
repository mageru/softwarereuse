package reuze;
//package aima.core.probability.temporal.generic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/*import aima.core.probability.CategoricalDistribution;
import aima.core.probability.FiniteProbabilityModel;
import aima.core.probability.RandomVariable;
import aima.core.probability.proposition.AssignmentProposition;
import aima.core.probability.proposition.Proposition;
import aima.core.probability.temporal.ForwardBackwardInference;
import aima.core.probability.util.ProbUtil;
import aima.core.probability.util.ProbabilityTable;
import aima.core.probability.util.RandVar;*/

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 576.<br>
 * <br>
 * 
 * <pre>
 * function FORWARD-BACKWARD(ev, prior) returns a vector of probability distributions
 *   inputs: ev, a vector of evidence values for steps 1,...,t
 *           prior, the prior distribution on the initial state, <b>P</b>(X<sub>0</sub>)
 *   local variables: fv, a vector of forward messages for steps 0,...,t
 *                    b, a representation of the backward message, initially all 1s
 *                    sv, a vector of smoothed estimates for steps 1,...,t
 *                    
 *   fv[0] <- prior
 *   for i = 1 to t do
 *       fv[i] <- FORWARD(fv[i-1], ev[i])
 *   for i = t downto 1 do
 *       sv[i] <- NORMALIZE(fv[i] * b)
 *       b <- BACKWARD(b, ev[i])
 *   return sv
 * </pre>
 * 
 * Figure 15.4 The forward-backward algorithm for smoothing: computing posterior
 * probabilities of a sequence of states given a sequence of observations. The
 * FORWARD and BACKWARD operators are defined by Equations (15.5) and (15.9),
 * respectively.<br>
 * <br>
 * <b>Note:</b> An implementation of the FORWARD-BACKWARD algorithm using the
 * general purpose probability APIs, i.e. the underlying model implementation is
 * abstracted away.
 * 
 * @author Ciaran O'Reilly
 */
public class mpt_InferenceForwardBackward implements mpt_i_InferenceForwardBackward {

	private mp_i_ModelFinite transitionModel = null;
	private Map<mp_i_RandomVariable, mp_i_RandomVariable> tToTm1StateVarMap = new HashMap<mp_i_RandomVariable, mp_i_RandomVariable>();
	private mp_i_ModelFinite sensorModel = null;

	/**
	 * Instantiate an instance of the Forward Backward algorithm.
	 * 
	 * @param transitionModel
	 *            the transition model.
	 * @param tToTm1StateVarMap
	 *            a map from the X<sub>t<sub> random variables in the transition
	 *            model the to X<sub>t-1</sub> random variables.
	 * @param sensorModel
	 *            the sensor model.
	 */
	public mpt_InferenceForwardBackward(mp_i_ModelFinite transitionModel,
			Map<mp_i_RandomVariable, mp_i_RandomVariable> tToTm1StateVarMap,
			mp_i_ModelFinite sensorModel) {
		this.transitionModel = transitionModel;
		this.tToTm1StateVarMap.putAll(tToTm1StateVarMap);
		this.sensorModel = sensorModel;
	}

	//
	// START-ForwardBackwardInference

	// function FORWARD-BACKWARD(ev, prior) returns a vector of probability
	// distributions
	public List<mp_i_CategoricalDistributionIterator> forwardBackward(
			List<List<mp_PropositionTermOpsAssignment>> ev, mp_i_CategoricalDistributionIterator prior) {
		// local variables: fv, a vector of forward messages for steps 0,...,t
		List<mp_i_CategoricalDistributionIterator> fv = new ArrayList<mp_i_CategoricalDistributionIterator>(
				ev.size() + 1);
		// b, a representation of the backward message, initially all 1s
		mp_i_CategoricalDistributionIterator b = initBackwardMessage();
		// sv, a vector of smoothed estimates for steps 1,...,t
		List<mp_i_CategoricalDistributionIterator> sv = new ArrayList<mp_i_CategoricalDistributionIterator>(
				ev.size());

		// fv[0] <- prior
		fv.add(prior);
		// for i = 1 to t do
		for (int i = 0; i < ev.size(); i++) {
			// fv[i] <- FORWARD(fv[i-1], ev[i])
			fv.add(forward(fv.get(i), ev.get(i)));
		}
		// for i = t downto 1 do
		for (int i = ev.size() - 1; i >= 0; i--) {
			// sv[i] <- NORMALIZE(fv[i] * b)
			sv.add(0, fv.get(i + 1).multiplyBy(b).normalize());
			// b <- BACKWARD(b, ev[i])
			b = backward(b, ev.get(i));
		}

		// return sv
		return sv;
	}

	public mp_i_CategoricalDistributionIterator forward(mp_i_CategoricalDistributionIterator f1_t,
			List<mp_PropositionTermOpsAssignment> e_tp1) {
		final mp_i_CategoricalDistributionIterator s1 = new mp_ProbabilityTable(f1_t.getFor());
		// Set up required working variables
		mp_i_Proposition[] props = new mp_i_Proposition[s1.getFor().size()];
		int i = 0;
		for (mp_i_RandomVariable rv : s1.getFor()) {
			props[i] = new mp_RandomVariable(rv.getName(), rv.getDomain());
			i++;
		}
		final mp_i_Proposition Xtp1 = mp_ProbUtil.constructConjunction(props);
		final mp_PropositionTermOpsAssignment[] xt = new mp_PropositionTermOpsAssignment[tToTm1StateVarMap
				.size()];
		final Map<mp_i_RandomVariable, mp_PropositionTermOpsAssignment> xtVarAssignMap = new HashMap<mp_i_RandomVariable, mp_PropositionTermOpsAssignment>();
		i = 0;
		for (mp_i_RandomVariable rv : tToTm1StateVarMap.keySet()) {
			xt[i] = new mp_PropositionTermOpsAssignment(tToTm1StateVarMap.get(rv),
					"<Dummy Value>");
			xtVarAssignMap.put(rv, xt[i]);
			i++;
		}

		// Step 1: Calculate the 1 time step prediction
		// &sum;<sub>x<sub>t</sub></sub>
		mp_i_CategoricalDistributionIterator.Iterator if1_t = new mp_i_CategoricalDistributionIterator.Iterator() {
			public void iterate(Map<mp_i_RandomVariable, Object> possibleWorld,
					double probability) {
				// <b>P</b>(X<sub>t+1</sub> | x<sub>t</sub>)*
				// P(x<sub>t</sub> | e<sub>1:t</sub>)
				for (Map.Entry<mp_i_RandomVariable, Object> av : possibleWorld
						.entrySet()) {
					xtVarAssignMap.get(av.getKey()).setValue(av.getValue());
				}
				int i = 0;
				for (double tp : transitionModel
						.posteriorDistribution(Xtp1, xt).getValues()) {
					s1.setValue(i, s1.getValues()[i] + (tp * probability));
					i++;
				}
			}
		};
		f1_t.iterateOver(if1_t);

		// Step 2: multiply by the probability of the evidence
		// and normalize
		// <b>P</b>(e<sub>t+1</sub> | X<sub>t+1</sub>)
		mp_i_CategoricalDistributionIterator s2 = sensorModel.posteriorDistribution(mp_ProbUtil
				.constructConjunction(e_tp1.toArray(new mp_i_Proposition[e_tp1
						.size()])), Xtp1);

		return s2.multiplyBy(s1).normalize();
	}

	public mp_i_CategoricalDistributionIterator backward(mp_i_CategoricalDistributionIterator b_kp2t,
			List<mp_PropositionTermOpsAssignment> e_kp1) {
		final mp_i_CategoricalDistributionIterator b_kp1t = new mp_ProbabilityTable(
				b_kp2t.getFor());
		// Set up required working variables
		mp_i_Proposition[] props = new mp_i_Proposition[b_kp1t.getFor().size()];
		int i = 0;
		for (mp_i_RandomVariable rv : b_kp1t.getFor()) {
			mp_i_RandomVariable prv = tToTm1StateVarMap.get(rv);
			props[i] = new mp_RandomVariable(prv.getName(), prv.getDomain());
			i++;
		}
		final mp_i_Proposition Xk = mp_ProbUtil.constructConjunction(props);
		final mp_PropositionTermOpsAssignment[] ax_kp1 = new mp_PropositionTermOpsAssignment[tToTm1StateVarMap
				.size()];
		final Map<mp_i_RandomVariable, mp_PropositionTermOpsAssignment> x_kp1VarAssignMap = new HashMap<mp_i_RandomVariable, mp_PropositionTermOpsAssignment>();
		i = 0;
		for (mp_i_RandomVariable rv : b_kp1t.getFor()) {
			ax_kp1[i] = new mp_PropositionTermOpsAssignment(rv, "<Dummy Value>");
			x_kp1VarAssignMap.put(rv, ax_kp1[i]);
			i++;
		}
		final mp_i_Proposition x_kp1 = mp_ProbUtil.constructConjunction(ax_kp1);
		props = new mp_i_Proposition[e_kp1.size()];
		final mp_i_Proposition pe_kp1 = mp_ProbUtil.constructConjunction(e_kp1
				.toArray(props));

		// &sum;<sub>x<sub>k+1</sub></sub>
		mp_i_CategoricalDistributionIterator.Iterator ib_kp2t = new mp_i_CategoricalDistributionIterator.Iterator() {
			public void iterate(Map<mp_i_RandomVariable, Object> possibleWorld,
					double probability) {
				// Assign current values for x<sub>k+1</sub>
				for (Map.Entry<mp_i_RandomVariable, Object> av : possibleWorld
						.entrySet()) {
					x_kp1VarAssignMap.get(av.getKey()).setValue(av.getValue());
				}

				// P(e<sub>k+1</sub> | x<sub>k+1</sub>)
				// P(e<sub>k+2:t</sub> | x<sub>k+1</sub>)
				double p = sensorModel.posterior(pe_kp1, x_kp1) * probability;

				// <b>P</b>(x<sub>k+1</sub> | X<sub>k</sub>)
				int i = 0;
				for (double tp : transitionModel.posteriorDistribution(x_kp1,
						Xk).getValues()) {
					b_kp1t.setValue(i, b_kp1t.getValues()[i] + (tp * p));
					i++;
				}
			}
		};
		b_kp2t.iterateOver(ib_kp2t);

		return b_kp1t;
	}

	// END-ForwardBackwardInference
	//

	//
	// PRIVATE METHODS
	//
	private mp_i_CategoricalDistributionIterator initBackwardMessage() {
		mp_ProbabilityTable b = new mp_ProbabilityTable(tToTm1StateVarMap.keySet());

		for (int i = 0; i < b.size(); i++) {
			b.setValue(i, 1.0);
		}

		return b;
	}
}
