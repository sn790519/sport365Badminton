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
import com.sport365.badminton.activity.view.PlayView;
import com.sport365.badminton.activity.view.PlayView.PlayListen;
import com.sport365.badminton.entity.obj.MatchEntityObj;
import com.sport365.badminton.entity.obj.SportAdvertismentObj;
import com.sport365.badminton.entity.reqbody.ActiveregistReqBody;
import com.sport365.badminton.entity.reqbody.GetMatchListReqBody;
import com.sport365.badminton.entity.resbody.ActiveRegistResBody;
import com.sport365.badminton.entity.resbody.GetMatchListResBody;
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
import com.sport365.badminton.view.DialogFactory;
import com.sport365.badminton.view.advertisement.AdvertisementView;
import com.sport365.badminton.view.pullrefresh.PullToRefreshBase;
import com.sport365.badminton.view.pullrefresh.PullToRefreshListView;

import java.util.ArrayList;

/**
 * 比赛列表页面
 *
 * @author Frank
 */
public class PlayListActivity extends BaseActivity implements PullToRefreshBase.OnRefreshListener {
	private static final int FLAG = 1;

	// 判断活动列表页面的来源的值
	public static final String ACTIVITYFROM = "ACTIVITYFROM";

	// 常量
	public static final String VENUEID = "VENUEID";
	public static final String CLUBID = "CLUBID";

	// 正常的列表的请求
	public static final int PLAYLIST = 0;

	// 运动会所点击比赛的请求
	public static final int ACTIVITYCENTERLIST = 1;
	// 社团点击比赛的请求
	public static final int CLUBLIST = 2;

	private EditText et_search_text; // 搜索输入框
	private LinearLayout ll_ad_layout; // 广告
	private Button btn_search;// 搜索用

	private PullToRefreshListView lv_play;
	private PlayAdapter clubAdapter;
	private ArrayList<SportAdvertismentObj> advertismentlist = new ArrayList<SportAdvertismentObj>(); // 广告
	private AdvertisementView advertisementControlLayout;

