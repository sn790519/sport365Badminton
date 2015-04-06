package com.sport365.badminton.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import com.sport365.badminton.R;
import com.sport365.badminton.utils.Utilities;
import com.sport365.badminton.utils.WXShareUtil;

/**
 * 分享的弹框
 *
 * @author Frank
 */
public class SharePopWindow extends PopupWindow implements View.OnClickListener {
	private Context mContext;
	private LayoutInflater mInflater;
	private View contentView;

	// 分享渠道
	private ImageView iv_sina;//新浪
	private ImageView iv_tengxun;//腾讯
	private ImageView iv_renren;//人人
	private ImageView iv_qqair;//qq空间
	private ImageView iv_text;//短信
	private ImageView iv_weixinfirends;//微信好友
	private ImageView iv_weixin;//微信朋友圈
	private ImageView iv_qq;//qq

	private Button btn_cancle;// 取消按钮

	private String shareUrl;
	private String shareTitle;

	public SharePopWindow(Context context) {
		super(context);
		this.mContext = context;
		initView();
		setPopupWindowProperties();
	}

	// 初始化弹框
	private void initView() {
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		contentView = mInflater.inflate(R.layout.share_pop_layout, null);
		iv_sina = (ImageView) contentView.findViewById(R.id.iv_sina);
		iv_sina.setOnClickListener(this);
		iv_tengxun = (ImageView) contentView.findViewById(R.id.iv_tengxun);
		iv_tengxun.setOnClickListener(this);
		iv_renren = (ImageView) contentView.findViewById(R.id.iv_renren);
		iv_renren.setOnClickListener(this);
		iv_qqair = (ImageView) contentView.findViewById(R.id.iv_qqair);
		iv_qqair.setOnClickListener(this);
		iv_text = (ImageView) contentView.findViewById(R.id.iv_text);
		iv_text.setOnClickListener(this);
		iv_weixinfirends = (ImageView) contentView.findViewById(R.id.iv_weixinfirends);
		iv_weixinfirends.setOnClickListener(this);
		iv_weixin = (ImageView) contentView.findViewById(R.id.iv_weixin);
		iv_weixin.setOnClickListener(this);
		iv_qq = (ImageView) contentView.findViewById(R.id.iv_qq);
		iv_qq.setOnClickListener(this);
		btn_cancle = (Button) contentView.findViewById(R.id.btn_cancle);
		btn_cancle.setOnClickListener(this);

	}

	public SharePopWindow setUrlANDSharetitle(String shareUrl, String shareTitle) {
		this.shareUrl = shareUrl;
		this.shareTitle = shareTitle;
		return this;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.iv_sina:
				Utilities.showToast("构建中...", mContext);
				break;
			case R.id.iv_tengxun:
				Utilities.showToast("构建中...", mContext);
				break;
			case R.id.iv_renren:
				Utilities.showToast("构建中...", mContext);
				break;
			case R.id.iv_qqair:
				Utilities.showToast("构建中...", mContext);
				break;
			case R.id.iv_text:
				sendSMS(shareTitle + shareUrl);
				break;
			case R.id.iv_weixinfirends:
				WXShareUtil.getInstance(mContext).sendWebpage(false, shareUrl, shareTitle, shareTitle, null);
				break;
			case R.id.iv_weixin:
				WXShareUtil.getInstance(mContext).sendWebpage(true, shareUrl, shareTitle, shareTitle, null);
				break;
			case R.id.iv_qq:
				Utilities.showToast("构建中...", mContext);
				break;
			case R.id.btn_cancle:// 取消
				dismiss();
				break;
		}
	}

	/**
	 * 发送短信
	 *
	 * @param smsBody
	 */
	private void sendSMS(String smsBody) {
		Uri smsToUri = Uri.parse("smsto:");
		Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
		intent.putExtra("sms_body", smsBody);
		mContext.startActivity(intent);
	}

	/**
	 * 设置弹窗属性
	 */
	private void setPopupWindowProperties() {
		// 设置SelectPicPopupWindow的View
		this.setContentView(contentView);
		// 设置SelectPicPopupWindow弹出窗体的宽、高
		this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
		this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		this.setOutsideTouchable(true);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(new BitmapDrawable());
	}


}
