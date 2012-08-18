package com.kernel.intelcurrent.modeladapter;

import java.net.UnknownHostException;
import java.util.Map;

import javax.net.ssl.SSLException;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.kernel.intelcurrent.model.ErrorEntry;
import com.kernel.intelcurrent.model.Task;
import com.kernel.intelcurrent.model.User;
import com.weibo.net.AccessToken;
import com.weibo.net.Token;
import com.weibo.net.Weibo;
import com.weibo.net.WeiboParameters;

/**sina Adapter
 * @author sheling*/
public class SinaAdapter extends ModelAdapter {

	private static final String TAG = SinaAdapter.class.getSimpleName();
	
	private Context context;
	
	private ErrorEntry error;
	private Weibo weibo;
	private Token accessToken;
	private Map<String,Object> map;
	
	public SinaAdapter(Context context,Task t) {
		super(t);
		this.context = context;
		map = t.param;
		weibo = (Weibo) map.get("weibo");
		error = new ErrorEntry();
		error.platform = User.PLATFORM_SINA_CODE;
		accessToken = weibo.getAccessToken();
	}

	@Override
	public void run() {
		try{
			switch(task.type){
			case Task.WEIBO_ADD:
				addWeibo();
				break;
			}
		} catch(SSLException e){
			Log.e(TAG, "exception: "+e.toString()+e.getStackTrace());
			error.exception = ErrorEntry.EXCEPTION_SSL;
			e.printStackTrace();
		} catch (ConnectTimeoutException e){
			Log.e(TAG, "exception: "+e.toString()+e.getStackTrace());
			error.exception = ErrorEntry.EXCEPTION_CONNECT_TIME_OUT;
			e.printStackTrace();
		} catch (UnknownHostException e){
			Log.e(TAG, "exception: "+e.toString()+e.getStackTrace());
			error.exception = ErrorEntry.EXCEPTION_UNKNOWN_HOST;
			e.printStackTrace();
		} catch (JSONException e){
			Log.e(TAG, "exception: "+e.toString()+e.getStackTrace());
			e.printStackTrace();
			error.exception = ErrorEntry.EXCEPTION_JSON;
		} catch(Exception e){
			Log.e(TAG, "exception: "+e.toString()+e.getStackTrace());
			e.printStackTrace();
			error.exception = ErrorEntry.EXCEPTION_OTHER;
		}
		
		task.errors.put(error.platform, error);
		model.callBack(task);
	}

	public void addWeibo() throws Exception{
		WeiboParameters params = new WeiboParameters();
		params.add("access_token",accessToken.getToken());
		params.add("status", (String)map.get("content"));
		String pic = (String)map.get("imgurl");
		String response = null;
		if(pic != null){
			Log.d(TAG, "add pic weibo: "+params.toString());
			params.add("pic", pic);
			response = weibo.request(context,"https://upload.api.weibo.com/2/statuses/upload.json",
					params,"POST",accessToken);	
		}else{
			response = weibo.request(context,"https://api.weibo.com/2/statuses/update.json",
					params,"POST",accessToken);	
		}
		JSONObject json = new JSONObject(response);
		if(json.has("error")){
			error.ret= 1;
			error.errorCode = json.getInt("error_code");
			error.detail = json.getString("error");
		}else{
			error.ret = 0;
			//success;
		}
		Log.d(TAG, "response: "+response);
	}
}
