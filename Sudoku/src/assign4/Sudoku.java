package assign4;

import java.util.*;
import java.lang.Comparable.*;

/*
 * Encapsulates a Sudoku grid to be solved.
 * CS108 Stanford.
 */
public class Sudoku {
	// Provided grid data for main/testing
	// The instance variable strategy is up to you.
	
	// Provided easy 1 6 grid
	// (can paste this text into the GUI too)
	public static final int[][] easyGrid = Sudoku.stringsToGrid(
	"1 6 4 0 0 0 0 0 2",
	"2 0 0 4 0 3 9 1 0",
	"0 0 5 0 8 0 4 0 7",
	"0 9 0 0 0 6 5 0 0",
	"5 0 0 1 0 2 0 0 8",
	"0 0 8 9 0 0 0 3 0",
	"8 0 9 0 4 0 2 0 0",
	"0 7 3 5 0 9 0 0 1",
	"4 0 0 0 0 0 6 7 9");
	
	
	// Provided medium 5 3 grid
	public static final int[][] mediumGrid = Sudoku.stringsToGrid(
	 "530070000",
	 "600195000",
	 "098000060",
	 "800060003",
	 "400803001",
	 "700020006",
	 "060000280",
	 "000419005",
	 "000080079");
	
	// Provided hard 3 7 grid
	// 1 solution this way, 6 solutions if the 7 is changed to 0
	public static final int[][] hardGrid = Sudoku.stringsToGrid(
	"3 7 0 0 0 0 0 8 0",
	"0 0 1 0 9 3 0 0 0",
	"0 4 0 7 8 0 0 0 3",
	"0 9 3 8 0 0 0 1 2",
	"0 0 0 0 4 0 0 0 0",
	"5 2 0 0 0 6 7 9 0",
	"6 0 0 0 2 1 0 4 0",
	"0 0 0 5 3 0 9 0 0",
	"0 3 0 0 0 0 0 5 1");
	
	
	public static final int SIZE = 9;  // size of the whole 9x9 puzzle
	public static final int PART = 3;  // size of each 3x3 part
	public static final int MAX_SOLUTIONS = 100;
	

	// Provided various static utility methods to
	// convert data formats to int[][] grid.
	
	/**
	 * Returns a 2-d grid parsed from strings, one string per row.
	 * The "..." is a Java 5 feature that essentially
	 * makes "rows" a String[] array.
	 * (provided utility)
	 * @param rows array of row strings
	 * @return grid
	 */
	public static int[][] stringsToGrid(String... rows) {
		int[][] result = new int[rows.length][];
		for (int row = 0; row<rows.length; row++) {
			result[row] = stringToInts(rows[row]);
		}
		return result;
	}
	
	
	/**
	 * Given a single string containing 81 numbers, returns a 9x9 grid.
	 * Skips all the non-numbers in the text.
	 * (provided utility)
	 * @param text string of 81 numbers
	 * @return grid
	 */
	public static int[][] textToGrid(String text) {
		int[] nums = stringToInts(text);
		if (nums.length != SIZE*SIZE) {
			throw new RuntimeException("Needed 81 numbers, but got:" + nums.length);
		}
		
		int[][] result = new int[SIZE][SIZE];
		int count = 0;
		for (int row = 0; row<SIZE; row++) {
			for (int col=0; col<SIZE; col++) {
				result[row][col] = nums[count];
				count++;
			}
		}
		return result;
	}
	
	
	/**
	 * Given a string containing digits, like "1 23 4",
	 * returns an int[] of those digits {1 2 3 4}.
	 * (provided utility)
	 * @param string string containing ints
	 * @return array of ints
	 */
	public static int[] stringToInts(String string) {
		int[] a = new int[string.length()];
		int found = 0;
		for (int i=0; i<string.length(); i++) {
			if (Character.isDigit(string.charAt(i))) {
				a[found] = Integer.parseInt(string.substring(i, i+1));
				found++;
			}
		}
		int[] result = new int[found];
		System.arraycopy(a, 0, result, 0, found);
		return result;
	}


	// Provided -- the deliverable main().
	// You can edit to do easier cases, but turn in
	// solving hardGrid.
	public static void main(String[] args) {
		Sudoku sudoku1;
		sudoku1 = new Sudoku(hardGrid);
		
		System.out.println(sudoku1); // print the raw problem
		int count1 = sudoku1.solve();
		System.out.println("solutions:" + count1);
		System.out.println("elapsed:" + sudoku1.getElapsed() + "ms");
		System.out.println(sudoku1.getSolutionText());
		
//		Sudoku sudoku2;
//		sudoku2 = new Sudoku(mediumGrid);
//		
//		System.out.println(sudoku2); // print the raw problem
//		int count2 = sudoku2.solve();
//		System.out.println("solutions:" + count2);
//		System.out.println("elapsed:" + sudoku2.getElapsed() + "ms");
//		System.out.println(sudoku2.getSolutionText());
//		
//		Sudoku sudoku3;
//		sudoku3 = new Sudoku(easyGrid);
//		
//		System.out.println(sudoku3); // print the raw problem
//		int count3 = sudoku3.solve();
//		System.out.println("solutions:" + count3);
//		System.out.println("elapsed:" + sudoku3.getElapsed() + "ms");
//		System.out.println(sudoku3.getSolutionText());
	}
	
	
	private int[][] grid;
	private String solution;
	private boolean solved;
	private long startTime;
	private long endTime;
	private int solutionCount;
	private ArrayList<Spot> emptyList;
	
	
	// create a Spot inner class
	public class Spot implements Comparable<Spot> {
		public int row;
		public int col;
		public int availableCount;
	
		
		/** constructor
		 * @param row
		 * @param col
		 */
		public Spot(int row, int col) {
			this.row = row;
			this.col = col;
			availableCount = availableValues().size();
		}
		
		
		/** 
		 * set the spot to given value
		 * @param value
		 */
		public void set(int value) {
			grid[row][col] = value;
		}
		
