// Author: Lucas Ryan
// Filename: Treap.java
// Date: June 21, 2016

import java.util.Random;


//Note BloomFilters can't be resized since we aren't actually storing the data
//it would be impossible to generate the hashcodes we need
class BloomFilter<T> {

	boolean [][] hash_table;

	public BloomFilter(int [] size) {
		hash_table = new boolean[size.length][];
		for(int i = 0; i < size.length; i++) {
			hash_table[i] = new boolean[size[i]];
		}
	}

	// Adding data to hash, only add something if not already hashed
	// Runtime: O(n), n = number of tables
	public boolean add(T data) {

		// already been hashed
		if(contains(data)) {
			return false;
		}

		// make sure our location will be positive
		int val = data.hashCode();
		if(val < 0) val = val - Integer.MIN_VALUE;

		// go through each of tables to flag location as used
		for(int i = 0; i < hash_table.length; i++)
			hash_table[i][val % hash_table[i].length] = true;
		// successfully added to bloom filter
		return true;
	}


	// Checking to see if data has been stored already
	// Runtime: O(n), n = number of tables
	public boolean contains(T data) {
		int val = data.hashCode();
		if(val < 0) val = val - Integer.MIN_VALUE;

		// go through each table to see if locations are flagged as being used
		for(int i = 0; i < hash_table.length; i++) {
			// return false if location is false, since don't have it
			if(hash_table[i][val % hash_table[i].length] == false)
				return false;
		}

		// then must have it
		return true;
	}

}




// Wrapper class that will be used to store the data
// for our Binary Tree, and priority value for our
// min-heap, in order to have a Treap.
class Node<T>{

	T data;              // data actually storing
	int priority;        // priority to keep in heap
	Node<T> left, right; // children

	public Node(T data, int priority) {
		this.data = data;
		this.priority = priority;
	}
}


public class Treap<T extends Comparable<T>> {

	BloomFilter<Integer> hashPriority; // keep track priorities
	int count;	// count of number nodes
	Node<T> root; // our sexy Treap root node

	// Initialize our bloom filter, treap, and node count
	public Treap() {
		// table sizes booleans should be
		hashPriority = new BloomFilter<Integer>(new int[] {1744, 2003, 4124, 6421});
		root = null;
		count = 0;
	}


	// Generate a random priority to associate with node insert
	// into tree BST style and then do rotations to maintain heap
	// the hashCode of an integer is it's own number
	// Assume: data being passed isn't null reference
	public void add(T data) {

		// generate a NEW & UNIQUE key for new node
		int priority;
		do {
			Random rand = new Random();
			priority = rand.nextInt();
		} while(hashPriority.contains(priority));

		// insert into treap
		root = add(root, data, priority);
	}

	// if priority value already used, then won't insert given node
	// Assume: data being passed isn't null reference
	public void add(T data, int priority) {
		if(!hashPriority.contains(priority)) {
			// insert into treap
			root = add(root, data, priority);
		}
	}


	// Private recursive call that inserts new node, once insert
	// as return from recursive calls we check to see if we need
	// to swap the newly inserted node with parent in order to keep
	// following the min-heap rules.
	// Assumes: priority value is unique and is in bloomFilter
	// Runtime of O(log(n)) since insertion is O(log(n)) and rotation
	// is runtime O(1)
	private Node<T> add(Node<T> root, T data, int priority) {
		// if found where to store, store node and update count of nodes
		// update our records once we have a successful insertion into the treap
		if(root == null) {
			//once find a key update records to say its been used
			hashPriority.add(priority);
			count++;
			return new Node<T>(data, priority);
		}

		// find which side to go
		int result = data.compareTo(root.data);

		// inserting data before current node, go left
		if (result < 0) {
			root.left = add(root.left, data, priority);
			// root is new node's parent, and we only rotate if parent has greater heap
			// if need to do a switch call needed rotation operation
			if(root.left.priority < root.priority) {
				root = rightRotate(root, root.left);
			}
		}

		// inserting data after current node, to right
		else if(0 < result) {
			root.right = add(root.right, data, priority);

			if(root.right.priority < root.priority) {
				root = leftRotate(root, root.right);
			}
		}

		// return node of newly modified subtree
		return root;
	}


