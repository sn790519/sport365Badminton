package com.sport365.badminton.http.json.res;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.sport365.badminton.base.JsonHelper;
import com.sport365.badminton.http.json.JsonBean;


/**
 * JSON format class of Response.
 * 
 * <pre>
 *  {
 *     "response": {
 *         "header": {
 *             "rspType": "0",
 *             "rspCode": "0000",
 *             "rspDesc": "返回成功"
 *         },
 *         "body": {
 *             "isUpdate": "1"
 *         }
 *     }
 * }
 * </pre>
 * 
 * @author zf08526
 * @param <T>
 *            Class type of "Body" extends "BaseResBody" in "Response".
 */
public class ResponseContainer<T> extends JsonBean {
	private ResponseContent<T> response = new ResponseContent<T>();

	public ResponseContent<T> getResponse() {
		return response;
	}

	public void setResponse(ResponseContent<T> response) {
		this.response = response;
	}

	/**
	 * Convert JSON string to response class.
	 * 
	 * @param json
	 *            JSON String of "response"
	 * @param bodyClass
	 *            class in response.
	 * @return response class instance.
	 */
	public ResponseContainer<T> fromJson(String json, Class<T> bodyClass) {
		Type objectType = buildType(ResponseContainer.class, bodyClass);
		return JsonHelper.fromJson(json, objectType);
	}

	/*
	 * public String toJson(Class<T> classType) { Gson gson = new Gson(); Type
	 * objectType = buildType(Response.class, classType); return
	 * gson.toJson(this, objectType); }
	 */

	private ParameterizedType buildType(final Class<?> raw, final Type... args) {
		return new ParameterizedType() {
			public Type getRawType() {
				return raw;
			}

			public Type[] getActualTypeArguments() {
				return args;
			}

			public Type getOwnerType() {
				return null;
			}
		};
	}

}
