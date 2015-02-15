package com.sport365.badminton.map;

import android.content.Context;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.MyLocationData;
import com.sport365.badminton.utils.Utilities;

/**
 * 百度定位帮助类(需要改进)
 * Created by vincent on 2015/2/15.
 */
public class BDLocationHelper {
	private final String BAIDU_DEFAULT_VALUE = "4.9E-324";
	public BDLocationListener myListener;
	private LocationClient mLocClient;
	private LocationClientOption option;

	private Context mContext;
	public static BDLocation mCurrentLocation;
	public static MyLocationData mMyLocationData;


	public BDLocationHelper(Context context, BDLocationListener bdLocationListener) {
		this.mContext = context;
		this.myListener = bdLocationListener;
	}


	/**
	 * 设置当前定位的位置
	 */
	public void setCurrentLocation(BDLocation location) {
		if (null != location) {
			mCurrentLocation = location;
			mMyLocationData = new MyLocationData.Builder()
					.accuracy(mCurrentLocation.getRadius())
					.direction(100).latitude(mCurrentLocation.getLatitude())
					.longitude(mCurrentLocation.getLongitude()).build();
		}
		if (null != mLocClient && mLocClient.isStarted()) {
			mLocClient.stop();
		}
	}

	/**
	 * 开始定位
	 */
	public void startLocation() {
		mLocClient = new LocationClient(mContext);
		mLocClient.registerLocationListener(myListener);
		option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(2000);
		option.setAddrType("all");
		option.setIsNeedAddress(true);
		mLocClient.setLocOption(option);
		mLocClient.start();
	}


}
