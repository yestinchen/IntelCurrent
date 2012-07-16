package com.kernel.intelcurrent.model;

import java.util.LinkedList;
import java.util.Map;

import android.app.Activity;
/**
 * classname:Task.java
 * @author 许凌霄
 * */
public class Task 
{
	private static final String TAG=Task.class.getSimpleName();
	
	/*-------关于组的操作 @author sheling----*/
	public static final int G_GET_GROUP_LIST = 0x000001;
	public static final int G_ADD_GROUP = 0x000002;
	public static final int G_DEL_GROUP = 0x000003;
	public static final int G_RENAME = 0x000004;
	public static final int G_ADD_USER = 0x000005;
	public static final int G_DEL_USER = 0x000006;
	public static final int G_GET_USER_LIST = 0x000007;
	public static final int G_GET_API_GROUP = 0x000008;
	public static final int G_GET_GROUP_TIMELINE = 0x000009;
	/*-----关于微博的操作----*/
	public static final int WEIBO_ADD = 0x000011;
	public static final int WEIBO_COMMENTS_BY_ID = 0x000012;
	public static final int WEIBO_REPOST = 0x000013;
	public static final int WEIBO_FAV_ADD = 0x000014;
	public static final int WEIBO_COMMENTS_ADD = 0x000015;
	public static final int WEIBO_SHOW = 0x000016;
	public static final int WEIBO_PRIVATE_ADD = 0x000017;
	public static final int WEIBO_ADD_AT = 0x000018;
	public static final int WEIBO_COMMENTS_RE = 0x000019;
	/*-------关于用户的操作------*/
	public static final int USER_INFO = 0x000021;
	public static final int USER_FANS_LIST = 0x000022;
	public static final int USER_FRIENDS_LIST = 0x000023;
	public static final int USER_FAV_LIST = 0x000024;
	public static final int USER_WEIBO_LIST = 0x000025;
	public static final int USER_FRIENDS_ADD = 0x000026;
	public static final int USER_FREINDS_DEL = 0x000027;
	public static final int USER_SEARCH = 0x000028;
	/*-----关于消息的操作-----*/
	public static final int MSG_COMMENTS_MENTIONS = 0x000031;
	public static final int MSG_COMMENTS_ME_LIST = 0x000032;
	public static final int MSG_PRIVATE_LIST = 0x000033;
	public static final int MSG_NUM_NOTIFICATION = 0x000034;
	
	/**
	 * 任务类型
	 * */
	public int type;
	
	/**
	 * 线程总数
	 * */
	public int total;
	
	/**
	 * 目前回复结果的线程数
	 * */
	public int current;
	
	/**
	 * 任务目标activity
	 * */
	public Activity target;
	
	/**
	 * 任务结果集
	 * */
	public LinkedList<Object> result=new LinkedList<Object>();
	
	/**
	 * 任务参数表
	 * */
    public Map<String,Object> param;
    public Task(int t,Map<String,Object> p,Activity target)
    {
    	type=t;
    	param=p;
    	this.target=target;
    }
}
