// Board.java
package edu.stanford.cs108.tetris;

/**
 CS108 Tetris Board.
 Represents a Tetris board -- essentially a 2-d grid
 of booleans. Supports tetris pieces and row clearing.
 Has an "undo" feature that allows clients to add and remove pieces efficiently.
 Does not do any drawing or have any idea of pixels. Instead,
 just represents the abstract 2-d board.
*/
public class Board	{
	// Some ivars are stubbed out for you:
	private int width;
	private int height;
	private int maxHeight;
	private boolean[][] grid;
	private boolean DEBUG = true;
	boolean committed;
	
	private boolean[] [] xGrid; // copy of original grid
	int[] widths; // stores how many filled spots there are in each row
	int[] heights; // stores the height to which each column has been filled
	int[] xWidths; //copy of widths
	int[] xHeights; // copy of heights
	private int xMaxHeight; // copy of maxHeight;

	// Here a few trivial methods are provided:
	
	/**
	 Creates an empty board of the given width and height
	 measured in blocks.
	*/
	public Board(int width, int height) {
		this.width = width;
		this.height = height;
		grid = new boolean[width][height];
		committed = true;
		
		widths = new int[height];
		heights = new int[width];
		
		// make a copy for undo()
		xGrid = new boolean[width][height];
		xWidths = new int[height];
		xHeights = new int[width];
	}
	
	
	/**
	 Returns the width of the board in blocks.
	*/
	public int getWidth() {
		return width;
	}
	
	
	/**
	 Returns the height of the board in blocks.
	*/
	public int getHeight() {
		return height;
	}
	
	
	/**
	 Returns the max column height present in the board.
	 For an empty board this is 0.
	*/
	public int getMaxHeight() {
		maxHeight = 0;
		for(int i = 0; i < heights.length; i++) {
			if(heights[i] > maxHeight) {
				maxHeight = heights[i];
			}
		}
		return maxHeight;
	}
	
	
	/**
	 Checks the board for internal consistency -- used
	 for debugging.
	*/
	public void sanityCheck() {
		if (DEBUG) {
			int[] checkWidths = new int[height];
			int[] checkHeights = new int[width];
			int checkMaxHeight = 0;
			for(int col = 0; col < width; col ++) {
				for(int row = 0; row < height; row ++) {
					if(grid[col][row]) {
						checkWidths[row] ++;
						if(checkHeights[col] <= row) {
							checkHeights[col] = row + 1;
						}
					}
				}
			}
			
			// check widths
			for(int i = 0; i < height; i ++) {
				if(checkWidths[i] != widths[i]) {
					throw new RuntimeException("widths[" + i + "] is incorrect");
				}
			}
						
			// check heights
			for(int i = 0; i < width; i ++) {
				if(checkHeights[i] != heights[i]) {
					//System.out.println(checkHeights[i]);//
					//System.out.println(heights[i]);//
					throw new RuntimeException("heights[" + i + "] is incorrect");
				}
				// update checkMaxHeight
				if(checkMaxHeight < checkHeights[i]) {
					checkMaxHeight = checkHeights[i];
				}
			}
			
			// check maxHeight
			if(checkMaxHeight != maxHeight) {
				throw new RuntimeException("maxHeight is incorrect");
			}
		}
	}
	
	/**
	 Given a piece and an x, returns the y
	 value where the piece would come to rest
	 if it were dropped straight down at that x.
	 
	 <p>
	 Implementation: use the skirt and the col heights
	 to compute this fast -- O(skirt length).
	*/
	public int dropHeight(Piece piece, int x) {
		if(x < 0 || x >= height) return -1;
		int dropHeight = 0;
		int[] skirt = piece.getSkirt();
		for(int i = 0; i < piece.getWidth(); i++) {
			if(heights[x + i] - skirt[i] > dropHeight) {
				dropHeight = heights[x + i] - skirt[i];
			}
		}
		return dropHeight; 
	}
	
	
	/**
	 Returns the height of the given column --
	 i.e. the y value of the highest block + 1.
	 The height is 0 if the column contains no blocks.
	*/
	public int getColumnHeight(int x) {
		return heights[x];
	}
	
	
	/**
	 Returns the number of filled blocks in
	 the given row.
	*/
	public int getRowWidth(int y) {
		 return widths[y];
	}
	
	
	/**
	 Returns true if the given block is filled in the board.
	 Blocks outside of the valid width/height area
	 always return true.
	*/
	public boolean getGrid(int x, int y) {
		if(x < 0 || x >= width || y < 0 || y >= height || grid[x][y]) {return true;}
		return false; 
	}
	
	
	public static final int PLACE_OK = 0;
	public static final int PLACE_ROW_FILLED = 1;
	public static final int PLACE_OUT_BOUNDS = 2;
	public static final int PLACE_BAD = 3;
	
