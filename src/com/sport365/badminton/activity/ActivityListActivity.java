package com.sport365.badminton.activity;

import java.util.ArrayList;

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
import com.sport365.badminton.activity.view.ActivityView;
import com.sport365.badminton.activity.view.ActivityView.ActivityListen;
import com.sport365.badminton.entity.obj.ActiveEntityObj;
import com.sport365.badminton.entity.obj.SportAdvertismentObj;
import com.sport365.badminton.entity.reqbody.ActiveregistReqBody;
import com.sport365.badminton.entity.reqbody.GetAllActiveListReqBody;
import com.sport365.badminton.entity.reqbody.GetactivememberlistReqBody;
import com.sport365.badminton.entity.reqbody.GetnearactivelistReqBody;
import com.sport365.badminton.entity.resbody.ActiveRegistResBody;
import com.sport365.badminton.entity.resbody.GetAllActiveListResBody;
import com.sport365.badminton.entity.resbody.GetactivememberlistResBody;
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
import com.sport365.badminton.view.DialogFactory;
import com.sport365.badminton.view.advertisement.AdvertisementView;
import com.sport365.badminton.view.pullrefresh.PullToRefreshBase;
import com.sport365.badminton.view.pullrefresh.PullToRefreshListView;
import org.w3c.dom.Text;

/**
 * 活动列表页面
 *
 * @author Frank
 */
public class ActivityListActivity extends BaseActivity implements PullToRefreshBase.OnRefreshListener {

	private static final int FLAG = 1;

	// 判断活动列表页面的来源的值
	public static final String ACTIVITYFROM = "ACTIVITYFROM";
	// 正常的列表页面
	public static final int ACTIVITYLIST = 1;
	// 我身边list
	public static final int NEARACTIVITYLIST = 2;
	// 运动日历
	public static final int CADACTIVITYLIST = 3;
	//运动会所点击活动的请求
	public static final int ACTIVITYCENTERLIST = 4;
	//社团点击活动的请求
	public static final int CLUBLIST = 5;


	// 常量
	public static final String VENUEID = "VENUEID";
	public static final String CLUBID = "CLUBID";

	private EditText et_search_text; // 搜索输入框
	private LinearLayout ll_ad_layout; // 广告
	private Button btn_search;// 搜索用

	private PullToRefreshListView lv_activity;
	private ActivityAdapter activityAdapter;
	private ArrayList<SportAdvertismentObj> advertismentlist = new ArrayList<SportAdvertismentObj>(); // 广告
	private AdvertisementView advertisementControlLayout;

	public ArrayList<ActiveEntityObj> alctiveList = new ArrayList<ActiveEntityObj>();// 列表

	private String date;// 运动日历

