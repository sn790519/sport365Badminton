package com.sport365.badminton.activity.view;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.sport365.badminton.R;
import com.sport365.badminton.view.MyRadioButton;

/**
 * RadioGroupView控件
 */
public class SportRadioGroupView extends LinearLayout implements RadioGroup.OnCheckedChangeListener {
	public RadioGroup rg_menu;
	public RadioButton rb_menu_first;
	public RadioButton rb_menu_second;
	public RadioButton rb_menu_third;
	public RadioButton rb_menu_four;

	private int[] drawableIds;// 图片的资源ids的列表
	private String[] strStr;// 文案描述的列表

	private SportCheckListen sportCheckListen;

	/**
	 * 构造函数
	 *
	 * @param context
	 * @param drawableIds
	 * @param strStr
	 */
	public SportRadioGroupView(Context context, int[] drawableIds, String[] strStr) {
		super(context);
		inflate(context, R.layout.choose_item_layout, this);
		rg_menu = (RadioGroup) findViewById(R.id.rg_menu);
		rb_menu_first = (RadioButton) findViewById(R.id.rb_menu_first);
		rb_menu_second = (RadioButton) findViewById(R.id.rb_menu_second);
		rb_menu_third = (RadioButton) findViewById(R.id.rb_menu_third);
		rb_menu_four = (RadioButton) findViewById(R.id.rb_menu_four);
		// 根据drawablesIds进行适配
		rg_menu.setOnCheckedChangeListener(this);
	}

	/**
	 * 设置单选的监听
	 *
	 * @param sportCheckListen
	 */
	public SportRadioGroupView setSportCheckListen(SportCheckListen sportCheckListen) {
		this.sportCheckListen = sportCheckListen;
		return this;
	}


	@Override
	public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
		switch (checkedId) {
			case R.id.rb_menu_first:
				if (sportCheckListen != null) {
					sportCheckListen.FirstOnClick();
				}
				break;
			case R.id.rb_menu_second:
				if (sportCheckListen != null) {
					sportCheckListen.SecondOnClick();
				}
				break;
			case R.id.rb_menu_third:
				if (sportCheckListen != null) {
					sportCheckListen.ThirdOnClick();
				}
				break;
			case R.id.rb_menu_four:
				if (sportCheckListen != null) {
					sportCheckListen.FourOnClick();
				}
				break;

		}
	}

	/**
	 * 点击切换的单选按钮的事件操作
	 */
	public interface SportCheckListen {
		public void FirstOnClick();

		public void SecondOnClick();

		public void ThirdOnClick();

		public void FourOnClick();
	}
}
