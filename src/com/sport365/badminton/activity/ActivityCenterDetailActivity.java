package com.sport365.badminton.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sport365.badminton.BaseActivity;
import com.sport365.badminton.R;
import com.sport365.badminton.entity.obj.AdvertisementObject;
import com.sport365.badminton.http.base.ImageLoader;
import com.sport365.badminton.view.advertisement.AdvertisementView;

/**
 * 运动会所的详情页面
 * 
 * @author Frank
 * 
 */
public class ActivityCenterDetailActivity extends BaseActivity {

	private LinearLayout ll_ad_layout;// 广告
	private TextView tv_venue; // 场馆
	private ImageView imageView; // 图片
	private TextView tv_time; // 时间
	private TextView tv_phone; // 电话
	private TextView tv_distance; // 地址
	private TextView tv_club; // 俱乐部
	private TextView tv_activity; // 活动
	private TextView tv_game; // 比赛
	private ArrayList<AdvertisementObject> advertismentlist = new ArrayList<AdvertisementObject>(); // 广告
	private AdvertisementView advertisementControlLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_center_detail_layout);
		setActionBarTitle("运动会所详情");
		initView();
		initADdata();
	}

	private void initView() {
		ll_ad_layout = (LinearLayout) findViewById(R.id.ll_ad_layout);
		tv_venue = (TextView) findViewById(R.id.tv_venue);
		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_phone = (TextView) findViewById(R.id.tv_phone);
		tv_distance = (TextView) findViewById(R.id.tv_distance);
		tv_club = (TextView) findViewById(R.id.tv_club);
		tv_activity = (TextView) findViewById(R.id.tv_activity);
		tv_game = (TextView) findViewById(R.id.tv_game);
		imageView = (ImageView) findViewById(R.id.imageView);
	}

	private void initADdata() {
		AdvertisementObject ad_one = new AdvertisementObject();
		ad_one.imageUrl = "http://a.hiphotos.baidu.com/image/pic/item/bba1cd11728b4710f197b4c1c0cec3fdfc032306.jpg";
		ad_one.redirectUrl = "http://www.baidu.com";
		AdvertisementObject ad_two = new AdvertisementObject();
		ad_two.imageUrl = "http://f.hiphotos.baidu.com/image/pic/item/024f78f0f736afc344e8b203b019ebc4b64512eb.jpg";
		AdvertisementObject ad_three = new AdvertisementObject();
		ad_three.imageUrl = "http://f.hiphotos.baidu.com/image/pic/item/377adab44aed2e73de410b4a8401a18b87d6fa28.jpg";
		advertismentlist.add(ad_one);
		advertismentlist.add(ad_two);
		advertismentlist.add(ad_three);
		advertisementControlLayout = new AdvertisementView(this);
		if (advertismentlist != null && advertismentlist.size() > 0) {
			advertisementControlLayout.setAdvertisementData(advertismentlist);
			ll_ad_layout.setVisibility(View.VISIBLE);
		}
		advertisementControlLayout.setAdvertisementRate(8, 3);
		advertisementControlLayout.setImageLoader(ImageLoader.getInstance());
		ll_ad_layout.addView(advertisementControlLayout);
	}
}
