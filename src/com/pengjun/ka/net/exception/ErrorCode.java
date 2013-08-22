package com.pengjun.ka.net.exception;

public enum ErrorCode {
	Success("success"), NetError("net error");

	String info;

	ErrorCode(String str) {
		this.info = str;
	}
}
