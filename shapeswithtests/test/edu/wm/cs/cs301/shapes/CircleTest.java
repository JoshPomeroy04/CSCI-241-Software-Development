package edu.wm.cs.cs301.shapes;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CircleTest {

	@Test
	void testArea() {
		Circle c = new Circle();
		
		double expectedArea = Math.PI * 1.0 * 1.0;
		double actualArea = c.area();
		
		assertEquals(expectedArea, actualArea);
	}
	
	@Test
	void testPerimeter() {
		Circle c = new Circle(2.0);
		
		double expectedPerimeter = 2.0 * Math.PI * 2.0;
		double actualPerimeter = c.perimeter();
		
		assertEquals(expectedPerimeter, actualPerimeter);
	}
	
	@Test
	void testSetSize() {
		//no get size method provided, so we need to verify indirectly
		Circle c = new Circle(1.0);
		
		c.setSize(10.0);
		double actualArea = Math.PI * 100.0;
		double actualPerimeter = 2.0 * Math.PI * 10.0;	//hand calculation
		
		assertEquals(actualArea, c.area());
		assertEquals(actualPerimeter, c.perimeter());
	}
		
	@Test
	void testExpectedExceptionConstructor() {
		try {
			new Circle(-1.0);
			fail("IllegalArgumentException expected but not thrown.");
		} catch(IllegalArgumentException iae) {
			assertEquals("Cannot have 0 or negative radius", iae.getMessage());
		}
	}

	@Test
	void testExpectedExceptionSetSize() {
		Circle c = new Circle(1.0);
		IllegalArgumentException thrown = 
				assertThrows(IllegalArgumentException.class, () -> {
					c.setSize(-1.0);
	  });
	  assertEquals("Cannot have 0 or negative radius", thrown.getMessage());
	}
	
}
