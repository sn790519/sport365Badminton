package com.sport365.badminton.entity.resbody;

import com.sport365.badminton.entity.obj.ActiveEntityObj;
import com.sport365.badminton.entity.obj.ClubTabEntityObj;
import com.sport365.badminton.entity.obj.MatchEntityObj;
import com.sport365.badminton.entity.obj.SportAdvertismentObj;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by kjh08490 on 2015/3/7.
 */
public class GetVenueDetailByIdResBody implements Serializable {
	// VenueEntityObj
	public String venueId;
	public String name;
	public String logo;
	public String openingTime;
	public String closingTime;
	public String provinceId;
	public String provinceName;
	public String cityId;
	public String cityName;
	public String countyId;
	public String countyName;
	public String longitude;
	public String latitude;
	public String address;
	public String telephone;
	public String sort;
	public String remark;
	public String isValid;
	public String clubNum;
	public String activeNum;
	public String matchNum;
	public String isTop;
	public String isRecommend;
	public String recommendValue;
	public String isCache;

	// 活动的列表
	public ArrayList<ActiveEntityObj> activeList = new ArrayList<ActiveEntityObj>();
	// 俱乐部的列表
	public ArrayList<ClubTabEntityObj> clubList = new ArrayList<ClubTabEntityObj>();
	// 比赛列表
	public ArrayList<MatchEntityObj> matchList = new ArrayList<MatchEntityObj>();

	public ArrayList<SportAdvertismentObj> venueAdvertismentList = new ArrayList<SportAdvertismentObj>();// 首页顶部广告
}
