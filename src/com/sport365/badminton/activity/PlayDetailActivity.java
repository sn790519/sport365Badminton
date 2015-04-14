package com.sport365.badminton.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sport365.badminton.BaseActivity;
import com.sport365.badminton.R;
import com.sport365.badminton.activity.view.PlayView;
import com.sport365.badminton.entity.obj.MatchEntityObj;
import com.sport365.badminton.entity.obj.SportAdvertismentObj;
import com.sport365.badminton.entity.reqbody.ActiveregistReqBody;
import com.sport365.badminton.entity.reqbody.GetMatchDetailByIDReqBody;
import com.sport365.badminton.entity.reqbody.GetactivememberlistReqBody;
import com.sport365.badminton.entity.resbody.ActiveRegistResBody;
import com.sport365.badminton.entity.resbody.GetMatchDetailByIDResBody;
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
import com.sport365.badminton.view.SharePopWindow;
import com.sport365.badminton.view.advertisement.AdvertisementView;

import java.util.ArrayList;

/**
 * 比赛详情页面
 * 
 * @author Frank
 */
public class PlayDetailActivity extends BaseActivity {

	private MatchEntityObj matchEntityObj;

	private LinearLayout ll_ad_layout;
	private LinearLayout ll_title_layout;
	private ArrayList<SportAdvertismentObj> advertismentlist = new ArrayList<SportAdvertismentObj>(); // 广告
	private AdvertisementView advertisementControlLayout;

	private TextView tv_matchrule;
	private LinearLayout ll_bottom;

	// 分享内容
	private String shareUrl;
	private String shareTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBarTitle("比赛详情");
		setContentView(R.layout.play_detail_layout);
		mActionbar_right.setImageResource(R.drawable.share_tad_icon);
		mActionbar_right.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// 分享
				SharePopWindow actionBarPopupWindow = new SharePopWindow(mContext);
				actionBarPopupWindow.setAnimationStyle(R.style.AnimDialogBottom);
				actionBarPopupWindow.setUrlANDSharetitle(shareUrl, shareTitle);
				actionBarPopupWindow.showAtLocation(findViewById(R.id.flag), Gravity.CENTER | Gravity.BOTTOM, 0, 0);
			}
		});
		initData();
		initView();
		// initADdata();
		initTitleLayout();
		INIT_GET_MATCH_INFO_BYID();
	}

	private void initData() {
		matchEntityObj = (MatchEntityObj) getIntent().getSerializableExtra("MatchEntityObj");
	}

	private void initView() {
		ll_ad_layout = (LinearLayout) findViewById(R.id.ll_ad_layout);
		ll_title_layout = (LinearLayout) findViewById(R.id.ll_title_layout);
		ll_bottom = (LinearLayout) findViewById(R.id.ll_bottom);
		ll_bottom.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				activeRegist(matchEntityObj.matchId);
			}
		});
		tv_matchrule = (TextView) findViewById(R.id.tv_matchrule);
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
		PlayView playView = new PlayView(mContext);
		playView.setDateView(matchEntityObj);
		playView.setBottonVisible(View.GONE);
		playView.setArrowVisible(View.GONE);
		playView.setBackgroundWhiteColor();
		playView.setPlayListen(new PlayView.PlayListen() {

			@Override
			public void goMapShow() {
				Intent intent = new Intent(PlayDetailActivity.this, MapViewActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString(MapViewActivity.LAT, matchEntityObj.latitude);
				bundle.putString(MapViewActivity.LON, matchEntityObj.longitude);
				bundle.putString(MapViewActivity.NAME, matchEntityObj.matchName);
				intent.putExtras(bundle);
				startActivity(intent);
			}

			@Override
			public void doBookName() {
				// do nothing
			}

			@Override
			public void checkBookName() {
				// 
				getActiveMemberList(matchEntityObj.matchId);
			}
		});
		ll_title_layout.addView(playView);
	}

	/**
	 * 俱乐部详情
	 */
	private void INIT_GET_MATCH_INFO_BYID() {
		final GetMatchDetailByIDReqBody reqBody = new GetMatchDetailByIDReqBody();
		reqBody.matchId = matchEntityObj.matchId;
		sendRequestWithDialog(new ServiceRequest(mContext, new SportWebService(SportParameter.GET_MATCH_DETAIL_BYID), reqBody), null, new IRequestProxyCallback() {

			@Override
			public void onSuccess(HttpTaskHelper.JsonResponse jsonResponse, HttpTaskHelper.RequestInfo requestInfo) {
				ResponseContent<GetMatchDetailByIDResBody> de = jsonResponse.getResponseContent(GetMatchDetailByIDResBody.class);
				GetMatchDetailByIDResBody resBody = de.getBody();
				if (resBody != null && !TextUtils.isEmpty(resBody.matchRule)) {
					tv_matchrule.setText(Html.fromHtml(resBody.matchRule));
					ll_bottom.setVisibility(View.VISIBLE);
					shareUrl = resBody.shareUrl;
					shareTitle = resBody.shareTitle;
				}
			}

			@Override
			public void onError(ResponseContent.Header header, HttpTaskHelper.RequestInfo requestInfo) {
				// TODO Auto-generated method stub
				super.onError(header, requestInfo);
			}
		});
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
					}
				});
			}

			@Override
			public void btnCloseClickListener(View v) {

			}
		}, true);

	}

	// 活动中拉取报名列表
	private void getActiveMemberList(String activeId) {
		GetactivememberlistReqBody reqBody = new GetactivememberlistReqBody();
		reqBody.activeId = activeId;
		reqBody.typeId = "1";
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

}
