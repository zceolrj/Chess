package com.rule;

import com.message.Header;
import com.message.Producer;
import com.message.impl.ChessMessage;

/**
 * 时间规则类
 * 
 * @author zceolrj
 *
 */
public class TimeRule extends Thread
{
    /**
     * 已经使用的时间         0:red, 1:black    unit:second
     */
    private long usedTime[] = {0, 0};
    
    /**
     * 毫秒
     */
    private long millSeconds[] = {0, 0};
    
    /**
     * 是否需要计时
     */
    private boolean needCount;
    
    /**
     * 同步对象
     */
    private Object synchObject = new Object();
    
    /**
     * 基本时间
     */
    private long baseTime;
    
    /**
     * 附加时间
     */
    private long addTime;
    
    /**
     * 黑白双方可以使用的总时间        0:red    1:black
     */
    private long totalTime[] = {0, 0};
    
    /**
     * 双方的时间是否已经耗尽      0:red    1:black
     */
    private boolean timeExhausted[] = {false, false};
    
    /**
     * 计时接口
     */
    private PlayerTimer playerTimer;
    
    /**
     * 时间规则配置
     */
    private TimeRuleConfig currentConfig;
    
    /**
     * 默认的时间规则配置
     */
    private final TimeRuleConfig defaultTimeRuleConfig = new TimeRuleConfig();
    
    /**
     * 
     */
    private Producer producer;
    
    /**
     * 默认构造函数,就设置默认的时间配置
     */
    public TimeRule()
    {
    	resetTimeRule(defaultTimeRuleConfig);
    }
    
    /**
     * 设置一个时间规则配置
     * 
     * @param trc
     */
    public TimeRule(TimeRuleConfig trc)
    {
    	resetTimeRule(trc);
    }
    
    /**
     * 重置为默认的时间规则配置
     */
    public void resetToDefault()
    {
    	resetTimeRule(defaultTimeRuleConfig);
    }
    
    /**
     * 重置时间规则
     * 
     * @param trc
     */
    public void resetTimeRule(TimeRuleConfig trc)
    {
    	currentConfig = trc;
    	usedTime[0] = 0;
    	usedTime[1] = 0;
    	baseTime = trc.getBaseTime();
    	addTime = trc.getAddTimePerStep();
    	totalTime[0] = totalTime[1] = baseTime;
    	needCount = false;
    }
    
    /**
     * 重置时间规则
     */
    public void resetTimeRule()
    {
    	if(currentConfig!=null)
    	{
    		resetTimeRule(currentConfig);
    	}
    	else
    	{
    		resetTimeRule(defaultTimeRuleConfig);
    	}
    }
    
    /**
     * 计时
     * 
     * @param currentPlayer 当前玩家
     * @param lastMillis 上次开始计时的时间点
     */
    protected void countTime(int currentPlayer, long lastMillis)
    {
    	long current = millSeconds[currentPlayer] + System.currentTimeMillis() - lastMillis;
    	millSeconds[currentPlayer] = current%1000;
    	usedTime[currentPlayer] += current/1000;
    	
    	//如果使用的时间超过了总时间，则时间耗尽
    	timeExhausted[currentPlayer] = usedTime[currentPlayer]> totalTime[currentPlayer];
    }
    
    /**
     * 重置时间并开始计时
     */
    public void resetTimeAndBeginCount()
    {
    	usedTime[0] = 0;
    	usedTime[1] = 0;
    	totalTime[0] = totalTime[1] = baseTime;
    	needCount = true;
    	if(this.isAlive())
    	{
    		synchronized(synchObject)
    		{
    			synchObject.notify();
    			return;
    		}
    	}
    	start();
    }
    
    /**
     * 暂停计时
     */
    public void pauseCount()
    {
    	needCount = false;
    }
    
    /**
     * 继续计时
     */
    public void continueCount()
    {
    	needCount = true;
    	synchronized(synchObject)
    	{
    		synchObject.notify();
    	}
    }
    
    /**
     * 获取显示字符串：时：分：秒
     * 
     * @param seconds
     * @return
     */
    public String getDisplayString(long seconds)
    {
    	long h = seconds/3600;
    	long m = (seconds%3600)/60;
    	long s = seconds%60;
    	return (h + ":" + m + ":" + s);
    }
    
    /**
     * 获取红发或者黑方的总时间
     * 
     * @param redOrBlack
     * @return
     */
    public long getTotalTime(int redOrBlack)
    {
    	return totalTime[redOrBlack];
    }
    
    /**
     * 获取红发或黑方已经使用的时间
     * 
     * @param rb
     * @return
     */
    public long getUsedTime(int rb)
    {
    	int index = 0;
    	if(rb==Rule.PLAYER_RED)
    	{
    		index = 0;
    	}
    	else if(rb==Rule.PLAYER_BLACK)
    	{
    		index = 1;
    	}
    	return usedTime[index];
    }
    
    /**
     * 更新总时间
     * 
     * @param redOrBlack
     */
    public void updateTotalTime(int redOrBlack)
    {
    	//加上附加时间
    	totalTime[redOrBlack] += addTime;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Thread#run()
     * 
     * 该线程的run方法
     */
    public void run()
    {
    	long startMills;
    	int player;
    	synchronized(synchObject)
    	{
    		while(true)
    		{
    			if(!needCount)//如果不需要计时,则等待
    			{
    				try
    				{
    					synchObject.wait();
    				}
    				catch(InterruptedException e)
    				{
    					e.printStackTrace();
    				}
    			}
    			try
    			{
    				//从玩家计时接口获取当前玩家----很巧妙,一个计数器可以为两个玩家计时
    				player = playerTimer.getCurrentPlayer();
    				startMills = System.currentTimeMillis();
    				
    				//每隔一秒计时一次
    				synchObject.wait(1000);
    				
    				//记录当前玩家使用的时间
    				countTime(player, startMills);
    				//每隔一秒在界面上显示一次时间
    				playerTimer.display();
    				
    				//如果玩家时间耗尽了
    				if(timeExhausted[player])
    				{
    					if(player==0)
    					{
    						producer.send(new ChessMessage(Header.RED_TIME_USED, null, true));
    					}
    					else
    					{
    						producer.send(new ChessMessage(Header.BLACK_TIME_USED, null, true));
    					}
    					this.resetTimeRule();
    				}
    			}
    			catch(InterruptedException e)
    			{
    				e.printStackTrace();
    			}
    		}
    	}
    }

	public PlayerTimer getPlayerTimer() {
		return playerTimer;
	}

	public void setPlayerTimer(PlayerTimer playerTimer) {
		this.playerTimer = playerTimer;
	}
    
    public void setProducer(Producer producer)
    {
    	this.producer = producer;
    }
}