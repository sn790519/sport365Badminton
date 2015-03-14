
package com.sport365.badminton.wxpay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.sport365.badminton.utils.SystemConfig;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 *         微信支付回调类
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

	private IWXAPI api;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		api = WXAPIFactory.createWXAPI(this, SystemConfig.WEIXIN_APP_ID);
		api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		if (resp != null && resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			//TODO 发送支付成功事件
			switch (resp.errCode) {
				case BaseResp.ErrCode.ERR_OK:
					Toast.makeText(WXPayEntryActivity.this, "微信支付成功", Toast.LENGTH_LONG).show();
					break;
				case BaseResp.ErrCode.ERR_USER_CANCEL:
					Toast.makeText(WXPayEntryActivity.this, "微信支付未完成", Toast.LENGTH_LONG).show();
					break;
			}
		}
		finish();
	}

}