package com.sport365.badminton.activity;

import android.os.Bundle;
import android.view.Window;
import android.webkit.JavascriptInterface;

/**
 * 抽奖
 */
public class LotteryActivity extends MyWebViewActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		my_webview.addJavascriptInterface(new Object() {

			@JavascriptInterface
			public void closeActivity() {
				myHandler.post(new Runnable() {
					@Override
					public void run() {
						LotteryActivity.this.finish();
					}
				});
			}
		}, "javaMethod");
	}


}
