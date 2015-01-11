package com.sport365.badminton.entity.obj;

import java.io.Serializable;

/**
 * 活动的主题的实体
 */
public class SportThemeObj implements Serializable {

	private static final long serialVersionUID = 1L;

	
	/*{
        "imageUrl": "http://www.17workout.com/Admin/UploadFile/api/zt1.png",
        "title": "我身边",
        "urlType": "0",
        "jumpUrl": ""
    }*/
	
	public String imageUrl;
	public String title;
	public String urlType;
	public String jumpUrl;
	
}
