package reuze;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import reuze.aa_i_Action;
import reuze.apm_i_ProblemBidirectional;
import reuze.dag_GraphSearch;
import reuze.d_Metrics;
import reuze.mpb_i_Node;
import reuze.a_Problem;
import reuze.das_i_SearchProblem;
import reuze.aa_SearchUtils;
import reuze.d_QueueFIFO;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 90.<br>
 * <br>
 * Bidirectional search.<br>
 * <br>
 * <b>Note:</b> Based on the description of this algorithm i.e. 'Bidirectional
 * search is implemented by replacing the goal test with a check to see whether
 * the frontiers of the two searches intersect;', it is possible for the
 * searches to pass each other's frontiers by, in particular if the problem is
 * not fully reversible (i.e. unidirectional links on a graph), and could
 * instead intersect at the explored set.
 * 
 * @author Ciaran O'Reilly
 * @author Mike Stampone
 */
public class das_Bidirectional implements das_i_SearchProblem {
	public enum SearchOutcome {
		PATH_FOUND_FROM_ORIGINAL_PROBLEM, PATH_FOUND_FROM_REVERSE_PROBLEM, PATH_FOUND_BETWEEN_PROBLEMS, PATH_NOT_FOUND
	};

	protected d_Metrics metrics;

	private SearchOutcome searchOutcome = SearchOutcome.PATH_NOT_FOUND;

	private static final String NODES_EXPANDED = "nodesExpanded";

	private static final String QUEUE_SIZE = "queueSize";

	private static final String MAX_QUEUE_SIZE = "maxQueueSize";

	private static final String PATH_COST = "pathCost";

	public das_Bidirectional() {
		metrics = new d_Metrics();
	}

	public List<aa_i_Action> search(a_Problem p) throws Exception {

		assert (p instanceof apm_i_ProblemBidirectional);

		searchOutcome = SearchOutcome.PATH_NOT_FOUND;

		clearInstrumentation();

		a_Problem op = ((apm_i_ProblemBidirectional) p).getOriginalProblem();
		a_Problem rp = ((apm_i_ProblemBidirectional) p).getReverseProblem();

		CachedStateQueue<aa_TreeSearchNode> opFrontier = new CachedStateQueue<aa_TreeSearchNode>();
		CachedStateQueue<aa_TreeSearchNode> rpFrontier = new CachedStateQueue<aa_TreeSearchNode>();

		dag_GraphSearch ogs = new dag_GraphSearch();
		dag_GraphSearch rgs = new dag_GraphSearch();
		// Ensure the instrumentation for these
		// are cleared down as their values
		// are used in calculating the overall
		// bidirectional metrics.
		ogs.clearInstrumentation();
		rgs.clearInstrumentation();

		aa_TreeSearchNode opNode = new aa_TreeSearchNode(op.getInitialState());
		aa_TreeSearchNode rpNode = new aa_TreeSearchNode(rp.getInitialState());
		opFrontier.insert(opNode);
		rpFrontier.insert(rpNode);

		setQueueSize(opFrontier.size() + rpFrontier.size());
		setNodesExpanded(ogs.getNodesExpanded() + rgs.getNodesExpanded());

		while (!(opFrontier.isEmpty() && rpFrontier.isEmpty())) {
			// Determine the nodes to work with and expand their fringes
			// in preparation for testing whether or not the two
			// searches meet or one or other is at the GOAL.
			if (!opFrontier.isEmpty()) {
				opNode = opFrontier.pop();
				opFrontier.addAll(ogs.getResultingNodesToAddToFrontier(opNode,
						op));
			} else {
				opNode = null;
			}
			if (!rpFrontier.isEmpty()) {
				rpNode = rpFrontier.pop();
				rpFrontier.addAll(rgs.getResultingNodesToAddToFrontier(rpNode,
						rp));
			} else {
				rpNode = null;
			}

			setQueueSize(opFrontier.size() + rpFrontier.size());
			setNodesExpanded(ogs.getNodesExpanded() + rgs.getNodesExpanded());

			//
			// First Check if either frontier contains the other's state
			if (null != opNode && null != rpNode) {
				aa_TreeSearchNode popNode = null;
				aa_TreeSearchNode prpNode = null;
				if (opFrontier.containsNodeBasedOn(rpNode.getState())) {
					popNode = opFrontier.getNodeBasedOn(rpNode.getState());
					prpNode = rpNode;
				} else if (rpFrontier.containsNodeBasedOn(opNode.getState())) {
					popNode = opNode;
					prpNode = rpFrontier.getNodeBasedOn(opNode.getState());
					// Need to also check whether or not the nodes that
					// have been taken off the frontier actually represent the
					// same state, otherwise there are instances whereby
					// the searches can pass each other by
				} else if (opNode.getState().equals(rpNode.getState())) {
					popNode = opNode;
					prpNode = rpNode;
				}
				if (null != popNode && null != prpNode) {
					List<aa_i_Action> actions = retrieveActions(op, rp, popNode,
							prpNode);
					// It may be the case that it is not in fact possible to
					// traverse from the original node to the goal node based on
					// the reverse path (i.e. unidirectional links: e.g.
					// InitialState(A)<->C<-Goal(B) )
					if (null != actions) {
						return actions;
					}
				}
			}

			//
			// Check if the original problem is at the GOAL state
			if (null != opNode && aa_SearchUtils.isGoalState(op, opNode)) {
				// No need to check return value for null here
				// as an action path discovered from the goal
				// is guaranteed to exist
				return retrieveActions(op, rp, opNode, null);
			}
			//
			// Check if the reverse problem is at the GOAL state
			if (null != rpNode && aa_SearchUtils.isGoalState(rp, rpNode)) {
				List<aa_i_Action> actions = retrieveActions(op, rp, null, rpNode);
				// It may be the case that it is not in fact possible to
				// traverse from the original node to the goal node based on
				// the reverse path (i.e. unidirectional links: e.g.
				// InitialState(A)<-Goal(B) )
				if (null != actions) {
					return actions;
				}
			}
		}

		// Empty List can indicate already at Goal
		// or unable to find valid set of actions
		return new ArrayList<aa_i_Action>();
	}

