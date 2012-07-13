package com.kernel.intelcurrent.model;

/**
 * classname:User.java
 * @author 许凌霄
 * */
public class User 
{
  private static final String TAG=User.class.getSimpleName();
  /**
   * 用户唯一id
   * */
  public String id;
  
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
  
  /**
   *是否关注我 
   * */
  public boolean ismyfan;
  
  /**
   * 用户所在平台
   * */
  public String platform;
  
  /**
   * 最新微博
   * */
  public Status newest;
}
