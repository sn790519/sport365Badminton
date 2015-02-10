package com.sport365.badminton.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.sport365.badminton.BaseActivity;
import com.sport365.badminton.R;
import com.sport365.badminton.entity.obj.SportAdvertismentObj;
import com.sport365.badminton.http.base.ImageLoader;
import com.sport365.badminton.view.advertisement.AdvertisementView;

/**
 * 活动列表页面
 *
 * @author Frank
 */
public class ActivityListActivity extends BaseActivity {
	private EditText et_search_text;                                                // 搜索输入框
	private LinearLayout ll_ad_layout;                                                // 广告

	private ListView lv_activity;
	private ActivityAdapter activityAdapter;
	private ArrayList<SportAdvertismentObj> advertismentlist = new ArrayList<SportAdvertismentObj>();    // 广告
	private AdvertisementView advertisementControlLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBarTitle("活动");
		setContentView(R.layout.activity_layout);
		lv_activity = (ListView) findViewById(R.id.lv_activity);
		lv_activity.addHeaderView(initHeadView());
		activityAdapter = new ActivityAdapter();
		lv_activity.setAdapter(activityAdapter);
		lv_activity.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(ActivityListActivity.this, ActivityDetailActivity.class);
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
		SportAdvertismentObj ad_one = new SportAdvertismentObj();
		ad_one.imageUrl = "http://a.hiphotos.baidu.com/image/pic/item/bba1cd11728b4710f197b4c1c0cec3fdfc032306.jpg";
		ad_one.redirectUrl = "http://www.baidu.com";
		advertismentlist.add(ad_one);
		advertisementControlLayout = new AdvertisementView(this);
		if (advertismentlist != null && advertismentlist.size() > 0) {
			advertisementControlLayout.setAdvertisementData(advertismentlist);
			ll_ad_layout.setVisibility(View.VISIBLE);
		}
		advertisementControlLayout.setAdvertisementRate(8, 3);
		advertisementControlLayout.setImageLoader(ImageLoader.getInstance());
		ll_ad_layout.addView(advertisementControlLayout);
	}

	class ActivityAdapter extends BaseAdapter {

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
				convertView = mLayoutInflater.inflate(R.layout.activity_item_layout, null);
				viewHolder.tv_venue = (TextView) convertView.findViewById(R.id.tv_venue);
				viewHolder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
				viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
				viewHolder.tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
				viewHolder.tv_distance = (TextView) convertView.findViewById(R.id.tv_distance);
				viewHolder.tv_sign_alredy = (TextView) convertView.findViewById(R.id.tv_sign_alredy);
				viewHolder.tv_activity_sign = (TextView) convertView.findViewById(R.id.tv_activity_sign);
				viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
				viewHolder.iv_activity_flag = (ImageView) convertView.findViewById(R.id.iv_activity_flag);
				viewHolder.iv_tag_top = (ImageView) convertView.findViewById(R.id.iv_tag_top);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			return convertView;
		}

	}

	class ViewHolder {
		TextView tv_venue;        // 场馆
		TextView tv_price;        // 价格
		ImageView imageView;        // 图片
		ImageView iv_tag_top;        // 置顶图片
		ImageView iv_activity_flag;        // 进行中
		TextView tv_time;        // 时间
		TextView tv_phone;        // 电话
		TextView tv_distance;    // 地址
		TextView tv_sign_alredy;        // 俱乐部
		TextView tv_activity_sign;    // 活动
	}
}
