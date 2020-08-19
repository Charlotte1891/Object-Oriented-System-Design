package edu.stanford.cs108.tetris;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

public class PieceTest2 {
	
	private Piece pyr1, pyr2, pyr3, pyr4; 
	private Piece l1, l2, l3, l4;
	private Piece st1, st2, st3, st4;
	private Piece sqr1, sqr2, sqr3, sqr4;
	private Piece s, sRotated;

	@Before
	public void setUp()  throws Exception {	
		pyr1 = new Piece(Piece.PYRAMID_STR);
		pyr2 = pyr1.computeNextRotation();
		pyr3 = pyr2.computeNextRotation();
		pyr4 = pyr3.computeNextRotation();
		
	    l1 = new Piece(Piece.L1_STR);
	    l2 = l1.computeNextRotation();
	    l3 = l2.computeNextRotation();
	    l4 = l3.computeNextRotation();
		
	    st1 = Piece.getPieces()[0];
	    st2 = st1.computeNextRotation();
	    st3 = st2.computeNextRotation();
	    st4 = st3.computeNextRotation();
	    
	    sqr1 = Piece.getPieces()[5];
	    sqr2 = sqr1.computeNextRotation();
	    sqr3 = sqr2.computeNextRotation();
	    sqr4 = sqr3.computeNextRotation();
	    
		s = new Piece(Piece.S1_STR);
		sRotated = s.computeNextRotation();
	}
	
	
	@Test
	public void testSampleSize(){
		assertEquals(3, pyr1.getWidth());
		assertEquals(2, l1.getWidth());
		assertEquals(1, st1.getWidth());
		assertEquals(2, sqr1.getWidth());
		assertEquals(3, s.getWidth());
		
		assertEquals(3, pyr2.getHeight());
		assertEquals(2, l2.getHeight());
		assertEquals(1, st2.getHeight());
		assertEquals(2, sqr2.getHeight());
		assertEquals(3, sRotated.getHeight());
	}
	
	
	@Test
	public void testSampleSkirt() {		
		assertTrue(Arrays.equals(new int[] {1, 0, 1}, pyr3.getSkirt()));
		assertTrue(Arrays.equals(new int[] {2, 0}, l3.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0}, st3.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0, 0}, sqr4.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0, 0, 1}, s.getSkirt()));		
	}
	
	
	@Test
	public void testEquals() {
		assertTrue(new Piece("0 0  0 1  0 2  0 3").equals(new Piece("0 1  0 3  0 0  0 2")));
		assertTrue(new Piece("0 0  0 1  0 2  1 0").equals(new Piece("0 1  0 2  0 0  1 0")));
		assertTrue(pyr1.equals(pyr4.computeNextRotation()));
	    assertFalse(pyr1.equals(pyr2));
		assertTrue(l1.equals(l4.computeNextRotation()));
		assertFalse(l1.equals(l2));
		assertTrue(st1.equals(st3));
	    assertFalse(st1.equals(st2));
		assertTrue(sqr1.equals(sqr2));
		assertTrue(sqr2.equals(sqr3));
		assertFalse(s.equals(sRotated));
	}
	
	@Test
	public void testFastRotations() {
		assertTrue(st2.equals(st1.fastRotation()));
		assertTrue(st1.equals(st1.fastRotation().fastRotation()));
		assertTrue(l2.equals(Piece.getPieces()[1].fastRotation()));
		assertTrue(l1.equals(Piece.getPieces()[1].fastRotation().fastRotation().fastRotation().fastRotation()));
		assertTrue(pyr3.equals(Piece.getPieces()[6].fastRotation().fastRotation()));
	}


}
