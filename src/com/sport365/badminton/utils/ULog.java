package com.sport365.badminton.utils;

import java.util.Locale;

import com.sport365.badminton.params.Systemconfig;

public class ULog {
	public static String	TAG	= "com.sport365.badminton";

	/**
	 * 设置日志输出标记
	 * 
	 * @param tag
	 *            日志标记
	 */
	public static void setTag(String tag) {
		debug(tag, "Changing log tag to %s", tag);
		TAG = tag;
	}

	/**
	 * 输出verbose级别日志
	 * 
	 * @param format
	 *            日志格式
	 * @param args
	 *            替换参数
	 */
	public static void verbose(String format, Object... args) {
		if (Systemconfig.DEBUG) {
			android.util.Log.v(TAG, buildMessage(format, args));
		}
	}

	/**
	 * 输出debug级别日志
	 * 
	 * @param format
	 *            日志格式
	 * @param args
	 *            替换参数
	 */
	public static void debug(String format, Object... args) {
		if (Systemconfig.DEBUG) {
			android.util.Log.d(TAG, buildMessage(format, args));
		}
	}

	/**
	 * 输出info级别日志
	 * 
	 * @param format
	 *            日志格式
	 * @param args
	 *            替换参数
	 */
	public static void info(String format, Object... args) {
		if (Systemconfig.DEBUG) {
			android.util.Log.i(TAG, buildMessage(format, args));
		}
	}

	/**
	 * 输出error级别日志
	 * 
	 * @param format
	 *            日志格式
	 * @param args
	 *            替换参数
	 */
	public static void error(String format, Object... args) {
		String msg = buildMessage(format, args);
		android.util.Log.e(TAG, msg);
	}

	/**
	 * 输出error级别日志
	 * 
	 * @param tr
	 *            异常
	 * @param format
	 *            日志格式
	 * @param args
	 *            替换参数
	 */
	public static void error(Throwable tr, String format, Object... args) {
		String msg = buildMessage(format, args);
		android.util.Log.e(TAG, msg, tr);
	}

	/**
	 * 输出warn级别日志
	 * 
	 * @param format
	 *            日志格式
	 * @param args
	 *            替换参数
	 */
	public static void warn(String format, Object... args) {
		android.util.Log.w(TAG, buildMessage(format, args));
	}

	/**
	 * Formats the caller's provided message and prepends useful info like
	 * calling thread ID and method name.
	 */
	private static String buildMessage(String format, Object... args) {
		String msg = (args == null || (args != null && args.length <= 0)) ? format : String.format(Locale.US, format, args);
		StackTraceElement[] trace = new Throwable().fillInStackTrace().getStackTrace();

		String caller = "<unknown>";
		// Walk up the stack looking for the first caller outside of VolleyLog.
		// It will be at least two frames up, so start there.
		for (int i = 2; i < trace.length; i++) {
			Class<?> clazz = trace[i].getClass();
			if (!clazz.equals(ULog.class)) {
				String callingClass = trace[i].getClassName();
				callingClass = callingClass.substring(callingClass.lastIndexOf('.') + 1);
				callingClass = callingClass.substring(callingClass.lastIndexOf('$') + 1);
				caller = callingClass + "." + trace[i].getMethodName();
				break;
			}
		}
		return String.format(Locale.US, "[%d] %s: %s", Thread.currentThread().getId(), caller, msg);
	}
}
