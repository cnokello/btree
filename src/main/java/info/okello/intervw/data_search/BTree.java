package info.okello.intervw.data_search;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author nelson.okello
 * @version 1.0.0
 * @created 17-Dec-2014
 * 
 *          A generic binary tree implementation.
 * 
 *
 * @param <Key>
 *            Class to be used as key of a node. Extends Comparable that imposes
 *            total ordering. This makes lookups faster
 * @param <Value>
 *            Class to be used as value of a node
 */
public class BTree<Key extends Comparable<Key>, Value> {

	/**
	 * The root node
	 */
	private Node root;

	/**
	 * 
	 * @author nelson.okello
	 * @version 1.0.0
	 * @created 17-Dec-2014
	 * 
	 *          Used purely for data storage. No operations on the data are
	 *          defined.
	 * 
	 *          Properties of a node include the key, value, pointers to its
	 *          left and right subtrees and its size.By taking the node as the
	 *          root, the size returns how many children its got (the entire
	 *          subtree)
	 *
	 */
	private class Node {
		private Key key;
		private List<Value> val;
		private Node left, right;
		private int N;

		public Node(Key key, List<Value> val, int N) {
			this.key = key;
			this.val = val;
			this.N = N;
		}
	}

	/**
	 * Inserts a node. If a value passed is null, then the node with the
	 * specified key is removed
	 * 
	 * The tree is traversed from the root to where the node is to be inserted.
	 * This method uses another helper method to recursively traverse the tree
	 * in order to insert the node at the right point. The call to the helper
	 * method passes the root node as the starting point.
	 * 
	 * @param key
	 *            Key of the node to insert or to remove if value is null.
	 * @param val
	 *            Value of the node to be inserted
	 */
	public void insert(Key key, Value val) {
		if (val == null) {
			remove(key);
			return;
		}

		root = insert(root, key, val);
	}

	/**
	 * Helper method for inserting a new node.
	 * 
	 * Recursively traverses the tree until the point of insertion is reached
	 * 
	 * @param node
	 *            Root node of the sub-tree to check for insertion point
	 * @param key
	 *            Key of the node to be inserted
	 * @param val
	 *            Value of the node to be inserted
	 * @return Returns the new tree after insertion
	 */
	private Node insert(Node node, Key key, Value val) {
		List<Value> valList = new ArrayList<Value>();
		valList.add(val);
		if (node == null)
			return new Node(key, valList, 1);

		// Determine the comparator value
		int cmp = key.compareTo(node.key);

		// If new key is less than the key of the current node, insert to the
		// left
		if (cmp < 0)
			node.left = insert(node.left, key, val);

		// If new key is greater than the one of the current node, insert to the
		// right
		else if (cmp > 0)
			node.right = insert(node.right, key, val);

		// If the two keys are the same, then insert the new value here
		else
			node.val.add(val);

		node.N = 1 + size(node.left) + size(node.right);
		return node;
	}

	/**
	 * Deletes a node with the specified key
	 * 
	 * @param key
	 *            Key of the node to delete
	 */
	public void remove(Key key) {
		root = remove(root, key);
	}

	/**
	 * Helper method that deletes a node with the specified key. Recursively
	 * traverses the tree until the node to be removed is reached.
	 * 
	 * @param node
	 *            Node to be deleted
	 * @param key
	 *            Key of the node to be deleted
	 * @return Returns the new tree after node deletion
	 */
	private Node remove(Node node, Key key) {
		if (node == null)
			return null;

		// Determine the comparator value
		int cmp = key.compareTo(node.key);

		// If the key is less than the key of the current node, then the node to
		// be removed is on the left of the current node
		if (cmp < 0)
			node.left = remove(node.left, key);

		// If it's greater than the key of the current node, then the key to be
		// removed is on the right of the current node
		else if (cmp > 0)
			node.right = remove(node.right, key);

		// If the two keys are equal
		else {
			if (node.right == null)
				return node.left;
			if (node.left == null)
				return node.right;

			Node n = node;
			node = min(n.right);
			node.right = deleteMin(n.right);
			node.left = n.left;
		}

		node.N = size(node.left) + size(node.right) + 1;
		return node;
	}

	/**
	 * Determines the size of the tree, i.e., the number of nodes in the tree
	 * between the key 'lower' and the key 'upper'
	 * 
	 * @param lower
	 *            The lower bound key to use
	 * @param upper
	 *            The upper bound key to use
	 * @return Retruns the number of nodes between the 'lower' and the 'upper'
	 *         bound keys.
	 */
	public int size(Key lower, Key upper) {
		if (lower.compareTo(upper) > 0)
			return 0;
		if (contains(upper))
			return position(upper) - position(lower) + 1;
		else
			return position(upper) - position(lower);
	}

