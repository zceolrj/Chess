package com.rule;

/**
 * 时间规则配置类
 * 
 * @author zceolrj
 *
 */
public class TimeRuleConfig 
{
    /**
     * 基本时间
     */
    private long baseTime;
    
    /**
     * 每一步的附加时间
     */
    private long addTimePerStep;
    
    /**
     * 默认构造函数
     */
    public TimeRuleConfig()
    {
    	baseTime = 60*30;
    	addTimePerStep = 3;
    }

	public long getBaseTime() {
		return baseTime;
	}

	public void setBaseTime(long baseTime) {
		this.baseTime = baseTime;
	}

	public long getAddTimePerStep() {
		return addTimePerStep;
	}

	public void setAddTimePerStep(long addTimePerStep) {
		this.addTimePerStep = addTimePerStep;
	}
}
