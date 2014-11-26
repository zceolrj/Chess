package com.rule;

/**
 * 玩家计时接口
 * 
 * @author zceolrj
 *
 */
public interface PlayerTimer 
{
    /**
     * 是否需要计时
     * 
     * @return
     */
    public boolean isNeedCount();
    
    /**
     * 设置是否需要计时
     * 
     * @param needCount 需要计时
     */
    public void setNeedCount(boolean needCount);
    
    /**
     * 显示
     */
    public void display();
    
    /**
     * 获得当前玩家
     * 
     * @return
     */
    public int getCurrentPlayer();
}
