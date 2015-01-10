package com.sport365.badminton.base;

import java.lang.reflect.Type;

/**
 * 请求数据暂存
 */
public class BaseActivityDataBundle<T, T1> {
	// 显示错误提示内容
	public static String TypeNormal = "1";
	// 不显示错误提示内容
	public static String TypeNoTips = "2";
	// 显示错误提示内容,屏蔽弹出框的关闭功能
	public static String TypeNormalAndNoCancel = "13";
	// 不显示错误提示内容,屏蔽弹出框的关闭功能
	public static String TypeNoTipsAndNoCancel = "23";
	
	private T obj;//返回的对象
	private String[] strParameter;//传递的参数
	private String showTipType = TypeNormal;//显示类型
	private int iShowTip;//进度条显示的资源
	private T1 m;//请求的数据
	private Type type;//返回的类型
	public T getObj() {
		return obj;
	}
	public void setObj(T obj) {
		this.obj = obj;
	}
	public String[] getStrParameter() {
		return strParameter;
	}
	public void setStrParameter(String[] strParameter) {
		this.strParameter = strParameter;
	}
	public int getiShowTip() {
		return iShowTip;
	}
	public void setiShowTip(int iShowTip) {
		this.iShowTip = iShowTip;
	}
	//获取全部ShowTipType值
	public String getShowTipType2() {
		return showTipType;
	}
	//用于tip显示判断
	public String getShowTipType() {
		return showTipType.substring(0, 1);
	}
	public void setShowTipType(String showTipType) {
		this.showTipType = showTipType;
	}
	
	//判断是否需要屏蔽弹框取消功能
	public boolean getIsCancelable() {
		
		return !(getShowTipType2().equals(BaseActivityDataBundle.TypeNormalAndNoCancel)
				|| (getShowTipType2().equals(BaseActivityDataBundle.TypeNoTipsAndNoCancel)));
		
	}
	public T1 getM() {
		return m;
	}
	public void setM(T1 m) {
		this.m = m;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
}
