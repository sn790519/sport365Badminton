package com.sport365.badminton.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.sport365.badminton.activity.view.ActivityCenterView;
import com.sport365.badminton.entity.obj.VenueEntityObj;

import java.util.ArrayList;

/**
 * Created by kjh08490 on 2015/2/15.
 */
public class ActivityCenterAdapter extends BaseAdapter {
	private ArrayList<VenueEntityObj> venueEntity = new ArrayList<VenueEntityObj>();
	private Context mContext;

	public ActivityCenterAdapter(Context mContext, ArrayList<VenueEntityObj> venueEntity) {
		this.venueEntity = venueEntity;
		this.mContext = mContext;
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
		VenueEntityObj venueEntityobj = venueEntity.get(position);
		if (convertView == null) {
			ActivityCenterView view = new ActivityCenterView(mContext);
			view.setDateView(venueEntityobj);
			convertView = view;
			convertView.setTag(view);
		} else {
			convertView = (ActivityCenterView) convertView.getTag();
			((ActivityCenterView)convertView).setDateView(venueEntityobj);
		}
		return convertView;
	}

}
