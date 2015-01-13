package com.sport365.badminton.activity;

import android.os.Bundle;

import com.sport365.badminton.BaseActivity;
import com.sport365.badminton.R;

public class MainActivity extends BaseActivity {
	private String		TAG	= MainActivity.class.getSimpleName();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

}
