package reuze.test;
//package aima.test.core.unit.probability.hmm.exact;

import org.junit.Before;
import org.junit.Test;

import reuze.mp_HMMInferenceForwardBackward;

//import aima.core.probability.example.HMMExampleFactory;
//import aima.core.probability.hmm.exact.HMMForwardBackward;
//import aima.test.core.unit.probability.temporal.CommonForwardBackwardTest;

/**
 * 
 * @author Ciaran O'Reilly
 *
 */
public class HMMForwardBackwardTest extends CommonForwardBackwardTest {

	//
	private mp_HMMInferenceForwardBackward uw = null;

	@Before
	public void setUp() {
		uw = new mp_HMMInferenceForwardBackward(HMMExampleFactory.getUmbrellaWorldModel());
	}

	@Test
	public void testForwardStep_UmbrellaWorld() {
		super.testForwardStep_UmbrellaWorld(uw);
	}

	@Test
	public void testBackwardStep_UmbrellaWorld() {
		super.testBackwardStep_UmbrellaWorld(uw);
	}

	@Test
	public void testForwardBackward_UmbrellaWorld() {
		super.testForwardBackward_UmbrellaWorld(uw);
	}
}
