package com.sport365.badminton.entity.reqbody;

/**
 * 活动的列表请求
 *
 * @author Frank
 */
public class GetAllActiveListReqBody {

	/*ClubId	俱乐部Id	否	Int	俱乐部id
	VenueId	场馆id	否	Int	
	ProvinceId	省份id	否	Int	
	CityId	城市id	否	Int	
	CountyId	区域id	否	Int	
	ActiveDate	活动日期	否	DaTeTime	活动日期，活动日历专用字段
	page	页数	是	Int	
	pagesize	页码	是	int	*/

	public String page;
	public String pageSize;
	public String provinceId;
	public String cityId;
	public String countyId;
	public String activeDate;// 运动日历
	public String clubId;// 俱乐部详情 底部 需要用到（后期）
	public String venueId;// 会所底详情 底部 需要用到（后期）

	public String activeTitle;//关键字搜索


}
