package com.sport365.badminton.utils;

import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MILLISECOND;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.SECOND;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Config;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Toast;

import com.actionbarsherlock.internal.nineoldandroids.animation.Animator;
import com.actionbarsherlock.internal.nineoldandroids.animation.Animator.AnimatorListener;
import com.actionbarsherlock.internal.nineoldandroids.animation.ObjectAnimator;
import com.sport365.badminton.R;
import com.sport365.badminton.dialog.CommonShowInfoDialog;
import com.sport365.badminton.dialog.HotelShowInfoDialogListener;
import com.sport365.badminton.params.SystemConfig;

/**
 * 业务逻辑工具类，包含动画
 * 
 */
public class Utilities {

	/** 链接色 */
	public static final String LINK_COLOR = "#0088cc";
	public static String FILE_ROOT = "";
	public static String JSON_FILE_ROOT = "";
	public static String JSON_FOREVER_FILE_ROOT = "";
	public static String SCREAM_VOICE_FILE_ROOT = "";
	private static String json_fileName = "json";
	private static String json_forEverName = "fejson";
	private static String scream_voice_file_name = "voice";
	public static String fileName = "TongCheng";

	public static String DATABASE_NAME = "tongchengpro.db";
	public static int DATABASE_VERSION = 3;
	static boolean DEBUG = true && Config.DEBUG;
	// if(DEBUG)
	public static double latitude = 0;
	public static double longitude = 0;
	public static String cityName;
	public static String district;
	public static String province;
	public static String street;
	public static long locationTime;// 记录定位时间
	public static String address;// 定位地址信息
	// 获取手机屏幕分辨率
	public static DisplayMetrics dm;

	// 列表页滑动动画
	private static Animation translate_foot_bottom_to_top,// 底部TAB显示动画
			translate_foot_top_to_bottom;// 底部TAB隐藏动画
	private static boolean inScroll = false;// 是否已经滑动过,动画是否已经结束
	private static View myTabViewBottom;
	private static Handler ticpkerHandler = new Handler();;
	private static Runnable animationRunnable = new Runnable() {
		@Override
		public void run() {
			if (myTabViewBottom != null) {
				if (myTabViewBottom.getVisibility() == View.GONE) {
					myTabViewBottom.clearAnimation();
					myTabViewBottom
							.startAnimation(translate_foot_bottom_to_top);
				}
			}
			ticpkerHandler.removeCallbacks(this);
		}
	};

