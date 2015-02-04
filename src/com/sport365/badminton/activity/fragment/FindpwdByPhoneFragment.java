package com.sport365.badminton.activity.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.sport365.badminton.BaseFragment;
import com.sport365.badminton.R;

public class FindpwdByPhoneFragment extends BaseFragment {


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.home_my_layout, container, false);
	}


	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish() {//计时完毕时触发
//			btn_send_code.setText("重新发送");
//			btn_send_code.setClickable(true);
//			btn_send_code.setBackgroundColor(getResources().getColor(R.color.base_blue));
		}

		@Override
		public void onTick(long millisUntilFinished) {//计时过程显示
//			btn_send_code.setClickable(false);
//			btn_send_code.setText(millisUntilFinished / 1000 + "秒");
		}
	}
}
