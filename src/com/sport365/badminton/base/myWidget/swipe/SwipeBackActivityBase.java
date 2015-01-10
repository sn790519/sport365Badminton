package com.sport365.badminton.base.myWidget.swipe;

/**
 * 滑动返回接口
 * @author yy09011
 */
public interface SwipeBackActivityBase {
    public abstract SwipeBackLayout getSwipeBackLayout();
    /**
     * 设置开启滑动返回
     * @param enable
     */
    public abstract void setSwipeBackEnable(boolean enable);

    /**
     * 滑动并关闭activity
     */
    public abstract void scrollToFinishActivity();

}
