package com.kernel.intelcurrent.model;

import java.util.ArrayList;
import java.util.LinkedList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.kernel.intelcurrent.db.DataBaseHelper;


/**
 * 用于直接操纵数据库的类
 * 封装有所有需要与数据库交互的方法
 * @author sheling*/
public class DBModel {

	private static final String TAG = DBModel.class.getSimpleName();
	
	private DBModel(){};
	
	private static class Holder{
		private static DBModel model = new DBModel();
	}
	
	public static DBModel getInstance(){
		return Holder.model;
	}
	/**
	 * 添加组
	 * */
	public void addGroup(Context context,Group group){
		String sql1 = "insert into t_group values(?,?)";
		String sql2 = "insert into t_ginfo values(?,?,?)";
		DataBaseHelper helper = new DataBaseHelper(context);
		SQLiteDatabase db = helper.getWritableDatabase();
		String key = group.getName().hashCode()+"";
		db.execSQL(sql1, new String[]{key,group.getName()});
		if(group.users.size()!=0){
			for(SimpleUser user:group.users){
				db.execSQL(sql2, new String[]{key,user.id,user.platform+""});
			}
		}
		db.close();
		helper.close();
	}
	/**
	 * 删除某一个分组
	 * @param context
	 * @param group
	 */
	public void delGroup(Context context,Group group){
		String sql1="delete from t_ginfo where gid="+group.name.hashCode();
		String sql2="delete from t_group where gid="+group.name.hashCode();
		DataBaseHelper helper = new DataBaseHelper(context);
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL(sql1);
		db.execSQL(sql2);
		db.close();
		helper.close();
	}
	/**
	 * 判断某用户是否已经在某个组中
	 * @param context
	 * @param gid	组id
	 * @param user_id	用户openid
	 * @return	存在返回true,否则false;
	 */
	public boolean UserIsExists(Context context,int gid,String user_id){
		boolean flag =false;
		String sql="select userid from t_ginfo where gid="+gid+"and userid="+user_id;
		DataBaseHelper helper=new DataBaseHelper(context);
		SQLiteDatabase db=helper.getReadableDatabase();
		Cursor cursor=db.rawQuery(sql,null);
		while(cursor.moveToNext()){
			if(cursor.getString(cursor.getColumnIndex("userid"))!=null){
				flag=true;
			}
		}
		cursor.deactivate();
		cursor.close();
		db.close();
		helper.close();
		return flag;
	}
	/**
	 * 批量添加用户到组
	 * @param gid 组的id
	 * @param users 用户的openid和platform(腾讯为1)
	 */
	public void addUsers(Context context,int gid,LinkedList<SimpleUser> users){
		String sql = "insert into t_ginfo values("+gid+",?,?)";
		DataBaseHelper helper=new DataBaseHelper(context);
		SQLiteDatabase db=helper.getWritableDatabase();
		if(users.size()!=0){
			for(SimpleUser user:users){
				db.execSQL(sql, new String[]{user.id,user.platform+""});
			}
		}
		db.close();
		helper.close();
	}
	/**
	 * 根据组id判断组是否已经存在，存在返回true,不存在返回false;
	 */
	public boolean GoupIsExists(Context context,int gid){
			boolean flag=false;
			String sql="select gname from t_group where gid=?";
			DataBaseHelper helper=new DataBaseHelper(context);
			SQLiteDatabase db=helper.getReadableDatabase();
			Cursor cursor=db.rawQuery(sql, new String[]{gid+""});
			while(cursor.moveToNext()){
				if(cursor.getString(cursor.getColumnIndex("gname"))!=null){
					flag=true;
				}
			}
			cursor.deactivate();
			cursor.close();
			db.close();
			helper.close();
			return flag;
	}
	public ArrayList<SimpleUser> getUsersByGname(Context context,String gname){
		ArrayList<SimpleUser> users=new ArrayList<SimpleUser>();
			String sql="select userid,platform from t_ginfo where gid="+gname.hashCode();
			DataBaseHelper helper=new DataBaseHelper(context);
			SQLiteDatabase db=helper.getReadableDatabase();
			Cursor cursor=db.rawQuery(sql, null);
			while(cursor.moveToNext()){
				SimpleUser user=new SimpleUser();
				user.id = cursor.getString(cursor.getColumnIndex("userid"));
				user.platform = cursor.getInt(cursor.getColumnIndex("platform"));
				users.add(user);
			}
			cursor.deactivate();
			cursor.close();
			db.close();
			helper.close();
		return users;
	}
	/**
	 * 获取所有的组的组名
	 * @param context
	 * @return
	 */
	public ArrayList<Group> getAllGroups(Context context){
			String sql="select gname from t_group";
			DataBaseHelper helper=new DataBaseHelper(context);
			SQLiteDatabase db=helper.getReadableDatabase();
			Cursor cursor=db.rawQuery(sql, null);
			ArrayList<Group> groups=new ArrayList<Group>();
			while(cursor.moveToNext()){
				Group group=new Group();
				group.setName(cursor.getString(cursor.getColumnIndex("gname")));
				groups.add(group);
			}
			cursor.deactivate();
			cursor.close();
			db.close();
			helper.close();
			return groups;
	}
	/**
	 * 获取组列表
	 * @return ArrayList<Group>*/
	public ArrayList<Group> getGroupList(Context context){
		String sql = "select t_ginfo.gid id,t_group.gname gname," +
				"t_ginfo.userid userid,t_ginfo.platform platform from t_group,t_ginfo" +
				" where t_group.gid = t_ginfo.gid order by t_ginfo.gid";
		DataBaseHelper helper = new DataBaseHelper(context);
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, null);
		ArrayList<Group> groups  = new ArrayList<Group>();
		Group group = null;
		while(cursor.moveToNext()){
			if(group == null){
				group = new Group();
				group.setName(cursor.getString(cursor.getColumnIndex("gname")));
			}else if(!group.getName().equals(
					cursor.getString(cursor.getColumnIndex("gname")))){
				Log.v(TAG, "add group:"+group);
				groups.add(group);
				group = new Group();
				group.setName(cursor.getString(cursor.getColumnIndex("gname")));
			}
			Log.v(TAG, "group:"+group);
			SimpleUser user = new SimpleUser();
			user.id = cursor.getString(cursor.getColumnIndex("userid"));
			user.platform = cursor.getInt(cursor.getColumnIndex("platform"));
			group.addUser(user);
		}
		//加上最后一个
		groups.add(group);
		cursor.deactivate();
		db.close();
		helper.close();
		return groups;
	}

	
	public ArrayList<WeiboDraftEntryDAO> getAllDrafts(Context context){
		String sql = "select content,img,type,created,statusid,platform from t_wb_draft ";
		DataBaseHelper helper = new DataBaseHelper(context);
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, null);
		ArrayList<WeiboDraftEntryDAO> drafts = new ArrayList<WeiboDraftEntryDAO>();
		while(cursor.moveToNext()){
			WeiboDraftEntryDAO draft = new WeiboDraftEntryDAO();
			draft.content = cursor.getString(cursor.getColumnIndex("content"));
			draft.img = cursor.getString(cursor.getColumnIndex("img"));
			draft.type = cursor.getInt(cursor.getColumnIndex("type"));
			draft.created = cursor.getLong(cursor.getColumnIndex("created"));
			draft.statusid = cursor.getString(cursor.getColumnIndex("statusid"));
			draft.platform = cursor.getInt(cursor.getColumnIndex("platform"));
			drafts.add(draft);
		}
		cursor.deactivate();
		db.close();
		helper.close();
		return drafts;
	}
	
	public void removeDraft(Context context,long timestamp){
		String sql = "delete from t_wb_draft where created = ?";
		DataBaseHelper helper = new DataBaseHelper(context);
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL(sql, new String[]{timestamp+""});
		db.close();
		helper.close();
	}
	
	public void addDraft(Context context,WeiboDraftEntryDAO draft){
		String sql = "insert into t_wb_draft values(?,?,?,?,?,?,?)";
		DataBaseHelper helper = new DataBaseHelper(context);
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL(sql, new String[]{draft.userid,draft.content,draft.img,draft.type+"",draft.created+"",draft.statusid,draft.platform+""});
		db.close();
		helper.close();
	}
}
