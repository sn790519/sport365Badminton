package com.sport365.badminton.activity.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sport365.badminton.BaseFragment;
import com.sport365.badminton.R;

public class HomeMyFragment extends BaseFragment {


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		return inflater.inflate(R.layout.home_my_layout, container, false);
	}

}
