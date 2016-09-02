/*
 * Author: Lucas Ryan 
 * FileName: Maze.java
 * Date: July 31, 2016
 * NID: lu469191
 */
import java.util.ArrayDeque;
import java.util.Queue;

// wrapper class to keep hold of row, column, and value at cell
class Point {
	int row;
	int col;
	int val;

	public Point(int r, int c, int v) {
		row = r;
		col = c;
		val = v;
	}
}

public class Maze {
	// associated array to keep track of moves to get there
	static int[][] assocMaze; 
	// 2D array to store possible moves can make
	final static int[][] MOVE_SET = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };

	// implement a BFS to find the shortest path from
	// start to end. expect we don't have a null array
	public static void solve(char[][] maze) throws Exception {
		// if maze has either 1 row or 1 column, then our start is the end so return maze
		if (3 < maze.length || 3 < maze[0].length) {
			
			assocMaze = new int[maze.length][maze[0].length];
			// going to be used to implement BFS traversal of maze
			Queue<Point> queue = new ArrayDeque<Point>();
			// add starting point of maze
			queue.add(new Point(1, 1, 1));
			
			// continue until find end of maze
			while (!queue.isEmpty()) {
				// get the next node
				Point p = queue.remove();
				// search through the possible moves we can make
				for (int i = 0; i < MOVE_SET.length; i++) {
					// get the neighboring cell
					int nextRow = p.row + MOVE_SET[i][0];
					int nextCol = p.col + MOVE_SET[i][1];
					// skip over if back at beginning of maze
					if (nextRow == 1 && nextCol == 1) continue;
					// mark the move if not a wall and haven't been there yet
					if (maze[nextRow][nextCol] != '#' && assocMaze[nextRow][nextCol] == 0) {
						// mark the distance to get there	
						assocMaze[nextRow][nextCol] = p.val + 1;
						// add to queue to look at its kids
						queue.add(new Point(nextRow, nextCol, p.val + 1));
					}
				}
			}
			maze = makeMaze(maze);
		}
	}

	// use the assocMaze to mark the shortest path from start to end 
	private static char[][] makeMaze(char[][] maze) {
		// flag to mark if we have completed the path
		boolean isComplete = false;
		// going to start at end of maze
		int currRow = maze.length - 2;
		int currCol = maze[0].length - 2;
		// we will be working our way back, end to start
		while (!isComplete) {
			// check all possible directions could have came from
			// we can break out of the loop once we find the one 
			// neighbor cell we are looking for, since only one
			// possible cell could have came from
			for (int i = 0; i < MOVE_SET.length; i++) {
				
				int nextRow = currRow + MOVE_SET[i][0];
				int nextCol = currCol + MOVE_SET[i][1];
				// set flag if reached the start point
				if (nextRow == 1 && nextCol == 1) {
					isComplete = true;
					break;
				} 
				// look at neighboring cells and select the one we came from
				// which will be the one that is 1 less than current distance 
				// from start point
				else if (maze[nextRow][nextCol] != '#' &&
						(assocMaze[nextRow][nextCol] - assocMaze[currRow][currCol] == -1)) {
					// mark cell as taken
					maze[nextRow][nextCol] = '.';
					// update to cell at
					currRow = nextRow;
					currCol = nextCol;
					break;
				}
			}
		}
		// return modified maze
		return maze;
	}

	// return true when the maze cell is a valid location and not a wall
	private static boolean isValidMove(int row, int col, char[][] maze) {
		return (maze[row][col] != '#');
	}
}
