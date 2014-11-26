package com.engine;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import org.apache.commons.logging.*;

public class SearchEngine 
{
    private static Log log = LogFactory.getLog(SearchEngine.class);
	
	public static final int MaxBookMove = 40;//使用开局库的最大步数
    public static final int MaxKiller = 4;//搜索杀招的最大步数
    private static final int BookUnique = 1;//指示节点类型,下同
	private static final int BookMulti = 2;
	private static final int HashAlpha = 4;
    private static final int HashBeta = 8;
    private static final int HashPv = 16;
    
    private static final int ObsoleteValue = -CCEvalue.MaxValue - 1;
    private static final int UnknownValue = -CCEvalue.MaxValue - 2;
    
    public static final int CLOCK_S = 1000;//1s = 1000millseconds
    public static final int CLOCK_M = 1000*60;//1minute = 60seconds
    private static final Random rand = new Random();
    
    public MoveNode bestMove;
    
    //for search control
    private int depth;
    private long properTimer, limitTimer;
    
    //搜索过程的全局变量,包括:
    //1.搜索树和历史表
    private ActiveBoard activeBoard;
    //public ActiveBoard board;
    private int histTab[][];
    
    //2.搜索选项
    private int selectMask,style;//下棋风格default = EngineOption.Normal;
    //selectMask:随机性, wideQuiesc(保守true if style==EngineOption.solid)
    //futility(true if style==EngineOption.risky冒进)
    //NullMove 是否空着裁剪
    //private boolean wideQuiesc;
    private boolean futility, nullMove;
    private boolean ponder;
    
    //3.时间控制参数
    private boolean stop;
    private long startTimer, minTimer, maxTimer;
    private int startMove;
    
    //4.统计信息:Main Search Nodes,Quiescence Search Nodes and Hash Nodes
    private int nodes, nullNodes, hashNodes, killerNodes, alphaNodes, betaNodes, pvNodes, mateNodes, leafNodes;
    private int quiescNullNodes, quiescAlphaNodes, quiescBetaNodes, quiescPvNodes, quiescMateNodes;
    private int hitBeta, hitAlpha, hitPv;
    
    //5.搜索结果
    private int lastScore, pvLineNum;
    private MoveNode pvLine[] = new MoveNode[ActiveBoard.MAX_MOVE_NUM];
    
    //6.Hash and Book Structure
    private int hashMask, maxBookPos, bookPosNum;
    public HashRecord[] hashList;
    private BookRecord[] bookList;
    
    public int recordIndex;
    
    public static int level = 0;
    
    public SearchEngine()
    {
    	int i;
    	histTab = new int[90][90];
    	nodes=nullNodes=hashNodes=killerNodes=betaNodes=pvNodes=alphaNodes=mateNodes=leafNodes=0;
    	selectMask=0;//1<<10-1;//随机性
    	style=EngineOption.Normal;
    	//wideQuiesc=style==EngineOption.Solid;
    	futility=style==EngineOption.Risky;
    	nullMove = true;
    	
    	//Search Results
    	lastScore=0;
    	pvLineNum=0;
    	MoveNode[] PvLine = new MoveNode[ActiveBoard.MAX_MOVE_NUM];
    	for(i=0;i<ActiveBoard.MAX_MOVE_NUM;i++)
    	{
    		PvLine[i] = new MoveNode();
    	}
    	newHash(17, 14);
    	depth = 8;
    	properTimer = CLOCK_M*1;
    	limitTimer = CLOCK_M*20;
    }
    
    public SearchEngine(ActiveBoard activeBoard)
    {
    	this();
    	this.activeBoard = activeBoard;
    	
    	//hashList = new HashRecord[1<<17];

    	//newHash(17);
    	//recordIndex = 0;
    }
    
    //Begin History and Hash Table Procedures
    public void newHash(int hashScale, int bookScale)
    {
    	histTab = new int[90][90];
    	
    	//这里有个技巧.假设有两个数a和b,且a<b,b=Math.pow(2,n)-1   那么这种情况下a%b和a&b是相等的
    	hashMask = (1<<hashScale)-1;
    	maxBookPos = 1<<bookScale;
    	hashList = new HashRecord[hashMask+1];
    	for(int i=0;i<hashMask+1;i++)
    	{
    		hashList[i] = new HashRecord();
    	}
    	
    	bookList = new BookRecord[maxBookPos];
    	
    	clearHistTab();//清空历史表，所有值都清0
    	clearHash();//将hashList中所有flag设为0
    }
    
    public void newHash(int hashScale)
    {
    	hashMask = (1<<hashScale)-1;
    	hashList = new HashRecord[hashMask+1];
    	for(int i=0;i<hashMask+1;i++)
    	{
    		hashList[i] = new HashRecord();
    	}
    }
    
    public void clearHistTab()
    {
    	for(int i=0;i<90;i++)
    	{
    		for(int j=0;j<90;j++)
    		{
    			histTab[i][j] = 0;
    		}
    	}
    }
    
    public void clearHash()
    {
    	for(int i=0;i<hashMask;i++)
    	{
    		hashList[i].flag = 0;
    	}
    }
    
    
    
