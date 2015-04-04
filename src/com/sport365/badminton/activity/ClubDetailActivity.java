package com.sport365.badminton.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.sport365.badminton.BaseActivity;
import com.sport365.badminton.R;
import com.sport365.badminton.activity.fragment.MapViewFragment;
import com.sport365.badminton.activity.view.ActivityCenterView;
import com.sport365.badminton.activity.view.ActivityView;
import com.sport365.badminton.activity.view.ActivityView.ActivityListen;
import com.sport365.badminton.activity.view.ClubView;
import com.sport365.badminton.activity.view.PlayView;
import com.sport365.badminton.activity.view.PlayView.PlayListen;
import com.sport365.badminton.activity.view.SportRadioGroupView;
import com.sport365.badminton.entity.obj.ActiveEntityObj;
import com.sport365.badminton.entity.obj.ClubTabEntityObj;
import com.sport365.badminton.entity.obj.MatchEntityObj;
import com.sport365.badminton.entity.obj.SportAdvertismentObj;
import com.sport365.badminton.entity.obj.VenueEntityObj;
import com.sport365.badminton.entity.reqbody.ActiveregistReqBody;
import com.sport365.badminton.entity.reqbody.GetClubInfoByidReqBody;
import com.sport365.badminton.entity.resbody.ActiveRegistResBody;
import com.sport365.badminton.entity.resbody.GetClubInfoByidResBody;
import com.sport365.badminton.entity.webservice.SportParameter;
import com.sport365.badminton.entity.webservice.SportWebService;
import com.sport365.badminton.http.base.HttpTaskHelper;
import com.sport365.badminton.http.base.IRequestProxyCallback;
import com.sport365.badminton.http.base.ImageLoader;
import com.sport365.badminton.http.json.req.ServiceRequest;
import com.sport365.badminton.http.json.res.ResponseContent;
import com.sport365.badminton.utils.SystemConfig;
import com.sport365.badminton.utils.Utilities;
import com.sport365.badminton.view.DialogFactory;
import com.sport365.badminton.view.advertisement.AdvertisementView;

/**
 * 俱乐部详情页面
 * 
 * @author Frank
 */
public class ClubDetailActivity extends BaseActivity implements MapViewFragment.OnRoutePlanSuccessListener {
	private LinearLayout ll_ad_layout;// 广告
	private LinearLayout ll_title_layout;
	private LinearLayout ll_tab;
	private LinearLayout ll_content;
	private ArrayList<SportAdvertismentObj> advertismentlist = new ArrayList<SportAdvertismentObj>(); // 广告
	private AdvertisementView advertisementControlLayout;

