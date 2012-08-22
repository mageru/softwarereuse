package reuze.test;
//package aima.test.core.unit.search.csp;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import reuze.ac_OpsAssignmentVariables;
import reuze.ac_StrategyBacktracking;
import reuze.ac_CSP;
import reuze.ac_Map;
import reuze.ac_SolutionStrategyMinConflicts;

/**
 * @author Ravi Mohan
 * 
 */
public class MapCSPTest {
	private ac_CSP csp;

	@Before
	public void setUp() {
		csp = new ac_Map();
	}

	@Test
	public void testBackTrackingSearch() {
		ac_OpsAssignmentVariables results = new ac_StrategyBacktracking().solve(csp);
		Assert.assertNotNull(results);
		Assert.assertEquals(ac_Map.GREEN, results.getAssignment(ac_Map.WA));
		Assert.assertEquals(ac_Map.RED, results.getAssignment(ac_Map.NT));
		Assert.assertEquals(ac_Map.BLUE, results.getAssignment(ac_Map.SA));
		Assert.assertEquals(ac_Map.GREEN, results.getAssignment(ac_Map.Q));
		Assert.assertEquals(ac_Map.RED, results.getAssignment(ac_Map.NSW));
		Assert.assertEquals(ac_Map.GREEN, results.getAssignment(ac_Map.V));
		Assert.assertEquals(ac_Map.RED, results.getAssignment(ac_Map.T));
	}

	@Test
	public void testMCSearch() {
		new ac_SolutionStrategyMinConflicts(100).solve(csp);
	}
}
