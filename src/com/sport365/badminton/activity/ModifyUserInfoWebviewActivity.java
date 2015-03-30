package com.sport365.badminton.activity;

import android.os.Bundle;
import android.webkit.JavascriptInterface;

/**
 * 修改个人信息
 */
public class ModifyUserInfoWebviewActivity extends MyWebViewActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		my_webview.addJavascriptInterface(new Object() {

			@JavascriptInterface
			public void closeActivity() {
				myHandler.post(new Runnable() {
					@Override
					public void run() {
						ModifyUserInfoWebviewActivity.this.finish();
					}
				});
			}
		}, "javaMethod");
	}


}
