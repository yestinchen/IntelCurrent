package com.kernel.intelcurrent.activity;

import java.util.HashMap;
import java.util.Map;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.kernel.intelcurrent.model.OAuthManager;
import com.tencent.weibo.oauthv2.OAuthV2;
import com.tencent.weibo.oauthv2.OAuthV2Client;
import com.tencent.weibo.webview.OAuthV2AuthorizeWebView;

public class LoginActivity extends Activity{
	
	private OAuthV2 t_oauth;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		init();
		Intent intent=new Intent(LoginActivity.this,OAuthV2AuthorizeWebView.class);
		intent.putExtra("oauth", t_oauth);
		startActivityForResult(intent, 2);
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
	                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
						startActivity(intent);
						this.finish();
	                }
	            } 
	        }
	}
	private void init(){	
		//腾讯微博的OAuth的实例		
		t_oauth=new OAuthV2(OAuthManager.TENCENT_REDIRECT_URI);
		t_oauth.setClientId(OAuthManager.TENCENT_CUSTOMER_ID);
		t_oauth.setClientSecret(OAuthManager.SINA_CUSTOMER_KEY);
		OAuthV2Client.getQHttpClient().shutdownConnection();
	}

}
