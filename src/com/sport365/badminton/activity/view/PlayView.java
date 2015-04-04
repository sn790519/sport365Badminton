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
import com.sport365.badminton.entity.obj.MatchEntityObj;
import com.sport365.badminton.http.base.ImageLoader;

/**
 * 比赛的view
 * Created by kjh08490 on 2015/3/7.
 */
public class PlayView extends RelativeLayout implements OnClickListener {
	private TextView tv_title;
	private TextView tv_play_activity_pic;        //周期活动
	private ImageView imageView;        // 图片
	private ImageView iv_arrow;        // 箭头
	private TextView tv_paly_name;        // 活动名称
	private TextView tv_play_num;        // 活动报名的人数
	private TextView tv_play_price;    // 价格
	private TextView tv_time_on;        // 时间
	private TextView tv_place_big;    // 大区域
	private TextView tv_distance;        // 小区域
	private TextView tv_company_name;        // 主办方

	private LinearLayout rl_layout;// 背景
	/**
	 * 设置监听
	 */
	private PlayListen mPlayListen;


	private LinearLayout ll_bottom;// 底部报名的按钮

	public PlayView(Context context) {
		super(context);
		inflate(context, R.layout.play_item_layout, this);
		tv_play_activity_pic = (TextView) findViewById(R.id.tv_play_activity_pic);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_paly_name = (TextView) findViewById(R.id.tv_paly_name);
		tv_play_num = (TextView) findViewById(R.id.tv_play_num);
		tv_play_price = (TextView) findViewById(R.id.tv_play_price);
		tv_time_on = (TextView) findViewById(R.id.tv_time_on);
		tv_place_big = (TextView) findViewById(R.id.tv_place_big);
		tv_distance = (TextView) findViewById(R.id.tv_distance);
		tv_distance.setOnClickListener(this);
		tv_company_name = (TextView) findViewById(R.id.tv_company_name);
		ll_bottom = (LinearLayout) findViewById(R.id.ll_bottom);
		ll_bottom.setOnClickListener(this);
		imageView = (ImageView) findViewById(R.id.imageView);
		iv_arrow = (ImageView) findViewById(R.id.iv_arrow);
		rl_layout = (LinearLayout) findViewById(R.id.rl_layout);
	}

	public RelativeLayout setDateView(MatchEntityObj mMatchEntityObj) {
		if (mMatchEntityObj != null) {
			// title
			String matchName = !TextUtils.isEmpty(mMatchEntityObj.matchName) ? mMatchEntityObj.matchName : "";
			tv_title.setText(matchName);
			// 名称
			String venueName = !TextUtils.isEmpty(mMatchEntityObj.venueName) ? mMatchEntityObj.venueName : "";
			tv_paly_name.setText(venueName);
			// 主办方
			String matchPresenter = !TextUtils.isEmpty(mMatchEntityObj.matchPresenter) ? mMatchEntityObj.matchPresenter : "";
			tv_company_name.setText(matchPresenter);
			// 图片
			String matchLogo = !TextUtils.isEmpty(mMatchEntityObj.matchLogo) ? mMatchEntityObj.matchLogo : "";
			ImageLoader.getInstance().displayImage(matchLogo, imageView);
			//  时间
			String beginDate = !TextUtils.isEmpty(mMatchEntityObj.beginDate) ? mMatchEntityObj.beginDate : "";
			String endDate = !TextUtils.isEmpty(mMatchEntityObj.endDate) ? mMatchEntityObj.endDate : "";
			tv_time_on.setText("时间：" + beginDate + "--" + endDate);

			// 大区域
			String matchAdress = !TextUtils.isEmpty(mMatchEntityObj.matchAdress) ? mMatchEntityObj.matchAdress : "";
			tv_distance.setText(matchAdress);

			//奖金
			String matchAwardFee = !TextUtils.isEmpty(mMatchEntityObj.matchAwardFee) ? mMatchEntityObj.matchAwardFee : "";
			tv_place_big.setText("￥" + matchAwardFee);

			// 价格
			String matchFee = !TextUtils.isEmpty(mMatchEntityObj.matchFee) ? mMatchEntityObj.matchFee : "";
			tv_play_price.setText("￥" + matchFee);
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
	 * 方法将箭头隐藏
	 *
	 * @param FlagVisible
	 * @return
	 */
	public RelativeLayout setArrowVisible(int FlagVisible) {
		iv_arrow.setVisibility(FlagVisible);
		return this;
	}

	/**
	 * 设置监听
	 *
	 * @param mPlayListen
	 */
	public void setPlayListen(PlayListen mPlayListen) {
		this.mPlayListen = mPlayListen;
	}

	/**
	 * 设置比赛监听
	 *
	 * @author Frank
	 */
	public interface PlayListen {
		/**
		 * 去预定
		 */
		public void doBookName();

		/**
		 * 地图查看位置
		 */
		public void goMapShow();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.tv_distance:
				if (mPlayListen != null) {
					mPlayListen.goMapShow();
				}
				break;
			case R.id.ll_bottom:
				if (mPlayListen != null) {
					mPlayListen.doBookName();
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

