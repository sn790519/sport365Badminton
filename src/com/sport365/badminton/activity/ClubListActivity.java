package com.sport365.badminton.activity;

import android.app.Activity;
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
import com.sport365.badminton.entity.resbody.PageInfo;
import com.sport365.badminton.entity.webservice.SportParameter;
import com.sport365.badminton.entity.webservice.SportWebService;
import com.sport365.badminton.http.base.HttpTaskHelper;
import com.sport365.badminton.http.base.IRequestProxyCallback;
import com.sport365.badminton.http.base.ImageLoader;
import com.sport365.badminton.http.json.req.ServiceRequest;
import com.sport365.badminton.http.json.res.ResponseContent;
import com.sport365.badminton.utils.BundleKeys;
import com.sport365.badminton.utils.SystemConfig;
import com.sport365.badminton.utils.Utilities;
import com.sport365.badminton.view.advertisement.AdvertisementView;
import com.sport365.badminton.view.pullrefresh.PullToRefreshBase;
import com.sport365.badminton.view.pullrefresh.PullToRefreshListView;

import java.util.ArrayList;

/**
 * 俱乐部列表页面
 *
 * @author Frank
 */
public class ClubListActivity extends BaseActivity implements PullToRefreshBase.OnRefreshListener {
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

	private PullToRefreshListView lv_activity_center;
	private ClubAdapter clubAdapter;
	private ArrayList<SportAdvertismentObj> advertismentlist = new ArrayList<SportAdvertismentObj>(); // 广告
	private AdvertisementView advertisementControlLayout;