	private ClubTabEntityObj clubTabEntityObj;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.club_detail_layout);
		setActionBarTitle("俱乐部详情");
		mActionbar_right.setVisibility(View.GONE);
		initData();
		initView();
		init_GET_CLUB_INFO_BYID();
	}

	private void initData() {
		clubTabEntityObj = (ClubTabEntityObj) getIntent().getSerializableExtra("ClubTabEntityObj");
	}

	private void initView() {
		ll_ad_layout = (LinearLayout) findViewById(R.id.ll_ad_layout);
		ll_title_layout = (LinearLayout) findViewById(R.id.ll_title_layout);
		ll_tab = (LinearLayout) findViewById(R.id.ll_tab);
		ll_content = (LinearLayout) findViewById(R.id.ll_content);
	}

	/**
	 * 初始化头部信息
	 */
	private void initTitleLayout() {
		ClubView clubView = new ClubView(mContext);
		clubView.setDateView(clubTabEntityObj);
		clubView.setBottonVisible(View.GONE);
		clubView.setTopRecommadImageViewVisible(View.GONE);
		clubView.setRechangeVisible(View.VISIBLE);
		clubView.setClubListen(new ClubView.ClubListen() {
			@Override
			public void doRechange() {
				Utilities.showToast("充值页面", mContext);
				Intent intent = new Intent(ClubDetailActivity.this, MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				intent.putExtra(MainActivity.PAYTYPE, MainActivity.PAYTYPE);
				startActivity(intent);
				ClubDetailActivity.this.finish();
			}

			@Override
			public void lookActivitys() {
				// do nothing
			}

			@Override
			public void lookMathces() {
				// do nothing
			}

			@Override
			public void goMapShow() {
				Utilities.showToast("查看地图", mContext);
				Intent intent = new Intent(ClubDetailActivity.this, MapViewActivity.class);
				intent.putExtra(MapViewActivity.LAT, clubTabEntityObj.latitude);
				intent.putExtra(MapViewActivity.LON, clubTabEntityObj.longitude);
				startActivity(intent);
			}
		});
		ll_title_layout.addView(clubView);
	}

	/**
	 * 俱乐部详情
	 */
	// 活动的列表
	public ArrayList<VenueEntityObj> venueList = new ArrayList<VenueEntityObj>();
	// 活动的列表
	public ArrayList<ActiveEntityObj> activeList = new ArrayList<ActiveEntityObj>();
	// 比赛列表
	public ArrayList<MatchEntityObj> matchList = new ArrayList<MatchEntityObj>();

	private void init_GET_CLUB_INFO_BYID() {
		GetClubInfoByidReqBody reqBody = new GetClubInfoByidReqBody();
		reqBody.clubId = "1";
		sendRequestWithDialog(new ServiceRequest(mContext, new SportWebService(SportParameter.GET_CLUB_INFO_BYID), reqBody), null, new IRequestProxyCallback() {

			@Override
			public void onSuccess(HttpTaskHelper.JsonResponse jsonResponse, HttpTaskHelper.RequestInfo requestInfo) {
				ResponseContent<GetClubInfoByidResBody> de = jsonResponse.getResponseContent(GetClubInfoByidResBody.class);
				GetClubInfoByidResBody resBody = de.getBody();
				if (resBody != null) {
					venueList = resBody.venueList;
					activeList = resBody.activeList;
					matchList = resBody.matchList;
					advertismentlist = resBody.clubAdvertismentList;
					initADdata();
					initTitleLayout();
					initTabLayout();
					addVenueListView(venueList);
				}
			}

			@Override
			public void onError(ResponseContent.Header header, HttpTaskHelper.RequestInfo requestInfo) {
				// TODO Auto-generated method stub
				super.onError(header, requestInfo);
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

	/**
	 * 初始化tab信息
	 */
	private void initTabLayout() {
		ll_tab.addView(new SportRadioGroupView(mContext, null, null).setSportCheckListen(new SportRadioGroupView.SportCheckListen() {
			@Override
			public void FirstOnClick() {
				addVenueListView(venueList);
			}

			@Override
			public void SecondOnClick() {
				addMapView();
			}

			@Override
			public void ThirdOnClick() {
				addActivityListView(activeList);
			}

			@Override
			public void FourOnClick() {
				addMatchListView(matchList);
			}
		}));
	}

	/**
	 * 加入活动的列表的view
	 */
	private void addVenueListView(final ArrayList<VenueEntityObj> venueList) {
		setMatchLayoutParams();
		ll_content.removeAllViews();
		for (int i = 0; venueList != null && i < venueList.size(); i++) {
			final int flag = i;
			ActivityCenterView activityCenterView = new ActivityCenterView(mContext);
			activityCenterView.setDateView(venueList.get(i));
			activityCenterView.setBottonVisible(View.GONE);
			activityCenterView.setMarginTop();
			activityCenterView.setActivityCenterListen(new ActivityCenterView.ActivityCenterListen() {
				@Override
				public void lookTeam() {
					// do nothing
				}

				@Override
				public void lookActivity() {
					// do nothing
				}

				@Override
				public void lookMathce() {
					// do nothing
				}

				@Override
				public void goMapShow() {
					Utilities.showToast("查看地图", mContext);
					Intent intent = new Intent(ClubDetailActivity.this, MapViewActivity.class);
					intent.putExtra(MapViewActivity.LAT, venueList.get(flag).latitude);
					intent.putExtra(MapViewActivity.LON, venueList.get(flag).longitude);
					startActivity(intent);
				}
			});
			activityCenterView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 运动会所页面
					Intent intent = new Intent(ClubDetailActivity.this, ActivityCenterDetailActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("VenueEntityObj", venueList.get(flag));
					intent.putExtras(bundle);
					startActivity(intent);
				}
			});
			ll_content.addView(activityCenterView);
		}
	}

	/**
	 * 加入活动的列表的view
	 */
	private void addActivityListView(final ArrayList<ActiveEntityObj> activeList) {
		setMatchLayoutParams();
		ll_content.removeAllViews();
		for (int i = 0; activeList != null && i < activeList.size(); i++) {
			final int flag = i;
			ActivityView activityView = new ActivityView(mContext);
			activityView.setDateView(activeList.get(i));
			activityView.setBottonVisible(View.GONE);
			activityView.setActivityListen(new ActivityListen() {

				@Override
				public void lookBookNames() {
					// do notying
				}

				@Override
				public void doBook() {
					// do nothing
				}

				@Override
				public void goMapShow() {
					Utilities.showToast("查看地图", mContext);
					Intent intent = new Intent(ClubDetailActivity.this, MapViewActivity.class);
					intent.putExtra(MapViewActivity.LAT, activeList.get(flag).latitude);
					intent.putExtra(MapViewActivity.LON, activeList.get(flag).longitude);
					startActivity(intent);
				}
			});
			activityView.setMarginTop();
			activityView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 去活动详情
					Intent intent = new Intent(ClubDetailActivity.this, ActivityDetailActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("ActiveEntityObj", activeList.get(flag));
					intent.putExtras(bundle);
					startActivity(intent);
				}
			});
			ll_content.addView(activityView);
		}
	}

	/**
	 * 加入比赛列表的view
	 * 
	 * @param matchList
	 */
	private void addMatchListView(final ArrayList<MatchEntityObj> matchList) {
		setMatchLayoutParams();
		ll_content.removeAllViews();
		for (int i = 0; matchList != null && i < matchList.size(); i++) {
			final int flag = i;
			PlayView playView = new PlayView(mContext);
			playView.setDateView(matchList.get(i));
			playView.setMarginTop();
			playView.setPlayListen(new PlayListen() {

				@Override
				public void goMapShow() {
					Utilities.showToast("地图页面", mContext);
					Intent intent = new Intent(ClubDetailActivity.this, MapViewActivity.class);
					intent.putExtra(MapViewActivity.LAT, matchList.get(flag).latitude);
					intent.putExtra(MapViewActivity.LON, matchList.get(flag).longitude);
					startActivity(intent);
				}

				@Override
				public void doBookName() {
					activeRegist(matchList.get(flag).matchId);
				}
			});
			playView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 去比赛详情
					Intent intent = new Intent(ClubDetailActivity.this, PlayDetailActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("MatchEntityObj", matchList.get(flag));
					intent.putExtras(bundle);
					startActivity(intent);
				}
			});
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
	int[] xy = new int[2];// 用于mapview的xy的记录

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
		// 路线规划成功，显示路线说明
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
				intent.setClass(ClubDetailActivity.this, LookRouteActivity.class);
				ClubDetailActivity.this.startActivity(intent);
			}
		});

	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		// 获取ll_content的开始坐标xy
		ll_content.getLocationInWindow(xy);
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
						if(resbody != null){
							Utilities.showDialogWithMemberName(mContext, resbody.returnMsg);
						}else{
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
