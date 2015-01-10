package com.sport365.badminton;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

public class BaseApplication extends Application {
	private static BaseApplication instance;

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		// 百度地图初始化
		SDKInitializer.initialize(getApplicationContext());
	}

	public static BaseApplication getInstance() {
		return instance;
	}

}
