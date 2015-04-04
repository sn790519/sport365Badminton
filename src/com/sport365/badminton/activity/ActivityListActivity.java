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

/**
 * 活动列表页面
 * 
 * @author Frank
 */
public class ActivityListActivity extends BaseActivity {

	// 判断活动列表页面的来源的值
	public static final String ACTIVITYFROM = "ACTIVITYFROM";
	// 正常的列表页面
	public static final int ACTIVITYLIST = 1;
	// 我身边list
	public static final int NEARACTIVITYLIST = 2;
	// 运动日历
	public static final int CADACTIVITYLIST = 3;

	private EditText et_search_text; // 搜索输入框
	private LinearLayout ll_ad_layout; // 广告

	private ListView lv_activity;
	private ActivityAdapter activityAdapter;
	private ArrayList<SportAdvertismentObj> advertismentlist = new ArrayList<SportAdvertismentObj>(); // 广告
	private AdvertisementView advertisementControlLayout;

	public ArrayList<ActiveEntityObj> alctiveList = new ArrayList<ActiveEntityObj>();// 列表

	private String date;// 运动日历

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		switch (getIntent().getIntExtra(ACTIVITYFROM, 1)) {
		case ACTIVITYLIST:// 列表
			init_GET_ALL_ACTIVE_LIST();
			break;
		case NEARACTIVITYLIST:// 我身边
			nearRequest();
			break;
		case CADACTIVITYLIST:// 价格日历
			// 运动日历来的日期
			date = getIntent().getStringExtra("date");
			cadRequest();
			break;

		}

	}

	/**
	 * 初始化view
	 */
	private void initView() {
		setActionBarTitle(getIntent().getStringExtra(BundleKeys.ACTIONBAETITLE));
		setContentView(R.layout.activity_layout);
		lv_activity = (ListView) findViewById(R.id.lv_activity);
		lv_activity.addHeaderView(initHeadView());
	}

	/**
	 * 初始化头部layout
	 * 
	 * @return
	 */
	private View initHeadView() {
		View headView = mLayoutInflater.inflate(R.layout.activity_center_headview_layout, null);
		et_search_text = (EditText) headView.findViewById(R.id.et_search_text);
		ll_ad_layout = (LinearLayout) headView.findViewById(R.id.ll_ad_layout);
		return headView;
	}

	/**
	 * 活动列表
	 */
	private void init_GET_ALL_ACTIVE_LIST() {
		GetAllActiveListReqBody reqBody = new GetAllActiveListReqBody();
		reqBody.page = "1";
		reqBody.pageSize = "10";
		reqBody.provinceId = "";
		reqBody.cityId = "";
		reqBody.countyId = "";
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

	// 价格日历来的请求
	private void cadRequest() {
		GetAllActiveListReqBody reqBody = new GetAllActiveListReqBody();
		reqBody.page = "1";
		reqBody.pageSize = "20";
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
	private void nearRequest() {
		GetnearactivelistReqBody reqBody = new GetnearactivelistReqBody();
		reqBody.page = "1";
		reqBody.pageSize = "20";
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

	// 处理成功
	private void onSuccessHandle(GetAllActiveListResBody resBody) {
		// 广告
		advertismentlist = resBody.activeAdvertismentList;
		initADdata();

		// 列表数据
		alctiveList = resBody.alctiveList;
		activityAdapter = new ActivityAdapter(mContext, alctiveList);
		lv_activity.setAdapter(activityAdapter);
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
			ActiveEntityObj mActiveEntityObj = alctiveList.get(position);
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
				reqBody.typeId = "0";
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
