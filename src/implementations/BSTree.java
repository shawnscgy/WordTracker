package implementations;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import utilities.BSTreeADT;
import utilities.Iterator;

public class BSTree<E extends Comparable<? super E>> implements BSTreeADT<E>, Iterable<E>, Serializable
{
	private static final long serialVersionUID = 5954474188997005247L;
	private BSTreeNode<E> root;
	public BSTree()
	{
		this.root = null;
	}
	/**
	 * construct a binary search tree with an element
	 * @param element
	 * @throws NullPointerException 
	 */
	public BSTree(E element)
	{
		if (element == null)
		{
			throw new NullPointerException("Element must not be null.");
		}
		this.root = new BSTreeNode<>(element);
	}

	@Override
	public BSTreeNode<E> getRoot() throws NullPointerException {
		if(root == null)
		{
			throw new NullPointerException("There is no root in this tree.");
		}
		return root;
	}
	/**
	 * get the height of the BSTree, which is defined as the maximum number
	 * that is from the root to the leaves in any path
	 * @return the height of BSTree
	 */
	@Override
	public int getHeight() 
	{
		return calculateHeight(root);
	}
	/**
	 * Recursively calculate the height of current node
	 * @param node
	 * @return the height of BSTree
	 */
	private int calculateHeight(BSTreeNode<E> node)
	{
		if(node == null)
		{
			return 0;
		}
		int leftHeight = calculateHeight(node.getLeft());
		int rightHeight = calculateHeight(node.getRight());
		return (leftHeight > rightHeight ? leftHeight : rightHeight) + 1;
	}
	/**
	 * Calculate the total number of the nodes in BSTree
	 * @return the number of the nodes
	 */
	@Override
	public int size() 
	{
		return calculateSize(root);
	}
	/**
	 * Calculate number of the nodes that below the current node 
	 * @param node: current node
	 * @return the size of BSTree
	 */
	private int calculateSize(BSTreeNode<E> node)
	{
		if(node == null)
		{
			return 0;
		}
		return calculateSize(node.getLeft()) + calculateSize(node.getRight()) + 1;// 1 is the number of the root
	}
	/**
	 * Check if the BSTree is empty
	 * @return boolean of weather the tree is empty
	 */
	@Override
	public boolean isEmpty() {
		return root == null;
	}
	/**
	 * Make the tree an empty tree that contains no nodes
	 */
	@Override
	public void clear() 
	{
		root = null;
	}

	@Override
	public boolean contains(E entry) throws NullPointerException 
	{	
		if(entry == null)
		{
			throw new NullPointerException("The entry cannot be null.");
		}
		return isInThisNode(root, entry);
	}
	/**
	 * Search for the element in each node recursively
	 * @param node: current node
	 * @param entry: the target element
	 * @return a boolean value represent weather the tree contains the entry
	 */
	private boolean isInThisNode(BSTreeNode<E> node, E entry)
	{
		if(node == null)
		{
			return false;
		}
		int compare = entry.compareTo(node.getElement());
		if(compare == 0)
		{
			return true;
		}
		else
		{
			return compare > 0 ? isInThisNode(node.getRight(), entry) : isInThisNode(node.getLeft(), entry);
		}
	}
	
	

	@Override
	public BSTreeNode<E> search(E entry) throws NullPointerException 
	{
		if(entry == null)
		{
			throw new NullPointerException("The entry cannot be null.");
		}
		return isThisNode(root, entry);
	}
	/**
	 * Search for the node recursively that contains the entry 
	 * @param node: current node
	 * @param entry: the target element
	 * @return the node that contains entry, if not, return null
	 */
	private BSTreeNode<E> isThisNode(BSTreeNode<E> node, E entry)
	{
		if(node == null)
		{
			return null;
		}
		int compare = entry.compareTo(node.getElement());
		if(compare == 0)
		{
			return node;
		}
		else
		{
			return compare < 0 ? isThisNode(node.getLeft(), entry) : isThisNode(node.getRight(), entry);
		}
	}

	@Override
	public boolean add(E newEntry) throws NullPointerException 
	{
		if(newEntry == null)
		{
			throw new NullPointerException("New Entry cannot be null.");
		}
		BSTreeNode<E> newNode = new BSTreeNode<E>(newEntry);
		if(root == null)
		{
			this.root = newNode;
		}
		else 
		{
			addProcessor(root, newNode);
		}
		return true;
	}
	/**
	 * Recursively find the position newEntry should be
	 * @param node
	 * @param newEntry
	 * @return the current node
	 */
	private void addProcessor(BSTreeNode<E> node, BSTreeNode<E> newNode)
	{
		int compare = node.getElement().compareTo(newNode.getElement());
		// NEW ENTRY is equanl to greater than node ELEMENT. Set it the LEFT
		if(compare >= 0)
		{
			if(node.getLeft() == null)
			{
				node.setLeft(newNode);
				newNode.setParent(node);
				return;
			}
			addProcessor(node.getLeft(), newNode);
		}
		//// NEW ENTRY is less than node ELEMENT. Set it the RIGHT
		else if(compare < 0)
		{
			if(node.getRight() == null)
			{
				node.setRight(newNode);
				newNode.setParent(node);
				return;
			}
			addProcessor(node.getRight(), newNode);
		}
	}

