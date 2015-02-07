package com.sport365.badminton.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import com.sport365.badminton.BaseActivity;
import com.sport365.badminton.BaseFragment;
import com.sport365.badminton.R;
import com.sport365.badminton.activity.fragment.FindPwdByEmailFragment;
import com.sport365.badminton.activity.fragment.ModifyPhoneFragment;
import com.sport365.badminton.view.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;


/**
 * 修改手机号码
 * Created by vincent on 15/2/1.
 */
public class ModifyPhoneActivity extends BaseActivity implements View.OnClickListener {

	private ViewPager mViewPager;
	private TabPageIndicator tab_indicator;
	private FragmentPagerAdapter mAdapter;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_findpwd);
		initActionBar();
		findViews();
	}

	private void initActionBar() {
		setActionBarTitle("修改手机号码");
		mActionbar_right.setVisibility(View.GONE);
	}

	private void findViews() {
		tab_indicator = (TabPageIndicator) findViewById(R.id.tab_indicator);
		mViewPager = (ViewPager) findViewById(R.id.viewPager);

		List<BaseFragment> list_fragments=new ArrayList<BaseFragment>();
		ModifyPhoneFragment fragment1=new ModifyPhoneFragment();
		FindPwdByEmailFragment fragment=new FindPwdByEmailFragment();
		list_fragments.add(fragment1);
		list_fragments.add(fragment);
		mAdapter = new TabAdapter(getSupportFragmentManager(),list_fragments);
		mViewPager.setAdapter(mAdapter);
		tab_indicator.setViewPager(mViewPager, 0);
	}


	public class TabAdapter extends FragmentPagerAdapter {
		public final String[] TITLES = new String[]{"1.安全验证", "2.验证手机号"};
		private List<BaseFragment> list_fragments;

		public TabAdapter(FragmentManager fm, List<BaseFragment> list_fragments) {
			super(fm);
			this.list_fragments = list_fragments;
		}

		@Override
		public Fragment getItem(int arg0) {
			return list_fragments.get(arg0);
		}


		@Override
		public CharSequence getPageTitle(int position) {
			return TITLES[position % TITLES.length];
		}


		@Override
		public int getCount() {
			return TITLES.length;
		}


	}

}