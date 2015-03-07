package com.sport365.badminton.activity.view;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.sport365.badminton.R;
import com.sport365.badminton.entity.obj.ClubTabEntityObj;
import com.sport365.badminton.http.base.ImageLoader;

/**
 * 俱乐部的view
 * Created by kjh08490 on 2015/3/7.
 */
public class ClubView extends RelativeLayout {
	private TextView tv_venue; // 场馆
	private TextView tv_price; // 价格
	private ImageView imageView; // 图片
	private TextView tv_time; // 时间
	private TextView tv_phone; // 电话
	private TextView tv_distance; // 地址
	private TextView tv_club; // 俱乐部
	private TextView tv_activity; // 活动
	private TextView tv_game; // 比赛

	private LinearLayout ll_bottom;

	public ClubView(Context context) {
		super(context);
		inflate(context, R.layout.club_item_layout, this);
		tv_venue = (TextView) findViewById(R.id.tv_venue);
		tv_price = (TextView) findViewById(R.id.tv_price);
		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_phone = (TextView) findViewById(R.id.tv_phone);
		tv_distance = (TextView) findViewById(R.id.tv_distance);
		tv_club = (TextView) findViewById(R.id.tv_club);
		tv_activity = (TextView) findViewById(R.id.tv_activity);
		tv_game = (TextView) findViewById(R.id.tv_game);
		imageView = (ImageView) findViewById(R.id.imageView);
		ll_bottom = (LinearLayout) findViewById(R.id.ll_bottom);
	}

	public RelativeLayout setDateView(ClubTabEntityObj mClubTabEntityObj) {
		if (mClubTabEntityObj != null) {
			// 场馆
			String clubName = !TextUtils.isEmpty(mClubTabEntityObj.clubName) ? mClubTabEntityObj.clubName : "";
			tv_venue.setText(clubName);
			// 价格
			String privce = "20元";
			tv_price.setText(privce);
			// 图片
			String clubLogo = !TextUtils.isEmpty(mClubTabEntityObj.clubLogo) ? mClubTabEntityObj.clubLogo : "";
			ImageLoader.getInstance().displayImage(clubLogo, imageView);
			// 时间
			tv_time.setText("时间");
			// 地址
			String provinceName = !TextUtils.isEmpty(mClubTabEntityObj.provinceName) ? mClubTabEntityObj.provinceName : "";
			String cityName = !TextUtils.isEmpty(mClubTabEntityObj.cityName) ? mClubTabEntityObj.cityName : "";
			String countyName = !TextUtils.isEmpty(mClubTabEntityObj.countyName) ? mClubTabEntityObj.countyName : "";
			tv_distance.setText(provinceName + "  " + cityName + "  " + countyName + "\n" + "缺少");
			// 俱乐部
			String activeNum = !TextUtils.isEmpty(mClubTabEntityObj.activeNum) ? mClubTabEntityObj.activeNum : "";
			tv_club.setText("俱乐部（" + activeNum + "）");
			// 活动
			tv_activity.setText("活动（" + activeNum + "）");
			String matchNum = !TextUtils.isEmpty(mClubTabEntityObj.matchNum) ? mClubTabEntityObj.matchNum : "";
			// 比赛
			tv_game.setText("比赛（" + matchNum + "）");
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
