package com.sport365.badminton.activity.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.navisdk.CommonParams;
import com.baidu.navisdk.comapi.mapcontrol.BNMapController;
import com.baidu.navisdk.comapi.mapcontrol.MapParams;
import com.baidu.navisdk.comapi.routeplan.BNRoutePlaner;
import com.baidu.navisdk.comapi.routeplan.IRouteResultObserver;
import com.baidu.navisdk.comapi.routeplan.RoutePlanParams;
import com.baidu.navisdk.model.NaviDataEngine;
import com.baidu.navisdk.model.RoutePlanModel;
import com.baidu.navisdk.model.datastruct.RoutePlanNode;
import com.baidu.navisdk.ui.widget.RoutePlanObserver;
import com.sport365.badminton.BaseFragment;
import com.sport365.badminton.R;
import com.sport365.badminton.utils.SharedPreferencesKeys;
import com.sport365.badminton.utils.SharedPreferencesUtils;
import com.sport365.badminton.utils.ULog;
import com.sport365.badminton.utils.Utilities;

import java.util.ArrayList;

/**
 * 地图
 */
public class MapViewFragment extends BaseFragment {
	public BDLocationListener myListener = new MyLocationListener();
	private BaiduMap mBaiduMap;
	private LocationClient mLocClient;
	private MapView mMapView = null;
	private LocationClientOption option;
	private View view;
	private MapStatusUpdate zoomTo;
	private InfoWindow mInfoWindow;
	private Marker mCurrentmMrker;

	boolean isSuccessLocation = false;// 是否首次定位
	private boolean isDestroy = false;

	// 弹出气泡窗口
	private View mapPopView;
	private TextView tv_name;
	private Dialog alertDialog;

	// 定位失败，百度返回默认的经纬度值
	private final String BAIDU_DEFAULT_VALUE = "4.9E-324";
	private LayoutInflater mLayoutInflater;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_mapview, container, false);
		mLayoutInflater = inflater;
		initBaiduMap();
		return view;
	}

	private void initBaiduMap() {
		mMapView = (MapView) view.findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		zoomTo = MapStatusUpdateFactory.zoomTo(Float.parseFloat("11"));//缩放到11
		mBaiduMap.animateMapStatus(zoomTo);

		initLocation();
		initPopView();
		initOverlay();
	}

	// 定位
	private void initLocation() {
		mBaiduMap.setMyLocationEnabled(true);
		mLocClient = new LocationClient(getActivity().getApplicationContext());
		mLocClient.registerLocationListener(myListener);
		option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(2000);
		option.setAddrType("all");
		option.setIsNeedAddress(true);
		mLocClient.setLocOption(option);
		mLocClient.start();
	}

	private void initOverlay() {
		//添加Marker
		LatLng point = new LatLng(31.297048, 120.704062);
		LatLng point1 = new LatLng(31.309636, 120.672442);
		LatLng point2 = new LatLng(31.279766, 120.66468);
		BitmapDescriptor bitmap = BitmapDescriptorFactory
				.fromResource(R.drawable.icon_balloon);
		OverlayOptions option = new MarkerOptions()
				.position(point)
				.icon(bitmap).title("景点");
		//在地图上添加Marker，并显示
		Marker marker = (Marker) mBaiduMap.addOverlay(option);

		OverlayOptions option1 = new MarkerOptions()
				.position(point1).title("景点1")
				.icon(bitmap);
		Marker marker1 = (Marker) mBaiduMap.addOverlay(option1);

		OverlayOptions option2 = new MarkerOptions()
				.position(point2).title("景点2")
				.icon(bitmap);
		Marker marker2 = (Marker) mBaiduMap.addOverlay(option2);

		MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(point);
		mBaiduMap.animateMapStatus(u);
		mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(final Marker marker) {
				tv_name.setText(marker.getTitle());
				LatLng ll = marker.getPosition();
				mInfoWindow = new InfoWindow(mapPopView, ll, -5);
				mBaiduMap.showInfoWindow(mInfoWindow);
				return true;
			}
		});
	}

	private void initPopView() {
		//地图弹出黑色气泡窗
		mapPopView = mLayoutInflater.inflate(R.layout.pop_map_layout, null);
		LinearLayout ll_popclick = (LinearLayout) mapPopView.findViewById(R.id.ll_popclick);
		tv_name = (TextView) mapPopView.findViewById(R.id.tv_pop_name);
		ll_popclick.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showNavDialog();
			}
		});
	}

	@Override
	public void onPause() {
		super.onPause();
		BNRoutePlaner.getInstance().setRouteResultObserver(null);
		BNMapController.getInstance().onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		BNMapController.getInstance().onResume();
	}

	/**
	 * 定位监听函数
	 */
	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;
			// 此处设置开发者获取到的方向信息，顺时针0-360
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			String latitude = String.valueOf(location.getLatitude());
			String longitude = String.valueOf(location.getLongitude());
			if (!BAIDU_DEFAULT_VALUE.equals(latitude) && !BAIDU_DEFAULT_VALUE.equals(longitude)) {//4.9E-324
				SharedPreferencesUtils.getInstance(getActivity()).putString(SharedPreferencesKeys.LOCATION_LAT, latitude);
				SharedPreferencesUtils.getInstance(getActivity()).putString(SharedPreferencesKeys.LOCATION_LON, longitude);
				SharedPreferencesUtils.getInstance(getActivity()).commitValue();
				//定给成功
				isSuccessLocation = true;
			}
			mBaiduMap.setMyLocationData(locData);
			//如果定位失败每隔两秒再定位一次
