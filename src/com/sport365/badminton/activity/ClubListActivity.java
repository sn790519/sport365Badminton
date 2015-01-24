package com.sport365.badminton.activity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.sport365.badminton.BaseActivity;
import com.sport365.badminton.R;
import com.sport365.badminton.entity.obj.AdvertisementObject;
import com.sport365.badminton.http.base.ImageLoader;
import com.sport365.badminton.view.advertisement.AdvertisementView;

import java.util.ArrayList;

/**
 * 俱乐部列表页面
 *
 * @author Frank
 */
public class ClubListActivity extends BaseActivity {
	private EditText et_search_text;                                                // 搜索输入框
	private LinearLayout ll_ad_layout;                                                // 广告

	private ListView lv_activity_center;
	private ClubAdapter clubAdapter;
	private ArrayList<AdvertisementObject> advertismentlist = new ArrayList<AdvertisementObject>();    // 广告
	private AdvertisementView advertisementControlLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.club_layout);
		setActionBarTitle("俱乐部");
		lv_activity_center = (ListView) findViewById(R.id.lv_club);
		lv_activity_center.addHeaderView(initHeadView());
		clubAdapter = new ClubAdapter();
		lv_activity_center.setAdapter(clubAdapter);
		lv_activity_center.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(ClubListActivity.this, ClubDetailActivity.class);
				startActivity(intent);
			}
		});
		initADdata();
	}

	private View initHeadView() {
		View headView = mLayoutInflater.inflate(R.layout.activity_center_headview_layout, null);
		et_search_text = (EditText) headView.findViewById(R.id.et_search_text);
		ll_ad_layout = (LinearLayout) headView.findViewById(R.id.ll_ad_layout);
		return headView;
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

	class ClubAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return 20;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = mLayoutInflater.inflate(R.layout.club_item_layout, null);
				viewHolder.tv_venue = (TextView) convertView.findViewById(R.id.tv_venue);
				viewHolder.tv_tips = (TextView) convertView.findViewById(R.id.tv_tips);
				viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
				viewHolder.tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
				viewHolder.tv_distance = (TextView) convertView.findViewById(R.id.tv_distance);
				viewHolder.tv_club = (TextView) convertView.findViewById(R.id.tv_club);
				viewHolder.tv_activity = (TextView) convertView.findViewById(R.id.tv_activity);
				viewHolder.tv_game = (TextView) convertView.findViewById(R.id.tv_game);
				viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			return convertView;
		}

	}

	class ViewHolder {
		TextView tv_venue;        // 场馆
		TextView tv_tips;        // 价格
		ImageView imageView;        // 图片
		TextView tv_time;        // 时间
		TextView tv_phone;        // 电话
		TextView tv_distance;    // 地址
		TextView tv_club;        // 俱乐部
		TextView tv_activity;    // 活动
		TextView tv_game;        // 比赛
	}
}
