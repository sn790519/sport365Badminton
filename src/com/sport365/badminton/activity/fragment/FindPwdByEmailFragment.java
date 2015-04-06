package com.sport365.badminton.activity.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.sport365.badminton.BaseFragment;
import com.sport365.badminton.R;
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
 * 发送验证码到邮箱
 */
public class FindPwdByEmailFragment extends BaseFragment implements View.OnClickListener {

    private EditText et_phone;
    private EditText et_code;
    private EditText et_new_pwd;
    private EditText et_confirm_pwd;
    private Button btn_send_code;
    private Button btn_reset_code;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_findpwdbyemail, container, false);
        findViews(view);
        return view;
    }

    private void findViews(View view) {
        et_phone = (EditText) view.findViewById(R.id.et_phone);
        et_code = (EditText) view.findViewById(R.id.et_code);
        et_new_pwd = (EditText) view.findViewById(R.id.et_new_pwd);
        et_confirm_pwd = (EditText) view.findViewById(R.id.et_confirm_pwd);
        btn_send_code = (Button) view.findViewById(R.id.btn_send_code);
        btn_send_code.setOnClickListener(this);
        btn_reset_code = (Button) view.findViewById(R.id.btn_reset_code);
        btn_reset_code.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_send_code:
                if (TextUtils.isEmpty(et_phone.getText().toString())) {
                    Utilities.showToast("请输入邮箱地址", getActivity());
                    return;
                }
                SendMobileCodeReqBody reqBody = new SendMobileCodeReqBody();
                reqBody.mobile = et_phone.getText().toString();
                sendMobileCode(reqBody);
                break;
            case R.id.btn_reset_code:
                if (TextUtils.isEmpty(et_code.getText().toString())) {
                    Utilities.showToast(getString(R.string.register_hint_code), getActivity());
                    return;
                }
                if (TextUtils.isEmpty(et_new_pwd.getText().toString())) {
                    Utilities.showToast("请输入新密码", getActivity());
                    return;
                }
                if (TextUtils.isEmpty(et_confirm_pwd.getText().toString())) {
                    Utilities.showToast("请确认新密码", getActivity());
                    return;
                }
                if (et_new_pwd.getText().toString().equals(et_confirm_pwd.getText().toString())) {
                    Utilities.showToast("密码输入错误", getActivity());
                    return;
                }


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
        sendRequestWithDialog(new ServiceRequest(getActivity(), webService, reqBody), null, new IRequestProxyCallback() {

            @Override
            public void onSuccess(HttpTaskHelper.JsonResponse jsonResponse, HttpTaskHelper.RequestInfo requestInfo) {
                ResponseContent<ResponseBody> de = jsonResponse.getResponseContent(ResponseBody.class);
                Utilities.showToast(de.getHeader().getRspDesc(), getActivity());
                btn_reset_code.setClickable(true);
                btn_reset_code.setBackgroundColor(getResources().getColor(R.color.base_blue));
                //请求成功之后，60S内不能点击
                TimeCount timeCount = new TimeCount(60 * 1000, 1000);
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