	// 列表
	private ArrayList<MatchEntityObj> matchTabEntity = new ArrayList<MatchEntityObj>();
	// 页码
	private PageInfo pageInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.play_layout);
		String titleName = getIntent().getStringExtra(BundleKeys.ACTIONBAETITLE);
		setActionBarTitle(TextUtils.isEmpty(titleName) ? "比赛" : titleName);
		mActionbar_right.setImageResource(R.drawable.btn_tab_choose);
		mActionbar_right.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PlayListActivity.this, CitySelectorActivity.class);
				startActivityForResult(intent, FLAG);
			}
		});
		lv_play = (PullToRefreshListView) findViewById(R.id.lv_play);
		lv_play.addHeaderView(initHeadView());
		lv_play.setMode(PullToRefreshListView.MODE_AUTO_REFRESH);
		lv_play.setOnRefreshListener(this);

		switch (getIntent().getIntExtra(ACTIVITYFROM, PLAYLIST)) {
			case PLAYLIST:// 正常列表
				init_Get_Match_List(1);
				break;
			case ACTIVITYCENTERLIST:// 运动会所进来的
				getMatchListByVenuId(1, getIntent().getStringExtra(VENUEID));
				break;

			case CLUBLIST:// 社团进来的
				getMatchListByClubId(1, getIntent().getStringExtra(CLUBID));
				break;
		}

	}

	private View initHeadView() {
		View headView = mLayoutInflater.inflate(R.layout.activity_center_headview_layout, null);
		et_search_text = (EditText) headView.findViewById(R.id.et_search_text);
		et_search_text.setHint("请输入比赛的名称");
		btn_search = (Button) headView.findViewById(R.id.btn_search);
		btn_search.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 搜索关键字
				if (et_search_text != null && !TextUtils.isEmpty(et_search_text.getText().toString())) {
					getMatchListByKeyWorld(1, et_search_text.getText().toString());
				} else {
					Utilities.showToast("请输入会所的名称关键字", mContext);
				}
			}
		});
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


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == FLAG && resultCode == Activity.RESULT_OK) {
			Utilities.showToast("返回处理", mContext);
			matchTabEntity.clear();
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
		getMatchListByCity(1, proviceid, cityid, countryid);
	}

	// 城市筛选
	private void getMatchListByCity(int page, String provinceId, String cityId, String countyId) {
		init_Get_Match_List(1, proviceid, cityid, countryid, "", "");
	}

	private void init_Get_Match_List(int page) {
		init_Get_Match_List(page, "", "", "", "", "");
	}

	private void getMatchListByVenuId(int page, String venueId) {
		init_Get_Match_List(page, "", "", "", venueId, "");
	}

	private void getMatchListByClubId(int page, String clubId) {
		init_Get_Match_List(page, "", "", "", "", clubId);
	}

	/**
	 * 比赛列表
	 */
	private void init_Get_Match_List(int page, String provinceId, String cityId, String countyId, String venueId, String clubId) {
		GetMatchListReqBody reqBody = new GetMatchListReqBody();
		reqBody.page = String.valueOf(page);
		reqBody.pageSize = SystemConfig.PAGESIZE;
		reqBody.provinceId = provinceId;
		reqBody.cityId = cityId;
		reqBody.countyId = countyId;
		reqBody.venueId = venueId;
		reqBody.clubId = clubId;
		if (page == 1) {
			sendRequestWithDialog(new ServiceRequest(mContext, new SportWebService(SportParameter.GET_MATCH_LIST), reqBody), null, new IRequestProxyCallback() {

				@Override
				public void onSuccess(HttpTaskHelper.JsonResponse jsonResponse, HttpTaskHelper.RequestInfo requestInfo) {
					ResponseContent<GetMatchListResBody> de = jsonResponse.getResponseContent(GetMatchListResBody.class);
					GetMatchListResBody resBody = de.getBody();
					successHandle(resBody);
				}

				@Override
				public void onError(ResponseContent.Header header, HttpTaskHelper.RequestInfo requestInfo) {
					// TODO Auto-generated method stub
					super.onError(header, requestInfo);
				}
			});
		} else {
			sendRequestWithNoDialog(new ServiceRequest(mContext, new SportWebService(SportParameter.GET_MATCH_LIST), reqBody), new IRequestProxyCallback() {

				@Override
				public void onSuccess(HttpTaskHelper.JsonResponse jsonResponse, HttpTaskHelper.RequestInfo requestInfo) {
					ResponseContent<GetMatchListResBody> de = jsonResponse.getResponseContent(GetMatchListResBody.class);
					GetMatchListResBody resBody = de.getBody();
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

	private void getMatchListByKeyWorld(int page, String matchName) {
		setNUll();
		GetMatchListReqBody reqBody = new GetMatchListReqBody();
		reqBody.page = String.valueOf(page);
		reqBody.pageSize = SystemConfig.PAGESIZE;
		reqBody.matchName = matchName;
		if (page == 1) {
			matchTabEntity.clear();
			clubAdapter.notifyDataSetChanged();
			lv_play.removeFooterView(getFooterView());
			sendRequestWithDialog(new ServiceRequest(mContext, new SportWebService(SportParameter.GET_MATCH_LIST), reqBody), null, new IRequestProxyCallback() {

				@Override
				public void onSuccess(HttpTaskHelper.JsonResponse jsonResponse, HttpTaskHelper.RequestInfo requestInfo) {
					ResponseContent<GetMatchListResBody> de = jsonResponse.getResponseContent(GetMatchListResBody.class);
					GetMatchListResBody resBody = de.getBody();
					successHandle(resBody);
				}

				@Override
				public void onError(ResponseContent.Header header, HttpTaskHelper.RequestInfo requestInfo) {
					// TODO Auto-generated method stub
					super.onError(header, requestInfo);
				}
			});
		} else {
			sendRequestWithNoDialog(new ServiceRequest(mContext, new SportWebService(SportParameter.GET_MATCH_LIST), reqBody), new IRequestProxyCallback() {

				@Override
				public void onSuccess(HttpTaskHelper.JsonResponse jsonResponse, HttpTaskHelper.RequestInfo requestInfo) {
					ResponseContent<GetMatchListResBody> de = jsonResponse.getResponseContent(GetMatchListResBody.class);
					GetMatchListResBody resBody = de.getBody();
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

	// 处理成功
	private void successHandle(GetMatchListResBody resBody) {
		if (resBody != null) {
			// 广告
			advertismentlist = resBody.matchAdvertismentList;
			pageInfo = resBody.pageInfo;
			initADdata();
			// 列表
			matchTabEntity.addAll(resBody.matchTabEntity);
			if (clubAdapter == null) {
				clubAdapter = new PlayAdapter(mContext, matchTabEntity);
				lv_play.setAdapter(clubAdapter);
				lv_play.onRefreshComplete();
			} else {
				clubAdapter.notifyDataSetChanged();
				lv_play.onRefreshComplete();
			}
			lv_play.setCurrentBottomAutoRefreshAble(true);
			lv_play.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Intent intent = new Intent(PlayListActivity.this, PlayDetailActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("MatchEntityObj", matchTabEntity.get(position - lv_play.getHeaderViewsCount()));
					intent.putExtras(bundle);
					startActivity(intent);
				}
			});
		}
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
				getMatchListByKeyWorld(page + 1, et_search_text.getText().toString());
				return true;
			}

			// 判断是否是城市信息
			if (TextUtils.isEmpty(proviceid) || TextUtils.isEmpty(cityid) || TextUtils.isEmpty(countryid)) {

			} else {
				getMatchListByCity(page + 1, proviceid, cityid, countryid);
				return true;
			}

			// 判断是什么进行请求的
			switch (getIntent().getIntExtra(ACTIVITYFROM, PLAYLIST)) {
				case PLAYLIST:// 正常列表
					init_Get_Match_List(page + 1);
					break;
				case ACTIVITYCENTERLIST:// 运动会所进来的
					getMatchListByVenuId(page + 1, getIntent().getStringExtra(VENUEID));
					break;

				case CLUBLIST:// 社团进来的
					getMatchListByClubId(page + 1, getIntent().getStringExtra(CLUBID));
					break;
			}
			return true;
		} else {
			lv_play.onRefreshComplete();
			if (lv_play.getFooterViewsCount() == 0) {
				lv_play.addFooterView(getFooterView(), null, false);
			}
			return false;
		}
	}

	class PlayAdapter extends BaseAdapter {
		private ArrayList<MatchEntityObj> matchTabEntity;
		private Context mContext;

		public PlayAdapter(Context mContext, ArrayList<MatchEntityObj> matchTabEntity) {
			this.matchTabEntity = matchTabEntity;
			this.mContext = mContext;
		}

		@Override
		public int getCount() {
			return matchTabEntity.size();
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
			final MatchEntityObj mMatchEntityObj = matchTabEntity.get(position);
			if (convertView == null) {
				convertView = new PlayView(mContext);
			}
			((PlayView) convertView).setDateView(mMatchEntityObj);
			((PlayView) convertView).setPlayListen(new PlayListen() {

				@Override
				public void goMapShow() {
					Utilities.showToast("地图页面", mContext);
					Intent intent = new Intent(PlayListActivity.this, MapViewActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString(MapViewActivity.LAT,  mMatchEntityObj.latitude);
					bundle.putString(MapViewActivity.LON,  mMatchEntityObj.longitude);
					intent.putExtras(bundle);
					startActivity(intent);
				}

				@Override
				public void doBookName() {
					activeRegist(matchTabEntity.get(position).matchId);
				}
			});
			return convertView;
		}
	}

	// 比赛中报名
	private void activeRegist(final String activeId) {
		if (!SystemConfig.isLogin()) {
			new DialogFactory(mContext).showDialog("", "你还没有登录，请登录。", "确定", new DialogFactory.onBtnClickListener() {

				@Override
				public void btnLeftClickListener(View v) {
					Intent intent = new Intent(mContext, LoginActivity.class);
					mContext.startActivity(intent);
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
				reqBody.typeId = "1";
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
					}
				});
			}

			@Override
			public void btnCloseClickListener(View v) {

			}
		}, true);

	}

}
