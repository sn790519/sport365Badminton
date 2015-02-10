package com.sport365.badminton.activity.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.sport365.badminton.BaseFragment;
import com.sport365.badminton.R;
import com.sport365.badminton.utils.SharedPreferencesKeys;
import com.sport365.badminton.utils.SharedPreferencesUtils;

/**
 * 地图
 */
public class MapViewFragment extends BaseFragment {
	public BDLocationListener myListener = new MyLocationListener();
	private BaiduMap mBaiduMap;
	private LocationClient mLocClient;
	boolean isSuccessLocation = false;// 是否首次定位
	private MapView mMapView = null;
	private LocationClientOption option;
	private MapViewFragment sceneryDetailView;
	private View view;
	private Marker mMarker;
	private MapStatusUpdate zoomTo;
	private BitmapDescriptor bitmap;

	private boolean isDestroy = false;

	private String[] textIdsWithBus = {"驾车", "公交", "步行"};
//	private MKPlanNode start, end;

	/**
	 * 定位失败，百度返回默认的经纬度值
	 */
	private final String BAIDU_DEFAULT_VALUE = "4.9E-324";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_mapview, container, false);
		initBaiduMap();
		return view;
	}

	private void initBaiduMap() {
		mMapView = (MapView) view.findViewById(R.id.bmapView);
//		 地图初始化
		mBaiduMap = mMapView.getMap();
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化
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
		zoomTo = MapStatusUpdateFactory.zoomTo(Float.parseFloat("11"));//缩放到11
		mBaiduMap.animateMapStatus(zoomTo);

		LatLng point = new LatLng(31.310284, 120.680922);
		//构建Marker图标
		BitmapDescriptor bitmap = BitmapDescriptorFactory
				.fromResource(R.drawable.icon_balloon);
		//构建MarkerOption，用于在地图上添加Marker
		OverlayOptions option = new MarkerOptions()
				.position(point)
				.icon(bitmap);
		//在地图上添加Marker，并显示
		mBaiduMap.addOverlay(option);
		MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(point);
		mBaiduMap.animateMapStatus(u);

		mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(Marker marker) {

				return true;
			}
		});

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

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}


//	private void showNavDialog() {
//		AlertDialog.Builder builder = new AlertDialog.Builder(this);
//		builder.setIcon(android.R.drawable.ic_menu_more);
//		builder.setTitle("请选择");
//		ListItemAdapter adapter = new ListItemAdapter();
//		adapter.textIds = textIdsWithBus;
//		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialogInterface, int which) {
//
//				if (start.pt.getLatitudeE6() == 0) {
//					// 从缓存重新设置起点位置
//					if (Utilities.latitude != 0 && Utilities.longitude != 0) {
//						start.pt = new GeoPoint((int) (Utilities.latitude * 1E6), (int) (Utilities.longitude * 1E6));
//					} else {
//						BDLocation location = BDLocationManager.getInstance(NavigationMapActivity.this).getmLocationClient().getLastKnownLocation();
//						if (location != null) {
//							start.pt = new GeoPoint((int) (location.getLatitude() * 1E6), (int) (location.getLongitude() * 1E6));
//						} else {
//							BDLocationManager.getInstance(NavigationMapActivity.this).startLocation(true);//开启定位
//							Utilities.showToast("正在获取定位信息，请稍后再试！", getApplication());
//							return;
//						}
//					}
//				}
//				if (end == null) {
//					if (destinations != null && !destinations.isEmpty()) {
//						end = new MKPlanNode();
//						end.pt = new GeoPoint((int) (destinations.get(0).lat * 1E6), (int) (destinations.get(0).lon * 1E6));
//					} else {
//						Utilities.showToast("抱歉，无法获得相应的导航信息！", getApplication());
//						return;
//					}
//				}
//				Utilities.mkRoute = null;
//				Utilities.tranRoute = null;
//
//				if (which == 0) {
//					navType = "驾车";
//					mKSearch.setDrivingPolicy(MKSearch.ECAR_TIME_FIRST);
//					// 驾乘路线搜索.
//					mKSearch.drivingSearch("", start, "", end);
//				} else if (which == 1) {
//					if (navigationWithBus) {
//						navType = "公交";
//						mKSearch.setTransitPolicy(MKSearch.EBUS_TRANSFER_FIRST);
//						mKSearch.transitSearch(data.cityName, start, end);
//					} else {
//						navType = "步行";
//						mKSearch.walkingSearch("", start, "", end);
//					}
//				} else if (which == 2) {
//					navType = "步行";
//					mKSearch.walkingSearch("", start, "", end);
//				}
//
//				if (!TextUtils.isEmpty(navType)) {
//					setActionBarTitle(navType);
//				}
//				if (!alertDialog.isShowing())
//					alertDialog.show();
//			}
//		};
//		builder.setAdapter(adapter, listener);
//		Dialog dialog = builder.create();
//		dialog.show();
//	}


//	public void addOverLay(ArrayList<Scenery> scenerys) {
//		if (null != mBaiduMap) {
//			mBaiduMap.clear();
//		}
//
//		LatLng ll = null;
//		Scenery scenery = null;
//		Button button = null;
//		String lat = "", lon = "";
//		OverlayOptions ooA;
//
//		mBaiduMap.animateMapStatus(zoomTo);
//		for (int i = 0; i < scenerys.size(); i++) {
//			scenery = scenerys.get(i);
//			lat = scenery.latitude;
//			lon = scenery.longitude;
//			if (TextUtils.isEmpty(lat) || TextUtils.isEmpty(lon)) {
//				return;
//			}
//			ll = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
//
//			if (i == 0) {//将地图的中心点标记到第一个景点的经纬度上
//				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
//				mBaiduMap.animateMapStatus(u);
//			}
//			button = new Button(getActivity());
//			button.setBackgroundResource(R.drawable.map_bubble_price);
//			button.setGravity(Gravity.CENTER);
//			button.setPadding(0, 0, 0, 20);
//			button.setText(scenery.sceneryName);
//			bitmap = BitmapDescriptorFactory.fromView(button);
//			ooA = new MarkerOptions().position(ll).icon(bitmap).zIndex(9).draggable(false);
//			mMarker = (Marker) (mBaiduMap.addOverlay(ooA));
//			mMarker.setZIndex(Integer.parseInt(scenery.sceneryId));
//			mHashMap.put(scenery.sceneryId, scenery);
//		}

//	}


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
		if (null != bitmap) {
			bitmap.recycle();
		}
	}

	class ListItemAdapter extends BaseAdapter {

		String[] textIds;
		int[] imgIds;

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