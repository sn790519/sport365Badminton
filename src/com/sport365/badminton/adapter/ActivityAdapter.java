package com.sport365.badminton.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.sport365.badminton.activity.view.ActivityView;
import com.sport365.badminton.entity.obj.ActiveEntityObj;

import java.util.ArrayList;

/**
 * Created by kjh08490 on 2015/2/15.
 */
public class ActivityAdapter extends BaseAdapter {
	public ArrayList<ActiveEntityObj> alctiveList = new ArrayList<ActiveEntityObj>();// 列表

	private Context mContext;

	public ActivityAdapter(Context mContext, ArrayList<ActiveEntityObj> alctiveList) {
		this.alctiveList = alctiveList;
		this.mContext = mContext;
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
		ActiveEntityObj mActiveEntityObj = alctiveList.get(position);
		if (convertView == null) {
			ActivityView view = new ActivityView(mContext);
			view.setDateView(mActiveEntityObj);
			convertView = view;
			convertView.setTag(view);
		} else {
			convertView = (ActivityView) convertView.getTag();
		}
		return convertView;
	}

}