    /**
     * 查询hashList，即从置换表中查找
     * 
     * @param hashMove
     * @param alpha
     * @param beta
     * @param depth
     * @return
     */
    private int probeHash(MoveNode hashMove, int alpha, int beta, int depth)
    {
    	boolean MateNode;
    	HashRecord tempHash;
    	
    	//得到当前局面在置换表中的项
    	int tempInt = (int)(activeBoard.getZobristKey()&hashMask);
    	tempHash = hashList[tempInt];
    	
    	long tempLong1 = activeBoard.getZobristLock();
    	long tempLong2 = tempHash.zobristLock;
    	
    	//如果置换表中保存了当前局面的招法
    	//且确定当前局面的zobristLock和置换表中的zobristLock相等
    	if(tempHash.flag!=0 && tempLong1==tempLong2)
    	{
    		MateNode = false;
    		if(tempHash.value>CCEvalue.MaxValue-ActiveBoard.MAX_MOVE_NUM/2)
    		{
    			tempHash.value -= activeBoard.getMoveNum() - startMove;
    			MateNode = true;
    		}
    		else if(tempHash.value<ActiveBoard.MAX_MOVE_NUM/2-CCEvalue.MaxValue)
    		{
    			tempHash.value += activeBoard.getMoveNum() - startMove;
    			MateNode = true;
    		}
    		if(MateNode || tempHash.depth>=depth)
    		{
    			if((tempHash.flag & HashBeta)!=0)
    			{
    				if(tempHash.value>=beta)
    				{
    					hitBeta++;
    					return tempHash.value;
    				}
    			}
    			else if((tempHash.flag & HashAlpha)!=0)
    			{
    				if(tempHash.value<=alpha)
    				{
    					hitAlpha++;
    					return tempHash.value;
    				}
    			}
    			else if((tempHash.flag & HashPv)!=0)
    			{
    				hitPv++;
    				return tempHash.value;
    			}
    			else
    			{
    				return UnknownValue;
    			}
    		}
    		if(tempHash.bestMove.src == -1)
    		{
    			return UnknownValue;
    		}
    		else
    		{
    			hashMove = tempHash.bestMove;
    			return ObsoleteValue;
    		}
    	}
    	return UnknownValue;
    }
    
    private void recordHash(int value, MoveNode move, int depth)
    {
    	HashRecord record = new HashRecord(value, move, depth);
    	hashList[recordIndex] = record;
    	recordIndex++;
    }
    
    /**
     * 将特定深度的最佳着法存入置换表
     * 
     * @param flag 标志位，标志某一局面是否已经存储了一个最佳着法
     * @param value  着法的值
     * @param depth  搜索的深度
     * @param move  着法
     */
    private void recordHash(int flag, int value, int depth, MoveNode move)
    {
    	//首先找到当前局面对应置换表中哪一项
    	HashRecord tempHash = hashList[(int)(activeBoard.getZobristKey()&hashMask)];
    	
    	//如果该项已经存有着法且搜索的深度比此次还要深，那么不用更新置换表，直接返回
    	if((tempHash.flag!=0) && tempHash.depth>depth)
    	{
    		return;
    	}
    	
    	//将本次的新信息保存起来
    	tempHash.zobristLock = activeBoard.getZobristLock();
    	tempHash.flag = flag;
    	tempHash.value = value;
    	tempHash.depth = depth;
    	
    	if(tempHash.value>CCEvalue.MaxValue-ActiveBoard.MAX_MOVE_NUM/2)
    	{
    		tempHash.value+=activeBoard.getMoveNum()-startMove;
    	}
    	else if(tempHash.value<ActiveBoard.MAX_MOVE_NUM/2-CCEvalue.MaxValue)
    	{
    		tempHash.value-=activeBoard.getMoveNum()-startMove;
    	}
    	tempHash.bestMove = move;
    	
    	//更新置换表
    	hashList[(int)(activeBoard.getZobristKey()&hashMask)] = tempHash;
    }
    
    //----------------------------------------------------------------------------------关注here
    /**
     * 获取最佳着法及其线路，显然最后的结果是保存在pvLine数组中的
     * 
     */
    private void GetPvLine()
    {
    	//首先找到当前局面对应的置换表中的项
    	HashRecord tempHash = hashList[(int)(activeBoard.getZobristKey()&hashMask)];
    	
    	//确认该项的信息
    	if((tempHash.flag!=0)
    			&& tempHash.bestMove.src!=-1 && tempHash.zobristLock==activeBoard.getZobristLock())
    	{
    		//将置换表的最佳着法保存到pvLine数组中
    		pvLine[pvLineNum] = tempHash.bestMove;
    		activeBoard.movePiece(tempHash.bestMove);
    		pvLineNum++;
    		/*---------------------------------------------------------------------------------------关注here
    		 * if(activeBoard.isLoop(1)==0)
    		 * {
    		 *     GetPvLine();
    		 * }
    		 */
    		activeBoard.undoMove();
    	}
    }
    
    //---------------------------------------------------------------------------------------关注here
    //record example：i0h0 4 rnbakabr1/9/4c1c1n/p1p1N3p/9/6p2/P1P1P3P/2N1C2C1/9/R1BAKAB1R w - - 0 7
    //i0h0:Move--代表着法  4:evalue--代表评价值,  other:FEN String--Fen字符串
    //暂时未使用该方法
	//@SuppressWarnings("unused")
	public void loadBook(final String bookFile)//开局库
    {
    	int bookMoveNum;
    	//int value;
    	//int i;
    	BufferedReader inFile = null;
    	String lineStr;
    	int index = 0;
    	MoveNode bookMove = new MoveNode();
    	HashRecord tempHash;
    	ActiveBoard bookBoard = new ActiveBoard();
    	try 
    	{
			inFile = new BufferedReader(new FileReader(bookFile), 1024*1024);
		} 
    	catch (FileNotFoundException e) 
    	{
			e.printStackTrace();
		}
    	if(inFile==null)
    	{
    		return;
    	}
    	bookPosNum = 0;
    	int recordedToHash = 0;//for test
    	
    	//读取文件
    	try 
    	{
			while((lineStr=inFile.readLine())!=null)
			{
				bookMove = new MoveNode();
				//lineStr的前4个字符为着法
				bookMove.move(lineStr);
				index = 0;
				
				//如果该着法不为空
				if(bookMove.src!=-1)
				{
					//index加5的目的是跳过4个字符的着法，使index尽快找到FEN字符串
					index+=5;
					while(lineStr.charAt(index)==' ')
					{
						index++;
					}
					bookBoard.loadFen(lineStr.substring(index));
					long tmpZobristKey = bookBoard.getZobristKey();
					int piece = bookBoard.getSquarePiece(bookMove.src);
					if(piece!=0)//如果src上棋子存在
					{
						//寻找该局面在置换表中对应的项
						tempHash = hashList[(int)(tmpZobristKey&hashMask)];
						if(tempHash.flag!=0)//如果保存了该局面的着法
						{
							//如果确认置换表中该项和当前局面相同
							if(tempHash.zobristLock == bookBoard.getZobristLock())
							{
								if((tempHash.flag&BookMulti)!=0)//多个相同走法----------here?
								{
									bookMoveNum = bookList[tempHash.value].moveNum;
									if(bookMoveNum<MaxBookMove)
									{
										bookList[tempHash.value].moveList[bookMoveNum] = bookMove;
										bookList[tempHash.value].moveNum++;
										recordedToHash++;//for test
									}
								}
								else
								{
									if(bookPosNum<maxBookPos)
									{
										tempHash.flag = BookMulti;
										bookList[bookPosNum] = new BookRecord();
										bookList[bookPosNum].moveNum = 2;
										bookList[bookPosNum].moveList[0] = tempHash.bestMove;
										bookList[bookPosNum].moveList[1] = bookMove;
										tempHash.value = bookPosNum;
										bookPosNum++;
										hashList[(int)(bookBoard.getZobristKey()&hashMask)] = tempHash;
										recordedToHash++;
									}
								}
							}
						}
						else
						{
							tempHash.zobristLock = bookBoard.getZobristLock();
							tempHash.flag = BookUnique;
							tempHash.depth = 0;
							tempHash.value = 0;
							tempHash.bestMove = bookMove;
							hashList[(int)(bookBoard.getZobristKey()&hashMask)] = tempHash;
							recordedToHash++;
						}
					}
				}
			}
			inFile.close();
			log.info("recordedToHash: "+recordedToHash);
		} 
    	catch (IOException e) 
    	{
			e.printStackTrace();
		}   	
    }
    
