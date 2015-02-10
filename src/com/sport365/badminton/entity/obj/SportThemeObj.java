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
        "jumpUrl": ""，
        "typeId": "1"
    }*/
	
	public String imageUrl;
	public String title;
	public String urlType;// 0:客户端跳转 1:跳h5
	public String jumpUrl;
	public String typeId;// 1:我身边（定位） -- 场馆列表 2:运动日历 -- 活动列表 3:抽奖： h5 4:期待：h5
	
}
