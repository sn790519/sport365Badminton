package com.sport365.badminton.view.pullrefresh;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.sport365.badminton.R;

public class LoadingLayout extends FrameLayout {

	static final int DEFAULT_ROTATION_ANIMATION_DURATION = 150;

	private final ImageView headerImage;
	private final TextView headerText;
	private ProgressBar customProgressBar;
	private String pullLabel;
	private String refreshingLabel;
	private String releaseLabel;
	private boolean invisibleRefreashIcon = false; // 是否隐藏上拉刷新的图标
	private AnimationDrawable animationDrawable;
	private LinearLayout ll_refresh_tip;

	public LoadingLayout(Context context, final int mode, String releaseLabel,
						 String pullLabel, String refreshingLabel) {
		super(context);
		ViewGroup header = (ViewGroup) LayoutInflater.from(context).inflate(
				R.layout.pull_to_refresh_header, this);
		headerText = (TextView) header.findViewById(R.id.pull_to_refresh_text);
		headerImage = (ImageView) header.findViewById(R.id.animation_iv);
		headerImage.setVisibility(View.INVISIBLE);
		ll_refresh_tip = (LinearLayout) header.findViewById(R.id.ll_refresh_tip);
		animationDrawable = (AnimationDrawable) headerImage.getDrawable();
		animationDrawable.start();
		customProgressBar = (ProgressBar) header
				.findViewById(R.id.custom_pb);
		this.releaseLabel = releaseLabel;
		this.pullLabel = pullLabel;
		this.refreshingLabel = refreshingLabel;
		// update the header text
		headerText.setText(pullLabel);
	}

	public void reset() {
		if (invisibleRefreashIcon) {
			return;
		}
		headerText.setText(pullLabel);
		headerImage.setVisibility(View.INVISIBLE);
		customProgressBar.setVisibility(View.VISIBLE);
	}

	public void releaseToRefresh() {
		if (invisibleRefreashIcon) {
			return;
		}
		headerText.setText(releaseLabel);
		headerImage.setVisibility(View.INVISIBLE);
		customProgressBar.setVisibility(View.VISIBLE);
	}

	public void setPullLabel(String pullLabel) {
		this.pullLabel = pullLabel;
	}

	public void refreshing() {
		if (invisibleRefreashIcon) {
			return;
		}
		headerText.setText(refreshingLabel);
		headerImage.setVisibility(View.VISIBLE);
		customProgressBar.setVisibility(View.INVISIBLE);
	}

	public void setRefreshingLabel(String refreshingLabel) {
		this.refreshingLabel = refreshingLabel;
	}

	public void setReleaseLabel(String releaseLabel) {
		this.releaseLabel = releaseLabel;
	}

	public void pullToRefresh() {
		if (invisibleRefreashIcon) {
			return;
		}
		headerText.setText(pullLabel);
		headerImage.setVisibility(View.INVISIBLE);
		customProgressBar.setVisibility(View.VISIBLE);
	}

	public void setTextColor(int color) {
		headerText.setTextColor(color);
	}

	/**
	 * 实现上拉提示没有更多内容
	 */
	public void setUpPullRefreashEmpty() {
		invisibleRefreashIcon = true;
		if (headerText != null) {
			headerText.setText("没有更多内容");
			headerText.setTextColor(R.color.c_tcolor_dark_grey);

		}
		if (headerImage != null) {
			headerImage.setVisibility(View.GONE);
		}
		if (customProgressBar != null) {
			customProgressBar.setVisibility(View.GONE);
		}
	}

	public void setUpPullUnRefreashEmpty() {
		invisibleRefreashIcon = false;
	}

	/**
	 * 更新进度条
	 *
	 * @param headerHeight
	 * @param newHeight
	 */
	public void refreshProgressBar(int headerHeight, int newHeight) {
		if (newHeight < 0) {
			newHeight = 0;
		}
		int progress = 0;
		progress = (int) (((float) newHeight / (float) headerHeight) * 100);
		customProgressBar.setProgress(progress);
	}

	public void showRefreshTip() {
		ll_refresh_tip.setVisibility(View.VISIBLE);
	}

	public void hideRefreshTip() {
		ll_refresh_tip.setVisibility(View.INVISIBLE);
	}

}