		/**
		 * reset the spot to 0
		 */
		public void reset() {
			grid[row][col] = 0;
		}
		
		
		/** 
		 * get the hashset of available values for the spot
		 * @return the set of values haven't been occupied by other spots
		 * in the same column, row, part
		 */
		private HashSet<Integer> availableValues(){
			// original hashset provides 9 choices
			HashSet<Integer> availableValueSet = new HashSet<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
			for(int i = 0; i < SIZE; i ++) {
				// remove occupied col value
				availableValueSet.remove(grid[i][col]);
				// remove occupied row value
				availableValueSet.remove(grid[row][i]);
				// remove occupied values inside a 3x3 part
				int checkRow = row / PART * PART + i / PART;
				int checkCol = col / PART * PART + i % PART;
				availableValueSet.remove(grid[checkRow][checkCol]);
			}
			return availableValueSet;
		}
		
		
		/** 
		 * @return an integer 
		 * to sort the spots into order by the size of 
		 * their set of assignable numbers, with the smallest
		 * set(most constrained)spot first
		 */
		@Override
		public int compareTo(Spot o) {
			return this.availableCount - o.availableCount;
		}

		
	}
	
	
	/**
	 * Sets up based on the given ints.
	 */
	public Sudoku(int[][] ints) {
		grid = ints;
		solution = " ";
		solved = false;
		startTime = 0;
		endTime = 0;
		solutionCount = 0;
		getEmptySpot();
	}
	
	
	/**
	 * get a list of all the empty spots
	 */
	private void getEmptySpot() {
		emptyList = new ArrayList<Spot>();
		for(int row = 0; row < SIZE; row++) {
			for(int col = 0; col < SIZE; col++) {
				// if the spot is empty
				if(grid[row][col] == 0) {
					emptyList.add(new Spot(row, col));
				}
			}
		}
		// sort the spots into order by the size of 
		// their set of assignable numbers, with the smallest
		// set(most constrained)spot first
		Comparator<Spot> compareByNumber = (Spot o1, Spot o2) -> o1.compareTo(o2); 
		Collections.sort(emptyList, compareByNumber);
	}
	
	
	
	/**
	 * Text form
	 */
	public Sudoku(String text) {
		this(textToGrid(text));
	}
	
	
	@Override
	public String toString() {
		return gridToString(grid);
	}
	
	
	private String gridToString(int[][] board) {
		StringBuilder sb = new StringBuilder();
		for(int row = 0; row < SIZE; row ++) {
			for(int col = 0; col < SIZE; col ++) {
				sb.append(board[row][col]);
				// add a space after each number
				sb.append(" ");
			}
			if(row != SIZE - 1) {
				sb.append("\n");
			}
		}
		return sb.toString();
	}
	
	
	/**
	 * Solves the puzzle, invoking the underlying recursive search.
	 */
	public int solve() {
		startTime = System.currentTimeMillis();
		recursiveHelper(0);
		endTime = System.currentTimeMillis();
		return solutionCount; 
	}
	
	
	// recursive helper function of solve
	private void recursiveHelper(int index) {
		// failure base case: exceed the max number of solutions
		if(solutionCount >= MAX_SOLUTIONS) {
			return;
		}
		
		// success base case: all the empty spots have been filled 
		if(index == emptyList.size()) {
			// found first solution
			if(!solved && solutionCount == 0) {
				// save the current solution
				for(int row = 0; row < SIZE; row ++) {
					for(int col = 0; col < SIZE; col ++) {
						solution = this.toString();
					}
				}	
			}
			solutionCount ++;
			solved = true;
			return;
		}
		
		// recursive case
		Spot curPos = emptyList.get(index);
		HashSet<Integer> avaiableSet =curPos.availableValues();
		// for each spot, have #avaiableSet.size() choices
		for(int value : avaiableSet) {
			// choose
			curPos.set(value);
			recursiveHelper(index + 1);
			// unchoose
			curPos.reset();
		}
		
	}
	
	
	public String getSolutionText() {
		if(solved) return solution + "\n"; 
		else return "No solutions found!\n";
	}
	
	
	public long getElapsed() {
		return endTime - startTime;
	}

}
