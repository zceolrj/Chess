package com.main;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.engine.ActiveBoard;
import com.engine.LostException;
import com.engine.MoveNode;
import com.engine.SearchEngine;
import com.engine.SortedMove;
import com.message.Consumer;
import com.message.Header;
import com.message.Message;
import com.message.MessageListener;
import com.message.MessageQueue;
import com.message.Producer;
import com.message.impl.ChessMessage;
import com.message.impl.ChessMessageQueue;
import com.message.impl.MessageConsumer;
import com.message.impl.MessageProducer;
import com.message.impl.PieceMessageDeliver;
import com.message.support.MsgNetConnection;
import com.message.support.OuterMsgReceiver;
import com.message.support.OuterMsgSender;
import com.pieces.PieceArray;
import com.pieces.PieceFactory;
import com.pieces.Qizi;
import com.rule.PlayerTimer;
import com.rule.TimeRule;
import com.rule.TimeRuleConfig;

public class ChessMainFrame extends JFrame
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 日志
	 */
	private static Log log = LogFactory.getLog(ChessMainFrame.class);
	
	/**
	 * 消息提示标签-------------------------
	 */
	private JLabel noteInfo;
	
	/**
	 * 系统设置-------------------------------- 
	 */
	private JButton button_setSysCfg;
	
	/**
	 * 重置------------------------------------
	 */
	private JButton button_resetAll;
	
	/**
	 * 倒转棋盘--------------------------------
	 */
	private JButton button_reverseBoard;
    
	/**
	 * 电脑移动的按钮-----------------------------
	 */
	private JButton button_cmpMove;
	
	/**
	 * 返回按钮------------------------------------
	 */
	private JButton button_turnBack;
	
	/**
	 * 保存文件按钮
	 */
	private JButton button_saveFile;
	
	/**
	 * 打开文件按钮
	 */
	private JButton button_openFile;
	
	/**
	 * 上一步按钮
	 */
	private JButton button_preStep;
	
	/**
	 * 下一步按钮
	 */
	private JButton button_nextStep;
	
	/**
	 * 最后一步按钮----------------------------------
	 */
	private JButton button_endStep;
	
	/**
	 * 选择文件按钮---------------------------打开文件用的？
	 */
	private JFileChooser fileChooserButton;
	
	/**
	 * 红方时间textfield
	 */
	private JTextField textField_redTime;
	
	/**
	 * 黑方时间textfield
	 */
	private JTextField textField_blackTime;
	
	/**
	 * 获取连接按钮-------------------------------
	 */
	private JButton button_getConnect;
	
	/**
	 * 设置规则按钮
	 */
	private JButton button_setRule;
	
	/**
	 * 显示规则按钮
	 */
	private JButton button_displayRule;
	
	/**
	 * 开始按钮
	 */
	private JButton button_start;
	
	
	//select red or black
	/**
	 * 
	 */
	JPanel panel0;
	
	/**
	 * 
	 */
	JPanel panel1;
	
	/**
	 * 
	 */
	JPanel panel2;
	
	/**
	 * 
	 */
	JPanel panel3;
	
	/**
	 * 
	 */
	JPanel panel4;
	
	/**
	 * 
	 */
	private Container content;
	
	/**
	 * 图画面板
	 */
	private PictureBoard pictureBoard;
	
	/**
	 * 玩家
	 */
	private int player = 0;
	
	/**
	 * 是否倒置了棋盘
	 */
	private boolean boardReversed = false;
	
	/**
	 * 电脑是否正在思考
	 */
	private boolean computerIsThinking = false;
	
	/**
	 * 时间规则
	 */
	private final TimeRule timeRule = new TimeRule();
	
	/**
	 * 时间规则设置
	 */
	private TimeRuleConfig timeRuleConfig = new TimeRuleConfig();
	
	/**
	 * 系统配置信息------------------------------------------
	 */
	private SysConfigInfo sysCfg = new SysConfigInfo();
	
	/**
	 * 是否可以开始游戏
	 */
	private ReadyToPlay readyToPlay;
	
	/**
	 * 竞争是否开始了？------------------------------------------
	 */
	private boolean competitorStarted = false;
	
	
	//computer
	
	/**
	 * 游戏是否开始了
	 */
	private boolean started = false;
	
	/**
	 * 是否是第一步
	 */
	private boolean isFirstStep = true;
	
	/**
	 * 竞争者姓名
	 */
	private String competitorName;
	
	/**
	 * 设置规则命令
	 */
	private static final String SET_RULE_COMMAND = "set time rule";
	
	/**
	 * 显示规则命令
	 */
	private static final String DISPLAY_RULE_COMMAND = "display time rule";
	
	/**
	 * 棋盘面板格子的大小？---------------------
	 */
	private int boardGridSize;
	
	/**
	 * -------------------------------------------？here
	 */
	private int view = 0;
	
	/**
	 * 线的位置？------------------------------？
	 */
	private int lineLoc;
	
	/**
	 * 动态局面
	 */
	private ActiveBoard activeBoard;
	
	/**
	 * 棋子数组
	 */
	private PieceArray pieceArr = new PieceArray();
	
	/**
	 * 90个棋子
	 */
	private Qizi pieceIndex[] = new Qizi[90];
	
	/**
	 * 被吃的棋子数组
	 */
	private PieceArray capturedArr = new PieceArray();
	
	/**
	 * 最后/上一步选择的棋子-----------------------
	 */
	private Qizi lastSelected;
	
	/**
	 * 所有的着法
	 */
	private SortedMove allMoves;
	
	/**
	 * -------------------------------------------------------------?
	 */
	private int[][] hisTable = new int[90][90];
	
	/**
	 * -------------------------------------------------------------？
	 */
	private Translation translation;
	
	/**
	 * 搜索引擎
	 */
	private SearchEngine searchEngine;
	
	
	//
	/*******************************************************************
	 * message processor--------------数据处理机
	 ********************************************************************/
	/**
	 * 象棋消息队列
	 */
	private MessageQueue msgQueue = new ChessMessageQueue();
	
	/**
	 * 消息生产者
	 */
	private Producer producer = new MessageProducer(msgQueue);
	
	/**
	 * 消息消费者
	 */
	private Consumer consumer = new MessageConsumer();
	
	/**
	 * 消息传递者
	 */
	private PieceMessageDeliver deliver = new PieceMessageDeliver(msgQueue);
	
	/**
	 * 本地消息监听器
	 */
	private MessageListener localListener = new LocalMessageListener();
	
	/**
	 * 远程消息监听器
	 */
	private MessageListener remoteListener = new RemoteMessageListener();
	
	/**
	 * 消息网络连接
	 */
	private MsgNetConnection netConnection = new MsgNetConnection();
	
	/**
	 * 外部消息发送器
	 */
	private OuterMsgSender outSender = new OuterMsgSender(netConnection);
	
	/**
	 * 外部消息接收器
	 */
	private OuterMsgReceiver outReceiver = new OuterMsgReceiver(producer, netConnection);
	
	
	/*******************************************************************
	 * button listeners
	 ************************************************************/
	/**
	 * 上一步、下一步、返回按钮监听器
	 */
	private LastNextTurnBackButtonListener lnt = new LastNextTurnBackButtonListener();
	
	/**
	 * 打开、保存按钮监听器
	 */
	private OpenSaveButtonListener fhl = new OpenSaveButtonListener();
	
	/**
	 * 连接监听器
	 */
	private ConnectActionListener connectListener = new ConnectActionListener();
	
	/**
	 * 系统信息按钮监听器
	 */
	private SysInfoButtonsListener sral = new SysInfoButtonsListener();
	
	/**
	 * 构造函数
	 */
	public ChessMainFrame()
	{
		super("Chess_Java");
		
		//初始化
		initialize();
		
		//创建图形界面
		createGui();
	}
	
	/**
	 * 
	 * 
	 * @param bs
	 * @param view
	 * @param appname
	 */
	public ChessMainFrame(int bs, int view, String appname)
	{
		super(appname);
		setBoardGridSize(bs);
		setView(view);
		initialize();
		createGui();
	}
	
	/**
	 * 初始化
	 */
	private void initialize()
	{
		JPanel jContentPane = new JPanel();
		jContentPane.setLayout(null);
		this.setContentPane(jContentPane);
		
		for(int k=0;k<90;k++)//初始化历史表
		{
			for(int j=0;j<90;j++)
			{
				hisTable[k][j] = 0;
			}
		}
		
		setBoardGridSize(50);
		
		this.getActiveBoard();
		
		this.getSearchEngine();
		
		this.initActiveBoard();
		
		this.getTranslation();
		
		this.getPictureBoard();
		
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
		
		//时间规则配置
		timeRuleConfig = new TimeRuleConfig();
		timeRule.setProducer(producer);
		timeRule.setPlayerTimer(new TimeCounter());
		
		//系统信息配置
		sysCfg = new SysConfigInfo();
		
		readyToPlay = new ReadyToPlay();
		
		allMoves = new SortedMove();
		
		//棋子消息传递者注册一个消费者
		deliver.registerAConsumer(consumer);
		//消费者注册两个监听器
		consumer.registerAListener(localListener);
		consumer.registerAListener(remoteListener);
		
	    outReceiver.setProducer(producer);
		
		//棋子消息传递者线程启动--该线程不断地在消息队列中获取一条小心,然后传递该消息
		deliver.start();
	}
	
	/**
	 * 创建图形界面
	 */
	public void createGui()
	{
		JDialog.setDefaultLookAndFeelDecorated(true);
		content = this.getContentPane();
		
		//文件选择按钮
		fileChooserButton = new JFileChooser();
		//设置文件选择按钮的默认目录
		fileChooserButton.setCurrentDirectory(new File("./save"));
		
		//设置主窗体的尺寸大小
		this.setSize(new Dimension(getPictureBoard().getWidth()+140,
				getPictureBoard().getHeight()+80));
		this.setLocation(SCREEN.getLocationForCenter(getSize()));
		
		getNoteInfo().setBounds(10, pictureBoard.getHeight()+15,
				pictureBoard.getWidth()-20, 25);
		
		getPanel0().setLocation(pictureBoard.getWidth()+15,5);
		getPanel1().setLocation(pictureBoard.getWidth()+15, 135);
		getPanel2().setLocation(pictureBoard.getWidth()+15, 203);
		getPanel3().setLocation(pictureBoard.getWidth()+15, 273);
		getPanel4().setLocation(pictureBoard.getWidth()+15, 400);
		
		content.add(noteInfo);
		content.add(getPanel0());
		content.add(getPanel1());
		content.add(getPanel2());
		content.add(getPanel3());
		content.add(getPanel4());
		content.add(getPictureBoard());
		
		//初始化图画面板和所有的棋子
		initPictureBoardAndPieces();
		
		//显示所有的棋子
		displayAllQizi();
	}
