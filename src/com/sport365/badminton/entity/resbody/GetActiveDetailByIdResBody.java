package com.sport365.badminton.entity.resbody;

import com.sport365.badminton.entity.obj.ActiveEntityObj;
import com.sport365.badminton.entity.obj.ClubTabEntityObj;
import com.sport365.badminton.entity.obj.MatchEntityObj;
import com.sport365.badminton.entity.obj.VenueEntityObj;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 活动返回实体
 * Created by kjh08490 on 2015/3/7.
 */
public class GetActiveDetailByIdResBody implements Serializable {
	// 活动的列表
	public ArrayList<VenueEntityObj> venueList = new ArrayList<VenueEntityObj>();
	// 俱乐部的列表
	public ArrayList<ClubTabEntityObj> clubList = new ArrayList<ClubTabEntityObj>();
	// 比赛列表
	public ArrayList<MatchEntityObj> matchList = new ArrayList<MatchEntityObj>();
}