	// 页码
	private PageInfo pageInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String titleName = getIntent().getStringExtra(BundleKeys.ACTIONBAETITLE);
		setActionBarTitle(TextUtils.isEmpty(titleName) ? "活动" : titleName);
		mActionbar_right.setImageResource(R.drawable.btn_tab_choose);
		mActionbar_right.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ActivityListActivity.this, CitySelectorActivity.class);
				startActivityForResult(intent, FLAG);
			}
		});
		setContentView(R.layout.activity_layout);
		initView();
		switch (getIntent().getIntExtra(ACTIVITYFROM, 1)) {
			case ACTIVITYLIST:// 列表
				init_GET_ALL_ACTIVE_LIST(1);
				break;
			case NEARACTIVITYLIST:// 我身边
				nearRequest(1);
				break;
			case CADACTIVITYLIST:// 价格日历
				// 运动日历来的日期
				date = getIntent().getStringExtra("date");
				cadRequest(1);
				break;
			case ACTIVITYCENTERLIST:
				String venueid = getIntent().getStringExtra(VENUEID);
				activityCenterForActivity(1, venueid);
				break;
			case CLUBLIST:
				String clubid = getIntent().getStringExtra(CLUBID);
				clubForActivity(1, clubid);
				break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == FLAG && resultCode == Activity.RESULT_OK) {
			Utilities.showToast("返回处理", mContext);
			alctiveList.clear();
			activityAdapter.notifyDataSetChanged();
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
		init_GET_ALL_ACTIVE_LIST(1, proviceid, cityid, countryid);
	}

	/**
	 * 初始化view
	 */
	private void initView() {
		lv_activity = (PullToRefreshListView) findViewById(R.id.lv_activity);
		lv_activity.addHeaderView(initHeadView());
		lv_activity.setMode(PullToRefreshListView.MODE_AUTO_REFRESH);
		lv_activity.setOnRefreshListener(this);
	}

	/**
	 * 初始化头部layout
	 *
	 * @return
	 */
	private View initHeadView() {
		View headView = mLayoutInflater.inflate(R.layout.activity_center_headview_layout, null);
		et_search_text = (EditText) headView.findViewById(R.id.et_search_text);
		et_search_text.setHint("请输入活动的名称");
		btn_search = (Button) headView.findViewById(R.id.btn_search);
		btn_search.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 搜索关键字
				if (et_search_text != null && !TextUtils.isEmpty(et_search_text.getText().toString())) {
					getActivityListBykeyword(1, et_search_text.getText().toString());
				} else {
					Utilities.showToast("请输入活动的名称关键字", mContext);
				}
			}
		});
		ll_ad_layout = (LinearLayout) headView.findViewById(R.id.ll_ad_layout);
		return headView;
	}

	/**
	 * 活动列表
	 */
	private void init_GET_ALL_ACTIVE_LIST(int page) {
		init_GET_ALL_ACTIVE_LIST(page, "", "", "");
	}

	private void init_GET_ALL_ACTIVE_LIST(int page, String provinceId, String cityId, String countyId) {
		if (et_search_text != null) {
			et_search_text.setText("");
		}
		GetAllActiveListReqBody reqBody = new GetAllActiveListReqBody();
		reqBody.page = String.valueOf(page);
		reqBody.pageSize = SystemConfig.PAGESIZE;
		reqBody.provinceId = provinceId;
		reqBody.cityId = cityId;
		reqBody.countyId = countyId;
		sendRequestWithDialog(new ServiceRequest(mContext, new SportWebService(SportParameter.GET_ALL_ACTIVE_LIST), reqBody), null, new IRequestProxyCallback() {

			@Override
			public void onSuccess(HttpTaskHelper.JsonResponse jsonResponse, HttpTaskHelper.RequestInfo requestInfo) {
				ResponseContent<GetAllActiveListResBody> de = jsonResponse.getResponseContent(GetAllActiveListResBody.class);
				GetAllActiveListResBody resBody = de.getBody();
				if (resBody != null) {
					onSuccessHandle(resBody);
				}
			}

			@Override
			public void onError(ResponseContent.Header header, HttpTaskHelper.RequestInfo requestInfo) {
				// TODO Auto-generated method stub
				super.onError(header, requestInfo);
			}
		});
	}

	// 当搜索关键字的时候需要重新为null
	private void setNUll() {
		proviceid = "";
		cityid = "";
		countryid = "";
	}

	private void getActivityListBykeyword(int page, String acitityNameKey) {
		setNUll();
		GetAllActiveListReqBody reqBody = new GetAllActiveListReqBody();
		reqBody.page = String.valueOf(page);
		reqBody.pageSize = SystemConfig.PAGESIZE;
		reqBody.activeTitle = acitityNameKey;
		if (page == 1) {
			alctiveList.clear();
			activityAdapter.notifyDataSetChanged();
			if (lv_activity != null && lv_activity.getFooterViewsCount() > 0) {
				lv_activity.removeFooterView(getFooterView());
			}
			sendRequestWithDialog(new ServiceRequest(mContext, new SportWebService(SportParameter.GET_ALL_ACTIVE_LIST), reqBody), null, new IRequestProxyCallback() {

				@Override
				public void onSuccess(HttpTaskHelper.JsonResponse jsonResponse, HttpTaskHelper.RequestInfo requestInfo) {
					ResponseContent<GetAllActiveListResBody> de = jsonResponse.getResponseContent(GetAllActiveListResBody.class);
					GetAllActiveListResBody resBody = de.getBody();
					if (resBody != null) {
						onSuccessHandle(resBody);
					}
				}

				@Override
				public void onError(ResponseContent.Header header, HttpTaskHelper.RequestInfo requestInfo) {
					// TODO Auto-generated method stub
					super.onError(header, requestInfo);
				}
			});
		} else {
			sendRequestWithNoDialog(new ServiceRequest(mContext, new SportWebService(SportParameter.GET_ALL_ACTIVE_LIST), reqBody), new IRequestProxyCallback() {

				@Override
				public void onSuccess(HttpTaskHelper.JsonResponse jsonResponse, HttpTaskHelper.RequestInfo requestInfo) {
					ResponseContent<GetAllActiveListResBody> de = jsonResponse.getResponseContent(GetAllActiveListResBody.class);
					GetAllActiveListResBody resBody = de.getBody();
					if (resBody != null) {
						onSuccessHandle(resBody);
					}
				}

				@Override
				public void onError(ResponseContent.Header header, HttpTaskHelper.RequestInfo requestInfo) {
					// TODO Auto-generated method stub
					super.onError(header, requestInfo);
				}
			});
		}
	}

	// 价格日历来的请求
	private void cadRequest(int page) {
		if (et_search_text != null) {
			et_search_text.setText("");
		}
		GetAllActiveListReqBody reqBody = new GetAllActiveListReqBody();
		reqBody.page = String.valueOf(page);
		reqBody.pageSize = SystemConfig.PAGESIZE;
		reqBody.activeDate = date;// 运动日历用
		sendRequestWithDialog(new ServiceRequest(mContext, new SportWebService(SportParameter.GET_ALL_ACTIVE_LIST), reqBody), null, new IRequestProxyCallback() {

			@Override
			public void onSuccess(HttpTaskHelper.JsonResponse jsonResponse, HttpTaskHelper.RequestInfo requestInfo) {
				ResponseContent<GetAllActiveListResBody> de = jsonResponse.getResponseContent(GetAllActiveListResBody.class);
				GetAllActiveListResBody resBody = de.getBody();
				if (resBody != null) {
					onSuccessHandle(resBody);
				}
			}

			@Override
			public void onError(ResponseContent.Header header, HttpTaskHelper.RequestInfo requestInfo) {
				// TODO Auto-generated method stub
				super.onError(header, requestInfo);
			}
		});
	}

	// 我身边的请求
	private void nearRequest(int page) {
		if (et_search_text != null) {
			et_search_text.setText("");
		}
		GetnearactivelistReqBody reqBody = new GetnearactivelistReqBody();
		reqBody.page = String.valueOf(page);
		reqBody.pageSize = SystemConfig.PAGESIZE;
		reqBody.latitude = BDLocationHelper.mCurrentLocation.getLatitude() + "";
		reqBody.longitude = BDLocationHelper.mCurrentLocation.getLongitude() + "";
		sendRequestWithDialog(new ServiceRequest(mContext, new SportWebService(SportParameter.GET_NEAR_ACTIVELIST), reqBody), null, new IRequestProxyCallback() {

			@Override
			public void onSuccess(HttpTaskHelper.JsonResponse jsonResponse, HttpTaskHelper.RequestInfo requestInfo) {
				ResponseContent<GetAllActiveListResBody> de = jsonResponse.getResponseContent(GetAllActiveListResBody.class);
				GetAllActiveListResBody resBody = de.getBody();
				if (resBody != null) {
					onSuccessHandle(resBody);
				}
			}

			@Override
			public void onError(ResponseContent.Header header, HttpTaskHelper.RequestInfo requestInfo) {
				// TODO Auto-generated method stub
				super.onError(header, requestInfo);
			}
		});
	}

	// 运动会所点击活动的请求
	private void activityCenterForActivity(int page, String venueId) {
		if (et_search_text != null) {
			et_search_text.setText("");
		}
		GetAllActiveListReqBody reqBody = new GetAllActiveListReqBody();
		reqBody.page = String.valueOf(page);
		reqBody.pageSize = SystemConfig.PAGESIZE;
		reqBody.venueId = venueId;
		sendRequestWithDialog(new ServiceRequest(mContext, new SportWebService(SportParameter.GET_ALL_ACTIVE_LIST), reqBody), null, new IRequestProxyCallback() {

			@Override
			public void onSuccess(HttpTaskHelper.JsonResponse jsonResponse, HttpTaskHelper.RequestInfo requestInfo) {
				ResponseContent<GetAllActiveListResBody> de = jsonResponse.getResponseContent(GetAllActiveListResBody.class);
				GetAllActiveListResBody resBody = de.getBody();
				if (resBody != null) {
					onSuccessHandle(resBody);
				}
			}

			@Override
			public void onError(ResponseContent.Header header, HttpTaskHelper.RequestInfo requestInfo) {
				// TODO Auto-generated method stub
				super.onError(header, requestInfo);
			}
		});
	}

	// 运动会所点击活动的请求
	private void clubForActivity(int page, String clubid) {
		if (et_search_text != null) {
			et_search_text.setText("");
		}
		GetAllActiveListReqBody reqBody = new GetAllActiveListReqBody();
		reqBody.page = String.valueOf(page);
		reqBody.pageSize = SystemConfig.PAGESIZE;
		reqBody.clubId = clubid;
		sendRequestWithDialog(new ServiceRequest(mContext, new SportWebService(SportParameter.GET_ALL_ACTIVE_LIST), reqBody), null, new IRequestProxyCallback() {

			@Override
			public void onSuccess(HttpTaskHelper.JsonResponse jsonResponse, HttpTaskHelper.RequestInfo requestInfo) {
				ResponseContent<GetAllActiveListResBody> de = jsonResponse.getResponseContent(GetAllActiveListResBody.class);
				GetAllActiveListResBody resBody = de.getBody();
				if (resBody != null) {
					onSuccessHandle(resBody);
				}
			}

			@Override
			public void onError(ResponseContent.Header header, HttpTaskHelper.RequestInfo requestInfo) {
				// TODO Auto-generated method stub
				super.onError(header, requestInfo);
			}
		});
	}

	// 处理成功
	private void onSuccessHandle(GetAllActiveListResBody resBody) {
		if (resBody != null) {
			pageInfo = resBody.pageInfo;
			// 广告
			advertismentlist = resBody.activeAdvertismentList;
			// 列表数据
			alctiveList.addAll(resBody.alctiveList);
		}
		initADdata();
		if (activityAdapter == null) {
			activityAdapter = new ActivityAdapter(mContext, alctiveList);
			lv_activity.setAdapter(activityAdapter);
			lv_activity.onRefreshComplete();
		} else {
			activityAdapter.notifyDataSetChanged();
			lv_activity.onRefreshComplete();
		}
		lv_activity.setCurrentBottomAutoRefreshAble(true);
		lv_activity.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(ActivityListActivity.this, ActivityDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("ActiveEntityObj", alctiveList.get(position - lv_activity.getHeaderViewsCount()));
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
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
				getActivityListBykeyword(page + 1, et_search_text.getText().toString());
				return true;
			}

			// 判断是否是城市信息
			if (TextUtils.isEmpty(proviceid) || TextUtils.isEmpty(cityid) || TextUtils.isEmpty(countryid)) {

			} else {
				init_GET_ALL_ACTIVE_LIST(page + 1, proviceid, cityid, countryid);
				return true;
			}

			// 判断是什么进行请求的
			switch (getIntent().getIntExtra(ACTIVITYFROM, 1)) {
				case ACTIVITYLIST:// 列表
					init_GET_ALL_ACTIVE_LIST(page + 1);
					break;
				case NEARACTIVITYLIST:// 我身边
					nearRequest(page + 1);
					break;
				case CADACTIVITYLIST:// 价格日历
					// 运动日历来的日期
					date = getIntent().getStringExtra("date");
					cadRequest(page + 1);
					break;
				case ACTIVITYCENTERLIST:
					String venueid = getIntent().getStringExtra(VENUEID);
					activityCenterForActivity(page + 1, venueid);
					break;
				case CLUBLIST:
					String clubid = getIntent().getStringExtra(CLUBID);
					clubForActivity(1, clubid);
					break;
			}
			return true;
		} else {
			lv_activity.onRefreshComplete();
			if (lv_activity.getFooterViewsCount() == 0) {
				lv_activity.addFooterView(getFooterView(), null, false);
			}
			return false;
		}
	}

	class ActivityAdapter extends BaseAdapter {
		public ArrayList<ActiveEntityObj> alctiveList = new ArrayList<ActiveEntityObj>();// 列表

		private Context mContext;

		public ActivityAdapter(Context mContext, ArrayList<ActiveEntityObj> alctiveList) {
			this.alctiveList = alctiveList;
			this.mContext = mContext;
		}

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
		public View getView(final int position, View convertView, ViewGroup parent) {
			final ActiveEntityObj mActiveEntityObj = alctiveList.get(position);
			if (convertView == null) {
				convertView = new ActivityView(mContext);
			}
			((ActivityView) convertView).setDateView(mActiveEntityObj);
			((ActivityView) convertView).setActivityListen(new ActivityListen() {

				@Override
				public void lookBookNames() {
					getActiveMemberList(alctiveList.get(position).activeId);
				}

				@Override
				public void doBook() {
					activeRegist(alctiveList.get(position).activeId);
				}

				@Override
				public void goMapShow() {
					Utilities.showToast("查看地图", mContext);
					Intent intent = new Intent(ActivityListActivity.this, MapViewActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString(MapViewActivity.LAT, mActiveEntityObj.latitude);
					bundle.putString(MapViewActivity.LON, mActiveEntityObj.longitude);
					bundle.putString(MapViewActivity.NAME, mActiveEntityObj.activeTitle);
					intent.putExtras(bundle);
					startActivity(intent);
				}
			});
			return convertView;
		}

	}

	// 活动中拉取报名列表
	private void getActiveMemberList(String activeId) {
		GetactivememberlistReqBody reqBody = new GetactivememberlistReqBody();
		reqBody.activeId = activeId;
		reqBody.typeId = "0";
		sendRequestWithDialog(new ServiceRequest(mContext, new SportWebService(SportParameter.GET_ACTIVE_MEMBERLIST), reqBody), null, new IRequestProxyCallback() {

			@Override
			public void onSuccess(HttpTaskHelper.JsonResponse jsonResponse, HttpTaskHelper.RequestInfo requestInfo) {
				ResponseContent<GetactivememberlistResBody> de = jsonResponse.getResponseContent(GetactivememberlistResBody.class);
				GetactivememberlistResBody resBody = de.getBody();
				String members = "";
				for (int i = 0; i < resBody.activeRegistList.size(); i++) {
					members = members + resBody.activeRegistList.get(i).nickName + "\n";
				}
				if (resBody != null) {
					Utilities.showDialogWithMemberName(mContext, members);
				}
			}

			@Override
			public void onError(ResponseContent.Header header, HttpTaskHelper.RequestInfo requestInfo) {
				// TODO Auto-generated method stub
				super.onError(header, requestInfo);
			}
		});
	}

	// 活动中报名
	private void activeRegist(final String activeId) {
		if (!SystemConfig.isLogin()) {
			new DialogFactory(mContext).showDialog2Btn("", "你还没有登录，请登录。", "确定", "取消", new DialogFactory.onBtnClickListener() {

				@Override
				public void btnLeftClickListener(View v) {
					startActivity(new Intent(ActivityListActivity.this, LoginActivity.class));
				}

				@Override
				public void btnNeutralClickListener(View v) {

				}

				@Override
				public void btnRightClickListener(View v) {

				}

				@Override
				public void btnCloseClickListener(View v) {

				}
			}, true);
			return;
		}

		new DialogFactory(mContext).showDialog2Btn("", "你将进行活动报名，请确认？", "取消", "确定", new DialogFactory.onBtnClickListener() {

			@Override
			public void btnLeftClickListener(View v) {
			}

			@Override
			public void btnNeutralClickListener(View v) {

			}

			@Override
			public void btnRightClickListener(View v) {
				ActiveregistReqBody reqBody = new ActiveregistReqBody();
				reqBody.activeId = activeId;
				reqBody.typeId = "0";
				reqBody.memberId = SystemConfig.memberId;
				sendRequestWithDialog(new ServiceRequest(mContext, new SportWebService(SportParameter.ACTIVE_REGIST), reqBody), null, new IRequestProxyCallback() {

					@Override
					public void onSuccess(HttpTaskHelper.JsonResponse jsonResponse, HttpTaskHelper.RequestInfo requestInfo) {
						ResponseContent<ActiveRegistResBody> de = jsonResponse.getResponseContent(ActiveRegistResBody.class);
						ActiveRegistResBody resbody = de.getBody();
						if (resbody != null) {
							Utilities.showDialogWithMemberName(mContext, resbody.returnMsg);
						} else {
							Utilities.showDialogWithMemberName(mContext, "报名失败，请联系管理员.");
						}
					}

					@Override
					public void onError(ResponseContent.Header header, HttpTaskHelper.RequestInfo requestInfo) {
						// TODO Auto-generated method stub
						super.onError(header, requestInfo);
						Utilities.showDialogWithMemberName(mContext, header.getRspDesc());
					}
				});
			}

			@Override
			public void btnCloseClickListener(View v) {

			}
		}, true);

	}

}
