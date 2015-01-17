package com.sport365.badminton.activity.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sport365.badminton.BaseFragment;
import com.sport365.badminton.R;
import com.sport365.badminton.view.DialogFactory;

public class HomePayFragment extends BaseFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_pay_layout, container, false);
		Button btn_btn = (Button) view.findViewById(R.id.btn_btn);

		btn_btn.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				new DialogFactory(getActivity()).showDialog3Btn("提示", "这是内容", "左边", "中间", "右边", null, true);
			}
		});

		return view;
	}
}
