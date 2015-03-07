package com.sport365.badminton.activity.view;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.sport365.badminton.R;
import com.sport365.badminton.entity.obj.VenueEntityObj;
import com.sport365.badminton.http.base.ImageLoader;

/**
 * 运动会所的view
 * Created by kjh08490 on 2015/3/7.
 */
public class ActivityCenterView extends RelativeLayout {
	private LinearLayout ll_bottom;// 底部框
	private TextView tv_venue;        // 场馆
	private ImageView imageView;        // 图片
	private TextView tv_time;        // 时间
	private TextView tv_phone;        // 电话
	private TextView tv_distance;    // 地址
	private TextView tv_club;        // 俱乐部
	private TextView tv_activity;    // 活动
	private TextView tv_game;        // 比赛

	public ActivityCenterView(Context context) {
		super(context);
		inflate(context, R.layout.activity_center_item_layout, this);
		tv_venue = (TextView) findViewById(R.id.tv_venue);
		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_phone = (TextView) findViewById(R.id.tv_phone);
		tv_distance = (TextView) findViewById(R.id.tv_distance);
		tv_club = (TextView) findViewById(R.id.tv_club);
		tv_activity = (TextView) findViewById(R.id.tv_activity);
		tv_game = (TextView) findViewById(R.id.tv_game);
		imageView = (ImageView) findViewById(R.id.imageView);
		ll_bottom = (LinearLayout) findViewById(R.id.ll_bottom);
	}

	public RelativeLayout setDateView(VenueEntityObj venueEntityobj) {
		if (venueEntityobj != null) {
			// 名称
			tv_venue.setText(!TextUtils.isEmpty(venueEntityobj.name) ? venueEntityobj.name : "");
			// 图片
			ImageLoader.getInstance().displayImage(!TextUtils.isEmpty(venueEntityobj.logo) ? venueEntityobj.logo : "", imageView);
			// 时间
			String openingTime = !TextUtils.isEmpty(venueEntityobj.openingTime) ? venueEntityobj.openingTime : "";
			String closingTime = !TextUtils.isEmpty(venueEntityobj.closingTime) ? venueEntityobj.closingTime : "";
			tv_time.setText(openingTime + "--" + closingTime);
			// 地址
			String provinceName = !TextUtils.isEmpty(venueEntityobj.provinceName) ? venueEntityobj.provinceName : "";
			String cityName = !TextUtils.isEmpty(venueEntityobj.cityName) ? venueEntityobj.cityName : "";
			String countyName = !TextUtils.isEmpty(venueEntityobj.countyName) ? venueEntityobj.countyName : "";
			String address = !TextUtils.isEmpty(venueEntityobj.address) ? venueEntityobj.address : "";
			tv_distance.setText(provinceName + "  " + cityName + "  " + countyName + "  " + "\n" + address);
			//俱乐部
			String clubNum = !TextUtils.isEmpty(venueEntityobj.clubNum) ? venueEntityobj.clubNum : "";
			tv_club.setText("俱乐部（" + clubNum + "）");
			//活动
			String activeNum = !TextUtils.isEmpty(venueEntityobj.activeNum) ? venueEntityobj.activeNum : "";
			tv_club.setText("活动（" + activeNum + "）");
			//比赛
			String matchNum = !TextUtils.isEmpty(venueEntityobj.matchNum) ? venueEntityobj.matchNum : "";
			tv_club.setText("比赛（" + matchNum + "）");
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

}
