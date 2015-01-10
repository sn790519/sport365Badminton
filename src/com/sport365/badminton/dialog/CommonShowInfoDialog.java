package com.sport365.badminton.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sport365.badminton.R;

/**
 * 显示信息对话框
 * @author Tc003167
 *
 */
public class CommonShowInfoDialog extends Dialog implements OnClickListener {
	
	private Context context;
	private HotelShowInfoDialogListener listener;
	private TextView tv_dialog_content,tv_button_line,percentTV,tv_line;
	private LinearLayout ll_dialog_btn;
	private Button btn_dialog_left, btn_dialog_right;
	private ProgressBar progressBar;
	private LinearLayout progressBarLayout;
	
	private int vis = View.GONE;//按钮是否可见
	private CharSequence content;//展示内容
	private String strLeftBtn, strRightBtn;//按钮信息
	private ImageButton imgbtn_guanbi;
	private int contentGravity = Gravity.LEFT; // 提示文案的对齐方式	默认左对齐
	private boolean rightCancle=true;//右键按钮点击是否关闭弹框
	
	public CommonShowInfoDialog(Context context) {
		super(context, R.style.MessageBox);
		this.context = context;
	}
	
	public void setCloseBtnGone(){//不可见
		imgbtn_guanbi.setVisibility(View.INVISIBLE) ;
	}

	public CommonShowInfoDialog(Context context,
                                int vis, CharSequence content, String strLeft, String strRight) {
		this(context);
		this.context = context;
		this.vis = vis;
		this.content = content;
		this.strLeftBtn = strLeft;
		this.strRightBtn = strRight;
	}
	
	public CommonShowInfoDialog(Context context, HotelShowInfoDialogListener listener,
                                int vis, CharSequence content, String strLeft, String strRight) {
		this(context);
		this.context = context;
		this.listener = listener;
		this.vis = vis;
		this.content = content;
		this.strLeftBtn = strLeft;
		this.strRightBtn = strRight;
	}
	
	public CommonShowInfoDialog(Context context, HotelShowInfoDialogListener listener,
                                int vis, CharSequence content, String strRight) {
		this(context);
		this.context = context;
		this.listener = listener;
		this.vis = vis;
		this.content = content;
		this.strLeftBtn = "";
		this.strRightBtn = strRight;
    }
	/**
	 * 设置显示内容
	 * @param content
	 */
	public void setContent(String content)
	{
		this.content=content;
	}
	
	public void showdialog() {

		getWindow().setWindowAnimations(R.style.centerDialogWindowAnim);
		// 设置窗口弹出动画
//		setCanceledOnTouchOutside(true);
		WindowManager.LayoutParams wl = getWindow().getAttributes();
		wl.gravity = Gravity.CENTER; 
		getWindow().setAttributes(wl);
		getWindow().setBackgroundDrawableResource(android.R.color.transparent) ;
		show();
	}
	
	// 需要设置提示文案的对其方式用这个方法
	public void showdialog(int gravity) {
		contentGravity = gravity;
		getWindow().setWindowAnimations(R.style.centerDialogWindowAnim);
		// 设置窗口弹出动画
//		setCanceledOnTouchOutside(true);
		WindowManager.LayoutParams wl = getWindow().getAttributes();
		wl.gravity = Gravity.CENTER; 
		getWindow().setAttributes(wl);
		getWindow().setBackgroundDrawableResource(android.R.color.transparent) ;
		show();
	}
	
