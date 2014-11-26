package com.engine;

/**
 * 动态局面类
 * 
 * @author soft
 *
 */
public class ActiveBoard 
{
	public static final int[] RANK = {
		0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
		0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
		0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
		0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
		0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
		0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
		0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
		0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
		0, 1, 2, 3, 4, 5, 6, 7, 8, 9
	};
	
	public static final int FILE[] = {
		0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
		2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
		3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
		4, 4, 4, 4, 4, 4, 4, 4, 4, 4,
		5, 5, 5, 5, 5, 5, 5, 5, 5, 5,
		6, 6, 6, 6, 6, 6, 6, 6, 6, 6,
		7, 7, 7, 7, 7, 7, 7, 7, 7, 7,
		8, 8, 8, 8, 8, 8, 8, 8, 8, 8
	};
	
	public static final int RANKS[] = {
		0, 0, 0, 0, 0, 0, 0, 0, 0,
		1, 1, 1, 1, 1, 1, 1, 1, 1,
		2, 2, 2, 2, 2, 2, 2, 2, 2,
		3, 3, 3, 3, 3, 3, 3, 3, 3,
		4, 4, 4, 4, 4, 4, 4, 4, 4,
		5, 5, 5, 5, 5, 5, 5, 5, 5,
		6, 6, 6, 6, 6, 6, 6, 6, 6,
		7, 7, 7, 7, 7, 7, 7, 7, 7,
		8, 8, 8, 8, 8, 8, 8, 8, 8,
		9, 9, 9, 9, 9, 9, 9, 9, 9
	};
	
	public static final int FILES[] = {
		0, 1, 2, 3, 4, 5, 6, 7, 8,
		0, 1, 2, 3, 4, 5, 6, 7, 8,
		0, 1, 2, 3, 4, 5, 6, 7, 8,
		0, 1, 2, 3, 4, 5, 6, 7, 8,
		0, 1, 2, 3, 4, 5, 6, 7, 8,
		0, 1, 2, 3, 4, 5, 6, 7, 8,
		0, 1, 2, 3, 4, 5, 6, 7, 8,
		0, 1, 2, 3, 4, 5, 6, 7, 8,
		0, 1, 2, 3, 4, 5, 6, 7, 8,
		0, 1, 2, 3, 4, 5, 6, 7, 8
	};
	
	public static final int[] BOTTOM = {
		0, 10, 20, 30, 40, 50, 60, 70, 80
	};
	
	public static final int[] BOTTOMS = {
		0, 9, 18, 27, 36, 45, 54, 63, 72, 81
	};
	
	public static final int MAX_MOVE_NUM = 256;
	public final static int LOOP_HASH_MASK = 0x3ff;
	public final static int MAX_CONSECUTIVE_MOVES = 200;
	
	private int player;//0-red  1-black
    
    //90 index of the board  0 means no piece   16~31means red pieces   32~47means black pieces  
    private int[] square;
    
    //[48]  0~15=-1   means the piece's square in the board
    private int[] pieces;
    
    private String[] bitFiles;//9条纵线的二进制表示
    
    private String[] bitRanks;//10条横线的二进制表示
    
    public int[] evalue;//双方棋子的子力值
    
    //Zobrist Key and Lock
    private long zobristKey;
    private long zobristLock;
    
    public int moveNum;//移动了多少步
    public MoveNode[] moveList;//移动的招数列表
    
    public final static int[] PIECE_TYPE = {
    	-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    	 0,  1,  1,  2,  2,  3,  3,  4,  4,  5,  5,  6,  6,  6,  6,  6,
    	 7,  8,  8,  9,  9, 10, 10, 11, 11, 12, 12, 13, 13, 13, 13, 13
    };
    
