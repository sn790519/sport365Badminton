package com.sport365.badminton.utils;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

public class SportBadmintonApplication extends Application {
	private static SportBadmintonApplication instance;

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		// 百度地图初始化
		SDKInitializer.initialize(getApplicationContext());
	}

	public static SportBadmintonApplication getInstance() {
		return instance;
	}

}
