package com.sport365.badminton.base.flip;

import android.os.Bundle;

import com.sport365.badminton.R;
import com.sport365.badminton.base.myWidget.swipe.SwipeBackActivity;

/**
 * 右滑切换activity效果基类 若要屏蔽该功能则将MyBaseActivity更改为继承自Activity，否则请继承该类。
 * 若要控制该页面是否需要滑动效果，可通过setCanFlip()方法设置该页面是否可以右滑翻页。
 * 
 * @author Ruyan.Zhao 6045
 * @since tongcheng_client_5.2
 * 
 * @version tongcheng_client_7.1 替换
 *          FlipActivityHelper更改滑动返回效果，继承于SwipeBackActivity
 * @author yy09011
 * 
 */
public abstract class BaseFlipActivity extends SwipeBackActivity {

	private int exitAnim = R.anim.slide_out_right;
	private String activity_tag;
	/** 当前页面是否已被finish掉，防止finish反复调用造成状态丢失的问题 */
	private boolean isFinished = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity_tag = getIntent().getStringExtra("activity_tag");
	}

	protected void onCreate(Bundle savedInstanceState, int actionbar_resource_id) {
		super.onCreate(savedInstanceState, actionbar_resource_id);
		activity_tag = getIntent().getStringExtra("activity_tag");
	}

	public boolean isCanFlip() {
		return getSwipeBackLayout().getEnableGesture();
	}

	/**
	 * 设置当前页面是否支持右滑切换
	 */
	public void setCanFlip(boolean canFlip) {
		setSwipeBackEnable(canFlip);
	}

	/**
	 * 自定义activity的回退按键事件，加入动画
	 */
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	public void finish() {
		if (isFinished) {
			return;
		}
		// if (!getSupportFragmentManager().popBackStackImmediate()) {
		ActivityHelper.overridePendingTransition(this, 0, exitAnim);
		super.finish();
		isFinished = true;
		// }
	}

	/**
	 * 设置activity退出动画
	 * 
	 * @param exitAnim
	 *            退出动画的resId
	 */
	protected void setExitAnim(int exitAnim) {
		this.exitAnim = exitAnim;
	}

}
