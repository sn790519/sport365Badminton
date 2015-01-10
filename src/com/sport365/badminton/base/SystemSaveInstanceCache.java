package com.sport365.badminton.base;

import java.io.Serializable;

/**
 * 被回收的时候，保存的系统级的静态变量的缓存
 * 
 * @author lh4698
 * 
 */
public class SystemSaveInstanceCache implements Serializable {
	private static SystemSaveInstanceCache sSingleton = null;

	public static synchronized SystemSaveInstanceCache getInstance() {
		if (sSingleton == null) {
			sSingleton = new SystemSaveInstanceCache();
		}
		return sSingleton;
	}

	private SystemSaveInstanceCache() {

	}

	// 大首页的静态变量的缓存
	// systemConfig下的缓存对象
	public String memberId;
	public String userName;
	public String trueName;
	public String userId;
	public String socialType;
	public String mobile;
	public String IP;
	public boolean isLogin;
	public String deviceId;
	public String pushInfo;
	public double latitude = 0;
	public double longitude = 0;

}
