package com.sport365.badminton.entity.resbody;

import com.sport365.badminton.entity.obj.*;

import java.io.Serializable;
import java.util.ArrayList;

/**
 */
public class GetClubInfoByidResBody implements Serializable {
	// 活动的列表
	public ArrayList<ActiveEntityObj> activeList = new ArrayList<ActiveEntityObj>();
	// 活动的列表
	public ArrayList<VenueEntityObj> venueList = new ArrayList<VenueEntityObj>();
	// 比赛列表
	public ArrayList<MatchEntityObj> matchList = new ArrayList<MatchEntityObj>();

	public ArrayList<SportAdvertismentObj> clubAdvertismentList = new ArrayList<SportAdvertismentObj>();// 首页顶部广告

}
