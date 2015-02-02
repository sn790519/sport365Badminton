package com.sport365.badminton.activity;

import android.app.Activity;
import android.os.Bundle;
import com.baidu.mapapi.map.MapView;
import com.sport365.badminton.R;

public class MapFragmentDemo extends Activity {
	MapView mMapView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//在使用SDK各组件之前初始化context信息，传入ApplicationContext
		//注意该方法要再setContentView方法之前实现
		setContentView(R.layout.activity_fragment);
		//获取地图控件引用
//		120.736615,31.282195

		mMapView = (MapView) findViewById(R.id.bmapView);
//		mMapView.
	}

//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		//在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
//		mMapView.onDestroy();
//	}
//
//	@Override
//	protected void onResume() {
//		super.onResume();
//		//在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
//		mMapView.onResume();
//	}
//
//	@Override
//	protected void onPause() {
//		super.onPause();
//		//在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
//		mMapView.onPause();
//	}
}
