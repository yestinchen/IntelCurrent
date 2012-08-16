package com.kernel.intelcurrent.model;
/**用于存取weibo条目的对象*/
public class WeiboDraftEntryDAO {
	public String userid;
	public String content;
	public String img;
	public int type;
	public long created;
	public String statusid;
	public int platform;
	@Override
	public String toString() {
		return "WeiboDraftEntryDAO [userid=" + userid + ", content=" + content
				+ ", img=" + img + ", type=" + type + ", created=" + created
				+ ", statusid=" + statusid + ", platform=" + platform + "]";
	}
}