    //End History and Hash Tables Procedures
    
    //Begin Search Procedures
    //Search Procedures
    private int RAdapt(int depth)
    {
    	//根据不同情况来调整R值得做法,称为"适应性空着裁剪"(Adaptive Null-Move Pruning)
    	//其内容可以概括为
    	//a.深度小于或等于6时,用R=2的空着裁剪进行搜索
    	//b.深度大于8时,用R=3
    	//c.深度是6或7时,如果每方棋子都大于或等于3个,则用R=3,否则用R=2
    	
    	if(depth<=6)
    	{
    		return 2;
    	}
    	else if(depth<=8)
    	{
    		return activeBoard.getEvalue(0)<CCEvalue.EndgameMargin ||
    		    activeBoard.getEvalue(1)<CCEvalue.EndgameMargin?2:3;
    	}
    	else
    	{
    		return 3;
    	}
    }
    
    //如果有人用马吃掉你的炮，那么你最好吃他的马。alpha-beta搜索不是特别针对这种情况的。
    //你把深度参数传递给函数，当深度到达零就做完了，即使一方被将死。
    //一个应对的方法称为"静态搜索"。当alpha-beta用尽深度后，通过调用静态搜索来代替调用evaluation().
    //静态搜索不应该强迫吃子
    //-----------------------------------------------------------------------------------关注here
    private int quiesc(int alpha, int beta)//只对吃子
    {
    	int i, bestValue, thisAlpha, thisValue;
    	boolean inCheck, movable;
    	MoveNode thisMove;
    	SortedMove moveSort = new SortedMove();
    	
    	//1.Return if a Loop position is detected  --------------------------------------------here
    	if(activeBoard.getMoveNum()>startMove)
    	{
    		//thisValue = activeBoard.isLoop(1);
    		/*
    		if(thisValue!=0)
    		{
    			return activeBoard.loopValue(thisValue, activeBoard.getMoveNum()-startMove);
    		}
    		*/
    	}
    	
    	//2.Initialize
    	inCheck = activeBoard.lastMove().chk;
    	movable = false;
    	bestValue = -CCEvalue.MaxValue;
    	thisAlpha = alpha;
    	
    	//3.For non-check position, try Null-Move before generate moves
    	if(!inCheck)
    	{
    		movable = true;
    		thisValue = activeBoard.evaluation()+(selectMask!=0?(rand.nextInt()&selectMask)-(rand.nextInt()&selectMask):0);
    		if(thisValue>bestValue)
    		{
    			//这个函数调用静态评价，如果评价好得足以截断而不需要试图吃子时，就马上返回beta
    			if(thisValue>=beta)
    			{
    				quiescNullNodes++;
    				return thisValue;
    			}
    			bestValue = thisValue;
    			//如果评价不足以产生截断，但是比alpha好，那么就更新alpha来反映静态评价
    			if(thisValue>thisAlpha)
    			{
    				thisAlpha = thisValue;
    			}
    		}
    	}
    	
    	//4.Generate and sort all moves for check position, or capture moves for non-check position
    	moveSort.generateAllMoves(activeBoard, histTab);
    	//moveSort.GenMoves(activeBoard, inCheck?histTab:null);-------------------------here
        
    	for(i=0;i<moveSort.moveNum;i++)
    	{
    		moveSort.bubbleSort(i);
    		thisMove = moveSort.moveList[i];
    		
    		//if inCheck or capture
    		if(inCheck)
    		{
    			if(activeBoard.movePiece(thisMove))
    			{
    				movable = true;
    				
    				//5.Call Quiescence Alpha-Beta Search for every leagal moves
    				thisValue = -quiesc(-beta, -thisAlpha);
    				
    				//for debug
    				String tmpStr = "";
    				for(int k=0;k<activeBoard.getMoveNum();k++)
    				{
    					tmpStr = tmpStr + activeBoard.moveList[k] + ",";
    				}
    				tmpStr = tmpStr + "Value:" + thisValue + "\n";
    				
    				activeBoard.undoMove();
    				
    				//6.select the best move for Fail-Soft Alpha-Beta
    				if(thisValue>bestValue)
    				{
    					if(thisValue>=beta)
    					{
    						quiescBetaNodes++;
    						return thisValue;
    					}
    					bestValue = thisValue;
    					if(thisValue>thisAlpha)
    					{
    						thisAlpha = thisValue;
    					}
    				}
    			}
    		}
    	}
    	
    	//7.Return a loose value if no leagal moves
    	if(!movable)
    	{
    		quiescMateNodes++;
    		return activeBoard.getMoveNum() - startMove - CCEvalue.MaxValue;
    	}
    	if(thisAlpha>alpha)
    	{
    		quiescPvNodes++;
    	}
    	else
    	{
    		quiescAlphaNodes++;
    	}
    	return bestValue;
    }
    
