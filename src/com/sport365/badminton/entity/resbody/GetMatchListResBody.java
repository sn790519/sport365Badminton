package com.sport365.badminton.entity.resbody;

import com.sport365.badminton.entity.obj.MatchEntityObj;
import com.sport365.badminton.entity.obj.SportAdvertismentObj;

import java.io.Serializable;
import java.util.ArrayList;

/**
 */
public class GetMatchListResBody implements Serializable {

	public PageInfo pageInfo = new PageInfo();
	public String totalCount;
	public ArrayList<MatchEntityObj> matchTabEntity = new ArrayList<MatchEntityObj>();

	public ArrayList<SportAdvertismentObj> matchAdvertismentList = new ArrayList<SportAdvertismentObj>();// 广告

}
