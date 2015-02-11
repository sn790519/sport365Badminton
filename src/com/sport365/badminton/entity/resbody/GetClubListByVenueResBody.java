package com.sport365.badminton.entity.resbody;

import com.sport365.badminton.entity.obj.ActiveEntityObj;
import com.sport365.badminton.entity.obj.ClubTabEntityObj;
import com.sport365.badminton.entity.obj.SportAdvertismentObj;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by kjh08490 on 2015/2/10.
 */
public class GetClubListByVenueResBody implements Serializable {
	public PageInfo pageInfo = new PageInfo();
	public String totalCount;
	public ArrayList<ClubTabEntityObj> clubTabEntity = new ArrayList<ClubTabEntityObj>();

	public ArrayList<SportAdvertismentObj> clubAdvertismentList = new ArrayList<SportAdvertismentObj>();// 广告
}