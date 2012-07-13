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
	public LinkedList<Object> result;
	
	/**
	 * 任务参数表
	 * */
    private Map<String,Object> param;
    public Task(int t,Map<String,Object> p,Activity target)
    {
    	type=t;
    	param=p;
    	this.target=target;
    }
}
