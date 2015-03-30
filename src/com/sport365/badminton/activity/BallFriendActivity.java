package com.sport365.badminton.activity;

import android.os.Bundle;
import android.webkit.JavascriptInterface;

/**
 * 惠球友
 */
public class BallFriendActivity extends MyWebViewActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
	}


}
