package com.sport365.badminton.activity.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.sport365.badminton.BaseFragment;
import com.sport365.badminton.R;
import com.sport365.badminton.activity.MainActivity;

public class HomeMyFragment extends BaseFragment implements View.OnClickListener {

	private RelativeLayout rl_pay;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.home_my_layout, container, false);
		findViews(view);
		return view;
	}

	private void findViews(View view) {
		rl_pay = (RelativeLayout) view.findViewById(R.id.rl_pay);
		rl_pay.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
			case R.id.rl_pay:
				((MainActivity) getActivity()).rb_menu_pay.setChecked(true);
				break;
		}
	}
}
