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
	 * 会所详情页面
	 */
	GET_VENUE_DETAIL_BYID("GetVenueDetailById", "VenueHandler.ashx", CacheOptions.NO_CACHE),
	/**
	 * 活动列表
	 */
	GET_ALL_ACTIVE_LIST("GetAllActiveList", "ActiveHandler.ashx", CacheOptions.NO_CACHE),
	/**
	 * 活动详情
	 */
	GET_ACTIVE_DETAIL_BYID("GetActiveDetailById", "ActiveHandler.ashx", CacheOptions.NO_CACHE),
	/**
	 * 俱乐部列表
	 */
	GET_CLUB_LIST_BYVENUE("GetClubListByVenue", "ClubHandler.ashx", CacheOptions.NO_CACHE),
	/**
	 * 俱乐部详情
	 */
	GET_CLUB_INFO_BYID("GetClubInfoByid", "ClubHandler.ashx", CacheOptions.NO_CACHE),

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