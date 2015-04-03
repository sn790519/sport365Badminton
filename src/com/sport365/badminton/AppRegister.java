package com.sport365.badminton;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.sport365.badminton.utils.SystemConfig;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by kjh08490 on 2015/4/1.
 */
public class AppRegister extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		final IWXAPI msgApi = WXAPIFactory.createWXAPI(context, null);

		// 将该app注册到微信
		msgApi.registerApp(SystemConfig.WEIXIN_APP_ID);
	}
}