package edu.wm.cs.cs301.wordle.model;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;

import org.junit.jupiter.api.*;

class WordleResponseTest {
	WordleResponse item;
	
	@BeforeEach
	void setUp() {
		item = new WordleResponse('a', new Color(106, 170, 100), new Color(120, 124, 126));
	}
	
	@Test
	void testGetChar() {
		assertEquals('a', item.getChar());
	}

	@Test
	void testGetBackgroundColor() {
		assertEquals(new Color(106, 170, 100), item.getBackgroundColor());
	}

	@Test
	void testGetForegroundColor() {
		assertEquals(new Color(120, 124, 126), item.getForegroundColor());
	}

}
