package com.sport365.badminton.http.json;

import java.io.Serializable;

import com.sport365.badminton.http.base.JsonHelper;

/**
 * A helper class which override toString() by default to encode this object to
 * JSON string.
 * 
 */
public class JsonBean implements Serializable {

	/**
	 * Encodes this object as a compact JSON string, such as:
	 * {"query":"Pizza","locations":[94043,90210]}
	 * 
	 * Overrides: toString() in Object Returns: a printable representation of
	 * this object.
	 */
	public String toString() {
		return JsonHelper.toJson(this);
	}

}
