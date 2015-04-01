package com.sport365.badminton.entity.webservice;

import com.sport365.badminton.http.json.CacheOptions;

public enum SportParameter {


	/**
	 *获取群活动
	 */
	GET_ACTIVE_LIST_BY_MEMBERID("GetActiveListByMemberId", "ActiveHandler.ashx", CacheOptions.NO_CACHE),
	/**
	 * 修改个人信息
	 */
	MODIFY_MEMBERINFO_BY_ID("ModifyMemberInfoById", "MemberHandler.ashx", CacheOptions.NO_CACHE),
	/**
	 * 获取省份列表
	 */
	GET_PROVINCE_LIST("GetProvinceList", "AdministrativeDivisionsHandler.ashx", CacheOptions.NO_CACHE),
	/**
	 * 获取城市列表
	 */
	GET_CITY_LIST_BY_PROVINCEID("GetCityListByProvinceId", "AdministrativeDivisionsHandler.ashx", CacheOptions.NO_CACHE),
	/**
	 * 修改手机号
	 */
	MODIFY_MEMBER_MOBILE("ModifyMemberMobile", "MemberHandler.ashx", CacheOptions.NO_CACHE),
	/**
	 * 通过手机号码重置密码
	 */
	RESET_PASSWORD_BY_MOBILE("resetpasswordbymobile", "MemberHandler.ashx", CacheOptions.NO_CACHE),
	/**
	 * 发送验证码
	 */
	SEND_MOBILE_CODE("SendMobileCode", "MemberHandler.ashx", CacheOptions.NO_CACHE),
	/**
	 * 注册
	 */
	REGISTER("Register", "MemberHandler.ashx", CacheOptions.NO_CACHE),

	/**
	 * 登录
	 */
	LOGIN("Login", "MemberHandler.ashx", CacheOptions.NO_CACHE),
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
	 * 比赛列表
	 */
	GET_MATCH_LIST("GetMatchList", "MatchHandler.ashx", CacheOptions.NO_CACHE),
	/**
	 * 比赛详情
	 */
	GET_MATCH_DETAIL_BYID("getmatchdetailbyid", "MatchHandler.ashx", CacheOptions.NO_CACHE),
	/**
	 * 支付宝wap支付
	 */
	ALIWAP_PAY("AliWapPay", "PaymentHandler.ashx", CacheOptions.NO_CACHE),
	/**
	 * 支付宝客户端支付
	 */
	ALICLIENT_PAY("AliClientPay", "PaymentHandler.ashx", CacheOptions.NO_CACHE),
	/**
	 * 微信支付
	 */
	WEIXIN_PAY("weixinpay", "PaymentHandler.ashx", CacheOptions.NO_CACHE),
	/**
	 * 价格日历
	 */
	GET_ACTIVE_CALENDARLIST("getactivecalendarlist", "ActiveHandler.ashx", CacheOptions.NO_CACHE),

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