package com.sport365.badminton.activity.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.sport365.badminton.BaseFragment;
import com.sport365.badminton.R;
import com.sport365.badminton.entity.obj.AdvertisementObject;
import com.sport365.badminton.http.base.ImageLoader;
import com.sport365.badminton.view.advertisement.AdvertisementView;

public class HomePageFragment extends BaseFragment {
	private ArrayList<AdvertisementObject> advertismentlist = new ArrayList<AdvertisementObject>();// 广告
	private AdvertisementView advertisementControlLayout;
	private LinearLayout ll_ad_layout;
	Bundle outState;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (outState != null) {
			System.out.println("HomePageFragment+++++++++++++++++++++onCreateView+" + outState.getString("key") + "+++++++++++++++++++++++");
		} else {
			System.out.println("HomePageFragment----onCreate()");
		}
	}

	/**
	 * onPause保存的数据在onResume()显示调用
	 */
	@Override
	public void onResume() {
		super.onResume();
		if (outState != null) {
			System.out.println("HomePageFragment+++++++++++++++++++++onResume()+" + outState.getString("key") + "+++++++++++++++++++++++");
		} else {
			System.out.println("HomePageFragment---onResume()");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			// 处理onSaveInstanceState异常保存的数据
			System.out.println("HomePageFragment+++++++++++++++++++++onCreateView+" + savedInstanceState.getBoolean("hasTabs") + "+++++++++++++++++++++++");
		}

		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.home_page_layout, container, false);
		Button btn = (Button) view.findViewById(R.id.btn);
		btn.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("image/*");
				intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
				intent.putExtra(Intent.EXTRA_TEXT, "终于可以了!!!");
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(Intent.createChooser(intent, getActivity().getTitle()));
			}
		});
		ll_ad_layout = (LinearLayout) view.findViewById(R.id.ll_ad_layout);
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
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	private AdvertisementObject initADdata() {
		AdvertisementObject ad_one = new AdvertisementObject();
		ad_one.imageUrl = "http://a.hiphotos.baidu.com/image/pic/item/bba1cd11728b4710f197b4c1c0cec3fdfc032306.jpg";
		ad_one.redirectUrl = "http://www.baidu.com";
		return ad_one;
	}

	/**
	 * 切换fragment保存数据,并在onResume()方法中调用
	 */
	@Override
	public void onPause() {
		super.onPause();
		System.out.println("HomeBallFriendFragment+++++++++++++++++++++pause ball friend++++++++++++++++++++++");
		outState = new Bundle();
		outState.putString("key", "key");
	}

	/**
	 * 异常情况保存数据
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean("hasTabs", false);
		System.out.println("HomeBallFriendFragment+++++++++++++++++++++onSaveInstanceState ball friend++++++++++++++++++++++");
	}
}
