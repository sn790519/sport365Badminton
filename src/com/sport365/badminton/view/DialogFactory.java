package com.sport365.badminton.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.sport365.badminton.R;

public class DialogFactory {
    private Context context;
    private Dialog dialog;

    public DialogFactory(Context context) {
        this.context = context;
    }

    public boolean isShowing() {
        return (null != dialog) && dialog.isShowing();
    }

    public void dismiss() {
        if (isShowing()) {
            dialog.dismiss();
        }
    }

    /**
     * 设置对话框在屏幕的宽度比例
     */
    private void setDialogWidth(Context context, Dialog dialog, double width) {
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // 竖屏 竖屏的时候取屏幕宽度，作为对话框的宽度基础值*比例
            lp.width = (int) (display.getWidth() * width); // 设置宽度
        } else {// 横屏 横屏的时候取高度
            lp.width = (int) (display.getHeight() * width); // 设置宽度
        }
        dialog.getWindow().setAttributes(lp);
    }

    public void showToast(String text, int duration) {
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }


    public Dialog getDialog() {
        return dialog;
    }

    /**
     * 一个按钮
     *
     * @param title           标题
     * @param content         提示内容
     * @param okContent       dialog左边按钮内容
     * @param onClickListener
     * @param isCancelable
     */
    public void showDialog(String title, String content, String okContent, final onBtnClickListener onClickListener, boolean isCancelable) {
        showDialog3Btn(title, content, okContent, null, null, onClickListener, isCancelable);
    }

    public void showDialog(String title, String content, String okContent, final onBtnClickListener onClickListener) {
        showDialog(title, content, okContent, onClickListener, false);
    }

    /**
     * 2个按钮
     *
     * @param title           标题
     * @param content         提示内容
     * @param ok              dialog左边按钮内容
     * @param cancle          dialog右边按钮内容
     * @param onClickListener 点击事件
     * @param isCancelable    能否被返回键取消
     */
    public void showDialog2Btn(String title, String content, String ok, String cancle, final onBtnClickListener onClickListener, boolean isCancelable) {
        showDialog3Btn(title, content, ok, null, cancle, onClickListener, isCancelable);
    }

    public void showDialog2Btn(String title, String content, String ok, String cancle, final onBtnClickListener onClickListener) {
        this.showDialog2Btn(title, content, ok, cancle, onClickListener, false);
    }

    /**
     * @param title
     * @param content
     */
    public void showDialogWithClose(String title, String content) {
        onBtnClickListener onClickListener = new onBtnClickListener() {
            @Override
            public void btnLeftClickListener(View v) {

            }

            @Override
            public void btnNeutralClickListener(View v) {

            }

            @Override
            public void btnRightClickListener(View v) {

            }

            @Override
            public void btnCloseClickListener(View v) {
                if (null != dialog & dialog.isShowing())
                    dialog.dismiss();
            }
        };
        this.showDialog3Btn(title, content, null, null, null, onClickListener, true);
    }

    /**
     * 3个按钮
     *
     * @param title              提示标题
     * @param content            提示内容
     * @param btn_left_content   dialog左边确定按钮文字
     * @param btn_center_content dialog中间自定义按钮文字
     * @param btn_right_content  dialog右边取消按钮文字
     * @param onClickListener    点击事件
     * @param isCancelable       能否响应返回键
     */
    public void showDialog3Btn(String title, final String content, String btn_left_content, String btn_center_content, String btn_right_content, final onBtnClickListener onClickListener,
                               boolean isCancelable) {
        dialog = new Dialog(context, R.style.dialog);
        dialog.setCancelable(isCancelable);
        dialog.setContentView(R.layout.dialog_factory_button);
        Button btn_left = (Button) dialog.findViewById(R.id.dialog_factory_bt_opt);// 左一
        Button btn_Cancel = (Button) dialog.findViewById(R.id.dialog_factory_bt_close);// 右上角

        Button btn_center = (Button) dialog.findViewById(R.id.dialog_factory_bt_ok);// 中间
        Button btn_right = (Button) dialog.findViewById(R.id.dialog_factory_bt_cancle);// 右一

        TextView tvContent = (TextView) dialog.findViewById(R.id.dialog_factory_tv_content);
        tvContent.setMovementMethod(new ScrollingMovementMethod());
        TextView tvTitle = (TextView) dialog.findViewById(R.id.dialog_factory_tv_title);

        tvTitle.setText(title);


        if (TextUtils.isEmpty(btn_left_content) && TextUtils.isEmpty(btn_center_content) && TextUtils.isEmpty(btn_right_content)) {
            LinearLayout ll_bottom = (LinearLayout) dialog.findViewById(R.id.ll_bottom);
            ll_bottom.setVisibility(View.GONE);
        }


        if (tvContent != null) {
            tvContent.setText((null == content) ? "" : content);
        }

        if (TextUtils.isEmpty(btn_left_content)) {
            btn_left.setVisibility(View.GONE);
        } else {
            btn_left.setVisibility(View.VISIBLE);
            btn_left.setText(TextUtils.isEmpty(btn_left_content) ? context.getString(R.string.ok) : btn_left_content);
        }


        if (btn_center_content != null) {
            btn_center.setText(btn_center_content);
            btn_center.setVisibility(View.VISIBLE);
        } else {
            btn_center.setVisibility(View.GONE);
        }
        if (btn_right_content != null) {
            btn_right.setText(btn_right_content);
            btn_right.setVisibility(View.VISIBLE);
        } else {
            btn_right.setVisibility(View.GONE);
        }
        if (btn_right_content == null && btn_center_content == null) {
            btn_left.setBackgroundResource(R.color.orangered);
        }

        btn_left.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (onClickListener != null)
                    onClickListener.btnLeftClickListener(v);
                dialog.dismiss();
            }
        });
        if (btn_center_content != null) {
            btn_center.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (onClickListener != null)
                        onClickListener.btnNeutralClickListener(v);
                    dialog.dismiss();
                }
            });
        }

        if (btn_right_content != null) {
            btn_right.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (onClickListener != null)
                        onClickListener.btnRightClickListener(v);
                    dialog.dismiss();
                }
            });
        }

        btn_Cancel.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (onClickListener != null)
                    onClickListener.btnCloseClickListener(v);
                dialog.dismiss();
            }
        });

        if (context instanceof Activity && !((Activity) context).isFinishing())
            setDialogWidth(context, dialog, 0.9);
        else
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.show();

    }


    public interface onBtnClickListener {
        /**
         * 左边确认按钮
         *
         * @param v
         */
        public abstract void btnLeftClickListener(View v);

        /**
         * dialog中间自定义按钮
         *
         * @param v
         */
        public abstract void btnNeutralClickListener(View v);

        /**
         * dialog最右边按钮
         *
         * @param v
         */
        public abstract void btnRightClickListener(View v);

        /**
         * 右上角关闭
         *
         * @param v
         */
        public abstract void btnCloseClickListener(View v);
    }


}
