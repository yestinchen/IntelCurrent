package com.kernel.intelcurrent.model;

import java.io.Serializable;
import java.util.Comparator;

/**
 * classname:User.java
 * @author 许凌霄
 * */
public class User implements Serializable
{
  /**
	 * 
	 */
	private static final long serialVersionUID = -3990550183984216423L;
private static final String TAG=User.class.getSimpleName();
  public static final String PLATFORM_TENCENT = "腾讯微博";
  public static final String PLATFORM_SINA = "新浪微博";
  
  public static final int PLATFORM_TENCENT_CODE = 1;
  public static final int PLATFORM_SINA_CODE = 2;
  /**
   * 用户唯一id
   * */
  public String id;
  
  /**
   * 用户账号名
   */
  public String name;
  
  /**
   * 用户昵称
   * */
  public String nick;
  
  /**
   * 所在省份代码
   * */
  public int province;
  
  /**
   * 所在城市代码
   * */
  public int city;
  
  /**
   * 所在地
   * */
  public String location;
  
  /**
   * 个人简介
   * */
  public String description;
  
  /**
   * 个人主页
   * */
  public String homepage;
  
  /**
   * 头像url
   * */
  public String head;
  
  /**
   * 性别：0-未知，1-男性，2-女性
   * */
  public int gender;
  
  /**
   * 粉丝数
   * */
  public int fansnum;
  
  /**
   * 关注数
   * */
  public int idolnum;
  
  /**
   * 收藏数
   * */
  public int favnum;
  
  /**
   * 微博数
   * */
  public int statusnum;
  
  /**
   * 注册时间
   * */
  public String regTime;
  
  /**
   * 是否我的关注
   * */
  public boolean ismyidol;
  
  @Override
public String toString() {
	return "User [id=" + id + ", name=" + name + ", nick=" + nick
			+ ", province=" + province + ", city=" + city + ", location="
			+ location + ", description=" + description + ", homepage="
			+ homepage + ", head=" + head + ", gender=" + gender + ", fansnum="
			+ fansnum + ", idolnum=" + idolnum + ", favnum=" + favnum
			+ ", statusnum=" + statusnum + ", regTime=" + regTime
			+ ", ismyidol=" + ismyidol + ", ismyfan=" + ismyfan + ", platform="
			+ platform + "]";
}

/**
   *是否关注我 
   * */
  public boolean ismyfan;
  
  /**
   * 用户所在平台
   * */
  public int platform;


  public static class NickNameComparator implements Comparator<User>{

	@Override
	public int compare(User lhs, User rhs) {
		return lhs.nick.compareTo(rhs.nick);
	}
	  
  }
  
  /**
   * 最新微博
   * */
//  public Status newest=new Status();
}
