package com.pengjun.ka.db.model;

import java.io.Serializable;

public class ArSearchCondition implements Serializable {

	private static final long serialVersionUID = -4580647869739349892L;

	private String startAccount;
	private String endAccount;
	private String startDate;
	private String endDate;
	private String type;

	public String getStartAccount() {
		return startAccount;
	}

	public void setStartAccount(String startAccount) {
		this.startAccount = startAccount;
	}

	public String getEndAccount() {
		return endAccount;
	}

	public void setEndAccount(String endAccount) {
		this.endAccount = endAccount;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
