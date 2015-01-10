package com.sport365.badminton.base;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.sport365.badminton.R;

public abstract class BaseActionBarActivity extends SherlockFragmentActivity {
	private ActionBar mActionBar;
	private ImageView actionbar_back;
	private ImageView actionbar_title_icon;
	private ImageView action_icon;
	private TextView actionbar_title, actionbar_icon;
	// 默认的actionBar资源文件id
	private final int DEFAULT_ACTIONBAR_RESOURCE_ID = R.layout.main_action_bar;
	private final int DEFALUT_ACTIONBAR_BG_ID = R.drawable.navibar_common_bg;
	private int actionbar_resource_id = DEFAULT_ACTIONBAR_RESOURCE_ID;
	private View mTitleView;
	private long firstClickTime;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initDefaultActionbar();
	}

	protected void onCreate(Bundle savedInstanceState, int actionbar_resource_id) {
		super.onCreate(savedInstanceState);
		this.actionbar_resource_id=actionbar_resource_id;
		initDefaultActionbar();
	}

	protected void initDefaultActionbar() {
		mTitleView = getLayoutInflater().inflate(actionbar_resource_id, null);
		mActionBar = this.getSupportActionBar();
		if (mActionBar != null) {
			mActionBar.setDisplayShowCustomEnabled(true);// 可以自定义actionbar
			mActionBar.setDisplayShowTitleEnabled(false);// 不显示logo
			mActionBar.setDisplayShowHomeEnabled(false);
			mActionBar.setBackgroundDrawable(getResources().getDrawable(DEFALUT_ACTIONBAR_BG_ID));
			if (actionbar_resource_id == DEFAULT_ACTIONBAR_RESOURCE_ID) {
				actionbar_back = (ImageView) mTitleView
						.findViewById(R.id.actionbar_back);
				actionbar_back.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						onBackPressed();
					}
				});
				actionbar_icon = (TextView) mTitleView
						.findViewById(R.id.actionbar_icon);
				actionbar_icon.setVisibility(View.GONE);
				action_icon = (ImageView) mTitleView
						.findViewById(R.id.action_icon);
				action_icon.setVisibility(View.GONE);
				actionbar_title = (TextView) mTitleView
						.findViewById(R.id.actionbar_title);
				actionbar_title.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						actionbar_back.performClick();

					}
				});
				actionbar_title_icon = (ImageView) mTitleView
						.findViewById(R.id.actionbar_title_icon);
			}
			;
			ActionBar.LayoutParams params = new ActionBar.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			mActionBar.setCustomView(mTitleView, params);
		}
	}

	public void setActionBarTitle(String title) {
		if (actionbar_title != null) {
			actionbar_title.setText(title);
		}
	}

	public void setHomeBar(String text) {
		if (actionbar_icon != null && action_icon != null) {
			actionbar_back.setVisibility(View.GONE);
			actionbar_icon.setVisibility(View.VISIBLE);
			actionbar_icon.setText(text);
			action_icon.setVisibility(View.VISIBLE);
		}
	}

	public void setHomeBar() {
		if (actionbar_icon != null && action_icon != null) {
			actionbar_back.setVisibility(View.GONE);
			actionbar_icon.setVisibility(View.VISIBLE);
			action_icon.setVisibility(View.VISIBLE);
		}
	}

	public TextView getActionBarTitle() {
		return actionbar_title;
	}

	public ActionBar getmActionBar() {
		return mActionBar;
	}

	public View getActionBarTitleView() {
		return mTitleView;
	}

	public ImageView getActionbarTitleIcon() {
		return actionbar_title_icon;
	}

	/**
	 * 在被回收的时候，保存一些系统级的变量值
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	/**
	 * 该方法为最底层调用的启动activity方法，其余启动方法均会执行到此处
	 * 设置延迟时间防止连续点击启动新activity
	 * @param intent
	 * @param requestCode
	 *//*
	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		long time=System.currentTimeMillis();
		if(time- firstClickTime >SystemConfig.DOUBLE_CLICK_TIME||time<firstClickTime)
		{
			firstClickTime =time;
			super.startActivityForResult(intent,requestCode);
		}
	}*/
}
