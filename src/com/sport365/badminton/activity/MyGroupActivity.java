package com.sport365.badminton.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import com.sport365.badminton.BaseActivity;
import com.sport365.badminton.R;
import com.sport365.badminton.entity.obj.ActivityObj;
import com.sport365.badminton.entity.reqbody.GetActiveListByMemberIdReqBody;
import com.sport365.badminton.entity.resbody.GetActiveListByMemberIdResBody;
import com.sport365.badminton.entity.webservice.SportParameter;
import com.sport365.badminton.entity.webservice.SportWebService;
import com.sport365.badminton.http.base.HttpTaskHelper;
import com.sport365.badminton.http.base.IRequestProxyCallback;
import com.sport365.badminton.http.json.req.ServiceRequest;
import com.sport365.badminton.http.json.res.ResponseContent;
import com.sport365.badminton.utils.SystemConfig;
import com.sport365.badminton.utils.Utilities;

import java.util.ArrayList;

/**
 * 我的群活动
 */
public class MyGroupActivity extends BaseActivity {
	private Context mContext;
	private ListAdapter adapter;
	private ListView listview;
	private GetActiveListByMemberIdResBody resBody = new GetActiveListByMemberIdResBody();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_group);
		mContext = MyGroupActivity.this;
		initActionBar();
		listview = (ListView) findViewById(R.id.listview);
		adapter = new ListAdapter(mContext, resBody.alctiveList);
		listview.setAdapter(adapter);
		getMyGroupActivity("1");
	}

	private void initActionBar() {
		setActionBarTitle("我的群活动");
		mActionbar_right.setVisibility(View.GONE);
		mActionbar_right_text.setVisibility(View.GONE);
	}


	private void getMyGroupActivity(String page) {
		GetActiveListByMemberIdReqBody reqBody = new GetActiveListByMemberIdReqBody();
		reqBody.memberId = SystemConfig.memberId;
		reqBody.Page = page;
		reqBody.PageSize = "20";
		SportWebService webService = new SportWebService(SportParameter.GET_ACTIVE_LIST_BY_MEMBERID);
		sendRequestWithDialog(new ServiceRequest(mContext, webService, reqBody), null, new IRequestProxyCallback() {

			@Override
			public void onSuccess(HttpTaskHelper.JsonResponse jsonResponse, HttpTaskHelper.RequestInfo requestInfo) {
				ResponseContent<GetActiveListByMemberIdResBody> de = jsonResponse.getResponseContent(GetActiveListByMemberIdResBody.class);
				GetActiveListByMemberIdResBody resBody = de.getBody();

			}

			@Override
			public void onError(ResponseContent.Header header, HttpTaskHelper.RequestInfo requestInfo) {
				super.onError(header, requestInfo);
				Utilities.showToast(header.getRspDesc(), mContext);
			}
		});
	}


	private class ListAdapter extends BaseAdapter {
		private Context context;
		private ArrayList<ActivityObj> alctiveList = new ArrayList<ActivityObj>();

		public ListAdapter(Context context, ArrayList<ActivityObj> alctiveList) {
			this.context = context;
			this.alctiveList = alctiveList;
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
			return position;
		}

		@Override
		public View getView(int position, View currentView, ViewGroup parent) {
			ViewHolder holder = null;
			if (currentView == null) {
				holder = new ViewHolder();
//				currentView = layoutInflater.inflate(R.layout.scenery_refund_item,null);
//				holder.tv_refund_name = (TextView) findViewById(R.id.tv_refund_name);
//				holder.tv_refund_phone = (TextView) findViewById(R.id.tv_refund_phone);
//				holder.cb_check = (CheckBox) findViewById(R.id.cb_check);
				currentView.setTag(holder);
			} else {
				holder = (ViewHolder) currentView.getTag();
			}
			return currentView;
		}

		private class ViewHolder {
			private TextView tv_refund_name;
			private TextView tv_refund_phone;
			private CheckBox cb_check;
		}
	}

}
