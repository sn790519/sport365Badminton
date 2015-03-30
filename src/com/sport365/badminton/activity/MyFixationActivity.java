package com.sport365.badminton.activity;

import android.os.Bundle;
import android.webkit.JavascriptInterface;
import com.sport365.badminton.R;
import com.sport365.badminton.view.LoadingDialog;

/**
 * 我的固定活动
 */
public class MyFixationActivity extends MyWebViewActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		my_webview.addJavascriptInterface(new Object() {

			@JavascriptInterface
			public void closeActivity() {
				myHandler.post(new Runnable() {
					@Override
					public void run() {
						MyFixationActivity.this.finish();
					}
				});
			}
		}, "javaMethod");
	}


}
