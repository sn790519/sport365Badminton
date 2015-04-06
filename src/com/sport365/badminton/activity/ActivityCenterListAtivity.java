package com.sport365.badminton.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.sport365.badminton.BaseActivity;
import com.sport365.badminton.R;
import com.sport365.badminton.activity.view.ActivityCenterView;
import com.sport365.badminton.entity.obj.SportAdvertismentObj;
import com.sport365.badminton.entity.obj.VenueEntityObj;
import com.sport365.badminton.entity.reqbody.GetVenueListReqBody;
import com.sport365.badminton.entity.reqbody.GetnearvenuelistReqBody;
import com.sport365.badminton.entity.resbody.GetVenueListResBody;
import com.sport365.badminton.entity.resbody.PageInfo;
import com.sport365.badminton.entity.webservice.SportParameter;
import com.sport365.badminton.entity.webservice.SportWebService;
import com.sport365.badminton.http.base.HttpTaskHelper;
import com.sport365.badminton.http.base.IRequestProxyCallback;
import com.sport365.badminton.http.base.ImageLoader;
import com.sport365.badminton.http.json.req.ServiceRequest;
import com.sport365.badminton.http.json.res.ResponseContent;
import com.sport365.badminton.map.BDLocationHelper;
import com.sport365.badminton.utils.BundleKeys;
import com.sport365.badminton.utils.SystemConfig;
import com.sport365.badminton.utils.Utilities;
import com.sport365.badminton.view.advertisement.AdvertisementView;
import com.sport365.badminton.view.pullrefresh.PullToRefreshBase;
import com.sport365.badminton.view.pullrefresh.PullToRefreshListView;

import java.util.ArrayList;

/**
 * 运动会所列表页面
 *
 * @author Frank
 */
public class ActivityCenterListAtivity extends BaseActivity implements PullToRefreshBase.OnRefreshListener {
	// 来源的标记
	public static final String ACTIVITYCENTERFROM = "activitycenterfrom";
	// 正常的列表
	public static final int ACTIVITYCENTERLIST = 1;
	// 我身边的来
	public static final int ACTIVITYCENTERNEATLIST = 2;

	private EditText et_search_text; // 搜索输入框
	private LinearLayout ll_ad_layout; // 广告
	private Button btn_search;// 搜索用

	private PullToRefreshListView lv_activity_center;
	private ActivityCenterAdapter activityCenterAdapter;
	private ArrayList<SportAdvertismentObj> advertismentlist = new ArrayList<SportAdvertismentObj>(); // 广告
	public ArrayList<VenueEntityObj> venueEntity = new ArrayList<VenueEntityObj>();// 列表数据
	private AdvertisementView advertisementControlLayout;

