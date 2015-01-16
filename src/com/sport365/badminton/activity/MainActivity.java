package com.sport365.badminton.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.sport365.badminton.BaseActivity;
import com.sport365.badminton.BaseFragment;
import com.sport365.badminton.R;
import com.sport365.badminton.activity.fragment.HomeBallFriendFragment;
import com.sport365.badminton.activity.fragment.HomeMyFragment;
import com.sport365.badminton.activity.fragment.HomePageFragment;
import com.sport365.badminton.activity.fragment.HomePayFragment;

/**
 * 首页的4个fragment
 * 
 */
public class MainActivity extends BaseActivity {
	private String TAG = MainActivity.class.getSimpleName();

	private RadioGroup rg_menu;
	private RadioButton rb_menu_mian;
	private RadioButton rb_menu_pay;
	private RadioButton rb_menu_ball_friend;
	private RadioButton rb_menu_my;

	private ArrayList<BaseFragment> fragments = new ArrayList<BaseFragment>();// 存放fragment
	private int currentTab = 0; // 当前Tab页面索引

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setHomeBar("");
		setActionBarRightMenu(R.drawable.line_tad_more, new RightClickListen() {

			@Override
			public void onRightMenuClick() {
				Toast.makeText(MainActivity.this, "Right Click Test", Toast.LENGTH_LONG).show();
			}
		});
		initMainView();
		initFragments();
		rb_menu_mian.setChecked(true);

	}

	private void initMainView() {
		rg_menu = (RadioGroup) findViewById(R.id.rg_menu);
		rb_menu_mian = (RadioButton) findViewById(R.id.rb_menu_mian);
		rb_menu_pay = (RadioButton) findViewById(R.id.rb_menu_pay);
		rb_menu_ball_friend = (RadioButton) findViewById(R.id.rb_menu_ball_friend);
		rb_menu_my = (RadioButton) findViewById(R.id.rb_menu_my);
		rg_menu.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_menu_mian:
					changeFragment(0);
					break;
				case R.id.rb_menu_pay:
					changeFragment(1);
					break;
				case R.id.rb_menu_ball_friend:
					changeFragment(2);
					break;
				case R.id.rb_menu_my:
					changeFragment(3);
					break;
				}
			}
		});
	}

	private void initFragments() {
		HomePageFragment homePageFragment = new HomePageFragment();
		Bundle home = new Bundle();
		homePageFragment.setArguments(home);
		fragments.add(homePageFragment);

		HomePayFragment homePayFragment = new HomePayFragment();
		Bundle pay = new Bundle();
		homePayFragment.setArguments(pay);
		fragments.add(homePayFragment);

		HomeBallFriendFragment homeBallFriendFragment = new HomeBallFriendFragment();
		Bundle ballfriend = new Bundle();
		homeBallFriendFragment.setArguments(ballfriend);
		fragments.add(homeBallFriendFragment);

		HomeMyFragment homeMyFragment = new HomeMyFragment();
		Bundle my = new Bundle();
		homeMyFragment.setArguments(my);
		fragments.add(homeMyFragment);
	}

	private void changeFragment(int index) {

		BaseFragment fragment = fragments.get(index);
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		getCurrentFragment().onPause(); // 暂停当前tab

		if (fragment.isAdded()) {
			// fragment.onStart(); // 启动目标tab的onStart()
			fragment.onResume(); // 启动目标tab的onResume()
		} else {
			ft.add(R.id.ll_fragment_container, fragment);
		}
		showTab(index); // 显示目标tab
		// transaction.addToBackStack(null);
		ft.commit();

	}

	public BaseFragment getCurrentFragment() {
		return fragments.get(currentTab);
	}

	private void showTab(int idx) {
		for (int i = 0; i < fragments.size(); i++) {
			BaseFragment fragment = fragments.get(i);
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			if (idx == i) {
				ft.show(fragment);
			} else {
				ft.hide(fragment);
			}
			ft.commit();
		}
		currentTab = idx; // 更新目标tab为当前tab
	}

	/**
	 * 异常情况保存数据
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

	}

	/**
	 * 获取异常情况保存的数据
	 */
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

}
