package com.sport365.badminton.activity;

import android.os.Bundle;

import com.sport365.badminton.BaseActivity;
import com.sport365.badminton.R;

/**
 * 比赛详情页面
 * 
 * @author Frank
 * 
 */
public class PlayDetailActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBarTitle("比赛详情");
		setContentView(R.layout.play_detail_layout);
	}
}