	@Override
	public BSTreeNode<E> removeMin() 
	{
		if(root == null)
		{
			return null;
		}
		return minRemover(root);
	}
	/**
	 * Remove the node with the least element in the tree. 
	 * The least one represent a node without left
	 * @param node: current node
	 * @return the node with the least element
	 */
	private BSTreeNode<E> minRemover(BSTreeNode<E> node)
	{
		// The node with the least element 
		if(node.getLeft() == null)
		{
			BSTreeNode<E> minNode = node;
			BSTreeNode<E> parent = node.getParent();
			BSTreeNode<E> right = node.getRight();
			if(parent == null)
			{
				this.root = null;
			}
			else
			{
				parent.setLeft(right);
			}
			
			if(right != null)
			{
				right.setParent(parent);
			}
			return minNode;
		}
		return minRemover(node.getLeft());
	}

	@Override
	public BSTreeNode<E> removeMax() 
	{
		if(root == null)
		{
			return null;
		}
		return maxRemover(root);
	}
	/**
	 * Remove the node with the greatest element in the tree.
	 * The greatest one is a node without right 
	 * @param node: current node
	 * @return the node with the greatest element
	 */
	public BSTreeNode<E> maxRemover(BSTreeNode<E> node)
	{
		// The node with the greatest element
		if(node.getRight() == null)
		{
			BSTreeNode<E> maxNode = node;
			BSTreeNode<E> parent = node.getParent();
			BSTreeNode<E> left = node.getLeft();
			
			if(parent == null)
			{
				this.root = null;
			}
			else
			{
				parent.setRight(left);
			}
			if(left != null)
			{
				left.setParent(parent);
			}
			return maxNode;
		}
		return maxRemover(node.getRight());
	}

	@Override
	public Iterator<E> inorderIterator() 
	{
		return new Inorder();
	}
	private class Inorder implements Iterator<E>, Serializable
	{
		private static final long serialVersionUID = 5070514065639810938L;
		private List<E> copyOfElements;
		private int index;
		
		// Constructor
		public Inorder()
		{
			this.index = 0;
			copyOfElements =  new ArrayList<E>();
			assignElements(copyOfElements, root);
		}
		// Recursively assign elements in an inorder list
		private void assignElements(List<E> copyOfElements, BSTreeNode<E> node)
		{
			if(node == null)
			{
				return;
			}
			assignElements(copyOfElements, node.getLeft());
			copyOfElements.add(node.getElement());
			assignElements(copyOfElements, node.getRight());
		}
		@Override
		public boolean hasNext() 
		{
			return index < copyOfElements.size();
		}

		@Override
		public E next() throws NoSuchElementException 
		{
			if(!hasNext())
			{
				throw new NoSuchElementException();
			}
			E toReturn = copyOfElements.get(index);
			index++;
			return toReturn;
		}
		
	}

	@Override
	public Iterator<E> preorderIterator() 
	{
		return new Preorder();
	}
	private class Preorder implements Iterator<E>, Serializable
	{
		private static final long serialVersionUID = 7585735174090486122L;
		private List<E> copyOfElements;
		private int index;
		public Preorder()
		{
			this.index = 0;
			copyOfElements = new ArrayList<E>();
			assignElements(copyOfElements, root);
		}
		public void assignElements(List<E> copyOfElements, BSTreeNode<E> node)
		{
			if(node.getLeft() == null)
			{
				copyOfElements.add(node.getElement());
				return;
			}
			copyOfElements.add(node.getElement());
			assignElements(copyOfElements, node.getLeft());
			assignElements(copyOfElements, node.getRight());
		}
		@Override
		public boolean hasNext() 
		{
			return index < copyOfElements.size();
		}

		@Override
		public E next() throws NoSuchElementException
		{
			if(!hasNext())
			{
				throw new NoSuchElementException();
			}
			return copyOfElements.get(index++);
		}
		
	}

	@Override
	public Iterator<E> postorderIterator() 
	{
		return new Postorder();
	}
	private class Postorder implements Iterator<E>, Serializable
	{
		private static final long serialVersionUID = -933842934583770404L;
		private List<E> copyOfElements;
		private int index;
		public Postorder()
		{
			this.index = 0;
			this.copyOfElements = new ArrayList<E>();
			assignElements(copyOfElements, root);
		}
		public void assignElements(List<E> copyOfElements, BSTreeNode<E> node)
		{
			if(node.getLeft() == null)
			{
				copyOfElements.add(node.getElement());
				return;
			}
			assignElements(copyOfElements, node.getLeft());
			assignElements(copyOfElements, node.getRight());
			copyOfElements.add(node.getElement());
		}
		@Override
		public boolean hasNext() 
		{
			return index < copyOfElements.size();
		}

		@Override
		public E next() throws NoSuchElementException 
		{
			if(!hasNext())
			{
				throw new NoSuchElementException();
			}
			return copyOfElements.get(index++);
		}
		
	}
	@SuppressWarnings("unchecked")
	@Override
	public java.util.Iterator<E> iterator() 
	{
		return (java.util.Iterator<E>) this.inorderIterator();
	}

}
