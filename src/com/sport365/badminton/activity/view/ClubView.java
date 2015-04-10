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
import com.sport365.badminton.entity.obj.ClubTabEntityObj;
import com.sport365.badminton.http.base.ImageLoader;

/**
 * 俱乐部的view Created by kjh08490 on 2015/3/7.
 */
public class ClubView extends RelativeLayout implements OnClickListener {

	private RelativeLayout rl_layout;//

	private TextView tv_venue; // 场馆
	private TextView tv_price; // 价格
	private ImageView imageView; // 图片
	private TextView tv_time; // 时间
	private TextView tv_phone; // 电话
	private TextView tv_distance; // 地址
	private TextView tv_rechange; // 充值
	private TextView tv_activity; // 活动
	private TextView tv_game; // 比赛
	private TextView btn_rechange; // 充值
	private ImageView iv_tag_top;

	private LinearLayout ll_bottom;
	// 设置club的底部的监听
	private ClubListen mClubListen;

	public ClubView(Context context) {
		super(context);
		inflate(context, R.layout.club_item_layout, this);
		tv_venue = (TextView) findViewById(R.id.tv_venue);
		tv_price = (TextView) findViewById(R.id.tv_price);
		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_phone = (TextView) findViewById(R.id.tv_phone);
		tv_distance = (TextView) findViewById(R.id.tv_distance);
		tv_distance.setOnClickListener(this);
		tv_rechange = (TextView) findViewById(R.id.tv_rechange);
		tv_rechange.setOnClickListener(this);
		tv_activity = (TextView) findViewById(R.id.tv_activity);
		tv_activity.setOnClickListener(this);
		tv_game = (TextView) findViewById(R.id.tv_game);
		tv_game.setOnClickListener(this);
		btn_rechange = (TextView) findViewById(R.id.btn_rechange);
		btn_rechange.setOnClickListener(this);
		imageView = (ImageView) findViewById(R.id.imageView);
		ll_bottom = (LinearLayout) findViewById(R.id.ll_bottom);
		rl_layout = (RelativeLayout) findViewById(R.id.rl_layout);
		iv_tag_top = (ImageView) findViewById(R.id.iv_tag_top);
	}

	public RelativeLayout setDateView(ClubTabEntityObj mClubTabEntityObj) {
		if (mClubTabEntityObj != null) {
			// 场馆
			String clubName = !TextUtils.isEmpty(mClubTabEntityObj.clubName) ? mClubTabEntityObj.clubName : "";
			tv_venue.setText(clubName);
			// 均价
			tv_phone.setText("均价：");
			// 价格
			String privce = !TextUtils.isEmpty(mClubTabEntityObj.activeFee) ? mClubTabEntityObj.activeFee : "";
			tv_price.setText(privce + "元/2小时/次");
			// 图片
			String clubLogo = !TextUtils.isEmpty(mClubTabEntityObj.clubLogo) ? mClubTabEntityObj.clubLogo : "";
			ImageLoader.getInstance().displayImage(clubLogo, imageView);
			// 群主
			String nickname = !TextUtils.isEmpty(mClubTabEntityObj.nickname) ? mClubTabEntityObj.nickname : "";
			tv_time.setText("群主：" + nickname);
			// 地址
			String provinceName = !TextUtils.isEmpty(mClubTabEntityObj.provinceName) ? mClubTabEntityObj.provinceName : "";
			String cityName = !TextUtils.isEmpty(mClubTabEntityObj.cityName) ? mClubTabEntityObj.cityName : "";
			String countyName = !TextUtils.isEmpty(mClubTabEntityObj.countyName) ? mClubTabEntityObj.countyName : "";
			tv_distance.setText(provinceName + "  " + cityName + "  " + countyName + "\n" + "缺少");
			// 俱乐部
			tv_rechange.setText("充值会费");
			// 活动
			String activeNum = !TextUtils.isEmpty(mClubTabEntityObj.activeNum) ? mClubTabEntityObj.activeNum : "";
			tv_activity.setText("活动（" + activeNum + "）");
			// 比赛
			String matchNum = !TextUtils.isEmpty(mClubTabEntityObj.matchNum) ? mClubTabEntityObj.matchNum : "";
			tv_game.setText("比赛（" + matchNum + "）");

			// 水印置顶1：置顶isTop
			if ("1".equals(mClubTabEntityObj.isTop)) {
				iv_tag_top.setImageResource(R.drawable.indicator_common_set_up);
			}
			// 水印是否推荐1：推荐isRecommend
			else if ("1".equals(mClubTabEntityObj.isRecommend)) {
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
	 * 设置推荐置顶图片GONE
	 * 
	 * @param FlagVisible
	 * @return
	 */
	public RelativeLayout setTopRecommadImageViewVisible(int FlagVisible) {
		iv_tag_top.setVisibility(FlagVisible);
		return this;

	}

	/**
	 * 设置充值按显示，默认不显示
	 * 
	 * @param FlagVisible
	 * @return
	 */
	public RelativeLayout setRechangeVisible(int FlagVisible) {
		btn_rechange.setVisibility(FlagVisible);
		return this;
	}

	/**
	 * 设置club监听
	 * 
	 * @param mClubListen
	 */
	public void setClubListen(ClubListen mClubListen) {
		this.mClubListen = mClubListen;
	}

	/**
	 * Club底部button的listen
	 * 
	 * @author Frank
	 */
	public interface ClubListen {
		/**
		 * 去充值事件
		 */
		public void doRechange();

		/**
		 * 查看活动
		 */
		public void lookActivitys();

		/**
		 * 查看比赛
		 */
		public void lookMathces();

		/**
		 * 地图查看位置
		 */
		public void goMapShow();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_rechange:
			if (mClubListen != null) {
				mClubListen.doRechange();
			}
			break;
		case R.id.tv_game:
			if (mClubListen != null) {
				mClubListen.lookMathces();
			}
			break;
		case R.id.tv_activity:
			if (mClubListen != null) {
				mClubListen.lookActivitys();
			}
			break;
		case R.id.btn_rechange:
			if (mClubListen != null) {
				mClubListen.doRechange();
			}
			break;
		case R.id.tv_distance:
			if (mClubListen != null) {
				mClubListen.goMapShow();
			}
			break;
		}
	}

	/**
	 * 设置item每一项margintop的值
	 */
	public RelativeLayout setMarginTop() {
		RelativeLayout.LayoutParams ll = (RelativeLayout.LayoutParams) rl_layout.getLayoutParams();
		ll.setMargins(0, 18, 0, 0);
		rl_layout.setLayoutParams(ll);
		return this;
	}

}