    /**
     * 搜索算法，包括
     * 1.Hash Table
     * 2.超出边界的Alpha-Beta搜索
     * 3.适应性空招裁剪
     * 4.选择性拓展
     * 5.使用Hash Table的迭代加深
     * 6.杀招表
     * 7.将军拓展
     * 8.主要变例搜索
     * 9.历史启发表
     * 
     * 
     * @param alpha alpha
     * @param beta beta
     * @param depth 搜索深度
     * @return value value
     */
    private int search(KillerStruct killerTab, int alpha, int beta, int depth)
    {
    	int i;
    	//int j;
    	int thisDepth;//当前的搜索深度
    	int futPrune;//------------------------关注here
    	int hashFlag;
    	boolean inCheck;//是否被将军
    	boolean movable;//是否可以移动棋子
    	boolean searched;//
    	int hashValue;
    	int bestValue;
    	int thisAlpha;
    	//int thisBeta;
    	int thisValue;
    	int futValue = 0;
    	MoveNode thisMove = new MoveNode();
    	MoveNode bestMove = new MoveNode();
    	SortedMove moveSort = new SortedMove();
    	KillerStruct subKillerTab = new KillerStruct();
    	//1.重复循环检测
    	
    	//2.是否需要扩展，被将军时需要进行扩展
    	inCheck = activeBoard.lastMove().chk;
    	thisDepth = depth;
    	if(inCheck)
    	{
    		thisDepth++;
    	}
    	
    	//3.Return if hit the Hash Table,如果能在置换表中找到最佳值则直接返回
    	hashValue = probeHash(thisMove, alpha, beta, thisDepth);
    	if(hashValue>= -CCEvalue.MaxValue && hashValue<=CCEvalue.MaxValue)
    	{
    		return hashValue;
    	}
    	
    	//4.Return if interrupted or timeout
    	if(interrupt())
    	{
    		return 0;
    	}
    	
    	//5.正式开始搜索
    	if(thisDepth>0)
    	{
    		movable = false;
    		searched = false;
    		bestValue = -CCEvalue.MaxValue;
    		thisAlpha = alpha;
    		//thisBeta = beta;
    		hashFlag = HashAlpha;
    		subKillerTab.moveNum = 0;
    		
    		//6.是否需要空着裁剪与冒进----------------------待关注-------------------here
    		futPrune = 0;
    		if(futility)//----------------------------------------------------------here
    		{
    			//冒进
    			/*if(thisDepth==3 && !inCheck && activeBoard.evaluation() + CCEvalue.RazorMargin<=alpha
    					&&activeBoard.getEvalue(activeBoard.getOppPlayer())>CCEvalue.EndgameMargin)
    			{
    				thisDepth = 2;
    			}
    			if(thisDepth<3)
    			{
    				futValue = activeBoard.evaluation() + (thisDepth == 2?CCEvalue.ExtFutMargin:CCEvalue.SelFutMargin);
    				if(!inCheck&&futValue<=alpha)
    				{
    					futPrune = thisDepth;
    					bestValue = futValue;
    				}
    			}*/
    		}
    		
    		//7.空着裁剪------------------待关注---------------------here
    		
    		
    		//在搜索着法以前(事实上在生成着法以前)，你做一个减少深度的搜索，让对手先走，如果这个
    		//搜索的结果大于或等于beta，那么可以简单地返回beta而不需要搜索任何着法
    		if(nullMove && futPrune==0 && !inCheck && activeBoard.lastMove().src!=-1 &&
    				activeBoard.getEvalue(activeBoard.getPlayer())>CCEvalue.EndgameMargin)
    		{
    			activeBoard.nullMove();//走一步空着
    			
    			//这个代码中可以用一个诀窍，我们需要知道空着搜索的值是否大于等于beta，如果还不如beta，
    			//那么不需要知道它到底比beta差多少，因此可以用极小窗口，试图让裁剪做的更快。
    			//实际上用(beta-1,beta)调用搜索，但是由于递归时必须把alpha和beta颠倒并取负数，所以成了代码中的样子
    			//thisValue = -search(subKillerTab, -beta, 1-beta, thisDepth-1-RAdapt(thisDepth));
    			thisValue = -search(subKillerTab, -beta, 1-beta, thisDepth-1-RAdapt(thisDepth));
    			activeBoard.undoNull();//撤销空着
    			if(thisValue>=beta)
    			{
    				nullNodes++;
    				return beta;
    			}
    		}
    		
    		//8.搜索命中Hash Table,但是Hash Table中的深度不及thisDepth
    		//这一步的必要性，置换表中有当前局面在特定深度下的最佳着法，那么在"迭代加深"中，
    		//首先搜索好的着法可以大幅度提高搜索效率。因为如果在散列项里找到特定深度下的最佳着法，
    		//那么首先搜索这个着法，这样会改进着法顺序，减少分支因子
    		if(hashValue==ObsoleteValue)//表示得到的是一个过时的值
    		{
    			//这里thisMove为Hash Tablez中特定深度的最佳着法
    			if(activeBoard.movePiece(thisMove))//从该着法开始搜索
    			{
    				movable = true;
    				if(futPrune!=0 && -activeBoard.evaluation() + (futPrune==2?CCEvalue.ExtFutMargin:CCEvalue.SelFutMargin)<=alpha
    						&& activeBoard.lastMove().chk)
    				{//这个if判断下面的杀招表中也出现了--------------------------------here
    					activeBoard.undoMove();
    				}
    				else
    				{
    					thisValue = -search(subKillerTab, -beta, -thisAlpha,thisDepth-1);
    					searched = true;
    					activeBoard.undoMove();
        				if(stop)
        				{
        					return 0;
        				}
        				if(thisValue>bestValue)
        				{
        					if(thisValue>=beta)
        					{
        						histTab[thisMove.src][thisMove.dst] += 1<<(thisDepth -1);//----历史表中该为何值？-----------here
        						recordHash(HashBeta, beta, thisDepth, thisMove);
        						hashNodes++;//为什么是hashNodes？--------------------------------------here
        						return thisValue;
        					}
        					bestValue = thisValue;
        					bestMove = thisMove;
        					if(thisValue>thisAlpha)
        					{
        						thisAlpha = thisValue;
        						hashFlag = HashPv;
        						
        						//为什么这里又不需要添加hashNodes了？？？------------------------------here
        						
        						//这个if判断是否有必要加？加了是不是为了保证该着法与当前局面的对应？------here
        						if(activeBoard.getMoveNum()==startMove)
        						{
        						    recordHash(hashFlag, thisAlpha, thisDepth, thisMove);
        						    popInfo(thisAlpha, depth);//--------------------------------------here  why?
        						}
        					}
        				}
    				}
    				
    			}
    		}
    		
    		//9.命中杀着表-----------------------------------------------------------here
    		for(i=0;i<killerTab.moveNum;i++)
    		{
    			thisMove = killerTab.moveList[i];
    			if(activeBoard.isLeagelMove(thisMove))
    			{
    				if(activeBoard.movePiece(thisMove))
    				{
    					movable = true;
    					if(futPrune!=0 && -activeBoard.evaluation() + (futPrune==2?CCEvalue.ExtFutMargin:CCEvalue.SelFutMargin)<=alpha
    							&& activeBoard.lastMove().chk)
    					{
    						activeBoard.undoMove();
    					}
    					else
    					{
    						if(searched)
    						{
    							thisValue = -search(subKillerTab, -thisAlpha-1, -thisAlpha,thisDepth-1);
    							if(thisValue>thisAlpha && thisValue<beta)
    							{
    								thisValue=-search(subKillerTab,-beta,-thisAlpha,thisDepth-1);
    							}
    						}
    						else
    						{
    							thisValue = -search(subKillerTab,-beta,-thisAlpha,thisDepth-1);
    							searched = true;
    						}
    						activeBoard.undoMove();
    						if(stop)
    						{
    							return 0;
    						}
    						if(thisValue>bestValue)
    						{
    							if(thisValue>=beta)
    							{
    								killerNodes++;
    								histTab[thisMove.src][thisMove.dst] += 1<<(thisDepth-1);
    								recordHash(HashBeta, beta, thisDepth, thisMove);
    								return thisValue;
    							}
    							bestValue = thisValue;
    							bestMove = thisMove;
    							if(thisValue>thisAlpha)
    							{
    								thisAlpha = thisValue;
    								hashFlag = HashPv;
    								
    								if(activeBoard.getMoveNum()==startMove)
    								{
    									recordHash(HashPv, thisAlpha, thisDepth, thisMove);
    									popInfo(thisAlpha, depth);//--------------------------------------here  why?
    								}
    							}
    						}
    					}
    				}
    			}
    		}
    		
    		//10.生成当前所有合法着法并排序
    		moveSort.generateAllMoves(activeBoard, histTab);
    		for(i=0;i<moveSort.moveNum;i++)
    		{
    			moveSort.bubbleSort(i);
    			thisMove = moveSort.moveList[i];
    			
    			if(activeBoard.movePiece(thisMove))
    			{
    				movable = true;
    				//11.Alpha-Beta Search
    				if(futPrune!=0 && -activeBoard.evaluation()+(futPrune==2?CCEvalue.ExtFutMargin:CCEvalue.SelFutMargin)<=alpha
    						&& activeBoard.lastMove().chk)
    				{
    					activeBoard.undoMove();
    				}
    				else
    				{
    					if(searched)
    					{
    						thisValue = -search(subKillerTab, -thisAlpha-1, - thisAlpha, thisDepth-1);
    						if(thisValue>thisAlpha && thisValue<beta)
    						{
    							thisValue = -search(subKillerTab, -beta, -thisAlpha, thisDepth-1);
    						}
    					}
    					else
    					{
    						thisValue = -search(subKillerTab, -beta, - thisAlpha, thisDepth-1);
            				searched = true;
    					}
    					
        				activeBoard.undoMove();
        				if(stop)
        				{
        					return 0;
        				}
        				//12.超出边界的Alpha-Beta
        				if(thisValue>bestValue)
        				{
        					if(thisValue>=beta)
        					{
        						betaNodes++;
        						
        						histTab[thisMove.src][thisMove.dst] += 1<<(thisDepth-1);//-------------here
        						
        						recordHash(HashBeta, beta, thisDepth, thisMove);
        						
        						if(killerTab.moveNum<MaxKiller)//--------------------here
        						{
        							killerTab.moveList[killerTab.moveNum] = thisMove;
        							killerTab.moveNum++;
        						}
        						
        						return thisValue;
        					}
        					bestValue = thisValue;
        					bestMove = thisMove;
        					if(thisValue>thisAlpha)
        					{
        						thisAlpha = thisValue;
        						hashFlag = HashPv;
        						
        						//这个if条件判断是否有必要,是为了严格保证着法和局面的一致吗？
                                if(activeBoard.getMoveNum()==startMove)
                                {
        						    recordHash(hashFlag, thisAlpha, thisDepth, thisMove);
        						    popInfo(thisAlpha, depth);//--------------------------------here
                                }
        					}
        				}
    				}
    				
    			}
    		}
    		//13.无路可走，输棋
    		if(!movable)
    		{
    			mateNodes++;//---------------------------------???????
    			return activeBoard.getMoveNum()-startMove-CCEvalue.MaxValue;
    		}
    		
    		//14.Update History Tables and Hash Tables --------------------------------here
    		if(futPrune!=0 && bestValue==futValue)
    		{
    			bestMove.src = bestMove.dst = -1;
    		}
    		if((hashFlag & HashAlpha)!=0)
    		{
    			alphaNodes++;
    		}
    		else
    		{
    			pvNodes++;
    			histTab[bestMove.src][bestMove.dst] += 1<<(thisDepth-1);
    			if(killerTab.moveNum<MaxKiller)
    			{
    				killerTab.moveList[killerTab.moveNum] = bestMove;
    				killerTab.moveNum++;
    			}
    		}
    		recordHash(hashFlag, thisAlpha, thisDepth, bestMove);
    		
    		return bestValue;
    	}
    	//此时depth=0.当alpha-beta搜索用尽深度后，通过调用静态搜索来代替调用深度为0时的evaluation().
    	//这个函数也对局面作出评价，只是避免了在明显有对策的情况下看错局势.
    	else//15.静态搜索------------------------------------关注here
    	{
    		thisValue = quiesc(alpha, beta);
    		thisMove.src = bestMove.dst = -1;
    		if(thisValue<=alpha)
    		{
    			recordHash(HashAlpha, alpha, 0, thisMove);
    		}
    		else if(thisValue>=beta)
    		{
    			recordHash(HashBeta, beta, 0, thisMove);
    		}
    		else
    		{
    			recordHash(HashPv, thisValue, 0, thisMove);
    		}
    		leafNodes++;
    		return thisValue;
    	}
    	//return 0;
    }
    
