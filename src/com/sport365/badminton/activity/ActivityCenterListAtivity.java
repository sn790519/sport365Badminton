package com.sport365.badminton.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.sport365.badminton.BaseActivity;
import com.sport365.badminton.R;
import com.sport365.badminton.entity.obj.SportAdvertismentObj;
import com.sport365.badminton.entity.obj.VenueEntityObj;
import com.sport365.badminton.entity.reqbody.GetVenueListReqBody;
import com.sport365.badminton.entity.resbody.GetVenueListResBody;
import com.sport365.badminton.entity.webservice.SportParameter;
import com.sport365.badminton.entity.webservice.SportWebService;
import com.sport365.badminton.http.base.HttpTaskHelper;
import com.sport365.badminton.http.base.IRequestProxyCallback;
import com.sport365.badminton.http.base.ImageLoader;
import com.sport365.badminton.http.json.req.ServiceRequest;
import com.sport365.badminton.http.json.res.ResponseContent;
import com.sport365.badminton.utils.BundleKeys;
import com.sport365.badminton.view.advertisement.AdvertisementView;

/**
 * 运动会所列表页面
 *
 * @author Frank
 */
public class ActivityCenterListAtivity extends BaseActivity {

	private EditText et_search_text;                                                // 搜索输入框
	private LinearLayout ll_ad_layout;                                                // 广告

	private ListView lv_activity_center;
	private ActivityCenterAdapter activityCenterAdapter;
	private ArrayList<SportAdvertismentObj> advertismentlist = new ArrayList<SportAdvertismentObj>();    // 广告
	public ArrayList<VenueEntityObj> venueEntity = new ArrayList<VenueEntityObj>();// 列表数据
	private AdvertisementView advertisementControlLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_center_layout);
		setActionBarTitle(getIntent().getStringExtra(BundleKeys.ACTIONBAETITLE));
		lv_activity_center = (ListView) findViewById(R.id.lv_activity_center);
		lv_activity_center.addHeaderView(initHeadView());
		init_Get_Venue_List();
	}

	/**
	 * 会所列表
	 */
	private void init_Get_Venue_List() {
		GetVenueListReqBody reqBody = new GetVenueListReqBody();
		reqBody.page = "1";
		reqBody.pageSize = "10";
		reqBody.provinceId = "17";
		reqBody.cityId = "220";
		reqBody.countyId = "2143";
		sendRequestWithDialog(new ServiceRequest(mContext, new SportWebService(SportParameter.GET_VENUE_LIST), reqBody), null, new IRequestProxyCallback() {

			@Override
			public void onSuccess(HttpTaskHelper.JsonResponse jsonResponse, HttpTaskHelper.RequestInfo requestInfo) {
				ResponseContent<GetVenueListResBody> de = jsonResponse.getResponseContent(GetVenueListResBody.class);
				GetVenueListResBody resBody = de.getBody();
				if (resBody != null) {
					advertismentlist = resBody.venueAdvertismentList;
					venueEntity = resBody.venueEntity;
				}
				initADdata();
				activityCenterAdapter = new ActivityCenterAdapter();
				lv_activity_center.setAdapter(activityCenterAdapter);
				lv_activity_center.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						Intent intent = new Intent(ActivityCenterListAtivity.this, ActivityCenterDetailActivity.class);
						startActivity(intent);
					}
				});
			}

			@Override
			public void onError(ResponseContent.Header header, HttpTaskHelper.RequestInfo requestInfo) {
				// TODO Auto-generated method stub
				super.onError(header, requestInfo);
			}
		});
	}

	private View initHeadView() {
		View headView = mLayoutInflater.inflate(R.layout.activity_center_headview_layout, null);
		et_search_text = (EditText) headView.findViewById(R.id.et_search_text);
		ll_ad_layout = (LinearLayout) headView.findViewById(R.id.ll_ad_layout);
		return headView;
	}

	private void initADdata() {
		advertisementControlLayout = new AdvertisementView(this);
		if (advertismentlist != null && advertismentlist.size() > 0) {
			advertisementControlLayout.setAdvertisementData(advertismentlist);
			ll_ad_layout.setVisibility(View.VISIBLE);
		}
		advertisementControlLayout.setAdvertisementRate(8, 3);
		advertisementControlLayout.setImageLoader(ImageLoader.getInstance());
		ll_ad_layout.addView(advertisementControlLayout);
	}

	class ActivityCenterAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return venueEntity.size();
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
				convertView = mLayoutInflater.inflate(R.layout.activity_center_item_layout, null);
				viewHolder.tv_venue = (TextView) convertView.findViewById(R.id.tv_venue);
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
			VenueEntityObj venueEntityobj = venueEntity.get(position);
			if (venueEntityobj != null) {
				// 名称
				viewHolder.tv_venue.setText(!TextUtils.isEmpty(venueEntityobj.name)?venueEntityobj.name:"");
				// 图片
				mImageLoader.displayImage(!TextUtils.isEmpty(venueEntityobj.logo)?venueEntityobj.logo:"",viewHolder.imageView);
				// 时间
				String openingTime = !TextUtils.isEmpty(venueEntityobj.openingTime)?venueEntityobj.openingTime:"";
				String closingTime = !TextUtils.isEmpty(venueEntityobj.closingTime)?venueEntityobj.closingTime:"";
				viewHolder.tv_time.setText(openingTime+"--"+closingTime);
				// 地址
				String provinceName = !TextUtils.isEmpty(venueEntityobj.provinceName)?venueEntityobj.provinceName:"";
				String cityName = !TextUtils.isEmpty(venueEntityobj.cityName)?venueEntityobj.cityName:"";
				String countyName = !TextUtils.isEmpty(venueEntityobj.countyName)?venueEntityobj.countyName:"";
				String address = !TextUtils.isEmpty(venueEntityobj.address)?venueEntityobj.address:"";
				viewHolder.tv_distance.setText(provinceName+"  "+cityName+"  "+countyName+"  "+"\n"+address);
				//俱乐部
				String clubNum = !TextUtils.isEmpty(venueEntityobj.clubNum)?venueEntityobj.clubNum:"";
				viewHolder.tv_club.setText("俱乐部（"+clubNum+"）");
				//活动
				String activeNum = !TextUtils.isEmpty(venueEntityobj.activeNum)?venueEntityobj.activeNum:"";
				viewHolder.tv_club.setText("活动（"+activeNum+"）");
				//比赛
				String matchNum = !TextUtils.isEmpty(venueEntityobj.matchNum)?venueEntityobj.matchNum:"";
				viewHolder.tv_club.setText("比赛（"+matchNum+"）");
			}
			return convertView;
		}

	}

	class ViewHolder {
		TextView tv_venue;        // 场馆
		ImageView imageView;        // 图片
		TextView tv_time;        // 时间
		TextView tv_phone;        // 电话
		TextView tv_distance;    // 地址
		TextView tv_club;        // 俱乐部
		TextView tv_activity;    // 活动
		TextView tv_game;        // 比赛
	}

}