    public final static char[] PIECE_CHAR = {
    	'0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0',
    	'K','A','A','B','B','N','N','R','R','C','C','P','P','P','P','P',
    	'k','a','a','b','b','n','n','r','r','c','c','p','p','p','p','p'
    };

	   
    public ActiveBoard()
    {
    	player = 0;
    	square = new int[90];
    	for(int i=0;i<90;i++)
    	{
    		square[i] = 0;
    	}
    	
    	pieces = new int[48];
    	for(int i=0;i<48;i++)
    	{
    		pieces[i] = -1;
    	}
    	
    	bitFiles = new String[9];
    	for(int i=0;i<9;i++)
    	{
    		bitFiles[i] = "0000000000";
    	}
    	
    	bitRanks = new String[10];
    	for(int i=0;i<10;i++)
    	{
    		bitRanks[i] = "000000000";
    	}
    	
    	evalue = new int[2];
    	
    	zobristKey = zobristLock = 0;
    	moveNum = 1;
    	moveList = new MoveNode[MAX_MOVE_NUM];
    	for(int i=0;i<MAX_MOVE_NUM;i++)
    	{
    		moveList[i] = new MoveNode();
    	}
    	
    }
    
    /**
     * 改变走子方
     */
    private void changeSide()
    {
    	player = 1 - player;
    	zobristKey ^= IllegalMove.ZobristKeyPlayer;
    	zobristLock ^= IllegalMove.ZobristLockPlayer;
    }
    
    /**
     * 设置棋子，将棋盘上指定的位置设置为某棋子
     * 
     * @param squareIndex 棋盘上的位置
     * @param pieceIndex 棋子
     */
    private void setPiece(int squareIndex, int pieceIndex)
    {
    	square[squareIndex] = pieceIndex;
    	pieces[pieceIndex] = squareIndex;
    	
    	//更新棋盘的行列字符串数组
    	updateBitFileRank(squareIndex, true);
    	//更新每一方的自力总和
    	updateEvalue(squareIndex, pieceIndex, true);
    }
    
    /**
     * 清楚棋盘指定位置上的棋子
     * 
     * @param squareIndex 棋盘上的位置
     */
    private void clearPiece(int squareIndex)
    {
    	int pieceIndex = square[squareIndex];
    	square[squareIndex] = 0;
    	pieces[pieceIndex] = -1;
    	
    	//更新棋盘的行列字符串数组
    	updateBitFileRank(squareIndex, false);
    	//更新每一方的自力总和
    	updateEvalue(squareIndex, pieceIndex, false);
    }
    
    private void updateBitFileRank(int squareIndex, boolean isAdd)
    {
    	int row = squareIndex/9;
    	int column = squareIndex%9;
    	if(isAdd)
    	{
    	    bitRanks[row] = bitRanks[row].substring(0, column) + "1" +
    	        bitRanks[row].substring(column+1, bitRanks[row].length());
    	    bitFiles[column] = bitFiles[column].substring(0, row) + "1" +
    	        bitFiles[column].substring(row+1, bitFiles[column].length());
    	}
    	else
    	{
    		bitRanks[row] = bitRanks[row].substring(0, column) + "0" +
	            bitRanks[row].substring(column+1, bitRanks[row].length());
	        bitFiles[column] = bitFiles[column].substring(0, row) + "0" +
	            bitFiles[column].substring(row+1, bitFiles[column].length());
    	}
    }
    
    /**
     * 更新局面双方的自力总和
     * 
     * @param squareIndex 棋子在棋盘上的位置
     * @param pieceIndex 棋子
     * @param isAdd 是添加该棋子还是移去该棋子
     */
    private void updateEvalue(int squareIndex, int pieceIndex, boolean isAdd)
    {
    	if(pieceIndex==0)
    	{
    		return;
    	}
    	int pieceType = ActiveBoard.PIECE_TYPE[pieceIndex];
    	zobristKey ^= IllegalMove.ZobristKeyTable[pieceType][squareIndex];
    	zobristLock ^= IllegalMove.ZobristLockTable[pieceType][squareIndex];
    	
    	int side = 0;
    	int value = 0;
    	if(pieceType<7)
    	{
    		side = 0;
    		value = CCEvalue.BasicValues[pieceType] + CCEvalue.PosValues[pieceType][89-squareIndex];
    	}
    	else
    	{
    		side = 1;
    		value = CCEvalue.BasicValues[pieceType-7] + CCEvalue.PosValues[pieceType-7][squareIndex];
    	}
    	
    	if(isAdd)
    	{
    		evalue[side] += value;
    	}
    	else
    	{
    		evalue[side] -= value;
    	}
    }
    
