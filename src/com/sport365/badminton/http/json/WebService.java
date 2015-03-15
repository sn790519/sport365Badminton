package com.sport365.badminton.http.json;

import android.text.TextUtils;

import com.sport365.badminton.utils.SystemConfig;

/**
 * Every web service should extends this to implement an entire web service.
 * 
 */
public abstract class WebService {

	/**
	 * Override this method to set service action.
	 */
	protected abstract String getServiceAction();

	/**
	 * Override this method to set service name
	 */
	public abstract String getServiceName();

	/**
	 * Override this method reset this web service's cache options, otherwise no
	 * cache will be used for this web service.
	 */
	public CacheOptions getCacheOptions() {
		return CacheOptions.buildCacheOptions(CacheOptions.NO_CACHE);
	}

	/**
	 * Override this method control the head message show.
	 * 
	 * @return
	 */
	public boolean isShowPromptMessage() {
		return false;
	}

	public final String getServiceUrl() {
		String serviceAction = getServiceAction();
		if (TextUtils.isEmpty(serviceAction)) {
			throw new RuntimeException("service action is empty or null");
		}

		if (serviceAction.startsWith("/")) {
			throw new RuntimeException("Service action cannot start with '/' for " + serviceAction);
		}

		return SystemConfig.HostName + getServiceAction();
	}

}
