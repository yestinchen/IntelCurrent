package com.kernel.intelcurrent.service;
import java.util.HashMap;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.kernel.intelcurrent.activity.Updateable;
import com.kernel.intelcurrent.model.ErrorEntry;
import com.kernel.intelcurrent.model.Group;
import com.kernel.intelcurrent.model.ICModel;
import com.kernel.intelcurrent.model.SimpleUser;
import com.kernel.intelcurrent.model.Task;
import com.kernel.intelcurrent.model.User;
/**
 * classname:MainService.java
 * @author 许凌霄
 * */
public class MainService extends Service
{
    private static final String TAG=MainService.class.getSimpleName();
    private static MainService service;
    private ICModel model;
    private IBinder mBinder=new ICBinder();
    private Activity currentActivity;
    
	@Override
	public void onCreate() {
    	service=this;
    	model=ICModel.getICModel();
		super.onCreate();
	}

	@Override
	public IBinder onBind(Intent intent) 
	{
		return mBinder;
	}
	public static MainService getInstance()
	{
		return service;
	}
	public void changeCurrentActivity(Activity current)
	{
		currentActivity=current;
	}
	
	/*=======================操纵ICModel的方法开始=======================================*/
	
	/**得到Timeline的信息
	 * @author sheling*/
	public void getTimeline(long time,Group group,int pageflag, long pagetime,String lastid){
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("pageflag", pageflag);
		map.put("pagetime", pagetime);
		map.put("lastid", lastid);
		map.put("reqnum", 25);
		
		StringBuilder openids = new StringBuilder();
		for(SimpleUser user:group.users){
			openids.append(user.id).append("_");
		}
		map.put("fopenids", openids.toString());
		Task t = new Task(time,Task.G_GET_GROUP_TIMELINE,map,null);
		model.doTask(t,this);
	}
	/**
	 * 批量获取一批用户的简单信息
	 *@see fopenids:需要读取的用户的openid列表,用下划线_隔开，(<=30);
	 */
	public void getSimpleUserList(String fopenids,long time){
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("fopenids", fopenids);
		Task t=new Task(time,Task.USER_SIMPLE_INFO_LIST,map,null);
		model.doTask(t, this);
	}
	/**
	 * 获取用户的信息 //fopenids为0为用户自己（腾讯）
	 * @author allenjin
	 */
	public void getUserInfo(String openid){
			HashMap<String,Object> map = new HashMap<String,Object>();
			map.put("openid", openid);
			Task t=new Task(Task.USER_INFO,map,null);
			model.doTask(t, this);
			Log.v(TAG,"getuserinfo in service");
	}
	/**
	 * 获取提及用户的微博列表（包括：at和评论）
	 * @author allenjin
	 * @param type:0x1 原创发表 0x2 转载 0x8 回复 0x10 空回 0x20 提及 0x40 点评，若为0则为全部获取
	 */
	public void getMentionWeiboList(int type,int pageflag, long pagetime,String lastid){
		HashMap<String,Object> map=new HashMap<String,Object>();
		map.put("type", type);
		map.put("pageflag", pageflag);
		map.put("pagetime", pagetime);
		map.put("lastid", lastid);
		map.put("reqnum", 20);
		Task t;
		if(type==(0x8|0x40)){
			t=new Task(Task.MSG_COMMENTS_ME_LIST,map,null);//评论
		}else{
			t=new Task(Task.MSG_COMMENTS_MENTIONS,map,null);
		}
		model.doTask(t, this);
	}
	/**
	 * 获取私信内容
	 * @author allenjin
	 */
	public void getPriMsgList(int pageflag, long pagetime,String lastid){
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("pageflag", pageflag);
		map.put("pagetime", pagetime);
		map.put("lastid", lastid);
		map.put("reqnum", 20);
		Task t=new Task(Task.MSG_PRIVATE_LIST,map,null);
		model.doTask(t, this);
	}
	/**
	 * 获取用户自己的粉丝列表
	 * @param startindex 填当前获取的为第几页 第一页为:1;
	 */
	public void getUserFansList(int startindex){
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("reqnum", 20);
		map.put("startindex",20*(startindex-1));
		Task t=new Task(Task.USER_FANS_LIST,map,null);
		model.doTask(t, this);
	}
	/**
	 * 获取用户自己的收听列表
	 * @param startindex 填当前获取的为第几页 第一页为:1;
	 */
	public void getUserFriendsList(int startindex){
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("reqnum", 20);
		map.put("startindex",20*(startindex-1));
		Task t=new Task(Task.USER_FRIENDS_LIST,map,null);
		model.doTask(t, this);
	}
	/**
	 * 获取用户的收藏列表
	 * @author allenjin
	 */
	public void getUserShoucangList(int pageflag, long pagetime,String lastid){
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("pageflag", pageflag);
		map.put("pagetime", pagetime);
		map.put("lastid", lastid);
		map.put("reqnum", 20);
		Task t=new Task(Task.USER_FAV_LIST,map,null);
		model.doTask(t, this);
	}
	/**
	 * 获取用户自己发表的微博列表
	 * @author allenjin
	 */
	public void getUserWeiboList(int pageflag, long pagetime,String lastid){
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("pageflag", pageflag);
		map.put("pagetime", pagetime);
		map.put("lastid", lastid);
		map.put("reqnum", 20);
		Task t=new Task(Task.USER_WEIBO_LIST,map,null);
		model.doTask(t, this);
	}
	/**
	 * 获取其他用户的听众列表
	 * @param name	用户名
	 * @param startindex	填当前获取的为第几页 第一页为:1;
	 */
	public void getOtherFansList(String name,int startindex){
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("reqnum", 20);
		map.put("name", name);
		map.put("startindex",20*(startindex-1));
		Task t=new Task(Task.USER_OTHER_FANS_LIST,map,null);
		model.doTask(t, this);
	}
	/**
	 * 获取其他用户的收听列表
	 * @param name 用户名
	 * @param startindex	填当前获取的为第几页 第一页为:1;
	 */
	public void getOtherFollowList(String name,int startindex){
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("reqnum", 20);
		map.put("startindex",20*(startindex-1));
		map.put("name", name);
		Task t=new Task(Task.USER_OTHER_FRIENDS_LIST,map,null);
		model.doTask(t, this);
	}
	/**
	 * 获取其他用户发表的微博列表
	 * @param fopenids 其他用户的openid
	 * @author allenjin
	 */
	public void getOtherWeiboList(String fopenids,int pageflag, long pagetime,String lastid){
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("pageflag", pageflag);
		map.put("pagetime", pagetime);
		map.put("fopenids", fopenids);
		map.put("lastid", lastid);
		map.put("reqnum", 20);
		Task t=new Task(Task.USER_OTHER_WEIBO_LIST,map,null);
		model.doTask(t, this);
	}
	/**
	 * 取消收听用户
	 * @param name 用户名
	 * @author allenjin
	 */
	public void delFriend(String name){
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("name", name);
		Task t=new Task(Task.USER_FREINDS_DEL,map,null);
		model.doTask(t, this);
	}
	/**
	 * 根据关键字搜索用户
	 * @param keyword 1-20字节
	 */
	public void searchUser(long time,String keyword,int page){
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("keyword", keyword);
		map.put("pagesize", "15");
		map.put("page", page);
		Task t=new Task(time,Task.USER_SEARCH,map,null);
		model.doTask(t, this);
	}
	/**
	 * 收听用户
	 * @param name 用户名
	 * @author allenjin
	 */
	public void addFriend(String name){
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("name", name);
		Task t=new Task(Task.USER_FRIENDS_ADD,map,null);
		model.doTask(t, this);
	}
	/**
	 * 获取其他用户信息
	 * @param openid 其他用户的openid
	 * @author allenjin
	 */
	public void getOtherUserInfo(String openid){
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("openid", openid);
		Task t=new Task(Task.USER_OTHER_INFO,map,null);
		model.doTask(t, this);
		Log.v(TAG,"getuserinfo in service");
	}
	/**添加一条微博*/
	public void addWeibo(String content,String imgUrl,int platform){
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("content", content);
		map.put("imgurl", imgUrl);
		map.put("platform", platform);
		Task t = new Task(Task.WEIBO_ADD,map,null);
		model.doTask(t, this);
		Log.v(TAG, "add weibo"+content+"img"+imgUrl);
	}
	
