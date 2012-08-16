package com.kernel.intelcurrent.activity;

import android.app.Activity;
import android.opengl.Visibility;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

public class SearchActivity extends Activity implements Updateable{
	private EditText input;
	private TextView title,search_btn;
	private ImageView del_btn;
	private ListView listview;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acitivity_search);
		findViews();
		setListener();
	}
	public void findViews(){
		input=(EditText)findViewById(R.id.search_input);
		title=(TextView)findViewById(R.id.common_head_tv_title);
		title.setText(R.string.search_title);
		search_btn=(TextView)findViewById(R.id.search_btn);
		del_btn=(ImageView)findViewById(R.id.search_del);
		del_btn.setVisibility(View.INVISIBLE);
		listview=(ListView)findViewById(R.id.search_userlist);
	}
	public void setListener(){
		search_btn.setOnClickListener(new OnBtnClickListener());
		del_btn.setOnClickListener(new OnBtnClickListener());
		input.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if(s.length()==0){
					del_btn.setVisibility(View.INVISIBLE);
				}else{
					del_btn.setVisibility(View.VISIBLE);
				}
			
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
			}
		});
	}
	private class OnBtnClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.search_btn:	
				Toast.makeText(SearchActivity.this,input.getText(),Toast.LENGTH_SHORT).show();
				break;
			case R.id.search_del:
				input.setText("");
				break;
			}
		}
		
	}
	@Override
	public void update(int type, Object param) {
		
	}

}
