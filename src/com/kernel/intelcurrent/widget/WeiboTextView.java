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
	

	/**设置内容文字
	 * @param text 文字
	 * @param <br>省略了构造参数isLinkAvaiable,默认false
	 * 
	 * */
	public void setText(String text){
		setText(text,false);
	}
	
	/**设置内容文字
	 * @param text 文字
	 *@param isLinkAvaiable 点击链接是否有效
	 * */
	public void setText(String text,boolean isLinkAvaiable){
		tts = new TextToSpannable( text,context);
		super.setText(tts.parse());
		if(isLinkAvaiable){
			super.setMovementMethod(LinkMovementMethod.getInstance());
		}
	}

}
