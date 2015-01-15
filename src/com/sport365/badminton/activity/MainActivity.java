package com.sport365.badminton.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.sport365.badminton.BaseActivity;
import com.sport365.badminton.R;
import com.sport365.badminton.activity.fragment.HomeBallFriendFragment;
import com.sport365.badminton.activity.fragment.HomeMyFragment;
import com.sport365.badminton.activity.fragment.HomePageFragment;
import com.sport365.badminton.activity.fragment.HomePayFragment;

public class MainActivity extends BaseActivity {
	private String TAG = MainActivity.class.getSimpleName();

	private RadioGroup rg_menu;
	private RadioButton rb_menu_mian;
	private RadioButton rb_menu_pay;
	private RadioButton rb_menu_ball_friend;
	private RadioButton rb_menu_my;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setHomeBar("");
		setActionBarRightMenu(R.drawable.line_tad_more, new RightClickListen() {

			@Override
			public void onRightMenuClick() {
				Toast.makeText(MainActivity.this, "Right Click Test", Toast.LENGTH_LONG).show();
				initHomePayFragment();
			}
		});
		initMainView();
		rb_menu_mian.setChecked(true);
		initHomePageFragment();
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
					initHomePageFragment();
					break;
				case R.id.rb_menu_pay:
					initHomePayFragment();
					break;
				case R.id.rb_menu_ball_friend:
					initHomeBallFriendFragment();
					break;
				case R.id.rb_menu_my:
					initHomeMyFragment();
					break;
				}
			}
		});
	}

	private void initHomePageFragment() {
		HomePageFragment homePageFragment = new HomePageFragment();
		Bundle args = new Bundle();
		homePageFragment.setArguments(args);
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

		// Replace whatever is in the ll_fragment_container view with this
		// fragment,
		// and add the transaction to the back stack so the user can
		// navigate back
		transaction.replace(R.id.ll_fragment_container, homePageFragment);
		// transaction.addToBackStack(null);
		// Commit the transaction
		transaction.commit();
	}

	/**
	 * 切换到HomePayfragment
	 */
	public void initHomePayFragment() {
		HomePayFragment homePayFragment = new HomePayFragment();
		Bundle args = new Bundle();
		homePayFragment.setArguments(args);
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

		// Replace whatever is in the ll_fragment_container view with this
		// fragment,
		// and add the transaction to the back stack so the user can
		// navigate back
		transaction.replace(R.id.ll_fragment_container, homePayFragment);
		// transaction.addToBackStack(null);
		// Commit the transaction
		transaction.commit();
	}

	public void initHomeBallFriendFragment() {
		HomeBallFriendFragment homeBallFriendFragment = new HomeBallFriendFragment();
		Bundle args = new Bundle();
		homeBallFriendFragment.setArguments(args);
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

		// Replace whatever is in the ll_fragment_container view with this
		// fragment,
		// and add the transaction to the back stack so the user can
		// navigate back
		transaction.replace(R.id.ll_fragment_container, homeBallFriendFragment);
		// transaction.addToBackStack(null);
		// Commit the transaction
		transaction.commit();
	}

	public void initHomeMyFragment() {
		HomeMyFragment homeMyFragment = new HomeMyFragment();
		Bundle args = new Bundle();
		homeMyFragment.setArguments(args);
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

		// Replace whatever is in the ll_fragment_container view with this
		// fragment,
		// and add the transaction to the back stack so the user can
		// navigate back
		transaction.replace(R.id.ll_fragment_container, homeMyFragment);
		// transaction.addToBackStack(null);
		// Commit the transaction
		transaction.commit();
	}

}
