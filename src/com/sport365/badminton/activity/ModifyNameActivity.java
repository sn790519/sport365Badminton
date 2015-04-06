package com.sport365.badminton.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.sport365.badminton.BaseActivity;
import com.sport365.badminton.R;
import com.sport365.badminton.entity.reqbody.ModifyPhoneReqBody;
import com.sport365.badminton.entity.reqbody.SendMobileCodeReqBody;
import com.sport365.badminton.entity.webservice.SportParameter;
import com.sport365.badminton.entity.webservice.SportWebService;
import com.sport365.badminton.http.base.HttpTaskHelper;
import com.sport365.badminton.http.base.IRequestProxyCallback;
import com.sport365.badminton.http.json.req.ServiceRequest;
import com.sport365.badminton.http.json.res.ResponseContent;
import com.sport365.badminton.utils.SystemConfig;
import com.sport365.badminton.utils.Utilities;
import com.squareup.okhttp.ResponseBody;


/**
 * 修改姓名
 * Created by vincent on 15/2/1.
 */
public class ModifyNameActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_phone;
    private EditText et_code;
    private EditText et_old_phone;
    private Button btn_binding;
    private Button btn_send_code;

    private TimeCount timeCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifify_name);
        initActionBar();
        findViews();
    }

    private void initActionBar() {
        setActionBarTitle("修改姓名");
        mActionbar_right.setVisibility(View.GONE);
    }

    private void findViews() {
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_code = (EditText) findViewById(R.id.et_code);
        et_old_phone = (EditText) findViewById(R.id.et_old_phone);
        btn_binding = (Button) findViewById(R.id.btn_binding);
        btn_binding.setOnClickListener(this);
        btn_send_code = (Button) findViewById(R.id.btn_send_code);
        btn_send_code.setOnClickListener(this);
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
            case R.id.btn_binding:
                if (TextUtils.isEmpty(et_phone.getText().toString())) {
                    Utilities.showToast(getString(R.string.register_hint), mContext);
                    return;
                }
                if (TextUtils.isEmpty(et_code.getText().toString())) {
                    Utilities.showToast("请输入验证码", mContext);
                    return;
                }
                if (TextUtils.isEmpty(et_old_phone.getText().toString())) {
                    Utilities.showToast("请出入原手机号码", mContext);
                    return;
                }

                ModifyPhoneReqBody reqBody1 = new ModifyPhoneReqBody();
                reqBody1.newmobile = et_phone.getText().toString();
                reqBody1.code = et_code.getText().toString();
                reqBody1.memberId = SystemConfig.loginResBody.memberId;
                reqBody1.mobile = et_old_phone.getText().toString();
                bindNewPhone(reqBody1);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timeCount != null) {
            timeCount.cancel();
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
                //请求成功之后，60S内不能点击
                timeCount = new TimeCount(60 * 1000, 1000);
                timeCount.start();
                btn_send_code.setBackgroundColor(getResources().getColor(R.color.grey));
                btn_send_code.setClickable(false);
            }

            @Override
            public void onError(ResponseContent.Header header, HttpTaskHelper.RequestInfo requestInfo) {
                super.onError(header, requestInfo);
            }
        });
    }

    /**
     * 绑定新手机号码
     *
     * @param reqBody
     */
    private void bindNewPhone(ModifyPhoneReqBody reqBody) {
        SportWebService webService = new SportWebService(SportParameter.MODIFY_MEMBER_MOBILE);
        sendRequestWithDialog(new ServiceRequest(mContext, webService, reqBody), null, new IRequestProxyCallback() {

            @Override
            public void onSuccess(HttpTaskHelper.JsonResponse jsonResponse, HttpTaskHelper.RequestInfo requestInfo) {
                ResponseContent<ResponseBody> de = jsonResponse.getResponseContent(ResponseBody.class);
                Utilities.showToast(de.getHeader().getRspDesc(), mContext);
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
            btn_send_code.setText(millisUntilFinished / 1000 + "秒");
        }
    }


}