package com.sport365.badminton.utils;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.text.TextUtils;

import com.sport365.badminton.BaseApplication;
import com.sport365.badminton.params.SystemConfig;

/**
 * SharedPreferences操作
 */
public class SharedPreferencesUtils {
	private SharedPreferences sharedPreferences;
	private Editor editor;
	private static SharedPreferencesUtils sharedPrefUtils;
	private Map<String, SoftReference<String>> prefCache = new HashMap<String, SoftReference<String>>();

	private SharedPreferencesUtils() {
		BaseApplication baseApplication = BaseApplication.getInstance();
		Context context = baseApplication.getApplicationContext();
		sharedPreferences = context.getSharedPreferences(SystemConfig.PREFERENCES_NAME, Context.MODE_PRIVATE);
		editor = sharedPreferences.edit();
	}

	public synchronized static SharedPreferencesUtils getInstance() {
		if (sharedPrefUtils == null) {
			sharedPrefUtils = new SharedPreferencesUtils();
		}
		return sharedPrefUtils;
	}

	/**
	 * 写入Boolean
	 */
	public boolean putBoolean(String name, boolean value) {
		if (editor == null || TextUtils.isEmpty(name)) {
			return false;
		}
		editor.putBoolean(name, value);
		prefCache.put(name, new SoftReference<String>(String.valueOf(value)));
		return true;
	}

	/**
	 * 读取Boolean
	 */
	public Boolean getBoolean(String name, boolean defaultValue) {
		if (sharedPreferences == null || TextUtils.isEmpty(name)) {
			return defaultValue;
		}

		SoftReference<String> softRef = prefCache.get(name);
		if (softRef == null || (softRef != null && TextUtils.isEmpty(softRef.get()))) {
			boolean value = sharedPreferences.getBoolean(name, defaultValue);
			prefCache.put(name, new SoftReference<String>(String.valueOf(value)));
		}

		return Boolean.valueOf(prefCache.get(name).get());
	}

	/**
	 * 写入String
	 */
	public boolean putString(String name, String value) {
		if (editor == null || TextUtils.isEmpty(name)) {
			return false;
		}
		editor.putString(name, value);
		prefCache.put(name, new SoftReference<String>(value));
		return true;
	}

	/**
	 * 读取String
	 */
	public String getString(String name, String defaultValue) {
		if (sharedPreferences == null || TextUtils.isEmpty(name)) {
			return defaultValue;
		}
		SoftReference<String> softRef = prefCache.get(name);
		if (softRef == null || (softRef != null && TextUtils.isEmpty(softRef.get()))) {
			String value = sharedPreferences.getString(name, defaultValue);
			prefCache.put(name, new SoftReference<String>(value));
		}
		return prefCache.get(name).get();
	}

	/**
	 * 写入Long
	 */
	public boolean putLong(String name, long value) {
		if (editor == null || TextUtils.isEmpty(name)) {
			return false;
		}
		editor.putLong(name, value);
		prefCache.put(name, new SoftReference<String>(String.valueOf(value)));
		return true;
	}

	/**
	 * 读取Long
	 */
	public Long getLong(String name, long defaultValue) {
		if (sharedPreferences == null || TextUtils.isEmpty(name)) {
			return defaultValue;
		}

		SoftReference<String> softRef = prefCache.get(name);
		if (softRef == null || (softRef != null && TextUtils.isEmpty(softRef.get()))) {
			long value = sharedPreferences.getLong(name, defaultValue);
			prefCache.put(name, new SoftReference<String>(String.valueOf(value)));
		}
		return Long.valueOf(prefCache.get(name).get());
	}

	/**
	 * 写入Int
	 */
	public boolean putInt(String name, int value) {
		if (editor == null || TextUtils.isEmpty(name)) {
			return false;
		}
		editor.putInt(name, value);
		prefCache.put(name, new SoftReference<String>(String.valueOf(value)));
		return true;
	}

	/**
	 * 读取Int
	 */
	public Integer getInt(String name, int defaultValue) {
		if (sharedPreferences == null || TextUtils.isEmpty(name)) {
			return defaultValue;
		}
		SoftReference<String> softRef = prefCache.get(name);
		if (softRef == null || (softRef != null && TextUtils.isEmpty(softRef.get()))) {
			int value = sharedPreferences.getInt(name, defaultValue);
			prefCache.put(name, new SoftReference<String>(String.valueOf(value)));
		}
		return Integer.valueOf(prefCache.get(name).get());
	}

