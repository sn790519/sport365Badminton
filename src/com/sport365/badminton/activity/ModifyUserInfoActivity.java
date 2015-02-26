package com.sport365.badminton.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.sport365.badminton.BaseActivity;
import com.sport365.badminton.R;
import com.sport365.badminton.view.wheelwidget.ArrayWheelAdapter;
import com.sport365.badminton.view.wheelwidget.WheelView;

/**
 * 修改信息
 */
public class ModifyUserInfoActivity extends BaseActivity implements View.OnClickListener {
	private Button btn_login;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_user_info);
		initActionBar();
		findViews();
	}

	private void initActionBar() {
		setActionBarTitle("修改个人信息");
		mActionbar_right.setVisibility(View.GONE);
		mActionbar_right_text.setVisibility(View.GONE);
	}

	private void findViews() {
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_login.setOnClickListener(listener);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);

		switch (v.getId()) {
			case R.id.btn_login:
				break;
		}
	}


	//初始化时汉字会挤压，在汉字左右两边添加空格即可
	private String[] names = {"浙江","江苏","山东"," 江西 "," 湖南 "," 广东 "};

	private View.OnClickListener listener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			final AlertDialog dialog = new AlertDialog.Builder(ModifyUserInfoActivity.this).create();
			dialog.setTitle("选择分类");
			final WheelView catalogWheel = new WheelView(ModifyUserInfoActivity.this);
			dialog.setButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					// 实现下ui的刷新
				}
			});
			dialog.setButton2("取消", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});

			catalogWheel.setVisibleItems(5);
			catalogWheel.setCyclic(true);
			catalogWheel.setAdapter(new ArrayWheelAdapter<String>(names));
			dialog.setView(catalogWheel);
			dialog.show();
		}
	};

}