    private boolean interrupt()
    {
    	return stop?true:false;
    }
    
    public void stopSearch()
    {
    	this.stop = true;
    }
    
    private void popInfo(int value, int depth)
    {
    	int i;
    	//int quiescNodes, nps, npsQuiesc;
    	char[] moveStr;
    	//long tempLong;
    	if(depth!=0)
    	{
    		String logString = "PVNode: depth=" + depth+ ",score=" + value + ",Move: "+"\n";
    		
    		pvLineNum = 0;
    		GetPvLine();
    		
    		for(i = 0;i<pvLineNum;i++)
    		{
    			moveStr = pvLine[i].location();
    			logString += " " + String.copyValueOf(moveStr) + "\n";
    		}
    		if(ponder && System.currentTimeMillis() > minTimer && value + CCEvalue.InadequateValue>lastScore)
    		{
    			stop = true;
    		}
    		if(log.isDebugEnabled())
    		{
    			log.debug(logString);
    		}
    	}
    }
    
    public void setupControl(int depth, long properTimer, long limitTimer)
    {
    	this.depth = depth;
    	this.properTimer = properTimer;
    	this.limitTimer = limitTimer;
    }
    
    public void control()
    {
    	int i, moveNum, thisValue;
    	char[] moveStr;
    	stop = false;
    	bestMove = null;
    	MoveNode thisMove = new MoveNode();
    	MoveNode uniqueMove = new MoveNode();
    	HashRecord tempHash;
    	SortedMove moveSort = new SortedMove();
    	KillerStruct subKillerTab = new KillerStruct();
    	
    	//The computer thinking procedure
    	//1.Search the moveNodes in Book----------------------------------here
    	int tmpInt = (int)(activeBoard.getZobristKey() & hashMask);
    	tempHash = hashList[tmpInt];
    	if(tempHash.flag!=0 && tempHash.zobristLock==activeBoard.getZobristLock())
    	{
    		if((tempHash.flag==BookUnique))
    		{
    			moveStr = tempHash.bestMove.location();
    			bestMove = new MoveNode(String.copyValueOf(moveStr));
    			return;
    		}
    		else if(tempHash.flag==BookMulti)
    		{
    			thisValue = 0;
    			i = Math.abs(rand.nextInt())%(bookList[tempHash.value].moveNum);
    			moveStr = bookList[tempHash.value].moveList[i].location();
    			bestMove = new MoveNode(String.copyValueOf(moveStr));
    			return;
    		}
    	}
    	
    	//2.Initialize Timer and other Counter
    	startTimer = System.currentTimeMillis();
    	minTimer = startTimer + (properTimer>>1);
    	maxTimer = properTimer<<1;
    	if(maxTimer>limitTimer)
    	{
    		maxTimer = limitTimer;
    	}
    	maxTimer += startTimer;
    	stop = false;
    	startMove = activeBoard.getMoveNum();
    	nodes=nullNodes=hashNodes=killerNodes=betaNodes=pvNodes=alphaNodes=mateNodes=leafNodes=0;
    	quiescNullNodes=quiescBetaNodes=quiescPvNodes=quiescAlphaNodes=quiescMateNodes=0;
    	hitBeta = hitPv = hitAlpha = 0;
    	pvLineNum = 0;
    	
    	
    	//3.不合法,主动送将
    	if(activeBoard.isChecked(activeBoard.getOppPlayer()))
    	{
    		return;
    	}
    	//thisValue = activeBoard.isLoop(3);
    	/*if(thisValue!=0)
    	{
    		throw new LostException("");
    	}
    	if(activeBoard.getMoveNum()>ActiveBoard.MAX_MOVE_NUM)
    	{
    		throw new LostException("");
    	}*/
    	
    	//4.测试所有应将的着法
    	if(activeBoard.lastMove().chk)//如果此时处于被将状态
    	{
    		moveNum = 0;
    		moveSort.generateAllMoves(activeBoard, histTab);//生成所有着法
    		for(i=0;i<moveSort.moveNum;i++)
    		{
    			thisMove = moveSort.moveList[i];
    			if(activeBoard.movePiece(thisMove))
    			{
    				activeBoard.undoMove();
    				uniqueMove = thisMove;
    				moveNum++;
    				//如果moveNum>1,说明有多于1个着法能解除被将状态,则去慢慢找吧    跳出该循环
    				if(moveNum>1)
    				{
    					break;
    				}
    					
    			}
    		}
    		//如果moveNum=0,说明没有着法能解除被将状态,则游戏该结束了,输了
    		if(moveNum==0)
    		{
    			if(log.isDebugEnabled())
    			{
    				log.debug("score " + -CCEvalue.MaxValue + "\n");
    			}
    		}
    		//如果moveNum=1 说明恰有一个着法能解除被将状态   则该着法必定是最佳着法
    		if(moveNum==1)
    		{
    			moveStr = uniqueMove.location();
    			if(log.isDebugEnabled())
    			{
    				log.debug("bestmove " + String.copyValueOf(moveStr) + "\n");
    			}
    			bestMove = new MoveNode(String.copyValueOf(moveStr));
    			return;
    		}
    	}
    	
    	//5.迭代加深
    	if(depth==0)
    	{
    		return;
    	}
    	for(i=4;i<=depth;i++)
    	{
    		if(log.isDebugEnabled())
    		{
    			log.debug("info depth " + i + "\n");
    		}
    		subKillerTab.moveNum = 0;
    		thisValue = search(subKillerTab, -CCEvalue.MaxValue, CCEvalue.MaxValue, i);
    		popInfo(thisValue, depth);
    		if(stop)
    		{
    			break;
    		}
    		lastScore = thisValue;
    		
    		//6.stop thinking if timeout or solved
    		if(!ponder && System.currentTimeMillis()>minTimer)
    		{
    			break;
    		}
    		if(thisValue>CCEvalue.MaxValue-ActiveBoard.MAX_MOVE_NUM/2
    				|| thisValue<ActiveBoard.MAX_MOVE_NUM/2-CCEvalue.MaxValue)
    		{
    			break;
    		}
    	}
    	
    	//7.得到最佳着法及其线路
    	if(pvLineNum!=0)
    	{
    		moveStr = pvLine[0].location();
    		bestMove = new MoveNode(String.copyValueOf(moveStr));
    		if(log.isDebugEnabled())
    		{
    			log.debug("bestmove: " + String.copyValueOf(moveStr) + "\n");
    		}
    		if(pvLineNum>1)
    		{
    			moveStr = pvLine[1].location();
    			if(log.isDebugEnabled())
    			{
    				log.debug("ponder: "+String.copyValueOf(moveStr) + "\n");
    			}
    		}
    		else
    		{
    			if(log.isDebugEnabled())
    			{
    				//log.info("score: " + thisValue);----------------------------------here,该取消注释，但是要等isLoop
    			}
    		}
    	}
    	
    	//-----------------------------------------------------------------
    	
    	/*int depth = 2;
    	//MaxMin(4);
    	//System.out.println("before search");
    	AlphaBeta(depth, -1000, 1000);
    	//System.out.println("after search");
    	getBestMove(depth);
    	//System.out.println(bestMove.toString()+"    ----------");
    	activeBoard.makeMove(bestMove);*/
    }
//End Control Procedures
    