	public void showdialogWithoutClose() {
		showdialog();
		if(imgbtn_guanbi!=null)
			imgbtn_guanbi.setVisibility(View.INVISIBLE);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_hotel_info_show);
		initUI();
	}
	
	private void initUI() {
		tv_dialog_content = (TextView) findViewById(R.id.tv_dialog_content);
		ll_dialog_btn = (LinearLayout) findViewById(R.id.ll_dialog_btn);
		tv_button_line = (TextView) findViewById(R.id.tv_button_line);
		tv_line  = (TextView) findViewById(R.id.tv_line);
		btn_dialog_left = (Button) findViewById(R.id.btn_dialog_left);
		btn_dialog_right = (Button) findViewById(R.id.btn_dialog_right);
		progressBarLayout=(LinearLayout) findViewById(R.id.progress_layout);
		progressBar=(ProgressBar) findViewById(R.id.progressbar);
		percentTV=(TextView) findViewById(R.id.progress_percent);
		btn_dialog_left.setOnClickListener(this);
		btn_dialog_right.setOnClickListener(this);
		imgbtn_guanbi = (ImageButton) findViewById(R.id.imgbtn_guanbi);
		imgbtn_guanbi.setOnClickListener(this);
		
		if (!TextUtils.isEmpty(content)) {
			tv_dialog_content.setText(content);
		}
		
		ll_dialog_btn.setVisibility(vis);
		if (vis == View.VISIBLE) {
			if ("".equals(strLeftBtn)) {
				btn_dialog_left.setVisibility(View.GONE);
                tv_button_line.setVisibility(View.GONE);
			} else
				btn_dialog_left.setText(strLeftBtn);
			
			btn_dialog_right.setText(strRightBtn);	
			// 有button按钮的时候隐藏关闭按钮
			imgbtn_guanbi.setVisibility(View.GONE);
		} else {
			tv_line.setVisibility(View.GONE);
		}
		setContentGravity(contentGravity);
	}

	@Override
	public void onClick(View v) {
		if (btn_dialog_left == v) {
			if (listener != null) {
				listener.refreshUI(HotelShowInfoDialogListener.BTN_LEFT);
			}
		} else if (btn_dialog_right == v) {
			if (listener != null) {
				listener.refreshUI(HotelShowInfoDialogListener.BTN_RIGHT);
			}
			if(isRightCancle())
				cancel();
			else
				return;
		} else if (imgbtn_guanbi == v) {
			if (listener != null) {
				listener.refreshUI(HotelShowInfoDialogListener.BTN_CANCEL);
			}
		}
		cancel();
	}
	
	/**
	 * 设置提示内容的对齐方式
	 * @param gravity
	 */
	public void setContentGravity(int gravity) {
		tv_dialog_content.setGravity(gravity);
	}
	
	
	public HotelShowInfoDialogListener getListener() {
		return listener;
	}

	public void setListener(HotelShowInfoDialogListener listener) {
		this.listener = listener;
	}

	public boolean isRightCancle() {
		return rightCancle;
	}

	/**
	 * 设置右键按钮点击后是否关闭弹框
	 * @param rightCancle <br/>
	 * 	true ：关闭  <br/>
	 * false ：不关闭
	 */
	public void setRightCancle(boolean rightCancle) {
		this.rightCancle = rightCancle;
	}

	/**
	 * 显示进度条
	 */
	public void showProgress(){
		progressBarLayout.setVisibility(View.VISIBLE);
	}
	
	/**
	 * 更新进度条
	 * @param percent
	 */
	public void setProgress(int percent){
		progressBar.setProgress(percent);
		percentTV.setText(percent+"%");
	}
	
	/**
	 * 设置右键的文案
	 * @param text
	 */
	public void setRightBtn(String text){
		btn_dialog_right.setText(text);
	}
	/**
	 * 设置右键的文案
	 * @param text
	 */
	public void setLeftBtn(String text){
		btn_dialog_left.setText(text);
	}

	public Button getBtn_dialog_right() {
		return btn_dialog_right;
	}

	public void setBtn_dialog_right(Button btn_dialog_right) {
		this.btn_dialog_right = btn_dialog_right;
	}
	
	public void setRightBtnTextColor(int id){
		if(btn_dialog_right!=null){
			this.btn_dialog_right.setTextColor(context.getResources().getColor(id));
		}
	}
	
}
