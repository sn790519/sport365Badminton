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
import com.sport365.badminton.entity.obj.ClubTabEntityObj;
import com.sport365.badminton.entity.obj.SportAdvertismentObj;
import com.sport365.badminton.entity.reqbody.GetClubListByVenueReqBody;
import com.sport365.badminton.entity.resbody.GetClubListByVenueResBody;
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
 * 俱乐部列表页面
 *
 * @author Frank
 */
public class ClubListActivity extends BaseActivity {
	private EditText et_search_text; // 搜索输入框
	private LinearLayout ll_ad_layout; // 广告

	private ListView lv_activity_center;
	private ClubAdapter clubAdapter;
	private ArrayList<SportAdvertismentObj> advertismentlist = new ArrayList<SportAdvertismentObj>(); // 广告
	private AdvertisementView advertisementControlLayout;

	public ArrayList<ClubTabEntityObj> clubTabEntity = new ArrayList<ClubTabEntityObj>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.club_layout);
		setActionBarTitle(getIntent().getStringExtra(BundleKeys.ACTIONBAETITLE));
		lv_activity_center = (ListView) findViewById(R.id.lv_club);
		lv_activity_center.addHeaderView(initHeadView());
		init_GET_CLUB_LIST_BYVENUE();
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
	 * 俱乐部列表
	 */
	private void init_GET_CLUB_LIST_BYVENUE() {
		GetClubListByVenueReqBody reqBody = new GetClubListByVenueReqBody();
		reqBody.page = "1";
		reqBody.pageSize = "10";
		reqBody.provinceId = "17";
		reqBody.cityId = "220";
		reqBody.countyId = "2143";
		sendRequestWithDialog(new ServiceRequest(mContext, new SportWebService(SportParameter.GET_CLUB_LIST_BYVENUE), reqBody), null, new IRequestProxyCallback() {

			@Override
			public void onSuccess(HttpTaskHelper.JsonResponse jsonResponse, HttpTaskHelper.RequestInfo requestInfo) {
				ResponseContent<GetClubListByVenueResBody> de = jsonResponse.getResponseContent(GetClubListByVenueResBody.class);
				GetClubListByVenueResBody resBody = de.getBody();
				if (resBody != null) {
					//广告
					advertismentlist = resBody.clubAdvertismentList;
					initADdata();
					//数据
					clubTabEntity = resBody.clubTabEntity;
					clubAdapter = new ClubAdapter();
					lv_activity_center.setAdapter(clubAdapter);
					lv_activity_center.setOnItemClickListener(new AdapterView.OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
							Intent intent = new Intent(ClubListActivity.this, ClubDetailActivity.class);
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

	class ClubAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return clubTabEntity.size();
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
				viewHolder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
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
			ClubTabEntityObj mClubTabEntityObj = clubTabEntity.get(position);
			if (mClubTabEntityObj != null) {
				// 场馆
				String clubName = !TextUtils.isEmpty(mClubTabEntityObj.clubName)?mClubTabEntityObj.clubName:"";
				viewHolder.tv_venue.setText(clubName);
				// 价格
				String privce = "20元";
				viewHolder.tv_price.setText(privce);
				// 图片
				String clubLogo = !TextUtils.isEmpty(mClubTabEntityObj.clubLogo)?mClubTabEntityObj.clubLogo:"";
				mImageLoader.displayImage(clubLogo,viewHolder.imageView);
				// 时间
				viewHolder.tv_time.setText("时间");
				// 地址
				String provinceName = !TextUtils.isEmpty(mClubTabEntityObj.provinceName)?mClubTabEntityObj.provinceName:"";
				String cityName = !TextUtils.isEmpty(mClubTabEntityObj.cityName)?mClubTabEntityObj.cityName:"";
				String countyName = !TextUtils.isEmpty(mClubTabEntityObj.countyName)?mClubTabEntityObj.countyName:"";
				viewHolder.tv_distance.setText(provinceName+"  "+cityName+"  "+countyName+"\n"+"缺少");
				// 俱乐部
				String activeNum = !TextUtils.isEmpty(mClubTabEntityObj.activeNum)?mClubTabEntityObj.activeNum:"";
				viewHolder.tv_club.setText("俱乐部（"+activeNum+"）");
				// 活动
				viewHolder.tv_activity.setText("活动（"+activeNum+"）");
				String matchNum = !TextUtils.isEmpty(mClubTabEntityObj.matchNum)?mClubTabEntityObj.matchNum:"";
				// 比赛
				viewHolder.tv_game.setText("比赛（"+matchNum+"）");
			}
			return convertView;
		}

	}

	class ViewHolder {
		TextView tv_venue; // 场馆
		TextView tv_price; // 价格
		ImageView imageView; // 图片
		TextView tv_time; // 时间
		TextView tv_phone; // 电话
		TextView tv_distance; // 地址
		TextView tv_club; // 俱乐部
		TextView tv_activity; // 活动
		TextView tv_game; // 比赛
	}
}
