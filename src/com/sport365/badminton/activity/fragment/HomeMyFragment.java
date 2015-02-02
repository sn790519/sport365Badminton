package com.sport365.badminton.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.sport365.badminton.BaseFragment;
import com.sport365.badminton.R;
import com.sport365.badminton.activity.LoginActivity;
import com.sport365.badminton.activity.MainActivity;
import com.sport365.badminton.activity.MapFragmentDemo;
import com.sport365.badminton.activity.MyAccountActivity;
import com.sport365.badminton.params.SystemConfig;

public class HomeMyFragment extends BaseFragment implements View.OnClickListener {


	/**
	 * 顶部登陆
	 */
	private RelativeLayout rl_top;
	private RelativeLayout rl_user_account;
	/**
	 * 在线充值
	 */
	private RelativeLayout rl_pay;
	/**
	 * 充值记录
	 */
	private RelativeLayout rl_pay_history;
	private TextView tv_no_login;
	private TextView tv_user_name;
	private TextView tv_user_score;
	private Button btn_logout;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.home_my_layout, container, false);
		findViews(view);
		return view;
	}

	private void findViews(View view) {
		rl_pay = (RelativeLayout) view.findViewById(R.id.rl_pay);
		rl_pay.setOnClickListener(this);
		rl_pay_history = (RelativeLayout) view.findViewById(R.id.rl_pay_history);
		rl_pay_history.setOnClickListener(this);
		rl_top = (RelativeLayout) view.findViewById(R.id.rl_top);
		rl_top.setOnClickListener(this);
		rl_user_account = (RelativeLayout) view.findViewById(R.id.rl_user_account);
		tv_no_login = (TextView) view.findViewById(R.id.tv_no_login);
		tv_user_name = (TextView) view.findViewById(R.id.tv_user_name);
		tv_user_score = (TextView) view.findViewById(R.id.tv_user_score);
		if (SystemConfig.isLogin()) {
			tv_no_login.setVisibility(View.GONE);
			rl_user_account.setVisibility(View.VISIBLE);
		} else {
			tv_no_login.setVisibility(View.VISIBLE);
			rl_user_account.setVisibility(View.GONE);
		}


	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
			case R.id.rl_pay:
				((MainActivity) getActivity()).rb_menu_pay.setChecked(true);
				break;
			case R.id.rl_pay_history:
				startActivity(new Intent(getActivity(), MapFragmentDemo.class));
				break;
			case R.id.rl_top:
				if (SystemConfig.isLogin()) {
					startActivity(new Intent(getActivity(), MyAccountActivity.class));
				} else {
					startActivity(new Intent(getActivity(), LoginActivity.class));
				}
				break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (null != SystemConfig.loginResBody && SystemConfig.isLogin()) {
			tv_no_login.setVisibility(View.GONE);
			rl_user_account.setVisibility(View.VISIBLE);
			tv_user_name.setText(SystemConfig.loginResBody.account);
			tv_user_score.setText(SystemConfig.loginResBody.consumeMoney);
		}
	}

}
