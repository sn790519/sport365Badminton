package com.sport365.badminton.utils;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.widget.Toast;
import com.sport365.badminton.params.SystemConfig;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Calendar;
import java.util.Date;

public class Utilities {

	public static String FILE_ROOT = "";
	public static String JSON_FILE_ROOT = "";
	public static String JSON_FOREVER_FILE_ROOT = "";
	public static String SCREAM_VOICE_FILE_ROOT = "";
	private static String json_fileName = "json";
	private static String json_forEverName = "fejson";
	private static String scream_voice_file_name = "voice";
	public static String fileName = "badminton";


	public static void CheckFileRoot(Application app) {
		/**
		 * 判断SD卡是否存在
		 */
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			// 存在的操作
			FILE_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath();
			FILE_ROOT = FILE_ROOT + File.separator + fileName + File.separator;
			new File(FILE_ROOT).mkdirs();
			FileTools.installPath = Utilities.FILE_ROOT + File.separator;
			// json
			JSON_FILE_ROOT = FILE_ROOT + json_fileName + File.separator;
			new File(JSON_FILE_ROOT).mkdirs();
			// 永久存储的json
			JSON_FOREVER_FILE_ROOT = FILE_ROOT + json_forEverName + File.separator;
			new File(JSON_FOREVER_FILE_ROOT).mkdirs();
			// scream voice
			SCREAM_VOICE_FILE_ROOT = FILE_ROOT + scream_voice_file_name + File.separator;
			new File(SCREAM_VOICE_FILE_ROOT).mkdirs();
		} else {
			// 存入内部路径
			FILE_ROOT = app.getDir("service_api_cache", Context.MODE_PRIVATE).getAbsolutePath();
			FILE_ROOT = FILE_ROOT + File.separator + fileName + File.separator;
			new File(FILE_ROOT).mkdirs();
			FileTools.installPath = Utilities.FILE_ROOT + File.separator;
			// json
			JSON_FILE_ROOT = FILE_ROOT + json_fileName + File.separator;
			new File(JSON_FILE_ROOT).mkdirs();

			// 永久存储的json
			JSON_FOREVER_FILE_ROOT = FILE_ROOT + json_forEverName + File.separator;
			// scream voice
			SCREAM_VOICE_FILE_ROOT = FILE_ROOT + scream_voice_file_name + File.separator;
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

		Toast toast = Toast.makeText(context.getApplicationContext(), message, duration);
		toast.show();
	}

	public static String getFieldValue(Object target, String fname) { // 获取字段值
		// 如:username 字段,getUsername()
		if (target == null || fname == null || "".equals(fname)) {// 如果类型不匹配，直接退出
			return "";
		}
		Class clazz = target.getClass();
		try { // 先通过getXxx()方法设置类属性值
			String methodname = "get" + Character.toUpperCase(fname.charAt(0)) + fname.substring(1);
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
			String methodname = "get" + Character.toUpperCase(fname.charAt(0)) + fname.substring(1);
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

	public static void setFieldValue(Object target, String fname, Class ftype, Object fvalue) { // 设置字段值
		// 如:username
		// 字段,setUsername(String
		// username)
		if (target == null || fname == null || "".equals(fname) || (fvalue != null && !ftype.isAssignableFrom(fvalue.getClass()))) {// 如果类型不匹配，直接退出
			return;
		}
		Class clazz = target.getClass();
		try { // 先通过setXxx()方法设置类属性值
			String methodname = "set" + Character.toUpperCase(fname.charAt(0)) + fname.substring(1);
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


	/**
	 * 为了提升查询效率，采用map的方式，这个方法是根据传入的日期来比较，得到一个key， 具体的算法是年的差值*10000+月份*100
	 * +日,这样就可以的到唯一的key
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


}
