package com.sport365.badminton.activity;

import android.os.Bundle;

import com.sport365.badminton.BaseActivity;
import com.sport365.badminton.R;

/**
 * 比赛列表页面
 * 
 * @author Frank
 * 
 */
public class PlayListActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBarTitle("比赛");
		setContentView(R.layout.play_layout);
	}

}
