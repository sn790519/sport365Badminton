package com.sport365.badminton.entity.obj;

import java.io.Serializable;
/**
 * 
 *热词的实体
 */
public class HomeKeyWordObj implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/*{
        "imageUrl": "http://www.17workout.com/Admin/UploadFile/api/venue.png",
        "title": "运动会所",
        "urlType": "0",
        "jumpUrl": "http://www.17workout.com/Admin/UploadFile/api/venue.png"
    }*/
	public String imageUrl;
	public String title;
	public String urlType;
	public String jumpUrl;
}
