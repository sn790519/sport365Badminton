package com.sport365.badminton.activity;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.*;
import android.widget.ProgressBar;
import com.sport365.badminton.BaseActivity;
import com.sport365.badminton.R;
import com.sport365.badminton.utils.BundleKeys;
import com.sport365.badminton.utils.ULog;

/**
 * 统一的webview的activity
 */
public class MyWebViewActivity extends BaseActivity {

    private WebView my_webview;
    private String loadurl = "";
    private String title = "";
    private ProgressBar pb_webview_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.my_webview_layout);

        pb_webview_loading = (ProgressBar) findViewById(R.id.pb_webview_loading);
        my_webview = (WebView) findViewById(R.id.my_webview);
        loadurl = getIntent().getStringExtra(BundleKeys.WEBVIEEW_LOADURL);
        title = getIntent().getStringExtra(BundleKeys.WEBVIEEW_TITLE);
        setActionBarTitle(title);
        mActionbar_right.setVisibility(View.GONE);

        /*----设置WebView控件参数----*/
        WebSettings webSettings = my_webview.getSettings();
        webSettings.setJavaScriptEnabled(true);// 设置响应JS
//        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
//        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

//        my_webview.setVerticalScrollBarEnabled(false);

        my_webview.addJavascriptInterface(new Object() {

            public void closeActivity() {
                Log.e("TAG","--------------");
                MyWebViewActivity.this.finish();
            }
        }, "javaMethod");


        my_webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        my_webview.setWebViewClient(new WebViewClient() {// 设置WebView客户端对象
            /**
             * 重写方法，否则点击页面时WebView会重新启动系统浏览器
             */
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                ULog.error(url);
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
                handler.proceed();// 让https的站点通过访问，设置为可通过证书。
            }
        });

        my_webview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                // Activity和Webview根据加载程度决定进度条的进度大小
                // 当加载到100%的时候 进度条自动消失
                if (progress == 100) {
                    pb_webview_loading.setVisibility(View.GONE);
                } else {
                    pb_webview_loading.setProgress(progress);
                    pb_webview_loading.setVisibility(View.VISIBLE);
                }
            }


            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
        });
        my_webview.loadUrl(loadurl);
    }

}