	/**
	 * Returns PATH_FOUND_FROM_ORIGINAL_PROBLEM if the path was found from the
	 * initial state, PATH_FOUND_FROM_REVERSE_PROBLEM if the path was found from
	 * a goal, PATH_FOUND_FROM_BETWEEN_PROBLEMS if a branch from the initial
	 * state met a branch from a goal state, or PATH_NOT_FOUND if no path from
	 * the initial state to a goal state was found.
	 * 
	 * @return PATH_FOUND_FROM_ORIGINAL_PROBLEM if the path was found from the
	 *         initial state, PATH_FOUND_FROM_REVERSE_PROBLEM if the path was
	 *         found from a goal, PATH_FOUND_FROM_BETWEEN_PROBLEMS if a branch
	 *         from the initial state met a branch from a goal state, or
	 *         PATH_NOT_FOUND if no path from the initial state to a goal state
	 *         was found.
	 */
	public SearchOutcome getSearchOutcome() {
		return searchOutcome;
	}

	/**
	 * Returns all the metrics of the search.
	 * 
	 * @return all the metrics of the search.
	 */
	public d_Metrics getMetrics() {
		return metrics;
	}

	/**
	 * Sets all metrics to zero.
	 */
	public void clearInstrumentation() {
		metrics.set(NODES_EXPANDED, 0);
		metrics.set(QUEUE_SIZE, 0);
		metrics.set(MAX_QUEUE_SIZE, 0);
		metrics.set(PATH_COST, 0.0);
	}

	/**
	 * Returns the number of nodes expanded.
	 * 
	 * @return the number of nodes expanded.
	 */
	public int getNodesExpanded() {
		return metrics.getInt(NODES_EXPANDED);
	}

	/**
	 * Sets the number of nodes expanded.
	 * 
	 * @param nodesExpanded
	 *            the number of nodes expanded
	 */
	public void setNodesExpanded(int nodesExpanded) {
		metrics.set(NODES_EXPANDED, nodesExpanded);
	}

	/**
	 * Returns the queue size.
	 * 
	 * @return the queue size.
	 */
	public int getQueueSize() {
		return metrics.getInt(QUEUE_SIZE);
	}

	/**
	 * Sets the queue size and adjusts the maximum queue size if the specified
	 * size is greater than the current maximum.
	 * 
	 * @param queueSize
	 *            the number of items in the queue.
	 */
	public void setQueueSize(int queueSize) {
		metrics.set(QUEUE_SIZE, queueSize);
		int maxQSize = metrics.getInt(MAX_QUEUE_SIZE);
		if (queueSize > maxQSize) {
			metrics.set(MAX_QUEUE_SIZE, queueSize);
		}
	}

	/**
	 * Returns the maximum queue size.
	 * 
	 * @return the maximum queue size.
	 */
	public int getMaxQueueSize() {
		return metrics.getInt(MAX_QUEUE_SIZE);
	}

	/**
	 * Returns the cost of the path from the initial state to a goal state.
	 * 
	 * @return the cost of the path from the initial state to a goal state.
	 */
	public double getPathCost() {
		return metrics.getDouble(PATH_COST);
	}

	/**
	 * Sets the cost of the path from the initial state to a goal state.
	 * 
	 * @param pathCost
	 *            the cost of the path from the initial state to a goal state.
	 */
	public void setPathCost(Double pathCost) {
		metrics.set(PATH_COST, pathCost);
	}

