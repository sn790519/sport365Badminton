package com.sport365.badminton.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.sport365.badminton.R;
import com.sport365.badminton.entity.obj.ActiveEntityObj;
import com.sport365.badminton.http.base.ImageLoader;

import java.util.ArrayList;

/**
 * Created by kjh08490 on 2015/2/15.
 */
public class ActivityAdapter extends BaseAdapter {
	public ArrayList<ActiveEntityObj> alctiveList = new ArrayList<ActiveEntityObj>();// 列表
	private LayoutInflater mLayoutInflater;

	public ActivityAdapter(LayoutInflater mLayoutInflater, ArrayList<ActiveEntityObj> alctiveList) {
		this.mLayoutInflater = mLayoutInflater;
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
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.activity_item_layout, null);
			viewHolder.tv_venue = (TextView) convertView.findViewById(R.id.tv_venue);
			viewHolder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
			viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			viewHolder.tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
			viewHolder.tv_distance = (TextView) convertView.findViewById(R.id.tv_distance);
			viewHolder.tv_sign_alredy = (TextView) convertView.findViewById(R.id.tv_sign_alredy);
			viewHolder.tv_activity_sign = (TextView) convertView.findViewById(R.id.tv_activity_sign);
			viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
			viewHolder.iv_activity_flag = (ImageView) convertView.findViewById(R.id.iv_activity_flag);
			viewHolder.iv_tag_top = (ImageView) convertView.findViewById(R.id.iv_tag_top);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		ActiveEntityObj mActiveEntityObj = alctiveList.get(position);
		if (mActiveEntityObj != null) {
			// 场馆
			String clubName = !TextUtils.isEmpty(mActiveEntityObj.clubName) ? mActiveEntityObj.clubName : "";
			viewHolder.tv_venue.setText(clubName);
			// 价格
			String activeFee = !TextUtils.isEmpty(mActiveEntityObj.activeFee) ? mActiveEntityObj.activeFee : "";
			viewHolder.tv_price.setText(activeFee + "元");
			// 图片
			String activeLogo = !TextUtils.isEmpty(mActiveEntityObj.activeLogo) ? mActiveEntityObj.activeLogo : "";
			ImageLoader.getInstance().displayImage(activeLogo, viewHolder.imageView);
			// 时间
			String activeDate = !TextUtils.isEmpty(mActiveEntityObj.activeDate) ? mActiveEntityObj.activeDate : "";
			String endTime = TextUtils.isEmpty(mActiveEntityObj.endTime) ? mActiveEntityObj.endTime : "";
			viewHolder.tv_time.setText(activeDate + "--" + endTime);
			// 地址
			String provinceName = !TextUtils.isEmpty(mActiveEntityObj.provinceName) ? mActiveEntityObj.provinceName : "";
			String cityName = !TextUtils.isEmpty(mActiveEntityObj.cityName) ? mActiveEntityObj.cityName : "";
			String countyName = !TextUtils.isEmpty(mActiveEntityObj.countyName) ? mActiveEntityObj.countyName : "";
			String venueName = !TextUtils.isEmpty(mActiveEntityObj.venueName) ? mActiveEntityObj.venueName : "";
			viewHolder.tv_distance.setText(provinceName + "  " + cityName + "  " + countyName + "  " + "\n" + venueName);
			// 俱乐部
			String realNum = !TextUtils.isEmpty(mActiveEntityObj.realNum) ? mActiveEntityObj.realNum : "";
			viewHolder.tv_sign_alredy.setText(realNum + "人已报名");
			// 活动
			String huiTips = !TextUtils.isEmpty(mActiveEntityObj.huiTips) ? mActiveEntityObj.huiTips : "";
			viewHolder.tv_activity_sign.setText(huiTips);
		}
		return convertView;
	}

	class ViewHolder {
		TextView tv_venue;        // 场馆
		TextView tv_price;        // 价格
		ImageView imageView;        // 图片
		ImageView iv_tag_top;        // 置顶图片
		ImageView iv_activity_flag;        // 进行中
		TextView tv_time;        // 时间
		TextView tv_phone;        // 电话
		TextView tv_distance;    // 地址
		TextView tv_sign_alredy;        // 俱乐部
		TextView tv_activity_sign;    // 活动
	}
}
