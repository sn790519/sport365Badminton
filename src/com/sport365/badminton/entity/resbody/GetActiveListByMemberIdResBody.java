package com.sport365.badminton.entity.resbody;

import com.sport365.badminton.entity.obj.ActivityObj;
import com.sport365.badminton.entity.obj.PageInfo;

import java.util.ArrayList;

/**
 * Created by vincent on 2015/3/13.
 */
public class GetActiveListByMemberIdResBody {
	public PageInfo pageInfo;
	public String totalCount;
	public ArrayList<ActivityObj> alctiveList = new ArrayList<ActivityObj>();

}
