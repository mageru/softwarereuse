package reuze;

import java.util.List;

import reuze.aa_i_Action;
import reuze.d_Metrics;
import reuze.mpb_i_Node;
import reuze.a_Problem;
import reuze.aa_TreeSearchNodeExpanderQueue;
import reuze.das_i_SearchProblem;
import reuze.d_QueueLIFO;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 85.<br>
 * <br>
 * Depth-first search always expands the deepest node in the current frontier of
 * the search tree. <br>
 * <br>
 * <b>Note:</b> Supports both Tree and Graph based versions by assigning an
 * instance of TreeSearch or GraphSearch to its constructor.
 * 
 * @author Ravi Mohan
 * 
 */
public class das_SearchProblemDepthFirst implements das_i_SearchProblem {

	aa_TreeSearchNodeExpanderQueue search;

	public das_SearchProblemDepthFirst(aa_TreeSearchNodeExpanderQueue search) {
		this.search = search;
	}

	public List<aa_i_Action> search(a_Problem p) {
		return search.search(p, new d_QueueLIFO<aa_TreeSearchNode>());
	}

	public d_Metrics getMetrics() {
		return search.getMetrics();
	}
}