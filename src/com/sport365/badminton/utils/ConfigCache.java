package com.sport365.badminton.utils;

import java.io.File;

import android.text.TextUtils;

import com.sport365.badminton.params.SystemConfig;

public class ConfigCache {
    private static final String TAG = ConfigCache.class.getName();

    public static final int CONFIG_CACHE_MOBILE_TIMEOUT  = 600000;  //10 hour//1000;//test//

    /**
     * 是否读取缓存
     * @param filename
     * 	由请求转换的一个文件名
     * @return
     */
    public static boolean canReadFileCache(String filename) {
        if (TextUtils.isEmpty(filename)) {
            return false;
        }

        File file = new File(Utilities.JSON_FILE_ROOT, filename);
        if (file.exists() && file.isFile()) {
            long expiredTime = System.currentTimeMillis() - file.lastModified();
            //无网络仅读取缓存
            if (SystemConfig.mNetWorkState == Tools.NETWORN_NONE) {
                return true;
            }
            if (expiredTime > CONFIG_CACHE_MOBILE_TIMEOUT) {
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * 是否保存缓存
     *
     * @param filename
     * @return
     */
    public static boolean canSaveFileCache(String filename) {
        if (TextUtils.isEmpty(filename)) {
            return false;
        }

        File file = new File(Utilities.JSON_FILE_ROOT, filename);
        if (file.exists() && file.isFile()) {
            long expiredTime = System.currentTimeMillis() - file.lastModified();
            if (expiredTime > CONFIG_CACHE_MOBILE_TIMEOUT) {
                return true;
            }
            return false;
        }
        return true;
    }
}
