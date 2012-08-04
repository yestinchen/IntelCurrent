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

public class LoginActivity extends Activity {
	private TextView t_weibo,s_weibo;
	private OAuthV2 t_oauth;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		findViewById();
		init();
		setListener();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
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
				// TODO Auto-generated method stub
				SharedPreferences spf=LoginActivity.this.getSharedPreferences(OAuthManager.OAUTH_FILE, 0);
				String tencent=spf.getString(OAuthManager.TENCENT_WEIBO, null);//判断是否已经登陆过
				if(tencent==null){	//若为空证明没登陆或过期，重新登陆
					Intent intent=new Intent(LoginActivity.this,OAuthV2AuthorizeWebView.class);
					intent.putExtra("oauth", t_oauth);
					startActivityForResult(intent, 2);
				}else{
					Toast.makeText(LoginActivity.this, "已经登陆!直接跳转到主界面.", 3000).show();
				}			
			}
		});
		s_weibo.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(LoginActivity.this, "现在还未实现！", 3000).show();
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
}
