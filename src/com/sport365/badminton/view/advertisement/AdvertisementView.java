package com.sport365.badminton.view.advertisement;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.sport365.badminton.activity.MyWebViewActivity;
import com.sport365.badminton.entity.obj.AdvertisementObject;
import com.sport365.badminton.utils.BundleKeys;

/**
 * 广告专用.
 */
public class AdvertisementView extends BaseImageSwitcher<AdvertisementObject> {

	private String mEventId;
	private String mParam;
	private int tagLevel = 0;

	public AdvertisementView(Context context) {
		super(context);
		// setScreenRate(45, 14);
		setScreenRate(72, 13);
	}

	public AdvertisementView(Context context, int tagLevel) {
		super(context);
		this.tagLevel = tagLevel;
		// setScreenRate(45, 14);
		setScreenRate(72, 13);
	}

	public void setAdvertisementData(ArrayList<AdvertisementObject> datas) {
		setData(datas);
	}

	/**
	 * 广告的比例
	 * 
	 * @param widthRat
	 *            宽度
	 * @param heightRat
	 *            高度
	 */
	public void setAdvertisementRate(int widthRat, int heightRat) {
		setScreenRate(widthRat, heightRat);
	}

	public void setEventId(String eventId, String param) {
		this.mEventId = eventId;
		this.mParam = param;
	}

	@Override
	protected boolean performItemClick(AdapterView<?> parent, View view, int position, long id, int true_position) {
		if (!super.performItemClick(parent, view, position, id, true_position)) {

			final AdvertisementObject data_obj = mDatas.get(true_position);

			if (data_obj != null && !TextUtils.isEmpty(data_obj.redirectUrl)) {
				String tag = data_obj.tag;
				// NoticeTools.initNoticeUrl((Activity) mContext,
				// data_obj.redirectUrl, "");
				// NoticeTools替换 by Megatron King
				// URLPaserUtils.parseURL((Activity) mContext,
				// data_obj.redirectUrl);
				Toast.makeText(getContext(), "点击事件", Toast.LENGTH_LONG).show();
				Intent intent = new Intent(getContext(), MyWebViewActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString(BundleKeys.WEBVIEEW_LOADURL, data_obj.redirectUrl);
				bundle.putString(BundleKeys.WEBVIEEW_TITLE, "详情");
				intent.putExtras(bundle);
				getContext().startActivity(intent);

			}
		}
		return false;
	}

	@Override
	public String getUrlString(AdvertisementObject data) {
		return data.imageUrl;
	}
}
