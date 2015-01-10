package com.sport365.badminton;

import android.app.Activity;
import android.os.Bundle;

import com.sport365.badminton.utils.ULog;

public class BaseActivity extends Activity {
	public String	TAG	= BaseActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ULog.setTag(getClass().getSimpleName());
		ULog.debug("--->onCreate");
	}

	@Override
	protected void onStart() {
		super.onStart();
		ULog.debug("--->onStart");
	}

	@Override
	protected void onResume() {
		super.onResume();
		ULog.debug("--->onResume");
	}

	@Override
	protected void onPause() {
		super.onPause();
		ULog.debug("--->onPause");
	}

	@Override
	protected void onStop() {
		super.onStop();
		ULog.debug("--->onStop");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ULog.debug("--->onDestroy");
	}

}
