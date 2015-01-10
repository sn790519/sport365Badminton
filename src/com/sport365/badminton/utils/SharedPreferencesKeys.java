package com.sport365.badminton.utils;


/**
 * SharedPreferences的key
 */
public class SharedPreferencesKeys {

	// 登录、注册用
	public static final String MEMBERID = "memberId";
	public static final String LOGINNAME = "loginName"; // 登录名
	public static final String USERNAME = "userName"; // 昵称
	public static final String MOBILE = "mobile"; // 手机号码
	public static final String EMAIL = "email"; // 邮编
	public static final String SIGN = "sign"; // 签名
	public static final String PASSWORD = "password"; // 密码
	public static final String AUTHORIZE_CODE = "authorizeCode";// 自动登录的验证码
	public static final String ISBLACK = "isBlack"; // 黑名单
	public static final String TRUENAME = "trueName";
	public static final String SEX = "sex";
	public static final String ISWA = "iswa";
	public static final String SHORTCUT = "false"; // 桌面快捷方式
	public static final String THIRD_ACCOUNT = "thirdaccount";// 记录第三方登录账户关联信息
	public static final String THIRD_SOCIALTYPE = "third_socialtype";// 记录第三方登录账户类型
	public static final String THIRD_MEMBER_ID = "third_member_id";// 记录第三方登录账户的关联同程账号id
	public static final String THIRD_USER_ID = "third_user_id";// 记录第三方登录账户id

	public static final String MYHEADIMG = "myHeadImg";// 头像地址
	public static final String SIGNATURE = "signature";// 签名
	public static final String TIMES = "1";

	// 机场宝典
	public static final String AIRPORTID = "airportId";
	public static final String AIRPORTNAME = "airportName";
	public static final String AIRPORTCODE = "airportCode";
	public static final String AIRPORTTEL = "airporttel";

	public static final String TIP = "tip_";
	public static final String ORDERNAME = "orderName";
	public static final String ORDERPHONE = "orderPhone";
	public static final String ORDERIDCARD = "orderIdCard";

	// 推送用
	public static final String CHANNELID = "channelId"; // 推送渠道id
	public static final String CITYID = "cityId"; // 渠道用的城市id
	public static final String CITYNAME = "cityName"; // 城市名称
	public static final String ISACTIVEPUSH = "isActivePush";
	public static final String USERID = "userId";
	public static final String ACCESSTOKEN = "accessToken";
	public static final String XGPUSHTOKEN = "xGPushToken";//信鸽推送返回的Token值

	// TongChengActivity用
	public static final String STARTSTATION = "startStation"; // 火车出发站
	public static final String ENDSTATION = "endStation"; // 火车到达站
	public static final String DEVICEID = "deviceid"; // mydeviceid->deviceid

	public static final String SAVEFLOW = "saveflow";// 省流量

	// 从接口获取现金劵文案
	public static final String HotelCouponTips_fan = "HotelCouponTips_fan"; // 酒店-返文案
	public static final String HotelCouponTips_jian = "HotelCouponTips_jian"; // 酒店-立减文案

	public static final String SceneryCouponTips_fan = "SceneryCouponTips_fan"; // 景区-返文案
	public static final String SceneryCouponTips_jian = "SceneryCouponTips_jian"; // 景区-立减文案

	// 景点：SelectCitySceneryActivity
	public static final String SCENERYKEYWORD = "SceneryKeyWord"; // 景点关键词
	public static final String SCENERYCITYNAME = "SceneryCityName"; // 景点名称
	public static final String SCENERY_SHARE_TIP = "shareTip";//

	// 火车售票点查询
	public static final String TRAIN_SHENG = "trainsheng";
	public static final String TRAIN_SHI = "trainshi";
	public static final String TRAIN_QU = "trainqu";
	public static final String TRAIN_DISTRICTID = "trainDistrictId";
	public static final String TRAIN_SHENG_ID = "trainshengId";
	public static final String TRAIN_SHI_ID = "trainshiId";
	public static final String TRAIN_SAVED_PASSENGERS = "saved_passengers";
	public static final String TRAIN_OFFLINE_ORDER_PHONE = "offline_order_phone";

	// 更多
	public static final String MORE_FEEDBACK_PHONE = "MoreFeedBackPhone"; // 景点关键词

	// 酒店非登录常用地址
	public static final String HOTELPROVINCEID = "hotelprovinceId";// 省
	public static final String HOTELPROVINCESTRING = "hotelprovinceString";
	public static final String HOTELCITYID = "hotelcityId";// 市
	public static final String HOTELCITYSTRING = "hotelcityString";
	public static final String HOTELCOUNTRYID = "hotelcountryId";// 区
	public static final String HOTELCOUNTRYSTRING = "hotelcountryString";
	public static final String HOTELRECIVERSTREETADDRESS = "hotelreciverStreetAddress";// 街道
	public static final String HOTELRECIVERMOBILENUMBER = "hotelReciverMobileNumber";// 收件人号码
	public static final String HOTELRECIVERNAME = "hotelreciverName";// 收件人姓名
	public static final String HOTELPAYER = "hotelpayer";// 台头

