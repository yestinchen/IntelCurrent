package com.kernel.intelcurrent.activity;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import com.kernel.intelcurrent.model.OAuthManager;
import com.tencent.weibo.oauthv2.OAuthV2;
import com.tencent.weibo.oauthv2.OAuthV2Client;
import com.tencent.weibo.webview.OAuthV2AuthorizeWebView;
import com.weibo.net.DialogError;
import com.weibo.net.Weibo;
import com.weibo.net.WeiboDialogListener;
import com.weibo.net.WeiboException;
import com.weibo.net.WeiboParameters;

public class LoginActivity extends Activity implements WeiboDialogListener{
	
	private static final String TAG = LoginActivity.class.getSimpleName();
	
	private TextView t_weibo,s_weibo;
	private OAuthV2 t_oauth;
	private OAuthManager authManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		authManager = OAuthManager.getInstance();
		findViewById();
		init();
		setListener();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	       if (requestCode==2) {
	            if (resultCode==OAuthV2AuthorizeWebView.RESULT_CODE)    {
	                OAuthV2 oAuth=(OAuthV2) data.getExtras().getSerializable("oauth");
	                if(oAuth.getStatus()==0)
	                {
	                    Toast.makeText(getApplicationContext(), "登陆成功", Toast.LENGTH_SHORT).show();
	                    Map<String, String> map=new HashMap<String, String>();
	                    map.put(OAuthManager.TENCENT_ACCESS_TOKEN,oAuth.getAccessToken());
	                    map.put(OAuthManager.TENCENT_OPEN_ID, oAuth.getOpenid());
	                    map.put(OAuthManager.TENCENT_OPEN_KEY, oAuth.getOpenkey());
	                    map.put(OAuthManager.TENCENT_EXPIRES_IN, oAuth.getExpiresIn());
	                    map.put(OAuthManager.TENCENT_ACCESS_TOKEN_START_TIME,System.currentTimeMillis()+"");
	                    OAuthManager.getInstance().setOAuthKey(this, OAuthManager.TENCENT_PLATFORM, map);
	                    Log.v("openid", oAuth.getOpenid());
	                    Log.v("openkey", oAuth.getOpenkey());
	                    Log.v("access_token", oAuth.getAccessToken());
	                }
	            } 
	        }
	}
	
	private void findViewById(){
		t_weibo=(TextView)findViewById(R.id.tencent_weibo);
		s_weibo=(TextView)findViewById(R.id.sina_weibo);
	}
	private void setListener(){
		//腾讯微博的按钮监听
		t_weibo.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				int result = authManager.oAuthCheck(LoginActivity.this);
				if(result == OAuthManager.RESULT_BOTH_AVALIABLE ||
					result == OAuthManager.RESULT_ONLY_TENCENT_AVALIABLE){
					Toast.makeText(LoginActivity.this, "已经登陆!直接跳转到主界面.", Toast.LENGTH_SHORT).show();
				}else{
					Intent intent=new Intent(LoginActivity.this,OAuthV2AuthorizeWebView.class);
					intent.putExtra("oauth", t_oauth);
					startActivityForResult(intent, 2);
				}
			}
		});
		s_weibo.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				int result = authManager.oAuthCheck(LoginActivity.this);
				if(result == OAuthManager.RESULT_ONLY_SINA_AVALIABLE ||
						result == OAuthManager.RESULT_BOTH_AVALIABLE){
					Toast.makeText(LoginActivity.this, "已经登陆!直接跳转到主界面.", Toast.LENGTH_SHORT).show();
				}else{
					Weibo weibo = Weibo.getInstance();
					weibo.setupConsumerConfig(OAuthManager.SINA_CUSTOMER_ID, OAuthManager.SINA_CUSTOMER_KEY);
					weibo.setRedirectUrl(OAuthManager.SINA_REDIRECT_URI);
					weibo.authorize(LoginActivity.this,LoginActivity.this);
				}
			}
		});
	}
	private void init(){
		
		//腾讯微博的OAuth的实例		
		t_oauth=new OAuthV2(OAuthManager.TENCENT_REDIRECT_URI);
		t_oauth.setClientId(OAuthManager.TENCENT_CUSTOMER_ID);
		t_oauth.setClientSecret(OAuthManager.SINA_CUSTOMER_KEY);
		OAuthV2Client.getQHttpClient().shutdownConnection();
	}

	@Override
	public void onCancel() {
		
	}

	@Override
	public void onComplete(Bundle bundle) {
		String token = bundle.getString("access_token");
		String expires_in = bundle.getString("expires_in");
		Map<String,String> map = new HashMap<String, String>();
		map.put(OAuthManager.SINA_ACCESS_TOKEN, token);
		map.put(OAuthManager.SINA_EXPIRES_IN, expires_in);
		map.put(OAuthManager.SINA_UID,bundle.getString("uid"));
		map.put(OAuthManager.SINA_ACCESS_TOKEN_START_TIME, System.currentTimeMillis()+"");
		authManager.setOAuthKey(LoginActivity.this, OAuthManager.SINA_PLATFORM, map);
	}

	@Override
	public void onError(DialogError error) {
		Log.e(TAG, error.toString());
	}

	@Override
	public void onWeiboException(WeiboException e) {
		e.printStackTrace();
	}
}
