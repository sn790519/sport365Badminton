package com.sport365.badminton.base;

/**
 * 业务异常类
 * @author Tc003167
 *
 */
public class BizException extends Exception {
	private static final long serialVersionUID = -7294511800719769763L;
	private String errMessage;
	private int errCode = 0;//0-100为自定义的错误，>=100为http请求返回

	public BizException() {
		super();
	}

	public BizException(int errCode, String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
		this.setErrCode(errCode);
		this.errMessage = detailMessage;
	}
	
	public BizException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
		this.errMessage = detailMessage;
	}

	public BizException(String detailMessage) {
		super(detailMessage);
	}

	public BizException(Throwable throwable) {
		super(throwable);
	}

	public String getErrMessage() {
		return errMessage;
	}

	public void setErrMessage(String errMessage) {
		this.errMessage = errMessage;
	}

	public int getErrCode() {
		return errCode;
	}

	public void setErrCode(int errCode) {
		this.errCode = errCode;
	}
}