	// 保存酒店关键字筛选信息
	public static final String HOTEL_SELECT_KEY_LABELNAME = "hotel_select_key_labelname";
	public static final String HOTEL_SELECT_KEY_LABELID = "hotel_select_key_labelid";
	public static final String HOTEL_SELECT_KEY_LABELTYPE = "hotel_select_key_labeltype";
	public static final String HOTEL_SELECT_KEY_LATITUDE = "hotel_select_key_latitude";
	public static final String HOTEL_SELECT_KEY_LONGITUDE = "hotel_select_key_longitude";
	public static final String HOTEL_SELECT_KEY_KEYINDEX = "hotel_select_key_keyindex";
	public static final String HOTEL_SELECT_KEY_INDEX = "hotel_select_key_index";
	public static final String HOTEL_SELECT_KEY_LAT = "hotel_select_key_lat";
	public static final String HOTEL_SELECT_KEY_LON = "hotel_select_key_lon";

	public static final String HOTEL_KEYWORDID = "hotel_keywordid";
	public static final String HOTEL_KEYWORDTYPE = "hotel_keywordtype";
	public static final String HOTEL_CITYID = "hotel_cityid";
	public static final String HOTEL_KEYWORDNAME = "hotel_keywordname";
	public static final String HOTEL_CITYNAME_TUANGOU = "hotel_cityname_tuangou";
	public static final String HOTEL_CITYID_TUANGOU = "hotel_cityid_tuangou";

	public static final String HOTEL_COME_DATE = "hotel_come_date";
	public static final String HOTEL_LEAVE_DATE = "hotel_leave_date";

	public static final String HOTEL_PRICE_LEFT_INDEX = "hotel_price_left_index";
	public static final String HOTEL_PRICE_RIGHT_INDEX = "hotel_price_right_index";
	public static final String HOTEL_STAR_INDEX = "hotel_star_index";
	public static final String HOTEL_CHAIN_INDEX = "hotel_chain_index";

	public static final String HOTEL_TUANGOU_OPEN = "hotel_tuangou_open";

	public static final String HOMEIMGURL = "homeImgUrl";
	public static final String HOMEIMGSTARTDATE = "homeImgStartDate";
	public static final String HOMEIMGENDDATE = "homeImgEndDate";

	// 紧急通知栏
	public static final String EMERGENCYNOTIFICATIONTITLE = "emergencyNotificationTitle";

	public static final String HASBEENREAD = "hasBeenRead";

	// 度假出发城市和目的城市
	public static final String VACATION_LEAVE_CITY = "vacationLeaveCity";
	public static final String VACATION_LEAVE_CITYID = "vacationLeaveCityId";
	public static final String VACATION_ARRIVE_CITY = "vacationArriveCity";
	public static final String CRUISE_LEAVE_PORT = "cruiseLeavePort";
	public static final String CRUISE_LEAVE_PORTID = "cruiseLeavePortId";
	public static final String CRUISE_LINE = "cruiseLine";
	public static final String CRUISE_LINEID = "cruiseLineId";
	
	// 邮轮搜索
	public static final String CRUISE_SEARCH_PORT = "cruiseSearchPort";
	public static final String CRUISE_SEARCH_PORTID = "cruiseSearchPortId";
	public static final String CRUISE_SEARCH_LINE = "cruiseSearchLine";
	public static final String CRUISE_SEARCH_LINEID = "cruiseSearchLineId";
	public static final String CRUISE_SEARCH_DATE = "cruiseSearchDate";
	public static final String CRUISE_SEARCH_DATEVALUE = "cruiseSearchDateValue";
	public static final String CRUISE_SEARCH_COMPANY = "cruiseSearchCompany";
	public static final String CRUISE_SEARCH_COMPANYID = "cruiseSearchCompanyId";

	// 版本介绍
	public static final String NEW_FUNTION_INTRO = "newfuntionintro_";

	// 省流量提示框
	public static final String SHOWTIP = "showtip";

	// 数据库版本
	public static final String DATABASE_VERSION_FLIGHT_CITY = "databaseVersionFlightCity";
	public static final String DATABASE_VERSION_HOTEL_CITY = "databaseVersionHotelCity";
	public static final String DATABASE_VERSION_SCENERY_CITY = "databaseVersionSceneryCity";
	public static final String DATABASE_VERSION_TRAVEL_CITY = "databaseVersionTravelCity";
	public static final String DATABASE_VERSION_TRAIN_CITY = "databaseVersionTrainCity";
	public static final String DATABASE_VERSION_TRAIN_NOS = "databaseVersionTrainNos";
	public static final String DATABASE_VERSION_TRAIN_AREA = "databaseVersionTrainArea";
	public static final String DATABASE_VERSION_FLIGHT_INTERNATIONAL_CITY = "databaseVersionFlightInternationalCity";
	// 定位
	public static final String CITY_ID = "city_id";
	public static final String CITY_NAME = "city_name";
	public static final String PROVINCE_NAME = "province_name";

