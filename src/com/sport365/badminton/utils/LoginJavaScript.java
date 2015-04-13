package com.sport365.badminton.utils;

import android.content.Intent;
import android.webkit.WebView;
import com.sport365.badminton.activity.BallFriendActivity;
import com.sport365.badminton.activity.LoginActivity;

public class LoginJavaScript {


	private BallFriendActivity activity;
	private WebView webView;

	public LoginJavaScript(BallFriendActivity activity) {

		this.activity = activity;
	}

	public void startLoginActivity() {
		Intent intent = new Intent(activity, LoginActivity.class);
		activity.startActivity(intent);
	}
}

