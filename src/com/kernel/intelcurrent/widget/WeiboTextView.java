package com.kernel.intelcurrent.widget;

import com.kernel.intelcurrent.util.TextToSpannable;

import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.widget.TextView;

/**用来展示微博内容的富文本组件
 * @author sheling*/
public class WeiboTextView extends TextView {

	private TextToSpannable tts;
	private Context context;
	
	public WeiboTextView(Context context) {
		super(context);
		this.context = context;
	}
	
	public WeiboTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}
	
	public WeiboTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}
	
	
	public void setText(String text){
		tts = new TextToSpannable(text, context);
		super.setText(tts.parse());
		super.setMovementMethod(LinkMovementMethod.getInstance());
	}

}
