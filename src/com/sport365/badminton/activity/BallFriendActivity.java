package com.sport365.badminton.activity;

import android.os.Bundle;
import android.webkit.JavascriptInterface;
import com.sport365.badminton.utils.LoginJavaScript;

/**
 * 惠球友
 */
public class BallFriendActivity extends MyWebViewActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//关闭当前界面
		my_webview.addJavascriptInterface(new Object() {

			@JavascriptInterface
			public void closeActivity() {
				myHandler.post(new Runnable() {
					@Override
					public void run() {
						BallFriendActivity.this.finish();
					}
				});
			}
		}, "javaMethod");

		//打开登陆
		LoginJavaScript loginJavaScript = new LoginJavaScript(BallFriendActivity.this);
		my_webview.addJavascriptInterface(loginJavaScript, "javaMethod");




	}


}
