package com.kernel.intelcurrent.util;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.kernel.intelcurrent.activity.OtherUserInfoActivity;
import com.kernel.intelcurrent.activity.R;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

/**从普通文本转为富文本的类
 * @author sheling*/
public class TextToSpannable {
	
	private static final int NORMAL = 1;
	private static final int URL = 2;
	private static final int URL_HTML = 3;
	private static final int EMOTION_TENCENT = 4;
	private static final int EMOTION_SINA = 5;
	private static final int AT = 6;
	private static final int HUA_TI = 7;
	
	//默认状态，正常状态
	private int state = NORMAL;
	
	private static String TAG = TextToSpannable.class.getSimpleName();
	private String text;
	private SpannableStringBuilder spannableSB;
	private Context context;
	
	private int start;
	private int end;
	
	private int contentStart;
	private int htmlEndTmp;
	int offset = 0;
	
	public TextToSpannable(String text,Context context){
		spannableSB = new SpannableStringBuilder(text);
		this.text = text;
		this.context = context;
		start = 0;
		end = text.length();
	}
	
	public String getText(){
		return text;
	}
	
	/**
	 * 调用以开始解析，将普通字符串转为富文本
	 * @return SpannableStringBuilder
	 * @author sheling*/
	public SpannableStringBuilder parse(){
		for(;start != end;start++){
//			Log.d(TAG, "start:"+text.substring(start));
			switch(state){
			case NORMAL:
				if(text.startsWith("[",start)){
					state = EMOTION_SINA;
					contentStart = start;
				}else if(text.startsWith("/",start)){
					state = EMOTION_TENCENT;
					contentStart  = start;
//					start++;//不考虑当前表情开始位置的//
				}else if(text.startsWith("@",start)){
					state = AT;
					contentStart = start;
				}else if(text.startsWith("http://", start)){
					state = URL;
					contentStart = start;
					//加速循环
					start += 6;
				}else if(text.startsWith("<a",start)){
					state = URL_HTML;
					contentStart = start;
				}else if(text.startsWith("#",start)){
					state = HUA_TI;
					contentStart = start;
				}
				
				break;
			case EMOTION_SINA:
				if(text.startsWith("]", start) || text.startsWith(" ", start) || start+1 == end){
					state = NORMAL;
					buildSinaEmotion(contentStart,start+1);
				}
				break;
			case EMOTION_TENCENT:
				//必须用正则表达式匹配
				//另外要判断是否长度已经超过3了（加上\为4），如果超过了，果断结掉
				if(text.substring(start, start+1).matches("[^\u4e00-\u9fa5a-zA-Z]") || start-contentStart>3){
					state = NORMAL;
					buildTencentEmotion(contentStart,start);
					start--;//退回前一步，看当前位置是否有表情
				}else if(start+1 == end){
					state = NORMAL;
					buildTencentEmotion(contentStart,start+1);
				}
				break;
			case AT:
				if(text.substring(start, start+1).matches("[^\u4e00-\u9fa5a-zA-Z0-9-_]")){
					state = NORMAL;
					buildAt(contentStart,start);
				}else if(start+1 == end){
					state = NORMAL;
					buildAt(contentStart,start+1);
				}
				break;
			case URL:
				if(text.startsWith(" ",start) || text.startsWith(":",start)){
					state = NORMAL;
					buildUrl(contentStart,start);
				}else if(start+1 == end){
					buildUrl(contentStart,end);
				}
				break;
			case URL_HTML:
				if(text.startsWith(">",start)){
					htmlEndTmp = start+1;
				}else if(text.startsWith("</a>",start)){
					state = NORMAL;
					buildHtmlUrl(contentStart,start+4);
					//加速循环
					start +=3;
				}
				break;
			case HUA_TI:
				if(text.startsWith("#",start)){
					state = NORMAL;
					buildHuati(contentStart,start+1);
				}
				break;
			default:
				break;
			}
		}
		return spannableSB;
	}
	/**根据起始位置渲染表情*/
	private void buildSinaEmotion(int start ,int end){
		//根据表情是否正确结束来分类处理
		String subStr = text.substring(start, end);
		if(subStr.endsWith("]"))
			;//TODO  build sina emotion...
		
	}
	