	/**
	 * Determines if the tree contains the node with the specified key
	 * 
	 * @param key
	 *            Key of the node
	 * @return Returns TRUE if the tree contains the node. FALSE otherwise.
	 */
	public boolean contains(Key key) {
		return lookup(key) != null;
	}

	/**
	 * Determines the smallest key in the tree
	 * 
	 * @return Returns the smallest key in the tree.
	 */
	public Key min() {
		if (isEmpty())
			return null;
		return min(root).key;
	}

	/**
	 * Smallest key determination helper class. Recursively traverses the tree
	 * to find the smallest key
	 * 
	 * @param node
	 *            The current node to check
	 * @return Returns the node with the smallest key
	 */
	private Node min(Node node) {
		if (node.left == null)
			return node;
		else
			return min(node.left);
	}

	/**
	 * Checks if the tree is empty.
	 * 
	 * @return Returns TRUE if the tree is empty. FALSE otherwise.
	 */
	public boolean isEmpty() {
		return size() == 0;
	}

	/**
	 * Determines the number of nodes in the tree. Uses a helper method. To
	 * determine the size of the entire tree, it calls the helper method with
	 * the root node.
	 * 
	 * @return Returns the number of nodes in the tree.
	 */
	public int size() {
		return size(root);
	}

	/**
	 * Helper method for determining the size of the tree. Can be called passing
	 * any node as an argument and it'll return the the number of nodes in the
	 * sub-tree in which the specified node is the root.
	 * 
	 * @param node
	 *            The node whose size is to be determined
	 * @return Returns the number of nodes in the sub-tree whose root node is
	 *         'node'.
	 */
	private int size(Node node) {
		if (node == null)
			return 0;
		else
			return node.N;
	}

	/**
	 * Deletes the node with the smallest key. Makes use of a helper class to
	 * recursively check for and delete the node with the smallest key.
	 */
	public void deleteMin() {
		if (isEmpty())
			throw new NoSuchElementException("No elements to delete");
		root = deleteMin(root);
	}

	/**
	 * Helper class for deleting a node with the smallest key. Takes an
	 * arbitrary node as an argument, and recurvely check for and deletes the
	 * smallest element in the sub-tree in which the node is the root.
	 * 
	 * @param node
	 *            The root node of the sub-tree to check
	 * @return Returns the sub-tree after deleting the node with the smallest
	 *         key.
	 */
	private Node deleteMin(Node node) {
		if (node.left == null)
			return node.right;
		node.left = deleteMin(node.left);
		node.N = size(node.left) + size(node.right) + 1;
		return node;
	}

	/**
	 * Retrieves the node with the specified key. Uses a helper method to which
	 * it passes the root node as the starting point.
	 * 
	 * @param key
	 *            The key of the node to lookup
	 * @return Returns the value of the node with the specified key.
	 */
	public List<Value> lookup(Key key) {
		return lookup(root, key);
	}

	/**
	 * Lookup helper method that can take an arbitrary node as root and key to
	 * lookup.
	 * 
	 * @param node
	 *            Root node to start the lookup at.
	 * @param key
	 *            Key whose value is to be retrieved.
	 * @return Returns value of the node with the specified key.
	 */
	private List<Value> lookup(Node node, Key key) {
		if (node == null)
			return null;

		int cmp = key.compareTo(node.key);
		if (cmp < 0)
			return lookup(node.left, key);
		else if (cmp > 0)
			return lookup(node.right, key);
		else
			return node.val;
	}

	/**
	 * Gets the position of the node with the specified key.
	 * 
	 * @param key
	 *            The key of the node whose position is sought
	 * @return Returns the rank position of the node with the specified key.
	 */
	public int position(Key key) {
		return position(key, root);
	}

	/**
	 * Position helper method which takes an arbitrary node as the root node and
	 * traverses it to get the position of the node with the specified key.
	 * 
	 * @param key
	 *            Key of the node whose position is to be returned.
	 * @param node
	 *            Node to use as the root node of the sub-tree to be traversed.
	 * @return Returns the position of the key in the sub-tree whose root node
	 *         is 'node'.
	 */
	private int position(Key key, Node node) {
		if (node == null)
			return 0;

		int cmp = key.compareTo(node.key);
		if (cmp < 0)
			return position(key, node.left);
		else if (cmp > 0)
			return 1 + size(node.left) + position(key, node.right);
		else
			return size(node.left);
	}
}
