package com.test.arithmetic.tree;

import java.util.ArrayList;
import java.util.List;

public class TreeChildren<E> 
{
    /*
     * 记录子节点对象
     */
	class SonNode<E>
    {
    	E data;
    	int index;
    	SonNode<E> sonNode;
    	
    	public SonNode(E data, int index, SonNode<E> sonNode)
    	{
    		this.data = data;
    		this.index = index;
    		this.sonNode = sonNode;
    	}
    	
    	public String toString()
    	{
    		return (String)data;
    	}
    	
    	public Node<E> paseNode()
    	{
    		return new Node<E>(data, index);
    	}
    }
	
	/*
	 * 实际应用的Node对象
	 */
	class Node<E>
	{
		E data;
		int index;
		SonNode<E> firstSonNode;
		
		public Node(E data, int index)
		{
			this.data = data;
			this.index = index;
		}
		
		public Node(E data, int index, SonNode<E> sonNode)
		{
			this.data = data;
			this.index = index;
			this.firstSonNode = sonNode;
		}
		
		public boolean equals(Object obj)
		{
			if(((Node<E>)obj).data == this.data
			    && ((Node<E>)obj).index == this.index)
			{
			    return true;
			}
			return false;
		}
		
		public int hashCode()
		{
			return super.hashCode();
		}
		
		public String toString()
		{
			return (String)data;
		}
	}
	
	//默认数组大小
	private final int DefSize = 150;
	
	//记录节点个数
	private int nodeSize;
	
	//父节点对象
	private Node<E>[] nodes;
	
	public TreeChildren()
	{
		nodes = new Node[DefSize];
	}
	
	public TreeChildren(E e)
	{
		nodes = new Node[DefSize];
		nodeSize++;
		nodes[0] = new Node<E>(e, 0);
	}
	
	/*
	 * 为指定节点增加子节点
	 */
	public boolean addNodeByParent(E e, Node<E> parentNode)
	{
		for(int i=0;i<nodes.length;i++)
		{
			if(nodes[i]==null)
			{
				SonNode<E> sonNode = new SonNode<E>(e, i, null);
				SonNode<E> lastSonNode = getLastSonNodeByParent(parentNode);
				
				if(lastSonNode == null)
				{
					parentNode.firstSonNode = sonNode;
				}
				else
				{
					lastSonNode.sonNode = sonNode;
				}
				nodes[i] = sonNode.paseNode();
				nodeSize++;
				return true;
			}
		}
		return false;
	}
	
	/*
	 * 由SonNode到普通Node的转化
	 */
	public Node<E> paseNode(SonNode<E> sonNode)
	{
		for(int i=0;i<nodeSize;i++)
		{
			if(nodes[i]!=null && nodes[i].data==sonNode.data
					&& nodes[i].index == sonNode.index)
			{
				return nodes[i];
			}
		}
		return null;
	}
	
	/*
	 * 获得一个父节点的最后子节点对象
	 */
	public SonNode<E> getLastSonNodeByParent(Node<E> parentNode)
	{
		if(parentNode.firstSonNode==null)
		{
			return null;
		}
		
		SonNode<E> sonNodeNow = parentNode.firstSonNode;
		while(sonNodeNow.sonNode!=null)
		{
			sonNodeNow = sonNodeNow.sonNode;
		}
		return sonNodeNow;
	}
	
	/*
	 * 根据node获得索引
	 */
	public int getNodeIndex(Node<E> node)
	{
		for(int i=0;i<nodes.length;i++)
		{
			if(nodes[i].equals(node))
			{
				return i;
			}
		}
		return -1;
	}
	
	/*
	 * 判断是否是空树
	 */
	public boolean isEmpty()
	{
		return nodeSize==0;
	}
	
	/*
	 * 返回树的根节点
	 */
	public Node<E> getRootNode()
	{
		if(nodeSize==0)
		{
			return null;
		}
		return nodes[0];
	}
	
	/*
	 * 根据子节点返回父节点对象
	 */
	public Node<E> getParentNodeByChildNode(Node<E> childNode)
	{
		for(int i=0;i<nodes.length;i++)
		{
			if(nodes[i]!=null && nodes[i].firstSonNode!=null)
			{
				SonNode<E> sonNode = nodes[i].firstSonNode;
				while(sonNode!=null)
				{
					if(sonNode.data==childNode.data
							&& sonNode.index == childNode.index)
					{
						return nodes[i];
					}
					sonNode = sonNode.sonNode;
				}
			}
		}
		return null;
	}
	
	/*
	 * 根据父节点返回父子节点对象集合
	 */
	public List<SonNode<E>> getChildsNodeByParentNode(Node<E> parentNode)
	{
		if(parentNode==null)
		{
			return null;
		}
		
		SonNode<E> sonNodeNow = parentNode.firstSonNode;
		List<SonNode<E>> list = new ArrayList<SonNode<E>>();
		while(sonNodeNow.sonNode!=null)
		{
			list.add(sonNodeNow);
			sonNodeNow = sonNodeNow.sonNode;
		}
		return list;
	}
	
	/*
	 * 返回指定节点的第index个子节点，第1个代表第一个
	 */
	public SonNode<E> getIndexChildByParentNode(Node<E> parentNode, int index)
	{
		if(index==0)
		{
			throw new RuntimeException("没有第0个子节点，从1开始");
		}
		
		if(parentNode.firstSonNode==null)
		{
			return null;
		}
		
		int childCount = 0;
		SonNode<E> sonNode = parentNode.firstSonNode;
		while(sonNode!=null)
		{
			childCount++;
			if(index==childCount)
			{
				return sonNode;
			}
			sonNode = sonNode.sonNode;
		}
		return null;
	}
	
	/*
	 * 返回节点的深度
	 */
	public int returnNodeDeep(Node<E> node)
	{
		if(node==getRootNode())
		{
			return 1;
		}
		else
		{
			Node<E> parentNode = getParentNodeByChildNode(node);
			if(parentNode!=null)
			{
				return returnNodeDeep(parentNode)+1;
			}
			return 0;
		}
	}
	
	/*
	 * 返回树的深度
	 */
	public int returnTreeDeep()
	{
		int max = 0;
		for(int i=0;i<nodeSize;i++)
		{
			int nodeDeep = returnNodeDeep(nodes[i]);
			if(max<nodeDeep)
			{
				max = nodeDeep;
			}
		}
		return max;
	}
	
	public String toString()
	{
		StringBuffer str = new StringBuffer("[");
		for(int i=0;i<nodeSize;i++)
		{
			str.append("["+nodes[i].data+"],");
		}
		if(nodeSize>0)
		{
			return str.substring(0, str.lastIndexOf(","))+"]";
		}
		return str.append("]").toString();
	}
	
	
	
	
	
}
