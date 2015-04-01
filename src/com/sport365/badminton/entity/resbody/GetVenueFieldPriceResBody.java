package com.sport365.badminton.entity.resbody;

import java.io.Serializable;
import java.util.ArrayList;

import com.sport365.badminton.entity.obj.ActivityDateObj;

/**
 * Created by kjh08490 on 2015/3/7.
 */
public class GetVenueFieldPriceResBody implements Serializable {
	
	
	public String detaultDate;
	public ArrayList<ActivityDateObj> activeCalendar = new ArrayList<ActivityDateObj>();
}
