package com.sport365.badminton;

import com.baidu.mapapi.SDKInitializer;

import android.app.Application;

public class BaseApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		//百度地图初始化
		SDKInitializer.initialize(getApplicationContext());
	}

}
