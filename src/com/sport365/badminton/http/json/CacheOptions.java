package com.sport365.badminton.http.json;

public class CacheOptions {
	public final static int NO_CACHE = 0x10;
	public final static int USE_CACHE = 0x20;

	private boolean useCache = false;

	private CacheOptions() {
	}

	public boolean useCache() {
		return useCache;
	}

	public static CacheOptions buildCacheOptions(int cacheType) {
		CacheOptions options = new CacheOptions();
		switch (cacheType) {
			case NO_CACHE:
				options.useCache = false;
				break;
			case USE_CACHE:
				options.useCache = true;
				break;
		}

		return options;
	}

}
