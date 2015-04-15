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
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.TransitRouteOverlay;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.*;
import com.baidu.navisdk.comapi.mapcontrol.BNMapController;
import com.baidu.navisdk.comapi.routeplan.BNRoutePlaner;
import com.sport365.badminton.BaseFragment;
import com.sport365.badminton.R;
import com.sport365.badminton.activity.MapViewActivity;
import com.sport365.badminton.map.BDLocationHelper;
import com.sport365.badminton.utils.Utilities;

/**
 * 地图
 */
public class MapViewFragment extends BaseFragment {
	private BaiduMap mBaiduMap;
	private MapView mMapView = null;
	private View view;
	private MapStatusUpdate zoomTo;
	private InfoWindow mInfoWindow;
	private RoutePlanSearch mSearch;

	/**
	 * 当前选中的marker
	 */
	private Marker mCurrentmMrker;

	// 弹出气泡窗口
	private View mapPopView;
	private TextView tv_name;
	private Dialog alertDialog;

	private LayoutInflater mLayoutInflater;

	private DrivingRouteOverlay drivingOverlay;
	private WalkingRouteOverlay walkOverlay;
	private TransitRouteOverlay transitOverlay;

	/**
	 * 导航路线规划成功的回调
	 */
	private OnRoutePlanSuccessListener onRoutePlanSuccessListener;
	private String naviType;


	// 经纬度变量
	private String lat = "";
	private String lon = "";
	private String name = "目的地";


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
		zoomTo = MapStatusUpdateFactory.zoomTo(Float.parseFloat("13"));//缩放到11
		mBaiduMap.animateMapStatus(zoomTo);
		initLocation();
		initPopView();
//		initOverlay();
		Bundle bundle = getArguments();
		if (null != bundle) {
			lat = bundle.getString(MapViewActivity.LAT);
			lon = bundle.getString(MapViewActivity.LON);
			name = TextUtils.isEmpty(bundle.getString(MapViewActivity.NAME)) ? "目的地" : bundle.getString(MapViewActivity.NAME);
			if (!TextUtils.isEmpty(lat) && !TextUtils.isEmpty(lon)) {
				setData(lat, lon);
			}
		}
	}

	private void initLocation() {
		mBaiduMap.setMyLocationEnabled(true);
		mBaiduMap.setMyLocationData(BDLocationHelper.mMyLocationData);
	}


	public void setData(String lat, String lon) {
		LatLng point = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
		BitmapDescriptor bitmap = BitmapDescriptorFactory
				.fromResource(R.drawable.icon_balloon);
		OverlayOptions option = new MarkerOptions()
				.position(point)
				.icon(bitmap).title(name);
		//在地图上添加Marker，并显示
		Marker marker = (Marker) mBaiduMap.addOverlay(option);
		mCurrentmMrker = marker;
		MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(point);
		mBaiduMap.animateMapStatus(u);
		mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(final Marker marker) {
				tv_name.setText(marker.getTitle());
				LatLng ll = marker.getPosition();
				mInfoWindow = new InfoWindow(mapPopView, ll, -5);
				mBaiduMap.showInfoWindow(mInfoWindow);
				mCurrentmMrker = marker;
				return true;
			}
		});
	}

