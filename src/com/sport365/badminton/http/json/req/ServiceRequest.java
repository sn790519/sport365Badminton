package com.sport365.badminton.http.json.req;

import android.content.Context;
import android.util.Log;

import com.sport365.badminton.http.HttpEngine;
import com.sport365.badminton.http.base.HttpTaskHelper;
import com.sport365.badminton.http.base.JsonHelper;
import com.sport365.badminton.http.json.CacheOptions;
import com.sport365.badminton.http.json.WebService;
import com.sport365.badminton.params.SystemConfig;
import com.sport365.badminton.utils.Tools;

/**
 * A container contains request JSON, URL of web service and cache options.
 * 
 */
public final class ServiceRequest {
	public static final String LOG_TAG = HttpEngine.class.getSimpleName();

	private String requestJson = "";
	private String serviceUrl = "";
	private String serviceName = "";
	private boolean showPromptMessage = false;
	private CacheOptions cacheOptions = CacheOptions.buildCacheOptions(CacheOptions.NO_CACHE);
	private String cacheName = "";

	/**
	 * Be similar to {@link #ServiceRequest(Context, WebService, Object)}, but
	 * use this construction can be convenient.
	 * 
	 * @param context
	 *            android context
	 * @param service
	 *            business web service class
	 * @param body
	 *            business web service's body
	 */
	public <T extends WebService> ServiceRequest(Context context, Class<T> service, Object body) {
		if (service == null || body == null) {
			if (SystemConfig.IS_OPEN_DEBUG) {
				throw new RuntimeException("service or body cannot be empty.");
			}
		} else {
			try {
				T request = service.newInstance();
				this.serviceName = request.getServiceName();
				this.requestJson = HttpTaskHelper.convertObjectToJson(serviceName, body);
				this.serviceUrl = request.getServiceUrl();
				this.showPromptMessage = request.isShowPromptMessage();
				this.cacheOptions = request.getCacheOptions();
				String key = JsonHelper.toJson(body);
				if (cacheOptions.useCache()) {
					this.cacheName = serviceName + Tools.GetMD5(key);
				} else {
					this.cacheName = "";
				}
			} catch (Exception e) {
				if (SystemConfig.IS_OPEN_DEBUG) {
					Log.e(LOG_TAG, "create ServiceRequest error!");
				}
			}
		}
	}

	public ServiceRequest(Context context, WebService service, Object body) {
		if (service == null || body == null) {
			if (SystemConfig.IS_OPEN_DEBUG) {
				throw new RuntimeException("service or body cannot be empty.");
			}
		} else {

			try {
				this.serviceName = service.getServiceName();
				this.requestJson = HttpTaskHelper.convertObjectToJson(serviceName, body);
				this.serviceUrl = service.getServiceUrl();
				this.showPromptMessage = service.isShowPromptMessage();
				this.cacheOptions = service.getCacheOptions();
				String key = JsonHelper.toJson(body);
				if (cacheOptions.useCache()) {
					this.cacheName = serviceName + Tools.GetMD5(key);
				} else {
					this.cacheName = "";
				}
			} catch (Exception e) {
				if (SystemConfig.IS_OPEN_DEBUG) {
					Log.e(LOG_TAG, "create ServiceRequest error!");
				}
			}
		}
	}

	public String getRequestJson() {
		return requestJson;
	}

	public String getServiceUrl() {
		return serviceUrl;
	}

	public CacheOptions getCacheOptions() {
		return cacheOptions;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public boolean isShowPromptMessage() {
		return showPromptMessage;
	}

	public String getCacheFileName() {
		return cacheName;
	}
}