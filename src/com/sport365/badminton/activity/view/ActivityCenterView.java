package com.sport365.badminton.activity.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sport365.badminton.R;
import com.sport365.badminton.entity.obj.VenueEntityObj;
import com.sport365.badminton.http.base.ImageLoader;

/**
 * 运动会所的view
 */
public class ActivityCenterView extends RelativeLayout implements OnClickListener {
	private RelativeLayout rl_layout;//
	
	private LinearLayout ll_bottom;// 底部框
	private TextView tv_venue; // 场馆
	private ImageView imageView; // 图片
	private TextView tv_time; // 时间
	private TextView tv_phone; // 电话
	private TextView tv_distance; // 地址
	private TextView tv_club; // 俱乐部
	private TextView tv_activity; // 活动
	private TextView tv_game; // 比赛
	private ImageView iv_tag_top;

	private ActivityCenterListen mActivityCenterListen;

	public ActivityCenterView(Context context) {
		super(context);
		inflate(context, R.layout.activity_center_item_layout, this);
		tv_venue = (TextView) findViewById(R.id.tv_venue);
		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_phone = (TextView) findViewById(R.id.tv_phone);
		tv_distance = (TextView) findViewById(R.id.tv_distance);
		tv_distance.setOnClickListener(this);
		tv_club = (TextView) findViewById(R.id.tv_club);
		tv_club.setOnClickListener(this);
		tv_activity = (TextView) findViewById(R.id.tv_activity);
		tv_activity.setOnClickListener(this);
		tv_game = (TextView) findViewById(R.id.tv_game);
		tv_game.setOnClickListener(this);
		imageView = (ImageView) findViewById(R.id.imageView);
		ll_bottom = (LinearLayout) findViewById(R.id.ll_bottom);
		rl_layout = (RelativeLayout) findViewById(R.id.rl_layout);
		iv_tag_top = (ImageView) findViewById(R.id.iv_tag_top);
	}

	public RelativeLayout setDateView(VenueEntityObj venueEntityobj) {
		if (venueEntityobj != null) {
			// 名称
			String clubName = !TextUtils.isEmpty(venueEntityobj.name) ? venueEntityobj.name : "";
			//加入距离
			String distance = !TextUtils.isEmpty(venueEntityobj.distance) && !"0".equals(venueEntityobj.distance) ? "（" + venueEntityobj.distance + "）" : "";
			tv_venue.setText(clubName + distance);

			// 图片
			ImageLoader.getInstance().displayImage(!TextUtils.isEmpty(venueEntityobj.logo) ? venueEntityobj.logo : "", imageView);
			// 时间
			String openingTime = !TextUtils.isEmpty(venueEntityobj.openingTime) ? venueEntityobj.openingTime : "";
			String closingTime = !TextUtils.isEmpty(venueEntityobj.closingTime) ? venueEntityobj.closingTime : "";
			tv_time.setText(openingTime + "--" + closingTime);
			// phone
			tv_phone.setText(!TextUtils.isEmpty(venueEntityobj.telephone) ? venueEntityobj.telephone : "");
			// 地址
			String provinceName = !TextUtils.isEmpty(venueEntityobj.provinceName) ? venueEntityobj.provinceName : "";
			String cityName = !TextUtils.isEmpty(venueEntityobj.cityName) ? venueEntityobj.cityName : "";
			String countyName = !TextUtils.isEmpty(venueEntityobj.countyName) ? venueEntityobj.countyName : "";
			String address = !TextUtils.isEmpty(venueEntityobj.address) ? venueEntityobj.address : "";
			tv_distance.setText(provinceName + "  " + cityName + "  " + countyName + "  " + "\n" + address);
			// 社团&&!"0".equals(venueEntityobj.clubNum)
			String clubNum = !TextUtils.isEmpty(venueEntityobj.clubNum) ? "社团（" + venueEntityobj.clubNum + "）" : "社团";
			tv_club.setText(clubNum);
			// 活动&&!"0".equals(venueEntityobj.activeNum)
			String activeNum = !TextUtils.isEmpty(venueEntityobj.activeNum) ? "活动（" + venueEntityobj.activeNum + "）" : "活动";
			tv_activity.setText(activeNum);
			// 比赛&&!"0".equals(venueEntityobj.matchNum)
			String matchNum = !TextUtils.isEmpty(venueEntityobj.matchNum) ? "比赛（" + venueEntityobj.matchNum + "）" : "比赛";
			tv_game.setText(matchNum);
			// 水印置顶1：置顶isTop
			if ("1".equals(venueEntityobj.isTop)) {
				iv_tag_top.setImageResource(R.drawable.indicator_common_set_up);
			}
			// 水印是否推荐1：推荐isRecommend
			else if ("1".equals(venueEntityobj.isRecommend)) {
				iv_tag_top.setImageResource(R.drawable.indicator_common_recommended);
			} else {
				iv_tag_top.setImageBitmap(null);
			}
		}
		return this;
	}

	/**
	 * 隐藏item的底部框
	 * 
	 * @param FlagVisible
	 * @return
	 */
	public RelativeLayout setBottonVisible(int FlagVisible) {
		ll_bottom.setVisibility(FlagVisible);
		return this;
	}

	/**
	 * 设置推荐置顶图片GONE
	 * 
	 * @param FlagVisible
	 * @return
	 */
	public RelativeLayout setTopRecommadImageViewVisible(int FlagVisible) {
		iv_tag_top.setVisibility(FlagVisible);
		return this;
	}

	/**
	 * 设置ActivitycenterListen的底部的监听方法
	 * 
	 * @param mActivityCenterListen
	 */
	public void setActivityCenterListen(ActivityCenterListen mActivityCenterListen) {
		this.mActivityCenterListen = mActivityCenterListen;
	}

	/**
	 * 设置底部按钮的监听事件
	 * 
	 * @author Frank
	 */
	public interface ActivityCenterListen {
		/**
		 * 查看社团team
		 */
		public void lookTeam();

		/**
		 * 查看活动
		 */
		public void lookActivity();

		/**
		 * 查看比赛
		 */
		public void lookMathce();

		/**
		 * 地图查看位置
		 */
		public void goMapShow();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_club:
			if (mActivityCenterListen != null) {
				mActivityCenterListen.lookTeam();
			}
			break;
		case R.id.tv_activity:
			if (mActivityCenterListen != null) {
				mActivityCenterListen.lookActivity();
			}
			break;
		case R.id.tv_game:
			if (mActivityCenterListen != null) {
				mActivityCenterListen.lookMathce();
			}
			break;
		case R.id.tv_distance:
			if (mActivityCenterListen != null) {
				mActivityCenterListen.goMapShow();
			}
			break;
		}
	}

	/**
	 * 设置item每一项margintop的值
	 */
	public RelativeLayout setMarginTop() {
		RelativeLayout.LayoutParams ll = (RelativeLayout.LayoutParams) rl_layout.getLayoutParams();
		ll.setMargins(0, 18, 0, 0);
		rl_layout.setLayoutParams(ll);
		return this;
	}

}
