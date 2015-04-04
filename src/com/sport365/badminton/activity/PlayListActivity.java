package com.sport365.badminton.activity;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

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

/**
 * 比赛列表页面
 * 
 * @author Frank
 */
public class PlayListActivity extends BaseActivity {

	private EditText et_search_text; // 搜索输入框
	private LinearLayout ll_ad_layout; // 广告

	private ListView lv_play;
	private PlayAdapter clubAdapter;
	private ArrayList<SportAdvertismentObj> advertismentlist = new ArrayList<SportAdvertismentObj>(); // 广告
	private AdvertisementView advertisementControlLayout;

	// 列表
	private ArrayList<MatchEntityObj> matchTabEntity = new ArrayList<MatchEntityObj>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBarTitle(getIntent().getStringExtra(BundleKeys.ACTIONBAETITLE));
		setContentView(R.layout.play_layout);
		lv_play = (ListView) findViewById(R.id.lv_play);
		lv_play.addHeaderView(initHeadView());
		init_Get_Match_List();

	}

	private View initHeadView() {
		View headView = mLayoutInflater.inflate(R.layout.activity_center_headview_layout, null);
		et_search_text = (EditText) headView.findViewById(R.id.et_search_text);
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

	/**
	 * 比赛列表
	 */
	private void init_Get_Match_List() {
		final GetMatchListReqBody reqBody = new GetMatchListReqBody();
		reqBody.page = "1";
		reqBody.pageSize = "10";
		// reqBody.provinceId = "17";
		// reqBody.cityId = "220";
		// reqBody.countyId = "2143";
		sendRequestWithDialog(new ServiceRequest(mContext, new SportWebService(SportParameter.GET_MATCH_LIST), reqBody), null, new IRequestProxyCallback() {

			@Override
			public void onSuccess(HttpTaskHelper.JsonResponse jsonResponse, HttpTaskHelper.RequestInfo requestInfo) {
				ResponseContent<GetMatchListResBody> de = jsonResponse.getResponseContent(GetMatchListResBody.class);
				GetMatchListResBody resBody = de.getBody();
				if (resBody != null) {
					// 广告
					advertismentlist = resBody.matchAdvertismentList;
					initADdata();

					// 列表
					matchTabEntity = resBody.matchTabEntity;
					clubAdapter = new PlayAdapter(mContext, matchTabEntity);
					lv_play.setAdapter(clubAdapter);
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
			public void onError(ResponseContent.Header header, HttpTaskHelper.RequestInfo requestInfo) {
				// TODO Auto-generated method stub
				super.onError(header, requestInfo);
			}
		});
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
			MatchEntityObj mMatchEntityObj = matchTabEntity.get(position);
			if (convertView == null) {
				convertView = new PlayView(mContext);
			}
			((PlayView) convertView).setDateView(mMatchEntityObj);
			((PlayView) convertView).setPlayListen(new PlayListen() {

				@Override
				public void goMapShow() {

				}

				@Override
				public void doBookName() {
					activeRegist(matchTabEntity.get(position).activeId);
				}
			});
			return convertView;
		}
	}

	// 比赛中报名
	private void activeRegist(final String activeId) {
		if (!SystemConfig.isLogin()) {
			Utilities.showDialogWithMemberName(mContext, "你还没有登录，请登录。");
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
