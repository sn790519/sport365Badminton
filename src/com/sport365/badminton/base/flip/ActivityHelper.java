package com.sport365.badminton.base.flip;

import android.app.Activity;

/**
 * 自定义activity之间切换
 * 
 * @author Ruyan.Zhao 6045
 * @since tongcheng_client_5.2
 * 
 * @version tongcheng_client_7.1
 * 去掉接口，直接使用activity方法
 * @author yy09011
 */
public class ActivityHelper {

	/**
	 * 自定义activity之间切换的动画效果
	 * @param
	 * activity 目标activity
	 * @param 
	 * enterAnim 打开这个activiry的动画效果的resource ID，0表示没有动画.
	 * @param
	 * exitAnim 退出退个activiry的动画效果的resource ID，0表示没有动画.
	 */
    public static void overridePendingTransition(Activity activity, int enterAnim, int exitAnim) {
    	activity.overridePendingTransition(enterAnim, exitAnim);
    }

}