	/**添加一条评论*/
	public void addComment(String content,String reid,int platform){
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("content", content);
		map.put("reid", reid);
		map.put("platform", platform);
		Task t = new Task(Task.WEIBO_COMMENTS_ADD,map,null);
		model.doTask(t, this);
	}
	
	/**转发一条微博*/
	public void rePost(String content,String reid,int platform){
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("content", content);
		map.put("reid", reid);
		map.put("platform", platform);
		Task t = new Task(Task.WEIBO_REPOST,map,null);
		model.doTask(t, this);
	}
	
	/**获取评论列表
	 * 
	 *@param lastid  1-100为0，之后填上一个的id*/
	public void getCommentList(String rootid,int pageflag,long pagetime,String lastid,int platform){
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("flag", 1);
		map.put("rootid", rootid);
		map.put("pageflag", pageflag);
		map.put("pagetime", pagetime);
		map.put("reqnum", 10);
		map.put("twitterid", lastid);
		map.put("platform",platform);
		Task t = new Task(Task.WEIBO_COMMENTS_BY_ID,map,null);
		model.doTask(t, this);
	}
	
	public void searchAtUser(String keyword,int page,int platform){
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("keyword", keyword);
		map.put("pagesize", 15);
		map.put("page", page);
		map.put("platform", platform);
		Task t = new Task(Task.WEIBO_ADD_AT,map,null);
		model.doTask(t, this);
	}
	/*=======================操纵ICModel的方法结束=======================================*/
	
