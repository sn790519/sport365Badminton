package com.sport365.badminton.entity.webservice;

import com.sport365.badminton.http.json.CacheOptions;

/**
 * 景区接口 枚举
 * 
 * @author cws09147
 * 
 */
public enum SportParameter {
	/**
	 * 会所列表
	 */
	GET_VENUE_LIST("GetVenueList", "VenueHandler.ashx", CacheOptions.NO_CACHE),
	/**
	 * 1.获取365首页接口
	 */
	GET_SPROT_HOME("getsprothome", "AdministrativeDivisionsHandler.ashx", CacheOptions.USE_CACHE);

	private final String mServiceName;
	private final String mServiceAction;
	private final int mCache;

	private SportParameter(String serviceName, String serviceAction, int cache) {
		this.mServiceName = serviceName;
		this.mServiceAction = serviceAction;
		this.mCache = cache;
	}

	public String getServiceName() {
		return mServiceName;
	}

	public String getServiceAction() {
		return mServiceAction;
	}

	public int getCacheOptions() {
		return mCache;
	}
}