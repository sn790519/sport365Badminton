package com.sport365.badminton.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.sport365.badminton.BaseActivity;
import com.sport365.badminton.R;
import com.sport365.badminton.entity.reqbody.RegisterReqBody;
import com.sport365.badminton.entity.reqbody.SendMobileCodeReqBody;
import com.sport365.badminton.entity.webservice.SportParameter;
import com.sport365.badminton.entity.webservice.SportWebService;
import com.sport365.badminton.http.base.HttpTaskHelper;
import com.sport365.badminton.http.base.IRequestProxyCallback;
import com.sport365.badminton.http.json.req.ServiceRequest;
import com.sport365.badminton.http.json.res.ResponseContent;
import com.sport365.badminton.utils.Utilities;
import com.squareup.okhttp.ResponseBody;


/**
 * 注册
 * Created by vincent on 15/2/1.
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {

	private Button btn_send_code;
	private Button btn_register;
	private EditText et_phone;
	private EditText et_code;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		initActionBar();
		findViews();
	}

	private void initActionBar() {
		setActionBarTitle("注册");
		mActionbar_right.setVisibility(View.GONE);
	}

	private void findViews() {
		btn_send_code = (Button) findViewById(R.id.btn_send_code);
		btn_send_code.setOnClickListener(this);
		btn_register = (Button) findViewById(R.id.btn_register);
		btn_register.setClickable(false);
		btn_register.setOnClickListener(this);
		btn_register.setBackgroundColor(getResources().getColor(R.color.grey));
		et_phone = (EditText) findViewById(R.id.et_phone);
		et_code = (EditText) findViewById(R.id.et_code);

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
			case R.id.btn_send_code:
				if (TextUtils.isEmpty(et_phone.getText().toString())) {
					Utilities.showToast(getString(R.string.register_hint), mContext);
					return;
				}
				SendMobileCodeReqBody reqBody = new SendMobileCodeReqBody();
				reqBody.mobile = et_phone.getText().toString();
				sendMobileCode(reqBody);
				break;

			case R.id.btn_register:
				if (TextUtils.isEmpty(et_phone.getText().toString())) {
					Utilities.showToast(getString(R.string.register_hint), mContext);
					return;
				}
				if (TextUtils.isEmpty(et_code.getText().toString())) {
					Utilities.showToast(getString(R.string.register_hint_code), mContext);
					return;
				}
				RegisterReqBody reqBody1 = new RegisterReqBody();
				reqBody1.mobile = et_phone.getText().toString();
				reqBody1.code = et_code.getText().toString();
				register(reqBody1);
				break;
		}
	}


	/**
	 * 发送验证码
	 *
	 * @param reqBody
	 */
	private void sendMobileCode(SendMobileCodeReqBody reqBody) {
		SportWebService webService = new SportWebService(SportParameter.SEND_MOBILE_CODE);
		sendRequestWithDialog(new ServiceRequest(mContext, webService, reqBody), null, new IRequestProxyCallback() {

			@Override
			public void onSuccess(HttpTaskHelper.JsonResponse jsonResponse, HttpTaskHelper.RequestInfo requestInfo) {
				ResponseContent<ResponseBody> de = jsonResponse.getResponseContent(ResponseBody.class);
				Utilities.showToast(de.getHeader().getRspDesc(), mContext);
				btn_register.setClickable(true);
				btn_register.setBackgroundColor(getResources().getColor(R.color.base_blue));
				//请求成功之后，60S内不能点击
				TimeCount timeCount = new TimeCount(60 * 1000, 1000);
				timeCount.start();
				btn_send_code.setBackgroundColor(getResources().getColor(R.color.grey));
			}

			@Override
			public void onError(ResponseContent.Header header, HttpTaskHelper.RequestInfo requestInfo) {
				super.onError(header, requestInfo);
			}
		});
	}

	/**
	 * 注册
	 *
	 * @param reqBody
	 */
	private void register(RegisterReqBody reqBody) {
		SportWebService webService = new SportWebService(SportParameter.REGISTER);
		sendRequestWithDialog(new ServiceRequest(mContext, webService, reqBody), null, new IRequestProxyCallback() {

			@Override
			public void onSuccess(HttpTaskHelper.JsonResponse jsonResponse, HttpTaskHelper.RequestInfo requestInfo) {
				ResponseContent<ResponseBody> de = jsonResponse.getResponseContent(ResponseBody.class);
				Utilities.showToast(de.getHeader().getRspDesc(), mContext);
				Intent data = new Intent();
				data.putExtra("phone", et_phone.getText().toString());
				setResult(RESULT_FIRST_USER, data);
				finish();
			}

			@Override
			public void onError(ResponseContent.Header header, HttpTaskHelper.RequestInfo requestInfo) {
				super.onError(header, requestInfo);
			}
		});
	}


	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish() {//计时完毕时触发
			btn_send_code.setText("重新发送");
			btn_send_code.setClickable(true);
			btn_send_code.setBackgroundColor(getResources().getColor(R.color.base_blue));
		}

		@Override
		public void onTick(long millisUntilFinished) {//计时过程显示
			btn_send_code.setClickable(false);
			btn_send_code.setText(millisUntilFinished / 1000 + "秒");
		}
	}
}