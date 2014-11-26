package com.test.arithmetic.graph.package01;

public class GraphMatrix 
{
    private VertexNode[] verticesList;
    private int[][] edge;
    private int maxVertices;
    private int numVertices;
    private int maxWeight = Integer.MAX_VALUE;
    
    public GraphMatrix(int size)
    {
    	maxVertices = size;
    	numVertices = 0;
    	edge = new int[size][size];
    	verticesList = new VertexNode[size];
    	int i,j;
    	for(i=0;i<maxVertices;i++)
    	{
    		for(j=0;j<maxVertices;j++)
    		{
    			edge[i][j] = (i==j)?0:maxWeight;
    		}
    	}
    }
    
    public int getFirstNeighbor(int v)
    {
    	if(v!=-1)
    	{
    		for(int col = 0;col<numVertices;col++)
    		{
    			if(edge[v][col]>0 && edge[v][col]<maxWeight)
    			{
    				return col;
    			}
    		}
    	}
    	return -1;
    }
    
    public int getNextNeighbor(int v, int w)
    {
    	if(v!=-1 && w!=-1)
    	{
    		for(int col=w+1;col<numVertices;col++)
    		{
    			if(edge[v][col]>0 && edge[v][col]<maxWeight)
    			{
    				return col;
    			}
    		}
    	}
    	return -1;
    }
    
    public int getVertexPos(VertexNode vertex)
    {
    	for(int i=0;i<numVertices;i++)
    	{
    		if(verticesList[i]==vertex)
    		{
    			return i;
    		}
    	}
    	return -1;
    }
    
    public VertexNode getValue(int i)
    {
    	return i>=0 && i<numVertices?verticesList[i]:null;
    }
    
    public int getWeight(int v1, int v2)
    {
    	return v1!=-1 && v2!=-1 ? edge[v1][v2]:0;
    }
    
    public static void main(String[] args)
    {
    	GraphMatrix grpha = new GraphMatrix(4);
    }
}

class VertexNode
{
	private int key = 0;
	private String name;
	private String info;
	public int getKey() {
		return key;
	}
	public void setKey(int key) {
		this.key = key;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
}













