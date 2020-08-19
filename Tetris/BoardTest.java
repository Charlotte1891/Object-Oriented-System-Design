package edu.stanford.cs108.tetris;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;


public class BoardTest {
	private Board newBoard;
	private Piece pyr1, pyr2, pyr3, pyr4;
	private Piece l1, st1, sqr1, s;
	
	
	@Before
	public void setUp()  throws Exception {	
		newBoard = new Board(3, 6);
		pyr1 = new Piece(Piece.PYRAMID_STR);
		pyr2 = pyr1.computeNextRotation();
		pyr3 = pyr2.computeNextRotation();
		pyr4 = pyr3.computeNextRotation();
		
		l1 = new Piece(Piece.L1_STR);
		st1 = Piece.getPieces()[0];
		sqr1 = Piece.getPieces()[5];
		s = new Piece(Piece.S1_STR);
		
		newBoard.place(pyr1, 0, 0);
		
	}

	@Test
	public void testCase1() {
		assertEquals(2, newBoard.dropHeight(l1, 0));
		assertEquals(2, newBoard.getColumnHeight(1));
		assertEquals(3, newBoard.getRowWidth(0));
		assertEquals(2, newBoard.getMaxHeight());
		
		newBoard.undo();
		
		assertEquals(0, newBoard.dropHeight(st1, 0));
		
		newBoard.place(st1, 0, 0);
		
		assertEquals(0, newBoard.dropHeight(st1, 1));
		assertEquals(4, newBoard.getColumnHeight(0));
		assertEquals(1, newBoard.getRowWidth(3));
		assertEquals(4, newBoard.getMaxHeight());
		assertEquals(true, newBoard.getGrid(0, 3));
		
		newBoard.undo();
		
		assertEquals(0, newBoard.dropHeight(s.computeNextRotation(), 1));
		assertEquals(0, newBoard.getMaxHeight());
		assertEquals(false, newBoard.getGrid(0, 3));
		
		newBoard.place(s, 0, 0);
		
		assertEquals(1, newBoard.dropHeight(st1, 0));
		assertEquals(2, newBoard.getColumnHeight(1));
		assertEquals(2, newBoard.getRowWidth(1));
		assertEquals(2, newBoard.getMaxHeight());
		assertEquals(true, newBoard.getGrid(1, 1));
		
		newBoard.undo();
		
		assertEquals(0, newBoard.getMaxHeight());
		assertEquals(false, newBoard.getGrid(1, 1));
	}
	
	@Test
	public void testCase2() {
		newBoard.commit();
		
	    assertEquals(2, newBoard.getColumnHeight(1));
		assertEquals(3, newBoard.getRowWidth(0));
		
		newBoard.place(st1, 0, 1);
		
		int rowsCleared = newBoard.clearRows();
		
		assertEquals(4, newBoard.getColumnHeight(0));
		assertEquals(1, newBoard.getColumnHeight(1));
		assertEquals(2, newBoard.getRowWidth(0));
		assertEquals(1, newBoard.getRowWidth(3));
		
		newBoard.undo();
		
		assertEquals(5, newBoard.getColumnHeight(0));
		assertEquals(3, newBoard.getRowWidth(0));
		
		newBoard.place(st1, 2, 1);
		
		assertEquals(5, newBoard.getColumnHeight(2));
		assertEquals(2, newBoard.getColumnHeight(1));
		assertEquals(3, newBoard.getRowWidth(0));
		assertEquals(2, newBoard.getRowWidth(3));		
	}
	
	@Test
	public void testCase3() {
		newBoard.commit();
		
	    assertEquals(2, newBoard.getColumnHeight(1));
		assertEquals(3, newBoard.getRowWidth(0));
		
		newBoard.place(st1, 0, 1);
		
		int rowsCleared = newBoard.clearRows();
		
		assertEquals(4, newBoard.getColumnHeight(0));
		assertEquals(1, newBoard.getColumnHeight(1));
		assertEquals(2, newBoard.getRowWidth(0));
		assertEquals(1, newBoard.getRowWidth(3));
		
		newBoard.commit();
		
		newBoard.place(st1, 2, 0);
		
		int rowsCleared2 = newBoard.clearRows();
		
		assertEquals(3, newBoard.getColumnHeight(0));
		assertEquals(0, newBoard.getColumnHeight(1));
		assertEquals(3, newBoard.getColumnHeight(2));
		assertEquals(2, newBoard.getRowWidth(0));
		assertEquals(2, newBoard.getRowWidth(1));		
		assertEquals(2, newBoard.getRowWidth(2));
	}
}
