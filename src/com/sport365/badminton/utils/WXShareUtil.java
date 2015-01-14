package com.sport365.badminton.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.sport365.badminton.BaseApplication;
import com.sport365.badminton.R;
import com.sport365.badminton.params.SystemConfig;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXShareUtil {

	private IWXAPI api;
	private static final int THUMB_SIZE = 150;
	private static final String SDCARD_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath();
	private static WXShareUtil mWXShareUtil;

	private WXShareUtil(Context mContext) {
		api = WXAPIFactory.createWXAPI(mContext, SystemConfig.WEIXIN_APP_ID);

	}

	public static WXShareUtil getInstance(Context mContext) {
		if (mWXShareUtil == null) {
			mWXShareUtil = new WXShareUtil(mContext.getApplicationContext());
		}

		return mWXShareUtil;
	}

	// 分享到朋友圈(YES) or分享到微信好友(NO) toFriendCircle;
	//
	/**
	 * 1. 分享纯文字
	 * 
	 * @param toFriendCircle
	 *            是否分享到朋友圈
	 * @param context
	 *            分享的文本信息
	 */
	public void sendText(boolean toFriendCircle, String context) {
		registerApp();
		if (!isInstallWeiXin()) {
			return;
		}
		if (toFriendCircle && !isShareFriendCircle()) {
			return;
		}
		// 初始化一个WXTextObject对象
		WXTextObject textObj = new WXTextObject();
		textObj.text = context;

		// 用WXTextObject对象初始化一个WXMediaMessage对象
		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = textObj;
		// 发送文本类型的消息时，title字段不起作用
		// msg.title = "Will be ignored";
		msg.description = context;

		// 构造一个Req
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("text"); // transaction字段用于唯一标识一个请求
		req.message = msg;
		req.scene = toFriendCircle ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
		// 调用api接口发送数据到微信
		api.sendReq(req);
	}

	//
	/**
	 * 2. 分享纯图片
	 * 
	 * @param toFriendCircle
	 *            是否分享到朋友圈
	 * @param bmpImg
	 *            分享图片的bitmap对象
	 */
	public void sendImg(boolean toFriendCircle, Bitmap bmpImg) {
		registerApp();
		if (!isInstallWeiXin()) {
			return;
		}
		if (toFriendCircle && !isShareFriendCircle()) {
			return;
		}
		WXImageObject imgObj = new WXImageObject(bmpImg);

		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = imgObj;

		Bitmap thumbBmp = Bitmap.createScaledBitmap(bmpImg, THUMB_SIZE, THUMB_SIZE, true);
		// bmpImg.recycle();

		msg.thumbData = bmpToByteArray(thumbBmp, true); // 设置缩略图

		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("img");
		req.message = msg;
		req.scene = toFriendCircle ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
		api.sendReq(req);
	}

	/**
	 * 2. 分享纯图片
	 * 
	 * @param toFriendCircle
	 *            是否分享到朋友圈
	 * @param sdCardPath
	 *            相对路径，SDCARD_ROOT已经存在
	 */
	public void sendImg(boolean toFriendCircle, String sdCardPath) {
		registerApp();
		if (!isInstallWeiXin()) {
			return;
		}
		if (toFriendCircle && !isShareFriendCircle()) {
			return;
		}
		String path = SDCARD_ROOT + sdCardPath;
		File file = new File(path);
		if (!file.exists()) {
			String tip = "该文件不存在.";
			Utilities.showToast(tip + " path = " + path, BaseApplication.getInstance());
			return;
		}

		WXImageObject imgObj = new WXImageObject();
		imgObj.setImagePath(path);

		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = imgObj;

		Bitmap bmp = BitmapFactory.decodeFile(path);
		Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
		bmp.recycle();
		msg.thumbData = bmpToByteArray(thumbBmp, true);

		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("img");
		req.message = msg;
		req.scene = toFriendCircle ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
		api.sendReq(req);
	}

	/**
	 * 2. 分享纯图片
	 * 
	 * @param toFriendCircle
	 *            是否分享到朋友圈
	 * @param url
	 *            URL对象,不是url地址
	 */
	public void sendImg(boolean toFriendCircle, URL url) {
		registerApp();
		if (!isInstallWeiXin()) {
			return;
		}
		if (toFriendCircle && !isShareFriendCircle()) {
			return;
		}
		try {
			WXImageObject imgObj = new WXImageObject();
			imgObj.imageUrl = url.getPath();

			WXMediaMessage msg = new WXMediaMessage();
			msg.mediaObject = imgObj;

			Bitmap bmp = BitmapFactory.decodeStream(url.openStream());
			Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
			bmp.recycle();
			msg.thumbData = bmpToByteArray(thumbBmp, true);

			SendMessageToWX.Req req = new SendMessageToWX.Req();
			req.transaction = buildTransaction("img");
			req.message = msg;
			req.scene = toFriendCircle ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
			api.sendReq(req);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendImgWithUrl(boolean toFriendCircle, String url) {
		registerApp();
		if (!isInstallWeiXin()) {
			return;
		}
		if (toFriendCircle && !isShareFriendCircle()) {
			return;
		}
		try {
			WXImageObject imgObj = new WXImageObject();
			imgObj.imageUrl = url;

			WXMediaMessage msg = new WXMediaMessage();
			msg.mediaObject = imgObj;

			Bitmap bmp = BitmapFactory.decodeStream(new URL(url).openStream());
			Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
			bmp.recycle();
			msg.thumbData = bmpToByteArray(thumbBmp, true);

			SendMessageToWX.Req req = new SendMessageToWX.Req();
			req.transaction = buildTransaction("img");
			req.message = msg;
			req.scene = toFriendCircle ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
			api.sendReq(req);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 3. 分享网页
	/**
	 * 3. 分享网页
	 * 
	 * @param toFriendCircle
	 *            是否分享到朋友圈
	 * @param webPageUrl
	 *            分享网页的地址
	 * @param title
	 *            分享的标题
	 * @param description
	 *            分享的描述
	 * @param bitmap
	 *            分享网页的附带图片的bitmap对象
	 */
	public void sendWebpage(boolean toFriendCircle, String webPageUrl, String title, String description, Bitmap bitmap) {
		registerApp();
		if (!isInstallWeiXin()) {
			return;
		}
		if (toFriendCircle && !isShareFriendCircle()) {
			return;
		}
		WXWebpageObject webpage = new WXWebpageObject();
		webpage.webpageUrl = webPageUrl;
		WXMediaMessage msg = new WXMediaMessage(webpage);
		msg.title = title;
		msg.description = description;
		Bitmap thumb = null;
		if (bitmap != null) {
			thumb = Bitmap.createScaledBitmap(bitmap, 150, 150, true);
		} else {
			thumb = BitmapFactory.decodeResource(BaseApplication.getInstance().getResources(), R.drawable.icon_share_weixin);
		}
		msg.thumbData = bmpToByteArray(thumb, true);

		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("webpage");
		req.message = msg;
		req.scene = toFriendCircle ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
		api.sendReq(req);
	}

	// 4. 将app注册到微信
	/**
	 * 4. 将app注册到微信
	 * 
	 */
	private void registerApp() {
		api.registerApp(SystemConfig.WEIXIN_APP_ID);
	}

	// 5. 启动微信
	/**
	 * 5. 启动微信
	 * 
	 * @return 打开：true 否则：false
	 */
	public boolean startWEIXIN() {
		return api.openWXApp();
	}

	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}

	/**
	 * 判断是否安装微信
	 * 
	 * @return true:已安装 false:未安装微信客户端
	 */
	private boolean isInstallWeiXin() {
		if (!api.isWXAppInstalled()) {
			Utilities.showToast("未安装微信客户端", BaseApplication.getInstance().getApplicationContext());
			return false;
		}
		return true;

	}

	/**
	 * 判断是否可以进行朋友分享
	 * 
	 * @return true:支持分享到朋友圈 false:不支持
	 */
	public boolean isShareFriendCircle() {
		int wxSdkVersion = api.getWXAppSupportAPI();
		if (wxSdkVersion < SystemConfig.TIMELINE_SUPPORTED_VERSION) {
			Utilities.showToast("不支持分享到朋友圈", BaseApplication.getInstance().getApplicationContext());
			return false;
		}
		return true;
	}

	/**
	 * bmpToByteArray
	 * 
	 * @param bmp
	 *            bitmap对象
	 * @param needRecycle
	 *            bitmap是否需要回收
	 * @return
	 */
	private static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.JPEG, 60, output);
		if (needRecycle) {
			bmp.recycle();
		}

		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
}
