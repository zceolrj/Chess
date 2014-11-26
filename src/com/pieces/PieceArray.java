package com.pieces;

import java.util.ArrayList;

/**
 * 棋子数组--其实是关于棋子的一个类ArrayList
 * 
 * @author zceolrj
 *
 */
public class PieceArray 
{
    /**
     * 线性表
     */
    private ArrayList<Qizi> list;
    
    /**
     * 构造函数
     */
    public PieceArray()
    {
    	list = new ArrayList<Qizi>();
    }
    
    /**
     * 获取一个棋子
     * 
     * @param index 索引
     * @return
     */
    public Qizi getPiece(int index)
    {
    	if(index>=0&&index<list.size())
    	{
    		return list.get(index);
    	}
    	else
    	{
    		return null;
    	}
    }
    
    /**
     * 增加棋子
     * 
     * @param qz 棋子
     */
    public void add(Qizi qz)
    {
    	list.add(qz);
    }
    
    /**
     * 删除棋子
     * 
     * @param index 索引
     * @return
     */
    public Qizi remove(int index)
    {
    	if(index>=0&&index<list.size())
    	{
    		return list.remove(index);
    	}
    	else
    	{
    		return null;
    	}
    }
    
    /**
     * 删除棋子
     * 
     * @param qz
     * @return
     */
    public boolean remove(Qizi qz)
    {
    	return list.remove(qz);
    }
    
    /**
     * 获取棋子数组的大小
     * 
     * @return
     */
    public int size()
    {
    	return list.size();
    }
}
