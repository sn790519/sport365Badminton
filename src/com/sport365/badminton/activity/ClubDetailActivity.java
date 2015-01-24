package com.sport365.badminton.activity;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sport365.badminton.BaseActivity;
import com.sport365.badminton.R;
import com.sport365.badminton.entity.obj.AdvertisementObject;
import com.sport365.badminton.http.base.ImageLoader;
import com.sport365.badminton.view.advertisement.AdvertisementView;

import java.util.ArrayList;

/**
 * 俱乐部详情页面
 * 
 * @author Frank
 * 
 */
public class ClubDetailActivity extends BaseActivity {
	private LinearLayout ll_ad_layout;// 广告
	private TextView tv_venue;        // 场馆
	private TextView tv_price;        // 价格
	private ImageView imageView;        // 图片
	private ImageView iv_tag_top;        // 置顶图片
	private ImageView iv_activity_flag;        // 进行中
	private TextView tv_time;        // 时间
	private TextView tv_phone;        // 电话
	private TextView tv_distance;    // 地址
	private TextView tv_sign_alredy;        // 俱乐部
	private TextView tv_activity_sign;    // 活动

	private TextView tv_activity_rechange;// 活动充值
	private TextView tv_sign_now;// 活动充值
	private ArrayList<AdvertisementObject> advertismentlist = new ArrayList<AdvertisementObject>(); // 广告
	private AdvertisementView advertisementControlLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.club_detail_layout);
		setActionBarTitle("俱乐部详情");
		initView();
		initADdata();
	}
	private void initView() {
		ll_ad_layout = (LinearLayout) findViewById(R.id.ll_ad_layout);
		tv_venue = (TextView) findViewById(R.id.tv_venue);
		tv_price = (TextView) findViewById(R.id.tv_price);
		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_phone = (TextView) findViewById(R.id.tv_phone);
		tv_distance = (TextView) findViewById(R.id.tv_distance);
		tv_sign_alredy = (TextView) findViewById(R.id.tv_sign_alredy);
		tv_activity_sign = (TextView) findViewById(R.id.tv_activity_sign);
		tv_activity_rechange = (TextView) findViewById(R.id.tv_activity_rechange);
		tv_sign_now = (TextView) findViewById(R.id.tv_sign_now);
		imageView = (ImageView) findViewById(R.id.imageView);
		iv_activity_flag = (ImageView) findViewById(R.id.iv_activity_flag);
		iv_tag_top = (ImageView) findViewById(R.id.iv_tag_top);

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
