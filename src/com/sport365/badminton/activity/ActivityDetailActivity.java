package com.sport365.badminton.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import com.sport365.badminton.BaseActivity;
import com.sport365.badminton.R;
import com.sport365.badminton.activity.fragment.MapViewFragment;
import com.sport365.badminton.activity.view.*;
import com.sport365.badminton.entity.obj.*;
import com.sport365.badminton.entity.reqbody.ActiveregistReqBody;
import com.sport365.badminton.entity.reqbody.GetActiveDetailByIdReqBody;
import com.sport365.badminton.entity.reqbody.GetactivememberlistReqBody;
import com.sport365.badminton.entity.resbody.ActiveRegistResBody;
import com.sport365.badminton.entity.resbody.GetActiveDetailByIdResBody;
import com.sport365.badminton.entity.resbody.GetactivememberlistResBody;
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

import java.util.ArrayList;

/**
 * 活动详情页面
 *
 * @author Frank
 */
public class ActivityDetailActivity extends BaseActivity implements MapViewFragment.OnRoutePlanSuccessListener {
	private LinearLayout ll_ad_layout;// 广告
	private LinearLayout ll_title_layout;//
	private LinearLayout ll_tab;//
	private LinearLayout ll_content;//
	private ArrayList<SportAdvertismentObj> advertismentlist = new ArrayList<SportAdvertismentObj>(); // 广告
	private AdvertisementView advertisementControlLayout;

	private ActiveEntityObj activeEntityObj;

