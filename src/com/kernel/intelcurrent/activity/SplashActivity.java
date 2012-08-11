package com.kernel.intelcurrent.activity;

import java.io.File;

import com.kernel.intelcurrent.db.DataBaseHelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;

public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//初始化数据库，如果没有的话
		DataBaseHelper dbHelper = new DataBaseHelper(this);
		dbHelper.getReadableDatabase().close();
		dbHelper.close();

		//新建所需文件夹
		mkDirs();
		
		Intent intent = new Intent(this,MainActivity.class);
		startActivity(intent);
		finish();
	}
	
	private void mkDirs(){
		String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/icfiles";
		createDirIfNotExist(path);
		path += "/img";
		createDirIfNotExist(path);
		String path1 = path +"/sina";
		createDirIfNotExist(path1);
		String path2 = path +"/tencent";
		createDirIfNotExist(path2);

		String path11 = path1 +"/small";
		createDirIfNotExist(path11);
		path11 = path1 +"/middle";
		createDirIfNotExist(path11);
		path11 = path1 +"/large";
		createDirIfNotExist(path11);
		path11 = path1 +"/heads";
		createDirIfNotExist(path11);
		path11 = path1 +"/headl";
		createDirIfNotExist(path11);
		
		String path21 = path2 +"/small";
		createDirIfNotExist(path21);
		path21 = path2 +"/middle";
		createDirIfNotExist(path21);
		path21 = path2 +"/large";
		createDirIfNotExist(path21);
		path21 = path2 +"/heads";
		createDirIfNotExist(path21);
		path21 = path2 +"/headl";
		createDirIfNotExist(path21);
	}
	
	private void createDirIfNotExist(String path){
		File f = new File(path);
		if(f.isDirectory() && f.exists())	return;
		f.mkdir();
	}
	
}
