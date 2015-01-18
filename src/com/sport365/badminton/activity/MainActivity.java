package com.sport365.badminton.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.sport365.badminton.BaseActivity;
import com.sport365.badminton.BaseFragment;
import com.sport365.badminton.R;
import com.sport365.badminton.activity.fragment.HomeBallFriendFragment;
import com.sport365.badminton.activity.fragment.HomeMyFragment;
import com.sport365.badminton.activity.fragment.HomePageFragment;
import com.sport365.badminton.activity.fragment.HomePayFragment;
import com.sport365.badminton.utils.ULog;

/**
 * 首页的4个fragment
 * 
 */
public class MainActivity extends BaseActivity implements OnCheckedChangeListener {

	private RadioGroup rg_menu;
	private RadioButton rb_menu_mian;
	private RadioButton rb_menu_pay;
	private RadioButton rb_menu_ball_friend;
	private RadioButton rb_menu_my;

	private ArrayList<BaseFragment> fragments = new ArrayList<BaseFragment>(); // 存放fragment
	private int currentTab = 0; // 当前Tab页面索引

	/** 当前显示的fragment */
	private BaseFragment mCurrentFragment;
	/** 首页fragment */
	private BaseFragment mHomeFragment;
	/** 充值fragment */
	private BaseFragment mPayFragment;
	/** 惠球友fragment */
	private BaseFragment mBallfriendFragment;
	/** 我的fragment */
	private BaseFragment mMyFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mActionbar_left.setImageResource(R.drawable.icon_title365_logo);
		mActionbar_title.setVisibility(View.GONE);
		initMainView();
		rb_menu_mian.setChecked(true);
		setRightClick();
		ULog.debug("--->onCreate");
	}
	
	private void setRightClick()
	{
		mActionbar_right.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,ActivityCenterAtivity.class);
				startActivity(intent);
			}
		});
	}

	private void initMainView() {
		rg_menu = (RadioGroup) findViewById(R.id.rg_menu);
		rb_menu_mian = (RadioButton) findViewById(R.id.rb_menu_mian);
		rb_menu_pay = (RadioButton) findViewById(R.id.rb_menu_pay);
		rb_menu_ball_friend = (RadioButton) findViewById(R.id.rb_menu_ball_friend);
		rb_menu_my = (RadioButton) findViewById(R.id.rb_menu_my);
		rg_menu.setOnCheckedChangeListener(this);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		if (mCurrentFragment != null) {
			fragmentTransaction.hide(mCurrentFragment);
		}

		switch (checkedId) {
		case R.id.rb_menu_mian:
			if (mHomeFragment != null) {
				fragmentTransaction.show(mHomeFragment);
			} else {
				mHomeFragment = new HomePageFragment();
				fragmentTransaction.add(R.id.ll_fragment_container, mHomeFragment);
			}
			mCurrentFragment = mHomeFragment;
			break;
		case R.id.rb_menu_pay:
			if (mPayFragment == null) {
				fragmentTransaction.show(mPayFragment);
			} else {
				mPayFragment = new HomePayFragment();
				fragmentTransaction.add(R.id.ll_fragment_container, mPayFragment);
			}
			mCurrentFragment = mPayFragment;
			break;
		case R.id.rb_menu_ball_friend:
			if (mBallfriendFragment != null) {
				fragmentTransaction.show(mBallfriendFragment);
			} else {
				mBallfriendFragment = new HomeBallFriendFragment();
				fragmentTransaction.add(R.id.ll_fragment_container, mBallfriendFragment);
			}
			mCurrentFragment = mBallfriendFragment;
			break;
		case R.id.rb_menu_my:
			if (mMyFragment != null) {
				fragmentTransaction.show(mMyFragment);
			} else {
				mMyFragment = new HomeMyFragment();
				fragmentTransaction.add(R.id.ll_fragment_container, mMyFragment);
			}
			mCurrentFragment = mMyFragment;
			break;
		default:
			break;
		}

		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commitAllowingStateLoss();
		ULog.debug("---->onCheckedChanged()");
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:

			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

}
