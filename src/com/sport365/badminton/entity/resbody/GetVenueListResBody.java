package com.sport365.badminton.entity.resbody;

import com.sport365.badminton.entity.obj.SportAdvertismentObj;
import com.sport365.badminton.entity.obj.VenueEntityObj;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 会所的返回实体
 *
 * @author Frank
 */
public class GetVenueListResBody implements Serializable {

	public PageInfo pageInfo = new PageInfo();
	public String totalCount;
	public ArrayList<VenueEntityObj> venueEntity = new ArrayList<VenueEntityObj>();

	public ArrayList<SportAdvertismentObj> venueAdvertismentList = new ArrayList<SportAdvertismentObj>();// 首页顶部广告

}
