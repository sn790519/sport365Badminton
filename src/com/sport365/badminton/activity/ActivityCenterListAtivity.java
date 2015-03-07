package com.sport365.badminton.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.sport365.badminton.BaseActivity;
import com.sport365.badminton.R;
import com.sport365.badminton.adapter.ActivityCenterAdapter;
import com.sport365.badminton.entity.obj.SportAdvertismentObj;
import com.sport365.badminton.entity.obj.VenueEntityObj;
import com.sport365.badminton.entity.reqbody.GetVenueListReqBody;
import com.sport365.badminton.entity.resbody.GetVenueListResBody;
import com.sport365.badminton.entity.webservice.SportParameter;
import com.sport365.badminton.entity.webservice.SportWebService;
import com.sport365.badminton.http.base.HttpTaskHelper;
import com.sport365.badminton.http.base.IRequestProxyCallback;
import com.sport365.badminton.http.base.ImageLoader;
import com.sport365.badminton.http.json.req.ServiceRequest;
import com.sport365.badminton.http.json.res.ResponseContent;
import com.sport365.badminton.utils.BundleKeys;
import com.sport365.badminton.view.advertisement.AdvertisementView;

/**
 * 运动会所列表页面
 *
 * @author Frank
 */
public class ActivityCenterListAtivity extends BaseActivity {

	private EditText et_search_text;                                                // 搜索输入框
	private LinearLayout ll_ad_layout;                                                // 广告

	private ListView lv_activity_center;
	private ActivityCenterAdapter activityCenterAdapter;
	private ArrayList<SportAdvertismentObj> advertismentlist = new ArrayList<SportAdvertismentObj>();    // 广告
	public ArrayList<VenueEntityObj> venueEntity = new ArrayList<VenueEntityObj>();// 列表数据
	private AdvertisementView advertisementControlLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_center_layout);
		initView();
		init_Get_Venue_List();
	}

	private void initView() {
		setActionBarTitle(getIntent().getStringExtra(BundleKeys.ACTIONBAETITLE));
		lv_activity_center = (ListView) findViewById(R.id.lv_activity_center);
		lv_activity_center.addHeaderView(initHeadView());
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
	 * 会所列表
	 */
	private void init_Get_Venue_List() {
		final GetVenueListReqBody reqBody = new GetVenueListReqBody();
		reqBody.page = "1";
		reqBody.pageSize = "10";
		reqBody.provinceId = "17";
		reqBody.cityId = "220";
		reqBody.countyId = "2143";
		sendRequestWithDialog(new ServiceRequest(mContext, new SportWebService(SportParameter.GET_VENUE_LIST), reqBody), null, new IRequestProxyCallback() {

			@Override
			public void onSuccess(HttpTaskHelper.JsonResponse jsonResponse, HttpTaskHelper.RequestInfo requestInfo) {
				ResponseContent<GetVenueListResBody> de = jsonResponse.getResponseContent(GetVenueListResBody.class);
				GetVenueListResBody resBody = de.getBody();
				if (resBody != null) {
					advertismentlist = resBody.venueAdvertismentList;
					venueEntity = resBody.venueEntity;
				}
				initADdata();
				activityCenterAdapter = new ActivityCenterAdapter(mContext, venueEntity);
				lv_activity_center.setAdapter(activityCenterAdapter);
				lv_activity_center.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						Intent intent = new Intent(ActivityCenterListAtivity.this, ActivityCenterDetailActivity.class);
						Bundle bundle = new Bundle();
						bundle.putSerializable("VenueEntityObj", venueEntity.get(position-lv_activity_center.getHeaderViewsCount()));
						intent.putExtras(bundle);
						startActivity(intent);
					}
				});
			}

			@Override
			public void onError(ResponseContent.Header header, HttpTaskHelper.RequestInfo requestInfo) {
				// TODO Auto-generated method stub
				super.onError(header, requestInfo);
			}
		});
	}

	/**
	 * 初始化广告信息
	 */
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
}
