package reuze.test;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import reuze.aa_i_Action;
/*import aima.core.environment.nqueens.NQueensBoard;
import aima.core.environment.nqueens.NQueensFunctionFactory;
import aima.core.environment.nqueens.NQueensGoalTest;*/
import reuze.dg_NQueensBoard;
import reuze.dg_NQueensFunctionFactory;
import reuze.dg_NQueensGoalTest;
import reuze.a_Problem;
import reuze.das_i_SearchProblem;
import reuze.aa_AgentSearch;
import reuze.das_SearchProblemIterativeDeepening;

public class IterativeDeepeningSearchTest {

	@Test
	public void testIterativeDeepeningSearch() {
		try {
			a_Problem problem = new a_Problem(new dg_NQueensBoard(8),
					dg_NQueensFunctionFactory.getIActionsFunction(),
					dg_NQueensFunctionFactory.getResultFunction(),
					new dg_NQueensGoalTest());
			das_i_SearchProblem search = new das_SearchProblemIterativeDeepening();
			aa_AgentSearch agent = new aa_AgentSearch(problem, search);
			List<aa_i_Action> actions = agent.getActions();
			assertCorrectPlacement(actions);
			Assert.assertEquals("3656",
					agent.getInstrumentation().getProperty("nodesExpanded"));

		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Exception should not occur");
		}
	}

	private void assertCorrectPlacement(List<aa_i_Action> actions) {
		Assert.assertEquals(8, actions.size());
		Assert.assertEquals(
				"Action[name==placeQueenAt, location== ( 0 , 0 ) ]", actions
						.get(0).toString());
		Assert.assertEquals(
				"Action[name==placeQueenAt, location== ( 1 , 4 ) ]", actions
						.get(1).toString());
		Assert.assertEquals(
				"Action[name==placeQueenAt, location== ( 2 , 7 ) ]", actions
						.get(2).toString());
		Assert.assertEquals(
				"Action[name==placeQueenAt, location== ( 3 , 5 ) ]", actions
						.get(3).toString());
		Assert.assertEquals(
				"Action[name==placeQueenAt, location== ( 4 , 2 ) ]", actions
						.get(4).toString());
		Assert.assertEquals(
				"Action[name==placeQueenAt, location== ( 5 , 6 ) ]", actions
						.get(5).toString());
		Assert.assertEquals(
				"Action[name==placeQueenAt, location== ( 6 , 1 ) ]", actions
						.get(6).toString());
		Assert.assertEquals(
				"Action[name==placeQueenAt, location== ( 7 , 3 ) ]", actions
						.get(7).toString());
	}
}