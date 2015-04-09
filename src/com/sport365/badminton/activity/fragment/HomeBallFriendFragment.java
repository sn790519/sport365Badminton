package com.sport365.badminton.activity.fragment;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.*;
import com.sport365.badminton.BaseFragment;
import com.sport365.badminton.R;
import com.sport365.badminton.activity.MainActivity;
import com.sport365.badminton.utils.SystemConfig;
import com.sport365.badminton.utils.ULog;

/**
 * 惠球友
 */
public class HomeBallFriendFragment extends BaseFragment {

    protected WebView my_webview;
    protected Handler myHandler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.home_ballfriend_layout, container, false);
        my_webview = (WebView) view.findViewById(R.id.my_webview);
        WebSettings webSettings = my_webview.getSettings();
        webSettings.setJavaScriptEnabled(true);// 设置响应JS
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        my_webview.setVerticalScrollBarEnabled(false);
        my_webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        my_webview.setWebViewClient(new WebViewClient() {// 设置WebView客户端对象
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });

        my_webview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress == 100) {
                    if (null != mLoadingDialog) {
                        mLoadingDialog.dismiss();
                    }
                } else {
                    if (null != mLoadingDialog && !mLoadingDialog.isShowing()) {
                        mLoadingDialog.show();
                    }
                }
            }


            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
        });

        my_webview.addJavascriptInterface(new Object() {

            @JavascriptInterface
            public void closeActivity() {
                myHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        ((MainActivity) getActivity()).rb_menu_mian.setChecked(true);
                    }
                });
            }
        }, "javaMethod");

        my_webview.loadUrl("http://yundong.shenghuo365.net/yd365/cheap-index.html" + "?app=1");
        return view;
    }


}
