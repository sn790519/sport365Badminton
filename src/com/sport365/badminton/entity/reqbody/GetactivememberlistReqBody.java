package com.sport365.badminton.entity.reqbody;

/**
 * 拉去报名的人员的列表
 * 
 * @author Frank
 * 
 */
public class GetactivememberlistReqBody {
	// typeid ＝＝ 0 活动报名中拉去报名的list；activeId

	// typeid ＝＝ 1 比赛中的拉去报名的list；
	public String activeId;
	public String typeId;
}
