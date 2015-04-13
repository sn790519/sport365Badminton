package com.sport365.badminton.utils;

import android.content.Intent;
import android.os.Handler;
import android.webkit.JavascriptInterface;
import com.sport365.badminton.activity.BallFriendActivity;
import com.sport365.badminton.activity.LoginActivity;

public class LoginJavaScript {


    private BallFriendActivity activity;
    private Handler myHandler;

    public LoginJavaScript(BallFriendActivity activity, Handler myHandler) {
        this.myHandler = myHandler;
        this.activity = activity;
    }

    @JavascriptInterface
    public void startLoginActivity() {
        myHandler.post(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(activity, LoginActivity.class);
                activity.startActivityForResult(intent,0);
            }
        });

    }
}

