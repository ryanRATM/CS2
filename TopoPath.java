import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.Stack;

// Author: Lucas Ryan
// Date: June 30, 2016
// Filename: TopoPath.java

public class TopoPath {
	
	// Reads in a file to generate adjacency matrix to represent
	// topological graph.  We must find this graph is a topopath
	// (1) Follows rules of being valid topological graph (no cycles)
	// (2) Have a path to reach all nodes without visiting nodes again
	// Return true if topopath, false if not topological or no path exist
	// Assume: the file we are reading from exists
	// Runtime: O(n^2) = O(|E|)
	public static boolean hasTopoPath(String filename) throws IOException {
		
		// open the file to read the data to generate our matrix to display our graph
		Scanner sc = new Scanner(new File(filename));
		Stack<Integer> s = new Stack<Integer>();
		int count = 0;
		
		int size = sc.nextInt();
		
		boolean [][] adjMatrix = new boolean[size][size];
		int [] incoming = new int[adjMatrix.length];
		
		for(int i = 0; i < size; i++) {
			int numNeighbors = sc.nextInt();
			for(int j = 0; j < numNeighbors; j++) {
				// location represents nodes current node points to
				int location = sc.nextInt();
				adjMatrix[i][location - 1] = true;
			}
		}
		
		sc.close(); // close file
		
		// generate a matrix where each index represents a node in the graph
		// and the number in the index represents number of nodes point to that given node
		for(int i = 0; i < adjMatrix.length; i++) {
			for(int j = 0; j < adjMatrix.length; j++) {
				incoming[j] += ((adjMatrix[i][j]) ? 1 : 0);
			}
		}		
		
		
		// find the starting point in graph
		for(int i = 0; i < adjMatrix.length; i++)
			if(incoming[i] == 0) {
				s.push(i); // store node that start from
			}
		
		// if have more than one possible starting point than can't have a topological path
		if(s.size() != 1) return false;
		
		// implement an iterative DFS using stack to get through the topological graph
		// if able to get to through the graph, hitting each node without cycles then  
		// have a valid topologicalpath.
		while(!s.isEmpty()) {
			// get next node in stack
			int node = (int) s.pop();
			
			// update number of nodes we have hit 
			count++;
			
			// have a flag to see if we have added multiple children to the stack while at the same node
			// we set to false to say we haven't added any kids
			boolean haveInserted = false;
			// iterate through all of the nodes we can reach from where we are at in the graph
			for(int i = 0; i < adjMatrix.length; i++) {
				// if a given node exist and there are no more pre-reqs we can finally travel to it
				if(adjMatrix[node][i] && (--incoming[i] == 0)) {
					// check to see if we have already added a node at while at this current node
					// if we have then we know there is a split in our graph and there isn't a path through the graph 
					if(haveInserted) return false;
					s.push(i); // add node to stack
					haveInserted = true; // flag we have added a node
						
				}
			}
		}
		// if we haven't reached all of our nodes in the graph then we don't have a valid topologicalpath
		if(count != adjMatrix.length) return false;
		// we have a valid topological path
		return true;
	}
}
