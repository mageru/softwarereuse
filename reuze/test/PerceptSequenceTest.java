package reuze.test;
//package aima.test.core.unit.agent.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import reuze.aa_i_Percept;
import reuze.aa_PerceptWithDynamicAttributes;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class PerceptSequenceTest {

	@Test
	public void testToString() {
		List<aa_i_Percept> ps = new ArrayList<aa_i_Percept>();
		ps.add(new aa_PerceptWithDynamicAttributes("key1", "value1"));

		Assert.assertEquals("[Percept[key1==value1]]", ps.toString());

		ps.add(new aa_PerceptWithDynamicAttributes("key1", "value1", "key2", "value2"));

		Assert.assertEquals(
				"[Percept[key1==value1], Percept[key1==value1, key2==value2]]",
				ps.toString());
	}

	@Test
	public void testEquals() {
		List<aa_i_Percept> ps1 = new ArrayList<aa_i_Percept>();
		List<aa_i_Percept> ps2 = new ArrayList<aa_i_Percept>();

		Assert.assertEquals(ps1, ps2);

		ps1.add(new aa_PerceptWithDynamicAttributes("key1", "value1"));

		Assert.assertNotSame(ps1, ps2);

		ps2.add(new aa_PerceptWithDynamicAttributes("key1", "value1"));

		Assert.assertEquals(ps1, ps2);
	}
}
