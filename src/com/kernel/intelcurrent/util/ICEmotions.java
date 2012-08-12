package com.kernel.intelcurrent.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.kernel.intelcurrent.activity.R;

/**
 * 包括所有平台的需要解析的表情
 * 内部存储全为HashMap<String,Integer>
 * key-value对应关系为:表情名-表情资源文件id
 * @author sheling
 * */
public class ICEmotions {

	/**腾讯的表情集合*/
	private static HashMap<String,Integer> tencentEmotions = new HashMap<String, Integer>();
	/**用于新建微博页面的显示表情的较为清楚的表情集合*/
	private static ArrayList<EmotionPair> tencentNewEmotions;
	//静态初始化域(如果有更好的不需要静态的方法最好了)

	private ICEmotions(){};
	
	private static class Holder{
		private static ICEmotions emotions = new ICEmotions();
	}
	
	public static ICEmotions getInstance(){
		return Holder.emotions;
	}
	
	static{
		tencentEmotions.put("调皮",R.drawable.f000);
		tencentEmotions.put("呲牙",R.drawable.f001);
		tencentEmotions.put("惊讶",R.drawable.f002);
		tencentEmotions.put("难过",R.drawable.f003);
		tencentEmotions.put("酷",R.drawable.f004);
		tencentEmotions.put("冷汗",R.drawable.f005);
		tencentEmotions.put("抓狂",R.drawable.f006);
		tencentEmotions.put("吐",R.drawable.f007);
		tencentEmotions.put("偷笑",R.drawable.f008);
		tencentEmotions.put("可爱",R.drawable.f009);
		tencentEmotions.put("白眼",R.drawable.f010);
		tencentEmotions.put("傲慢",R.drawable.f011);
		tencentEmotions.put("微笑",R.drawable.f012);
		tencentEmotions.put("撇嘴",R.drawable.f013);
		tencentEmotions.put("色",R.drawable.f014);
		tencentEmotions.put("发呆",R.drawable.f015);
		tencentEmotions.put("得意",R.drawable.f016);
		tencentEmotions.put("流泪",R.drawable.f017);
		tencentEmotions.put("害羞",R.drawable.f018);
		tencentEmotions.put("嘘",R.drawable.f019);
		tencentEmotions.put("困",R.drawable.f020);
		tencentEmotions.put("尴尬",R.drawable.f021);
		tencentEmotions.put("发怒",R.drawable.f022);
		tencentEmotions.put("打哭",R.drawable.f023);
		tencentEmotions.put("流汗",R.drawable.f024);
		tencentEmotions.put("再见",R.drawable.f025);
		tencentEmotions.put("敲打",R.drawable.f026);
		tencentEmotions.put("擦汗",R.drawable.f027);
		tencentEmotions.put("委屈",R.drawable.f028);
		tencentEmotions.put("疑问",R.drawable.f029);
		tencentEmotions.put("睡",R.drawable.f030);
		tencentEmotions.put("亲亲",R.drawable.f031);
		tencentEmotions.put("憨笑",R.drawable.f032);
		tencentEmotions.put("衰",R.drawable.f033);
		tencentEmotions.put("阴险",R.drawable.f034);
		tencentEmotions.put("奋斗",R.drawable.f035);
		tencentEmotions.put("右哼哼",R.drawable.f036);
		tencentEmotions.put("拥抱",R.drawable.f037);
		tencentEmotions.put("坏笑",R.drawable.f038);
		tencentEmotions.put("鄙视",R.drawable.f039);
		tencentEmotions.put("晕",R.drawable.f040);
		tencentEmotions.put("大兵",R.drawable.f041);
		tencentEmotions.put("可怜",R.drawable.f042);
		tencentEmotions.put("饥饿",R.drawable.f043);
		tencentEmotions.put("咒骂",R.drawable.f044);
		tencentEmotions.put("抠鼻",R.drawable.f045);
		tencentEmotions.put("鼓掌",R.drawable.f046);
		tencentEmotions.put("糗大了",R.drawable.f047);
		tencentEmotions.put("左哼哼",R.drawable.f048);
		tencentEmotions.put("哈欠",R.drawable.f049);
		tencentEmotions.put("快哭了",R.drawable.f050);
		tencentEmotions.put("吓",R.drawable.f051);
		tencentEmotions.put("比嘴",R.drawable.f052);
		tencentEmotions.put("惊恐",R.drawable.f053);
		tencentEmotions.put("示爱",R.drawable.f054);
		tencentEmotions.put("爱心",R.drawable.f055);
		tencentEmotions.put("心碎",R.drawable.f056);
		tencentEmotions.put("蛋糕",R.drawable.f057);
		tencentEmotions.put("闪电",R.drawable.f058);
		tencentEmotions.put("炸弹",R.drawable.f059);
		tencentEmotions.put("刀",R.drawable.f060);
		tencentEmotions.put("足球",R.drawable.f061);
		tencentEmotions.put("瓢虫",R.drawable.f062);
		tencentEmotions.put("便便",R.drawable.f063);
		tencentEmotions.put("咖啡",R.drawable.f064);
		tencentEmotions.put("饭",R.drawable.f065);
		tencentEmotions.put("猪头",R.drawable.f066);
		tencentEmotions.put("玫瑰",R.drawable.f067);
		tencentEmotions.put("凋谢",R.drawable.f068);
		tencentEmotions.put("月亮",R.drawable.f069);
		tencentEmotions.put("太阳",R.drawable.f070);
		tencentEmotions.put("礼物",R.drawable.f071);
		tencentEmotions.put("强",R.drawable.f072);
		tencentEmotions.put("弱",R.drawable.f073);
		tencentEmotions.put("握手",R.drawable.f074);
		tencentEmotions.put("胜利",R.drawable.f075);
		tencentEmotions.put("抱拳",R.drawable.f076);
		tencentEmotions.put("勾引",R.drawable.f077);
		tencentEmotions.put("拳头",R.drawable.f078);
		tencentEmotions.put("差劲",R.drawable.f079);
		tencentEmotions.put("爱你",R.drawable.f080);
		tencentEmotions.put("NO",R.drawable.f081);
		tencentEmotions.put("OK",R.drawable.f082);
		tencentEmotions.put("爱情",R.drawable.f083);
		tencentEmotions.put("飞吻",R.drawable.f084);
		tencentEmotions.put("跳跳",R.drawable.f085);
		tencentEmotions.put("发抖",R.drawable.f086);
		tencentEmotions.put("怄火",R.drawable.f087);
		tencentEmotions.put("转圈",R.drawable.f088);
		tencentEmotions.put("磕头",R.drawable.f089);
		tencentEmotions.put("回头",R.drawable.f090);
		tencentEmotions.put("跳绳",R.drawable.f091);
		tencentEmotions.put("挥手",R.drawable.f092);
		tencentEmotions.put("激动",R.drawable.f093);
		tencentEmotions.put("街舞",R.drawable.f094);
		tencentEmotions.put("献吻",R.drawable.f095);
		tencentEmotions.put("左太极",R.drawable.f096);
		tencentEmotions.put("右太极",R.drawable.f097);
		tencentEmotions.put("菜刀",R.drawable.f098);
		tencentEmotions.put("西瓜",R.drawable.f099);
		tencentEmotions.put("啤酒",R.drawable.f100);
		tencentEmotions.put("骷髅",R.drawable.f101);
		tencentEmotions.put("篮球",R.drawable.f102);
		tencentEmotions.put("乒乓",R.drawable.f103);
		tencentEmotions.put("折磨",R.drawable.f104);
	}
	
