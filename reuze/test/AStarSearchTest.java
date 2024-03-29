package reuze.test;
//package aima.test.core.unit.search.informed;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import reuze.aa_i_Action;
import reuze.dg_EightPuzzleBoard;
import reuze.dg_EightPuzzleFunctionFactory;
import reuze.dg_EightPuzzleGoalTest;
import reuze.dg_EightPuzzleManhattanHeuristicFunction;
import reuze.d_MapExtendable;
import reuze.d_i_Map;
import reuze.aa_ActionsFunctionMap;
import reuze.aa_CostFunctionMapStep;
import reuze.d_MapRomaniaRoadsSimplified;
import reuze.apm_HeuristicAdaptableStraightLineDistance;
import reuze.a_GoalTestDefault;
import reuze.dag_GraphSearch;
import reuze.m_i_HeuristicFunction;
import reuze.a_Problem;
import reuze.aa_TreeSearchNodeExpanderQueue;
import reuze.das_i_SearchProblem;
import reuze.aa_AgentSearch;
import reuze.aa_TreeSearch;
import reuze.das_TreeAstar;

public class AStarSearchTest {

	@Test
	public void testAStarSearch() {
		// added to narrow down bug report filed by L.N.Sudarshan of
		// Thoughtworks and Xin Lu of UCI
		try {
			// EightPuzzleBoard extreme = new EightPuzzleBoard(new int[]
			// {2,0,5,6,4,8,3,7,1});
			// EightPuzzleBoard extreme = new EightPuzzleBoard(new int[]
			// {0,8,7,6,5,4,3,2,1});
			dg_EightPuzzleBoard board = new dg_EightPuzzleBoard(new int[] { 7, 1, 8,
					0, 4, 6, 2, 3, 5 });

			a_Problem problem = new a_Problem(board,
					dg_EightPuzzleFunctionFactory.getActionsFunction(),
					dg_EightPuzzleFunctionFactory.getResultFunction(),
					new dg_EightPuzzleGoalTest());
			das_i_SearchProblem search = new das_TreeAstar(new dag_GraphSearch(),
					new dg_EightPuzzleManhattanHeuristicFunction());
			aa_AgentSearch agent = new aa_AgentSearch(problem, search);
			Assert.assertEquals(23, agent.getActions().size());
			Assert.assertEquals("926",
					agent.getInstrumentation().getProperty("nodesExpanded"));
			Assert.assertEquals("534",
					agent.getInstrumentation().getProperty("queueSize"));
			Assert.assertEquals("535",
					agent.getInstrumentation().getProperty("maxQueueSize"));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Exception thrown");
		}
	}

	@Test
	public void testAIMA3eFigure3_15() throws Exception {
		d_i_Map romaniaMap = new d_MapRomaniaRoadsSimplified();
		a_Problem problem = new a_Problem(d_MapRomaniaRoadsSimplified.SIBIU,
				aa_ActionsFunctionMap.getActionsFunction(romaniaMap),
				aa_ActionsFunctionMap.getResultFunction(), new a_GoalTestDefault(
						d_MapRomaniaRoadsSimplified.BUCHAREST),
				new aa_CostFunctionMapStep(romaniaMap));

		das_i_SearchProblem search = new das_TreeAstar(new dag_GraphSearch(),
				new apm_HeuristicAdaptableStraightLineDistance(
						d_MapRomaniaRoadsSimplified.BUCHAREST, romaniaMap));
		aa_AgentSearch agent = new aa_AgentSearch(problem, search);

		List<aa_i_Action> actions = agent.getActions();

		Assert.assertEquals(
				"[Action[name==moveTo, location==RimnicuVilcea], Action[name==moveTo, location==Pitesti], Action[name==moveTo, location==Bucharest]]",
				actions.toString());
		Assert.assertEquals("278.0",
				search.getMetrics().get(aa_TreeSearchNodeExpanderQueue.METRIC_PATH_COST));
	}

