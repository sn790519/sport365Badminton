package com.sport365.badminton.activity.fragment;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sport365.badminton.BaseFragment;
import com.sport365.badminton.R;
import com.sport365.badminton.activity.ActivityCenterListAtivity;
import com.sport365.badminton.activity.ActivityListActivity;
import com.sport365.badminton.activity.CalendarTimesActivity;
import com.sport365.badminton.activity.ClubListActivity;
import com.sport365.badminton.activity.PlayListActivity;
import com.sport365.badminton.entity.obj.AdvertisementObject;
import com.sport365.badminton.http.base.ImageLoader;
import com.sport365.badminton.view.advertisement.AdvertisementView;

/**
 * 首页界面
 *
 * @author Frank
 */
public class HomePageFragment extends BaseFragment {
	private ArrayList<AdvertisementObject> advertismentlist = new ArrayList<AdvertisementObject>(); // 广告
	private AdvertisementView advertisementControlLayout;
	private LinearLayout ll_ad_layout;

	private AdvertisementView advertisementControlLayout_bottom;
	private LinearLayout ll_ad_layout_bottom;

	//运动会所
	private TextView tv_sport_field;
	//俱乐部
	private TextView tv_club;
	//活动
	private TextView tv_activity;
	//比赛
	private TextView tv_game;
	// 运动日历
	private ImageView iv_sport_calendar;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_page_layout, container, false);

		tv_sport_field = (TextView) view.findViewById(R.id.tv_sport_field);
		tv_club = (TextView) view.findViewById(R.id.tv_club);
		tv_activity = (TextView) view.findViewById(R.id.tv_activity);
		tv_game = (TextView) view.findViewById(R.id.tv_game);
		iv_sport_calendar = (ImageView) view.findViewById(R.id.iv_sport_calendar);
		
		tv_sport_field.setOnClickListener(this);
		tv_club.setOnClickListener(this);
		tv_activity.setOnClickListener(this);
		tv_game.setOnClickListener(this);
		iv_sport_calendar.setOnClickListener(this);


		ll_ad_layout = (LinearLayout) view.findViewById(R.id.ll_ad_layout);
		ll_ad_layout_bottom = (LinearLayout) view.findViewById(R.id.ll_ad_layout_bottom);
		advertisementControlLayout = new AdvertisementView(getActivity());
		advertisementControlLayout.setAdvertisementRate(8, 3);
		advertisementControlLayout.setImageLoader(ImageLoader.getInstance());
		ll_ad_layout.addView(advertisementControlLayout);
		for (int i = 0; i < 6; i++) {
			advertismentlist.add(initADdata());
		}
		if (advertismentlist != null && advertismentlist.size() > 0) {
			advertisementControlLayout.setAdvertisementData(advertismentlist);
			ll_ad_layout.setVisibility(View.VISIBLE);
		}

		advertisementControlLayout_bottom = new AdvertisementView(getActivity());
		advertisementControlLayout_bottom.setAdvertisementRate(8, 3);
		advertisementControlLayout_bottom.setImageLoader(ImageLoader.getInstance());
		ll_ad_layout_bottom.addView(advertisementControlLayout_bottom);
		if (advertismentlist != null && advertismentlist.size() > 0) {
			advertisementControlLayout_bottom.setAdvertisementData(advertismentlist);
			ll_ad_layout_bottom.setVisibility(View.VISIBLE);
		}


		return view;
	}

	private AdvertisementObject initADdata() {
		AdvertisementObject ad_one = new AdvertisementObject();
		ad_one.imageUrl = "http://a.hiphotos.baidu.com/image/pic/item/bba1cd11728b4710f197b4c1c0cec3fdfc032306.jpg";
		ad_one.redirectUrl = "http://www.baidu.com";
		return ad_one;
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		Intent intent = null;
		switch (v.getId()) {
			case R.id.tv_sport_field:
				intent = new Intent(getActivity(),ActivityCenterListAtivity.class);
				startActivity(intent);
				break;
			case R.id.tv_club:
				intent = new Intent(getActivity(),ClubListActivity.class);
				startActivity(intent);
				break;
			case R.id.tv_activity:
				intent = new Intent(getActivity(),ActivityListActivity.class);
				startActivity(intent);
				break;
			case R.id.tv_game:
				intent = new Intent(getActivity(),PlayListActivity.class);
				startActivity(intent);
				break;
			case R.id.iv_sport_calendar:
				intent = new Intent(getActivity(),CalendarTimesActivity.class);
				startActivity(intent);
				break;
		}
	}
}
