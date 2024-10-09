package edu.wm.cs.cs301.wordle.model;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;

import org.junit.jupiter.api.*;

class WordleModelTest {
	WordleModel model;
	
	@BeforeEach
	void setUp() {
		model = new WordleModel();
	}
	
	@AfterEach
	void tearDown() {
		model = null;
	}
	
	@Test
	void testGenerateCurrentWord() {
		model.generateCurrentWord();
		assertTrue(model.getCurrentWord().length() == model.getColumnCount());
	}

	@Test
	void testGetCurrentWord() {
		assertTrue(model.getCurrentWord() != null);
	}

	@Test
	void testSetCurrentColumn() {
		model.setCurrentColumn('a');
		assertEquals(0, model.getCurrentColumn());
		
		for (int i = model.getColumnCount()-1; i > 0; i--) {
			model.setCurrentColumn('a');
		}
		
		assertEquals(model.getColumnCount()-1, model.getCurrentColumn());
	}

	@Test
	void testBackspace() {
		model.backspace();
		assertEquals(-1, model.getCurrentColumn());
		
		for (int i = model.getColumnCount(); i > 0; i--) {
			model.setCurrentColumn('a');
		}
		model.backspace();
		assertEquals(model.getColumnCount()-2, model.getCurrentColumn());
	}

	@Test
	void testGetCurrentRow() {
		assertThrows(ArrayIndexOutOfBoundsException.class, () -> {model.getCurrentRow();});
		for (int i = model.getColumnCount(); i > 0; i--) {
			model.setCurrentColumn('a');
		}
		model.setCurrentRow();
		for (int i = 0; i < model.getColumnCount(); i++) {
			assertEquals('a', model.getCurrentRow()[i].getChar());
		}
	}

	@Test
	void testGetCurrentRowNumber() {
		assertEquals(-1, model.getCurrentRowNumber());
		model.setCurrentRow();
		assertEquals(0, model.getCurrentRowNumber());
		
		for (int i = 0; i < model.getMaximumRows()-1; i++) {
			model.setCurrentRow();
		}
		assertEquals(model.getMaximumRows()-1, model.getCurrentRowNumber());
	}

	@Test
	void testSetCurrentRow() {
		for (int i = 0; i < model.getColumnCount(); i++) {
			model.setCurrentColumn(model.getCurrentWord().charAt(i));
		}
		assertTrue(model.setCurrentRow());
		
		for (int i = 0; i < model.getColumnCount(); i++) {
			assertEquals(new Color(106, 170, 100), model.getCurrentRow()[i].getBackgroundColor());
		}
		
		for (int i = 0; i < model.getMaximumRows()-2; i++) {
			model.setCurrentRow();
		}
		assertTrue(!model.setCurrentRow());
		assertThrows(ArrayIndexOutOfBoundsException.class, () -> {model.setCurrentRow();});
	}

	@Test
	void testGetWordleGrid() {
		assertTrue(model.getWordleGrid() instanceof WordleResponse[][]);
	}

	@Test
	void testGetMaximumRows() {
		assertTrue((model.getMaximumRows() == 6)||(model.getMaximumRows() == 4)||(model.getMaximumRows() == 7));
	}

	@Test
	void testGetColumnCount() {
		assertTrue((model.getColumnCount() == 5)||(model.getColumnCount() == 3)||(model.getColumnCount() == 7));
	}

	@Test
	void testGetCurrentColumn() {
		model.setCurrentColumn('a');
		assertEquals(0, model.getCurrentColumn());
		model.backspace();
		assertEquals(-1, model.getCurrentColumn());
	}

	@Test
	void testGetTotalWordCount() {
		assertTrue(model.getTotalWordCount() > 0);
	}

}
