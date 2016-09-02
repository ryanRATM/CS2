import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Scanner;
// Author: Lucas Ryan, and used Sean Szumlanski's code
// Date: June, 30, 2016
// Filename: TopoSort.java

public class TopoSort {
	boolean[][] matrix;

	// Reads in a file to generate adjacency matrix to represent
	// topological graph. Assume: the file we are reading from exists
	public TopoSort(String filename) throws IOException {
		// open the file to read the data to generate our matrix to display our
		// graph
		Scanner sc = new Scanner(new File(filename));
		int size = sc.nextInt();
		matrix = new boolean[size][size];
		for (int i = 0; i < size; i++) {
			int numNeighbors = sc.nextInt();
			for (int j = 0; j < numNeighbors; j++) {
				// location represents nodes current node points to
				int location = sc.nextInt();
				matrix[i][location - 1] = true;
			}
		}
		sc.close();
	}

	// Finding to see if a given node X comes before node Y in
	// any variation in the graphs sort. First we check to see
	// if the graph has any cycles since there wont be a valid
	// sort, then return false. If there isn't a cycle we then
	// check to see if y is an ancestor of x, because there are
	// so many cases where x comes before y, it would be impossible
	// to solve it. Assume: x and y are both in the graph
	// Runtime: O(n^2) = O(|E|)
	public boolean hasTopoSort(int x, int y) {
		int[] incoming = new int[matrix.length];
		boolean[] visited = new boolean[matrix.length];
		Queue<Integer> q = new ArrayDeque<Integer>();
		int cnt = 0;


		// ************************************************************************
		// * THIS CODE BLOCK IS BASED OFF OF SZUMLANSKI's CODE FROM
		// TOPOSORT.JAVA *
		// ************************************************************************
		// Count the number of incoming edges incident to each vertex. For
		// sparse
		// graphs, this could be made more efficient by using an adjacency list.
		for (int i = 0; i < matrix.length; i++)
			for (int j = 0; j < matrix.length; j++)
				incoming[j] += (matrix[i][j] ? 1 : 0);

		// Any vertex with zero incoming edges is ready to be visited, so add it
		// to
		// the queue.
		for (int i = 0; i < matrix.length; i++)
			if (incoming[i] == 0)
				q.add(i);

		while (!q.isEmpty()) {
			// Pull a vertex out of the queue and add it to the topological
			// sort.
			int node = q.remove();

			// Count the number of unique vertices we see.
			++cnt;

			// All vertices we can reach via an edge from the current vertex
			// should
			// have their incoming edge counts decremented. If one of these hits
			// zero, add it to the queue, as it's ready to be included in our
			// topological sort.
			for (int i = 0; i < matrix.length; i++)
				if (matrix[node][i] && --incoming[i] == 0)
					q.add(i);
		}

		// If we pass out of the loop without including each vertex in our
		// topological sort, we must have a cycle in the graph.

		if (cnt != matrix.length)
			return false;

		// ************************************************************************
		// * THE END OF THE SZUMLANSKI'S CODE *
		// ************************************************************************

		// insert all of the nodes the y points to and mark as visited
		for (int i = 0; i < matrix.length; i++) {
			if (matrix[y - 1][i]) {
				q.add(i);
				visited[i] = true;
			}
		}

		// continue to search through graph until reach x or
		// can't reach any more nodes
		while (!q.isEmpty()) {
			// get the next node in queue
			int node = q.remove();
			// check to see if that node is x, if it is then
			// y is a pre-req of x, thus no matter any possible sort
			// y will always proceed x
			if (node == (x - 1))
				return false;

			// iterate through all the nodes that y links with
			for (int i = 0; i < matrix.length; i++) {
				// add it to the queue only if y links to it and we
				// haven't visited it yet
				if (matrix[node][i] && !visited[i]) {
					visited[i] = true; // mark as visited
					q.add(i);
				}
			}
		}

		// if we get through the queue without running into x then there is at
		// least one case where x proceeds y in a topological sort of the graph
		return true;
	}
}