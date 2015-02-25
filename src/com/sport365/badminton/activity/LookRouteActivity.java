package com.sport365.badminton.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.sport365.badminton.BaseActivity;
import com.sport365.badminton.R;
import com.sport365.badminton.utils.Utilities;

import java.util.List;

/**
 * 路线查看
 */
public class LookRouteActivity extends BaseActivity {

    private ListView listView;
    private RouteAdapter routeAdapter;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_look_route);
        Bundle bundle = getIntent().getExtras();
        String navType = bundle.getString("naviType");
        setActionBarTitle("查看" + navType + "路线");
        listView = (ListView) findViewById(R.id.look_route_listview);

        if (!TextUtils.isEmpty(navType)) {
            if ("驾车".equals(navType)) {

            } else if ("公交".equals(navType)) {

            } else if ("步行".equals(navType)) {
                WalkingRouteResult walkingRouteResult = Utilities.walkingRouteResult;
                List<WalkingRouteLine> list = walkingRouteResult.getRouteLines();
                count = list.size();
            }
        }


//		if (Utilities.tranRoute != null) {
//			count = Utilities.tranRoute.getNumLines();
//			routeAdapter = new RouteAdapter();
//			listView.setCacheColorHint(Color.TRANSPARENT);
//			listView.setAdapter(routeAdapter);
//		}

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


    class RouteAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return count;//Utilities.mkRoute.getNumSteps();//25;
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
//			LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.listitem_map_route_adapter, null);
//			TextView routeNumTextView = (TextView) linearLayout.findViewById(R.id.route_number);
//			TextView routeContentTextView = (TextView) linearLayout.findViewById(R.id.route_content);
//
//			routeNumTextView.setText((position + 1) + "、");
//			if (Utilities.mkRoute != null) {
//				routeContentTextView.setText(Utilities.mkRoute.getStep(position).getContent());
//			} else if (Utilities.tranRoute != null) {
//				MKLine line = Utilities.tranRoute.getLine(position);
//
//				String type = line.getType() == MKLine.LINE_TYPE_BUS ? "公交" : "城铁";
//
//				int numViaStops = line.getNumViaStops();
//
//				String offStop = line.getGetOffStop().name;
//				String onStop = line.getGetOnStop().name;
//				String title = line.getTitle();
//
//				String show = "乘坐" + type + " " + title + " 自 "
//						+ onStop + " 经过 " + numViaStops + " 站到 " + offStop;
//
//				routeContentTextView.setText(show);
//			}
//			return linearLayout;
            return null;
        }

    }
}
