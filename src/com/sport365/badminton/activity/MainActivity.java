package com.sport365.badminton.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.sport365.badminton.BaseActivity;
import com.sport365.badminton.R;
import com.sport365.badminton.entity.reqbody.ReqBody;
import com.sport365.badminton.entity.webservice.SportParameter;
import com.sport365.badminton.entity.webservice.SportWebService;
import com.sport365.badminton.http.base.DialogConfig;
import com.sport365.badminton.http.base.HttpTaskHelper.CancelInfo;
import com.sport365.badminton.http.base.HttpTaskHelper.JsonResponse;
import com.sport365.badminton.http.base.HttpTaskHelper.RequestInfo;
import com.sport365.badminton.http.base.IRequestProxyListener;
import com.sport365.badminton.http.json.req.ServiceRequest;
import com.sport365.badminton.http.json.res.ResponseContent.Header;

public class MainActivity extends BaseActivity {
	private String TAG = MainActivity.class.getSimpleName();

	private Button btn_with_dialog;
	private Button btn_with_no_dialog;

	private ImageView iv_test;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btn_with_dialog = (Button) findViewById(R.id.btn_with_dialog);
		btn_with_no_dialog = (Button) findViewById(R.id.btn_with_no_dialog);
		iv_test = (ImageView) findViewById(R.id.iv_test);
		imageLoader
				.displayImage(
						"http://f.hiphotos.baidu.com/image/pic/item/a1ec08fa513d26970c5be74a56fbb2fb4316d819.jpg",
						iv_test);
		btn_with_dialog.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				testWithRequestDialog();
			}
		});
		btn_with_no_dialog.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				testWithRequestNoDialog();
			}
		});
	}

	/*
	 * HttpEngine请求路径 (4801) : http:
	 * //yundong.shenghuo365.net/ShenghuoHandler.ashx HttpEngine请求参数 (4801) : {
	 * "request": { "body": { "clientInfo": { "clientIp": "1111111", "deviceId":
	 * "69e0f11cb4864e1f", "versionNumber": "1.0", "versionType": "VersionType"
	 * } }, "header": { "accountID": "c26b007f-c89e-431a-b8cc-493becbdd8a2",
	 * "digitalSign": "8bb29ece23f4298953a73ab8d100c8d2", "reqTime":
	 * "1420890793405", "serviceName": "shenghuohome", "version":
	 * "20111128102912" } } HttpEngine返回内容 (4801) : { "response": { "header": {
	 * "rspType": "1", "rspCode": "1000", "rspDesc": "参数不正确,ProductId参数不正确" } }
	 * } }
	 */

	private void testWithRequestDialog() {
		ReqBody reqBody = new ReqBody();
		// 请求有加载框
		sendRequestWithDialog(new ServiceRequest(mContext, new SportWebService(
				SportParameter.GET_INSURANCE_LIST), reqBody),
				new DialogConfig.Builder().build(),
				new IRequestProxyListener() {

					@Override
					public void onSuccess(JsonResponse jsonResponse,
							RequestInfo requestInfo) {
						Toast.makeText(MainActivity.this, "onSuccess",
								Toast.LENGTH_LONG).show();
					}

					@Override
					public void onError(Header header, RequestInfo requestInfo) {
						Toast.makeText(MainActivity.this, "onError",
								Toast.LENGTH_LONG).show();
					}

					@Override
					public void onCanceled(CancelInfo cancelInfo) {
						Toast.makeText(MainActivity.this, "onCanceled",
								Toast.LENGTH_LONG).show();
					}
				});
	}

	private void testWithRequestNoDialog() {
		ReqBody reqBody = new ReqBody();
		// 请求没有加载框
		sendRequestWithNoDialog(
				new ServiceRequest(mContext, new SportWebService(
						SportParameter.GET_INSURANCE_LIST), reqBody),
				new IRequestProxyListener() {

					@Override
					public void onSuccess(JsonResponse jsonResponse,
							RequestInfo requestInfo) {
						Toast.makeText(MainActivity.this, "onSuccess",
								Toast.LENGTH_LONG).show();
					}

					@Override
					public void onError(Header header, RequestInfo requestInfo) {
						Toast.makeText(MainActivity.this, "onError",
								Toast.LENGTH_LONG).show();
					}

					@Override
					public void onCanceled(CancelInfo cancelInfo) {
						Toast.makeText(MainActivity.this, "onCanceled",
								Toast.LENGTH_LONG).show();
					}
				});
	}

}
