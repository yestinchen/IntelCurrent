package com.kernel.intelcurrent.model;

/**用于描述申请的错误情况*/
public class ErrorEntry {
	
	/*=====以下定义的值为exception字段，表达执行时抛出的exception=====*/
	public static final int EXCEPTION_UNKNOWN_HOST = 0x00f0;
	public static final int EXCEPTION_SSL = 0x00f1;
	public static final int EXCEPTION_CONNECT_TIME_OUT = 0x00f2;
	public static final int EXCEPTION_JSON = 0x00f3;
	public static final int EXCEPTION_OTHER = 0x00ff;
	/*=====以上定义的值为exception字段，表达执行时抛出的exception=====*/
	
	public int ret;
	public int errorCode;
	public String detail;
	public int platform;
	public int exception = 0;
	@Override
	public String toString() {
		return "ErrorEntry [ret=" + ret + ", errorCode=" + errorCode
				+ ", detail=" + detail + ", platform=" + platform
				+ ", exception=" + exception + "]";
	}
	
	
}