	// 机票查询日期记录
	public static final String FLIGHT_SEARCH_DATE = "flight_search_date";
	public static final String FLIGHT_BACK_SEARCH_DATE = "flight_back_search_date";
	public static final String FLIGHT_SEARCH_HISTORY = "flight_search_history";
	public static final String FLIGHT_INTER_SEARCH_HISTORY = "flight_intersearch_history";

	public static final String HOTEL_STREET_SHOW = "hotel_street_show";

	// 国际机票订单填写页
	public static final String FLIGHT_LINK_EMAIL = "flight_link_email";// 联系人邮箱号

	/** 版本更新 */
	public static final String UPDATE_VERSION = "update_version";

	public static final String USER_MSG_COUNT = "user_msg_count";// 用户消息条数
	public static final String SYSTEM_MSG_COUNT = "system_msg_count";// 系统消息条数
	public static final String USER_MSG_PRE_COUNT = "user_msg_pre_count";// 用户消息先前条数
	public static final String SYSTEM_MSG_PRE_COUNT = "system_msg_pre_count";// 系统消息先前条数

	// h5 web version
	public static final String H5_PACKAGE_VERSION = "h5_package_version_";
	public static final String H5_REPLACED_LATEST_VERSION = "h5_replaced_latest_version_";

	// 机票航班动态查询航班号记录
	public static final String FLIGHT_DYNAMIC_SEARCH_FLIGHTNO = "flight_dynamic_search_flightno";

	// <2014-09-22, SharedPreferences统一，jiagj
	public final static String OLD_SEARCH_KEYS_TRAVEL = "TravelOldSearchKeys";
	public final static String OLD_SEARCH_KEYS_SCENERY = "oldSearchKeys";
	public final static String OLD_SEARCH_KEYS_ONE_SEARCH = "one_search_key";
	public final static String UMS_LOCAL_REPORT_POLICY = "ums_local_report_policy";
	public final static String STATUS = "status";

	// 出境邮轮提示文案
	public final static String VACATION_SUCCESS_TIP = "vacation_success_tip";
	public final static String VACATION_FAILURE_TIP = "vacation_failure_tip";

	// 旅游日历显示配置
	public static final String TRAVEL_CALENDAR_SHOW_HOLIDAY = "travel_calendar_show_holiday";
	public static final String TRAVEL_CALENDAR_SHOW_LUNAR = "travel_calendar_show_lunar";
	public static final String TRAVEL_CALENDAR_SHOW_SOLAR_TERM = "travel_calendar_show_solar_term";
	public static final String TRAVEL_CALENDAR_LIST_GUIDE = "travel_calendar_list_guide";
	public static final String TRAVEL_CALENDAR_MAIN_IMPORT_GUIDE = "travel_calendar_main_import_guide";
	public static final String TRAVEL_CALENDAR_MAIN_PULL_GUIDE = "travel_calendar_main_pull_guide";
	public static final String TRAVEL_CALENDAR_ALARM_MAP = "travel_calendar_alarm_map";
	public static final String TRAVEL_CALENDAR_IMPORT_LIST = "travel_calendar_import_list";
	public static final String TRAVEL_CALENDAR_FIRST_GUIDE = "travel_calendar_first_guide";
	
	// 本地包信息
	public static final String WEBAPP_INFO ="webapp_info";
	
	public static final String FLIGHT_COMPANY_TITLE = "flight_company_title";

	/**
	 * 是否查询连连常用卡，用过一次连连的情况下需要查询
	 */
	public static final String PAY_SEARCH_LIANLIAN_PAY_WAY = "pay_lianlian";
	public static final String PAY_LAST_PAY_WAY = "pay_last_pay_way";//记录支付方式
	public static final String PAY_LAST_PAY_LIANLIAN_NO = "pay_last_lianlian_no";//记录连连常用卡

	public static final String WECHAT_BIND_TIME = "wechat_bind_time";

	//记录上次保存的 Utilities中数据
	public static final String LAST_LATITUDE = "last_latitude";
	public static final String LAST_LONGITUDE = "last_longitude";
	public static final String LAST_CITY_NAME = "last_city_Name";
	public static final String LAST_ADDRESS = "last_address";
	public static final String LAST_DISTRICT = "last_district";
	public static final String LAST_STREET = "last_street";
}
