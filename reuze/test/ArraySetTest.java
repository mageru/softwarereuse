package reuze.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import reuze.d_ArraySet;


public class ArraySetTest {

	@Before
	public void setUp() throws Exception {
		
	}
	
	@Test
	public void testSize() {
		d_ArraySet test = new d_ArraySet();
		test.add(1);
		test.add(2);
		test.add(3);
		Assert.assertTrue(test.size()==3);
		test.add(4);
		Assert.assertTrue(test.size()==4);
	}

	@Test
	public void testAddE() {
		d_ArraySet test = new d_ArraySet();
		test.add(1);
		Assert.assertTrue(test.contains(1));
	}

	@Test
	public void testContainsAny() {
		d_ArraySet test = new d_ArraySet();
		test.add(1);
		test.add(3);
		test.add(5);
		List<Integer> numbers = new ArrayList<Integer>();
		numbers.add(5);
		List<Integer> nonumbers = new ArrayList<Integer>();
		numbers.add(6);		
		Assert.assertTrue(test.containsAny(numbers));
		Assert.assertFalse(test.containsAny(nonumbers));
	}

	@Test
	public void testGet() {
		d_ArraySet test = new d_ArraySet();
		test.add(1);
		test.add(3);
		test.add(5);
		Assert.assertTrue(test.get(0).equals(1));
		Assert.assertFalse(test.get(0).equals(0));
	}


}
