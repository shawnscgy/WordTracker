package implementations;

import java.io.Serializable;

public class BSTreeNode<E> implements Serializable
{

	private static final long serialVersionUID = -6783701409026289346L;
	private E element;
	private BSTreeNode<E> left;
	private BSTreeNode<E> right;
	private BSTreeNode<E> parent;
	
	public BSTreeNode(E element)
	{
		this.element = element;
		this.left = null;
		this.right = null;
		this.parent = null;
	}
	public E getElement()
	{
		return element;
	}
	public BSTreeNode<E> getLeft() {
		return left;
	}
	public void setLeft(BSTreeNode<E> left) {
		this.left = left;
	}
	public BSTreeNode<E> getRight() {
		return right;
	}
	public void setRight(BSTreeNode<E> right) {
		this.right = right;
	}
	public BSTreeNode<E> getParent() {
		return parent;
	}
	public void setParent(BSTreeNode<E> parent) {
		this.parent = parent;
	}
	public void setElement(E element) {
		this.element = element;
	}
	
}
