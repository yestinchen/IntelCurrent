package com.kernel.intelcurrent.model;
/**
 * classname:Comment.java
 * @author allenjin
 * */
public class Comment {
	
	private static final String TAG=Comment.class.getSimpleName();
	
	 /**
     * 评论id
     * */
	public String id;
	
	/**评论者name*/
	public String name;
	
	 /**
     * 评论者openid
     * */
	public String openid;
	
	 /**
     * 评论者nick名称
     * */
	public String nick;
	
	 /**
     * 评论时间戳
     * */
	public long timestamp;
	
	 /**
     * 评论内容
     * */
	public String text;

	@Override
	public String toString() {
		return "Comment [id=" + id + ", openid=" + openid + ", 用户名=" + nick
				+ ", timestamp=" + timestamp + ", 内容=" + text + "]";
	}
	
	 
}
