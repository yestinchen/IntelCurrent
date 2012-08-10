package com.kernel.intelcurrent.model;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kernel.intelcurrent.db.DataBaseHelper;


/**
 * 用于直接操纵数据库的类
 * 封装有所有需要与数据库交互的方法
 * @author sheling*/
public class DBModel {
	
	
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
		String sql = "select t_ginfo.gid,t_group.gname," +
				"t_ginfo.userid,t_ginfo.platform from t_group,t_ginfo" +
				"where t_group.gid = t_ginfo.gid group by t_ginfo.gid";
		DataBaseHelper helper = new DataBaseHelper(context);
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, null);
		ArrayList<Group> groups  = new ArrayList<Group>();
		Group group = null;
		while(cursor.moveToNext()){
			if(group == null){
				group = new Group();
			}else if(!group.getName().equals(cursor.getString(cursor.getColumnIndex("t_ginfo.gid")))){
				groups.add(group);
				group = new Group();
			}
			User user = new User();
			user.id = cursor.getString(cursor.getColumnIndex("t_ginfo.userid"));
			user.platform = cursor.getInt(cursor.getColumnIndex("t_ginfo.platform"));
			group.addUser(user);
		}
		cursor.deactivate();
		db.close();
		helper.close();
		return groups;
	}
	
}
