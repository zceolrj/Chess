package com.test.arithmetic.graph.package03;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class AdjMatrixGraph<E> 
{
    private final int MAX_WEIGHT = Integer.MAX_VALUE/2;
	
	protected ArrayList<E> vertexList;//顺序表存储图的顶点集合
    
    protected int[][] adjmatrix;//图的邻接矩阵
    
    //-----------1.构造图:增删改查-----------------//
    public AdjMatrixGraph(int n)//n为顶点的数目
    {
    	this.vertexList = new ArrayList<E>(n);
    	this.adjmatrix = new int[n][n];
    	for(int i=0;i<n;i++)
    	{
    		for(int j=0;j<n;j++)
    		{
    			//对角线为0,其他的都为无穷大
    			this.adjmatrix[i][j] = (i==j)?0:MAX_WEIGHT;
    		}
    	}
    }
    
    //构造函数内是一个字符串数组,一个是edge的set集合
    public AdjMatrixGraph(E[] vertices, Edge[] edges)
    {
    	this(vertices.length);
    	for(int i=0;i<vertices.length;i++)
    	{
    		insertVertex(vertices[i]);//添加顶点
    	}
    	for(int j=0;j<edges.length;j++)
    	{
    		insertEdge(edges[j]);//添加边
    	}
    }
    
    //构造函数内一个是数组集合,一个是edge的set集合
    public AdjMatrixGraph(ArrayList<E> list, Edge[] edges)
    {
    	this(list.size());
    	this.vertexList = list;
    	for(int j=0;j<edges.length;j++)
    	{
    		insertEdge(edges[j]);
    	}
    }
    
    //显示出一共顶点的数目
    public int vertexCount()
    {
    	return this.vertexList.size();
    }
    
    //根据顶点编号得到该顶点
    public E get(int i)
    {
    	return this.vertexList.get(i);
    }
    
    //插入一个顶点,若插入成功,返回true
    public boolean insertVertex(E vertex)
    {
    	return this.vertexList.add(vertex);
    }
    
    //插入一条权值为weight的边<vi,vj>,若该边已有,则不插入
    public boolean insertEdge(int i, int j, int weight)
    {
    	//先判断该边的两个顶点编号是否在范围内,该边的值是否为最大值,来确定所添加的边的值是否存在
    	if(i>=0 && i<vertexCount() && j>=0&&j<vertexCount()
    			&& i!=j && adjmatrix[i][j]==MAX_WEIGHT)
    	{
    		this.adjmatrix[i][j] = weight;//添加权值
    		return true;
    	}
    	return false;
    }
    
    public boolean insertEdge(Edge edge)
    {
    	if(edge==null)
    	{
    		return false;
    	}
    	return insertEdge(edge.start, edge.dest, edge.weight);
    }
    
    public String toString()
    {
    	String str = "顶点集合: ";
    	return str;
    }
    
    //删除边<vi, vj>,若成功,返回true
    public boolean removeEdge(int i, int j)
    {
    	//判断该边的两个顶点是否存在,以及该边的值是否为最大值来判断该边是否存在
    	if(i>=0&&i<vertexCount() &&j>=0&&j<vertexCount()
    			&&i!=j && this.adjmatrix[i][j]!=MAX_WEIGHT)
    	{
    		//设置该边的权值为无穷大,说明已不存在
    		this.adjmatrix[i][j] = MAX_WEIGHT;
    		return true;
    	}
    	return false;
    }
    
    //删除序号为v的顶点及其关联的边
    public boolean removeVertex(int v)
    {
    	int n = vertexCount();//删除之前的顶点数
    	if(v>=0 && v<n)
    	{
    		this.vertexList.remove(v);
    	
    	    /*for(int i=v;i<n-1;i++)
    	    {
    		    for(int j=0;j<n;j++)
    		    {
    			    this.adjmatrix[i][j] = this.adjmatrix[i+1][j];
    		    }
    	    }
    	    for(int j=v;j<n-1;j++)
    	    {
    		    for(int i=0;i<n-1;i++)
    		    {
    			    this.adjmatrix[i][j] = this.adjmatrix[i][j+1];
    		    }
    	    }*/
    		for(int i=0;i<n;i++)
    		{
    			for(int j=0;j<n;j++)
    			{
    				if(i>v && j>v)
    				{
    					this.adjmatrix[i-1][j-1] = this.adjmatrix[i][j];
    				}
    				else if(i>v)
    				{
    					this.adjmatrix[i-1][j] = this.adjmatrix[i][j];
    				}
    				else if(j>v)
    				{
    					this.adjmatrix[i][j-1] = this.adjmatrix[i][j];
    				}
    			}
    		}
    		for(int i=0;i<n;i++)
    		{
    			this.adjmatrix[n-1][i] = MAX_WEIGHT;
    			this.adjmatrix[i][n-1] = MAX_WEIGHT;
    		}
    	    return true;
    	}
    	return false;
    }
    
    //返回顶点v的第一个邻接顶点的序号
    public int getFirstNeighbor(int v)
    {
    	return getNextNeighbor(v, -1);
    }
    
    //返回v在w后的下一个邻接顶点
    public int getNextNeighbor(int v, int w)
    {
    	if(v>=0&&v<vertexCount()&&w>=-1&&w<vertexCount()&&v!=w)
    	{
    		for(int j=w+1;j<vertexCount();j++)
    		{
    			//w==-1时,从0开始寻找下一个邻接顶点
    			if(adjmatrix[v][j]>0&&adjmatrix[v][j]<MAX_WEIGHT)
    			{
    				return j;
    			}
    		}
    	}
    	return -1;
    }
    
    
    
    
    
    //-----------------2.最小生成树-------------------//
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public AdjMatrixGraph minSpanTree_prim()
    {
    	//n个顶点的最小生成树有n-1条边
    	Edge[] mst = new Edge[this.vertexCount()-1];
    	int un;
    	//存放所有已访问过的顶点集合
    	List<Integer> u = new ArrayList<Integer>();
    	u.add(0);//起始点默认为标识为0的顶点
    	for(int i=0;i<this.vertexCount()-1;i++)
    	{
    		int minweight = MAX_WEIGHT;//最小边的时候,权值
    		int minstart = MAX_WEIGHT;//最小边的时候,起点
    		int mindest = MAX_WEIGHT;//最小边的时候,终点
    		for(int j=0;j<u.size();j++)
    		{
    			un = u.get(j);
    			for(int k=0;k<this.vertexCount();k++)
    			{
    				//获取最小值的条件:1.该边比当前情况下的最小值小;2.该边还未访问过
    				if((minweight>adjmatrix[un][k])&&(!u.contains(k)))
    				{
    					minweight = adjmatrix[un][k];
    					minstart = un;
    					mindest = k;
    				}
    			}
    		}
    		System.out.println("一次遍历所添加的最小边:权值 起点 终点分别为:weight: "+minweight
    				+"start: "+minstart+"dest: "+mindest);
    		u.add(mindest);
    		Edge e = new Edge(minstart, mindest, adjmatrix[minstart][mindest]);
    		mst[i] = e;
    	}
    	//构造最小生成树相应的图对象
    	return new AdjMatrixGraph(this.vertexList, mst);
    }
    
    
    //---------------3.图的遍历(广度遍历,深度遍历)------------------//
    public void DFStraverse()
    {
    	int n = this.vertexCount();
    	boolean[] visited = new boolean[n];
    	for(int i=0;i<n;i++)
    	{
    		visited[i] = false;
    	}
    	//编号0为起始点,进行一次深度优先遍历(一次得到一个连通分量)
    	for(int j=0;j<n;j++)
    	{
    		if(!visited[j])
    		{
    			System.out.println("以该顶点为"+j+"起始点的遍历: ");
    			this.DFS(j, visited);
    		}
    	}
    }
    
    //参数1:遍历起始点的编号, 参数2::记录各个顶点是否被访问过
    public void DFS(int v, boolean[] visited2)
    {
    	boolean[] visited = visited2;
    	visited[v] = true;
    	System.out.println("遍历顶点"+v);
    	for(int w=this.getFirstNeighbor(v);w>=0;w=this.getNextNeighbor(v, w))
    	{
    		if(!visited[w])
    		{
    			visited[w] = true;
    			DFS(w, visited);
    		}
    	}
    }
    
    
    public void BFStraverse()
    {
    	int n = this.vertexCount();
    	boolean[] visited = new boolean[n];
    	Queue<Integer> queue = new LinkedList<Integer>();
    	for(int i=0;i<n;i++)
    	{
    		visited[i] = false;
    	}
    	
    	for(int j=0;j<n;j++)
    	{
    		if(!visited[j])
    		{
    			visited[j] = true;
    			System.out.println("遍历顶点: "+j);
    			queue.offer(j);
    		}
    		while(!queue.isEmpty())
    		{
    			int v = (Integer)queue.poll();
    			System.out.println("遍历点: "+v);
    			for(int w=this.getFirstNeighbor(v);w>=0;w=this.getNextNeighbor(v, w))
    			{
    				if(!visited[w])
    				{
    					visited[w] = true;
    					queue.offer(w);
    				}
    			}
    		}
    	}
    }
    
    
    
    //--------------------4.图的最短路径Dijkstra算法--------//
    public void Dijkstra()
    {
    	int n = this.vertexCount();
    	int minweight = MAX_WEIGHT;
    	int minUn = 0;
    	int[] minmatrix = new int[n];//存放当前起始点到其余各个顶点的距离
    	boolean[] isS = new boolean[n];//判断各个是否被访问过
    	String[] route = new String[n];//每个字符串是显示对应顶点最短距离的路径
    	for(int i=0;i<n;i++)//初始化
    	{
    		minmatrix[i] = adjmatrix[0][i];
    		isS[i] = false;
    		route[i] = "起点->"+i;
    	}
    	for(int i=0;i<n;i++)
    	{
    		//选择当前和起点连通的，且值最小的顶点
    		for(int k=0;k<n;k++)
    		{
    			if(!isS[k])
    			{
    				if(minmatrix[k]<minweight)
    				{
    					minweight = minmatrix[k];
    					minUn = k;
    				}
    			}
    		}
    		isS[minUn] = true;//将该点设置为已访问
    		for(int j=0;j<n;j++)
    		{
    			if(!isS[j])//判断该顶点还没加入到S中/属于U-S
    			{
    				if(minweight+adjmatrix[minUn][j]<minmatrix[j])
    				{
    					//通过当下最小值 访问到的其他顶点的距离小于原先的最小值 则进行交换值
    					minmatrix[j] = minweight+adjmatrix[minUn][j];
    					route[j] = route[minUn] + "->" + j;
    				}
    			}
    		}
    		minweight = MAX_WEIGHT;//因为要放到下一个循环中,所以一定要重设置一下,回到最大值
    	}
    	for(int m=0;m<n;m++)
    	{
    		System.out.println("从V0出发到达"+m+"点");
    		if(minmatrix[m]==MAX_WEIGHT)
    		{
    			System.out.println("没有到达该点的路径");
    		}
    		else
    		{
    			System.out.println("当前从V0出发到达该点的最短距离: "+minmatrix[m]);
    			System.out.println("当前从V0出发到达该点的最短距离: "+route[m]);
    		}
    	}
    }
}

class Edge
{
	int start;
	int dest;
	int weight;
	public Edge(int start, int dest, int weight)
	{
		this.start = start;
		this.dest = dest;
		this.weight = weight;
	}
}