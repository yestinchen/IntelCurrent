package com.kernel.intelcurrent.activity;

import java.io.IOException;
import java.net.MalformedURLException;

import com.kernel.intelcurrent.widget.MulitPointTouchListener;
import com.kernel.intelcurrent.widget.UrlImageView;
import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
/**
 * 用于查看图片的活动
 * @author sheling*/
public class ImageViewerActivity extends Activity {
	private ImageView view;
	private Matrix matrix = new Matrix();
	private Rect viewRect;
	private ImageButton zoom_in,zoom_out;
	private PointF mid;
	private String url,localImagePath;
	private UrlImageView urlView;
	private static final String TAG = ImageViewerActivity.class.getSimpleName();
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what){
			case 1:
		        center(true, true);
		        break;
			case 2:
				localImagePath = msg.obj.toString();
				urlView.setVisibility(View.GONE);
				urlView.recyle();
				view.setImageBitmap(BitmapFactory.decodeFile(localImagePath));
				final Message mess  = Message.obtain();
				mess.what = 3;
		        handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						handler.handleMessage(mess);
					}
				}, 30);
				break;
			case 3:
				initImagePosition();
				break;
			}
		}
		
	};
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_image_viewer);  
        mid = new PointF();
        findViews();
        init();
        setListener();
    }  
    
    private void init(){
    	Intent intent  = getIntent();
    	url = intent.getStringExtra("url");
    	if(url != null){
    		try {
    			urlView.bindUrl(url, UrlImageView.PLAT_FORM_TENCENT, UrlImageView.LARGE_IMAGE,handler);
    		} catch (MalformedURLException e) {
    			e.printStackTrace();
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    	}
    	
    }
    
    private void findViews(){
    	urlView = (UrlImageView)findViewById(R.id.activity_image_view_urlview);
    	view = (ImageView) findViewById(R.id.activity_image_view_image);  
    	zoom_in = (ImageButton) findViewById(R.id.activity_image_view_ibtn_zoom_in);
    	zoom_out = (ImageButton) findViewById(R.id.activity_image_view_ibtn_zoom_out);
    }
    
    private void setListener(){
    	 view.setOnTouchListener(new MulitPointTouchListener());
    	 //放大
    	 zoom_in.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				matrix.set(view.getImageMatrix());
				setMid();//设置放大的中心
				matrix.postScale(1.3f, 1.3f, mid.x,mid.y);
				view.setImageMatrix(matrix);
				view.invalidate();
				center(true,true);
			}
		});
    	 //缩小
    	 zoom_out.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				matrix.set(view.getImageMatrix());
				setMid();//设置放大的中心
				matrix.postScale(0.8f, 0.8f, mid.x,mid.y);
				view.setImageMatrix(matrix);
				view.invalidate();
				center(true,true);
			}
		});
    }
    private void setMid(){
    	viewRect = view.getDrawable().getBounds();
		mid.x = view.getDrawable().getBounds().centerX();
		mid.y = view.getDrawable().getBounds().centerY();
    }
    
    /**将图片放在中间*/
    public void center(boolean horizontal, boolean vertical) {  
    	viewRect = view.getDrawable().getBounds();
        Matrix m = new Matrix();  
        m.set(matrix);  
        RectF rect = new RectF(0, 0, viewRect.width(), viewRect.height());  
        m.mapRect(rect);  
        float height = rect.height();  
        float width = rect.width();  
        float deltaX = 0, deltaY = 0;  
        if (vertical) {  
            // 图片小于屏幕大小，则居中显示。大于屏幕，上方留空则往上移，下放留空则往下移  
            int screenHeight = view.getHeight();  
            if (height < screenHeight) {  
                deltaY = (screenHeight - height) / 2 - rect.top;  
            } else if (rect.top > 0) {  
                deltaY = -rect.top;  
            } else if (rect.bottom < screenHeight) {  
                deltaY = screenHeight - rect.bottom;  
            }  
        }  
        if (horizontal) {  
            int screenWidth = view.getWidth();  
            if (width < screenWidth) {  
                deltaX = (screenWidth - width) / 2 - rect.left;  
            } else if (rect.left > 0) {  
                deltaX = -rect.left;  
            } else if (rect.right < screenWidth) {  
                deltaX = screenWidth - rect.right;  
            }  
        }  
        Log.d(TAG, "sheight"+view.getHeight()+"ih:"+viewRect.height()+"swidth:"+view.getWidth()+"iw:"+viewRect.width());
        matrix.postTranslate(deltaX, deltaY); 
        view.setImageMatrix(matrix);
		view.invalidate();
    }  
    
    /**初始将图片铺满全屏*/
    private void initImagePosition(){
    	viewRect = view.getDrawable().getBounds();
        Matrix m = new Matrix();  
        m.set(matrix);  
        float widthF = viewRect.width()/(float)view.getWidth();
        float heightF = viewRect.height()/(float)view.getHeight();
        float scaleF = widthF > heightF ? 1/widthF:1/heightF;
        Log.d(TAG, "widthF:"+widthF+"heightF:"+heightF+"scaleF:"+scaleF);
        matrix.postScale(scaleF, scaleF, mid.x,mid.y);
        view.setImageMatrix(matrix);
		view.invalidate();
        center(true,true);
    }
}
