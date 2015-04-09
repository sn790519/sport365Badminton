package com.sport365.badminton.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import com.baidu.mapapi.search.core.SearchResult;
import com.sport365.badminton.BaseActivity;
import com.sport365.badminton.R;
import com.sport365.badminton.activity.fragment.MapViewFragment;

/**
 * 地图
 */
public class MapViewActivity extends BaseActivity implements MapViewFragment.OnRoutePlanSuccessListener {

	// 经纬度常量
	public static final String LAT = "LAT";
	public static final String LON = "LON";

	private String naviType;

	// 经纬度变量
	private String lat = "";
	private String lon = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mapview);
		initActionBar();
		initBundle();
		MapViewFragment newFragment = new MapViewFragment();
		Bundle bundle=new Bundle();
		bundle.putString(MapViewActivity.LAT, lat);
		bundle.putString(MapViewActivity.LON, lon);
		newFragment.setArguments(bundle);
		newFragment.setonRoutePlanSuccessListener(this);
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.fragment_container, newFragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	/**
	 * 获取经纬度
	 */
	private void initBundle() {
		lat = getIntent().getStringExtra(LAT);
		lon = getIntent().getStringExtra(LON);
	}

	private void initActionBar() {
		setActionBarTitle("地图");
		mActionbar_right.setVisibility(View.GONE);
		mActionbar_right_text.setText("线路说明");
		mActionbar_right_text.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("naviType", naviType);
				intent.setClass(MapViewActivity.this, LookRouteActivity.class);
				MapViewActivity.this.startActivity(intent);
			}
		});
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == event.getKeyCode()) {
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void routePlanSuccess(String naviType) {
		//路线规划成功，显示路线说明
		mActionbar_right_text.setVisibility(View.VISIBLE);
		if (!TextUtils.isEmpty(naviType)) {
			this.naviType = naviType;
		}
	}
}
