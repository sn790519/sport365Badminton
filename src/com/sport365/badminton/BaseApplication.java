package com.sport365.badminton;

import android.app.Application;

import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;

public class BaseApplication extends Application {
	public LocationClient mLocationClient;
	public static BaseApplication application = null;

	@Override
	public void onCreate() {
		super.onCreate();
		application = this;
		// 百度地图初始化
		SDKInitializer.initialize(getApplicationContext());
		// 百度定位初始化
		mLocationClient = new LocationClient(this.getApplicationContext());
	}

	public static BaseApplication getInstance() {
		if (application == null) {
			application = new BaseApplication();
			return application;
		}
		return application;
	}

}
