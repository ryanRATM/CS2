/*
 * @Author: Lucas Ryan
 * @Date: June 6, 2016
 * @Filename: GenericBST.java
 * NID: lu469191
 */

// Comments and code also from Prof Sean Szumlanski
// Basic binary search tree (BST) implementation that supports insert() and
// delete() operations. This framework is provide for you to modify as part of
// Programming Assignment #2.

import java.io.*;
import java.util.*;

// Implementing a Generic Class that holds data and 2 child nodes
class Node<T> {
	T data;
	Node<T> left, right;

	// setting data to whatever was passed in constructor
	Node(T data) {
		this.data = data;
	}
}


// BST can store objects of any class as long as that class implements Comparable Interface
// Implementing good coding standard, by keeping all functions that use private data unaccessible
// from user, so people can't as easily maliciously modify code/data
public class GenericBST<T extends Comparable<T>> {
	private Node<T> root;	// root of the tree of whatever objects of class storing

	// function the user can call, to protect from real insert function
	public void insert(T data) {
		root = insert(root, data);
	}

	// recursively function that returns a Node<T> of object
	// don't insert duplicate nodes (i.e) root.data == data
	private Node<T> insert(Node<T> root, T data) {
		if (root == null) {	// if empty tree, set as root
			return new Node<T>(data);
		}
		
		// finding if data comes before node.data inserting
		int result = data.compareTo(root.data); 
		
		if (result < 0) {	// inserting data before current node
			root.left = insert(root.left, data);
		} else if (result > 0) {	// inserting data after current node
			root.right = insert(root.right, data);
		} 
		return root;
	}
	
	// function the user can call, to protect code from real(private) delete function
	public void delete(T data) {
		root = delete(root, data);
	}

	// recurssive function that deletes a node from tree if node looking for in tree
	// implements binary search to find a match, once find deleting node replace it with
	// largest child node if has any, else just get rid of deleting node
	private Node<T> delete(Node<T> root, T data) {
		if (root == null) {	// if empty return null pointer
			return null;
		}
		
		int result = data.compareTo(root.data);	// finding if deleting node before or after current node	
		
		if (result < 0) {	// deleting node before current node
			root.left = delete(root.left, data);
		} else if (result > 0) {	// deleting node after current node
			root.right = delete(root.right, data);
		} else {	// found the node we are looking for
			if (root.left == null && root.right == null) {	// has no children, getting rid of node
				return null;
			} else if (root.right == null) {	// replace current node with left child if no right child
				return root.left;
			} else if (root.left == null) {		// replace current node with right child if no left child
				return root.right;
			} else {							// else delete the largest sub-child under deleting node
				root.data = findMax(root.left);
				root.left = delete(root.left, (T)root.data);
			}
		}

		return root;
	}

	// This method assumes root is non-null, since this is only called by
	// delete() on the left subtree, and only when that subtree is non-empty.
	private T findMax(Node<T> root) {
		while (root.right != null) {	// go to the most right node until hit null child
			root = root.right;
		}

		return (T) root.data;
	}

	// function the user can call, to protect code from real(private) delete function
	// Returns true if the value is found in the BST, false otherwise
	public boolean contains(T data) {
		return contains(root, data);
	}

	// implements binary search to find if given data is found in binary tree
	// return true if data is in binary tree, else return false if don't find or have empty tree
	private boolean contains(Node<T> root, T data) {
		if (root == null) {
			return false;	// we never find a match
		} 
		
		int result = data.compareTo(root.data);	// finding if desired data before or after current node's data
		
		if (result < 0) {						// desired data precedes current node's data, go left
			return contains(root.left, data);
		} else if (result > 0) {				// desired data follows after current node's data, go right
			return contains(root.right, data);
		} else {								// current node's data and data match, we have a match
			return true;
		}
	}

	// will print out the in-order traversal of tree
	// function the user can call, to protect from real(private) in-order function that does recursive calls
	public void inorder() {
		System.out.print("In-order Traversal:");
		inorder(root);
		System.out.println();
	}

	// Traversal path: go Left, show Center, go Right
	private void inorder(Node<T> root) {
		if (root == null)	// exit if reach dead-end, null pointer
			return;

		inorder(root.left);
		System.out.print(" " + root.data);
		inorder(root.right);
	}

	// will print out the pre-order traversal of tree
	// function the user can call, to protect from real(private) pre-order function that does recursive calls
	public void preorder() {
		System.out.print("Pre-order Traversal:");
		preorder(root);
		System.out.println();
	}

	// Traversal path: show Center, go Left, go Right
	private void preorder(Node<T> root) {
		if (root == null)	// exit if reach dead-end, null pointer
			return;

		System.out.print(" " + root.data);
		preorder(root.left);
		preorder(root.right);
	}

	// will print out the post-order traversal of tree
	// function the user can call, to protect from real(private) post-order function that does recursive calls
	public void postorder() {
		System.out.print("Post-order Traversal:");
		postorder(root);
		System.out.println();
	}

	// Traversal path: go Left, go Right, show Center
	private void postorder(Node<T> root) {
		if (root == null)	// exit if reach dead-end, null pointer
			return;

		postorder(root.left);
		postorder(root.right);
		System.out.print(" " + root.data);
	}

	public static void main(String[] args) {
		
	}	
}