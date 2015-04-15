package com.sport365.badminton.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
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
import com.sport365.badminton.entity.reqbody.GetVenueDetailByIdReqBody;
import com.sport365.badminton.entity.resbody.ActiveRegistResBody;
import com.sport365.badminton.entity.resbody.GetVenueDetailByIdResBody;
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
		mActionbar_right.setVisibility(View.GONE);
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
		activityCenterView.setTopRecommadImageViewVisible(View.GONE);
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
				Intent intent = new Intent(ActivityCenterDetailActivity.this, MapViewActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString(MapViewActivity.LAT, venueEntityObj.latitude);
				bundle.putString(MapViewActivity.LON, venueEntityObj.longitude);
				bundle.putString(MapViewActivity.NAME, venueEntityObj.name);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
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
		reqBody.venueId = venueEntityObj.venueId;
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
					// addMapView();
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
				addActivityListView(activeList);
			}

			@Override
			public void SecondOnClick() {
				addMapView();
			}

			@Override
			public void ThirdOnClick() {
				addClubListView(clubList);
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
	private void addActivityListView(final ArrayList<ActiveEntityObj> activeList) {
		setMatchLayoutParams();
		ll_content.removeAllViews();
		if (activeList == null || activeList.size() == 0) {
			ImageView iv = new ImageView(mContext);
			iv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.no_result_pic));
			ll_content.addView(iv);
			return;
		}
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
					Intent intent = new Intent(ActivityCenterDetailActivity.this, MapViewActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString(MapViewActivity.LAT, activeList.get(flag).latitude);
					bundle.putString(MapViewActivity.LON, activeList.get(flag).longitude);
					bundle.putString(MapViewActivity.NAME, activeList.get(flag).activeTitle);
					intent.putExtras(bundle);
					startActivity(intent);
				}
			});
			activityView.setMarginTop();
			activityView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 去活动详情
					Intent intent = new Intent(ActivityCenterDetailActivity.this, ActivityDetailActivity.class);
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
	 * 加入俱乐部列表的View
	 * 
	 * @param clubList
	 */
	private void addClubListView(final ArrayList<ClubTabEntityObj> clubList) {
		setMatchLayoutParams();
		ll_content.removeAllViews();
		if (clubList == null || clubList.size() == 0) {
			ImageView iv = new ImageView(mContext);
			iv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.no_result_pic));
			ll_content.addView(iv);
			return;
		}
		for (int i = 0; clubList != null && i < clubList.size(); i++) {
			final int flag = i;
			ClubView clubView = new ClubView(mContext);
			clubView.setDateView(clubList.get(i));
			clubView.setBottonVisible(View.GONE);
			clubView.setMarginTop();
			clubView.setClubListen(new ClubView.ClubListen() {

				@Override
				public void lookMathces() {
					// do nothing
				}

				@Override
				public void lookActivitys() {
					// do nothing
				}

				@Override
				public void doRechange() {
					// do nothing
				}

				@Override
				public void goMapShow() {
					Intent intent = new Intent(ActivityCenterDetailActivity.this, MapViewActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString(MapViewActivity.LAT, clubList.get(flag).latitude);
					bundle.putString(MapViewActivity.LON, clubList.get(flag).longitude);
					bundle.putString(MapViewActivity.NAME, clubList.get(flag).cityName);
					intent.putExtras(bundle);
					startActivity(intent);
				}
			});
			clubView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 去俱乐部详情
					Intent intent = new Intent(ActivityCenterDetailActivity.this, ClubDetailActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("ClubTabEntityObj", clubList.get(flag));
					intent.putExtras(bundle);
					startActivity(intent);

				}
			});

			ll_content.addView(clubView);
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
		if (matchList == null || matchList.size() == 0) {
			ImageView iv = new ImageView(mContext);
			iv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.no_result_pic));
			ll_content.addView(iv);
			return;
		}
		for (int i = 0; matchList != null && i < matchList.size(); i++) {
			final int flag = i;
			PlayView playView = new PlayView(mContext);
			playView.setDateView(matchList.get(i));
			playView.setMarginTop();
			playView.setPlayListen(new PlayListen() {

				@Override
				public void goMapShow() {
					Intent intent = new Intent(ActivityCenterDetailActivity.this, MapViewActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString(MapViewActivity.LAT, matchList.get(flag).latitude);
					bundle.putString(MapViewActivity.LON, matchList.get(flag).longitude);
					bundle.putString(MapViewActivity.NAME, matchList.get(flag).matchName);
					intent.putExtras(bundle);
					startActivity(intent);
				}

				@Override
				public void doBookName() {
					activeRegist(matchList.get(flag).matchId);
				}

				@Override
				public void checkBookName() {
					// TODO Auto-generated method stub
					
				}
			});
			playView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 去比赛详情
					Intent intent = new Intent(ActivityCenterDetailActivity.this, PlayDetailActivity.class);
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

	// 比赛中报名
	private void activeRegist(final String activeId) {
		if (!SystemConfig.isLogin()) {
			new DialogFactory(mContext).showDialog("提示", "你还没有登录，请登录。", "确定", new DialogFactory.onBtnClickListener() {

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

		new DialogFactory(mContext).showDialog2Btn("提示", "你将进行活动报名，请确认？", "取消", "确定", new DialogFactory.onBtnClickListener() {

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