	/**添加腾讯表情*/
	private void initTencentNewEmotions() {
		tencentNewEmotions = new ArrayList<ICEmotions.EmotionPair>();
		tencentNewEmotions.add(new EmotionPair(R.drawable.h000,"调皮"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h001,"呲牙"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h002,"惊讶"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h003,"难过"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h004,"酷"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h005,"冷汗"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h006,"抓狂"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h007,"吐"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h008,"偷笑"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h009,"可爱"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h010,"白眼"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h011,"傲慢"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h012,"微笑"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h013,"撇嘴"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h014,"色"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h015,"发呆"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h016,"得意"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h017,"流泪"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h018,"害羞"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h019,"嘘"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h020,"困"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h021,"尴尬"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h022,"发怒"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h023,"打哭"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h024,"流汗"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h025,"再见"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h026,"敲打"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h027,"擦汗"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h028,"委屈"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h029,"疑问"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h030,"睡"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h031,"亲亲"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h032,"憨笑"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h033,"衰"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h034,"阴险"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h035,"奋斗"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h036,"右哼哼"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h037,"拥抱"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h038,"坏笑"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h039,"鄙视"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h040,"晕"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h041,"大兵"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h042,"可怜"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h043,"饥饿"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h044,"咒骂"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h045,"抠鼻"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h046,"鼓掌"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h047,"糗大了"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h048,"左哼哼"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h049,"哈欠"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h050,"快哭了"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h051,"吓"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h052,"比嘴"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h053,"惊恐"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h054,"示爱"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h055,"爱心"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h056,"心碎"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h057,"蛋糕"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h058,"闪电"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h059,"炸弹"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h060,"刀"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h061,"足球"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h062,"瓢虫"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h063,"便便"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h064,"咖啡"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h065,"饭"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h066,"猪头"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h067,"玫瑰"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h068,"凋谢"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h069,"月亮"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h070,"太阳"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h071,"礼物"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h072,"强"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h073,"弱"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h074,"握手"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h075,"胜利"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h076,"抱拳"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h077,"勾引"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h078,"拳头"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h079,"差劲"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h080,"爱你"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h081,"NO"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h082,"OK"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h083,"爱情"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h084,"飞吻"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h085,"跳跳"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h086,"发抖"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h087,"怄火"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h088,"转圈"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h089,"磕头"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h090,"回头"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h091,"跳绳"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h092,"挥手"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h093,"激动"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h094,"街舞"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h095,"献吻"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h096,"左太极"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h097,"右太极"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h098,"菜刀"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h099,"西瓜"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h100,"啤酒"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h101,"骷髅"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h102,"篮球"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h103,"乒乓"));
		tencentNewEmotions.add(new EmotionPair(R.drawable.h104,"折磨"));

	}
	
	public void clearTencentEmotions(){
		tencentNewEmotions.clear();
		tencentNewEmotions = null;
	}
	/**
	 * 根据表情字符串返回表情资源id
	 * @return -1 如果不存在该表情
	 * @author sheling*/
	public static int getTencentEmotion(String emotionStr){
		if(!tencentEmotions.containsKey(emotionStr)) return -1;
		else return tencentEmotions.get(emotionStr);
	}
	
	/**返回初始化完毕 的高清腾讯表情集合HashMap*/
	public List<EmotionPair> getTencentNewEmotions(){
		if(tencentNewEmotions == null)
			initTencentNewEmotions();
		return tencentNewEmotions;
	}
	
	/**定义表情中的表情对，包含资源id以及名称*/
	public class EmotionPair{
		public int resid;
		public String name;
		public EmotionPair(int resid,String name){
			this.resid = resid;
			this.name = name;
		}
	}
}
