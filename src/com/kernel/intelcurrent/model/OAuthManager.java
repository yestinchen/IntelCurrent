package com.kernel.intelcurrent.model;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import com.kernel.intelcurrent.activity.LoginActivity;
import com.tencent.weibo.oauthv2.OAuthV2;
import com.weibo.net.AccessToken;
import com.weibo.net.Weibo;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

/**
 * 
 * @author allenjin
 *
 */
public class OAuthManager {
	
	public static final String TAG = OAuthManager.class.getSimpleName();
	
	public static final int TENCENT_PLATFORM=1;
	public static final int SINA_PLATFORM=2;
	public static final String OAUTH_FILE="oauth";
	
	public static final String SINA_WEIBO="sina";
	public static final String SINA_ACCESS_TOKEN="sina_access_token";
	public static final String SINA_UID = "sina_uid";
	public static final String SINA_EXPIRES_IN = "sina_expires_in";
	public static final String SINA_ACCESS_TOKEN_START_TIME = "sina_start_time";
	public static final String SINA_CUSTOMER_KEY="48797f21d90826561ef6ce79e06bcf46";
	public static final String SINA_CUSTOMER_ID="3683762445";
	public static final String SINA_REDIRECT_URI="http://weibo.com/sheling";
	
	public static final String TENCENT_WEIBO="tencent";
	public static final String TENCENT_ACCESS_TOKEN="tencent_access_token";
	public static final String TENCENT_OPEN_ID="tencent_openid";
	public static final String TENCENT_OPEN_KEY="tencent_openkey";
	public static final String TENCENT_EXPIRES_IN = "tencent_expires_in";
	public static final String TENCENT_ACCESS_TOKEN_START_TIME = "tencent_start_time";
	public static final String TENCENT_CUSTOMER_KEY="f73f4b5dbc88040e4ed8a8787e88fdc6";
	public static final String TENCENT_CUSTOMER_ID="801189892";
	public static final String TENCENT_REDIRECT_URI="http://t.qq.com/sheling2010";
	
	public static final int RESULT_NO_PLATFORM_AVALIABLE = 0;
	public static final int RESULT_ONLY_SINA_AVALIABLE = 1;
	public static final int RESULT_ONLY_TENCENT_AVALIABLE = 2;
	public static final int RESULT_BOTH_AVALIABLE = 3;
	/**
	 * 创建一个OAuthManager的单例
	 * @author allenjin
	 *
	 */
	private static class OAuthManagerHolder{
		public static OAuthManager	OAuthManager=new OAuthManager();
	}
	private OAuthManager(){
	}
	public static OAuthManager getInstance(){
		return OAuthManagerHolder.OAuthManager;
	}
	
	/**
	 * 判断OAuth认证是否过期
	 * @author allenjin
	 * <br/> modified by sheling
	 * @return int 
	 */
	public int oAuthCheck(Context context){
		int result= 0;
		SharedPreferences spf=context.getSharedPreferences(OAUTH_FILE, 0);
		boolean hasTencent=spf.getBoolean(TENCENT_WEIBO, false);
		boolean hasSina=spf.getBoolean(SINA_WEIBO, false);
		if(hasTencent){//判断是否登录了腾讯微博,若是,判断是否过期
			String exiresIn = spf.getString(TENCENT_EXPIRES_IN, "0");
			String startTime = spf.getString(TENCENT_ACCESS_TOKEN_START_TIME, "0");
			if(Long.valueOf(exiresIn)*1000 + Long.valueOf(startTime) > System.currentTimeMillis()){
				result += RESULT_ONLY_TENCENT_AVALIABLE;
			}
			Log.d(TAG, "check tencent"+result);
		}
		if(hasSina){//判断是否登录了新浪微博,若是,判断是否过期
			String exiresIn = spf.getString(SINA_EXPIRES_IN, "0");
			String startTime = spf.getString(SINA_ACCESS_TOKEN_START_TIME, "0");
			if(Long.valueOf(exiresIn)*1000 + Long.valueOf(startTime) > System.currentTimeMillis()){
				result += RESULT_ONLY_SINA_AVALIABLE;
			}
			Log.d(TAG, "check sina"+result);
		}
		Log.d(TAG, "oauth check result:"+result);
		return result;
	}
	