    public void makeMove(MoveNode move)
    {
    	int srcIndex = square[move.src];
    	int dstIndex = square[move.dst];
    	if(dstIndex!=0)
    	{
    		move.cap = dstIndex;
    		clearPiece(move.dst);
    	}
    	clearPiece(move.src);
    	setPiece(move.dst, srcIndex);
    	moveList[moveNum] = move;
    	moveNum++;
    	
    	changeSide();
    }
    
    /**
     * 移动棋子
     * 
     * @param move 一个着法
     * @return
     */
    public boolean movePiece(MoveNode move)
    {
    	if(move.src<0 || move.dst<0)
    	{
    		return false;
    	}
    	
    	int srcPiece = square[move.src];
    	int capPiece = square[move.dst];
    	//long oldZobristKey = zobristKey;
    	
    	//检查着法的目标位置是否有棋子，如果有则清除之
    	if(capPiece!=0)
    	{
    		clearPiece(move.dst);
    	}
    	clearPiece(move.src);
    	setPiece(move.dst, srcPiece);
    	
    	//如果移动该着法后本方处于被将状态，则不能移动该着法，必须还原
    	if(isChecked(player))
    	{
    		srcPiece = square[move.dst];
    		clearPiece(move.dst);
    		setPiece(move.src, srcPiece);
    		if(capPiece!=0)
    		{
    			setPiece(move.dst, capPiece);
    		}
    		return false;
    	}
    	else
    	{
    		MoveNode thisMove = move;
    		thisMove.cap = capPiece;
    		thisMove.chk = isChecked(player);
    		moveList[moveNum] = thisMove;
    		moveNum++;
    		changeSide();
    		return true;
    	}
    }
    
    public void undoMove()
    {
    	moveNum--;
    	MoveNode lastMove = moveList[moveNum];
    	int moved = square[lastMove.dst];
    	
    	clearPiece(lastMove.dst);  	
    	setPiece(lastMove.src, moved);
    	if(lastMove.cap!=0)
    	{
    		setPiece(lastMove.dst, lastMove.cap);
    	}
    	changeSide();
    }
    
    /**
     * 走一个空着，需要改变走子方，同时增加一个空着保存到moveList中
     */
    public void nullMove()
    {
    	MoveNode thisMove = new MoveNode();
    	changeSide();
    	thisMove.src = thisMove.dst = thisMove.cap = -1;
    	thisMove.chk = false;
    	moveList[moveNum] = thisMove;
    	moveNum++;
    }
    
    /**
     * 撤销空着
     */
    public void undoNull()
    {
    	moveNum--;
    	changeSide();
    }
    
    public int evaluation()
    {
    	if(pieces[player==0?16:32]==-1)
    	{
    		return -CCEvalue.MaxValue;
    	}
    	return evalue[player] - evalue[1-player];
    }
    
