package com.sport365.badminton.http.json.res;

import com.sport365.badminton.http.json.JsonBean;

/**
 * JSON format class of PageInfo in Request,
 * 
 * <pre>
 * "response": {
 *     "header": {
 *     		"id": "123",
 *     		"rspType":"0",
 *     		"rspCode":"0000",
 *     		"rspDesc":"返回成功"
 *  },
 *  "body": {
 *          "pageInfo": {
 *          "page": "1",
 *          "pageSize": "10",
 *          "totalPage": "56",
 *          "totalCount": "551"
 *      },
 *      "isUpdate":"1"
 *     }
 * }
 * </pre>
 * 
 */
public class ResponseContent<T> extends JsonBean {
	private Header	header;
	private T		body;

	public static final class Header extends JsonBean {
		private String	rspType;
		private String	rspCode;
		private String	rspDesc;

		public String getRspType() {
			return rspType;
		}

		public void setRspType(String rspType) {
			this.rspType = rspType;
		}

		public String getRspCode() {
			return rspCode;
		}

		public void setRspCode(String rspCode) {
			this.rspCode = rspCode;
		}

		public String getRspDesc() {
			return rspDesc;
		}

		public void setRspDesc(String rspDesc) {
			this.rspDesc = rspDesc;
		}

		public Header copy() {
			try {
				return (Header) clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public T getBody() {
		return body;
	}

	public void setBody(T body) {
		this.body = body;
	}

}
