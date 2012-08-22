package reuze.test;
//package aima.test.core.unit.search.framework;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import reuze.aa_i_Action;
import reuze.d_i_Map;
import reuze.aa_ActionsFunctionMap;
import reuze.aa_CostFunctionMapStep;
import reuze.d_MapRomaniaRoadsSimplified;
import reuze.dag_GraphSearch;
import reuze.a_Problem;
import reuze.das_i_SearchProblem;
import reuze.aa_AgentSearch;
import reuze.aa_SolutionChecker;
import reuze.das_SearchProblemBreadthFirst;

public class SolutionCheckerTest {

	@Test
	public void testMultiGoalProblem() throws Exception {
		d_i_Map romaniaMap = new d_MapRomaniaRoadsSimplified();
		a_Problem problem = new a_Problem(d_MapRomaniaRoadsSimplified.ARAD,
				aa_ActionsFunctionMap.getActionsFunction(romaniaMap),
				aa_ActionsFunctionMap.getResultFunction(), new DualMapGoalTest(
						d_MapRomaniaRoadsSimplified.BUCHAREST,
						d_MapRomaniaRoadsSimplified.HIRSOVA),
				new aa_CostFunctionMapStep(romaniaMap));

		das_i_SearchProblem search = new das_SearchProblemBreadthFirst(new dag_GraphSearch());

		aa_AgentSearch agent = new aa_AgentSearch(problem, search);
		Assert.assertEquals(
				"[Action[name==moveTo, location==Sibiu], Action[name==moveTo, location==Fagaras], Action[name==moveTo, location==Bucharest], Action[name==moveTo, location==Urziceni], Action[name==moveTo, location==Hirsova]]",
				agent.getActions().toString());
		Assert.assertEquals(5, agent.getActions().size());
		Assert.assertEquals("14",
				agent.getInstrumentation().getProperty("nodesExpanded"));
		Assert.assertEquals("1",
				agent.getInstrumentation().getProperty("queueSize"));
		Assert.assertEquals("5",
				agent.getInstrumentation().getProperty("maxQueueSize"));
	}

	class DualMapGoalTest implements aa_SolutionChecker {
		public String goalState1 = null;
		public String goalState2 = null;

		private Set<String> goals = new HashSet<String>();

		public DualMapGoalTest(String goalState1, String goalState2) {
			this.goalState1 = goalState1;
			this.goalState2 = goalState2;
			goals.add(goalState1);
			goals.add(goalState2);
		}

		public boolean isGoalState(Object state) {
			return goalState1.equals(state) || goalState2.equals(state);
		}

		public boolean isAcceptableSolution(List<aa_i_Action> actions, Object goal) {
			goals.remove(goal);
			return goals.isEmpty();
		}
	}
}
