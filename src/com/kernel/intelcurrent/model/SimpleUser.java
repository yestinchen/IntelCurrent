package com.kernel.intelcurrent.model;
/**
 * classname:SimpleUser.java
 * 此类主要用户搜索时展现成用户列表
 * @author allenjin
 * */
public class SimpleUser {

	/**
	 * 用户名账号
	 */
	public String name;

	/**
	 * 昵称
	 */
	public String nick;
	
	/**
	 * 用户openid
	 */
	public String id;
	
	/**
	 * 用户头像地址
	 */
	public String head;
	
	/**
	 * 微博平台
	 */
	public String platform;

	/**
	 * 粉丝数
	 */
	public int fansnum;

	@Override
	public String toString() {
		return "SimpleUser [name=" + name + ", nick=" + nick + ", id=" + id
				+ ", head=" + head + ", platform=" + platform + ", fansnum="
				+ fansnum + "]";
	}
	
	
}
