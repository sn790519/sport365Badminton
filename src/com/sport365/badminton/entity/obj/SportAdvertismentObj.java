package com.sport365.badminton.entity.obj;

import java.io.Serializable;

/**
 * 广告的对象
 */
public class SportAdvertismentObj implements Serializable {

	private static final long serialVersionUID = 1L;

	
	/*{
        "noticeUrlType": "",
        "redirectUrl": "",
        "imageName": "广告1",
        "imageUrl": "http://www.17workout.com/Admin/UploadFile/api/test1.png",
        "tag": "",
        "versionBegin": 0,
        "versionEnd": 0,
        "category": 0,
        "provinceId": "",
        "cityId": "",
        "sortIndex": 0,
        "isTop": 0
    }*/
	
	
	public String noticeUrlType;
	public String redirectUrl;
	public String imageName;
	public String imageUrl;
	public String tag;
	public String versionBegin;
	public String versionEnd;
	public String category;
	public String provinceId;
	public String cityId;
	public String sortIndex;
	public String isTop;

}