    public MoveNode getBestMove() throws LostException
    {
    	control();
    	return bestMove;
    }
    
    public void getBestMove(int depth)
    {
        control();
        
    	
    	/*int bestValue = -100000;
    	//System.out.println("============data in the hashList=================");
    	for(int i=0;i<recordIndex;i++)
    	{
    		HashRecord record = hashList[i];
    		if(record.depth!=depth)
    		{
    			continue;
    		}
    		//System.out.println(hashList[i].toString()+"@@@@@@@@@@@@@@@");
    		if(record.value>bestValue)
    		{
    			System.out.println("record.value: "+record.value+"--record.move: "+record.bestMove.toString());
    			bestValue = record.value;
    			bestMove = record.bestMove;
    		}
    	}*/
    }
    
    public void print()
    {
    	getBestMove(3);
    	System.out.println(bestMove.toString());    	
    }
    
    public void setActiveBoard(ActiveBoard activeBoard)
    {
    	this.activeBoard = activeBoard;
    }
    
    
    //------以下三个搜索方法为自己所写--------
    
    /**
     * 最大最小搜索
     * 
     * @param depth
     * @return
     */
    public int MaxMin(int depth)
    {    	
    	if(activeBoard.getPlayer()==1)
    	{    		
    		if(depth==0)
    		{
    			return activeBoard.evaluation();
    		}
    		SortedMove sortedMove = new SortedMove();
    		sortedMove.generateAllMoves(activeBoard, histTab);
    		int value;
    		int bestValue = -100000;
    		for(int i=0;i<sortedMove.moveNum;i++)
    		{
    			MoveNode thisMove = sortedMove.moveList[i];
    			if(!activeBoard.isLeagelMove(thisMove))
    			{
    				continue;
    			}
    			activeBoard.makeMove(thisMove);
    			value = MaxMin(depth-1);
    			activeBoard.undoMove();
    			if(value>bestValue)
    			{
    				bestValue = value;
    				bestMove = thisMove;
    				recordHash(bestValue, bestMove, depth);
    			}  			
    		}
    		return bestValue;
    	}
    	else
    	{    		
    		if(depth==0)
        	{
        		return -activeBoard.evaluation();
        	}
    		SortedMove sortedMove = new SortedMove();
    		sortedMove.generateAllMoves(activeBoard, histTab);
    		int value;
    		int bestValue = 100000;
    		for(int i=0;i<sortedMove.moveNum;i++)
    		{
    			if(!activeBoard.isLeagelMove(sortedMove.moveList[i]))
    			{
    				continue;
    			}
    			activeBoard.makeMove(sortedMove.moveList[i]);
    			value = MaxMin(depth-1);
    			activeBoard.undoMove();
    			if(value<bestValue)
    			{
    				bestValue = value;
    			}
    		}
    		return bestValue;
    	}   	
    }
    
