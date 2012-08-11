package com.kernel.intelcurrent.model;

import java.io.Serializable;
import java.util.ArrayList;

public class ICArrayList implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7173337308246024648L;
	/**
	 * 保存获取的结果并封装成list
	 */
	public ArrayList<Object> list = new ArrayList<Object>();
	/**
	 * 保存获取的结果的标志位
	 */
	public int hasNext;
	@Override
	public String toString() {
		return "ICArrayList [list=" + list + ", hasNext=" + hasNext + "]";
	}
}
