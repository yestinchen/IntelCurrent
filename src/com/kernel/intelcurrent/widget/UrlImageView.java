package com.kernel.intelcurrent.widget;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.kernel.intelcurrent.activity.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
/**@author sheling
 * 用于自动从互联网获取图片并显示
 * 目前两个平台：新浪和腾讯 
 * 新浪图片地址指明了图片的大小，传入图片大小的参数是为了确定放入的文件夹
 * 腾讯传入的是腾讯给定的地址，指定图片大小后组件会自动申请不同大小的图片，并存入不同的文件
 * */

public class UrlImageView extends RelativeLayout {
	private static final String LOG_TAG = "URLImageView";
	/*--对图片大小的三个定义--*/
	public static final int SMALL_IMAGE = 1;
	public static final int MIDDLE_IMAGE = 2;
	public static final int LARGE_IMAGE = 3;
	
	/*---对头像大小的两个定义----*/
	public static final int SMALL_HEAD = 4;
	public static final int LARGE_HEAD = 5;
	
	/*---对存储的文件夹的定义---*/
	public static final String TEMP_STORGE_PATH_DIR = "/icfiles/img/";
	public static final String TENCENT_IMG_FILES = "tencent/";
	public static final String SINA_IMG_FILES = "sina/";
	public static final String SMALL_IMAGE_FILES = "small/";
	public static final String MIDDLE_IMAGE_FILES = "middle/";
	public static final String LARGE_IMAGE_FILES = "large/";
	public static final String SMALL_HEAD_FILES = "heads/";
	public static final String LARGE_HEAD_FILES = "headl/";
	
	/*---对腾讯的不同大小图片的申请---*/
	public static final String TENCENT_IMAGE_LARGE = "/2000";
	public static final String TENCENT_IMAGE_MIDDLE = "/460";
	public static final String TENCENT_IMAGE_SMALL = "/160";
	/*---对腾讯的不同大小头像的申请---*/
	public static final String TENCENT_HEAD_LARGE = "/100";
	public static final String TENCENT_HEAD_SMALL = "/50";
	
	/*---对不同平台的定义---*/
	public static final String PLAT_FORM_TENCENT = "腾讯微博";
	public static final String PLAT_FORM_SINA = "新浪微博";
	
	private Bitmap bitmap;
	private Context context;
	public ImageView imageView;
	private ProgressBar progressBar;
	/**存储路径*/
	private String storePath = Environment.getExternalStorageDirectory() + TEMP_STORGE_PATH_DIR;
	/**图片的网络路径*/
	private String urlImagePath = null;
	/**图片存储名称*/
	private String imageName = null;
	/**图片后缀名*/
	private static String suffix = ".cache";
	
	private Handler extraHandler;
	public UrlImageView(Context context) {
		super(context);
		this.context = context;
		init();
	}
	
