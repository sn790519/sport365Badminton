package com.sport365.badminton.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sport365.badminton.R;
import com.sport365.badminton.entity.obj.MatchEntityObj;
import com.sport365.badminton.http.base.ImageLoader;

import java.util.ArrayList;

/**
 * Created by kjh08490 on 2015/2/15.
 */
public class PlayAdapter extends BaseAdapter {
	private LayoutInflater mLayoutInflater;
	private ArrayList<MatchEntityObj> matchTabEntity;

	public PlayAdapter(LayoutInflater mLayoutInflater, ArrayList<MatchEntityObj> matchTabEntity) {
		this.mLayoutInflater = mLayoutInflater;
		this.matchTabEntity = matchTabEntity;
	}

	@Override
	public int getCount() {
		return matchTabEntity.size();
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
			convertView = mLayoutInflater.inflate(R.layout.play_item_layout, null);
			viewHolder.tv_play_activity_pic = (TextView) convertView.findViewById(R.id.tv_play_activity_pic);
			viewHolder.tv_paly_name = (TextView) convertView.findViewById(R.id.tv_paly_name);
			viewHolder.tv_play_num = (TextView) convertView.findViewById(R.id.tv_play_num);
			viewHolder.tv_play_price = (TextView) convertView.findViewById(R.id.tv_play_price);
			viewHolder.tv_time_on = (TextView) convertView.findViewById(R.id.tv_time_on);
			viewHolder.tv_place_big = (TextView) convertView.findViewById(R.id.tv_place_big);
			viewHolder.tv_distance = (TextView) convertView.findViewById(R.id.tv_distance);
			viewHolder.ll_bottom = (LinearLayout) convertView.findViewById(R.id.ll_bottom);
			viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		MatchEntityObj mMatchEntityObj = matchTabEntity.get(position);
		if (mMatchEntityObj != null) {
			// 名称
			String matchName = !TextUtils.isEmpty(mMatchEntityObj.matchName) ? mMatchEntityObj.matchName : "";
			viewHolder.tv_paly_name.setText(matchName);

			// 图片
			String matchLogo = !TextUtils.isEmpty(mMatchEntityObj.matchLogo) ? mMatchEntityObj.matchLogo : "";
			ImageLoader.getInstance().displayImage(matchLogo, viewHolder.imageView);

			//  时间
			String beginDate = !TextUtils.isEmpty(mMatchEntityObj.beginDate) ? mMatchEntityObj.beginDate : "";
			String endDate = !TextUtils.isEmpty(mMatchEntityObj.endDate) ? mMatchEntityObj.endDate : "";
			viewHolder.tv_time_on.setText(beginDate + "--" + endDate);

			// 大区域
			String venueName = !TextUtils.isEmpty(mMatchEntityObj.venueName) ? mMatchEntityObj.venueName : "";
			viewHolder.tv_place_big.setText(venueName);

			//小区域
			String matchAdress = !TextUtils.isEmpty(mMatchEntityObj.matchAdress) ? mMatchEntityObj.matchAdress : "";
			viewHolder.tv_distance.setText(matchAdress);

			// 价格
			String matchFee = !TextUtils.isEmpty(mMatchEntityObj.matchFee) ? mMatchEntityObj.matchFee : "";
			viewHolder.tv_play_price.setText(matchFee);
		}
		return convertView;
	}

	class ViewHolder {
		TextView tv_play_activity_pic;        //周期活动
		ImageView imageView;        // 图片
		TextView tv_paly_name;        // 活动名称
		TextView tv_play_num;        // 活动报名的人数
		TextView tv_play_price;    // 价格
		TextView tv_time_on;        // 时间
		TextView tv_place_big;    // 大区域
		TextView tv_distance;        // 小区域
		LinearLayout ll_bottom;// 底部报名的按钮
	}
}


