package com.kernel.intelcurrent.activity;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class AboutActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		WebView webView=new WebView(this);
		setContentView(webView);
		webView.loadUrl("file:///android_asset/about.html");
		
	}
	
}