	/**
	 Attempts to add the body of a piece to the board.
	 Copies the piece blocks into the board grid.
	 Returns PLACE_OK for a regular placement, or PLACE_ROW_FILLED
	 for a regular placement that causes at least one row to be filled.
	 
	 <p>Error cases:
	 A placement may fail in two ways. First, if part of the piece may falls out
	 of bounds of the board, PLACE_OUT_BOUNDS is returned.
	 Or the placement may collide with existing blocks in the grid
	 in which case PLACE_BAD is returned.
	 In both error cases, the board may be left in an invalid
	 state. The client can use undo(), to recover the valid, pre-place state.
	*/
	public int place(Piece piece, int x, int y) {
		// flag !committed problem
		// need to save previous state before place is called
		if (!committed) throw new RuntimeException("place commit problem");
		committed = false;
		backUp();
		
		int result = PLACE_OK;
		
		for(TPoint tp : piece.getBody()) {
			int curX = x + tp.x;
			int curY = y + tp.y;
			// error case 1: piece falls out of bounds
			if (curX < 0 || curX >= width || curY < 0 || curY >= height) {
				return PLACE_OUT_BOUNDS;
			}
			// error case 2: piece overlaps filled spots
			if(grid[curX][curY]) {
				return  PLACE_BAD; 
			}
			else {
				grid[curX][curY] = true;
				// update widths and heights array
				// there may be space in a row
				widths[curY] ++;
				// there won't be space in a column
				if(heights[curX] < curY + 1) {
					heights[curX] = curY + 1;
				}
				// update maxHeight
				if(maxHeight < curY + 1) {
					maxHeight = curY + 1;
				}
				if(widths[curY] == width) result = PLACE_ROW_FILLED;
			}									
		}
		sanityCheck();
		return result;
	}
	

	
	private void backUp() {
		// make a copy of original widths
		System.arraycopy(widths, 0, xWidths, 0, widths.length);
		// make a copy of original heights
		System.arraycopy(heights, 0, xHeights, 0, heights.length);
		// make a copy of original grid
		for(int i = 0; i < width; i ++) {
			System.arraycopy(grid[i], 0, xGrid[i], 0, height);
		}
		// make a copy of maxHeight
		xMaxHeight = maxHeight;
	}
	
	
	/**
	 Deletes rows that are filled all the way across, moving
	 things above down. Returns the number of rows cleared.
	*/
	public int clearRows() {
		committed = false;
		backUp();
		int rowsCleared = 0;
		int currRow = 0;
		while(widths[currRow] != 0) {
			// if is filled then clear current row and update rowsCleared
			while(widths[currRow] == width) {
				clearCurRow(currRow);
				rowsCleared ++;
			}
			currRow ++;
		}
		sanityCheck();
		return rowsCleared;
	}

    // helper function to clear one row at a time
	private void clearCurRow(int currRow) {
		// until reach the upper bound
		while(currRow < maxHeight - 1) {
			// shift above row downward
			for(int i = 0; i < width; i++) {
				grid[i][currRow] = grid[i][currRow + 1];
				// update heights
				// case1: if is filled then the height will just pile up
				if(grid[i][currRow + 1]) {
					heights[i] = currRow + 1;
				} else {
					// case2: if not filled then go down until find the filled one
					int downRow = currRow - 1;
					while(downRow >= 0 && !grid[i][downRow]) {
						downRow--;
					}
					heights[i]= downRow + 1;
				}
				// update widths
				widths[currRow] = widths[currRow + 1];
			}
			currRow ++;
		}
		// set rows shifted in at the top of the board empty
		for(int i = 0; i < width; i++) {
			grid[i][currRow] = false;
			widths[currRow] = 0;
		}
		// update maxHeight;
		maxHeight --;
	}


	/**
	 Reverts the board to its state before up to one place
	 and one clearRows();
	 If the conditions for undo() are not met, such as
	 calling undo() twice in a row, then the second undo() does nothing.
	 See the overview docs.
	*/
	public void undo() {
		if(!committed) {
			// restore original grid
			boolean[][] tempGrid = grid;
			grid = xGrid;
			xGrid = tempGrid;
			
			// restore original widths
			int[] tempWidth = widths;
			widths = xWidths;
			xWidths = tempWidth;
			
			// restore original heights
			int[] tempHeight = heights;
			heights = xHeights;
			xHeights = tempHeight;
			
			//restore original maxHeight
			maxHeight = xMaxHeight;
			
			sanityCheck();
			commit();
		}
	}
	
	
	/**
	 Puts the board in the committed state.
	*/
	public void commit() {
		committed = true;
	}


	
	/*
	 Renders the board state as a big String, suitable for printing.
	 This is the sort of print-obj-state utility that can help see complex
	 state change over time.
	 (provided debugging utility) 
	 */
	public String toString() {
		StringBuilder buff = new StringBuilder();
		for (int y = height-1; y>=0; y--) {
			buff.append('|');
			for (int x=0; x<width; x++) {
				if (getGrid(x,y)) buff.append('+');
				else buff.append(' ');
			}
			buff.append("|\n");
		}
		for (int x=0; x<width+2; x++) buff.append('-');
		return(buff.toString());
	}
}