	private LinearLayout ll_joinmember;// 参与人员
	private LinearLayout ll_contactmember;// 联系管理员
	private TextView tv_activity_rechange;// 充值
	private TextView tv_sign_now;// 报名

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBarTitle("活动详情");
		setContentView(R.layout.activity_detail_layout);
		mActionbar_right.setVisibility(View.GONE);
		initData();
		initView();
//		initADdata();
		init_GET_ACTIVE_DETAIL_BYID();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		activeEntityObj = (ActiveEntityObj) getIntent().getSerializableExtra("ActiveEntityObj");
	}

	/**
	 * 初始化View
	 */
	private void initView() {
		ll_ad_layout = (LinearLayout) findViewById(R.id.ll_ad_layout);
		ll_title_layout = (LinearLayout) findViewById(R.id.ll_title_layout);
		ll_tab = (LinearLayout) findViewById(R.id.ll_tab);
		ll_content = (LinearLayout) findViewById(R.id.ll_content);
		ll_joinmember = (LinearLayout) findViewById(R.id.ll_joinmember);
		ll_joinmember.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 参与人员
				getActiveMemberList(activeEntityObj.activeId);
			}
		});
		ll_contactmember = (LinearLayout) findViewById(R.id.ll_contactmember);
		ll_contactmember.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 联系管理员
				new DialogFactory(mContext).showDialog2Btn("", "确定拨打电话：13052892876", "确定", "取消", new DialogFactory.onBtnClickListener() {

					@Override
					public void btnLeftClickListener(View v) {
						Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "13052892876"));
						startActivity(intent);
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
			}
		});

		tv_activity_rechange = (TextView) findViewById(R.id.tv_activity_rechange);
		tv_activity_rechange.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 活动充值
				Intent intent = new Intent(ActivityDetailActivity.this,
						MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				intent.putExtra(MainActivity.PAYTYPE, MainActivity.PAYTYPE);
				startActivity(intent);
				ActivityDetailActivity.this.finish();
			}
		});
		tv_sign_now = (TextView) findViewById(R.id.tv_sign_now);
		tv_sign_now.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 报名
				activeRegist(activeEntityObj.activeId);
			}
		});
		initTitleLayout();
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
					startActivity(new Intent(ActivityDetailActivity.this, LoginActivity.class));
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

	/**
	 * 初始化头部view
	 */
	private void initTitleLayout() {
		ActivityView activityView = new ActivityView(mContext);
		activityView.setDateView(activeEntityObj);
		activityView.setBottonVisible(View.GONE);
		activityView.setBackgroundWhiteColor();
		activityView.setActivityListen(new ActivityView.ActivityListen() {

			@Override
			public void lookBookNames() {
				// do nothing
			}

			@Override
			public void doBook() {
				// do nothing
			}

			@Override
			public void goMapShow() {
				Utilities.showToast("查看地图", mContext);
				Intent intent = new Intent(ActivityDetailActivity.this, MapViewActivity.class);
				intent.putExtra(MapViewActivity.LAT, activeEntityObj.latitude);
				intent.putExtra(MapViewActivity.LON, activeEntityObj.longitude);
				startActivity(intent);
			}
		});
		ll_title_layout.addView(activityView);
	}


	/**
	 * 活动详情
	 */
	// 会所的列表
	public ArrayList<VenueEntityObj> venueList = new ArrayList<VenueEntityObj>();
	// 俱乐部的列表
	public ArrayList<ClubTabEntityObj> clubList = new ArrayList<ClubTabEntityObj>();
	// 比赛列表
	public ArrayList<MatchEntityObj> matchList = new ArrayList<MatchEntityObj>();

	private void init_GET_ACTIVE_DETAIL_BYID() {
		GetActiveDetailByIdReqBody reqBody = new GetActiveDetailByIdReqBody();
		reqBody.activeId = "387";
		sendRequestWithDialog(new ServiceRequest(mContext, new SportWebService(SportParameter.GET_ACTIVE_DETAIL_BYID), reqBody), null, new IRequestProxyCallback() {

			@Override
			public void onSuccess(HttpTaskHelper.JsonResponse jsonResponse, HttpTaskHelper.RequestInfo requestInfo) {
				ResponseContent<GetActiveDetailByIdResBody> de = jsonResponse.getResponseContent(GetActiveDetailByIdResBody.class);
				GetActiveDetailByIdResBody resBody = de.getBody();
				if (resBody != null) {
					venueList = resBody.venueList;
					clubList = resBody.clubList;
					matchList = resBody.matchList;
//					initTabLayout();
					addMapView();
//					addVenueListView(venueList);
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
		addMapView();
		/*ll_tab.addView(new SportRadioGroupView(mContext, null, null).setSportCheckListen(new SportRadioGroupView.SportCheckListen() {
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
				addClubListView(clubList);
			}

			@Override
			public void FourOnClick() {
				addMatchListView(matchList);
			}
		}));*/
	}

	/**
	 * 加入活动的列表的view
	 */
	private void addVenueListView(ArrayList<VenueEntityObj> venueList) {
		setMatchLayoutParams();
		ll_content.removeAllViews();
		for (int i = 0; venueList != null && i < venueList.size(); i++) {
			ActivityCenterView activityCenterView = new ActivityCenterView(mContext);
			activityCenterView.setDateView(venueList.get(i));
			activityCenterView.setBottonVisible(View.GONE);
			ll_content.addView(activityCenterView);
		}
	}


	/**
	 * 加入俱乐部列表的View
	 *
	 * @param clubList
	 */
	private void addClubListView(ArrayList<ClubTabEntityObj> clubList) {
		setMatchLayoutParams();
		ll_content.removeAllViews();
		for (int i = 0; clubList != null && i < clubList.size(); i++) {
			ClubView clubView = new ClubView(mContext);
			clubView.setDateView(clubList.get(i));
			clubView.setBottonVisible(View.GONE);
			ll_content.addView(clubView);
		}
	}

	/**
	 * 加入比赛列表的view
	 *
	 * @param matchList
	 */
	private void addMatchListView(ArrayList<MatchEntityObj> matchList) {
		setMatchLayoutParams();
		ll_content.removeAllViews();
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

	private void addMapView() {
		ll_content.removeAllViews();
		MapViewFragment newFragment = new MapViewFragment();
		LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams) ll_content.getLayoutParams();
		// 设置mapview的高度
		ll.height = SystemConfig.dm.heightPixels * 11 / 20;
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
				intent.setClass(ActivityDetailActivity.this, LookRouteActivity.class);
				ActivityDetailActivity.this.startActivity(intent);
			}
		});

	}
}