	// calls the private remove function
	// Assume: None, won't affect treap if given node isn't in treap
	public void remove(T data) {
		// calling private recursive remove function
		root = remove(root, data);
	}

	// Recursive calls will return newly modified subtree
	// once we find the node we are looking for then we will
	// recursively rotate our node down the treap until it's a leaf
	// node.  Then we can just simply delete it.
	// Assume: Nothing, if no match found, no changes made to treap
	// Runtime: O(log(n)), since remove is O(log(n)) and rotation is O(1)
	private Node<T> remove(Node<T> node, T data) {
		// never find a match
		if(node == null) {
			return node;
		}

		// we have found our matching node
		else if (data.compareTo(node.data) == 0) {
			// when are finally a leaf node, update count once have successful delete
			if(node.left == null && node.right == null) {
				--count; // decrement counter
				node = null; // set to null
			}

			// if has two kids check one with lesser priority
			else if(node.left != null && node.right != null) {
				// find kid with lesser priority
				if(node.left.priority < node.right.priority) {
					// get child to be new parent of node and continue to rotate down
					node = rightRotate(node, node.left);
					node.right = remove(node.right, data);
				}

				// its the other child switch with that
				else {
					node = leftRotate(node, node.right);
					node.left = remove(node.left, data);
				}
			}

			// if only one kid rotate in opposite direction of side its on
			else if(node.left != null) {
				node = rightRotate(node, node.left);
				node.right = remove(node.right, data);
			}

			else if(node.right != null) {
				node = leftRotate(node, node.right);
				node.left = remove(node.left, data);
			}
		}

		// keep going to left
		else if (data.compareTo(node.data) < 0) {
			node.left = remove(node.left, data);
		}

		// keep going to right
		else if(0 < data.compareTo(node.data)) {
			node.right = remove(node.right, data);
		}

		// return newly modified subtree
		return node;
	}

	// Making the parent node the left-child of child
	// Returns the child node which is the new parent node
	// Assumes: that child and parent node are not null pointers
	// Runtime: O(1), just setting the child to be parents
	private Node<T> leftRotate(Node<T> parent, Node<T> child) {
		parent.right = child.left;	// parent adopts child's left-child
		child.left = parent;		// child becomes parent node's node
		return child;				// return new parent node
	}

	// Making the parent node the right-child of child
	// Returns the child node which is the new parent node
	// Assumes: that child and parent node are not null pointers
	// Runtime: O(1), just setting the child to be parents
	private Node<T> rightRotate(Node<T> parent, Node<T> child) {
		parent.left = child.right;	// parent adopts child's left-child
		child.right = parent;		// child becomes parent node's node
		return child;				// return new parent node
	}

	// returns the root node of our treap
	public Node<T> getRoot() {
		return root;
	}

	// Searches through the treap BST style to find node, if
	// find given data return true, false otherwise. Using an
	// iterative approach, but with BST principle.
	// Assume: that data isn't null pointer reference
	// Runtime: O(log(n))
	public boolean contains(T data) {
		// create temp node it iterate through treap with
		Node<T> iterNode = root;

		// continue until hit no where else to go
		while(iterNode != null) {

			// find out which side to go searching for data
			int result = data.compareTo(iterNode.data);

			if(result == 0) {
				return true;	// have a match
			}
			// keep searching, it will be on left side of current location
			else if (result < 0) {
				iterNode = iterNode.left;
			}
			// keep searching, it will be on right side of current location
			else if(0 < result) {
				iterNode = iterNode.right;
			}
		}
		return false;	// never found a match
	}

	// Returns the number of nodes we have saved in our treap
	public int size() {
		return count;
	}

	// Returns the height of our Treap, calls the private height
	public int height() {
		return height(root);
	}

	// Calculate the left height and right height
	// Returns the height of our Treap calling our recursive calls
	// Assumes: nothing, if null return -1, else compute given height
	// Runtime: O(n) because have to go every node in treap
	private int height(Node<T> node) {
		// no tree to follow
		if(node == null) {
			return -1;
		} else {
			int left = height(node.left);	// find left child's height
			int right = height(node.right);	// find right child's height

			// return max height of given children
			return (left < right) ? (right + 1): (left + 1);
		}
	}
}
