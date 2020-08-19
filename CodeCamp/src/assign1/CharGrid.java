// HW1 2-d array Problems
// CharGrid encapsulates a 2-d grid of chars and supports
// a few operations on the grid.

package assign1;

public class CharGrid {
	private char[][] grid;
	private int height;
	private int width;

	/**
	 * Constructs a new CharGrid with the given grid.
	 * Does not make a copy.
	 * @param grid
	 */
	public CharGrid(char[][] grid) {
		this.grid = grid;
		this.height = grid.length;
		this.width = grid[0].length;
	}
	
	/**
	 * Returns the area for the given char in the grid. (see handout).
	 * @param ch char to look for
	 * @return area for given char
	 */
	public int charArea(char ch) {
		int minRow = height;
		int maxRow = 0;
		int minCol = width;
		int maxCol = 0;
		// for each row
		for(int row = 0; row < height; row++) {
			// for each column
			for(int col = 0; col < width; col++) {
				if(grid[row][col] == ch) {
					if(row < minRow) minRow = row;
					if(col < minCol) minCol = col;
					maxRow = row + 1;
					maxCol = col + 1;
				}
			}
		}
		int charArea = (maxRow - minRow) * (maxCol - minCol);
		return charArea;
	}
	
	/**
	 * Returns the count of '+' figures in the grid (see handout).
	 * @return number of + in grid
	 */
	public int countPlus() {
		int count = 0;
		for(int row = 1; row < height - 1; row ++) {
			for(int col = 1; col < width - 1; col ++) {
				if(isValidPlus(row, col)) {
					count ++;
				}
			}
		}
		return count;
	}
	
	// return true if the plus is valid
	private boolean isValidPlus(int row, int col) {
		char centerChar = grid[row][col];
		int upArm = upArmLength(row, col, centerChar);
		int downArm = downArmLength(row, col, centerChar);
		int leftArm = leftArmLength(row, col, centerChar);
		int rightArm = rightArmLength(row, col, centerChar);
		if(upArm == downArm && downArm == leftArm 
			&& leftArm == rightArm && upArm >= 2) {
			return true;
		}else {
			return false;
		}	
	}
	
	// return the length of up arm
	private int upArmLength(int row, int col, char centerChar) {
		int upArm = 1;
		while(row >= 1 && grid[row - 1][col] == centerChar) {
			upArm ++;
			row --;	
		}
		return upArm;
	}
	
	// return the length of down arm
	private int downArmLength(int row, int col, char centerChar) {
		int downArm = 1;
		while(row <= height - 2 && grid[row + 1][col] == centerChar) {
			downArm ++;
			row ++;	
		}
		return downArm;
	}
	
	// return the length of left arm
	private int leftArmLength(int row, int col, char centerChar) {
		int leftArm = 1;
		while(col >= 1 && grid[row][col - 1] == centerChar) {
			leftArm ++;
			col --;
		}
		return leftArm;	
	}
	
	// return the length of right arm
	private int rightArmLength(int row, int col, char centerChar) {
		int rightArm = 1;
		while(col <= width - 2 && grid[row][col + 1] == centerChar) {
			rightArm ++;
			col ++;
		}
		return rightArm;
	}
}