    /**
     * 判断一个着法是否合法
     * 
     * @param move
     * @return
     */
    public boolean isLeagelMove(MoveNode move)
    {
    	if(square[move.src]==0)
    	{
    		return false;
    	}
    	
    	//所选的棋子是否是当前player的
    	if((player==0&&square[move.src]>31)||(player==1&&square[move.src]<32))
    	{
    		return false;
    	}
    	
    	//如果目标位置有本方的棋子，则不合理。换言之，判断所吃的棋子是否是对方的
    	if((player==0&&square[move.dst]<32&&square[move.dst]>15)||
    			(player==1&&square[move.dst]>31))
    	{
    		return false;
    	}
    	
    	int piece = square[move.src];
    	int pieceType = PIECE_TYPE[piece];
    	if(pieceType>=7)
    	{
    		pieceType -= 7;
    	}
    	SortedMove sortedMove;
    	int[][] histTab = new int[90][90];
    	switch(pieceType)
    	{
    	case 0:
    		sortedMove = new SortedMove();
    		sortedMove.generateKingMoves(this, histTab);
    		if(isContained(move, sortedMove.moveList, sortedMove.moveNum))
    		{
    			return true;
    		}
    	case 1:
    		sortedMove = new SortedMove();
    		sortedMove.generateAdvisorMoves(this, histTab);
    		if(isContained(move, sortedMove.moveList, sortedMove.moveNum))
    		{
    			return true;
    		}
    	case 2:
    		sortedMove = new SortedMove();
    		sortedMove.generateBishopMoves(this, histTab);
    		if(isContained(move, sortedMove.moveList, sortedMove.moveNum))
    		{
    			return true;
    		}
    	case 3:
    		sortedMove = new SortedMove();
    		sortedMove.generateKnightMoves(this, histTab);
    		
    		if(isContained(move, sortedMove.moveList, sortedMove.moveNum))
    		{
    			return true;
    		}
    	case 4:
    		sortedMove = new SortedMove();
    		sortedMove.generateRookMoves(this, histTab);
    		if(isContained(move, sortedMove.moveList, sortedMove.moveNum))
    		{
    			return true;
    		}
    	case 5:
    		sortedMove = new SortedMove();
    		sortedMove.generateCannonMoves(this, histTab);
    		if(isContained(move, sortedMove.moveList, sortedMove.moveNum))
    		{
    			return true;
    		}
    	case 6:
    		sortedMove = new SortedMove();
    		sortedMove.generatePawnMoves(this, histTab);
    		if(isContained(move, sortedMove.moveList, sortedMove.moveNum))
    		{
    			return true;
    		}
    	}   	   	
    	return false;
    }
    
    
    /**
     * 检测是否处于被将军的状态
     * 
     * @param players
     * @return
     */
    public boolean isChecked(int players)
    {
    	int srcSquare = pieces[(players+1)<<4];
    	if(srcSquare==-1)//被将
    	{
    		return true;
    	}
    	
    	SortedMove sortedMove = new SortedMove();
    	sortedMove.generateAllChecks(this, players);//生成所有的将军着法
    	
		for (int i = 0; i < sortedMove.horseChecks.length; i++) //判断马将军
		{
			if(sortedMove.horseChecks[i]!=-1)
			{
				if (square[sortedMove.horseChecks[i]] == (players == 0 ? 37 : 21)
						|| square[sortedMove.horseChecks[i]] == (players == 0 ? 38 : 22)) 
				{
					return true;
				}
			}
		}
		for (int i = 0; i < sortedMove.pawnChecks.length; i++) //判断兵将军
		{
			if(sortedMove.pawnChecks[i]!=-1)
			{
			    if (square[sortedMove.pawnChecks[i]] >= (players == 0 ? 43 : 27)
					    && square[sortedMove.pawnChecks[i]] <= (players == 0 ? 47 : 31)) 
			    {
				    return true;
			    }
			}
		}
		for (int i = 0; i < sortedMove.rookChecks.length; i++) 
		{
			if (sortedMove.rookChecks[i] != -1) 
			{
				if (square[sortedMove.rookChecks[i]] == (players == 0 ? 39 : 23)
						|| square[sortedMove.rookChecks[i]] == (players == 0 ? 40 : 24)) 
				{
					return true;
				}
			}
		}
		for (int i = 0; i < sortedMove.cannonChecks.length; i++) 
		{
			if(sortedMove.cannonChecks[i]!=-1)
			{
			    if (square[sortedMove.cannonChecks[i]] == (players == 0 ? 41 : 25)
					    || square[sortedMove.cannonChecks[i]] == (players == 0 ? 42 : 26)) 
			    {
				    return true;
			    }
			}
		}
    	return false;
    }
    
    public MoveNode lastMove()
    {
    	return moveList[moveNum-1];
    }
    
    /*
     * 判断一个着法是否能解除当前被将的状态
     */
    public boolean canMakeKingSafe(MoveNode move, int players)
    {
    	if(isChecked(players))
    	{
    		makeMove(move);
    		if(!isChecked(players))
    		{
    			undoMove();
    			return true;
    		}
    		undoMove();
    	}
    	return false;
    }
    
    /*
     * 判断一个着法走过是否导致原本不被将状态变成了被将状态。 
     */
    public boolean makeKingUnsafe(MoveNode move, int players)
    {
    	if(!isChecked(players))
    	{
    		makeMove(move);
    		if(isChecked(players))
    		{
    			undoMove();
    			return true;
    		}
    		undoMove();
    	}
    	return false;
    }
    
