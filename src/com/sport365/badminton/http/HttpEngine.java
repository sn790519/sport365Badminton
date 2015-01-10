package com.sport365.badminton.http;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.sport365.badminton.http.base.BizException;
import com.sport365.badminton.params.SystemConfig;
import com.sport365.badminton.utils.Tools;
import com.sport365.badminton.utils.Utilities;
import com.squareup.okhttp.CacheControl;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

public class HttpEngine {

	public static final String LOG_TAG = HttpEngine.class.getSimpleName();

	public static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");

	public static final String REQUEST_STORE_KEY_PRE = "TC-REQUEST-KEY-";

	// 服务器连接失败自定义code
	public static final int RESPONSE_CODE_CONNECT_FAILED = 33;

	private static int timeOut = 40000;

	private static HttpEngine instance = null;
	private OkHttpClient mOkHttpClient;
	private Context mContext;

	private HttpEngine() {
		mOkHttpClient = new OkHttpClient();
		mOkHttpClient.setConnectTimeout(timeOut, java.util.concurrent.TimeUnit.MILLISECONDS);
		mOkHttpClient.setReadTimeout(timeOut, java.util.concurrent.TimeUnit.MILLISECONDS);
		mOkHttpClient.setFollowRedirects(true);
	}

	public void initContext(Context context) {
		if (null == mContext) {
			mContext = context;
		} else {
			return;
		}
	}

	public static synchronized HttpEngine getInstance() {
		if (instance == null) {
			instance = new HttpEngine();
		}
		return instance;
	}

	private static String getFileName(String funName, HashMap<String, Object> param) {
		String fileName = "";
		fileName = funName;
		for (String o : param.keySet()) {
			fileName += "_" + o + "_" + param.get(o).toString();
		}
		return funName + Tools.GetMD5(fileName);
	}

	private static <T> HashMap<String, Object> changeTtoMap(T m) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		// 获取实体类的所有属性
		Field[] field = m.getClass().getDeclaredFields();
		// 遍历所有属性
		for (int j = 0; j < field.length; j++) {
			// 获取属性的名字
			String name = field[j].getName();

			Object value = Utilities.getFieldValueObj(m, name);
			if (value != null && !value.equals("")) {
				map.put(name, value);
			}
		}
		return map;
	}

	/**
	 * 异步创建网络连接方法
	 * 
	 * @param requestCall
	 *            the requestCall for okhttp
	 * @throws com.tongcheng.android.base.BizException
	 *             错误处理异常，用于判断请求成功以及自定义errorCode
	 */
	public void getConnectionResponseAsync(Call requestCall, Callback callback) throws BizException {

		try {

			if (null == requestCall) {
				return;
			}

			if (null != callback) {
				requestCall.enqueue(callback);
			}

		} catch (Exception e) {
			throw new BizException(RESPONSE_CODE_CONNECT_FAILED, "连接服务器失败", e);
		}
	}

	/**
	 * 获得请求对应的缓存文件名
	 * 
	 * @param requestServerName
	 *            请求具体接口名称
	 * @param m
	 * @param <T1>
	 * @return
	 */
	public <T1> String getRequestCacheFileName(String requestServerName, boolean useCache, T1 m) {
		HashMap<String, Object> map = changeTtoMap(m);
		return useCache ? getFileName(requestServerName, map) : "";
	}

	/**
	 * create request
	 * 
	 * @param url
	 *            传入完整的url链接
	 * @param jsonData
	 *            请求数据类型，json格式
	 * @param headers
	 *            请求的headers，可定制
	 * @return
	 */
	public Request createRequest(String url, String jsonData, HashMap<String, String> headers) {
		if (TextUtils.isEmpty(jsonData)) {
			return null;
		}

		// okhttp 缓存需要jdk1.7支持，在此不使用缓存
		Request request;
		RequestBody body = RequestBody.create(JSON, jsonData);
		Request.Builder builder = new Request.Builder().cacheControl(CacheControl.FORCE_NETWORK).url(url).tag(createRequestTag()).post(body);

		// add request header
		if (null != headers && headers.size() > 0) {
			for (Map.Entry entry : headers.entrySet()) {
				String key = entry.getKey().toString();
				String value = entry.getValue().toString();
				builder.addHeader(key, value).build();
			}
		}

		request = builder.build();

		if (SystemConfig.IS_OPEN_LOG_INFO) {
			Log.i(LOG_TAG + "请求路径", url);
			Log.i(LOG_TAG + "请求参数", jsonData.toString());
		}

		return request;
	}

	public Call createRequestCall(Request request) {
		if (null == mOkHttpClient || null == request) {
			return null;
		}
		return mOkHttpClient.newCall(request);
	}

	/**
	 * create request related tag
	 * 
	 * @return
	 */
	private String createRequestTag() {
		return REQUEST_STORE_KEY_PRE + UUID.randomUUID();
	}
}