	/**
	 * inner class:ICBinder
	 * 
	 * */
    public class ICBinder extends Binder
    {
    	/**
    	 * 让activity 获得MainService实例
    	 * */
    	public MainService getService()
 	   {
 	      return MainService.this;
 	   }
    }
    public void send(Task task)
    {
    	Message msg=new Message();
    	msg.obj=task;
    	msg.what=task.type;
    	handler.sendMessage(msg);
    }
    /**较检任务返回代码
     * @return true if result is okay*/
    private boolean checkTaskResultCode(Task task){
    	HashMap<Integer,ErrorEntry> map = task.errors;
    	if(map.get(User.PLATFORM_SINA_CODE) != null){
    		ErrorEntry error = map.get(User.PLATFORM_SINA_CODE);
    		if(error.ret == 0 && error.exception == 0) return true;
        	if(checkTaskException(error)){
        		checkTencentErrorCode();
        	}
    	}
    	if(map.get(User.PLATFORM_TENCENT_CODE) != null){
    		ErrorEntry error = map.get(User.PLATFORM_TENCENT_CODE);
        	if(error.ret == 0 && error.exception == 0) return true;
        	if(checkTaskException(error)){
        		checkTencentErrorCode();
        	}
    	}
    	return false;
    }
    
    /**检查任务执行中是否有异常
     * @return true 若无异常*/
    private boolean checkTaskException(ErrorEntry error){
    	switch(error.exception){
    	case ErrorEntry.EXCEPTION_SSL:
    		Toast.makeText(this, "当前连接出现SSL问题！请检查连接状况", Toast.LENGTH_SHORT).show();
    		return false;
    	case ErrorEntry.EXCEPTION_CONNECT_TIME_OUT:
    		Toast.makeText(this, "连接超时！请检查连接状况", Toast.LENGTH_SHORT).show();
    		return false;
    	case ErrorEntry.EXCEPTION_UNKNOWN_HOST:
    		Toast.makeText(this, "未知域名！请检查连接状况及DNS设置", Toast.LENGTH_SHORT).show();
    		return false;
    	case ErrorEntry.EXCEPTION_JSON:
    		Toast.makeText(this, "数据问题", Toast.LENGTH_SHORT).show();
    		return false;
    	case ErrorEntry.EXCEPTION_OTHER:
    		Toast.makeText(this, "其他异常！", Toast.LENGTH_SHORT).show();
    		return false;
		default:
			return true;
    	}
    }
    
    /**较检腾讯错误码*/
    private void checkTencentErrorCode(){
    	
    }
    
    /**
     * 处理底层消息的内部类
     * */
    private Handler handler=new Handler()
    {
    	public void handleMessage(Message msg)
    	{
        	if(checkTaskResultCode((Task)msg.obj))
    		((Updateable)currentActivity).update(msg.what,msg.obj);
    	}
    };
}
