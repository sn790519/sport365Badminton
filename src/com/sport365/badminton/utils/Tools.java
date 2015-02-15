package com.sport365.badminton.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import com.sport365.badminton.BaseApplication;
import com.sport365.badminton.params.SystemConfig;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.util.Enumeration;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Tools {

	private static final int BUFFER = 1024;

	public static final int NETWORN_NONE = 3;
	public static final int NETWORN_WIFI = 1;
	public static final int NETWORN_MOBILE = 2;
	public static final int NETWORN_OTHER = 0;

	/**
	 * 获得手机的DeviceId
	 *
	 * @return
	 */
	@SuppressWarnings("finally")
	public static String getDeviceId() {
		String deviceId = SystemConfig.deviceId;
		if (TextUtils.isEmpty(deviceId) || "00".equals(deviceId)) {
			try {
				Context context = BaseApplication.getInstance().getApplicationContext();
				// 先获取保存的deviceid
				deviceId = SharedPreferencesUtils.getInstance(context).getString(SharedPreferencesKeys.DEVICEID, "");
				if (TextUtils.isEmpty(deviceId)) {
					// 先获取androidid
					deviceId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
					// 在主流厂商生产的设备上，有一个很经常的bug，
					// 就是每个设备都会产生相同的ANDROID_ID：9774d56d682e549c
					if (TextUtils.isEmpty(deviceId) || "9774d56d682e549c".equals(deviceId)) {
						TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
						deviceId = telephonyManager.getDeviceId();
					}
					if (TextUtils.isEmpty(deviceId)) {
						deviceId = UUID.randomUUID().toString();
						deviceId = deviceId.replaceAll("-", "");
					}
					SharedPreferencesUtils.getInstance(context).putString(SharedPreferencesKeys.DEVICEID, deviceId);
					SharedPreferencesUtils.getInstance(context).commitValue();
				}
			} catch (Exception e) {
				deviceId = "00";
			} finally {
				return deviceId;
			}
		}
		if (TextUtils.isEmpty(deviceId)) {
			deviceId = "00";
		}
		return deviceId;
	}

	/**
	 * 加密手机号中间显示****
	 */
	public static String encryptMobileNumber(String mobile) {
		if (mobile.length() == 11) {
			String start = mobile.substring(0, 3);
			String end = mobile.substring(7);
			mobile = start + "****" + end;
		}
		return mobile;
	}

	/**
	 * 数据压缩
	 *
	 * @param is
	 * @param os
	 * @throws Exception
	 */
	public static void compress(InputStream is, OutputStream os) throws Exception {
		GZIPOutputStream gos = new GZIPOutputStream(os);
		int count;
		byte data[] = new byte[BUFFER];
		while ((count = is.read(data, 0, BUFFER)) != -1) {
			gos.write(data, 0, count);
		}
		gos.finish();
		gos.flush();
		gos.close();
	}

	/**
	 * 数据压缩
	 *
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static byte[] compress(String str) throws IOException {
		if (str == null || str.length() == 0) {
			return null;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip = new GZIPOutputStream(out);
		gzip.write(str.getBytes("UTF-8"));
		gzip.close();
		return out.toByteArray();
	}

	/**
	 * 数据压缩
	 *
	 * @param bt
	 * @return
	 * @throws Exception
	 */
	public static byte[] compress(byte[] bt) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip = new GZIPOutputStream(out);
		gzip.write(bt);
		gzip.close();
		return out.toByteArray();
	}

	/**
	 * 数据解压缩
	 */
	public static byte[] decompress(InputStream is) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// 解压缩
		decompress(is, baos);
		byte[] data = baos.toByteArray();
		baos.flush();
		baos.close();
		is.close();
		return data;
	}

	/**
	 * 数据解压缩
	 *
	 * @param is
	 * @param os
	 * @throws Exception
	 */
	public static void decompress(InputStream is, OutputStream os) throws Exception {
		GZIPInputStream gis = new GZIPInputStream(is);
		int count;
		byte data[] = new byte[BUFFER];
		while ((count = gis.read(data, 0, BUFFER)) != -1) {
			os.write(data, 0, count);
		}
		gis.close();
	}

	/**
	 * 直接拨打电话
	 */
	public static void dailPhoneNoAsk(Context context) {
		try {
			Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:4007991555"));
			context.startActivity(intent);
		} catch (Exception e) {
			Utilities.showToast("R.string.err_phone_tip", context);
		}
	}

	/**
	 * 用来获取手机拨号上网（包括CTWAP和CTNET）时由PDSN分配给手机终端的源IP地址。
	 *
	 * @return
	 * @author SHANHY
	 */
	public static String getPsdnIp() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (Exception e) {

		}
		return "127.0.0.1";
	}


	// 解决TextView排版问题
	public static String ToDBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}


	/**
	 * 把一个url的网络图片变成一个本地的BitMap
	 *
	 * @param url
	 * @return
	 */
	public static Bitmap returnBitMap(String url) {
		URL myFileUrl = null;
		Bitmap bitmap = null;
		InputStream is = null;
		try {
			myFileUrl = new URL(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
			conn.setDoInput(true);
			conn.connect();
			is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return bitmap;
	}

	// 判断当前设备是否是模拟器。如果返回TRUE，则当前是模拟器，不是返回FALSE
	public static boolean isEmulator(Context context) {
		try {
			TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			String imei = tm.getDeviceId();
			if (imei != null && imei.equals("000000000000000")) {
				return true;
			}
			return (Build.MODEL.equals("sdk")) || (Build.MODEL.equals("google_sdk"));
		} catch (Exception ioe) {
		}
		return false;
	}

	/**
	 * 1:大图 720p 2：中图 480p 6：高清图 1080p
	 */
	public static int getDisplayMetrics(Activity activity) {
		DisplayMetrics mDisplayMetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
		int width = mDisplayMetrics.widthPixels;
		int imageSizeType = 1;
		if (width <= 540) {
			imageSizeType = 2;
		} else if (width >= 1080) {
			imageSizeType = 6;
		}
		return imageSizeType;
	}

	/**
	 * 将sp值转换为px值，保证文字大小不变
	 *
	 * @param spValue
	 * @param fontScale （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	/**
	 * px 转sp
	 *
	 * @param the size of text in pixels
	 * @return
	 * @author yj6299 Aaron
	 */
	public static int px2sp(Context context, float pxValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);// 小数点四舍五入取整
	}


	/**
	 * Check whether network is connected currently.
	 *
	 * @param context application context
	 * @return return true if network is connected, otherwise return false.
	 */
	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * Reports the type of network to which the info in this {@code NetworkInfo}
	 * pertains.
	 *
	 * @return one of {@link ConnectivityManager#TYPE_MOBILE},
	 * {@link ConnectivityManager#TYPE_WIFI},
	 * {@link ConnectivityManager#TYPE_WIMAX},
	 * {@link ConnectivityManager#TYPE_ETHERNET},
	 * {@link ConnectivityManager#TYPE_BLUETOOTH}, or other types
	 * defined by {@link ConnectivityManager}
	 */
	public static int getConnectedType(Context context) {
		ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
		if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
			return mNetworkInfo.getType();
		}
		return -1;
	}

	/**
	 * 获取网络状态
	 *
	 * @param context
	 * @return
	 */
	public static int getNetworkState(Context context) {
		if (isNetworkConnected(context)) {
			int netWorkType = getConnectedType(context);
			if (netWorkType == ConnectivityManager.TYPE_WIFI) {
				return NETWORN_WIFI;
			} else if (netWorkType == ConnectivityManager.TYPE_MOBILE) {
				return NETWORN_MOBILE;
			} else {
				return NETWORN_OTHER;
			}
		} else {
			return NETWORN_NONE;
		}
	}

	/**
	 * 获取对应网络名称
	 *
	 * @param context
	 * @return
	 */
	public static String getNetworkStateName(Context context) {
		if (isNetworkConnected(context)) {
			int netWorkType = getConnectedType(context);
			if (netWorkType == ConnectivityManager.TYPE_WIFI) {
				return "wifi";
			} else if (netWorkType == ConnectivityManager.TYPE_MOBILE) {
				return "3G";
			} else {
				return "其他方式";
			}
		} else {
			return "无网络";
		}
	}

	/**
	 * decode res image
	 *
	 * @param res
	 * @param id
	 * @return
	 */
	public static Bitmap decodeImage(Resources res, int id, int requiredWidth, int requiredHeight) {
		try {
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeResource(res, id, o);
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int sampleSize = 1;
			if (width_tmp > height_tmp) {
				int var = width_tmp;
				width_tmp = height_tmp;
				height_tmp = var;
			}
			if (height_tmp > requiredHeight || width_tmp > requiredWidth) {
				final int heightRatio = (int) Math.floor((float) height_tmp / (float) requiredHeight);
				final int widthRatio = (int) Math.floor((float) width_tmp / (float) requiredWidth);
				sampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
			}

			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = sampleSize;
			return BitmapFactory.decodeResource(res, id, o2);
		} catch (Exception e) {

		}
		return null;
	}

	/**
	 * md5加密
	 */
	public static String GetMD5(String s) {
		return GetMD5(s, "utf-8");
	}

	/**
	 * md5加密(已重载)
	 */
	public static String GetMD5(String source, String inputCharset) {
		String s = null;
		char hexDigits[] = { // 用来将字节转换成 16 进制表示的字符
				'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
		try {
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			md.update(source.getBytes(inputCharset));
			byte tmp[] = md.digest(); // MD5 的计算结果是一个 128 位的长整数，
			// 用字节表示就是 16 个字节
			char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
			// 所以表示成 16 进制需要 32 个字符
			int k = 0; // 表示转换结果中对应的字符位置
			for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节
				// 转换成 16 进制字符的转换
				byte byte0 = tmp[i]; // 取第 i 个字节
				str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,
				// >>>
				// 为逻辑右移，将符号位一起右移
				str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
			}
			s = new String(str); // 换后的结果转换为字符串

		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 数组排序（冒泡排序法）
	 */
	public static String[] BubbleSort(String[] r) {
		int i, j; // 交换标志
		String temp;
		Boolean exchange;

		for (i = 0; i < r.length; i++) // 最多做R.Length-1趟排序
		{
			exchange = false; // 本趟排序开始前，交换标志应为假

			for (j = r.length - 2; j >= i; j--) {
				if (r[j + 1].compareTo(r[j]) < 0)// 交换条件
				{
					temp = r[j + 1];
					r[j + 1] = r[j];
					r[j] = temp;

					exchange = true; // 发生了交换，故将交换标志置为真
				}
			}

			if (!exchange) // 本趟排序未发生交换，提前终止算法
			{
				break;
			}
		}

		return r;
	}

	/**
	 * 获取数组加密值
	 */
	public static String GetMD5ByArray(String[] sortedstr, String key, String inputCharset) {
		// 构造待md5摘要字符串
		StringBuilder prestr = new StringBuilder();

		for (int i = 0; i < sortedstr.length; i++) {
			if (i == sortedstr.length - 1) {
				prestr.append(sortedstr[i]);
			} else {
				prestr.append(sortedstr[i] + "&");
			}
		}

		prestr.append(key);// 此处key为上面的innerKey
		return GetMD5(prestr.toString(), inputCharset);
	}

}