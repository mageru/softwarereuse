package reuze.test;
//package aima.test.core.unit.agent.impl;

import org.junit.Assert;
import org.junit.Test;

import reuze.aa_PerceptWithDynamicAttributes;

public class DynamicPerceptTest {

	@Test
	public void testToString() {
		aa_PerceptWithDynamicAttributes p = new aa_PerceptWithDynamicAttributes("key1", "value1");

		Assert.assertEquals("Percept[key1==value1]", p.toString());

		p = new aa_PerceptWithDynamicAttributes("key1", "value1", "key2", "value2");

		Assert.assertEquals("Percept[key1==value1, key2==value2]", p.toString());
	}

	@Test
	public void testEquals() {
		aa_PerceptWithDynamicAttributes p1 = new aa_PerceptWithDynamicAttributes();
		aa_PerceptWithDynamicAttributes p2 = new aa_PerceptWithDynamicAttributes();

		Assert.assertEquals(p1, p2);

		p1 = new aa_PerceptWithDynamicAttributes("key1", "value1");

		Assert.assertNotSame(p1, p2);

		p2 = new aa_PerceptWithDynamicAttributes("key1", "value1");

		Assert.assertEquals(p1, p2);
	}
}
