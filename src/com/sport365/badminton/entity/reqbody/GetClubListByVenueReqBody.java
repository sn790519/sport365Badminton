package com.sport365.badminton.entity.reqbody;

/**
 * 俱乐部列表
 *
 * @author Frank
 */
public class GetClubListByVenueReqBody {

	public String page;
	public String pageSize;
	public String provinceId;
	public String cityId;
	public String countyId;
	public String venueId;// 拓展
	public String venueName;// 拓展

	public String clubName;// 关键字搜索
}
