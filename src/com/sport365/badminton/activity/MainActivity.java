package com.sport365.badminton.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.baidu.lbsapi.auth.LBSAuthManagerListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.navisdk.BNaviEngineManager;
import com.baidu.navisdk.BaiduNaviManager;
import com.sport365.badminton.BaseActivity;
import com.sport365.badminton.BaseFragment;
import com.sport365.badminton.R;
import com.sport365.badminton.activity.fragment.HomeMyFragment;
import com.sport365.badminton.activity.fragment.HomePageFragment;
import com.sport365.badminton.activity.fragment.HomePayFragment;
import com.sport365.badminton.entity.reqbody.*;
import com.sport365.badminton.entity.webservice.SportParameter;
import com.sport365.badminton.entity.webservice.SportWebService;
import com.sport365.badminton.http.base.HttpTaskHelper.JsonResponse;
import com.sport365.badminton.http.base.HttpTaskHelper.RequestInfo;
import com.sport365.badminton.http.base.IRequestProxyCallback;
import com.sport365.badminton.http.json.req.ServiceRequest;
import com.sport365.badminton.http.json.res.ResponseContent.Header;
import com.sport365.badminton.map.BDLocationHelper;
import com.sport365.badminton.utils.BundleKeys;
import com.sport365.badminton.utils.SystemConfig;
import com.sport365.badminton.utils.ULog;
import com.sport365.badminton.utils.Utilities;
import com.sport365.badminton.view.ActionBarPopupWindow;
import com.sport365.badminton.view.DialogFactory;
import com.sport365.badminton.view.PopupWindowItem;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.ArrayList;

/**
 * 首页的4个fragment
 */
