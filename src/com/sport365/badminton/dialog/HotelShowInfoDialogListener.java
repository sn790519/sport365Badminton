package com.sport365.badminton.dialog;

/**
 * 酒店信息显示 回调消息
 * @author Tc003167
 *
 */
public interface HotelShowInfoDialogListener {
	public static String BTN_LEFT = "BTN_LEFT";
	public static String BTN_RIGHT = "BTN_RIGHT";
	public static String BTN_CANCEL = "BTN_CANCEL";
    public void refreshUI(String sType);
}
