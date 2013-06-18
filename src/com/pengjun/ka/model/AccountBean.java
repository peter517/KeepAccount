package com.pengjun.ka.model;

public class AccountBean {
	public String date;
	public String cost;
	public int category;

	public AccountBean(String date, String cost, int category) {
		super();
		this.date = date;
		this.cost = cost;
		this.category = category;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

}
