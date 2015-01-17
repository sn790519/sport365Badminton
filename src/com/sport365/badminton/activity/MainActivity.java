package com.sport365.badminton.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
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
import com.sport365.badminton.utils.Utilities;

/**
 * 首页的4个fragment
 * 
 */
public class MainActivity extends BaseActivity implements OnCheckedChangeListener {

	/** 再按一次退出应用 */
	private long			exitTime	= 0;

	private RadioGroup		rg_menu;
	private RadioButton		rb_menu_mian;
	private RadioButton		rb_menu_pay;
	private RadioButton		rb_menu_ball_friend;
	private RadioButton		rb_menu_my;

	/** 当前显示的fragment */
	private BaseFragment	mCurrentFragment;
	/** 首页fragment */
	private BaseFragment	mHomeFragment;
	/** 充值fragment */
	private BaseFragment	mPayFragment;
	/** 惠球友fragment */
	private BaseFragment	mBallfriendFragment;
	/** 我的fragment */
	private BaseFragment	mMyFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mActionbar_left.setImageResource(R.drawable.icon_title365_logo);
		mActionbar_title.setVisibility(View.GONE);
		findViews();
		rb_menu_mian.setChecked(true);
		ULog.debug("--->onCreate");
	}

	private void findViews() {
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
			mCurrentFragment.onPause();
			fragmentTransaction.hide(mCurrentFragment);
		}

		switch (checkedId) {
		case R.id.rb_menu_mian:
			if (mHomeFragment != null) {
				fragmentTransaction.show(mHomeFragment);
			}
			else {
				mHomeFragment = new HomePageFragment();
				fragmentTransaction.add(R.id.ll_fragment_container, mHomeFragment);
			}
			mCurrentFragment = mHomeFragment;
			break;
		case R.id.rb_menu_pay:
			if (mPayFragment != null) {
				fragmentTransaction.show(mPayFragment);
			}
			else {
				mPayFragment = new HomePayFragment();
				fragmentTransaction.add(R.id.ll_fragment_container, mPayFragment);
			}
			mCurrentFragment = mPayFragment;
			break;
		case R.id.rb_menu_ball_friend:
			if (mBallfriendFragment != null) {
				fragmentTransaction.show(mBallfriendFragment);
			}
			else {
				mBallfriendFragment = new HomeBallFriendFragment();
				fragmentTransaction.add(R.id.ll_fragment_container, mBallfriendFragment);
			}
			mCurrentFragment = mBallfriendFragment;
			break;
		case R.id.rb_menu_my:
			if (mMyFragment != null) {
				fragmentTransaction.show(mMyFragment);
			}
			else {
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
			if (rg_menu.getCheckedRadioButtonId() != R.id.rb_menu_mian) {
				rg_menu.check(R.id.rb_menu_mian);
			}
			else {
				if ((System.currentTimeMillis() - exitTime) > 2000) {
					Utilities.showToast(mContext.getResources().getString(R.string.press_more_exit), this);
					exitTime = System.currentTimeMillis();
				}
				else {
					MainActivity.this.finish();
				}
				return true;
			}
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

}
