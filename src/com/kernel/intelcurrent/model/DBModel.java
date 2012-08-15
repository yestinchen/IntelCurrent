package com.kernel.intelcurrent.model;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.kernel.intelcurrent.db.DataBaseHelper;
import com.weibo.net.Weibo;


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
		for(User user:group.users){
			db.execSQL(sql2, new String[]{key,user.id,user.platform+""});
		}
		db.close();
		helper.close();
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
			User user = new User();
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
		String sql = "select content,type,created,statusid,platform from t_wb_draft ";
		DataBaseHelper helper = new DataBaseHelper(context);
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, null);
		ArrayList<WeiboDraftEntryDAO> drafts = new ArrayList<WeiboDraftEntryDAO>();
		while(cursor.moveToNext()){
			WeiboDraftEntryDAO draft = new WeiboDraftEntryDAO();
			draft.content = cursor.getString(cursor.getColumnIndex("content"));
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
		String sql = "insert into t_wb_draft values(?,?,?,?,?,?)";
		DataBaseHelper helper = new DataBaseHelper(context);
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL(sql, new String[]{draft.userid,draft.content,draft.type+"",draft.created+"",draft.statusid,draft.platform+""});
		db.close();
		helper.close();
	}
}