	/**
	 * 写入Int
	 */
	public boolean putFloat(String name, float value) {
		if (editor == null || TextUtils.isEmpty(name)) {
			return false;
		}
		editor.putFloat(name, value);
		prefCache.put(name, new SoftReference<String>(String.valueOf(value)));
		return true;
	}

	/**
	 * 读取Int
	 */
	public Float getFolat(String name, float defaultValue) {
		if (sharedPreferences == null || TextUtils.isEmpty(name)) {
			return defaultValue;
		}

		SoftReference<String> softRef = prefCache.get(name);
		if (softRef == null || (softRef != null && TextUtils.isEmpty(softRef.get()))) {
			float value = sharedPreferences.getFloat(name, defaultValue);
			prefCache.put(name, new SoftReference<String>(String.valueOf(value)));
		}
		return Float.valueOf(prefCache.get(name).get());
	}

	/**
	 * sharedPreferences写入Set<String>
	 */
	public boolean putSet(String key, Set<String> value) {
		if (TextUtils.isEmpty(key) || sharedPreferences == null || editor == null) {
			return false;
		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			editor.putStringSet(key, value);
		} else {
			final String regularEx = "|";
			StringBuilder sBuilder = new StringBuilder();
			if (value != null) {
				for (String val : value) {
					sBuilder.append(val);
					sBuilder.append(regularEx);
				}
			}
			putString(key, sBuilder.toString());
		}

		return true;
	}

	/**
	 * sharedPreferences读取Set<String>
	 */
	public Set<String> getSet(String key, Set<String> defaultValue) {
		if (TextUtils.isEmpty(key) || sharedPreferences == null) {
			return defaultValue;
		}

		if (!sharedPreferences.contains(key)) {
			return null;
		}

		Set<String> valueSet = new HashSet<String>();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			valueSet = sharedPreferences.getStringSet(key, defaultValue);
		} else {
			String value = null;
			if (defaultValue != null) {
				final String regularExPut = "|";
				StringBuilder sBuilder = new StringBuilder();
				for (String val : defaultValue) {
					sBuilder.append(val);
					sBuilder.append(regularExPut);
				}
				value = getString(key, sBuilder.toString());
			} else {
				value = getString(key, "");
			}

			final String regularExGet = "\\|";
			String[] valueArray;
			if (!TextUtils.isEmpty(value)) {
				valueArray = value.split(regularExGet);
				for (int i = 0; i < valueArray.length; i++) {
					valueSet.add(valueArray[i]);
				}
			}
		}

		return valueSet;
	}

	/**
	 * commit
	 */
	public boolean commitValue() {
		if (editor == null) {
			return false;
		}

		return editor.commit();
	}

	/**
	 * remove
	 */
	public boolean removeValue(String key) {
		if (TextUtils.isEmpty(key) || sharedPreferences == null || editor == null) {
			return false;
		}

		if (!sharedPreferences.contains(key)) {
			return false;
		}

		editor.remove(key);

		if (prefCache.containsKey(key)) {
			prefCache.remove(key);
		}

		return true;
	}

	/**
	 * sharedPreferences写入List<String>
	 */
	public boolean putStringList(String name, List<String> value) {
		editor.remove(name).commit();
		return putListValue(name, value);
	}

	/**
	 * sharedPreferences读取List<String>
	 */
	public List<String> getStringList(String name) {
		List<String> linkedHashSet = new ArrayList<String>();
		List<String> set = getListValue(name, new ArrayList<String>());
		if (set != null && set.size() > 0) {
			linkedHashSet.addAll(set);
		}
		return linkedHashSet;
	}

	public List<String> getListValue(String key, List<String> defValue) {
		try {
			if (sharedPreferences == null) {
				return defValue;
			}
			final String regularEx = "\\|";
			String listString = getString(key, null);
			if (listString != null) {
				String[] values = listString.split(regularEx);
				List<String> list = new ArrayList<String>(values.length);
				for (String value : values) {
					if (!TextUtils.isEmpty(value))
						list.add(value);
				}
				return list;
			}

		} catch (Throwable e) {

		}
		return defValue;
	}

	public boolean putListValue(String key, List<String> values) {
		try {
			if (sharedPreferences == null || editor == null) {
				return false;
			}
			final String regularEx = "|";
			String str = "";
			if (values != null && !values.isEmpty()) {
				Object[] objects = values.toArray();
				for (Object obj : objects) {
					str += obj.toString();
					str += regularEx;
				}
				return putString(key, str);
			}
		} catch (Throwable e) {

		}
		return false;
	}

	/**
	 * 清空历史记录数据
	 */
	public boolean clearData() {
		if (editor == null) {
			return false;
		}

		editor.clear();
		return true;
	}
}
