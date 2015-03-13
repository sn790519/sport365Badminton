package com.sport365.badminton.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.sport365.badminton.activity.view.ClubView;
import com.sport365.badminton.entity.obj.ClubTabEntityObj;

import java.util.ArrayList;

/**
 * Created by kjh08490 on 2015/2/15.
 */
public class ClubAdapter extends BaseAdapter {
	public ArrayList<ClubTabEntityObj> clubTabEntity = new ArrayList<ClubTabEntityObj>();
	private Context mContext;

	public ClubAdapter(Context mContext, ArrayList<ClubTabEntityObj> clubTabEntity) {
		this.clubTabEntity = clubTabEntity;
		this.mContext = mContext;
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
		ClubTabEntityObj mClubTabEntityObj = clubTabEntity.get(position);
		if (convertView == null) {
			ClubView view = new ClubView(mContext);
			view.setDateView(mClubTabEntityObj);
			convertView = view;
			convertView.setTag(view);
		} else {
			convertView = (ClubView) convertView.getTag();
			((ClubView)convertView).setDateView(mClubTabEntityObj);
		}
		return convertView;
	}

}
