//
// TetrisGrid encapsulates a tetris board and has
// a clearRows() capability.
package assign1;

public class TetrisGrid {
	private boolean[][] grid;
	private int height;
	private int width;
	/**
	 * Constructs a new instance with the given grid.
	 * Does not make a copy.
	 * @param grid
	 */
	public TetrisGrid(boolean[][] grid) {
		this.grid = grid;
		this.width = grid.length;
		this.height = grid[0].length;
	}
	
	
	/**
	 * Does row-clearing on the grid (see handout).
	 */
	public void clearRows() {
		for(int row = 0; row < height; row++) {
			if(isFilled(row)) {
				for(int curRow = row; curRow < height; curRow++) {
				    for(int col = 0; col < width; col++) {
					    if(curRow < height - 1) {
						    grid[col][curRow] = grid[col][curRow + 1];
				        }else {
				    	    grid[col][curRow] = false;
			            }
			        }
				}
	        }
			// double check in case there are adjacent filled rows
			if(isFilled(row)) {
				row --;
			}
		}
	}
	
	// return true if the gird is filled
	private boolean isFilled(int row) {
		for(int col = 0; col < width; col++) {
			if(!grid[col][row]) {
				return false;	
			}
		}
		return true;	
	}
	
	/**
	 * Returns the internal 2d grid array.
	 * @return 2d grid array
	 */
	boolean[][] getGrid() {
		System.out.println(grid);
		return grid; 
	}
}