    public boolean isWon()
    {
    	int oppKingIndex = pieces[(1-player)==0?32:16];
    	if(oppKingIndex==-1)
    	{
    		return true;
    	}
    	return false;
    }
    
    public boolean isLost()
    {
    	int kingIndex = pieces[player==0?32:16];
    	if(kingIndex==-1)
    	{
    		return true;
    	}
    	return false;
    }
    
    /**
     * fen字符串实例: rnbakabnr/9/1c5c1/p1p1p1p1p/9/9/P1P1P1P1P/1C5C1/9/RNBAKABNR w 0 1
     * 第一部分(w前面的字符):表示棋盘布局，小写表示黑方棋子，大写表示红方棋子。
     *   例如前九个字母rnbakabnr表示棋盘第一行的棋子分别为黑方的"车马象士将士象马车"，
     *   "/"为棋盘行与行之间的分割；数字9(5,1)表示该行从该位置起连续9(5,1)个位置无棋子
     * 第二部分(w):表示轮到哪一方走棋，如果是w表示轮到红方(白方)走，是b表示黑方
     * 第三部分(w后的数字0):表示自然限着
     * 第四部分(w后的数字1):表示当前局面的回合数
     * 
     * @param fen
     */
    public void loadFen(String fen)
    {
    	int[] blackPieces = new int[7];
    	int[] redPieces = new int[7];
    	redPieces[0] = 16;
    	redPieces[1] = 17;
    	for(int i=2;i<7;i++)
    	{
    		redPieces[i] = redPieces[1] + 2*(i-1);
    	}
    	for(int i=0;i<7;i++)
    	{
    		blackPieces[i] = redPieces[i] + 16;
    	}
    	String[] parts = fen.split(" ");
    	String[] rows = parts[0].split("/");
    	for(int i=0;i<rows.length;i++)
    	{
    		int offset = 0;
    		for(int j=0;j<rows[i].length();j++)
    		{
    			char c = rows[i].charAt(j);
    			if(c>='0'&&c<='9')
    			{
    				int num = Character.getNumericValue(c);
    				for(int k=0;k<num;k++)
    				{
    					square[9*i+offset+k] = 0;
    				}
    				offset+=num;
    			}
    			if(c>='a'&&c<='z')
    			{
    				int pieceType = piecesType(c);
    				setPiece(9*i+offset, blackPieces[pieceType]);
    				blackPieces[pieceType]++;
    				offset++;
    			}
    			if(c>='A'&&c<='Z')
    			{
    				int pieceType = piecesType(c);
    				setPiece(9*i+offset, redPieces[pieceType]);
    				redPieces[pieceType]++;
    				offset++;
    			}
    		}
    	}
    	player = parts[1].equals("w")?0:1;
    }
    
    public String getFenStr()
    {
    	StringBuffer sb = new StringBuffer();
    	String rowStr;
    	for(int i=0;i<10;i++)
    	{
    		rowStr = bitRanks[i];
    		int constant = 0;
    		for(int j=0;j<9;j++)
    		{
    			
    			if(rowStr.charAt(j)=='0')
    			{
    				constant++;
    				if(j==8)
    				{
    					sb.append(constant);
    				}
    				continue;
    			}
    			else
    			{
    				if(constant!=0)
    				{
    					sb.append(constant);
    					constant = 0;
    				}
    				int location = 9*i+j;
    				int piece = square[location];
    				char pieceChar = PIECE_CHAR[piece];
    				sb.append(pieceChar);
    			}
    		}
    		if(i!=9)
    		{
    		    sb.append('/');
    		}
    	}
    	
    	sb.append(" ").append(player==0?'w':'b');
    	return sb.toString();
    }
    
    private int piecesType(char c)
    {
		switch (c) 
		{
		case 'k':
		case 'K':
			return 0;
		case 'a':
		case 'A':
			return 1;
		case 'b':
		case 'B':
			return 2;
		case 'n':
		case 'N':
			return 3;
		case 'r':
		case 'R':
			return 4;
		case 'c':
		case 'C':
			return 5;
		default:
			return 6;
		}
    }
    
    /*private char pieceChar(int type)
    {
    	return '0';
    }*/
    