	/**
	 * 将对应平台的OAuth认证信息存放在SharePreferences
	 * @author allenjin
	 */
	public void setOAuthKey(Context context,int type,Map<String,String> map){
		SharedPreferences spf=context.getSharedPreferences(OAUTH_FILE, 0);
		Editor editor=spf.edit();
		switch(type){
		case TENCENT_PLATFORM:
			editor.putBoolean(TENCENT_WEIBO,true);
			editor.putString(TENCENT_ACCESS_TOKEN, map.get(TENCENT_ACCESS_TOKEN));
			editor.putString(TENCENT_OPEN_ID, map.get(TENCENT_OPEN_ID));
			editor.putString(TENCENT_OPEN_KEY, map.get(TENCENT_OPEN_KEY));
			editor.putString(TENCENT_EXPIRES_IN, map.get(TENCENT_EXPIRES_IN));
			editor.putString(TENCENT_ACCESS_TOKEN_START_TIME, map.get(TENCENT_ACCESS_TOKEN_START_TIME));
			editor.commit();
			break;
		case SINA_PLATFORM:
			editor.putBoolean(SINA_WEIBO,true);
			editor.putString(SINA_UID, map.get(SINA_UID));
			editor.putString(SINA_ACCESS_TOKEN, map.get(SINA_ACCESS_TOKEN));
			editor.putString(SINA_EXPIRES_IN, map.get(SINA_EXPIRES_IN));
			editor.putString(SINA_ACCESS_TOKEN_START_TIME, map.get(SINA_ACCESS_TOKEN_START_TIME));
			editor.commit();
			break;
		}
	}
	
	/**
	 * 获取对应平台的OAuth认证返回的参数并封装
	 * @return Object result 腾讯返回oauth,新浪返回weibo对象;
	 * @author allenjin
	 */
	public Object getOAuthKey(Context context,int type){
		SharedPreferences spf=context.getSharedPreferences(OAUTH_FILE, 0);
		switch(type){
		case TENCENT_PLATFORM:
			OAuthV2 oauth=new OAuthV2(TENCENT_CUSTOMER_ID,TENCENT_CUSTOMER_KEY,TENCENT_REDIRECT_URI);
			oauth.setAccessToken(spf.getString(TENCENT_ACCESS_TOKEN, null));
			oauth.setOpenid(spf.getString(TENCENT_OPEN_ID, null));
			oauth.setOpenkey(spf.getString(TENCENT_OPEN_KEY, null));
//			oauth.setClientIP(getClientIP());
			oauth.setExpiresIn(spf.getString(TENCENT_EXPIRES_IN,null));
			return oauth;
		case SINA_PLATFORM:
			Weibo weibo = Weibo.getInstance();
			weibo.setupConsumerConfig(SINA_CUSTOMER_ID, SINA_CUSTOMER_KEY);
			weibo.setRedirectUrl(SINA_REDIRECT_URI);
			weibo.setAccessToken(new AccessToken(spf.getString(SINA_ACCESS_TOKEN, null),null));
			return weibo;
		}
		return null;
	}
	
	/**
	 * 获取手机的IP地址
	 * @author allenjin
	 */
	public String getClientIP(){
		String ip="";
		try{
			for(Enumeration<NetworkInterface>en=NetworkInterface.getNetworkInterfaces();
					en.hasMoreElements();){
				NetworkInterface intf=en.nextElement();
				for(Enumeration<InetAddress>enumipAddr=intf.getInetAddresses();
						enumipAddr.hasMoreElements();){
					InetAddress inetAddress=enumipAddr.nextElement();
					if(!inetAddress.isLoopbackAddress()){
						ip=inetAddress.getHostAddress().toString();
					}
				}
			}
				
		}catch (Exception e) {
			e.printStackTrace();
		}
		return ip;
	}
	
}