	//
	// PRIVATE METHODS
	//
	private List<aa_i_Action> retrieveActions(a_Problem op, a_Problem rp,
			aa_TreeSearchNode originalPath, aa_TreeSearchNode reversePath) {
		List<aa_i_Action> actions = new ArrayList<aa_i_Action>();

		if (null == reversePath) {
			// This is the simple case whereby the path has been found
			// from the original problem first
			setPathCost(originalPath.getPathCost());
			searchOutcome = SearchOutcome.PATH_FOUND_FROM_ORIGINAL_PROBLEM;
			actions = aa_SearchUtils.actionsFromNodes(originalPath
					.getPathFromRoot());
		} else {
			List<aa_TreeSearchNode> nodePath = new ArrayList<aa_TreeSearchNode>();
			Object originalState = null;
			if (null != originalPath) {
				nodePath.addAll(originalPath.getPathFromRoot());
				originalState = originalPath.getState();
			}
			// Only append the reverse path if it is not the
			// GOAL state from the original problem (if you don't
			// you could end up appending a partial reverse path
			// that looks back on its initial state)
			if (!aa_SearchUtils.isGoalState(op, reversePath)) {
				List<aa_TreeSearchNode> rpath = reversePath.getPathFromRoot();
				for (int i = rpath.size() - 1; i >= 0; i--) {
					// Ensure do not include the node from the reverse path
					// that is the one that potentially overlaps with the
					// original path (i.e. if started in goal state or where
					// they meet in the middle).
					if (!rpath.get(i).getState().equals(originalState)) {
						nodePath.add(rpath.get(i));
					}
				}
			}

			if (!canTraversePathFromOriginalProblem(op, nodePath, actions)) {
				// This is where it is possible to get to the initial state
				// from the goal state (i.e. reverse path) but not the other way
				// round, null returned to indicate an invalid path found from
				// the reverse problem
				return null;
			}

			if (null == originalPath) {
				searchOutcome = SearchOutcome.PATH_FOUND_FROM_REVERSE_PROBLEM;
			} else {
				// Need to ensure that where the original and reverse paths
				// overlap, as they can link based on their fringes, that
				// the reverse path is actually capable of connecting to
				// the previous node in the original path (if not root).
				if (canConnectToOriginalFromReverse(rp, originalPath,
						reversePath)) {
					searchOutcome = SearchOutcome.PATH_FOUND_BETWEEN_PROBLEMS;
				} else {
					searchOutcome = SearchOutcome.PATH_FOUND_FROM_ORIGINAL_PROBLEM;
				}
			}
		}

		return actions;
	}

	private boolean canTraversePathFromOriginalProblem(a_Problem op,
			List<aa_TreeSearchNode> path, List<aa_i_Action> actions) {
		boolean rVal = true;
		double pc = 0.0;

		for (int i = 0; i < (path.size() - 1); i++) {
			Object currentState = path.get(i).getState();
			Object nextState = path.get(i + 1).getState();
			boolean found = false;
			for (aa_i_Action a : (Set<aa_i_Action>)op.getActionsFunction().actions(currentState)) {
				Object isNext = op.getResultFunction().result(currentState, a);
				if (nextState.equals(isNext)) {
					found = true;
					pc += op.getStepCostFunction()
							.c(currentState, a, nextState);
					actions.add(a);
					break;
				}
			}

			if (!found) {
				rVal = false;
				break;
			}
		}

		setPathCost(true == rVal ? pc : 0.0);

		return rVal;
	}

	private boolean canConnectToOriginalFromReverse(a_Problem rp,
			aa_TreeSearchNode originalPath, aa_TreeSearchNode reversePath) {
		boolean rVal = true;

		// Only need to test if not already at root
		if (!originalPath.isRootNode()) {
			rVal = false;
			for (aa_i_Action a : (Set<aa_i_Action>)rp.getActionsFunction().actions(reversePath.getState())) {
				Object nextState = rp.getResultFunction().result(
						reversePath.getState(), a);
				if (originalPath.getParent().getState().equals(nextState)) {
					rVal = true;
					break;
				}
			}
		}

		return rVal;
	}
}

class CachedStateQueue<E> extends d_QueueFIFO<E> {
	private static final long serialVersionUID = 1;
	//
	private Map<Object, aa_TreeSearchNode> cachedState = new HashMap<Object, aa_TreeSearchNode>();

	public CachedStateQueue() {
		super();
	}

	public CachedStateQueue(Collection<? extends E> c) {
		super(c);
	}

	public boolean containsNodeBasedOn(Object state) {
		return cachedState.containsKey(state);
	}

	public aa_TreeSearchNode getNodeBasedOn(Object state) {
		return cachedState.get(state);
	}

	//
	// START-Queue
	public E pop() {
		E popped = super.pop();
		cachedState.remove(((aa_TreeSearchNode) popped).getState());
		return popped;
	}

	// END-Queue
	//

	// Note: This is called by FIFOQueue.insert()->LinkedList.offer();
	@Override
	public boolean add(E element) {
		boolean added = super.add(element);
		if (added) {
			cachedState.put(((aa_TreeSearchNode) element).getState(), (aa_TreeSearchNode) element);
		}
		return added;
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		for (E element : c) {
			cachedState.put(((aa_TreeSearchNode) element).getState(), (aa_TreeSearchNode) element);
		}
		return super.addAll(c);
	}
}
