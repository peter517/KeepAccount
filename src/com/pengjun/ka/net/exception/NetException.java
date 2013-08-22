package com.pengjun.ka.net.exception;

public class NetException extends Exception {

	private static final long serialVersionUID = 6404919973501768435L;

	private ErrorCode errorCode = null;
	private String info = null;

	public NetException(ErrorCode errCode, String info) {
		this.errorCode = errCode;
		this.info = info;
	}

	public String getMessage() {
		return super.getMessage();
	}

	@Override
	public String toString() {
		return "(errorCode = " + errorCode + ") " + info;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}

	public String getReason() {
		return info;
	}

}
