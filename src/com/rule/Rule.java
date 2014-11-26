package com.rule;

/**
 * 规则类，保存的都是常量
 * 
 * @author zceolrj
 *
 */
public class Rule 
{
    //red or black 红方 黑方
	public static final int PLAYER_RED = 0; 
    public static final int PLAYER_BLACK = 1;
    
    //direction 进 退 平
    public static final int DIR_JIN = 1;
    public static final int DIR_TUI = 2;
    public static final int DIR_PING = 3;
    
    //两个同样的兵种在一条纵线上
    public static final int FRONT = 1;
    public static final int BACK = 2;
    
    //时间规则的模式,快棋和多时间规则
    public static final int TIMERULE_QUICK = 1;
    public static final int TIMERULE_MULTI = 2;
}