	public static void CheckFileRoot(Application app) {
		/**
		 * 判断SD卡是否存在
		 */
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			// 存在的操作
			FILE_ROOT = Environment.getExternalStorageDirectory()
					.getAbsolutePath();
			FILE_ROOT = FILE_ROOT + File.separator + fileName + File.separator;
			new File(FILE_ROOT).mkdirs();
			FileTools.installPath = Utilities.FILE_ROOT + File.separator;
			// json
			JSON_FILE_ROOT = FILE_ROOT + json_fileName + File.separator;
			new File(JSON_FILE_ROOT).mkdirs();
			// 永久存储的json
			JSON_FOREVER_FILE_ROOT = FILE_ROOT + json_forEverName
					+ File.separator;
			new File(JSON_FOREVER_FILE_ROOT).mkdirs();
			// scream voice
			SCREAM_VOICE_FILE_ROOT = FILE_ROOT + scream_voice_file_name
					+ File.separator;
			new File(SCREAM_VOICE_FILE_ROOT).mkdirs();
		} else {
			// 存入内部路径
			FILE_ROOT = app.getDir("service_api_cache", Context.MODE_PRIVATE)
					.getAbsolutePath();
			FILE_ROOT = FILE_ROOT + File.separator + fileName + File.separator;
			new File(FILE_ROOT).mkdirs();
			FileTools.installPath = Utilities.FILE_ROOT + File.separator;
			// json
			JSON_FILE_ROOT = FILE_ROOT + json_fileName + File.separator;
			new File(JSON_FILE_ROOT).mkdirs();

			// 永久存储的json
			JSON_FOREVER_FILE_ROOT = FILE_ROOT + json_forEverName
					+ File.separator;
			// scream voice
			SCREAM_VOICE_FILE_ROOT = FILE_ROOT + scream_voice_file_name
					+ File.separator;
			new File(JSON_FOREVER_FILE_ROOT).mkdirs();
		}
		initEnv(app);
	}

	public static void initEnv(Application app) {
		SystemConfig.mNetWorkState = Tools.getNetworkState(app);
		SystemConfig.deviceId = Tools.getDeviceId();
		if (SystemConfig.deviceId == null || SystemConfig.deviceId.equals("")) {
			SystemConfig.deviceId = "00";
		}
	}

	public static void showToast(CharSequence message, Context context) {
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context.getApplicationContext(), message,
				duration);
		toast.show();
	}

	public static String getFieldValue(Object target, String fname) { // 获取字段值
		// 如:username 字段,getUsername()
		if (target == null || fname == null || "".equals(fname)) {// 如果类型不匹配，直接退出
			return "";
		}
		Class clazz = target.getClass();
		try { // 先通过getXxx()方法设置类属性值
			String methodname = "get" + Character.toUpperCase(fname.charAt(0))
					+ fname.substring(1);
			Method method = clazz.getDeclaredMethod(methodname); // 获取定义的方法
			if (!Modifier.isPublic(method.getModifiers())) { // 设置非共有方法权限
				method.setAccessible(true);
			}
			return (String) method.invoke(target); // 执行方法回调
		} catch (Exception me) {// 如果get方法不存在，则直接设置类属性值
			try {
				Field field = clazz.getDeclaredField(fname); // 获取定义的类属性
				if (!Modifier.isPublic(field.getModifiers())) { // 设置非共有类属性权限
					field.setAccessible(true);
				}
				return (String) field.get(target); // 获取类属性值

			} catch (Exception fe) {
			}
		}
		return "";
	}

	public static Object getFieldValueObj(Object target, String fname) { // 获取字段值
		// 如:username 字段,getUsername()
		if (target == null || fname == null || "".equals(fname)) {// 如果类型不匹配，直接退出
			return "";
		}
		Class clazz = target.getClass();
		try { // 先通过getXxx()方法设置类属性值
			String methodname = "get" + Character.toUpperCase(fname.charAt(0))
					+ fname.substring(1);
			Method method = clazz.getDeclaredMethod(methodname); // 获取定义的方法
			if (!Modifier.isPublic(method.getModifiers())) { // 设置非共有方法权限
				method.setAccessible(true);
			}
			return (Object) method.invoke(target); // 执行方法回调
		} catch (Exception me) {// 如果get方法不存在，则直接设置类属性值
			try {
				Field field = clazz.getDeclaredField(fname); // 获取定义的类属性
				if (!Modifier.isPublic(field.getModifiers())) { // 设置非共有类属性权限
					field.setAccessible(true);
				}
				return (Object) field.get(target); // 获取类属性值
			} catch (Exception fe) {
			}
		}
		return "";
	}

	public static void setFieldValue(Object target, String fname, Class ftype,
			Object fvalue) { // 设置字段值 如:username 字段,setUsername(String username)
		if (target == null
				|| fname == null
				|| "".equals(fname)
				|| (fvalue != null && !ftype
						.isAssignableFrom(fvalue.getClass()))) {// 如果类型不匹配，直接退出
			return;
		}
		Class clazz = target.getClass();
		try { // 先通过setXxx()方法设置类属性值
			String methodname = "set" + Character.toUpperCase(fname.charAt(0))
					+ fname.substring(1);
			Method method = clazz.getDeclaredMethod(methodname, ftype); // 获取定义的方法
			if (!Modifier.isPublic(method.getModifiers())) { // 设置非共有方法权限
				method.setAccessible(true);
			}
			method.invoke(target, fvalue); // 执行方法回调
		} catch (Exception me) {// 如果set方法不存在，则直接设置类属性值
			try {
				Field field = clazz.getDeclaredField(fname); // 获取定义的类属性
				if (!Modifier.isPublic(field.getModifiers())) { // 设置非共有类属性权限
					field.setAccessible(true);
				}
				field.set(target, fvalue); // 设置类属性值
			} catch (Exception fe) {

			}
		}
	}

	// 获取加密过的手机号
	public static String getEncryptPhone(String phone) {
		String result = "";
		if (phone.length() == 11) {
			String first = phone.substring(0, 3);
			String second = phone.substring(7, 11);
			result = first + "****" + second;
		} else {
			result = phone;
		}

		return result.toString();
	}

	// 从下面弹出背景的淡入的动画
	public static void showPopBg(final View ll_popupbg) {
		ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(ll_popupbg,
				"alpha", 0, 1).setDuration(500);
		objectAnimator.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator arg0) {
				ll_popupbg.setVisibility(View.VISIBLE);

			}

			@Override
			public void onAnimationRepeat(Animator arg0) {

			}

			@Override
			public void onAnimationEnd(Animator arg0) {

			}

			@Override
			public void onAnimationCancel(Animator arg0) {

			}
		});
		objectAnimator.start();
	}

	// 从下面弹出背景的淡出的动画
	public static void dismissPopBg(final View ll_popupbg) {
		ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(ll_popupbg,
				"alpha", 1, 0).setDuration(500);
		objectAnimator.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator arg0) {

			}

			@Override
			public void onAnimationRepeat(Animator arg0) {

			}

			@Override
			public void onAnimationEnd(Animator arg0) {
				ll_popupbg.setVisibility(View.GONE);

			}

			@Override
			public void onAnimationCancel(Animator arg0) {

			}
		});
		objectAnimator.start();

	}

	/**
	 * 从下面弹出背景的淡入的动画,在完成之前只能点击一次
	 * 
	 * @author Ruyan.Zhao 6045
	 * @since tongcheng_client_6.4 Jun 4, 2014 6:31:32 PM
	 */
	public static void showPopBg(final View ll_popupbg, final View view) {
		ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(ll_popupbg,
				"alpha", 0, 1).setDuration(500);
		objectAnimator.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator arg0) {
				ll_popupbg.setVisibility(View.VISIBLE);
				view.setClickable(false);
			}

			@Override
			public void onAnimationRepeat(Animator arg0) {

			}

			@Override
			public void onAnimationEnd(Animator arg0) {
				view.setClickable(true);

			}

			@Override
			public void onAnimationCancel(Animator arg0) {

			}
		});
		objectAnimator.start();
	}

	// 从下面弹出背景的淡出的动画
	/**
	 * 从下面弹出背景的淡出动画，动画完成前不可再次触发
	 * 
	 * @author Ruyan.Zhao 6045
	 * @since tongcheng_client_6.4 Jun 4, 2014 6:32:56 PM
	 */
	public static void dismissPopBg(final View ll_popupbg, final View view) {
		ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(ll_popupbg,
				"alpha", 1, 0).setDuration(500);
		objectAnimator.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator arg0) {
				view.setClickable(false);
			}

			@Override
			public void onAnimationRepeat(Animator arg0) {

			}

			@Override
			public void onAnimationEnd(Animator arg0) {
				ll_popupbg.setVisibility(View.GONE);
				view.setClickable(true);
			}

			@Override
			public void onAnimationCancel(Animator arg0) {

			}
		});
		objectAnimator.start();

	}

	/**
	 * 将日历清除时分秒和毫秒
	 * 
	 * @param cal
	 */
	public static void setMidnight(Calendar cal) {
		if (cal != null) {
			cal.set(HOUR_OF_DAY, 0);
			cal.set(MINUTE, 0);
			cal.set(SECOND, 0);
			cal.set(MILLISECOND, 0);
		}
	}

	/**
	 * 解压缩zip包
	 * 
	 * @author Ruyan.Zhao 6045
	 * @since tongcheng_client_6.0.1 2014-2-20 下午3:30:12
	 */
	public static void upZipFile(File zipFile, String folderPath)
			throws ZipException, IOException {
		if (!zipFile.exists()) {
			return;
		} else {
			ZipFile zf = new ZipFile(zipFile);
			InputStream inputStream;
			Enumeration en = zf.entries();
			while (en.hasMoreElements()) {
				ZipEntry zn = (ZipEntry) en.nextElement();
				if (!zn.isDirectory()) {
					inputStream = zf.getInputStream(zn);
					File f = new File(folderPath + zn.getName());
					File file = f.getParentFile();
					file.mkdirs();
					FileOutputStream outputStream = new FileOutputStream(
							folderPath + zn.getName());
					int len = 0;
					byte bufer[] = new byte[1024];
					while (-1 != (len = inputStream.read(bufer))) {
						outputStream.write(bufer, 0, len);
					}
					outputStream.close();
				}
			}
		}
	}

	/**
	 * 选择日期从价格日历回来后 现在在文本框的文案 是今天明天后天 或者具体日期 日期格式是dateFormat 决定 变成
	 * 今天明天后天的日期格式由dayFormat决定
	 * 
	 * @param calendar
	 * @param dateFormat
	 * @return
	 */
	public static String getDateCellValue(Calendar calendar,
			SimpleDateFormat dateFormat, SimpleDateFormat dayFormat) {
		String cellValue = getTodayFromDate(calendar.getTime());
		if (TextUtils.isEmpty(cellValue)) {
			cellValue = dateFormat.format(calendar.getTime());
		} else {
			cellValue = dayFormat.format(calendar.getTime()) + "  " + cellValue;
		}
		return cellValue;
	}

	/**
	 * 选择日期从价格日历回来后 现在在文本框的文案 是今天明天后天 或者具体日期 日期格式是yyyy-MM-dd EE 变成
	 * 今天明天后天的日期格式由dayFormat决定
	 * 
	 * @param calendar
	 * @return
	 */
	public static String getDateCellValueFormal(Calendar calendar) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd  EE");
		SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
		return getDateCellValue(calendar, dateFormat, dayFormat);
	}

	/**
	 * 为了提升查询效率，采用map的方式，这个方法是根据传入的日期来比较，得到一个key， 具体的算法是年的差值*10000+月份*100
	 * +日,这样就可以的到唯一的key
	 * 
	 * @param perDate
	 * @param currentDate
	 * @return
	 */
	public static int getDateKeyFromDate(Date perDate) {
		int dayofMonth = perDate.getDate();
		int month = perDate.getMonth() + 1;
		int year = perDate.getYear() - new Date().getYear();
		int mapDatekey = year * 10000 + month * 100 + dayofMonth;
		return mapDatekey;
	}

	/**
	 * 判断今天明天 和后天的逻辑
	 * 
	 * @param perDate
	 * @return
	 */
	public static String getTodayFromDate(Date perDate) {
		Date currentDate = new Date();
		Calendar tomorrowCalendar = Calendar.getInstance();
		tomorrowCalendar.add(Calendar.DAY_OF_MONTH, 1);

		Calendar houtianCalendar = Calendar.getInstance();
		houtianCalendar.add(Calendar.DAY_OF_MONTH, 2);

		int currentDateKey = getDateKeyFromDate(currentDate);
		int perDateKey = getDateKeyFromDate(perDate);
		int tomorrowDateKey = getDateKeyFromDate(tomorrowCalendar.getTime());
		int houtianDatekey = getDateKeyFromDate(houtianCalendar.getTime());

		if (currentDateKey == perDateKey) {
			return "今天";
		} else if (perDateKey == tomorrowDateKey) {
			return "明天";
		} else if (perDateKey == houtianDatekey) {
			return "后天";
		}
		return null;
	}

	public static boolean isCanSavePassengerName(String passengerName) {
		// 如果不包含英语字母
		int index = getFirstEnIndexFromString(passengerName);
		if (index == -1) {
			return true;
		}
		// 包含英语字母
		String tempPassengerName = passengerName.substring(index);
		int count = 0;
		String regEx = "[\\u4e00-\\u9fa5]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(tempPassengerName);
		while (m.find()) {
			for (int i = 0; i <= m.groupCount(); i++) {
				count = count + 1;
			}
		}
		if (count > 0) {
			// 含有汉字
			return false;
		} else {
			return true;
		}

	}

	/**
	 * 国际乘机人姓名匹配，只包含英文或"/",并且“/”只有一个，且不能在第一位和最后一位 符合要求返回true，否则返回false
	 * 
	 * @author Ruyan.Zhao 6045
	 * @since tongcheng_client_6.3 May 5, 2014 5:39:08 PM
	 */
	public static boolean isCanSaveInterPassengerName(String passengerName) {
		int count = 0;
		String regEx = "[a-zA-Z]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(passengerName);
		while (m.find()) {
			for (int i = 0; i <= m.groupCount(); i++) {
				count = count + 1;
			}
		}
		if (count == passengerName.length()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 是否包含字母
	 * 
	 * @param str
	 * @return
	 */
	public static int getFirstEnIndexFromString(String str) {
		int index = -1;
		String regex = ".*[a-zA-Z]+.*";
		Matcher m = Pattern.compile(regex).matcher(str);
		if (m.matches()) {
			// 如果匹配 就是含有英语字母
			if (!TextUtils.isEmpty(str)) {
				for (int i = 0; i < str.length(); i++) {
					int c = (int) str.charAt(i);
					Log.e("c", c + "");
					if ((c >= 65) && (c <= 90) || (c >= 97) && (c <= 122)) {
						// 包含字母
						index = i;
						break;
					}
				}
			}
		}
		return index;
	}

	/**
	 * 显示提示框的公共方法
	 * 
	 * @param tip
	 *            原因
	 */
	public static void displayDialPhoneDialog(final Activity activity,
			String tip) {
		String phoneTip = activity.getString(R.string.show_phone_tip);
		CommonShowInfoDialog dialog = new CommonShowInfoDialog(activity,
				new HotelShowInfoDialogListener() {
					@Override
					public void refreshUI(String sType) {
						if (sType.equals(HotelShowInfoDialogListener.BTN_LEFT)) {

						} else if (sType
								.equals(HotelShowInfoDialogListener.BTN_RIGHT)) {
							Tools.dailTcPhone(activity,
									activity.getString(R.string.phone_number));
						}
					}
				}, View.VISIBLE, tip + "\n" + phoneTip, "取消", "确定拨打");
		dialog.showdialog();
	}

}
