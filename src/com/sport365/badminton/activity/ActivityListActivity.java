package com.sport365.badminton.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.sport365.badminton.entity.obj.ActiveEntityObj;
import com.sport365.badminton.entity.obj.SportAdvertismentObj;
import com.sport365.badminton.entity.reqbody.GetAllActiveListReqBody;
import com.sport365.badminton.entity.resbody.GetAllActiveListResBody;
import com.sport365.badminton.entity.webservice.SportParameter;
import com.sport365.badminton.entity.webservice.SportWebService;
import com.sport365.badminton.http.base.HttpTaskHelper;
import com.sport365.badminton.http.base.IRequestProxyCallback;
import com.sport365.badminton.http.base.ImageLoader;
import com.sport365.badminton.http.json.req.ServiceRequest;
import com.sport365.badminton.http.json.res.ResponseContent;
import com.sport365.badminton.utils.BundleKeys;
import com.sport365.badminton.view.advertisement.AdvertisementView;
import org.w3c.dom.Text;

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

	public ArrayList<ActiveEntityObj> alctiveList = new ArrayList<ActiveEntityObj>();// 列表

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBarTitle(getIntent().getStringExtra(BundleKeys.ACTIONBAETITLE));
		setContentView(R.layout.activity_layout);
		lv_activity = (ListView) findViewById(R.id.lv_activity);
		lv_activity.addHeaderView(initHeadView());
		init_GET_ALL_ACTIVE_LIST();
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

	/**
	 * 活动列表
	 */
	private void init_GET_ALL_ACTIVE_LIST() {
		GetAllActiveListReqBody reqBody = new GetAllActiveListReqBody();
		reqBody.page = "1";
		reqBody.pageSize = "10";
		reqBody.provinceId = "17";
		reqBody.cityId = "220";
		reqBody.countyId = "2143";
		reqBody.activeDate = "2015-01-15";// 运动日历用
		sendRequestWithDialog(new ServiceRequest(mContext, new SportWebService(SportParameter.GET_ALL_ACTIVE_LIST), reqBody), null, new IRequestProxyCallback() {

			@Override
			public void onSuccess(HttpTaskHelper.JsonResponse jsonResponse, HttpTaskHelper.RequestInfo requestInfo) {
				ResponseContent<GetAllActiveListResBody> de = jsonResponse.getResponseContent(GetAllActiveListResBody.class);
				GetAllActiveListResBody resBody = de.getBody();
				if (resBody != null) {
					// 广告
					advertismentlist = resBody.activeAdvertismentList;
					initADdata();

					// 列表数据
					alctiveList = resBody.alctiveList;
					activityAdapter = new ActivityAdapter();
					lv_activity.setAdapter(activityAdapter);
					lv_activity.setOnItemClickListener(new AdapterView.OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
							Intent intent = new Intent(ActivityListActivity.this, ActivityDetailActivity.class);
							startActivity(intent);
						}
					});
				}
			}

			@Override
			public void onError(ResponseContent.Header header, HttpTaskHelper.RequestInfo requestInfo) {
				// TODO Auto-generated method stub
				super.onError(header, requestInfo);
			}
		});
	}

	class ActivityAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return alctiveList.size();
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
			ActiveEntityObj mActiveEntityObj = alctiveList.get(position);
			if (mActiveEntityObj != null) {
				// 场馆
				String clubName = !TextUtils.isEmpty(mActiveEntityObj.clubName)?mActiveEntityObj.clubName:"";
				viewHolder.tv_venue.setText(clubName);
				// 价格
				String activeFee = !TextUtils.isEmpty(mActiveEntityObj.activeFee)?mActiveEntityObj.activeFee:"";
				viewHolder.tv_price.setText(activeFee+"元");
				// 图片
				String activeLogo = !TextUtils.isEmpty(mActiveEntityObj.activeLogo)?mActiveEntityObj.activeLogo:"";
				mImageLoader.displayImage(activeLogo,viewHolder.imageView);
				// 时间
				String activeDate = !TextUtils.isEmpty(mActiveEntityObj.activeDate)?mActiveEntityObj.activeDate:"";
				String endTime = TextUtils.isEmpty(mActiveEntityObj.endTime)?mActiveEntityObj.endTime:"";
				viewHolder.tv_time.setText(activeDate+"--"+endTime);
				// 地址
				String provinceName = !TextUtils.isEmpty(mActiveEntityObj.provinceName)?mActiveEntityObj.provinceName:"";
				String cityName = !TextUtils.isEmpty(mActiveEntityObj.cityName)?mActiveEntityObj.cityName:"";
				String countyName = !TextUtils.isEmpty(mActiveEntityObj.countyName)?mActiveEntityObj.countyName:"";
				String venueName = !TextUtils.isEmpty(mActiveEntityObj.venueName)?mActiveEntityObj.venueName:"";
				viewHolder.tv_distance.setText(provinceName+"  "+cityName+"  "+countyName+"  "+"\n"+venueName);
				// 俱乐部
				String realNum = !TextUtils.isEmpty(mActiveEntityObj.realNum)?mActiveEntityObj.realNum:"";
				viewHolder.tv_sign_alredy.setText(realNum+"人已报名");
				// 活动
				String huiTips = !TextUtils.isEmpty(mActiveEntityObj.huiTips)?mActiveEntityObj.huiTips:"";
				viewHolder.tv_activity_sign.setText(huiTips);
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
