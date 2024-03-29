package reuze.test;
//package aima.test.core.unit.search.online;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import reuze.aa_i_Action;
import reuze.aa_i_Agent;
import reuze.aa_i_EnvironmentState;
import reuze.aa_i_EnvironmentView;
import reuze.d_MapExtendable;
import reuze.aa_EnvironmentMap;
import reuze.aa_ActionsFunctionMap;
import reuze.aa_CostFunctionMapStep;
import reuze.a_GoalTestDefault;
import reuze.aa_AgentDFSOnline;
import reuze.aa_SearchProblemOnline;

public class OnlineDFSAgentTest {

	d_MapExtendable aMap;

	StringBuffer envChanges;

	@Before
	public void setUp() {
		aMap = new d_MapExtendable();
		aMap.addBidirectionalLink("A", "B", 5.0);
		aMap.addBidirectionalLink("A", "C", 6.0);
		aMap.addBidirectionalLink("B", "D", 4.0);
		aMap.addBidirectionalLink("B", "E", 7.0);
		aMap.addBidirectionalLink("D", "F", 4.0);
		aMap.addBidirectionalLink("D", "G", 8.0);

		envChanges = new StringBuffer();
	}

	@Test
	public void testAlreadyAtGoal() {
		aa_EnvironmentMap me = new aa_EnvironmentMap(aMap);
		aa_AgentDFSOnline agent = new aa_AgentDFSOnline(new aa_SearchProblemOnline(
				aa_ActionsFunctionMap.getActionsFunction(aMap),
				new a_GoalTestDefault("A"), new aa_CostFunctionMapStep(aMap)),
				aa_ActionsFunctionMap.getPerceptToStateFunction());
		me.addAgent(agent, "A");
		me.addEnvironmentView(new TestEnvironmentView());
		me.stepUntilDone();

		Assert.assertEquals("Action[name==NoOp]->", envChanges.toString());
	}

	@Test
	public void testNormalSearch() {
		aa_EnvironmentMap me = new aa_EnvironmentMap(aMap);
		aa_AgentDFSOnline agent = new aa_AgentDFSOnline(new aa_SearchProblemOnline(
				aa_ActionsFunctionMap.getActionsFunction(aMap),
				new a_GoalTestDefault("G"), new aa_CostFunctionMapStep(aMap)),
				aa_ActionsFunctionMap.getPerceptToStateFunction());
		me.addAgent(agent, "A");
		me.addEnvironmentView(new TestEnvironmentView());
		me.stepUntilDone();

		Assert.assertEquals(
				"Action[name==moveTo, location==B]->Action[name==moveTo, location==A]->Action[name==moveTo, location==C]->Action[name==moveTo, location==A]->Action[name==moveTo, location==C]->Action[name==moveTo, location==A]->Action[name==moveTo, location==B]->Action[name==moveTo, location==D]->Action[name==moveTo, location==B]->Action[name==moveTo, location==E]->Action[name==moveTo, location==B]->Action[name==moveTo, location==E]->Action[name==moveTo, location==B]->Action[name==moveTo, location==D]->Action[name==moveTo, location==F]->Action[name==moveTo, location==D]->Action[name==moveTo, location==G]->Action[name==NoOp]->",
				envChanges.toString());
	}

	@Test
	public void testNoPath() {
		aMap = new d_MapExtendable();
		aMap.addBidirectionalLink("A", "B", 1.0);
		aa_EnvironmentMap me = new aa_EnvironmentMap(aMap);
		aa_AgentDFSOnline agent = new aa_AgentDFSOnline(new aa_SearchProblemOnline(
				aa_ActionsFunctionMap.getActionsFunction(aMap),
				new a_GoalTestDefault("X"), new aa_CostFunctionMapStep(aMap)),
				aa_ActionsFunctionMap.getPerceptToStateFunction());
		me.addAgent(agent, "A");
		me.addEnvironmentView(new TestEnvironmentView());

		me.stepUntilDone();

		Assert.assertEquals(
				"Action[name==moveTo, location==B]->Action[name==moveTo, location==A]->Action[name==moveTo, location==B]->Action[name==moveTo, location==A]->Action[name==NoOp]->",
				envChanges.toString());
	}

	@Test
	public void testAIMA3eFig4_19() {
		aMap = new d_MapExtendable();
		aMap.addBidirectionalLink("1,1", "1,2", 1.0);
		aMap.addBidirectionalLink("1,1", "2,1", 1.0);
		aMap.addBidirectionalLink("2,1", "3,1", 1.0);
		aMap.addBidirectionalLink("2,1", "2,2", 1.0);
		aMap.addBidirectionalLink("3,1", "3,2", 1.0);
		aMap.addBidirectionalLink("2,2", "2,3", 1.0);
		aMap.addBidirectionalLink("3,2", "3,3", 1.0);
		aMap.addBidirectionalLink("2,3", "1,3", 1.0);

		aa_EnvironmentMap me = new aa_EnvironmentMap(aMap);
		aa_AgentDFSOnline agent = new aa_AgentDFSOnline(new aa_SearchProblemOnline(
				aa_ActionsFunctionMap.getActionsFunction(aMap),
				new a_GoalTestDefault("3,3"), new aa_CostFunctionMapStep(aMap)),
				aa_ActionsFunctionMap.getPerceptToStateFunction());
		me.addAgent(agent, "1,1");
		me.addEnvironmentView(new TestEnvironmentView());
		me.stepUntilDone();

		Assert.assertEquals(
				"Action[name==moveTo, location==1,2]->Action[name==moveTo, location==1,1]->Action[name==moveTo, location==2,1]->Action[name==moveTo, location==1,1]->Action[name==moveTo, location==2,1]->Action[name==moveTo, location==2,2]->Action[name==moveTo, location==2,1]->Action[name==moveTo, location==3,1]->Action[name==moveTo, location==2,1]->Action[name==moveTo, location==3,1]->Action[name==moveTo, location==3,2]->Action[name==moveTo, location==3,1]->Action[name==moveTo, location==3,2]->Action[name==moveTo, location==3,3]->Action[name==NoOp]->",
				envChanges.toString());
	}

	private class TestEnvironmentView implements aa_i_EnvironmentView {
		public void notify(String msg) {
			envChanges.append(msg).append("->");
		}

		public void agentAdded(aa_i_Agent agent, aa_i_EnvironmentState state) {
			// Nothing.
		}

		public void agentActed(aa_i_Agent agent, aa_i_Action action,
				aa_i_EnvironmentState state) {
			envChanges.append(action).append("->");
		}
	}
}