	@Test
	public void testAIMA3eFigure3_24() throws Exception {
		d_i_Map romaniaMap = new d_MapRomaniaRoadsSimplified();
		a_Problem problem = new a_Problem(d_MapRomaniaRoadsSimplified.ARAD,
				aa_ActionsFunctionMap.getActionsFunction(romaniaMap),
				aa_ActionsFunctionMap.getResultFunction(), new a_GoalTestDefault(
						d_MapRomaniaRoadsSimplified.BUCHAREST),
				new aa_CostFunctionMapStep(romaniaMap));

		das_i_SearchProblem search = new das_TreeAstar(new aa_TreeSearch(),
				new apm_HeuristicAdaptableStraightLineDistance(
						d_MapRomaniaRoadsSimplified.BUCHAREST, romaniaMap));
		aa_AgentSearch agent = new aa_AgentSearch(problem, search);
		Assert.assertEquals(
				"[Action[name==moveTo, location==Sibiu], Action[name==moveTo, location==RimnicuVilcea], Action[name==moveTo, location==Pitesti], Action[name==moveTo, location==Bucharest]]",
				agent.getActions().toString());
		Assert.assertEquals(4, agent.getActions().size());
		Assert.assertEquals("5",
				agent.getInstrumentation().getProperty("nodesExpanded"));
		Assert.assertEquals("10",
				agent.getInstrumentation().getProperty("queueSize"));
		Assert.assertEquals("11",
				agent.getInstrumentation().getProperty("maxQueueSize"));
	}

	@Test
	public void testAIMA3eFigure3_24_using_GraphSearch() throws Exception {
		d_i_Map romaniaMap = new d_MapRomaniaRoadsSimplified();
		a_Problem problem = new a_Problem(d_MapRomaniaRoadsSimplified.ARAD,
				aa_ActionsFunctionMap.getActionsFunction(romaniaMap),
				aa_ActionsFunctionMap.getResultFunction(), new a_GoalTestDefault(
						d_MapRomaniaRoadsSimplified.BUCHAREST),
				new aa_CostFunctionMapStep(romaniaMap));

		das_i_SearchProblem search = new das_TreeAstar(new dag_GraphSearch(),
				new apm_HeuristicAdaptableStraightLineDistance(
						d_MapRomaniaRoadsSimplified.BUCHAREST, romaniaMap));
		aa_AgentSearch agent = new aa_AgentSearch(problem, search);
		Assert.assertEquals(
				"[Action[name==moveTo, location==Sibiu], Action[name==moveTo, location==RimnicuVilcea], Action[name==moveTo, location==Pitesti], Action[name==moveTo, location==Bucharest]]",
				agent.getActions().toString());
		Assert.assertEquals(4, agent.getActions().size());
		Assert.assertEquals("5",
				agent.getInstrumentation().getProperty("nodesExpanded"));
		Assert.assertEquals("4",
				agent.getInstrumentation().getProperty("queueSize"));
		Assert.assertEquals("6",
				agent.getInstrumentation().getProperty("maxQueueSize"));
	}

	@Test
	public void testCheckFrontierPathCost() throws Exception {
		d_MapExtendable map = new d_MapExtendable();
		map.addBidirectionalLink("start", "b", 2.5);
		map.addBidirectionalLink("start", "c", 1.0);
		map.addBidirectionalLink("b", "d", 2.0);
		map.addBidirectionalLink("c", "d", 4.0);
		map.addBidirectionalLink("c", "e", 1.0);
		map.addBidirectionalLink("d", "goal", 1.0);
		map.addBidirectionalLink("e", "goal", 5.0);
		a_Problem problem = new a_Problem("start",
				aa_ActionsFunctionMap.getActionsFunction(map),
				aa_ActionsFunctionMap.getResultFunction(), new a_GoalTestDefault(
						"goal"), new aa_CostFunctionMapStep(map));

		m_i_HeuristicFunction hf = new m_i_HeuristicFunction() {
			public double h(Object state) {
				return 0; // Don't have one for this test
			}
		};
		das_i_SearchProblem search = new das_TreeAstar(new dag_GraphSearch(), hf);
		aa_AgentSearch agent = new aa_AgentSearch(problem, search);

		List<aa_i_Action> actions = agent.getActions();

		Assert.assertEquals(
				"[Action[name==moveTo, location==b], Action[name==moveTo, location==d], Action[name==moveTo, location==goal]]",
				actions.toString());
		Assert.assertEquals("5.5",
				search.getMetrics().get(aa_TreeSearchNodeExpanderQueue.METRIC_PATH_COST));
	}
}
