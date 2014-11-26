package com.message.impl;

import com.message.Header;
import com.message.Message;

/**
 * 象棋消息
 * 
 * @author zceolrj
 *
 */
public class ChessMessage implements Message
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 是否是本地的
	 */
	private boolean local;
    
    /**
     * 消息体
     */
    private Object body;
    
    /**
     * 消息头
     */
    private Header header;
    
    public ChessMessage(Header header, Object body, boolean local)
    {
    	this.header = header;
    	this.local = local;
    	this.body = body;
    }

	public boolean isLocalMessage() {
		return local;
	}

	public Object getMessageBody() {
		return body;
	}

	public void setMessageBody(Object body) {
		this.body = body;
	}

	public Header getMessageHeader() {
		return header;
	}

	public void setMessageHeader(Header header) {
		this.header = header;
	}      
}
