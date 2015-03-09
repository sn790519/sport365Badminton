package com.sport365.badminton.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.sport365.badminton.BaseActivity;
import com.sport365.badminton.R;
import com.sport365.badminton.activity.fragment.MapViewFragment;
import com.sport365.badminton.activity.view.*;
import com.sport365.badminton.entity.obj.*;
import com.sport365.badminton.entity.reqbody.GetVenueDetailByIdReqBody;
import com.sport365.badminton.entity.resbody.GetVenueDetailByIdResBody;
import com.sport365.badminton.entity.webservice.SportParameter;
import com.sport365.badminton.entity.webservice.SportWebService;
import com.sport365.badminton.http.base.HttpTaskHelper;
import com.sport365.badminton.http.base.IRequestProxyCallback;
import com.sport365.badminton.http.base.ImageLoader;
import com.sport365.badminton.http.json.req.ServiceRequest;
import com.sport365.badminton.http.json.res.ResponseContent;
import com.sport365.badminton.params.SystemConfig;
import com.sport365.badminton.view.advertisement.AdvertisementView;

import java.util.ArrayList;

/**
 * 运动会所的详情页面
 *
 * @author Frank
 */
public class ActivityCenterDetailActivity extends BaseActivity implements MapViewFragment.OnRoutePlanSuccessListener {