    private boolean isContained(MoveNode move, MoveNode[] moveList, int moveNum)
    {
    	int length = moveList.length>moveNum?moveNum:moveList.length;
    	
    	for(int i=0;i<length;i++)
    	{
    		if(moveList[i].equals(move))
    		{
    			return true;
    		}
    	}
    	return false;
    }
    
    public int getPlayer()
    {
    	return player;
    }
    
    public int getOppPlayer()
    {
    	return 1 - player;
    }
    
    public int getPieceSquare(int pieceIndex)
    {
    	return pieces[pieceIndex];
    }
    
    public int getSquarePiece(int squareIndex)
    {
    	return square[squareIndex];
    }
    
    public int getRow(int squareIndex)
    {
    	return squareIndex/9;
    }
    
    public int getColumn(int squareIndex)
    {
    	return squareIndex%9;
    }
    
    public String getRowString(int row)
    {
    	return bitRanks[row];
    }
    
    public String getColumnString(int column)
    {
    	return bitFiles[column];
    }
    
    public long getZobristKey()
    {
    	return zobristKey;
    }
    
    public long getZobristLock()
    {
    	return zobristLock;
    }
    
    public int getMoveNum()
    {
    	return moveNum;
    }
    
    public int getEvalue(int side)
    {
    	return evalue[side];
    }
    
    public void print()
    {
    	for(int i=0;i<10;i++)
    	{
    		for(int j=0;j<9;j++)
    		{
    			System.out.print((square[9*i+j]<10?square[9*i+j]+" ":square[9*i+j])+ " ");
    		}
    		System.out.println();
    	}
    	
    	System.out.println("--------------------");
    	for(int i=0;i<10;i++)
    	{
    		System.out.println(bitRanks[i]);
    	}
    	System.out.println("--------------------");
    	for(int i=0;i<9;i++)
    	{
    		System.out.println(bitFiles[i]);
    	}
    	
    	System.out.println("player:"+player);
    	
    	for(int i=0;i<2;i++)
    	{
    		System.out.println("evalue["+i+"]"+evalue[i]);
    	}
    	
    	System.out.println("whether to be checked? "+isChecked(1));
    }
    
    
    
    
    public static void main(String[] args)
    {
    	ActiveBoard board = new ActiveBoard();
    	board.loadFen("rnbakabnr/9/1c5c1/p1p1p1p1p/9/9/P1P1P1P1P/1C5C1/9/RNBAKABNR w 0 1");
    	//board.clearPiece(89);
    	//board.makeMove(new MoveNode(70, 7));
    	//board.undoMove();
    	
    	
    	/*board.makeMove(new MoveNode(64, 1));
    	board.makeMove(new MoveNode(0, 1));
    	System.out.println(board.evaluation());*/
    	/*board.makeMove(new MoveNode(64, 46));
    	board.makeMove(new MoveNode(46, 49));
    	
    	board.print();
    	
    	boolean flag = board.canMakeKingSafe(new MoveNode(3, 13), 1);
    	if(flag)
    	{
    		System.out.println("This move can make king safe.");
    	}
    	else
    	{
    		System.out.println("This move cannot make king safe.");
    	}*/
    	
    	/*board.makeMove(new MoveNode(64, 67));
    	board.makeMove(new MoveNode(19, 20));
    	board.makeMove(new MoveNode(82, 63));
    	board.makeMove(new MoveNode(1, 18));
    	board.makeMove(new MoveNode(81, 82));
    	board.makeMove(new MoveNode(0, 9));
    	board.makeMove(new MoveNode(82, 1));
    	board.makeMove(new MoveNode(9, 12));
    	board.makeMove(new MoveNode(1, 2));
    	boolean flags = board.makeKingUnsafe(new MoveNode(3, 13), 1);
    	if(flags)
    	{
    		System.out.println("This move make king unsafe");
    	}
    	else
    	{
    		System.out.println("This move does not make king unsafe");
    	}
    	
    	boolean flag = board.canMakeKingSafe(new MoveNode(3, 13), 1);
    	if(flag)
    	{
    		System.out.println("This move can make king safe.");
    	}
    	else
    	{
    		System.out.println("This move cannot make king safe.");
    	}*/
    	board.print();
    	
    	String fenStr = board.getFenStr();
    	System.out.println(fenStr);
    }
       
}
