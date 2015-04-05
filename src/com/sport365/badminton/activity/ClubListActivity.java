package com.sport365.badminton.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.sport365.badminton.BaseActivity;
import com.sport365.badminton.R;
import com.sport365.badminton.activity.view.ClubView;
import com.sport365.badminton.entity.obj.ClubTabEntityObj;
import com.sport365.badminton.entity.obj.SportAdvertismentObj;
import com.sport365.badminton.entity.reqbody.GetClubListByVenueReqBody;
import com.sport365.badminton.entity.reqbody.GetclublistReqBody;
import com.sport365.badminton.entity.resbody.GetClubListByVenueResBody;
import com.sport365.badminton.entity.webservice.SportParameter;
import com.sport365.badminton.entity.webservice.SportWebService;
import com.sport365.badminton.http.base.HttpTaskHelper;
import com.sport365.badminton.http.base.IRequestProxyCallback;
import com.sport365.badminton.http.base.ImageLoader;
import com.sport365.badminton.http.json.req.ServiceRequest;
import com.sport365.badminton.http.json.res.ResponseContent;
import com.sport365.badminton.utils.BundleKeys;
import com.sport365.badminton.utils.Utilities;
import com.sport365.badminton.view.advertisement.AdvertisementView;

import java.util.ArrayList;

/**
 * 俱乐部列表页面
 *
 * @author Frank
 */
public class ClubListActivity extends BaseActivity {
	// 点击来源
	public static final String CLUBFROM = "ClubFrom";

	// 正常社团俱乐部列表
	public static final int CLUBLIST = 1;
	// 从运动会所进入社团列表
	public static final int ACTIVITYTOCLUBLIST = 2;

