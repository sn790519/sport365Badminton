package com.sport365.badminton.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.sport365.badminton.BaseActivity;
import com.sport365.badminton.R;
import com.sport365.badminton.entity.obj.AdvertisementObject;
import com.sport365.badminton.view.advertisement.AdvertisementView;

/**
 * 测试广告控件
 * 
 * @author kjh08490
 * 
 */
public class TestAdActivity extends BaseActivity {
	private ArrayList<AdvertisementObject> advertismentlist = new ArrayList<AdvertisementObject>();// 广告
	private AdvertisementView advertisementControlLayout;
	private LinearLayout ll_ad_layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(com.sport365.badminton.R.layout.ad_layout);
		ll_ad_layout = (LinearLayout) this.findViewById(R.id.ll_ad_layout);
		advertisementControlLayout = new AdvertisementView(this);
		advertisementControlLayout.setAdvertisementRate(8, 3);
		advertisementControlLayout.setImageLoader(mImageLoader);
		ll_ad_layout.addView(advertisementControlLayout);
		for (int i = 0; i < 6; i++) {
			advertismentlist.add(initADdata());
		}

		if (advertismentlist != null && advertismentlist.size() > 0) {
			advertisementControlLayout.setAdvertisementData(advertismentlist);
			ll_ad_layout.setVisibility(View.VISIBLE);
		}
	}

	private AdvertisementObject initADdata() {
		AdvertisementObject ad_one = new AdvertisementObject();
		ad_one.imageUrl = "http://a.hiphotos.baidu.com/image/pic/item/bba1cd11728b4710f197b4c1c0cec3fdfc032306.jpg";
		ad_one.redirectUrl = "http://www.baidu.com";
		return ad_one;
	}
}
