package reuze;

import reuze.a_GoalTest;

/**
 * @author Ravi Mohan
 * 
 */
public class dg_EightPuzzleGoalTest implements a_GoalTest {
	dg_EightPuzzleBoard goal = new dg_EightPuzzleBoard(new int[] { 0, 1, 2, 3, 4, 5,
			6, 7, 8 });

	public boolean isGoalState(Object state) {
		dg_EightPuzzleBoard board = (dg_EightPuzzleBoard) state;
		return board.equals(goal);
	}
}