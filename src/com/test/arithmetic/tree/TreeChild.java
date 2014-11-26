package com.test.arithmetic.tree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class TreeChild<E> 
{
    Node<E> root;
    int nodeCount;
    
    public TreeChild(Node<E> root)
    {
    	this.root = root;
    }
    
    public void addNodeOnParent(E e, Node<E> parentNode)
    {
    	if(parentNode==null)
    	{
    		return;
    	}
    	Node<E> newNode = new Node<E>(e);
    	Node<E> lastChild = getLastChildOfParent(parentNode);
    	
    	if(lastChild==null)
    	{
    		parentNode.firstSon = newNode;
    	}
    	else
    	{
    		lastChild.next = newNode;
    	}
    	nodeCount++;
    }
    
    public Node<E> getLastChildOfParent(Node<E> parentNode)
    {
    	if(parentNode==null)
    	{
    		return null;
    	}
    	Node<E> temp = parentNode.firstSon;
    	if(temp==null)
    	{
    		return null;
    	}
    	while(temp.next!=null)
    	{
    		temp = temp.next;
    	}
    	return temp;
    }
    
    public Node<E> getParentByChild(Node<E> childNode)
    {
    	List<Node<E>> nodeList = BFS();
    	List<Node<E>> childList;
    	for(int i=0;i<nodeList.size();i++)
    	{
    		Node<E> node = nodeList.get(i);
    		childList = getChildListByParent(node);
    		if(childList.contains(childNode))
    		{
    			return node;
    		}
    	}
    	return null;
    }
    
    public List<Node<E>> BFS()
    {
    	Queue<Node<E>> queue = new LinkedList<Node<E>>();
    	List<Node<E>> list = new ArrayList<Node<E>>();
    	List<Node<E>> childList;
    	queue.offer(root);
    	while(!queue.isEmpty())
    	{
    		Node<E> node = queue.poll();
    		list.add(node);
    		childList = getChildListByParent(node);
    		for(int i=0;i<childList.size();i++)
    		{
    			queue.offer(childList.get(i));
    		}
    	}
    	return list;
    }
    
    
    
    public List<Node<E>> getChildListByParent(Node<E> parentNode)
    {
    	if(parentNode==null)
    	{
    		return null;
    	}
    	Node<E> temp = parentNode.firstSon;
    	if(temp==null)
    	{
    		return null;
    	}
    	List<Node<E>> childList = new ArrayList<Node<E>>();
    	while(temp.next!=null)
    	{
    		childList.add(temp);
    		temp = temp.next;
    	}
    	return childList;
    }
    
    
    
	
	@SuppressWarnings("hiding")
	class Node<E>
	{
		E data;
		Node<E> next;
		Node<E> firstSon;
		
		public Node(E data)
		{
			this.data = data;
		}
	}
}
