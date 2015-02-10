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
import com.sport365.badminton.entity.reqbody.FindPwdByPhoneReqBody;
import com.sport365.badminton.entity.reqbody.SendMobileCodeReqBody;
import com.sport365.badminton.entity.webservice.SportParameter;
import com.sport365.badminton.entity.webservice.SportWebService;
import com.sport365.badminton.http.base.HttpTaskHelper;
import com.sport365.badminton.http.base.IRequestProxyCallback;
import com.sport365.badminton.http.json.req.ServiceRequest;
import com.sport365.badminton.http.json.res.ResponseContent;
import com.sport365.badminton.utils.Utilities;
import com.squareup.okhttp.ResponseBody;

public class FindPwdByPhoneFragment extends BaseFragment implements View.OnClickListener{

    private EditText et_phone;
    private EditText et_code;
    private EditText et_old_code;
    private EditText et_new_pwd;
    private EditText et_confirm_pwd;
    private Button btn_send_code;
    private Button btn_reset_code;

    private TimeCount timeCount;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_findpwdbyphone, container, false);
        findViews(view);
        return view;
    }

    private void findViews(View view) {
        et_phone = (EditText) view.findViewById(R.id.et_phone);
        et_code = (EditText) view.findViewById(R.id.et_code);
        et_old_code = (EditText) view.findViewById(R.id.et_old_code);
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
        switch (v.getId())
        {
            case R.id.btn_send_code:
                if (TextUtils.isEmpty(et_phone.getText().toString())) {
                    Utilities.showToast(getString(R.string.register_hint), getActivity());
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
                if (TextUtils.isEmpty(et_old_code.getText().toString())) {
                    Utilities.showToast("请输入原密码", getActivity());
                    return;
                }
                if (TextUtils.isEmpty(et_confirm_pwd.getText().toString())) {
                    Utilities.showToast("请确认新密码", getActivity());
                    return;
                }
                if (!et_new_pwd.getText().toString().equals(et_confirm_pwd.getText().toString())) {
                    Utilities.showToast("确认密码输入错误", getActivity());
                    return;
                }

                FindPwdByPhoneReqBody reqBody1 =new FindPwdByPhoneReqBody();
                reqBody1.mobile=et_phone.getText().toString();
                reqBody1.code=et_code.getText().toString();
                reqBody1.password=et_old_code.getText().toString();
                reqBody1.newpassword=et_new_pwd.getText().toString();
                resetPwd(reqBody1);

                break;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(timeCount!=null)
        {
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
        sendRequestWithDialog(new ServiceRequest(getActivity(), webService, reqBody), null, new IRequestProxyCallback() {

            @Override
            public void onSuccess(HttpTaskHelper.JsonResponse jsonResponse, HttpTaskHelper.RequestInfo requestInfo) {
                ResponseContent<ResponseBody> de = jsonResponse.getResponseContent(ResponseBody.class);
                Utilities.showToast(de.getHeader().getRspDesc(), getActivity());
                btn_reset_code.setClickable(true);
                btn_reset_code.setBackgroundColor(getResources().getColor(R.color.base_blue));
                //请求成功之后，60S内不能点击
                timeCount = new TimeCount(60 * 1000, 1000);
                timeCount.start();
                btn_send_code.setBackgroundColor(getResources().getColor(R.color.grey));
                btn_send_code.setClickable(false);
            }

            @Override
            public void onError(ResponseContent.Header header, HttpTaskHelper.RequestInfo requestInfo) {
                super.onError(header, requestInfo);
                Utilities.showToast(header.getRspDesc(), getActivity());
            }
        });
    }

    /**
     * 重置密码
     *
     * @param reqBody
     */
    private void resetPwd(FindPwdByPhoneReqBody reqBody) {
        SportWebService webService = new SportWebService(SportParameter.RESET_PASSWORD_BY_MOBILE);
        sendRequestWithDialog(new ServiceRequest(getActivity(), webService, reqBody), null, new IRequestProxyCallback() {

            @Override
            public void onSuccess(HttpTaskHelper.JsonResponse jsonResponse, HttpTaskHelper.RequestInfo requestInfo) {
                ResponseContent<ResponseBody> de = jsonResponse.getResponseContent(ResponseBody.class);
                Utilities.showToast(de.getHeader().getRspDesc(), getActivity());
                getActivity().finish();
            }

            @Override
            public void onError(ResponseContent.Header header, HttpTaskHelper.RequestInfo requestInfo) {
                super.onError(header, requestInfo);
                Utilities.showToast(header.getRspDesc(), getActivity());
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