//end of createGui
	
	/**
	 * 初始化动态局面
	 */
	public void initActiveBoard()
	{
		//加载fen字符串
		getActiveBoard().loadFen("rnbakabnr/9/1c5c1/p1p1p1p1p/9/9/P1P1P1P1P/1C5C1/9/RNBAKABNR w 0 1");
		
		//
		getTranslation().setFenStr("");
		
		//loadBook("./data/book.txt");//------------------------------------------------------------------here
		
		player = 0;
	}
	
	/**
	 * 初始化动态局面
	 * 
	 * @param fenStr FEN字符串
	 */
	public void initActiveBoard(String fenStr)
	{
		//加载FEN字符串
		getActiveBoard().loadFen(fenStr);
		player = activeBoard.getPlayer();
	}
	
	/******************************************************************
	 * add all picture pieces to content
	 ******************************************************************/
	/**
	 * 初始化图画面板和棋子
	 */
	public void initPictureBoardAndPieces()
	{
		//初始化棋子
		initPieces();
		
		//面板加上所有的棋子和图画面板
		for(int i=0;i<pieceArr.size();i++)
		{
			content.add(pieceArr.getPiece(i));
		}
		content.add(pictureBoard);
	}
	
	/**
	 * 显示所有的棋子
	 */
	public void displayAllQizi()
	{
		for(int i=0;i<pieceIndex.length;i++)
		{
			displayAQizi(pieceIndex[i]);
		}
	}
	
	/**
	 * 显示单个棋子
	 * 
	 * @param qz 棋子
	 */
	public void displayAQizi(Qizi qz)
	{
		//int x0;
		//int y0;
		if(qz==null)
		{
			return;
		}
		//设置棋子位置并显示
		setQzLocation(qz);
		qz.setVisible(true);
	}
	
	/**
	 * 设置棋子位置
	 * 
	 * @param qz 棋子
	 */
	public void setQzLocation(Qizi qz)
	{
		int x0;
		int y0;
		//获得棋子的x和y坐标
		x0 = qz.getCoordinateX();
		y0 = qz.getCoordinateY();
		
		y0 = 9 - y0;//---------------------------------------new code
		
		//如果棋盘倒置了需要修改坐标--------------------------------here
		if(boardReversed)
		{
			x0 = 8 - x0;
			y0 = 9 - y0;
		}
		
		//------------------------------------------------------------here
		qz.setLocation((int)(lineLoc + (x0-0.45)*boardGridSize), 
				(int)(lineLoc+(9-y0-0.45)*boardGridSize));
	}
	
	/**
	 * 初始化棋子
	 */
	private void initPieces()
	{
		//设置棋子尺寸大小
		PieceFactory.setPieceSize((int)(getBoardGridSize()*0.9));
		
		//获取fen字符串
		String s = activeBoard.getFenStr();
		
		Qizi tmpQizi;
		int row = 0;
		int col = 0;
		for(int i=0;i<90;i++)
		{
			pieceIndex[i] = null;
		}
		
		//棋子的鼠标监听器
		PieceMouseListener PML = new PieceMouseListener();
		
		for(int i=0;i<s.length()&&row<=9;)
		{
			//
			char tmpChar = s.charAt(i);
			if(tmpChar>='1' && tmpChar<='9')
			{
				i++;
				col += tmpChar - '0';
			}
			if(s.charAt(i)=='/' || s.charAt(i)==' ')
			{
				row++;
				col = 0;
			}
			else
			{
				//从棋子工厂获取棋子并设置鼠标监听器
				tmpQizi = PieceFactory.getAPiece(s.charAt(i), col, row);
				tmpQizi.addMouseListener(PML);
				
				//棋子数组中加上该棋子
				pieceArr.add(tmpQizi);
				pieceIndex[9*row+col] = pieceArr.getPiece(pieceArr.size()-1);
				col++;
			}
			i++;
		}
	}
	
	/**
	 * @param book
	 */
	public void loadBook(String book)
	{
		getSearchEngine().clearHash();
		getSearchEngine().clearHistTab();
		getSearchEngine().loadBook(book);
	}
	
	/**
	 * @param newBoard
	 */
	public void reloadPreActiveBoard(ActiveBoard newBoard)
	{
		
	}
	
	/**
	 * 
	 */
	public void reverseBoard()
	{
		
	}
	
	
	/**
	 * undo -- 撤销
	 * 
	 * @return true/false
	 */
	public boolean undo()
	{
		//待续
		return true;
	}
	
	/**
	 * redo -- 重做
	 * 
	 * @return true/false
	 */
	public boolean redo()
	{
		//待续
		return false;
	}
	
	/**
	 * 电脑走棋
	 */
	private void computerMove()
	{
		if(button_cmpMove.getText()=="Computer")//如果按钮的文本为"电脑"
		{
			button_cmpMove.setText("Stop");
			
			new Thread()
			{
				public void run()
				{
					computerIsThinking = true;
					outputInfo("Computer is thinking!");
					
					//ActiveBoard tmpActive = (ActiveBoard)ObjectCopyer.getACopy(activeBoard);
			        searchEngine.setActiveBoard(activeBoard);
			        MoveNode bestMove = null;
			        try
			        {
			            bestMove = searchEngine.getBestMove();
			        }
			        catch(LostException e)
			        {
			        	e.printStackTrace();
			        }
			        button_cmpMove.setText("Computer");
			        if(bestMove==null)
			        {
			        	outputInfo("Computer cannot find best Move!!!");
			        	button_cmpMove.setEnabled(true);
			        	return;
			        }
			        
			        movePiece(bestMove.src, bestMove.dst, true);
			        
			        //电脑移动,设置参数为false
			        afterMoved(false);
			        
			        outputInfo("computer moved: "+String.copyValueOf(bestMove.location()));
			        computerIsThinking = false;
				}
			}.start();
		}
		else//此时电脑移动按钮的文本为"停止"
		{
			searchEngine.stopSearch();
			button_cmpMove.setText("Computer");
		}
	}
	
	/**
	 * 移动棋子  -- for remoteMove,computer Move or redo or undo
	 * 
	 * @param src 
	 * @param dst
	 * @param needSave
	 */
	public void movePiece(int src, int dst, boolean needSave)
	{
		Qizi qz = pieceIndex[src];
		
		//int dstX = dst/10;//------------------original code
		//int dstY = dst%10;//------------------original code
		
		int dstX = dst%9;
		int dstY = dst/9;
		if(qz==null)
		{
			return;
		}
		if(lastSelected!=null)
		{
			lastSelected.setBorderPainted(false);
		}
		if(movePiece(qz, dstX, dstY, needSave))
		{
			//if true, model data and other information are saved,
			//following is to alter the view
			changeLocation(qz, dstX, dstY);
			lastSelected = qz;
			timeRule.updateTotalTime(player);
			player = 1 - player;
		}
	}
	
	/**
	 * 移动棋子  --  for mouseAction
	 * 
	 * @param qz
	 * @param dstX
	 * @param dstY
	 * @param needSave
	 * @return
	 */
	public boolean movePiece(Qizi qz, int dstX, int dstY, boolean needSave)
	{
		if(qz==null)
		{
			return false;
		}
		
		int srcX = qz.getCoordinateX();
		int srcY = qz.getCoordinateY();
		allMoves.generateAllMoves(activeBoard, hisTable);
		MoveNode tmpMove = null;
		int tmpSrc;
		int tmpDst;
		int moveNum = allMoves.moveNum;
		for(int i=0;i<moveNum;i++)
		{
			tmpSrc = allMoves.moveList[i].src;
			tmpDst = allMoves.moveList[i].dst;
			
			/*if(tmpSrc==srcX*10+srcY && tmpDst == dstX*10+dstY)//-------original code-------here?
			{
				tmpMove = new MoveNode(tmpSrc, tmpDst);
				if(activeBoard.movePiece(tmpMove))
				{
					if(needSave)
					{
						
					}
					if(isFirstStep)
					{
						timeRule.resetTimeAndBeginCount();
						isFirstStep = false;
					}
					return true;
				}
				else
				{
					return false;
				}
			}*/
			
			if(tmpSrc==srcY*9+srcX && tmpDst==dstY*9+dstX)//---------new code
			{
				tmpMove = new MoveNode(tmpSrc, tmpDst);
				System.out.println(tmpMove.toString());
				if(activeBoard.movePiece(tmpMove))
				{
					if(needSave)
					{
						
					}
					if(isFirstStep)
					{
						//这里开始计时----------------important
						//这里开始计时,不过似乎不对，应该从开始的那一刻起，先走棋的一方就该计时了
						timeRule.resetTimeAndBeginCount();
						isFirstStep = false;
					}
					return true;
				}
				else
				{
					return false;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * 该方法很重要,包含了网络对战中双方互相发送信息,主要就是通过该方法
	 * 
	 * @param mouseAction
	 */
	public void afterMoved(boolean mouseAction)
	{
		//如果是鼠标点击事件,则需要将着法移动信息等发送给对方,如果是电脑移动则不需要发送
		if(mouseAction)
		{
			int src = activeBoard.lastMove().src;
			int dst = activeBoard.lastMove().dst;
			
			//这里发送的是本地消息.消息头是PIECE_MOVED,注意这个消息
			//最终会被传送到LocalMessageListener中处理,RemoteMessageListener会忽略此消息
			producer.send(new ChessMessage(Header.PIECE_MOVED,
					src + "-" + dst, true));
		}
		decideFailer();//每走一步之后需要决定胜负
	}
	
	/**
	 * 决定胜负
	 */
	public void decideFailer()
	{
		allMoves.generateAllMoves(activeBoard, hisTable);
		MoveNode thisMove;
		for(int i=0;i<allMoves.moveNum;i++)
		{
			thisMove = allMoves.moveList[i];
			if(activeBoard.movePiece(thisMove))//如果能够移动着法,说明没有失败,直接返回
			{
				activeBoard.undoMove();
				return;
			}
		}
		//如果红方失败,则发送红方失败消息
		if(activeBoard.getPlayer()==0)
		{
			producer.send(new ChessMessage(Header.RED_FAILED, null, true));
		}
		else//黑方失败
		{
			producer.send(new ChessMessage(Header.BLACK_FAILED, null, true));
		}
	}
	
	public void changeLocation(Qizi srcQz, int dstX, int dstY)
	{
		//if dst has a qizi, move it to captured place[dst][1] and set to
		//visible to false
		
		//int src = srcQz.getCoordinate();-------原有代码
		//int dst = dstX*10 + dstY;-----------原有代码
		
		int src = srcQz.getCoordinates();//-------------new code
		int dst = dstY*9+dstX;//------------------------new code
		
		//如果目标位置有棋子，将其移入被吃棋子数组，并将目标位置的棋子设为不可见的
		if(pieceIndex[dst]!=null)
		{
			pieceIndex[dst].setVisible(false);
			capturedArr.add(pieceIndex[dst]);
			pieceIndex[dst] = null;
		}
		//将源棋子赋给目标位置的棋子
		pieceIndex[dst] = srcQz;
		pieceIndex[dst].setCoordinate(dstX, dstY);
		setQzLocation(pieceIndex[dst]);
		pieceIndex[dst].setBorderPainted(true);
		pieceIndex[src] = null;
	}
	
	/**
	 * 忽略鼠标事件
	 * 
	 * @param eventSource 特定的鼠标事件
	 * @return
	 */
	public boolean ignoreMouseAction(Object eventSource)
	{
		//如果电脑正在思考且点击事件不是电脑移动，则忽略此次点击事件
		if(computerIsThinking && eventSource!=button_cmpMove)
		{
			outputInfo("cannot take any action,computer is thinking");
			return true;
		}
		//如果比赛已经开始且点击事件为开始、规则设置、系统设置、获取连接等其中的一个
		//则忽略此次点击事件
		if(started)
		{
			if(eventSource==button_start||eventSource==button_setRule
					||eventSource==button_setSysCfg
					||eventSource==button_getConnect)
			{
				return true;
			}
		}
		//如果点击了棋子但是比赛还没有开始，那么忽略此次点击事件
		if(eventSource instanceof Qizi && !started)
		{
			return true;
		}
		if(sysCfg.getBattleModel()==2)//network--联网对战
		{
			//如果在网络对战的模式下点击电脑移动、最后一步、前一步、重置所有、
			//返回、下一步等按钮，则忽略此次点击事件
			if(eventSource==button_cmpMove || eventSource==button_endStep
					|| eventSource==button_preStep
					|| eventSource==button_resetAll
					|| eventSource==button_turnBack
					|| eventSource==button_nextStep)
			{
				return true;
			}
			if(eventSource instanceof Qizi)
			{
				Qizi tmp = (Qizi)eventSource;
				int rb1 = (tmp.getPieceType()<7?0:1);
				if(player!=sysCfg.getSelectedRb())
				{
					return true;
				}
				if(rb1!=sysCfg.getSelectedRb())
				{
					if(lastSelected==null)
					{
						return true;
					}
				}
			}
			if(eventSource==pictureBoard)
			{
				if(lastSelected!=null)
				{
					int rb2 = (lastSelected.getPieceType()<7?0:1);
					if(rb2!=sysCfg.getSelectedRb())
					{
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * 显示出消息提示
	 * 
	 * @param str 消息
	 */
	private void outputInfo(String str)
	{
		noteInfo.setText(str);
	}
	
	/**
	 * 设置时间规则--这里主要是创建并显示设置时间规则的对话框
	 * 
	 * @param trc
	 */
	private void setTimeRule(TimeRuleConfig trc)
	{
		SetRuleDialog.createAndDisplay(this, trc, true, null);
	}
	
	/**
	 * 
	 * 
	 * @param scfi
	 */
	private void setSysCfgInfo(SysConfigInfo scfi)
	{
		SetSysCfgDialog.createAndDisplay(this, scfi, true, null);
	}
	
	/**
	 * 
	 * 
	 * @param trc
	 * @param title
	 */
	private void displayTimeRule(TimeRuleConfig trc, String title)
	{
		SetRuleDialog.createAndDisplay(this, trc, false, title);
	}
	
	/**
	 * 
	 * 
	 * @param showmsg
	 * @return
	 */
	private int showMessageBox(String showmsg)
	{
		return JOptionPane.showConfirmDialog(ChessMainFrame.this, showmsg,
				"Information", JOptionPane.YES_NO_OPTION);
	}
	
	/**
	 * 
	 */
	private void resetAll()
	{
		
	}
	
	/**
	 * 打开文件
	 */
	private void openFile()
	{
		
	}
	
	/**
	 * 保存文件
	 */
	private void saveFile()
	{
		
	}
	
	
	/*******************************************************************
	 * following is getters and setters
	 *******************************************************************/
	public int getBoardGridSize()
	{
		return boardGridSize;
	}
	
	public void setBoardGridSize(int boardGridSize)
	{
		this.boardGridSize = boardGridSize;
		lineLoc = boardGridSize*2/3;
	}
	
	public int getView() {
		return view;
	}

	public void setView(int view) {
		this.view = view;
	}
	
	/**
	 * 获取图画面板
	 * 
	 * @return
	 */
	public PictureBoard getPictureBoard()
	{
		if(pictureBoard==null)
		{
			pictureBoard = new PictureBoard();
			pictureBoard.setBoardGridSize(boardGridSize);
			pictureBoard.setBounds(0, 0, boardGridSize*28/3, boardGridSize*32/3);
			pictureBoard.setOpaque(false);
			pictureBoard.addMouseListener(new PictureBoardMouseListener());
		}
		return pictureBoard;
	}
	
	public void setPictureBoard(PictureBoard pictureBoard)
	{
		this.pictureBoard = pictureBoard;
	}
	
	/**
	 * 获取动态局面
	 * 
	 * @return activeBoard
	 */
	public ActiveBoard getActiveBoard()
	{
		if(activeBoard==null)
		{
			activeBoard = new ActiveBoard();
		}
		return activeBoard;
	}
	
	/**
	 * 获取搜索引擎
	 * 
	 * @return searchEngine
	 */
	public SearchEngine getSearchEngine()
	{
		if(searchEngine==null)
		{
			searchEngine = new SearchEngine();
			searchEngine.setupControl(6, SearchEngine.CLOCK_S*20, 
                            SearchEngine.CLOCK_M*10);
		}
		return searchEngine;
	}
	
	public Translation getTranslation()
	{
		if(translation==null)
		{
			translation = new Translation();
		}
		return translation;
	}
	
	public JLabel getNoteInfo()
	{
		if(noteInfo==null)
		{
			noteInfo = new JLabel("");
			noteInfo.setForeground(Color.RED);
			noteInfo.setFont(getFont());
		}
		return noteInfo;
	}
	
	/**
	 * 包含返回、保存、打开、上一步、最后一步、下一步按钮
	 * 
	 * @return
	 */
	public JPanel getPanel0()
	{
		if(panel0==null)
		{
			panel0 = new JPanel();
			panel0.setSize(100, 125);
			panel0.setLayout(null);
			panel0.setBorder(BorderFactory.createEtchedBorder());
			
			getButton_turnBack().setLocation(5, 5);
			getButton_saveFile().setLocation(5, 35);
			getButton_openFile().setLocation(5, 65);
			getButton_preStep().setLocation(5, 95);
			getButton_endStep().setLocation(35, 95);
			getButton_nextStep().setLocation(70, 95);
			
			panel0.add(button_turnBack);
			panel0.add(button_saveFile);
			panel0.add(button_openFile);
			panel0.add(button_preStep);
			panel0.add(button_endStep);
			panel0.add(button_nextStep);
		}
		return panel0;
	}
	
	/**
	 * panel1包含系统信息和重置所有两个按钮
	 * 
	 * @return
	 */
	public JPanel getPanel1()
	{
		if(panel1==null)
		{
			panel1 = new JPanel();
			panel1.setLayout(null);
			panel1.setSize(100, 65);
			panel1.setBorder(BorderFactory.createEtchedBorder());
			
			getButton_setSysCfg().setLocation(5, 5);
			getButton_resetAll().setLocation(5, 35);
			
			panel1.add(button_setSysCfg);
			panel1.add(button_resetAll);
		}
		return panel1;
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public JPanel getPanel2()
	{
		if(panel2==null)
		{
			panel2 = new JPanel();
			panel2.setLayout(null);
			panel2.setSize(100, 65);
			panel2.setBorder(BorderFactory.createEtchedBorder());
			
			getButton_reverseBoard().setLocation(5, 5);
			getButton_cmpMove().setLocation(5, 35);
			
			panel2.add(button_reverseBoard);
			panel2.add(button_cmpMove);
		}
		return panel2;
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public JPanel getPanel3()
	{
		if(panel3==null)
		{
			panel3 = new JPanel();
			panel3.setBorder(BorderFactory.createEtchedBorder());
			panel3.setLayout(null);
			panel3.setSize(100, 125);
			
			getButton_getConnect().setLocation(5, 5);
			getButton_setRule().setLocation(5, 35);
			getButton_displayRule().setLocation(5, 65);
			getButton_start().setLocation(5, 95);
			
			panel3.add(button_getConnect);
			panel3.add(button_setRule);
			panel3.add(button_displayRule);
			panel3.add(button_start);
		}
		return panel3;
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public JPanel getPanel4()
	{
		if(panel4==null)
		{
			panel4 = new JPanel();
			panel4.setBorder(BorderFactory.createEtchedBorder());
			panel4.setLayout(null);
			panel4.setSize(100, 65);
			
			textField_redTime = new JTextField("redTime");
			textField_blackTime = new JTextField("blackTime");
			textField_redTime.setEditable(false);
			textField_blackTime.setEditable(false);
			textField_redTime.setSize(90, 25);
			textField_blackTime.setSize(90, 25);
			textField_redTime.setLocation(5, 5);
			textField_blackTime.setLocation(5, 35);
			
			panel4.add(textField_redTime);
			panel4.add(textField_blackTime);
		}
		return panel4;
	}
	
	/**
	 * 获取电脑移动按钮
	 * 
	 * @return
	 */
	public JButton getButton_cmpMove()
	{
		if(button_cmpMove==null)
		{
			button_cmpMove = new JButton("Computer");
			button_cmpMove.setSize(90, 25);
			button_cmpMove.setBorder(BorderFactory.createRaisedBevelBorder());
			button_cmpMove.addActionListener(lnt);
		}
		return button_cmpMove;
	}
	
	/**
	 * 获取显示规则按钮
	 * 
	 * @return
	 */
	public JButton getButton_displayRule()
	{
		if(button_displayRule==null)
		{
			button_displayRule = new JButton("displayRule");
			button_displayRule.setSize(90, 25);
			button_displayRule.setMargin(new Insets(1, 1, 1, 1));
			button_displayRule.setBorder(BorderFactory
					.createRaisedBevelBorder());
			button_displayRule.setActionCommand(DISPLAY_RULE_COMMAND);
			button_displayRule.addActionListener(sral);
		}
		return button_displayRule;
	}
	
	/**
	 * 获取连接按钮
	 * 
	 * @return
	 */
	public JButton getButton_getConnect()
	{
		if(button_getConnect==null)
		{
			button_getConnect = new JButton("connect");
			button_getConnect.setSize(90, 25);
			button_getConnect.setEnabled(false);
			button_getConnect
					.setBorder(BorderFactory.createRaisedBevelBorder());
			button_getConnect.addActionListener(connectListener);
		}
		return button_getConnect;
	}
	
	/**
	 * 获取打开文件按钮
	 * 
	 * @return
	 */
	public JButton getButton_openFile()
	{
		if(button_openFile==null)
		{
			button_openFile = new JButton("Read");
			button_openFile.setSize(90, 25);
			button_openFile.setMnemonic(KeyEvent.VK_R);
			button_openFile.setBorder(BorderFactory.createRaisedBevelBorder());
			button_openFile.addActionListener(fhl);
		}
		return button_openFile;
	}
	
	/**
	 * 获取重置所有按钮
	 * 
	 * @return
	 */
	public JButton getButton_resetAll()
	{
		if(button_resetAll==null)
		{
			button_resetAll = new JButton("resetAll");
			button_resetAll.setSize(90, 25);
			button_resetAll.setBorder(BorderFactory.createRaisedBevelBorder());
			button_resetAll.addActionListener(lnt);
		}
		return button_resetAll;
	}
	
	/**
	 * 获取反转棋盘按钮
	 * 
	 * @return
	 */
	public JButton getButton_reverseBoard()
	{
		if(button_reverseBoard==null)
		{
			button_reverseBoard = new JButton("Reverse");
			button_reverseBoard.setSize(90, 25);
			button_reverseBoard.setBorder(BorderFactory.createRaisedBevelBorder());
			button_reverseBoard.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					reverseBoard();
				}
			});
		}
		return button_reverseBoard;
	}
	
	/**
	 * 获取保存文件按钮
	 * 
	 * @return
	 */
	public JButton getButton_saveFile()
	{
		if(button_saveFile==null)
		{
			button_saveFile = new JButton("Save");
			button_saveFile.setSize(90, 25);
			button_saveFile.setMnemonic(KeyEvent.VK_S);
			button_saveFile.setBorder(BorderFactory.createRaisedBevelBorder());
			button_saveFile.addActionListener(fhl);
		}
		return button_saveFile;
	}
	
	/**
	 * 获取设置规则按钮
	 * 
	 * @return
	 */
	public JButton getButton_setRule()
	{
		if(button_setRule==null)
		{
			button_setRule = new JButton("set rule");
			button_setRule.setSize(90, 25);
			button_setRule.setBorder(BorderFactory.createRaisedBevelBorder());
			button_setRule.setActionCommand(SET_RULE_COMMAND);
			button_setRule.addActionListener(sral);
		}
		return button_setRule;
	}
	
	/**
	 * 获取设置系统信息按钮
	 * 
	 * @return
	 */
	public JButton getButton_setSysCfg()
	{
		if(button_setSysCfg==null)
		{
			button_setSysCfg = new JButton("Setting");
			button_setSysCfg.setSize(90, 25);
			button_setSysCfg.setBorder(BorderFactory.createRaisedBevelBorder());
			button_setSysCfg.addActionListener(sral);
		}
		return button_setSysCfg;
	}
	
	/**
	 * 获取开始按钮
	 * 
	 * @return
	 */
	public JButton getButton_start()
	{
		if(button_start==null)
		{
			button_start = new JButton("start");
			button_start.setActionCommand("start");
			button_start.setSize(90, 25);
			button_start.setBorder(BorderFactory.createRaisedBevelBorder());
			button_start.addActionListener(sral);
		}
		return button_start;
	}
	
	/**
	 * 获取返回按钮
	 * 
	 * @return
	 */
	public JButton getButton_turnBack()
	{
		if(button_turnBack==null)
		{
			button_turnBack = new JButton("back");//设置返回按钮的内容
			button_turnBack.setSize(90, 25);//设置按钮尺寸
			button_turnBack.setMnemonic(KeyEvent.VK_B);
			button_turnBack.setBorder(BorderFactory.createRaisedBevelBorder());
			button_turnBack.addActionListener(lnt);//设置按钮的事件监听
		}
		return button_turnBack;
	}
	
	/**
	 * 获取最后一步按钮
	 * 
	 * @return
	 */
	public JButton getButton_endStep()
	{
		if(button_endStep==null)
		{
			Image endImage = Toolkit.getDefaultToolkit().getImage(
					 "./image/end.gif");
			Image endImage1 = Toolkit.getDefaultToolkit().getImage(
					 "./image/end1.gif");
			button_endStep = new JButton();
			button_endStep.setSize(21, 21);
			button_endStep.setIcon(new ImageIcon(endImage));
			button_endStep.setBorder(BorderFactory.createRaisedBevelBorder());
			button_endStep.setPressedIcon(new ImageIcon(endImage1));
			button_endStep.addActionListener(lnt);
		}
		return button_endStep;
	}
	
	/**
	 * 获取下一步按钮
	 * 
	 * @return
	 */
	public JButton getButton_nextStep()
	{
		if(button_nextStep==null)
		{
			Image rightImage = Toolkit.getDefaultToolkit().getImage(
					"./image/right.gif");
			Image rightImage1 = Toolkit.getDefaultToolkit().getImage(
					"./image/right1.gif");
			button_nextStep = new JButton();
			button_nextStep.setSize(21, 21);
			button_nextStep.setIcon(new ImageIcon(rightImage));
			button_nextStep.setBorder(BorderFactory.createRaisedBevelBorder());
			button_nextStep.setPressedIcon(new ImageIcon(rightImage1));
			button_nextStep.addActionListener(lnt);
		}
		return button_nextStep;
	}
	
	/**
	 * 获取上一步按钮
	 * 
	 * @return
	 */
	public JButton getButton_preStep()
	{
		if(button_preStep==null)
		{
			Image leftImage = Toolkit.getDefaultToolkit().getImage(
					"./image/left.gif");
			Image leftImage1 = Toolkit.getDefaultToolkit().getImage(
					"./image/left1.gif");
			button_preStep = new JButton();
			button_preStep.setSize(21, 21);
			button_preStep.setIcon(new ImageIcon(leftImage));
			button_preStep.setBorder(BorderFactory.createRaisedBevelBorder());
			button_preStep.setPressedIcon(new ImageIcon(leftImage1));
			button_preStep.addActionListener(lnt);
		}
		return button_preStep;
	}
	
	
	
	//----------------------------------------------------------
	
	/**
	 * 本地消息监听器
	 * 
	 * @author soft
	 *
	 */
	private class LocalMessageListener implements MessageListener
	{
		public void onMessage(Message msg)
		{
			if(!msg.isLocalMessage())
			{
				return;
			}
			Header head = msg.getMessageHeader();
			Object body = msg.getMessageBody();
			if(head.equals(Header.CHART))
			{
				//sendout
			}
			else if(head.equals(Header.PIECE_MOVED))//如果消息头是PIECE_MOVED
			{
				if(outSender.available())
				{
					//如果外部发送器可用,则将该消息当成远程消息发送给客户端或者服务器
					outSender.send(new ChessMessage(head, body, false));
				}
			}
			else if(head.equals(Header.RED_TIME_USED)
					||head.equals(Header.RED_FAILED))
			{
				System.err.println("RED LOST!!!!!!!!!!!!!");
				int n = showMessageBox("");
				if(n==JOptionPane.YES_OPTION)
				{
					saveFile();
				}
				resetAll();
				//button_openFile.getActionListeners()[0].actionPerformed(new
				//ActionEvent(button_openFile,12324,"ffff"));
			}
			else if(head.equals(Header.BLACK_TIME_USED)
					||head.equals(Header.BLACK_FAILED))
			{
				System.err.println("BLACK LOST!!!!!!!!!!!!!!!!");
				int n = showMessageBox("");
				if(n==JOptionPane.YES_OPTION)
				{
					saveFile();
				}
				resetAll();
			}
		}
	}
	
	/**
	 * 客户端和服务器之间消息的传递--------------------------------------------here？
	 * 监听器
	 * 
	 * @author soft
	 *
	 */
	private class RemoteMessageListener implements MessageListener
	{
		public void onMessage(Message msg)
		{
			if(msg.isLocalMessage())//如果是本地消息,则直接返回
			{
				return;
			}
			if(!outSender.available())//如果外部消息发送器不可用,则返回
			{
				return;
			}
			
			Header head = msg.getMessageHeader();
			Object body = msg.getMessageBody();
			if(head.equals(Header.SET_TIME_RULE))//消息头为设置时间规则
			{
				System.err.println("received time rule from competitor!");
				displayTimeRule((TimeRuleConfig)body, "");
				int n = showMessageBox("Do you agree the time rule?");
				if(n==JOptionPane.YES_OPTION)//同意该时间规则
				{
					timeRuleConfig = (TimeRuleConfig)body;//消息体中获取时间规则的配置
					timeRule.resetTimeRule(timeRuleConfig);
					readyToPlay.setTimeRuleReady(true);
					outSender.send(new ChessMessage(Header.AGREE, null, false));
				}
				else//不同意该时间规则
				{
					System.err.println("send a message of disagree on time rule from competitor!");
					outSender.send(new ChessMessage(Header.DISAGREE, null, false));
				}
			}
			else if(head.equals(Header.AGREE))//消息头为同意
			{
				readyToPlay.setTimeRuleReady(true);
				timeRule.resetTimeRule(timeRuleConfig);
				showMessageBox("Competitor agreed!!!");
			}
			else if(head.equals(Header.DISAGREE))//消息头为不同意
			{
				showMessageBox("Competitor disagree!!!!!");
			}
			else if(head.equals(Header.SYSINFO))//消息头为系统配置信息
			{
				competitorStarted = true;
				readyToPlay.setSysCfgReady(true);
				SysConfigInfo cpt = (SysConfigInfo)body;
				competitorName = cpt.getUserName();
				log.info("Competitor name" + competitorName);
				sysCfg.setSelectedRb(1-cpt.getSelectedRb());
			}
			else if(head.equals(Header.PIECE_MOVED))//消息头为棋子移动信息
			{
				String tmpStr = (String)body;
				//从消息体中获取该着法的src和dst
				int src = Integer.parseInt(tmpStr.substring(0, tmpStr.indexOf('-')));
				int dst = Integer.parseInt(tmpStr.substring(tmpStr.indexOf('-')+1));
				//log.info("receive a Message to move!");
				movePiece(src, dst, true);
				afterMoved(false);
			}
		}
	}
	
	private class OpenSaveButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			
		}
	}
	
	/**************************************************************************
	 * for lastButton,nextButton,turnBack,computerStop,resetAllButton
	 **************************************************************************/
	private class LastNextTurnBackButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			//如果忽略点击事件，则直接返回
			if(ignoreMouseAction(e.getSource()))
			{
				return;
			}
			//如果是button_preStep按钮
			if(e.getSource()==button_preStep)
			{
				undo();
			}
			else if(e.getSource()==button_nextStep)//button_nextStep按钮
			{
				redo();
			}
			else if(e.getSource()==button_turnBack)//button_turnBack按钮
			{
				log.info("Turn back!!(need rewrite!!");
				undo();
			}
			else if(e.getSource()==button_cmpMove)//电脑移动按钮
			{
				computerMove();
			}
			else if(e.getSource()==button_endStep)//button_endStep按钮
			{
				
			}
			else if(e.getSource()==button_resetAll)//button_resetAll
			{
				log.info("reset all");
				resetAll();
			}
		}
	}
	
	/*******************************************************************
	 * private class section
	 ******************************************************************/
	private class ConnectActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if(ignoreMouseAction(e.getSource()))
			{
				return;
			}
			if(sysCfg.getSelectedSc()==1)//如果是服务器
			{
				netConnection.setPointAsServer(sysCfg.getPortNum());
				log.info("Server started!!!");
			}
			else//如果是客户端
			{
				netConnection.setPointAsClient(sysCfg.getIpAddress(), sysCfg.getPortNum());
				log.info("Client started!!!");
			}
			
			new Thread()
			{
				public void run()
				{
					netConnection.createConnection();
					if(netConnection.available())
					{
						log.info("Server and Client connected!");
						log.info("Starting outReceiver");
						outReceiver.setConnection(netConnection);
						outReceiver.startReceiveData();
						readyToPlay.setConnectionReady(true);
						competitorStarted = true;
						if(sysCfg.getSelectedSc()==1)//as server
						{
							outSender.send(new ChessMessage(Header.SYSINFO, sysCfg, false));
						}
					}
				}
			}.start();
		}
	}
	
	/********************************************************************
	 * for set time rule,display time rule,set syscfg and start button
	 ********************************************************************/
	private class SysInfoButtonsListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			//如果忽略掉此鼠标时间则直接返回
			if(ignoreMouseAction(e.getSource()))
			{
				return;
			}
			//如果是设置规则事件
			if(e.getActionCommand().equals(SET_RULE_COMMAND))
			{
				setTimeRule(timeRuleConfig);
				timeRule.resetTimeRule(timeRuleConfig);
				timeRule.getPlayerTimer().setNeedCount(true);
				if(sysCfg.getBattleModel()==2)//network
				{
					outSender.send(new ChessMessage(Header.SET_TIME_RULE,
							timeRuleConfig, false));
				}
				else
				{
					readyToPlay.setTimeRuleReady(true);
				}
			}
			//button_displayRule  显示规则事件
			else if(e.getActionCommand().equals(DISPLAY_RULE_COMMAND))
			{
				displayTimeRule(timeRuleConfig, "");
			}
			//button_start
			else if(e.getSource()==button_start)
			{
				if(!readyToPlay.isTimeRuleReady())
				{
					timeRule.resetToDefault();
					readyToPlay.setTimeRuleReady(true);
				}
				if(readyToPlay.canPlay())
				{
					if(view!=sysCfg.getSelectedRb())
					{
						reverseBoard();
					}
					started = true;
				}
			}
			//button_setSysCfg
			else if(e.getSource()==button_setSysCfg)
			{
				if(started)
				{
					return;
				}
				setSysCfgInfo(sysCfg);
				if(sysCfg.getBattleModel()==2)//network battle网络对战模式
				{
					button_getConnect.setEnabled(true);
				}
				else
				{
					readyToPlay.setConnectionReady(true);
					button_getConnect.setEnabled(false);
					competitorStarted = true;
				}
				readyToPlay.setSysCfgReady(true);
			}
		}
	}
	
	/**
	 * 棋盘面板的鼠标点击事件
	 * 
	 * @author soft
	 *
	 */
	private class PictureBoardMouseListener extends MouseAdapter
	{
		public void mouseClicked(MouseEvent e)
		{
			//如果忽略点击事件，则直接返回
			if(ignoreMouseAction(e.getSource()))
			{
				return;
			}
			int x = e.getX();
			int y = e.getY();
			Dimension d = pictureBoard.getXYCoordinate(x, y);
			x = d.width;
			y = d.height;
			
			y = 9 - y;//---------------------------------------new code
			
			if(boardReversed)
			{
				x = 8 - x;
				y = 9 - y;
			}
			pictureBoard.test.setText("x="+x+",y="+y);
			//如果上次选择的不为空，说明是移动棋子着法
			if(lastSelected!=null)
			{
				//如果符合移动规则
				if(movePiece(lastSelected, x, y, true))
				{
					changeLocation(lastSelected, x, y);
					
					//new code --------原来这两行是在下面
					timeRule.updateTotalTime(player);
					player = 1 - player;
					
					//如果模式是人机对战，则轮到电脑走棋---因为电脑不会点击棋盘
					if(sysCfg.getBattleModel()==3)
					{
						computerMove();
					}
					//timeRule.updateTotalTime(player);
					//player = 1 - player;
					
					//点击面板,说明不是电脑移动,则设置true
					afterMoved(true);
				}
			}
		}
	}
	
	/**
	 * 棋子的鼠标点击事件
	 * 
	 * @author soft
	 *
	 */
	private class PieceMouseListener extends MouseAdapter
	{
		public void mouseClicked(MouseEvent e)
		{
			if(ignoreMouseAction(e.getSource()))
			{
				return;
			}
			//如果上一次被选择的为空，那么将该棋子设为上次选择的，且将棋子边框着重
			if(lastSelected==null)
			{
				lastSelected = (Qizi)e.getSource();
				lastSelected.setBorderPainted(true);
			}
			else//上一次选择的棋子不为空
			{
				Qizi tmpQizi = (Qizi)e.getSource();
				int x1 = tmpQizi.getCoordinateX();
				int y1 = tmpQizi.getCoordinateY();
				
				//如果可以移动该着法
				if(movePiece(lastSelected, x1, y1, true))
				{
					changeLocation(lastSelected, x1, y1);
					//如果是人机对战模式，那么轮到电脑走棋。--因为只有人会去点击棋子
					if(sysCfg.getBattleModel()==3)
					{
						computerMove();
					}
					timeRule.updateTotalTime(player);
					player = 1 - player;
					afterMoved(true);
				}
				else//相当于重新选择了一个己方棋子
				{
					//将上次选择的棋子退色
					lastSelected.setBorderPainted(false);
					//令lastSelected = 当前棋子
					lastSelected = tmpQizi;
					//将当前棋子着重
					lastSelected.setBorderPainted(true);
				}
			}
		}
	}
	
	
	/**
	 * 时间计时器
	 * 
	 * @author zceolrj
	 *
	 */
	private class TimeCounter implements PlayerTimer
	{
		/**
		 * 是否需要计时
		 */
		private boolean needCount;
		
		/**
		 * 显示的字符串
		 */
		private String displayStr;
		
		/**
		 * 默认构造函数
		 */
		public TimeCounter()
		{
			this.needCount = false;
		}
		
		/* (non-Javadoc)
		 * @see com.rule.PlayerTimer#display()
		 * 
		 * 显示信息--显示的是时间信息---注意只显示当前玩家的计时信息
		 */
		public void display()
		{
			displayStr = timeRule.getDisplayString(timeRule.getUsedTime(player));
			if(player==0)
			{
				textField_redTime.setText("red: "+displayStr);
			}
			else
			{
				textField_blackTime.setText("black:"+displayStr);
			}
		}
		
		public int getCurrentPlayer()
		{
			return player;
		}
		
		public void setNeedCount(boolean needCount)
		{
			this.needCount = needCount;
		}
		
		public boolean isNeedCount()
		{
			return needCount;
		}
	}
}



