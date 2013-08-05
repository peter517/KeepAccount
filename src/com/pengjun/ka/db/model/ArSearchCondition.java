package com.pengjun.ka.db.model;

import java.io.Serializable;

public class ArSearchCondition implements Serializable {

	private static final long serialVersionUID = -4580647869739349892L;

	private static String startAccount;
	private static String endAccount;
	private static String startDate;
	private static String endDate;
	private static String type;

	public static String getType() {
		return type;
	}

	public static void setType(String type) {
		ArSearchCondition.type = type;
	}

	public static String getStartAccount() {
		return startAccount;
	}

	public static void setStartAccount(String startAccount) {
		ArSearchCondition.startAccount = startAccount;
	}

	public static String getEndAccount() {
		return endAccount;
	}

	public static void setEndAccount(String endAccount) {
		ArSearchCondition.endAccount = endAccount;
	}

	public static String getStartDate() {
		return startDate;
	}

	public static void setStartDate(String startDate) {
		ArSearchCondition.startDate = startDate;
	}

	public static String getEndDate() {
		return endDate;
	}

	public static void setEndDate(String endDate) {
		ArSearchCondition.endDate = endDate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