	private void buildTencentEmotion(int start,int end){
		Log.v(TAG, "substr:"+text.substring(start,end));
		//循环查找表情，直道表情找到，build。跳出
		while(end != start+1){
			int resid = ICEmotions.getTencentEmotion(text.substring(start+1,end));
			if(resid!= -1){
				buildEmotion(start,end,resid);
				break;
			}
			end--;
		}
	}
	
	/**
	 * 根据起始位置以及表情资源id构建表情文字
	 * @author sheling
	 * */
	private void buildEmotion(int start,int end,int resid){
		//根据表情是否正确结束来分类处理
		Drawable drawable = context.getResources().getDrawable(resid);
//		drawable.setBounds(0, 0, drawable.getIntrinsicWidth()*2, drawable.getIntrinsicHeight()*2);
		drawable.setBounds(0,0,32,32);
    	ImageSpan is = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
    	spannableSB.setSpan(is, start-offset, end-offset, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
	}
	
	/**根据起始位置渲染 @
	 * 点击可跳转另一个Activity
	 * @param start 开始点
	 * @param start 结束点*/
	private void buildAt(int start,int end){
		final String subString = text.substring(start,end);
		OnClickListener listener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(context, subString.substring(1), Toast.LENGTH_SHORT).show();
				Intent intent = new Intent();
				intent.setClass(context, OtherUserInfoActivity.class);
				intent.putExtra("user_name",subString.substring(1));
//				Bundle bundle = new Bundle();
//				bundle.putString("link",subString);
//				intent.putExtra("ext", bundle);
				context.startActivity(intent);
				Log.v(TAG, "clicked");
			}
		};
    	spannableSB.setSpan(new ClickableString(listener),start-offset, end-offset, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
	}
	/**根据起始位置渲染url*/
	private void buildUrl(int start,int end){
		spannableSB.setSpan(new MyUrlSpan(text.substring(start,end)),
				start-offset, end-offset, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
	}
	
	private void buildHtmlUrl(int start,int end){
		if(end > text.length()) return;
		String showString = text.substring(htmlEndTmp, end-4);
		Log.d(TAG, "subStr:"+showString);
		spannableSB.setSpan(new MyUrlSpan(showString), start-offset, end-offset, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		int orginLength = spannableSB.length();
		spannableSB = spannableSB.replace(start-offset, end-offset, showString);
		offset += orginLength - spannableSB.length();
		Log.d(TAG, "org:"+orginLength+"now:"+spannableSB.length());
	}
	
	private void buildHuati(int start,int end){
		final String subString = text.substring(start+1,end-1);
		OnClickListener listener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(context, subString, Toast.LENGTH_SHORT).show();
//				Intent intent = new Intent();
//				intent.setClass(context, com.demo.weibolistdemo.AnotherActivity.class);
//				Bundle bundle = new Bundle();
//				bundle.putString("link",subString);
//				intent.putExtra("ext", bundle);
//				context.startActivity(intent);
				Log.v(TAG, "clicked");
			}
		};
    	spannableSB.setSpan(new ClickableString(listener),start-offset, end-offset, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
	}
	
	
	/**可点击的String
	 * */
	class ClickableString extends ClickableSpan implements OnClickListener{

		private final OnClickListener listener;
    	
    	public ClickableString(OnClickListener l){
    		this.listener = l;
    	}
    	
		@Override
		public void onClick(View widget) {
			listener.onClick(widget);
		}

    	@Override
		public void updateDrawState(TextPaint ds) {
			super.updateDrawState(ds);
			ds.setUnderlineText(false);
			ds.setColor(context.getResources().getColor(R.color.weibo_title_blue));
		}
    }
	
	/**url span*/
	class MyUrlSpan extends URLSpan{

		public MyUrlSpan(String url) {
			super(url);
		}

		@Override
		public void updateDrawState(TextPaint ds) {
			super.updateDrawState(ds);
			ds.setUnderlineText(false);
			ds.setColor(context.getResources().getColor(R.color.weibo_title_blue));
		}
	}
	
}
