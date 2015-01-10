package com.sport365.badminton.base;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonHelper {
	private static final Gson GSON_INSTANCE = new GsonBuilder().create();

	public static <T> T fromJson(String json, Class<T> classOfT) {
		return GSON_INSTANCE.fromJson(json, classOfT);
	}

	public static <T> T fromJson(String json, Type type) {
		return (T) GSON_INSTANCE.fromJson(json, type);
	}

	public static String toJson(Object src) {
		return GSON_INSTANCE.toJson(src);
	}

	public static String toJson(Object src, Type typeOfSrc) {
		return GSON_INSTANCE.toJson(src, typeOfSrc);
	}
}
