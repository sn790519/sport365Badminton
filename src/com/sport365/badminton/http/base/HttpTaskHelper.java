package com.sport365.badminton.http.base;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.sport365.badminton.http.HttpEngine;
import com.sport365.badminton.http.RequestObject;
import com.sport365.badminton.http.RetCodes;
import com.sport365.badminton.http.json.req.ServiceRequest;
import com.sport365.badminton.http.json.res.ResponseContainer;
import com.sport365.badminton.http.json.res.ResponseContent;
import com.sport365.badminton.utils.SystemConfig;
import com.sport365.badminton.utils.ConfigCache;
import com.sport365.badminton.utils.FileTools;
import com.sport365.badminton.utils.Tools;
import com.sport365.badminton.utils.Utilities;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class HttpTaskHelper {
	public static final String LOG_TAG = HttpEngine.class.getSimpleName();

	public static final String TC_REQUEST = "request";
	public static final String TC_REQUEST_BODY = "body";
	public static final String TC_REQUEST_CLIENT_INFO = "clientInfo";

	// 服务器响应成功code
	public static final int RESPONSE_CODE_SUCCESS = 200;
	// 服务器压力，客户端任务响应不成功
	public static final int RESPONSE_CODE_FAILED_SERVER_STRESS = 503;
	// 服务器连接失败自定义code
	public static final int RESPONSE_CODE_CONNECT_FAILED = 33;

	public static final String RESPONSE_CODE_ERROR_RESULT = "55";
	public static final String RESPONSE_CODE_CANCEL_REQUEST = "66";
	public static final String READ_CACHE_KEY = "requestfromcache";
	public static final String REQUEST_EXCEPTION_KEY = "requestfromexception";

	private static final String CONNECTION_MD5_REQUEST_HEADER = "reqdata";
	private static final String CONNECTION_MD5_CODE = "280504b8b638913d3a8d1cd5a60f7046";
	private static final String CONNECTION_SUCCESS_RSP_TYPE = "0";
	private static final String CONNECTION_SUCCESS_RSP_CODE = "0000";

	private Context mContext;
	private Handler mUiHandler;

	private HashMap<String, Call> mRequestCallMap;
	private HashMap<String, IRequestListener> mRequestListenerHashMap;
	private Object mLock = new Object();

	private boolean mCancelAllCacheRequest;

	public HttpTaskHelper(Context context) {

		mContext = context.getApplicationContext();
		mRequestCallMap = new HashMap<String, Call>();
		mRequestListenerHashMap = new HashMap<String, IRequestListener>();
		HttpEngine.getInstance().initContext(context);
		mUiHandler = new Handler(context.getMainLooper());
		mCancelAllCacheRequest = false;
	}

	/**
	 * send json request
	 * 
	 * @param serviceRequest
	 *            request data.
	 * @param listener
	 *            response callback. return realRequest
	 */
	public Request sendRequest(ServiceRequest serviceRequest, IRequestListener listener) {
		Request realRequest = createConnectionRequest(serviceRequest);
		sendRequest(serviceRequest, realRequest, listener);
		return realRequest;
	}

	/**
	 * send json request
	 * 
	 * @param serviceRequest
	 *            request data.
	 * @param realRequest
	 *            connection related request
	 * @param listener
	 *            response callback.
	 */
	public void sendRequest(ServiceRequest serviceRequest, Request realRequest, IRequestListener listener) {

		if (null == serviceRequest || null == realRequest || null == listener) {
			if (SystemConfig.IS_OPEN_DEBUG) {
				throw new RuntimeException("serviceRequest or realRequest 、listener cannot be null.");
			} else {
				return;
			}
		}

		String fileName = serviceRequest.getCacheFileName();

		boolean bReadCache = ConfigCache.canReadFileCache(fileName);

		String key = "";
		if (realRequest.tag() instanceof String) {
			key = (String) realRequest.tag();
		}
		if (bReadCache) {
			// save data
			final ServiceRequest tmpServiceRequest = serviceRequest;
			final String tmpFileName = fileName;
			final IRequestListener tmpIRequestListener = listener;
			final String tmpKey = TextUtils.isEmpty(key) ? READ_CACHE_KEY : key;

			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					// 允许离线时，读取保存的数据
					readDataFromCache(tmpServiceRequest, tmpFileName, tmpIRequestListener, tmpKey);
				}
			});
			thread.start();
		} else {
			synchronized (mLock) {

				Call call = HttpEngine.getInstance().createRequestCall(realRequest);
				boolean success = readDataFromNetWork(serviceRequest, fileName, call, listener);

				// marked the request
				if (success && !TextUtils.isEmpty(key)) {
					mRequestCallMap.put(key, call);
					mRequestListenerHashMap.put(key, listener);
				}
			}
		}
	}

	/**
	 * create request
	 * 
	 * @param serviceRequest
	 *            business related request data
	 * @return connection request
	 */
	public Request createConnectionRequest(ServiceRequest serviceRequest) {
		try {
			validateAndReturnRequest(serviceRequest);
			Request request;
			String jsonData = serviceRequest.getRequestJson();
			String url = serviceRequest.getServiceUrl();

			HashMap<String, String> headers = createRequestHeaders(jsonData);

			request = HttpEngine.getInstance().createRequest(url, jsonData, headers);
			return request;
		} catch (Exception e) {
			e.printStackTrace();
			return new Request.Builder().tag(REQUEST_EXCEPTION_KEY).build();
		}
	}

	/**
	 * get request header example: ("reqdata" , data )
	 * 
	 * @param jsonData
	 * @return
	 */
	public HashMap<String, String> createRequestHeaders(String jsonData) {
		HashMap<String, String> headersMap = new HashMap<String, String>();
		String requestData = Tools.GetMD5(jsonData + CONNECTION_MD5_CODE);
		headersMap.put(CONNECTION_MD5_REQUEST_HEADER, requestData);
		return headersMap;
	}

	/**
	 * reCheck the request
	 * 
	 * @param serviceRequest
	 * @return serviceRequest
	 */
	private ServiceRequest validateAndReturnRequest(ServiceRequest serviceRequest) {
		if (SystemConfig.IS_OPEN_DEBUG) {
			if (serviceRequest == null) {
				throw new RuntimeException("Http request parameter cannot be null.");
			}

			if (TextUtils.isEmpty(serviceRequest.getServiceUrl())) {
				throw new RuntimeException("Service url cannot be empty or null.");
			}

			if (TextUtils.isEmpty(serviceRequest.getRequestJson())) {
				throw new RuntimeException("Request json cannot be empty or null.");
			}
		}

		return serviceRequest;
	}

	/**
	 * only read response data from SDcard.
	 * 
	 * @param serviceRequest
	 *            request service.
	 * @param fileName
	 *            cached data fileName.
	 * @param listener
	 *            response callback.
	 * @param requestKey
	 *            requestKey.
	 */
	private void readDataFromCache(ServiceRequest serviceRequest, String fileName, IRequestListener listener, String requestKey) {
		String content = "";
		String errorMessage = "服务器暂无响应，请稍后再试。";
		try {
			if (mCancelAllCacheRequest) {
				return;
			}
			content = FileTools.readFile(fileName);
			if (mCancelAllCacheRequest) {
				return;
			}
			// 打印返回内容
			if (SystemConfig.IS_OPEN_LOG_INFO) {
				Log.i(LOG_TAG + "返回内容(缓存)", content);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			if (null != content) {
				onSuccess(listener, serviceRequest, content, true, requestKey);
			} else {
				onError(listener, serviceRequest, RetCodes.IO_EXCEPTION, RESPONSE_CODE_ERROR_RESULT, errorMessage, true, requestKey);
			}
		}
	}

	/**
	 * read data from netWork connection
	 * 
	 * @param serviceRequest
	 *            request data.
	 * @param fileName
	 *            cached data fileName.
	 * @param requestCall
	 *            real call send request.
	 * @param requestListener
	 *            response call back.
	 */
	private boolean readDataFromNetWork(ServiceRequest serviceRequest, String fileName, Call requestCall, IRequestListener requestListener) {
		boolean successSendRequest = false;
		String errorMessage = "";
		try {

			HttpEngine.getInstance().getConnectionResponseAsync(requestCall, new ResponseCallBack(serviceRequest, fileName, requestListener));
			successSendRequest = true;
		} catch (BizException e) {
			e.printStackTrace();
			errorMessage = e.getErrMessage();

		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "服务器暂无响应，请稍后再试。";
		} finally {
			if (!TextUtils.isEmpty(errorMessage)) {
				onError(requestListener, serviceRequest, RetCodes.IO_EXCEPTION, RESPONSE_CODE_ERROR_RESULT, errorMessage, false, null);
			}
			return successSendRequest;
		}
	}

	/**
	 * cancel single request
	 */
	public void cancelRequest(Request request) {
		if (null == request) {
			return;
		}
		try {
			synchronized (mLock) {
				String key;
				if (request.tag() instanceof String) {
					key = (String) request.tag();
				} else {
					return;
				}
				// request already canceled just return
				if (!mRequestCallMap.containsKey(key) && !mRequestListenerHashMap.containsKey(key)) {
					return;
				}
				Call call = mRequestCallMap.get(key);
				mRequestCallMap.remove(key);

				IRequestListener listener = mRequestListenerHashMap.get(key);
				mRequestListenerHashMap.remove(key);

				if (null != call) {
					call.cancel();
				}
				if (null != listener) {
					// only support network request
					CancelInfo cancelInfo = new CancelInfo(key, false);
					listener.onCanceled(cancelInfo);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * cancel all request
	 */
	public void destoryAllRequest() {
		try {
			mCancelAllCacheRequest = true;
			synchronized (mLock) {
				if (null != mRequestCallMap && mRequestCallMap.size() > 0) {
					Iterator it = mRequestCallMap.entrySet().iterator();

					while (it.hasNext()) {
						Map.Entry entry = (Map.Entry) it.next();
						it.remove();
						Call call = (Call) entry.getValue();
						if (null != call) {
							call.cancel();
						}
					}
					mRequestCallMap.clear();
					mRequestListenerHashMap.clear();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * callback of notify the watcher
	 */
	private class ResponseCallBack implements Callback {

		private ServiceRequest mServiceRequest;
		private String mFileName;
		private IRequestListener mListener;

		private ResponseCallBack(ServiceRequest serviceRequest, String cacheFileName, IRequestListener listener) {
			mServiceRequest = serviceRequest;
			mFileName = cacheFileName;
			mListener = listener;
		}

		@Override
		public void onFailure(Request request, IOException e) {
			try {
				String key = null;
				synchronized (mLock) {
					// cancel callback
					if (null != mRequestCallMap && mRequestCallMap.isEmpty()) {
						return;
					}

					if (null != request && (request.tag() instanceof String)) {
						key = (String) request.tag();
						if (null != mRequestCallMap && mRequestCallMap.containsKey(key)) {
							mRequestCallMap.remove(key);
							mRequestListenerHashMap.remove(key);
						} else {
							return;
						}
					}

					int errCode = RESPONSE_CODE_CONNECT_FAILED;
					String errorMessage = "连接服务器失败。";
					boolean hasConnection = Tools.getNetworkState(mContext) != Tools.NETWORN_NONE;
					if (!hasConnection) {
						errorMessage = "网络连接失败，请检查一下网络设置。";
					}
					try {
						throw new BizException(errCode, errorMessage, null);
					} catch (BizException e1) {
						e1.printStackTrace();
						onError(mListener, mServiceRequest, String.valueOf(errCode), RESPONSE_CODE_ERROR_RESULT, errorMessage, false, key);
					}

				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

		@Override
		public void onResponse(Response response) throws IOException {

			try {
				String key = null;
				synchronized (mLock) {
					// cancel callback
					if (null != mRequestCallMap && mRequestCallMap.isEmpty()) {
						return;
					}

					// cancel event
					if (null == response) {
						return;
					}

					if (null != response.request() && (response.request().tag() instanceof String)) {
						key = (String) response.request().tag();
						if (null != mRequestCallMap && mRequestCallMap.containsKey(key)) {
							mRequestCallMap.remove(key);
							mRequestListenerHashMap.remove(key);
						} else {
							return;
						}
					}

					String content = "";
					int errCode = 0;
					String errRspType = RESPONSE_CODE_ERROR_RESULT;
					String errorMessage = "";
					try {

						if (response.code() == RESPONSE_CODE_SUCCESS) {

							content = response.body().string();

							// 打印返回内容
							if (SystemConfig.IS_OPEN_LOG_INFO) {
								Log.i(LOG_TAG + "返回内容", content);
							}

							// 解析json数据
							JSONObject responseContainerJson = new JSONObject(content);
							JSONObject responseJson = responseContainerJson.optJSONObject("response");
							if (responseJson == null) {
								throw new BizException("invalid response json for have no 'response' node.", new Exception());
							}

							JSONObject headerJson = responseJson.optJSONObject("header");
							if (headerJson == null) {
								throw new BizException("invalid response json for have no 'header' node.", new Exception());
							}

							String rspCode = headerJson.optString("rspCode", "");
							String rspType = headerJson.optString("rspType", "");
							String rspDesc = headerJson.optString("rspDesc", "");

							boolean hasResult = (CONNECTION_SUCCESS_RSP_TYPE).equals(rspType) && (CONNECTION_SUCCESS_RSP_CODE).equals(rspCode);

							// 解析数据
							boolean canSaveCache = ConfigCache.canSaveFileCache(mFileName) && hasResult;

							if (canSaveCache) {
								saveResponseData(content, mFileName);
							}

							if (!hasResult) {
								onError(mListener, mServiceRequest, rspCode, rspType, rspDesc, false, key, content);
							} else {
								onSuccess(mListener, mServiceRequest, content, false, key);
							}
						} else {// 连接服务器失败
							throw new BizException(response.code(), "服务器暂无响应，请稍后再试。", null);
						}

					} catch (JSONException e) {
						e.printStackTrace();
						errorMessage = "页面加载失败";
						errRspType = RetCodes.JSON_EXCEPTION;
					} catch (BizException e) {
						e.printStackTrace();
						errCode = e.getErrCode();
						errorMessage = e.getErrMessage();
					} catch (Exception e) {
						e.printStackTrace();
						errorMessage = "服务器暂无响应，请稍后再试。";
					} finally {
						if (!TextUtils.isEmpty(errorMessage)) {
							onError(mListener, mServiceRequest, String.valueOf(errCode), errRspType, errorMessage, false, key);
						}
					}
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * save response body to object and Save data to SDcard
	 * 
	 * @param content
	 *            response body string.if the body size > 1M ,should get
	 *            response steam to convert
	 * @param fileName
	 *            cached data fileName.
	 * @throws BizException
	 */
	private void saveResponseData(String content, String fileName) throws BizException {

		if (content.length() < 100) {
			content = content.replace(",\"body\":\"\"", "");
		}
		try {
			FileTools.saveToSD(fileName, content);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void onSuccess(final IRequestListener listener, final ServiceRequest serviceRequest, final String jsonResponse, final boolean fromCache, final String requestKey) {
		try {
			mUiHandler.post(new Runnable() {
				@Override
				public void run() {
					JsonResponse response = new JsonResponse(serviceRequest.getRequestJson(), jsonResponse);
					RequestInfo requestInfo = new RequestInfo(serviceRequest, requestKey, fromCache);
					if (null != listener) {
						listener.onSuccess(response, requestInfo);
					}
				}
			});
		} catch (Exception e) {

		}
	}

	private void onError(final IRequestListener listener, final ServiceRequest serviceRequest, final String rspCode, final String rspType, final String message, final boolean fromCache,
			final String requestKey) {
		onError(listener, serviceRequest, rspCode, rspType, message, fromCache, requestKey, null);
	}

	private void onError(final IRequestListener listener, final ServiceRequest serviceRequest, final String rspCode, final String rspType, final String message, final boolean fromCache,
			final String requestKey, final String jsonContent) {

		try {
			final boolean showPromptMessage = serviceRequest.isShowPromptMessage();
			mUiHandler.post(new Runnable() {
				@Override
				public void run() {
					if (showPromptMessage) {
						showTips(message);
					}
					ResponseContent.Header header = new ResponseContent.Header();
					header.setRspCode(rspCode);
					header.setRspType(rspType);
					header.setRspDesc(message);
					RequestInfo requestInfo = new RequestInfo(serviceRequest, requestKey, fromCache);
					if (!TextUtils.isEmpty(jsonContent)) {
						JsonResponse response = new JsonResponse(serviceRequest.getRequestJson(), jsonContent);
						requestInfo.setJsonResponse(response);
					}
					if (null != listener) {
						listener.onError(header, requestInfo);
					}
				}
			});
		} catch (Exception e) {

		}
	}

	/**
	 * 提示信息统一转换
	 */
	private void showTips(String message) {
		String msg = message;
		try {
			if (msg.contains("接口执行错误")) {
				Utilities.showToast("服务器异常，请稍后再试！", mContext);
			} else {
				// 屏蔽掉"|0"类型的报错
				msg = msg.replaceAll("(\\|){1}\\d+", "");
				Utilities.showToast(msg, mContext);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * A container contains both request JSON and response JSON, It's used as a
	 * return value for HTTP response.
	 * 
	 * @author 
	 */
	public class JsonResponse {
		private String request;
		private String response;

		public JsonResponse(String request, String response) {
			this.request = request;
			this.response = response;
		}

		/**
		 * Return client request JSON string.
		 */
		public String getRequestContent() {
			return request;
		}

		public <T> T getRequestContent(Class<T> reqBodyClass) {
			try {
				JSONObject requestJosn = new JSONObject(request);
				JSONObject requestContentJson = requestJosn.getJSONObject(TC_REQUEST);
				JSONObject bodyJson = requestContentJson.getJSONObject(TC_REQUEST_BODY);
				bodyJson.remove(TC_REQUEST_CLIENT_INFO);
				return JsonHelper.fromJson(bodyJson.toString(), reqBodyClass);
			} catch (Exception e) {
				if (SystemConfig.IS_OPEN_DEBUG) {
					throw new RuntimeException("getRequestContent error");
				} else {
					return null;
				}
			}
		}

		/**
		 * Return server response JSON string.
		 */
		public String getResponseContent() {
			return response;
		}

		public <T> ResponseContent<T> getResponseContent(Class<T> resBodyClass) {
			try {
				return new ResponseContainer<T>().fromJson(response, resBodyClass).getResponse();
			} catch (Exception e) {
				if (SystemConfig.IS_OPEN_DEBUG) {
					throw new RuntimeException("getResponseContent error");
				} else {
					return null;
				}
			}
		}
	}

	public class RequestInfo {
		private ServiceRequest serviceRequest;
		private String requestKey;
		private boolean formCache;
		// use for no result response case
		private JsonResponse jsonResponse;

		public RequestInfo(ServiceRequest serviceRequest, String requestKey, boolean formCache) {
			this.serviceRequest = serviceRequest;
			this.requestKey = requestKey;
			this.formCache = formCache;
		}

		public ServiceRequest getServiceRequest() {
			return serviceRequest;
		}

		public void setServiceRequest(ServiceRequest serviceRequest) {
			this.serviceRequest = serviceRequest;
		}

		public String getRequestKey() {
			return requestKey;
		}

		public void setRequestKey(String requestKey) {
			this.requestKey = requestKey;
		}

		public boolean isFormCache() {
			return formCache;
		}

		public void setFormCache(boolean formCache) {
			this.formCache = formCache;
		}

		public void setJsonResponse(JsonResponse jsonResponse) {
			this.jsonResponse = jsonResponse;
		}

		public JsonResponse getJsonResponse() {
			return jsonResponse;
		}
	}

	public class CancelInfo {
		private String requestKey;
		private boolean formCache;
		private ResponseContent.Header header;

		public CancelInfo(String requestKey, boolean formCache) {
			this.requestKey = requestKey;
			this.formCache = formCache;
			header = new ResponseContent.Header();
			header.setRspDesc("你已取消操作！");
			header.setRspType(RESPONSE_CODE_CANCEL_REQUEST);
		}

		public String getRequestKey() {
			return requestKey;
		}

		public void setRequestKey(String requestKey) {
			this.requestKey = requestKey;
		}

		public boolean isFormCache() {
			return formCache;
		}

		public void setFormCache(boolean formCache) {
			this.formCache = formCache;
		}

		public ResponseContent.Header getHeader() {
			return header;
		}

		public void setHeader(ResponseContent.Header header) {
			this.header = header;
		}
	}

	/**
	 * 将请求数据转换为json格式
	 * 
	 * @param stringInput
	 * @param m
	 * @param <T1>
	 * @return
	 */
	public static <T1> String convertObjectToJson(String stringInput, T1 m) {
		RequestObject object = new RequestObject();
		object.PartParameter(stringInput);
		HashMap<String, Object> map = changeTtoMap(m);
		object.appendMaptoBody(map);
		return JsonHelper.toJson(object);
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
}
