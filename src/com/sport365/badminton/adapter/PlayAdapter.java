package com.sport365.badminton.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.sport365.badminton.activity.view.PlayView;
import com.sport365.badminton.entity.obj.MatchEntityObj;

import java.util.ArrayList;

/**
 * Created by kjh08490 on 2015/2/15.
 */
public class PlayAdapter extends BaseAdapter {
	private ArrayList<MatchEntityObj> matchTabEntity;
	private Context mContext;

	public PlayAdapter(Context mContext, ArrayList<MatchEntityObj> matchTabEntity) {
		this.matchTabEntity = matchTabEntity;
		this.mContext = mContext;
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
		MatchEntityObj mMatchEntityObj = matchTabEntity.get(position);
		if (convertView == null) {
			PlayView view = new PlayView(mContext);
			view.setDateView(mMatchEntityObj);
			convertView = view;
			convertView.setTag(view);
		} else {
			convertView = (PlayView) convertView.getTag();
			((PlayView)convertView).setDateView(mMatchEntityObj);
		}
		return convertView;
	}

}


