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
 * 比赛列表页面
 * 
 * @author Frank
 * 
 */
public class PlayListActivity extends BaseActivity {

	private EditText et_search_text;                                                // 搜索输入框
	private LinearLayout ll_ad_layout;                                                // 广告

	private ListView lv_play;
	private ClubAdapter clubAdapter;
	private ArrayList<SportAdvertismentObj> advertismentlist = new ArrayList<SportAdvertismentObj>();    // 广告
	private AdvertisementView advertisementControlLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBarTitle("比赛");
		setContentView(R.layout.play_layout);
		lv_play = (ListView) findViewById(R.id.lv_play);
		lv_play.addHeaderView(initHeadView());
		clubAdapter = new ClubAdapter();
		lv_play.setAdapter(clubAdapter);
		lv_play.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(PlayListActivity.this, PlayDetailActivity.class);
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
				convertView = mLayoutInflater.inflate(R.layout.play_item_layout, null);
				viewHolder.tv_play_activity_pic = (TextView) convertView.findViewById(R.id.tv_play_activity_pic);
				viewHolder.tv_paly_name = (TextView) convertView.findViewById(R.id.tv_paly_name);
				viewHolder.tv_play_num = (TextView) convertView.findViewById(R.id.tv_play_num);
				viewHolder.tv_play_price = (TextView) convertView.findViewById(R.id.tv_play_price);
				viewHolder.tv_time_on = (TextView) convertView.findViewById(R.id.tv_time_on);
				viewHolder.tv_place_big = (TextView) convertView.findViewById(R.id.tv_place_big);
				viewHolder.tv_place_small = (TextView) convertView.findViewById(R.id.tv_place_small);
				viewHolder.ll_bottom = (LinearLayout) convertView.findViewById(R.id.ll_bottom);
				viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			return convertView;
		}

	}

	class ViewHolder {
		TextView tv_play_activity_pic;        //周期活动
		ImageView imageView;        // 图片
		TextView tv_paly_name;        // 活动名称
		TextView tv_play_num;        // 活动报名的人数
		TextView tv_play_price;    // 价格
		TextView tv_time_on;        // 时间
		TextView tv_place_big;    // 大区域
		TextView tv_place_small;        // 小区域
		LinearLayout ll_bottom;// 底部报名的按钮
	}

}