//			if (!isSuccessLocation && !isDestroy) {
//				LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
//				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
//				mBaiduMap.animateMapStatus(u);
//			}
		}
	}


	private void showNavDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setIcon(android.R.drawable.ic_menu_more);
		builder.setTitle("请选择");
		ListItemAdapter adapter = new ListItemAdapter();
		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int which) {
				if (which == 0) {
					routePlan();
				} else if (which == 1) {
					Utilities.showToast("11111", getActivity());
				} else if (which == 2) {
					Utilities.showToast("22222", getActivity());
				}
				if (!alertDialog.isShowing())
					alertDialog.show();
			}
		};
		builder.setAdapter(adapter, listener);
		alertDialog = builder.create();
		alertDialog.show();
	}

	private void routePlan() {
		RoutePlanNode startNode = new RoutePlanNode((int) (31.297048 * 1E6), (int) (120.704062 * 1E6),
				RoutePlanNode.FROM_MAP_POINT, "111", "aaaaaa");
		RoutePlanNode endNode = new RoutePlanNode((int) (31.309636 * 1E6), (int) (120.672442 * 1E6),
				RoutePlanNode.FROM_MAP_POINT, "222", "bbbbbb");
		//将起终点添加到nodeList
		ArrayList<RoutePlanNode> nodeList = new ArrayList<RoutePlanNode>(2);
		nodeList.add(startNode);
		nodeList.add(endNode);
		BNRoutePlaner.getInstance().setObserver(new RoutePlanObserver(getActivity(), null));
		//设置算路方式
		BNRoutePlaner.getInstance().setCalcMode(RoutePlanParams.NE_RoutePlan_Mode.ROUTE_PLAN_MOD_MIN_TIME);
		// 设置算路结果回调
		BNRoutePlaner.getInstance().setRouteResultObserver(mRouteResultObserver);
		// 设置起终点并算路
		boolean ret = BNRoutePlaner.getInstance().setPointsToCalcRoute(
				nodeList, CommonParams.NL_Net_Mode.NL_Net_Mode_OnLine);
		if (!ret) {
			Utilities.showToast("规划失败", getActivity());
		}
	}

	private RoutePlanModel mRoutePlanModel = null;
	private IRouteResultObserver mRouteResultObserver = new IRouteResultObserver() {

		@Override
		public void onRoutePlanYawingSuccess() {
			// TODO Auto-generated method stub
			ULog.debug("-------onRoutePlanYawingSuccess");
		}

		@Override
		public void onRoutePlanYawingFail() {
			// TODO Auto-generated method stub
			ULog.debug("-------onRoutePlanYawingFail");
		}

		@Override
		public void onRoutePlanSuccess() {
			// TODO Auto-generated method stub
			BNMapController.getInstance().setLayerMode(
					MapParams.Const.LayerMode.MAP_LAYER_MODE_ROUTE_DETAIL);
			mRoutePlanModel = (RoutePlanModel) NaviDataEngine.getInstance()
					.getModel(CommonParams.Const.ModelName.ROUTE_PLAN);
			ULog.debug("-------onRoutePlanSuccess");
		}

		@Override
		public void onRoutePlanFail() {
			// TODO Auto-generated method stub
			ULog.debug("-------onRoutePlanFail");
		}

		@Override
		public void onRoutePlanCanceled() {
			// TODO Auto-generated method stub
			ULog.debug("-------onRoutePlanCanceled");
		}

		@Override
		public void onRoutePlanStart() {
			// TODO Auto-generated method stub
			ULog.debug("-------onRoutePlanStart");
		}

	};

	@Override
	public void onDestroy() {
//		// 退出时销毁定位
		mLocClient.stop();
		isDestroy = true;
//		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		super.onDestroy();
	}

	class ListItemAdapter extends BaseAdapter {

		String[] textIds = {"驾车", "公交", "步行"};
		int[] imgIds = {R.drawable.car, R.drawable.bus, R.drawable.walk};

		@Override
		public int getCount() {
			return textIds.length;
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
		public View getView(int position, View contentView, ViewGroup parent) {
			TextView textView = new TextView(getActivity());
			// 获得array.xml中的数组资源getStringArray返回的是一个String数组
			String text = textIds[position];
			textView.setText(text);
			// 设置字体大小
			textView.setTextSize(18);
			AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			textView.setLayoutParams(layoutParams);
			// 设置水平方向上居中
			textView.setGravity(android.view.Gravity.CENTER_VERTICAL);
			textView.setMinHeight(65);
			// 设置文字颜色
			textView.setTextColor(Color.BLACK);
			// 设置图标在文字的左边
			textView.setCompoundDrawablesWithIntrinsicBounds(imgIds[position], 0, 0, 0);
			// 设置textView的左上右下的padding大小
			textView.setPadding(15, 30, 15, 30);
			// 设置文字和图标之间的padding大小
			textView.setCompoundDrawablePadding(15);
			return textView;
		}

	}
}