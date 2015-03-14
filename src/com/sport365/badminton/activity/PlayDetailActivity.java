package com.sport365.badminton.activity;

import android.os.Bundle;

import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sport365.badminton.BaseActivity;
import com.sport365.badminton.R;
import com.sport365.badminton.activity.view.ClubView;
import com.sport365.badminton.activity.view.PlayView;
import com.sport365.badminton.entity.obj.ClubTabEntityObj;
import com.sport365.badminton.entity.obj.MatchEntityObj;
import com.sport365.badminton.entity.obj.SportAdvertismentObj;
import com.sport365.badminton.entity.reqbody.GetClubInfoByidReqBody;
import com.sport365.badminton.entity.reqbody.GetMatchDetailByIDReqBody;
import com.sport365.badminton.entity.resbody.GetMatchDetailByIDResBody;
import com.sport365.badminton.entity.webservice.SportParameter;
import com.sport365.badminton.entity.webservice.SportWebService;
import com.sport365.badminton.http.base.HttpTaskHelper;
import com.sport365.badminton.http.base.IRequestProxyCallback;
import com.sport365.badminton.http.base.ImageLoader;
import com.sport365.badminton.http.json.req.ServiceRequest;
import com.sport365.badminton.http.json.res.ResponseContent;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBarTitle("比赛详情");
		setContentView(R.layout.play_detail_layout);
		initData();
		initView();
//		initADdata();
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
		ll_title_layout.addView(playView);
	}

	/**
	 * 俱乐部详情
	 */
	private void INIT_GET_MATCH_INFO_BYID() {
		GetMatchDetailByIDReqBody reqBody = new GetMatchDetailByIDReqBody();
		reqBody.matchId = "2";
		sendRequestWithDialog(new ServiceRequest(mContext, new SportWebService(SportParameter.GET_MATCH_DETAIL_BYID), reqBody), null, new IRequestProxyCallback() {

			@Override
			public void onSuccess(HttpTaskHelper.JsonResponse jsonResponse, HttpTaskHelper.RequestInfo requestInfo) {
				ResponseContent<GetMatchDetailByIDResBody> de = jsonResponse.getResponseContent(GetMatchDetailByIDResBody.class);
				GetMatchDetailByIDResBody resBody = de.getBody();
				if (resBody != null && !TextUtils.isEmpty(resBody.matchRule)) {
					tv_matchrule.setText(Html.fromHtml(resBody.matchRule));
					ll_bottom.setVisibility(View.VISIBLE);
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
