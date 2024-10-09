package edu.wm.cs.cs301.shapes;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RectangleTest {

	@Test
	void testArea() {
		Rectangle r = new Rectangle();
		
		double expectedArea = 1.0;
		double actualArea = r.area();
		
		assertEquals(expectedArea, actualArea);
	}
	
	@Test
	void testPerimeter() {
		Rectangle r = new Rectangle(2.5, 3.5);
		
		double expectedPerimeter = 2.5 + 2.5 + 3.5 + 3.5;
		double actualPerimeter = r.perimeter();
		
		assertEquals(expectedPerimeter, actualPerimeter);
	}
	
	@Test
	void testSetSize() {
		//no get size method provided, so we need to verify indirectly
		Rectangle r = new Rectangle(1.0, 1.0);
		
		r.setSize(3.0, 4.0);
		double expectedArea = 12.0;			//hand calculation
		double expectedPerimeter = 14.0;	//hand calculation
		
		assertEquals(expectedArea, r.area());
		assertEquals(expectedPerimeter, r.perimeter());
	}

	@Test
	void testExpectedExceptionConstructor() {
		try {
			new Rectangle(-1.0, -1.0);
			fail("IllegalArgumentException expected but not thrown.");
		} catch(IllegalArgumentException iae) {
			assertEquals("Cannot have 0 or negative length or width", iae.getMessage());
		}
	}

	@Test
	void testExpectedExceptionSetSize() {
		Rectangle r = new Rectangle(1.0, 1.0);
		IllegalArgumentException thrown = 
				assertThrows(IllegalArgumentException.class, () -> {
					r.setSize(-1.0, 1.0);
	  });
	  assertEquals("Cannot have 0 or negative length or width", thrown.getMessage());
	}

}
