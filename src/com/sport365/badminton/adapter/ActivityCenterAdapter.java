package com.sport365.badminton.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.sport365.badminton.R;
import com.sport365.badminton.entity.obj.VenueEntityObj;
import com.sport365.badminton.http.base.ImageLoader;

import java.util.ArrayList;

/**
 * Created by kjh08490 on 2015/2/15.
 */
public class ActivityCenterAdapter extends BaseAdapter {
	private ArrayList<VenueEntityObj> venueEntity = new ArrayList<VenueEntityObj>();
	private LayoutInflater mLayoutInflater;

	public ActivityCenterAdapter(LayoutInflater mLayoutInflater, ArrayList<VenueEntityObj> venueEntity) {
		this.mLayoutInflater = mLayoutInflater;
		this.venueEntity = venueEntity;
	}

	@Override
	public int getCount() {
		return venueEntity.size();
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.activity_center_item_layout, null);
			viewHolder.tv_venue = (TextView) convertView.findViewById(R.id.tv_venue);
			viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			viewHolder.tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
			viewHolder.tv_distance = (TextView) convertView.findViewById(R.id.tv_distance);
			viewHolder.tv_club = (TextView) convertView.findViewById(R.id.tv_club);
			viewHolder.tv_activity = (TextView) convertView.findViewById(R.id.tv_activity);
			viewHolder.tv_game = (TextView) convertView.findViewById(R.id.tv_game);
			viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		VenueEntityObj venueEntityobj = venueEntity.get(position);
		if (venueEntityobj != null) {
			// 名称
			viewHolder.tv_venue.setText(!TextUtils.isEmpty(venueEntityobj.name) ? venueEntityobj.name : "");
			// 图片
			ImageLoader.getInstance().displayImage(!TextUtils.isEmpty(venueEntityobj.logo) ? venueEntityobj.logo : "", viewHolder.imageView);
			// 时间
			String openingTime = !TextUtils.isEmpty(venueEntityobj.openingTime) ? venueEntityobj.openingTime : "";
			String closingTime = !TextUtils.isEmpty(venueEntityobj.closingTime) ? venueEntityobj.closingTime : "";
			viewHolder.tv_time.setText(openingTime + "--" + closingTime);
			// 地址
			String provinceName = !TextUtils.isEmpty(venueEntityobj.provinceName) ? venueEntityobj.provinceName : "";
			String cityName = !TextUtils.isEmpty(venueEntityobj.cityName) ? venueEntityobj.cityName : "";
			String countyName = !TextUtils.isEmpty(venueEntityobj.countyName) ? venueEntityobj.countyName : "";
			String address = !TextUtils.isEmpty(venueEntityobj.address) ? venueEntityobj.address : "";
			viewHolder.tv_distance.setText(provinceName + "  " + cityName + "  " + countyName + "  " + "\n" + address);
			//俱乐部
			String clubNum = !TextUtils.isEmpty(venueEntityobj.clubNum) ? venueEntityobj.clubNum : "";
			viewHolder.tv_club.setText("俱乐部（" + clubNum + "）");
			//活动
			String activeNum = !TextUtils.isEmpty(venueEntityobj.activeNum) ? venueEntityobj.activeNum : "";
			viewHolder.tv_club.setText("活动（" + activeNum + "）");
			//比赛
			String matchNum = !TextUtils.isEmpty(venueEntityobj.matchNum) ? venueEntityobj.matchNum : "";
			viewHolder.tv_club.setText("比赛（" + matchNum + "）");
		}
		return convertView;
	}

	class ViewHolder {
		TextView tv_venue;        // 场馆
		ImageView imageView;        // 图片
		TextView tv_time;        // 时间
		TextView tv_phone;        // 电话
		TextView tv_distance;    // 地址
		TextView tv_club;        // 俱乐部
		TextView tv_activity;    // 活动
		TextView tv_game;        // 比赛
	}
}