	public UrlImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	public UrlImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init();
	}
	
	public Bitmap getBitmap(){
		return bitmap;
	}
	
	public String getImagePath(){
		if(storePath.endsWith(suffix))
			return storePath;
		else 
			return null;
	}

	public void reset(){
		imageView.setVisibility(View.GONE);
		progressBar.setVisibility(View.VISIBLE);
		progressBar.setProgress(0);
	}
	
	public void recyle(){
		bitmap.recycle();
		System.gc();
	}
	
	/**初始化布局信息*/
	private void init(){
		LayoutInflater.from(context).inflate(R.layout.widget_url_image_view,this,true);
		imageView = (ImageView) findViewById(R.id.widget_url_imageview_imageView);
		progressBar = (ProgressBar) findViewById(R.id.widget_url_imageview_processBar);
		
	}

	/**绑定地址
	 * @param url url
	 * @param paltform 平台，常量定义 PLAT_FORM_TENCENT PLAT_FORM_SINA
	 * @param size 图片大小，常量定义  image:SMALL_IMAGE,LARGE_IMAGE,MIDDLE_IMAGE head:SMALL_HEAD,LARGE_HEAD
	 * @author sheling
	 * @throws IOException 
	 * @throws MalformedURLException */
	public void bindUrl(URL url,String platform,int size) throws MalformedURLException, IOException{
		bindUrl(url.toString(), platform, size);
	}
	
	
	public void bindUrl(String urlString,String platform,int size,Handler extraHanlder) throws MalformedURLException, IOException{
		this.extraHandler = extraHanlder;
		bindUrl(urlString,platform,size);
	}
	/**绑定地址
	 * @param urlString url字符串
	 * @param paltform 平台，常量定义  PLAT_FORM_TENCENT PLAT_FORM_SINA
	 * @param size 图片大小，常量定义 image:SMALL_IMAGE,LARGE_IMAGE,MIDDLE_IMAGE head:SMALL_HEAD,LARGE_HEAD
	 * @author sheling
	 * @throws IOException 
	 * @throws MalformedURLException */
	public void bindUrl(String urlString,String platform,int size) throws MalformedURLException, IOException{
		//一些初始化，否则listview重用布局后会出错
		storePath = Environment.getExternalStorageDirectory() + TEMP_STORGE_PATH_DIR;
		urlImagePath = null;
		imageName =null;
		
		imageName = urlString.hashCode()+suffix;
		urlImagePath = urlString;
		if(platform == PLAT_FORM_SINA){
			storePath += SINA_IMG_FILES;
			switch(size){
			case SMALL_IMAGE:
				storePath += SMALL_IMAGE_FILES;
				break;
			case MIDDLE_IMAGE:
				storePath += MIDDLE_IMAGE_FILES;
				break;
			case LARGE_IMAGE:
				storePath += LARGE_IMAGE_FILES;
				break;
			case SMALL_HEAD:
				storePath += SMALL_HEAD_FILES;
				break;
			case LARGE_HEAD:
				storePath += LARGE_HEAD_FILES;
				break;
			}
		}else if(platform == PLAT_FORM_TENCENT){
			storePath += TENCENT_IMG_FILES;
			switch(size){
			case SMALL_IMAGE:
				urlImagePath += TENCENT_IMAGE_SMALL;
				storePath += SMALL_IMAGE_FILES;
				break;
			case MIDDLE_IMAGE:
				urlImagePath += TENCENT_IMAGE_MIDDLE;
				storePath += MIDDLE_IMAGE_FILES;
				break;
			case LARGE_IMAGE:
				urlImagePath += TENCENT_IMAGE_LARGE;
				storePath += LARGE_IMAGE_FILES;
				break;
			case SMALL_HEAD:
				urlImagePath += TENCENT_HEAD_SMALL;
				storePath += SMALL_HEAD_FILES;
				break;
			case LARGE_HEAD:
				urlImagePath += TENCENT_HEAD_LARGE;
				storePath += LARGE_HEAD_FILES;
				break;
			}
		}
		//拼出完整路径
		storePath += imageName;
		bindUrl(new URL(urlImagePath));
		
		Log.v(LOG_TAG, "storePath:"+storePath+";url"+urlImagePath);
	}
	
	/**bind ImageURL
	 * @param url url
	 * @throws IOException */
	private void bindUrl(URL url) throws IOException{
		Log.v(LOG_TAG, "bindURL...");
		DownloadTask dTask = new DownloadTask(context);
	    dTask.execute(url,storePath);
//	    if(!bitmap.isRecycled()){
//	    	bitmap.recycle();
//	    }
	}

	/**
	 * 下载图片并更新显示条的内部类
	 * 弱引用异步任务
	 * @author sheling*/
	class DownloadTask extends WeakAsyncTask<Object, Long, String, Context>{
		public DownloadTask(Context target) {
			super(target);
		}
		
		@Override
		protected String doInBackground(Object... params) {
			// TODO get bitmap and set update process signal
		      /* 取得连接 */
		      HttpURLConnection conn;
		      File tmpFile = null;
		      tmpFile = new File((String) params[1]);
			try {
			      //存在，判断大小是否一致
			      if(tmpFile.exists()){
			    	  //固定的显示一个过程
			    	  publishProgress(80L);
			      }
			      else{
				      conn = (HttpURLConnection) ((URL)params[0]).openConnection();
					  conn.connect();
				      /* 取得返回的InputStream */
				      InputStream is = conn.getInputStream();
				      long size = conn.getContentLength();
				      Log.v(LOG_TAG,"文件大小："+size);
			    	  tmpFile.createNewFile();
				      FileOutputStream fops = new FileOutputStream(tmpFile);
				      int onceSize = 1024 * 4;
				      byte[] buffer = new byte[onceSize];
				      //得到剩余的字节长度
				      int length ,readed = 0;
				      while((length = is.read(buffer)) != -1 ){
				    	  fops.write(buffer, 0, length);
				    	  fops.flush();
				    	  readed += length;
				    	  publishProgress((readed*100)/size);
				      }
			    	  publishProgress(100L);
				      fops.close();
				      is.close();
				      conn.disconnect();
			      }
			} catch (IOException e) {
				e.printStackTrace();
			}
			return tmpFile.toString();
		}
		
		@Override
		protected void onProgressUpdate(Long... values) {
			// TODO update ProgressBar
			progressBar.setProgress(values[0].intValue());
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(String tmpFileStr) {
			// TODO show bitmap
			super.onPostExecute(tmpFileStr);
		     //读取文件
			File tmpFile = new File(tmpFileStr);
		    try {
				bitmap = BitmapFactory.decodeStream(new FileInputStream(tmpFile));
//				Log.v(LOG_TAG, "bitmapPath:"+tmpFileStr);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	      //更新显示
	      progressBar.setVisibility(View.GONE);
	      imageView.setVisibility(View.VISIBLE);
	      imageView.setImageBitmap(bitmap);

	      if(extraHandler != null){
	    	  Message msg = Message.obtain();
	    	  msg.what = 2;
	    	  msg.obj = tmpFileStr;
	    	  extraHandler.handleMessage(msg);
	      }
		}
	}
}
