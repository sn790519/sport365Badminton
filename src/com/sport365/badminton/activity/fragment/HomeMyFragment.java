package com.sport365.badminton.activity.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sport365.badminton.BaseFragment;
import com.sport365.badminton.R;

public class HomeMyFragment extends BaseFragment {

	Bundle outState;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if (outState != null) {
			System.out.println("HomeMyFragment+++++++++++++++++++++onCreateView+" + outState.getString("key") + "+++++++++++++++++++++++");
		} else {
			System.out.println("HomeMyFragment----onCreate()");
		}
	}

	/**
	 * onPause保存的数据在onResume()显示调用
	 */
	@Override
	public void onResume() {
		super.onResume();
		if (outState != null) {
			System.out.println("+++++++++++++++++++++onResume()+" + outState.getString("key") + "+++++++++++++++++++++++");
		} else {
			System.out.println("onResume()");
		}
	}

	/**
	 * 用Arraylist的方法进行保存，那么只会走一次oncreateView()方法
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			// 处理onSaveInstanceState异常保存的数据
			System.out.println("HomeMyFragment+++++++++++++++++++++onCreateView+" + savedInstanceState.getBoolean("hasTabs") + "+++++++++++++++++++++++");
		}

		return inflater.inflate(R.layout.home_my_layout, container, false);
	}

	/**
	 * 切换fragment保存数据,并在onResume()方法中调用
	 */
	@Override
	public void onPause() {
		super.onPause();
		System.out.println("HomeMyFragment+++++++++++++++++++++pause ball friend++++++++++++++++++++++");
		outState = new Bundle();
		outState.putString("key", "key");
	}

	/**
	 * 异常情况保存数据
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean("hasTabs", false);
		System.out.println("HomeMyFragment+++++++++++++++++++++onSaveInstanceState ball friend++++++++++++++++++++++");
	}
}