	private LinearLayout ll_ad_layout;// 广告
	private ArrayList<SportAdvertismentObj> advertismentlist = new ArrayList<SportAdvertismentObj>(); // 广告
	private AdvertisementView advertisementControlLayout;
	private LinearLayout ll_title_layout;
	private LinearLayout ll_tab;
	private LinearLayout ll_content;
	private VenueEntityObj venueEntityObj;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_center_detail_layout);
		setActionBarTitle("运动会所详情");
		initData();
		initView();
		init_GET_VENUE_DETAIL_BYID();
	}

	private void initData() {
		venueEntityObj = (VenueEntityObj) getIntent().getSerializableExtra("VenueEntityObj");
	}

	private void initView() {
		ll_ad_layout = (LinearLayout) findViewById(R.id.ll_ad_layout);
		ll_title_layout = (LinearLayout) findViewById(R.id.ll_title_layout);
		ll_tab = (LinearLayout) findViewById(R.id.ll_tab);
		ll_content = (LinearLayout) findViewById(R.id.ll_content);
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
	 * 初始化头部信息
	 */
	private void initTitleLayout() {
		ActivityCenterView activityCenterView = new ActivityCenterView(mContext);
		activityCenterView.setDateView(venueEntityObj);
		activityCenterView.setBottonVisible(View.GONE);
		ll_title_layout.addView(activityCenterView);
	}


	/**
	 * 会所详情
	 */
	// 活动的列表
	public ArrayList<ActiveEntityObj> activeList = new ArrayList<ActiveEntityObj>();
	// 俱乐部的列表
	public ArrayList<ClubTabEntityObj> clubList = new ArrayList<ClubTabEntityObj>();
	// 比赛列表
	public ArrayList<MatchEntityObj> matchList = new ArrayList<MatchEntityObj>();

	private void init_GET_VENUE_DETAIL_BYID() {
		final GetVenueDetailByIdReqBody reqBody = new GetVenueDetailByIdReqBody();
		reqBody.venueId = "6";
		sendRequestWithDialog(new ServiceRequest(mContext, new SportWebService(SportParameter.GET_VENUE_DETAIL_BYID), reqBody), null, new IRequestProxyCallback() {

			@Override
			public void onSuccess(HttpTaskHelper.JsonResponse jsonResponse, HttpTaskHelper.RequestInfo requestInfo) {
				ResponseContent<GetVenueDetailByIdResBody> de = jsonResponse.getResponseContent(GetVenueDetailByIdResBody.class);
				GetVenueDetailByIdResBody resBody = de.getBody();
				if (resBody != null) {
					activeList = resBody.activeList;
					clubList = resBody.clubList;
					matchList = resBody.matchList;
					advertismentlist = resBody.venueAdvertismentList;
					initADdata();
					initTitleLayout();
					initTabLayout();
					addActivityListView(activeList);
				}

			}

			@Override
			public void onError(ResponseContent.Header header, HttpTaskHelper.RequestInfo requestInfo) {
				// TODO Auto-generated method stub
				super.onError(header, requestInfo);
			}
		});
	}

	/**
	 * 初始化tab信息
	 */
	private void initTabLayout() {
		ll_tab.addView(new SportRadioGroupView(mContext, null, null).setSportCheckListen(new SportRadioGroupView.SportCheckListen() {
			@Override
			public void FirstOnClick() {
				Toast.makeText(mContext, "rb_menu_first", Toast.LENGTH_LONG).show();
				addActivityListView(activeList);
			}

			@Override
			public void SecondOnClick() {
				Toast.makeText(mContext, "rb_menu_second", Toast.LENGTH_LONG).show();
				addMapView();
			}

			@Override
			public void ThirdOnClick() {
				Toast.makeText(mContext, "rb_menu_third", Toast.LENGTH_LONG).show();
				addClubListView(clubList);
			}

			@Override
			public void FourOnClick() {
				Toast.makeText(mContext, "rb_menu_four", Toast.LENGTH_LONG).show();
				addMatchListView(matchList);
			}
		}));
	}

	/**
	 * 加入活动的列表的view
	 */
	private void addActivityListView(ArrayList<ActiveEntityObj> activeList) {
		ll_content.removeAllViews();
		setMatchLayoutParams();
		for (int i = 0; activeList != null && i < activeList.size(); i++) {
			ActivityView activityView = new ActivityView(mContext);
			activityView.setDateView(activeList.get(i));
			ll_content.addView(activityView);
		}
	}


	/**
	 * 加入俱乐部列表的View
	 *
	 * @param clubList
	 */
	private void addClubListView(ArrayList<ClubTabEntityObj> clubList) {
		ll_content.removeAllViews();
		setMatchLayoutParams();
		for (int i = 0; clubList != null && i < clubList.size(); i++) {
			ClubView clubView = new ClubView(mContext);
			clubView.setDateView(clubList.get(i));
			ll_content.addView(clubView);
		}
	}

	/**
	 * 加入比赛列表的view
	 *
	 * @param matchList
	 */
	private void addMatchListView(ArrayList<MatchEntityObj> matchList) {
		ll_content.removeAllViews();
		setMatchLayoutParams();
		for (int i = 0; matchList != null && i < matchList.size(); i++) {
			PlayView playView = new PlayView(mContext);
			playView.setDateView(matchList.get(i));
			ll_content.addView(playView);
		}
	}

	/**
	 * add ItemView 改变ll_content的高度
	 */
	private void setMatchLayoutParams() {
		LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams) ll_content.getLayoutParams();
		// 设置mapview的高度
		ll.height = LinearLayout.LayoutParams.MATCH_PARENT;
		ll_content.setLayoutParams(ll);
	}

	/**
	 * TODO 加入地图
	 */
	private String naviType;
	int[] xy = new int[2];//用于mapview的xy的记录
	private void addMapView() {
		ll_content.removeAllViews();
		MapViewFragment newFragment = new MapViewFragment();
		LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams) ll_content.getLayoutParams();
		// 设置mapview的高度
		ll.height = SystemConfig.dm.heightPixels - xy[1];
		ll_content.setLayoutParams(ll);
		newFragment.setonRoutePlanSuccessListener(this);
		getSupportFragmentManager().beginTransaction().add(R.id.ll_content, newFragment).commit();
	}

	@Override
	public void routePlanSuccess(String naviType) {
		//路线规划成功，显示路线说明
		mActionbar_right_text.setVisibility(View.VISIBLE);
		if (!TextUtils.isEmpty(naviType)) {
			this.naviType = naviType;
		}
	}

	/**
	 * 根据地图进行actionbar的重置
	 */
	private void initActionBar() {
		mActionbar_right_text.setText("线路说明");
		mActionbar_right_text.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("naviType", naviType);
				intent.setClass(ActivityCenterDetailActivity.this, LookRouteActivity.class);
				ActivityCenterDetailActivity.this.startActivity(intent);
			}
		});

	}


	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		// 获取ll_content的开始坐标xy
		ll_content.getLocationInWindow(xy);
	}
}
