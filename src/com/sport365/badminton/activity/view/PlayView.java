package com.sport365.badminton.activity.view;

import android.content.Context;
import android.text.TextUtils;
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
public class PlayView extends RelativeLayout {
	private TextView tv_play_activity_pic;        //周期活动
	private ImageView imageView;        // 图片
	private TextView tv_paly_name;        // 活动名称
	private TextView tv_play_num;        // 活动报名的人数
	private TextView tv_play_price;    // 价格
	private TextView tv_time_on;        // 时间
	private TextView tv_place_big;    // 大区域
	private TextView tv_distance;        // 小区域
	private LinearLayout ll_bottom;// 底部报名的按钮
	public PlayView(Context context) {
		super(context);
		inflate(context, R.layout.play_item_layout, this);
		tv_play_activity_pic = (TextView) findViewById(R.id.tv_play_activity_pic);
		tv_paly_name = (TextView) findViewById(R.id.tv_paly_name);
		tv_play_num = (TextView) findViewById(R.id.tv_play_num);
		tv_play_price = (TextView) findViewById(R.id.tv_play_price);
		tv_time_on = (TextView) findViewById(R.id.tv_time_on);
		tv_place_big = (TextView) findViewById(R.id.tv_place_big);
		tv_distance = (TextView) findViewById(R.id.tv_distance);
		ll_bottom = (LinearLayout) findViewById(R.id.ll_bottom);
		imageView = (ImageView) findViewById(R.id.imageView);
	}

	public RelativeLayout setDateView(MatchEntityObj mMatchEntityObj) {
		if (mMatchEntityObj != null) {
			// 名称
			String matchName = !TextUtils.isEmpty(mMatchEntityObj.matchName) ? mMatchEntityObj.matchName : "";
			tv_paly_name.setText(matchName);

			// 图片
			String matchLogo = !TextUtils.isEmpty(mMatchEntityObj.matchLogo) ? mMatchEntityObj.matchLogo : "";
			ImageLoader.getInstance().displayImage(matchLogo, imageView);

			//  时间
			String beginDate = !TextUtils.isEmpty(mMatchEntityObj.beginDate) ? mMatchEntityObj.beginDate : "";
			String endDate = !TextUtils.isEmpty(mMatchEntityObj.endDate) ? mMatchEntityObj.endDate : "";
			tv_time_on.setText(beginDate + "--" + endDate);

			// 大区域
			String venueName = !TextUtils.isEmpty(mMatchEntityObj.venueName) ? mMatchEntityObj.venueName : "";
			tv_place_big.setText(venueName);

			//小区域
			String matchAdress = !TextUtils.isEmpty(mMatchEntityObj.matchAdress) ? mMatchEntityObj.matchAdress : "";
			tv_distance.setText(matchAdress);

			// 价格
			String matchFee = !TextUtils.isEmpty(mMatchEntityObj.matchFee) ? mMatchEntityObj.matchFee : "";
			tv_play_price.setText(matchFee);
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
}
