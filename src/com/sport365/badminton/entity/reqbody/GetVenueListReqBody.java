package com.sport365.badminton.entity.reqbody;

/**
 * 会所的列表的接口支持经纬度的列表查询请求参数
 * 
 * @author Frank
 * 
 */
public class GetVenueListReqBody {

	public String page;
	public String pagesize;
	public String proviceId;
	public String cityId;
	public String countyId;
	public String lattitude;
	public String lon;

}
