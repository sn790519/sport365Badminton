package com.sport365.badminton.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.baidu.mapapi.search.route.*;
import com.sport365.badminton.BaseActivity;
import com.sport365.badminton.R;
import com.sport365.badminton.utils.Utilities;

import java.util.List;

/**
 * 路线查看
 */
public class LookRouteActivity extends BaseActivity {

	private Context context;
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_look_route);
		context = this;
		Bundle bundle = getIntent().getExtras();
		String navType = bundle.getString("naviType");
		setActionBarTitle("查看" + navType + "路线");
		mActionbar_right.setVisibility(View.GONE);
		listView = (ListView) findViewById(R.id.look_route_listview);

		if (!TextUtils.isEmpty(navType)) {
			if ("驾车".equals(navType)) {
				DrivingRouteResult drivingRouteResult = Utilities.drivingRouteResult;
				List<DrivingRouteLine.DrivingStep> list = drivingRouteResult.getRouteLines().get(0).getAllStep();
				DrivingRouteAdapter drivingRouteAdapter = new DrivingRouteAdapter(context, list);
				listView.setCacheColorHint(Color.TRANSPARENT);
				listView.setAdapter(drivingRouteAdapter);
			} else if ("公交".equals(navType)) {
				TransitRouteResult transitRouteResult = Utilities.transitRouteResult;
				List<TransitRouteLine.TransitStep> list = transitRouteResult.getRouteLines().get(0).getAllStep();
				TransitRouteAdapter transitRouteAdapter = new TransitRouteAdapter(context, list);
				listView.setCacheColorHint(Color.TRANSPARENT);
				listView.setAdapter(transitRouteAdapter);
			} else if ("步行".equals(navType)) {
				WalkingRouteResult walkingRouteResult = Utilities.walkingRouteResult;
				List<WalkingRouteLine.WalkingStep> list = walkingRouteResult.getRouteLines().get(0).getAllStep();
				WalkingRouteAdapter walkingRouteAdapter = new WalkingRouteAdapter(context, list);
				listView.setCacheColorHint(Color.TRANSPARENT);
				listView.setAdapter(walkingRouteAdapter);
			}
		}
//		listView.setOnItemClickListener(new OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//									long arg3) {
//				Intent intent = new Intent();
//				Bundle bundle = new Bundle();
//				bundle.putString("index", String.valueOf(arg3));
//				intent.putExtras(bundle);
//				LookRouteActivity.this.setResult(RESULT_FIRST_USER, intent);
//				finish();
//			}
//		});
	}

	@Override

	public void onBackPressed() {
		finish();
	}


	class DrivingRouteAdapter extends BaseAdapter {

		private Context context;
		private List<DrivingRouteLine.DrivingStep> list;

		public DrivingRouteAdapter(Context context, List<DrivingRouteLine.DrivingStep> list) {
			this.context = context;
			this.list = list;
		}

		@Override
		public int getCount() {
			if (list != null)
				return list.size();
			return 0;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = LayoutInflater.from(context).inflate(R.layout.listitem_map_route_adapter, null);
			TextView routeNumTextView = (TextView) convertView.findViewById(R.id.route_number);
			TextView routeContentTextView = (TextView) convertView.findViewById(R.id.route_content);
			routeNumTextView.setText((position + 1) + "、");
			routeContentTextView.setText(list.get(position).getInstructions());
			return convertView;
		}
	}

	class TransitRouteAdapter extends BaseAdapter {

		private Context context;
		private List<TransitRouteLine.TransitStep> list;

		public TransitRouteAdapter(Context context, List<TransitRouteLine.TransitStep> list) {
			this.context = context;
			this.list = list;
		}

		@Override
		public int getCount() {
			if (list != null)
				return list.size();
			return 0;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = LayoutInflater.from(context).inflate(R.layout.listitem_map_route_adapter, null);
			TextView routeNumTextView = (TextView) convertView.findViewById(R.id.route_number);
			TextView routeContentTextView = (TextView) convertView.findViewById(R.id.route_content);
			routeNumTextView.setText((position + 1) + "、");
			routeContentTextView.setText(list.get(position).getInstructions());
			return convertView;
		}
	}

	class WalkingRouteAdapter extends BaseAdapter {

		private Context context;
		private List<WalkingRouteLine.WalkingStep> list;

		public WalkingRouteAdapter(Context context, List<WalkingRouteLine.WalkingStep> list) {
			this.context = context;
			this.list = list;
		}

		@Override
		public int getCount() {
			if (list != null)
				return list.size();
			return 0;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = LayoutInflater.from(context).inflate(R.layout.listitem_map_route_adapter, null);
			TextView routeNumTextView = (TextView) convertView.findViewById(R.id.route_number);
			TextView routeContentTextView = (TextView) convertView.findViewById(R.id.route_content);
			routeNumTextView.setText((position + 1) + "、");
			routeContentTextView.setText(list.get(position).getInstructions());
			return convertView;
		}
	}
}
