package com.sport365.badminton.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import com.sport365.badminton.BaseActivity;
import com.sport365.badminton.R;
import com.sport365.badminton.activity.fragment.MapViewFragment;

/**
 * 我的固定活动
 */
public class MyFixationActivity extends BaseActivity {

	private String naviType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mapview);
		initActionBar();

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
				intent.setClass(MyFixationActivity.this, LookRouteActivity.class);
				MyFixationActivity.this.startActivity(intent);
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

}
