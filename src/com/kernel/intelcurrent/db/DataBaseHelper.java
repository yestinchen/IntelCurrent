package com.kernel.intelcurrent.db;

import com.kernel.intelcurrent.model.User;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 用于打开sqlite数据库类
 * @author sheling
 * */
public class DataBaseHelper extends SQLiteOpenHelper {

	private static final String TAG = DataBaseHelper.class.getSimpleName();
	public static final String DB_FILE = "database";
	
	public DataBaseHelper(Context context){
		super(context,DB_FILE,null,1);
	}
	
	public DataBaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String createSql_1 = "create table t_group( gid varchar(255) primary key, gname varchar(255) );";
		String createSql_2 = "create table t_ginfo( gid varchar(255), userid varchar(255), platform int );";
		String createSql_3 = "create table t_wb_draft(userid text not null,content text not null,img text,type int not null,created int not null,statusid text,platform int not null);";
		db.execSQL(createSql_1);
		db.execSQL(createSql_2);
		db.execSQL(createSql_3);
		insertTestData(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
	
	/**预置测试数据*/
	private void insertTestData(SQLiteDatabase db){
		insertTencentTestData(db);
	}
	
	/**腾讯的分组数据*/
	private void insertTencentTestData(SQLiteDatabase db){
		String sql1 = "insert into t_group values(?,?)";
		String sql2 = "insert into t_ginfo values(?,?,1)";
		db.execSQL(sql1,new String[]{"生活".hashCode()+"","生活"});
		db.execSQL(sql2,new String[]{"生活".hashCode()+"","5E8681F98139420CB194F0D1E4718370"});
		db.execSQL(sql2,new String[]{"生活".hashCode()+"","086FA8A117BCB9C8EBBC3FD4F2FD8376"});
		db.execSQL(sql2,new String[]{"生活".hashCode()+"","43F2B3CB5DA2213AFCDB76F1AC70C569"});
		db.execSQL(sql2,new String[]{"生活".hashCode()+"","428A900FCA135ACBF5811135E0DF1EE4"});
		db.execSQL(sql2,new String[]{"生活".hashCode()+"","89C271210B9B7861332B51E5264E946F"});
		db.execSQL(sql2,new String[]{"生活".hashCode()+"","8491A789BBA1A5B9435C643C8424FDF5"});

		db.execSQL(sql1,new String[]{"财经".hashCode()+"","财经"});
		db.execSQL(sql2,new String[]{"财经".hashCode()+"","82AFF1BC839CA811C0C6B6093262A41E"});
		db.execSQL(sql2,new String[]{"财经".hashCode()+"","D4D61FE9B75C6D2909CE9D61DB03345E"});
		db.execSQL(sql2,new String[]{"财经".hashCode()+"","9D69D933C8F86DC167DC70DFBAA407CA"});
		db.execSQL(sql2,new String[]{"财经".hashCode()+"","6C37CAC0BCBAA5ABC1CFAA2F38A39E56"});
		db.execSQL(sql2,new String[]{"财经".hashCode()+"","89C271210B9B7861332B51E5264E946F"});
		
		db.execSQL(sql1,new String[]{"测试".hashCode()+"","测试"});
		db.execSQL(sql2,new String[]{"测试".hashCode()+"","89C271210B9B7861332B51E5264E946F"});
		

		db.execSQL(sql1,new String[]{"未分组".hashCode()+"","未分组"});
		db.execSQL(sql2,new String[]{"未分组".hashCode()+"","82AFF1BC839CA811C0C6B6093262A41E"});
	}

}