//
//	private void initOverlay() {
//		//添加Marker
//		LatLng point = new LatLng(31.297048, 120.704062);
//		LatLng point1 = new LatLng(31.309636, 120.672442);
//		LatLng point2 = new LatLng(31.279766, 120.66468);
//		BitmapDescriptor bitmap = BitmapDescriptorFactory
//				.fromResource(R.drawable.icon_balloon);
//		OverlayOptions option = new MarkerOptions()
//				.position(point)
//				.icon(bitmap).title("景点");
//		//在地图上添加Marker，并显示
//		Marker marker = (Marker) mBaiduMap.addOverlay(option);
//
//		OverlayOptions option1 = new MarkerOptions()
//				.position(point1).title("景点1")
//				.icon(bitmap);
//		Marker marker1 = (Marker) mBaiduMap.addOverlay(option1);
//
//		OverlayOptions option2 = new MarkerOptions()
//				.position(point2).title("景点2")
//				.icon(bitmap);
//		Marker marker2 = (Marker) mBaiduMap.addOverlay(option2);
//
//		MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(point);
//		mBaiduMap.animateMapStatus(u);
//		mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
//			@Override
//			public boolean onMarkerClick(final Marker marker) {
//				tv_name.setText(marker.getTitle());
//				LatLng ll = marker.getPosition();
//				mInfoWindow = new InfoWindow(mapPopView, ll, -5);
//				mBaiduMap.showInfoWindow(mInfoWindow);
//				mCurrentmMrker = marker;
//				return true;
//			}
//		});
//	}

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


	private void showNavDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setIcon(android.R.drawable.ic_menu_more);
		builder.setTitle("请选择");
		ListItemAdapter adapter = new ListItemAdapter();
		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int which) {
				if (null != mCurrentmMrker) {
					PlanNode stNode = PlanNode.withLocation(new LatLng(BDLocationHelper.mCurrentLocation.getLatitude(), BDLocationHelper.mCurrentLocation.getLongitude()));
					PlanNode edNode = PlanNode.withLocation(mCurrentmMrker.getPosition());
					mSearch = RoutePlanSearch.newInstance();
					mSearch.setOnGetRoutePlanResultListener(onGetRoutePlanResultListener);
					if (which == 0) {
						//驾车
						mSearch.drivingSearch(new DrivingRoutePlanOption().from(stNode).to(edNode));
					} else if (which == 1) {
						//公交
						mSearch.transitSearch((new TransitRoutePlanOption()).from(stNode).city(BDLocationHelper.mCurrentLocation.getCity()).to(edNode));
					} else if (which == 2) {
						//步行
						mSearch.walkingSearch((new WalkingRoutePlanOption()).from(stNode).to(edNode));
					}
				}
				if (!alertDialog.isShowing())
					alertDialog.show();
			}
		};
		builder.setAdapter(adapter, listener);
		alertDialog = builder.create();
		alertDialog.show();
	}

	/**
	 * 路线规划
	 */
	OnGetRoutePlanResultListener onGetRoutePlanResultListener = new OnGetRoutePlanResultListener() {
		public void onGetWalkingRouteResult(WalkingRouteResult result) {
			//步行
			if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
				Utilities.showToast("抱歉，未找到结果", getActivity());
			}
			if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
				//起终点或途经点地址有岐义，通过以下接口获取建议查询信息
				//result.getSuggestAddrInfo()
				return;
			}
			if (result.error == SearchResult.ERRORNO.NO_ERROR) {
				walkOverlay = new WalkingRouteOverlay(mBaiduMap);
				mBaiduMap.setOnMarkerClickListener(walkOverlay);
				walkOverlay.setData(result.getRouteLines().get(0));
				if (null != transitOverlay)
					transitOverlay.removeFromMap();
				if (null != drivingOverlay)
					drivingOverlay.removeFromMap();
				walkOverlay.addToMap();
				walkOverlay.zoomToSpan();
				if (onRoutePlanSuccessListener != null) {
					naviType = "步行";
					Utilities.walkingRouteResult = result;
					onRoutePlanSuccessListener.routePlanSuccess(naviType);
				}
			}
			mSearch.destroy();
		}

		public void onGetTransitRouteResult(TransitRouteResult result) {
			//公交
			if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
				Utilities.showToast("抱歉，未找到结果", getActivity());
			}
			if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
				//起终点或途经点地址有岐义，通过以下接口获取建议查询信息
				//result.getSuggestAddrInfo()
				return;
			}
			if (result.error == SearchResult.ERRORNO.NO_ERROR) {
				transitOverlay = new TransitRouteOverlay(mBaiduMap);
				mBaiduMap.setOnMarkerClickListener(transitOverlay);
				transitOverlay.setData(result.getRouteLines().get(0));
				if (null != walkOverlay)
					walkOverlay.removeFromMap();
				if (null != drivingOverlay)
					drivingOverlay.removeFromMap();
				transitOverlay.addToMap();
				transitOverlay.zoomToSpan();
				if (onRoutePlanSuccessListener != null) {
					naviType = "公交";
					Utilities.transitRouteResult = result;
					onRoutePlanSuccessListener.routePlanSuccess(naviType);
				}
			}
			mSearch.destroy();
		}

		public void onGetDrivingRouteResult(DrivingRouteResult result) {
			//驾车
			if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
				Utilities.showToast("抱歉，未找到结果", getActivity());
			}
			if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
				//起终点或途经点地址有岐义，通过以下接口获取建议查询信息
				//result.getSuggestAddrInfo()
				return;
			}
			if (result.error == SearchResult.ERRORNO.NO_ERROR) {
				drivingOverlay = new DrivingRouteOverlay(mBaiduMap);
				mBaiduMap.setOnMarkerClickListener(drivingOverlay);
				drivingOverlay.setData(result.getRouteLines().get(0));
				if (null != walkOverlay)
					walkOverlay.removeFromMap();
				if (null != transitOverlay)
					transitOverlay.removeFromMap();
				drivingOverlay.addToMap();
				drivingOverlay.zoomToSpan();
				if (onRoutePlanSuccessListener != null) {
					naviType = "驾车";
					Utilities.drivingRouteResult = result;
					onRoutePlanSuccessListener.routePlanSuccess(naviType);
				}
			}
			mSearch.destroy();
		}
	};


	@Override
	public void onDestroy() {
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

	public void setonRoutePlanSuccessListener(OnRoutePlanSuccessListener listener) {
		this.onRoutePlanSuccessListener = listener;
	}

	public interface OnRoutePlanSuccessListener {
		public void routePlanSuccess(String naviType);
	}
}