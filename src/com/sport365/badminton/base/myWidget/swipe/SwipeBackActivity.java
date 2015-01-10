
package com.sport365.badminton.base.myWidget.swipe;

import android.os.Bundle;
import android.view.View;

import com.sport365.badminton.base.BaseActionBarActivity;
/**
 * 滑动返回BaseFlipActivity所继承的类
 * 不支持TabActivity
 * @author yy09011
 * @since 7.1
 *
 */
public class SwipeBackActivity extends BaseActionBarActivity implements SwipeBackActivityBase {
    private SwipeBackActivityHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper = new SwipeBackActivityHelper(this);
        mHelper.onActivityCreate();
    }
    
    @Override
	protected void onCreate(Bundle savedInstanceState, int actionbar_resource_id) {
		super.onCreate(savedInstanceState, actionbar_resource_id);
		mHelper = new SwipeBackActivityHelper(this);
        mHelper.onActivityCreate();
	}
    /**
     * 修改兼容 actionbar_resource_id onCreate创建
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if(mHelper!=null)
        {
        	mHelper.onPostCreate();
        }
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null && mHelper != null)
            return mHelper.findViewById(id);
        return v;
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    public void scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }
}
