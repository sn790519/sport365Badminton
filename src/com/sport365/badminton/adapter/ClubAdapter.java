package com.sport365.badminton.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.sport365.badminton.R;
import com.sport365.badminton.entity.obj.ClubTabEntityObj;
import com.sport365.badminton.http.base.ImageLoader;

import java.util.ArrayList;

/**
 * Created by kjh08490 on 2015/2/15.
 */
public class ClubAdapter extends BaseAdapter {
	private LayoutInflater mLayoutInflater;
	public ArrayList<ClubTabEntityObj> clubTabEntity = new ArrayList<ClubTabEntityObj>();

	public ClubAdapter(LayoutInflater mLayoutInflater, ArrayList<ClubTabEntityObj> clubTabEntity) {
		this.mLayoutInflater = mLayoutInflater;
		this.clubTabEntity = clubTabEntity;
	}

	@Override
	public int getCount() {
		return clubTabEntity.size();
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
			convertView = mLayoutInflater.inflate(R.layout.club_item_layout, null);
			viewHolder.tv_venue = (TextView) convertView.findViewById(R.id.tv_venue);
			viewHolder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
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
		ClubTabEntityObj mClubTabEntityObj = clubTabEntity.get(position);
		if (mClubTabEntityObj != null) {
			// 场馆
			String clubName = !TextUtils.isEmpty(mClubTabEntityObj.clubName) ? mClubTabEntityObj.clubName : "";
			viewHolder.tv_venue.setText(clubName);
			// 价格
			String privce = "20元";
			viewHolder.tv_price.setText(privce);
			// 图片
			String clubLogo = !TextUtils.isEmpty(mClubTabEntityObj.clubLogo) ? mClubTabEntityObj.clubLogo : "";
			ImageLoader.getInstance().displayImage(clubLogo, viewHolder.imageView);
			// 时间
			viewHolder.tv_time.setText("时间");
			// 地址
			String provinceName = !TextUtils.isEmpty(mClubTabEntityObj.provinceName) ? mClubTabEntityObj.provinceName : "";
			String cityName = !TextUtils.isEmpty(mClubTabEntityObj.cityName) ? mClubTabEntityObj.cityName : "";
			String countyName = !TextUtils.isEmpty(mClubTabEntityObj.countyName) ? mClubTabEntityObj.countyName : "";
			viewHolder.tv_distance.setText(provinceName + "  " + cityName + "  " + countyName + "\n" + "缺少");
			// 俱乐部
			String activeNum = !TextUtils.isEmpty(mClubTabEntityObj.activeNum) ? mClubTabEntityObj.activeNum : "";
			viewHolder.tv_club.setText("俱乐部（" + activeNum + "）");
			// 活动
			viewHolder.tv_activity.setText("活动（" + activeNum + "）");
			String matchNum = !TextUtils.isEmpty(mClubTabEntityObj.matchNum) ? mClubTabEntityObj.matchNum : "";
			// 比赛
			viewHolder.tv_game.setText("比赛（" + matchNum + "）");
		}
		return convertView;
	}

	class ViewHolder {
		TextView tv_venue; // 场馆
		TextView tv_price; // 价格
		ImageView imageView; // 图片
		TextView tv_time; // 时间
		TextView tv_phone; // 电话
		TextView tv_distance; // 地址
		TextView tv_club; // 俱乐部
		TextView tv_activity; // 活动
		TextView tv_game; // 比赛
	}
}