    /**
     * 负值最大函数
     * 
     * @param depth
     * @return
     */
    public int NegativeMax(int depth)
    {
    	if(depth==0)
    	{
    		return activeBoard.evaluation();
    	}
        SortedMove sortedMove = new SortedMove();
    	sortedMove.generateAllMoves(activeBoard, histTab);
    	int value;
    	int bestValue = -1000;
    	for(int i=0;i<sortedMove.moveNum;i++)
    	{
    		MoveNode thisMove = sortedMove.moveList[i];
    		if(!activeBoard.isLeagelMove(thisMove))
    		{
    			continue;
    		}
    		activeBoard.makeMove(thisMove);
    		value = -NegativeMax(depth-1);
    		activeBoard.undoMove();
    			
    		if(value>bestValue)
    		{
    			bestValue = value;
    			bestMove = thisMove;
    		}
    	}
    	return bestValue;   	
    }
        
    /**
     * 自定义的alpha-beta搜索
     * 
     * @param depth
     * @param alpha
     * @param beta
     * @return
     */
    public int AlphaBeta(int depth, int alpha, int beta)
    {
    	if(activeBoard.getPlayer()==1)
    	{
    		if(depth==0)
    		{
    			return activeBoard.evaluation();
    		}
    		SortedMove sortedMove = new SortedMove();
    		sortedMove.generateAllMoves(activeBoard, histTab);
    		int value;
    		for(int i=0;i<sortedMove.moveNum;i++)
    		{
    			sortedMove.bubbleSort(i);
    			MoveNode thisMove = sortedMove.moveList[i];
    			
    			if(!activeBoard.isLeagelMove(thisMove))
    			{
    				continue;
    			}
    			
    			activeBoard.makeMove(thisMove);
    			value = AlphaBeta(depth-1, alpha, beta);
    			activeBoard.undoMove();
    			if(value>beta)
    			{
    				recordHash(beta, bestMove, depth);
    				return beta;
    			}
    			if(value>alpha)
    			{
    				alpha = value;
    				bestMove = thisMove;
    				recordHash(alpha, bestMove, depth);
    			}
    		}
    		return alpha;
    	}
    	else
    	{
    		if(depth==0)
    		{
    			return -activeBoard.evaluation();
    		}
    		SortedMove sortedMove = new SortedMove();
    		sortedMove.generateAllMoves(activeBoard, histTab);
    		int value;
    		for(int i=0;i<sortedMove.moveNum;i++)
    		{
    			sortedMove.bubbleSort(i);
    			MoveNode thisMove = sortedMove.moveList[i];
    			if(!activeBoard.isLeagelMove(thisMove))
    			{
    				continue;
    			}
    			activeBoard.makeMove(thisMove);
    			value = AlphaBeta(depth-1, alpha, beta);
    			activeBoard.undoMove();
    			if(value<alpha)
    			{
    				return alpha;
    			}
    			if(value<beta)
    			{
    				beta = value;
    			}
    		}
    		return beta;
    	}
    }
    
    
    
    
    /**************************************************************************
     * test
     *************************************************************************/
    public static void main(String[] args)
    {
    	ActiveBoard board = new ActiveBoard();
    	board.loadFen("rnbakabnr/9/1c5c1/p1p1p1p1p/9/9/P1P1P1P1P/1C5C1/9/RNBAKABNR w 0 1");
    	
    	//board.makeMove(new MoveNode(64, 1));
    	
    	SearchEngine searchEngine = new SearchEngine(board);
    	searchEngine.loadBook("./data/BOOK.txt");
    	//searchEngine.control();
    	MoveNode bestMove = null;
		try 
		{
			bestMove = searchEngine.getBestMove();
		} 
		catch (LostException e) 
		{
			e.printStackTrace();
		}
    	System.out.println("bestMove: "+bestMove.toString());
    	/*MoveNode move1 = new MoveNode(64,46);
		MoveNode move2 = new MoveNode(19,82);
		MoveNode move3 = new MoveNode(81,82);
		MoveNode move4 = new MoveNode(7,24);
		MoveNode move5 = new MoveNode(46,49);
		board.makeMove(move1);
		board.makeMove(move2);
		board.makeMove(move3);
		board.makeMove(move4);
		board.makeMove(move5);*/
    	
    	//MoveNode move01 = new MoveNode(64, 67);
		//MoveNode move02 = new MoveNode(25, 88);
		//MoveNode move03 = new MoveNode(89, 88);
		//board.makeMove(move01);
		//board.makeMove(move02);
		//board.makeMove(move03);
		//System.out.println("player: "+board.getPlayer());
    	
    	//System.out.println("best value 01: "+searchEngine.MaxMin(4));
    	//searchEngine.print();
    	
    	//System.out.println("best value 02: "+searchEngine.NegativeMax(3));
    	//searchEngine.print();
    	/*long start = System.currentTimeMillis();
    	System.out.println("best value 03: "+searchEngine.AlphaBeta(3, -1000, 1000));
    	searchEngine.print();
    	long end = System.currentTimeMillis();
    	long total = end - start;
    	System.out.println(total/1000);*/
    	
    	String tmpStr = "\n\n********************************************************************\n";
		//tmpStr = tmpStr + "[Test Time] "+c.getTime()+"\n";
		//tmpStr = tmpStr + "[Fen String] "+ FenStr + "\n"; 
		//tmpStr = tmpStr + "   Deep =" + steps+",Used Time:"+ minutes +":" + second%60 + "\n";
		tmpStr = tmpStr + "[Nodes] " + searchEngine.nodes+"\n";
		tmpStr = tmpStr + "[AlphaNodes] " + searchEngine.alphaNodes+"\n";
		tmpStr = tmpStr + "[BetaNodes] " + searchEngine.betaNodes+"\n";
		tmpStr = tmpStr + "[HashNodes] " + searchEngine.hashNodes+"\n";
		tmpStr = tmpStr + "[KillerNodes] " + searchEngine.killerNodes+"\n";
		tmpStr = tmpStr + "[LeafNodes] " + searchEngine.leafNodes+"\n";
		tmpStr = tmpStr + "[NullNodes] " + searchEngine.nullNodes+"\n";
		tmpStr = tmpStr + "[QuiescAlphaNodes] " + searchEngine.quiescAlphaNodes+"\n";
		tmpStr = tmpStr + "[QuiescBetaNodesNodes] " + searchEngine.quiescBetaNodes+"\n";
		tmpStr = tmpStr + "[QuiescMateNodes] " + searchEngine.quiescMateNodes+"\n";
		tmpStr = tmpStr + "[QuiescNullNodes] " + searchEngine.quiescNullNodes+"\n";
		tmpStr = tmpStr + "[QuiescPvNodes] " + searchEngine.quiescPvNodes+"\n";
		tmpStr = tmpStr + "[HitAlpha] " + searchEngine.hitAlpha+"\n";
		tmpStr = tmpStr + "[HitBeta] " + searchEngine.hitBeta+"\n";
		tmpStr = tmpStr + "[HitPv] " + searchEngine.hitPv+"\n";
		tmpStr = tmpStr + "[PvNodes] " + searchEngine.pvNodes+"\n";
		tmpStr = tmpStr + "[MateNodes] " + searchEngine.mateNodes+"\n";
		
		tmpStr = tmpStr + "[BetaNode] " + searchEngine.betaNodes+"\n";
		tmpStr = tmpStr + "[BPS] "+ searchEngine.nodes;
		//tmpStr = tmpStr + "[BPS] "+ searchEngine.nodes/seconds;
    }
}















