package com.sport365.badminton.entity.resbody;

import com.sport365.badminton.entity.obj.ActiveEntityObj;
import com.sport365.badminton.entity.obj.SportAdvertismentObj;
import com.sport365.badminton.entity.obj.VenueEntityObj;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by kjh08490 on 2015/2/10.
 */
public class GetAllActiveListResBody implements Serializable{

	public PageInfo pageInfo = new PageInfo();
	public String totalCount;
	public ArrayList<ActiveEntityObj> alctiveList = new ArrayList<ActiveEntityObj>();

	public ArrayList<SportAdvertismentObj> activeAdvertismentList = new ArrayList<SportAdvertismentObj>();// 广告
}
