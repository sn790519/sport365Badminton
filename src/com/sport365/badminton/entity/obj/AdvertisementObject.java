package com.sport365.badminton.entity.obj;

import java.io.Serializable;

public class AdvertisementObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8382730913718940191L;

	public String noticeUrlType; // 1
	public String imageName; // iPhone测试

	/**
	 * 重定向. ()
	 */
	public String redirectUrl;

	/**
	 * 图片tag.
	 */
	public String tag;

	public String imageUrl;
	
	public String privinceId;
	public String cityId;
	public String sortIndex;
	public String versionBegin;
	public String versionEnd;
	public String category;
	public String isTop;

}
