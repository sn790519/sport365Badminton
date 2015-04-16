package com.sport365.badminton.entity.resbody;

import java.io.Serializable;
import java.util.ArrayList;

import com.sport365.badminton.entity.obj.ActivityDateObj;

/**
 */
public class GetVenueFieldPriceResBody implements Serializable {
	
	
	public String detaultDate;
	public ArrayList<ActivityDateObj> activeCalendar = new ArrayList<ActivityDateObj>();
}
