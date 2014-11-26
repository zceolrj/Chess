package com.engine;

public class BookRecord 
{
	int moveNum;
	MoveNode[] moveList;
	public BookRecord()
	{
		moveList = new MoveNode[SearchEngine.MaxBookMove];
		moveNum = 0;
	}
}
