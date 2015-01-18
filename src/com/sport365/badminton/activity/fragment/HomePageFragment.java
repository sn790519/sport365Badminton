package com.sport365.badminton.activity.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.sport365.badminton.BaseFragment;
import com.sport365.badminton.R;
import com.sport365.badminton.entity.obj.AdvertisementObject;
import com.sport365.badminton.http.base.ImageLoader;
import com.sport365.badminton.view.advertisement.AdvertisementView;

/**
 * 首页界面
 * 
 * @author Frank
 * 
 */
public class HomePageFragment extends BaseFragment {
	private ArrayList<AdvertisementObject> advertismentlist = new ArrayList<AdvertisementObject>(); // 广告
	private AdvertisementView advertisementControlLayout;
	private LinearLayout ll_ad_layout;

	private AdvertisementView advertisementControlLayout_bottom;
	private LinearLayout ll_ad_layout_bottom;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_page_layout, container, false);
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

}