	public ArrayList<ClubTabEntityObj> clubTabEntity = new ArrayList<ClubTabEntityObj>();
	// 页码
	private PageInfo pageInfo;
	private Button btn_search;// 搜索用

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.club_layout);
		String titleName = getIntent().getStringExtra(BundleKeys.ACTIONBAETITLE);
		setActionBarTitle(TextUtils.isEmpty(titleName) ? "社团" : titleName);
		mActionbar_right.setImageResource(R.drawable.btn_tab_choose);
		mActionbar_right.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ClubListActivity.this, CitySelectorActivity.class);
				startActivityForResult(intent, FLAG);
			}
		});
		initView();
		switch (getIntent().getIntExtra(CLUBFROM, CLUBLIST)) {
			case CLUBLIST:
				init_GET_CLUB_LIST_BYVENUE(1);
				break;
			case ACTIVITYTOCLUBLIST:
				String venueId = getIntent().getStringExtra(VENUEID);
				init_GET_CLUB_LIST(1, venueId);
				break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == FLAG && resultCode == Activity.RESULT_OK) {
			Utilities.showToast("返回处理", mContext);
			clubTabEntity.clear();
			clubAdapter.notifyDataSetChanged();
			initBackDate(data);
		}
	}

	private String proviceid = "";
	private String cityid = "";
	private String countryid = "";

	// 处理省市页面的数据
	private void initBackDate(Intent data) {
		proviceid = data.getStringExtra(CitySelectorActivity.PROVINCE);
		cityid = data.getStringExtra(CitySelectorActivity.CITY);
		countryid = data.getStringExtra(CitySelectorActivity.COUNTRY);
		init_GET_CLUB_LIST_BYVENUE(1, proviceid, cityid, countryid);
	}

	private static final int FLAG = 1;

	private void initView() {
		lv_activity_center = (PullToRefreshListView) findViewById(R.id.lv_club);
		lv_activity_center.addHeaderView(initHeadView());
		lv_activity_center.setMode(PullToRefreshListView.MODE_AUTO_REFRESH);
		lv_activity_center.setOnRefreshListener(this);
	}

	private View initHeadView() {
		View headView = mLayoutInflater.inflate(R.layout.activity_center_headview_layout, null);
		et_search_text = (EditText) headView.findViewById(R.id.et_search_text);
		et_search_text.setHint("请输入俱乐部的名称");
		btn_search = (Button) headView.findViewById(R.id.btn_search);
		btn_search.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 搜索关键字
				if (et_search_text != null && !TextUtils.isEmpty(et_search_text.getText().toString())) {
					getClubListByKey(1, et_search_text.getText().toString());
				} else {
					Utilities.showToast("请输入会所的名称关键字", mContext);
				}
			}
		});
		ll_ad_layout = (LinearLayout) headView.findViewById(R.id.ll_ad_layout);
		return headView;
	}

	private void init_GET_CLUB_LIST_BYVENUE(int page) {
		init_GET_CLUB_LIST_BYVENUE(page, "", "", "");
	}

	/**
	 * 俱乐部列表
	 */
	private void init_GET_CLUB_LIST_BYVENUE(int page, String provinceId, String cityId, String countyId) {
		GetClubListByVenueReqBody reqBody = new GetClubListByVenueReqBody();
		reqBody.page = String.valueOf(page);
		reqBody.pageSize = SystemConfig.PAGESIZE;
		reqBody.provinceId = provinceId;
		reqBody.cityId = cityId;
		reqBody.countyId = countyId;
		if (page == 1) {
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
		} else {
			sendRequestWithNoDialog(new ServiceRequest(mContext, new SportWebService(SportParameter.GET_CLUB_LIST), reqBody), new IRequestProxyCallback() {

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
	}

	// 当搜索关键字的时候需要重新为null
	private void setNUll() {
		proviceid = "";
		cityid = "";
		countryid = "";
	}

	private void getClubListByKey(int page, String clubName) {
		setNUll();
		GetClubListByVenueReqBody reqBody = new GetClubListByVenueReqBody();
		reqBody.page = String.valueOf(page);
		reqBody.pageSize = SystemConfig.PAGESIZE;
		reqBody.clubName = clubName;
		if (page == 1) {
			clubTabEntity.clear();
			clubAdapter.notifyDataSetChanged();
			lv_activity_center.removeFooterView(getFooterView());
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
		} else {
			sendRequestWithNoDialog(new ServiceRequest(mContext, new SportWebService(SportParameter.GET_CLUB_LIST), reqBody), new IRequestProxyCallback() {

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

	}

	// 从运动会所进入的请求
	private void init_GET_CLUB_LIST(int page, String venueId) {
		GetclublistReqBody reqBody = new GetclublistReqBody();
		reqBody.page = String.valueOf(page);
		reqBody.pageSize = SystemConfig.PAGESIZE;
		reqBody.venueId = venueId;
		if (page == 1) {
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
		} else {
			sendRequestWithNoDialog(new ServiceRequest(mContext, new SportWebService(SportParameter.GET_CLUB_LIST_BYVENUE), reqBody), new IRequestProxyCallback() {

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
	}

	private void successHandle(GetClubListByVenueResBody resBody) {
		if (resBody != null) {
			//广告
			advertismentlist = resBody.clubAdvertismentList;
			pageInfo = resBody.pageInfo;
			initADdata();
			//数据
			clubTabEntity.addAll(resBody.clubTabEntity);
			if (clubAdapter == null) {
				clubAdapter = new ClubAdapter(mContext, clubTabEntity);
				lv_activity_center.setAdapter(clubAdapter);
				lv_activity_center.onRefreshComplete();
			} else {
				clubAdapter.notifyDataSetChanged();
				lv_activity_center.onRefreshComplete();
			}
			lv_activity_center.setCurrentBottomAutoRefreshAble(true);
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

	@Override
	public boolean onRefresh(int curMode) {
		// 判断页码
		int page = 1;
		int totalPage = 1;
		if (pageInfo != null) {
			if (!TextUtils.isEmpty(pageInfo.page)) {
				try {
					page = Integer.valueOf(pageInfo.page);
				} catch (Exception e) {
					page = 1;
				}
			}
			if (!TextUtils.isEmpty(pageInfo.totalPage)) {
				try {
					totalPage = Integer.valueOf(pageInfo.totalPage);
				} catch (Exception e) {
					totalPage = 1;
				}
			}

		}
		if (page < totalPage) {
			// 判断关键字搜索 优先级高
			if (et_search_text != null && !TextUtils.isEmpty(et_search_text.getText().toString())) {
				getClubListByKey(page + 1, et_search_text.getText().toString());
				return true;
			}

			// 判断是否是城市信息
			if (TextUtils.isEmpty(proviceid) || TextUtils.isEmpty(cityid) || TextUtils.isEmpty(countryid)) {

			} else {
				init_GET_CLUB_LIST_BYVENUE(page + 1, proviceid, cityid, countryid);
				return true;
			}

			// 判断是什么进行请求的
			switch (getIntent().getIntExtra(CLUBFROM, CLUBLIST)) {
				case CLUBLIST:
					init_GET_CLUB_LIST_BYVENUE(page + 1);
					break;
				case ACTIVITYTOCLUBLIST:
					String venueId = getIntent().getStringExtra(VENUEID);
					init_GET_CLUB_LIST(page + 1, venueId);
					break;
			}
			return true;
		} else {
			lv_activity_center.onRefreshComplete();
			if (lv_activity_center.getFooterViewsCount() == 0) {
				lv_activity_center.addFooterView(getFooterView(), null, false);
			}
			return false;
		}
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
