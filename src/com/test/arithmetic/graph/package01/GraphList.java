package com.test.arithmetic.graph.package01;

public class GraphList 
{
    private int maxVertices;//途中的最大定点数
    private int numEdges;//当前边数
    private int numVertices;
    private Vertex[] NodeTable;
    
    public GraphList(int size)
    {
    	maxVertices = size;
    	numVertices = 0;
    	numEdges = 0;
    	NodeTable = new Vertex[size];
    	if(NodeTable==null)
    	{
    		System.out.println("GraphList is failed to create");
    		System.exit(1);
    	}
    	for(int i=0;i<maxVertices;i++)
    	{
    		NodeTable[i].setAdj(null);
    	}
    }
    
    public void destroy()
    {
    	for(int i=0;i<numVertices;i++)
    	{
    		Edge p = NodeTable[i].getAdj();
    		while(p!=null)
    		{
    			NodeTable[i].setAdj(p.getLink());
    			p = NodeTable[i].getAdj();
    		}
    	}
    }
    
    public int getVertexPos(int vertx) {
        //给出顶点vertex在图中的位置
        for (int i = 0; i < numVertices; i++)
            if (NodeTable[i].getKey() == vertx)
                return i;
        return -1;
    }
    
    public int getValue(int i) { //取顶点 i 的值
        return (i >= 0 && i < numVertices) ? NodeTable[i].getKey() : 0;
    }
    
    public int getFirstNeighbor(int v) {
        //给出顶点位置为 v 的第一个邻接顶点的位置,
        //如果找不到, 则函数返回-1
        if (v != -1) {
            //顶点v存在
            Edge p = NodeTable[v].getAdj(); //对应边链表第一个边结点
            if (p != null) {
                //存在, 返回第一个邻接顶点
                return p.getDest();
            }
        }
        return -1; //第一个邻接顶点不存在
    }
    
    public int getNextNeighbor(int v, int w) {
        //给出顶点v的邻接顶点w的下一个邻接顶点的位置,
        //若没有下一个邻接顶点, 则函数返回-1
        if (v != -1) { //顶点v存在
            Edge p = NodeTable[v].getAdj();
            while (p != null && p.getDest() != w)
                p = p.getLink();
            if (p != null && p.getLink() != null) {
                //返回下一个邻接顶点
                return p.getLink().getDest();
            }
        }
        //下一邻接顶点不存在
        return -1;
    }
    
    public void printGraph(){
        for(int i = 0; i < maxVertices; i++){
           
        }
       
    }
    public static void main(String[] args) {

    }
}

class Vertex
{
	private int key;//顶点编号
	private String name;//顶点相关信息
	private Edge adj;//和自己的邻接顶点之间的那条边
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
	public Edge getAdj() {
		return adj;
	}
	public void setAdj(Edge adj) {
		this.adj = adj;
	}
	
}

class Edge
{
	private int dest;//邻接顶点
	private int cost;//权值
	private String edgeInfo;//边信息
	private Edge link;//与这个顶点相关的所有边在邻接表中的下一条边
	public int getDest() {
		return dest;
	}
	public void setDest(int dest) {
		this.dest = dest;
	}
	public int getCost() {
		return cost;
	}
	public void setCost(int cost) {
		this.cost = cost;
	}
	public String getEdgeInfo() {
		return edgeInfo;
	}
	public void setEdgeInfo(String edgeInfo) {
		this.edgeInfo = edgeInfo;
	}
	public Edge getLink() {
		return link;
	}
	public void setLink(Edge link) {
		this.link = link;
	}
	
	
	
}










