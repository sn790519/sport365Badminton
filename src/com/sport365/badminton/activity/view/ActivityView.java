package com.sport365.badminton.activity.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sport365.badminton.R;
import com.sport365.badminton.entity.obj.ActiveEntityObj;
import com.sport365.badminton.http.base.ImageLoader;

/**
 * 活动的view Created by kjh08490 on 2015/3/7.
 */
public class ActivityView extends RelativeLayout implements OnClickListener {
	private RelativeLayout rl_layout;// 背景

	private TextView tv_venue; // 场馆
	private TextView tv_price; // 价格
	private ImageView imageView; // 图片
	private ImageView iv_hui; // 是否有惠的标志
	private ImageView iv_tag_top; // 置顶图片
	private ImageView iv_activity_flag; // 进行中
	private TextView tv_time; // 时间
	private TextView tv_venueName; // 电话
	private TextView tv_distance; // 地址
	private TextView tv_sign_alredy; // 俱乐部
	private TextView tv_activity_sign; // 活动
	private TextView tv_hui_tips; // 惠的文案

	private LinearLayout ll_bottom;
	/**
	 * 设置底部按钮的监听事件
	 */
	private ActivityListen mActivityListen;

	public ActivityView(Context context) {
		super(context);
		inflate(context, R.layout.activity_item_layout, this);
		tv_venue = (TextView) findViewById(R.id.tv_venue);
		tv_price = (TextView) findViewById(R.id.tv_price);
		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_venueName = (TextView) findViewById(R.id.tv_phone);
		tv_distance = (TextView) findViewById(R.id.tv_distance);
		tv_distance.setOnClickListener(this);
		tv_sign_alredy = (TextView) findViewById(R.id.tv_sign_alredy);
		// 设置已经报名的点击事件
		tv_sign_alredy.setOnClickListener(this);
		tv_activity_sign = (TextView) findViewById(R.id.tv_activity_sign);
		tv_hui_tips = (TextView) findViewById(R.id.tv_hui_tips);
		tv_activity_sign.setOnClickListener(this);
		imageView = (ImageView) findViewById(R.id.imageView);
		iv_hui = (ImageView) findViewById(R.id.iv_hui);
		iv_activity_flag = (ImageView) findViewById(R.id.iv_activity_flag);
		iv_tag_top = (ImageView) findViewById(R.id.iv_tag_top);
		ll_bottom = (LinearLayout) findViewById(R.id.ll_bottom);
		rl_layout = (RelativeLayout) findViewById(R.id.rl_layout);
	}

	public RelativeLayout setDateView(final ActiveEntityObj mActiveEntityObj) {
		if (mActiveEntityObj != null) {
			// 活动名称
			String clubName = !TextUtils.isEmpty(mActiveEntityObj.activeTitle) ? mActiveEntityObj.activeTitle : "";
			tv_venue.setText(clubName);
			// 价格
			String activeFee = !TextUtils.isEmpty(mActiveEntityObj.activeFee) ? mActiveEntityObj.activeFee : "";
			tv_price.setText(activeFee + "元");
			// 图片
			String activeLogo = !TextUtils.isEmpty(mActiveEntityObj.activeLogo) ? mActiveEntityObj.activeLogo : "";
			ImageLoader.getInstance().displayImage(activeLogo, imageView);
			// 惠
			tv_hui_tips.setVisibility(View.GONE);
			String huiTips = !TextUtils.isEmpty(mActiveEntityObj.huiTips) ? mActiveEntityObj.huiTips : "";
			tv_hui_tips.setText(huiTips);
			if ("1".equals(mActiveEntityObj.isHui)) {
				iv_hui.setVisibility(View.VISIBLE);
				iv_hui.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						tv_hui_tips.setVisibility(tv_hui_tips.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
					}
				});
			} else {
				iv_hui.setVisibility(View.GONE);
			}
			// 时间
			String activeDate = !TextUtils.isEmpty(mActiveEntityObj.activeDate) ? mActiveEntityObj.activeDate : "";
			String activeHours = !TextUtils.isEmpty(mActiveEntityObj.activeHours) ? mActiveEntityObj.activeHours : "";
			tv_time.setText(activeDate + "  (" + activeHours + "小时)");
			// 会所名称
			tv_venueName.setText(!TextUtils.isEmpty(mActiveEntityObj.venueName) ? mActiveEntityObj.venueName : "待定,联系客服");
			// 地址
			String provinceName = !TextUtils.isEmpty(mActiveEntityObj.provinceName) ? mActiveEntityObj.provinceName : "";
			String cityName = !TextUtils.isEmpty(mActiveEntityObj.cityName) ? mActiveEntityObj.cityName : "";
			String countyName = !TextUtils.isEmpty(mActiveEntityObj.countyName) ? mActiveEntityObj.countyName : "";
			String venueName = !TextUtils.isEmpty(mActiveEntityObj.venueName) ? mActiveEntityObj.venueName : "";
			tv_distance.setText(provinceName + "  " + cityName + "  " + countyName + "  " + "\n" + venueName);
			// 俱乐部
			String realNum = !TextUtils.isEmpty(mActiveEntityObj.realNum) ? mActiveEntityObj.realNum : "";
			tv_sign_alredy.setText(realNum + "人已报名");
			// 活动
			tv_activity_sign.setText("活动报名");

			// 水印置顶1：置顶isTop
			if ("1".equals(mActiveEntityObj.isTop)) {
				iv_tag_top.setImageResource(R.drawable.indicator_common_set_up);
			}
			// 水印是否推荐1：推荐isRecommend
			else if ("1".equals(mActiveEntityObj.isRecommend)) {
				iv_tag_top.setImageResource(R.drawable.indicator_common_recommended);
			} else {
				iv_tag_top.setImageBitmap(null);
			}
		}
		return this;
	}

	/**
	 * 隐藏item的底部框
	 *
	 * @param FlagVisible
	 * @return
	 */
	public RelativeLayout setBottonVisible(int FlagVisible) {
		ll_bottom.setVisibility(FlagVisible);
		return this;
	}

	/**
	 * 设置活动底部按钮的监听事件
	 *
	 * @param activityListe
	 */
	public void setActivityListen(ActivityListen activityListe) {
		this.mActivityListen = activityListe;
	}

	/**
	 * 设置活动的监听事件
	 *
	 * @author Frank
	 */
	public interface ActivityListen {
		/**
		 * 查看报名人员
		 */
		public void lookBookNames();

		/**
		 * 报名
		 */
		public void doBook();

		/**
		 * 地图查看位置
		 */
		public void goMapShow();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.tv_sign_alredy:
				if (mActivityListen != null) {
					mActivityListen.lookBookNames();
				}
				break;
			case R.id.tv_activity_sign:
				if (mActivityListen != null) {
					mActivityListen.doBook();
				}
				break;
			case R.id.tv_distance:
				if (mActivityListen != null) {
					mActivityListen.goMapShow();
				}
				break;
		}
	}

	/**
	 * 设置背景为白色
	 *
	 * @return
	 */
	public RelativeLayout setBackgroundWhiteColor() {
		if (rl_layout != null) {
			rl_layout.setBackgroundColor(getResources().getColor(R.color.white));
		}
		return this;
	}
}