	// 页码
	private PageInfo pageInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_center_layout);
		String titleName = getIntent().getStringExtra(BundleKeys.ACTIONBAETITLE);
		setActionBarTitle(TextUtils.isEmpty(titleName) ? "运动会所" : titleName);
		initView();
		switch (getIntent().getIntExtra(ACTIVITYCENTERFROM, 1)) {
			case ACTIVITYCENTERLIST:
				init_Get_Venue_List(1);
				break;
			case ACTIVITYCENTERNEATLIST:
				nearActivitycenterList(1);
				break;

		}
	}

	private void initView() {
		lv_activity_center = (PullToRefreshListView) findViewById(R.id.lv_activity_center);
		lv_activity_center.addHeaderView(initHeadView());
		lv_activity_center.setMode(PullToRefreshListView.MODE_AUTO_REFRESH);
		lv_activity_center.setOnRefreshListener(this);
	}

	/**
	 * 初始化头部layout
	 *
	 * @return
	 */
	private View initHeadView() {
		View headView = mLayoutInflater.inflate(R.layout.activity_center_headview_layout, null);
		et_search_text = (EditText) headView.findViewById(R.id.et_search_text);
		et_search_text.setHint("请输入会所的名称");
		ll_ad_layout = (LinearLayout) headView.findViewById(R.id.ll_ad_layout);
		btn_search = (Button) headView.findViewById(R.id.btn_search);
		btn_search.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 搜索关键字
				if (et_search_text != null && !TextUtils.isEmpty(et_search_text.getText().toString())) {
					getVenueListBykeyword(1, et_search_text.getText().toString());
				} else {
					Utilities.showToast("请输入会所的名称关键字", mContext);
				}
			}
		});
		return headView;
	}

	/*
	*会所列表
	 */
	private void init_Get_Venue_List(int page) {
		init_Get_Venue_List(page, "", "", "");
	}

	/**
	 * 会所列表
	 */
	private void init_Get_Venue_List(int page, String provinceId, String cityId, String countyId) {
		if (et_search_text != null) {
			et_search_text.setText("");
		}
		final GetVenueListReqBody reqBody = new GetVenueListReqBody();
		reqBody.page = String.valueOf(page);
		reqBody.pageSize = SystemConfig.PAGESIZE;
		reqBody.provinceId = provinceId;
		reqBody.cityId = cityId;
		reqBody.countyId = countyId;
		if (page == 1) {
			sendRequestWithDialog(new ServiceRequest(mContext, new SportWebService(SportParameter.GET_VENUE_LIST), reqBody), null, new IRequestProxyCallback() {

				@Override
				public void onSuccess(HttpTaskHelper.JsonResponse jsonResponse, HttpTaskHelper.RequestInfo requestInfo) {
					ResponseContent<GetVenueListResBody> de = jsonResponse.getResponseContent(GetVenueListResBody.class);
					GetVenueListResBody resBody = de.getBody();
					onSuccessHandle(resBody);
				}

				@Override
				public void onError(ResponseContent.Header header, HttpTaskHelper.RequestInfo requestInfo) {
					// TODO Auto-generated method stub
					super.onError(header, requestInfo);
				}
			});
		} else {
			sendRequestWithNoDialog(new ServiceRequest(mContext, new SportWebService(SportParameter.GET_VENUE_LIST), reqBody), new IRequestProxyCallback() {

				@Override
				public void onSuccess(HttpTaskHelper.JsonResponse jsonResponse, HttpTaskHelper.RequestInfo requestInfo) {
					ResponseContent<GetVenueListResBody> de = jsonResponse.getResponseContent(GetVenueListResBody.class);
					GetVenueListResBody resBody = de.getBody();
					onSuccessHandle(resBody);
				}

				@Override
				public void onError(ResponseContent.Header header, HttpTaskHelper.RequestInfo requestInfo) {
					// TODO Auto-generated method stub
					super.onError(header, requestInfo);
				}
			});
		}
	}

	/**
	 * 根据关键字搜索
	 *
	 * @param page
	 * @param venueNameKey
	 */
	private void getVenueListBykeyword(int page, String venueNameKey) {
		final GetVenueListReqBody reqBody = new GetVenueListReqBody();
		reqBody.page = String.valueOf(page);
		reqBody.pageSize = SystemConfig.PAGESIZE;
		reqBody.venueName = venueNameKey;
		if (page == 1) {
			// 关键字搜索时候，清空数据
			venueEntity.clear();
			activityCenterAdapter.notifyDataSetChanged();
			if (lv_activity_center != null && lv_activity_center.getFooterViewsCount() > 0) {
				lv_activity_center.removeFooterView(getFooterView());
			}
			sendRequestWithDialog(new ServiceRequest(mContext, new SportWebService(SportParameter.GET_VENUE_LIST), reqBody), null, new IRequestProxyCallback() {

				@Override
				public void onSuccess(HttpTaskHelper.JsonResponse jsonResponse, HttpTaskHelper.RequestInfo requestInfo) {
					ResponseContent<GetVenueListResBody> de = jsonResponse.getResponseContent(GetVenueListResBody.class);
					GetVenueListResBody resBody = de.getBody();
					onSuccessHandle(resBody);
				}

				@Override
				public void onError(ResponseContent.Header header, HttpTaskHelper.RequestInfo requestInfo) {
					// TODO Auto-generated method stub
					super.onError(header, requestInfo);
				}
			});
		} else {
			sendRequestWithNoDialog(new ServiceRequest(mContext, new SportWebService(SportParameter.GET_VENUE_LIST), reqBody), new IRequestProxyCallback() {

				@Override
				public void onSuccess(HttpTaskHelper.JsonResponse jsonResponse, HttpTaskHelper.RequestInfo requestInfo) {
					ResponseContent<GetVenueListResBody> de = jsonResponse.getResponseContent(GetVenueListResBody.class);
					GetVenueListResBody resBody = de.getBody();
					onSuccessHandle(resBody);
				}

				@Override
				public void onError(ResponseContent.Header header, HttpTaskHelper.RequestInfo requestInfo) {
					// TODO Auto-generated method stub
					super.onError(header, requestInfo);
				}
			});
		}
	}

	// 我身边运动会所的列表
	private void nearActivitycenterList(int page) {
		if (et_search_text != null) {
			et_search_text.setText("");
		}
		GetnearvenuelistReqBody reqBody = new GetnearvenuelistReqBody();
		reqBody.page = String.valueOf(page);
		reqBody.pageSize = SystemConfig.PAGESIZE;
		reqBody.latitude = BDLocationHelper.mCurrentLocation.getLatitude() + "";
		reqBody.longitude = BDLocationHelper.mCurrentLocation.getLongitude() + "";
		if (page == 1) {
			sendRequestWithDialog(new ServiceRequest(mContext, new SportWebService(SportParameter.GET_NEAR_VENUELIST), reqBody), null, new IRequestProxyCallback() {

				@Override
				public void onSuccess(HttpTaskHelper.JsonResponse jsonResponse, HttpTaskHelper.RequestInfo requestInfo) {
					ResponseContent<GetVenueListResBody> de = jsonResponse.getResponseContent(GetVenueListResBody.class);
					GetVenueListResBody resBody = de.getBody();
					onSuccessHandle(resBody);
				}

				@Override
				public void onError(ResponseContent.Header header, HttpTaskHelper.RequestInfo requestInfo) {
					// TODO Auto-generated method stub
					super.onError(header, requestInfo);
				}
			});
		} else {
			sendRequestWithNoDialog(new ServiceRequest(mContext, new SportWebService(SportParameter.GET_NEAR_VENUELIST), reqBody), new IRequestProxyCallback() {

				@Override
				public void onSuccess(HttpTaskHelper.JsonResponse jsonResponse, HttpTaskHelper.RequestInfo requestInfo) {
					ResponseContent<GetVenueListResBody> de = jsonResponse.getResponseContent(GetVenueListResBody.class);
					GetVenueListResBody resBody = de.getBody();
					onSuccessHandle(resBody);
				}

				@Override
				public void onError(ResponseContent.Header header, HttpTaskHelper.RequestInfo requestInfo) {
					// TODO Auto-generated method stub
					super.onError(header, requestInfo);
				}
			});
		}
	}

	private void onSuccessHandle(GetVenueListResBody resBody) {
		if (resBody != null) {
			advertismentlist = resBody.venueAdvertismentList;
			pageInfo = resBody.pageInfo;
			// 增加数据
			venueEntity.addAll(resBody.venueEntity);
		}
		initADdata();
		if (activityCenterAdapter == null) {
			activityCenterAdapter = new ActivityCenterAdapter(mContext, venueEntity);
			lv_activity_center.setAdapter(activityCenterAdapter);
			lv_activity_center.onRefreshComplete();
		} else {
			activityCenterAdapter.notifyDataSetChanged();
			lv_activity_center.onRefreshComplete();
		}
		lv_activity_center.setCurrentBottomAutoRefreshAble(true);
		lv_activity_center.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(ActivityCenterListAtivity.this, ActivityCenterDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("VenueEntityObj", venueEntity.get(position - lv_activity_center.getHeaderViewsCount()));
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
	}

	/**
	 * 初始化广告信息
	 */
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
				getVenueListBykeyword(page + 1, et_search_text.getText().toString());
				return true;
			}
			// 判断是什么进行请求的
			switch (getIntent().getIntExtra(ACTIVITYCENTERFROM, 1)) {
				case ACTIVITYCENTERLIST:
					init_Get_Venue_List(page + 1);
					break;
				case ACTIVITYCENTERNEATLIST:
					nearActivitycenterList(page + 1);
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

	class ActivityCenterAdapter extends BaseAdapter {
		private ArrayList<VenueEntityObj> venueEntity = new ArrayList<VenueEntityObj>();
		private Context mContext;

		public ActivityCenterAdapter(Context mContext, ArrayList<VenueEntityObj> venueEntity) {
			this.venueEntity = venueEntity;
			this.mContext = mContext;
		}

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
		public View getView(final int position, View convertView, ViewGroup parent) {
			final VenueEntityObj venueEntityobj = venueEntity.get(position);
			if (convertView == null) {
				convertView = new ActivityCenterView(mContext);
			}
			((ActivityCenterView) convertView).setDateView(venueEntityobj);
			((ActivityCenterView) convertView).setActivityCenterListen(new ActivityCenterView.ActivityCenterListen() {
				@Override
				public void lookTeam() {
					// 当数量为 0 时，不做任何处理
					if (TextUtils.isEmpty(venueEntity.get(position).clubNum) || "0".equals(venueEntity.get(position).clubNum)) {
						return;
					} else {
						// 请求列表
						Intent intent = new Intent(ActivityCenterListAtivity.this, ClubListActivity.class);
						intent.putExtra(ClubListActivity.CLUBFROM, ClubListActivity.ACTIVITYTOCLUBLIST);
						intent.putExtra(ClubListActivity.VENUEID, venueEntity.get(position).venueId);
						startActivity(intent);
					}

				}

				@Override
				public void lookActivity() {
					// 当数量为 0 时，不做任何处理
					if (TextUtils.isEmpty(venueEntity.get(position).activeNum) || "0".equals(venueEntity.get(position).activeNum)) {
						return;
					} else {
						// 请求列表
						Intent intent = new Intent(ActivityCenterListAtivity.this, ActivityListActivity.class);
						intent.putExtra(ActivityListActivity.ACTIVITYFROM, ActivityListActivity.ACTIVITYCENTERLIST);
						intent.putExtra(ActivityListActivity.VENUEID, venueEntity.get(position).venueId);
						startActivity(intent);
					}
				}

				@Override
				public void lookMathce() {
					// 当数量为 0 时，不做任何处理
					if (TextUtils.isEmpty(venueEntity.get(position).matchNum) || "0".equals(venueEntity.get(position).matchNum)) {
						return;
					} else {
						// 请求列表
						Intent intent = new Intent(ActivityCenterListAtivity.this, PlayListActivity.class);
						intent.putExtra(PlayListActivity.ACTIVITYFROM, PlayListActivity.ACTIVITYCENTERLIST);
						intent.putExtra(PlayListActivity.VENUEID, venueEntity.get(position).venueId);
						startActivity(intent);
					}
				}

				@Override
				public void goMapShow() {
					Intent intent = new Intent(ActivityCenterListAtivity.this, MapViewActivity.class);
					intent.putExtra(MapViewActivity.LAT, venueEntityobj.latitude);
					intent.putExtra(MapViewActivity.LON, venueEntityobj.longitude);
					startActivity(intent);
				}
			});
			return convertView;
		}

	}

}