public class MainActivity extends BaseActivity implements
		OnCheckedChangeListener {

	/**
	 * 再按一次退出应用
	 */
	private long exitTime = 0;

	public RadioGroup rg_menu;
	public RadioButton rb_menu_mian;
	public RadioButton rb_menu_pay;
	public RadioButton rb_menu_ball_friend;
	private RadioButton rb_menu_my;
	private RadioButton mCurrentButton;

	/**
	 * 当前显示的fragment
	 */
	private BaseFragment mCurrentFragment;
	/**
	 * 首页fragment
	 */
	private BaseFragment mHomeFragment;
	/**
	 * 充值fragment
	 */
	private HomePayFragment mPayFragment;
	/**
	 * 惠球友fragment
	 */
	private BaseFragment mBallfriendFragment;
	/**
	 * 我的fragment
	 */
	private BaseFragment mMyFragment;
	/**
	 * 百度导航是否初始化成功
	 */
	public static boolean mIsEngineInitSuccess = false;
	private BDLocationHelper bdLocationHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// 初始化导航引擎
		BaiduNaviManager.getInstance().initEngine(this, getSdcardDir(),
				mNaviEngineInitListener, new LBSAuthManagerListener() {
					@Override
					public void onAuthResult(int status, String msg) {
						if (0 == status) {
							mIsEngineInitSuccess = true;
						} else {
							mIsEngineInitSuccess = false;
						}
					}
				});
		bdLocationHelper = new BDLocationHelper(getApplicationContext(),
				new MyLocationListener());
		bdLocationHelper.startLocation();
		initActionBar();
		findViews();
		rb_menu_mian.setChecked(true);
		ULog.debug("--->onCreate");

		// init_Get_Venue_List();
		// init_GET_VENUE_DETAIL_BYI();
		// init_GET_CLUB_LIST_BYVENUE();
		// init_GET_CLUB_INFO_BYID();
		// init_GET_ALL_ACTIVE_LIST();
		// init_GET_ACTIVE_DETAIL_BYID();
	}

	private void initActionBar() {
		mActionbar_left.setImageResource(R.drawable.icon_title365_logo);
		mActionbar_left.setClickable(false);
		mActionbar_title.setVisibility(View.GONE);
		final OnClickListener tel_listener = new OnClickListener() {
			@Override
			public void onClick(View view) {
				// 弹出打电话
				new DialogFactory(mContext).showDialogWithClose("联系电话",
						SystemConfig.contactUs);
			}
		};
		final OnClickListener aboutUs_listener = new OnClickListener() {
			@Override
			public void onClick(View view) {
				new DialogFactory(mContext).showDialogWithClose(
						getString(R.string.about_us), SystemConfig.aboutUs);
			}
		};

		PopupWindowItem tel = new PopupWindowItem();
		tel.text = getString(R.string.tel);
		tel.onClickListener = tel_listener;

		PopupWindowItem aboutUs = new PopupWindowItem();
		aboutUs.text = getString(R.string.about_us);
		aboutUs.onClickListener = aboutUs_listener;
		final ArrayList<PopupWindowItem> list = new ArrayList<PopupWindowItem>();
		list.add(tel);
		list.add(aboutUs);
		mActionbar_right.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ActionBarPopupWindow actionBarPopupWindow = new ActionBarPopupWindow(
						mContext, 0, 0, list);
				actionBarPopupWindow.showAsDropDown(mActionbar_right, 0, 15);
			}
		});
	}

	private void findViews() {
		rg_menu = (RadioGroup) findViewById(R.id.rg_menu);
		rb_menu_mian = (RadioButton) findViewById(R.id.rb_menu_mian);
		rb_menu_pay = (RadioButton) findViewById(R.id.rb_menu_pay);
		rb_menu_ball_friend = (RadioButton) findViewById(R.id.rb_menu_ball_friend);
		rb_menu_my = (RadioButton) findViewById(R.id.rb_menu_my);
		rg_menu.setOnCheckedChangeListener(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		SystemConfig.clearData();
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		FragmentTransaction fragmentTransaction = getSupportFragmentManager()
				.beginTransaction();
		if (mCurrentFragment != null) {
			mCurrentFragment.onPause();
			fragmentTransaction.hide(mCurrentFragment);
		}

		switch (checkedId) {
		case R.id.rb_menu_mian:
			if (mHomeFragment != null) {
				fragmentTransaction.show(mHomeFragment);
			} else {
				mHomeFragment = new HomePageFragment();
				fragmentTransaction.add(R.id.ll_fragment_container,
						mHomeFragment);
			}
			mCurrentFragment = mHomeFragment;
			mCurrentButton = rb_menu_mian;
			break;
		case R.id.rb_menu_pay:
			if (mPayFragment != null) {
				fragmentTransaction.show(mPayFragment);
				mPayFragment.initData();
			} else {
				mPayFragment = new HomePayFragment();
				fragmentTransaction.add(R.id.ll_fragment_container,
						mPayFragment);
			}
			mCurrentFragment = mPayFragment;
			Utilities.isPayLoginLogic(mContext);
			mCurrentButton = rb_menu_pay;
			break;
		case R.id.rb_menu_ball_friend:
			if (SystemConfig.isLogin()) {
				Intent intent = new Intent(MainActivity.this,
						BallFriendActivity.class);
				intent.putExtra(BundleKeys.WEBVIEEW_LOADURL,
						"http://yundong.shenghuo365.net/yd365/cheap-index.html"
								+ SystemConfig.url_end + SystemConfig.memberId);
				startActivity(intent);
			} else {
				Utilities.showToast("请先登录", mContext);
				mCurrentButton.setChecked(true);
				return;
			}
			// if (mBallfriendFragment != null) {
			// fragmentTransaction.show(mBallfriendFragment);
			// } else {
			// mBallfriendFragment = new HomeBallFriendFragment();
			// fragmentTransaction.add(R.id.ll_fragment_container,
			// mBallfriendFragment);
			// }
			// mCurrentFragment = mBallfriendFragment;
			break;
		case R.id.rb_menu_my:
			if (mMyFragment != null) {
				fragmentTransaction.show(mMyFragment);
			} else {
				mMyFragment = new HomeMyFragment();
				fragmentTransaction
						.add(R.id.ll_fragment_container, mMyFragment);
			}
			mCurrentFragment = mMyFragment;
			mCurrentButton = rb_menu_my;
			break;
		default:
			break;
		}

		// fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commitAllowingStateLoss();
		ULog.debug("---->onCheckedChanged()");
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if (rg_menu.getCheckedRadioButtonId() == R.id.rb_menu_mian) {
				if ((System.currentTimeMillis() - exitTime) > 2000) {
					Utilities.showToast(
							mContext.getResources().getString(
									R.string.press_more_exit), this);
					exitTime = System.currentTimeMillis();
				} else {
					MainActivity.this.finish();
				}
			} else {
				rb_menu_mian.setChecked(true);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 重新刷新pay页面的数据
		if (mPayFragment != null) {
			mPayFragment.initData();
		}
		if (null != mCurrentButton) {
			mCurrentButton.setChecked(true);
		}
	}

	/**
	 * 会所列表
	 */
	private void init_Get_Venue_List() {
		GetVenueListReqBody reqBody = new GetVenueListReqBody();
		reqBody.page = "1";
		reqBody.pageSize = "10";
		reqBody.provinceId = "17";
		reqBody.cityId = "220";
		reqBody.countyId = "2143";
		sendRequestWithDialog(new ServiceRequest(mContext, new SportWebService(
				SportParameter.GET_VENUE_LIST), reqBody), null,
				new IRequestProxyCallback() {

					@Override
					public void onSuccess(JsonResponse jsonResponse,
							RequestInfo requestInfo) {
						// ResponseContent<GetSprotHomeResBody> de =
						// jsonResponse.getResponseContent(GetSprotHomeResBody.class);
						// GetSprotHomeResBody resBody = de.getBody();
					}

					@Override
					public void onError(Header header, RequestInfo requestInfo) {
						// TODO Auto-generated method stub
						super.onError(header, requestInfo);
					}
				});
	}

	/**
	 * 会所详情
	 */
	private void init_GET_VENUE_DETAIL_BYI() {
		GetVenueDetailByIdReqBody reqBody = new GetVenueDetailByIdReqBody();
		reqBody.venueId = "6";
		sendRequestWithDialog(new ServiceRequest(mContext, new SportWebService(
				SportParameter.GET_VENUE_DETAIL_BYID), reqBody), null,
				new IRequestProxyCallback() {

					@Override
					public void onSuccess(JsonResponse jsonResponse,
							RequestInfo requestInfo) {
						// ResponseContent<GetSprotHomeResBody> de =
						// jsonResponse.getResponseContent(GetSprotHomeResBody.class);
						// GetSprotHomeResBody resBody = de.getBody();
					}

					@Override
					public void onError(Header header, RequestInfo requestInfo) {
						// TODO Auto-generated method stub
						super.onError(header, requestInfo);
					}
				});
	}

	/**
	 * 活动列表
	 */
	private void init_GET_ALL_ACTIVE_LIST() {
		GetAllActiveListReqBody reqBody = new GetAllActiveListReqBody();
		reqBody.page = "1";
		reqBody.pageSize = "10";
		reqBody.provinceId = "17";
		reqBody.cityId = "220";
		reqBody.countyId = "2143";
		reqBody.activeDate = "2015-01-15";// 运动日历用
		sendRequestWithDialog(new ServiceRequest(mContext, new SportWebService(
				SportParameter.GET_VENUE_DETAIL_BYID), reqBody), null,
				new IRequestProxyCallback() {

					@Override
					public void onSuccess(JsonResponse jsonResponse,
							RequestInfo requestInfo) {
						// ResponseContent<GetSprotHomeResBody> de =
						// jsonResponse.getResponseContent(GetSprotHomeResBody.class);
						// GetSprotHomeResBody resBody = de.getBody();
					}

					@Override
					public void onError(Header header, RequestInfo requestInfo) {
						// TODO Auto-generated method stub
						super.onError(header, requestInfo);
					}
				});
	}

	/**
	 * 活动详情
	 */
	private void init_GET_ACTIVE_DETAIL_BYID() {
		GetActiveDetailByIdReqBody reqBody = new GetActiveDetailByIdReqBody();
		reqBody.activeId = "387";
		sendRequestWithDialog(new ServiceRequest(mContext, new SportWebService(
				SportParameter.GET_ACTIVE_DETAIL_BYID), reqBody), null,
				new IRequestProxyCallback() {

					@Override
					public void onSuccess(JsonResponse jsonResponse,
							RequestInfo requestInfo) {
						// ResponseContent<GetSprotHomeResBody> de =
						// jsonResponse.getResponseContent(GetSprotHomeResBody.class);
						// GetSprotHomeResBody resBody = de.getBody();
					}

					@Override
					public void onError(Header header, RequestInfo requestInfo) {
						// TODO Auto-generated method stub
						super.onError(header, requestInfo);
					}
				});
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
		sendRequestWithDialog(new ServiceRequest(mContext, new SportWebService(
				SportParameter.GET_CLUB_LIST_BYVENUE), reqBody), null,
				new IRequestProxyCallback() {

					@Override
					public void onSuccess(JsonResponse jsonResponse,
							RequestInfo requestInfo) {
						// ResponseContent<GetSprotHomeResBody> de =
						// jsonResponse.getResponseContent(GetSprotHomeResBody.class);
						// GetSprotHomeResBody resBody = de.getBody();
					}

					@Override
					public void onError(Header header, RequestInfo requestInfo) {
						// TODO Auto-generated method stub
						super.onError(header, requestInfo);
					}
				});
	}

	/**
	 * 俱乐部详情
	 */
	private void init_GET_CLUB_INFO_BYID() {
		GetClubInfoByidReqBody reqBody = new GetClubInfoByidReqBody();
		reqBody.clubId = "1";
		sendRequestWithDialog(new ServiceRequest(mContext, new SportWebService(
				SportParameter.GET_CLUB_INFO_BYID), reqBody), null,
				new IRequestProxyCallback() {

					@Override
					public void onSuccess(JsonResponse jsonResponse,
							RequestInfo requestInfo) {
						// ResponseContent<GetSprotHomeResBody> de =
						// jsonResponse.getResponseContent(GetSprotHomeResBody.class);
						// GetSprotHomeResBody resBody = de.getBody();
					}

					@Override
					public void onError(Header header, RequestInfo requestInfo) {
						// TODO Auto-generated method stub
						super.onError(header, requestInfo);
					}
				});
	}

	private String getSdcardDir() {
		if (Environment.getExternalStorageState().equalsIgnoreCase(
				Environment.MEDIA_MOUNTED)) {
			return Environment.getExternalStorageDirectory().toString();
		}
		return null;
	}

	/**
	 * 定位监听函数
	 */
	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null) {
				Utilities.showToast("定位失败", mContext);
				return;
			}
			bdLocationHelper.setCurrentLocation(location);
		}
	}

	private BNaviEngineManager.NaviEngineInitListener mNaviEngineInitListener = new BNaviEngineManager.NaviEngineInitListener() {
		public void engineInitSuccess() {
			mIsEngineInitSuccess = true;
		}

		public void engineInitStart() {
		}

		public void engineInitFail() {
		}
	};

}
