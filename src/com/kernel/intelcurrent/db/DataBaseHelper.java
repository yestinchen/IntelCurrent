package com.kernel.intelcurrent.db;

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
	
	public DataBaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String createSql_1 = "create table t_group( gid varchar(255), gname varchar(255) );";
		String createSql_2 = "create table t_ginfo( gid varchar(255), userid varchar(255), platform int );";
		db.execSQL(createSql_1);
		db.execSQL(createSql_2);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