	// 传参数用的常量
	public static final String VENUEID = "venueId";

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
		String titleName = getIntent().getStringExtra(BundleKeys.ACTIONBAETITLE);
		setActionBarTitle(TextUtils.isEmpty(titleName) ? "社团" : titleName);
		initView();
		switch (getIntent().getIntExtra(CLUBFROM, CLUBLIST)) {
			case CLUBLIST:
				init_GET_CLUB_LIST_BYVENUE();
				break;
			case ACTIVITYTOCLUBLIST:
				String venueId = getIntent().getStringExtra(VENUEID);
				init_GET_CLUB_LIST(venueId);
				break;
		}

	}

	private void initView() {
		lv_activity_center = (ListView) findViewById(R.id.lv_club);
		lv_activity_center.addHeaderView(initHeadView());
	}

	private View initHeadView() {
		View headView = mLayoutInflater.inflate(R.layout.activity_center_headview_layout, null);
		et_search_text = (EditText) headView.findViewById(R.id.et_search_text);
		et_search_text.setHint("请输入俱乐部的名称");
		ll_ad_layout = (LinearLayout) headView.findViewById(R.id.ll_ad_layout);
		return headView;
	}


	/**
	 * 俱乐部列表
	 */
	private void init_GET_CLUB_LIST_BYVENUE() {
		GetClubListByVenueReqBody reqBody = new GetClubListByVenueReqBody();
		reqBody.page = "1";
		reqBody.pageSize = "10";
//		reqBody.provinceId = "17";
//		reqBody.cityId = "220";
//		reqBody.countyId = "2143";
		sendRequestWithDialog(new ServiceRequest(mContext, new SportWebService(SportParameter.GET_CLUB_LIST_BYVENUE), reqBody), null, new IRequestProxyCallback() {

			@Override
			public void onSuccess(HttpTaskHelper.JsonResponse jsonResponse, HttpTaskHelper.RequestInfo requestInfo) {
				ResponseContent<GetClubListByVenueResBody> de = jsonResponse.getResponseContent(GetClubListByVenueResBody.class);
				GetClubListByVenueResBody resBody = de.getBody();
				successHandle(resBody);
			}

			@Override
			public void onError(ResponseContent.Header header, HttpTaskHelper.RequestInfo requestInfo) {
				// TODO Auto-generated method stub
				super.onError(header, requestInfo);
			}
		});
	}

	// 从运动会所进入的请求
	private void init_GET_CLUB_LIST(String venueId) {
		GetclublistReqBody reqBody = new GetclublistReqBody();
		reqBody.page = "1";
		reqBody.pageSize = "10";
		reqBody.venueId = venueId;
		sendRequestWithDialog(new ServiceRequest(mContext, new SportWebService(SportParameter.GET_CLUB_LIST), reqBody), null, new IRequestProxyCallback() {

			@Override
			public void onSuccess(HttpTaskHelper.JsonResponse jsonResponse, HttpTaskHelper.RequestInfo requestInfo) {
				ResponseContent<GetClubListByVenueResBody> de = jsonResponse.getResponseContent(GetClubListByVenueResBody.class);
				GetClubListByVenueResBody resBody = de.getBody();
				successHandle(resBody);
			}

			@Override
			public void onError(ResponseContent.Header header, HttpTaskHelper.RequestInfo requestInfo) {
				// TODO Auto-generated method stub
				super.onError(header, requestInfo);
			}
		});
	}

	private void successHandle(GetClubListByVenueResBody resBody) {
		if (resBody != null) {
			//广告
			advertismentlist = resBody.clubAdvertismentList;
			initADdata();
			//数据
			clubTabEntity = resBody.clubTabEntity;
			clubAdapter = new ClubAdapter(mContext, clubTabEntity);
			lv_activity_center.setAdapter(clubAdapter);
			lv_activity_center.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Intent intent = new Intent(ClubListActivity.this, ClubDetailActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("ClubTabEntityObj", clubTabEntity.get(position - lv_activity_center.getHeaderViewsCount()));
					intent.putExtras(bundle);
					startActivity(intent);
				}
			});
		}
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

	class ClubAdapter extends BaseAdapter {
		public ArrayList<ClubTabEntityObj> clubTabEntity = new ArrayList<ClubTabEntityObj>();
		private Context mContext;

		public ClubAdapter(Context mContext, ArrayList<ClubTabEntityObj> clubTabEntity) {
			this.clubTabEntity = clubTabEntity;
			this.mContext = mContext;
		}

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
		public View getView(final int position, View convertView, ViewGroup parent) {
			final ClubTabEntityObj mClubTabEntityObj = clubTabEntity.get(position);
			if (convertView == null) {
				convertView = new ClubView(mContext);
			}
			((ClubView) convertView).setDateView(mClubTabEntityObj);
			((ClubView) convertView).setClubListen(new ClubView.ClubListen() {

				@Override
				public void lookMathces() {
					// 当数量为 0 时，不做任何处理
					if (TextUtils.isEmpty(mClubTabEntityObj.matchNum) || "0".equals(mClubTabEntityObj.matchNum)) {
						return;
					} else {
						// 请求列表
						Intent intent = new Intent(ClubListActivity.this, PlayListActivity.class);
						intent.putExtra(PlayListActivity.ACTIVITYFROM, PlayListActivity.CLUBLIST);
						intent.putExtra(PlayListActivity.CLUBID, mClubTabEntityObj.clubId);
						startActivity(intent);
					}
				}

				@Override
				public void lookActivitys() {
					// 当数量为 0 时，不做任何处理
					if (TextUtils.isEmpty(mClubTabEntityObj.activeNum) || "0".equals(mClubTabEntityObj.activeNum)) {
						return;
					} else {
						// 请求列表
						Intent intent = new Intent(ClubListActivity.this, ActivityListActivity.class);
						intent.putExtra(ActivityListActivity.ACTIVITYFROM, ActivityListActivity.CLUBLIST);
						intent.putExtra(ActivityListActivity.VENUEID, mClubTabEntityObj.clubId);
						startActivity(intent);
					}
				}

				@Override
				public void doRechange() {
					Utilities.showToast("充值页面", mContext);
					Intent intent = new Intent(ClubListActivity.this,
							MainActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
					intent.putExtra(MainActivity.PAYTYPE, MainActivity.PAYTYPE);
					startActivity(intent);
					ClubListActivity.this.finish();
				}

				@Override
				public void goMapShow() {
					Utilities.showToast("查看地图", mContext);
					Intent intent = new Intent(ClubListActivity.this, MapViewActivity.class);
					intent.putExtra(MapViewActivity.LAT, mClubTabEntityObj.latitude);
					intent.putExtra(MapViewActivity.LON, mClubTabEntityObj.longitude);
					startActivity(intent);
				}
			});
			return convertView;
		}

	}

}
