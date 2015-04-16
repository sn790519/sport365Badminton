package com.sport365.badminton.entity.obj;

import java.io.Serializable;

/**
 * 价格日历的实体
 */
public class VenueFieldPrice implements Serializable {
	/*"id":"120832",
			"venueId":"6",
			"fieldId":"15",
			"fieldName":"场地一",
			"orderDate":"2014/9/23 0:00:00",
			"startTime":"2014/9/23 18:00:00",
			"endTime":"2014/9/23 19:00:00",
			"price":"60.00",
			"discountPrice":"50.00",
			"status":"0"*/
	public String venueId;
	public String fieldId;
	public String fieldName;
	public String orderDate;
	public String startTime;
	public String endTime;
	public String price;
	public String discountPrice;
	public String status;

}
