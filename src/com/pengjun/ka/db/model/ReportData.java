package com.pengjun.ka.db.model;

public class ReportData {

	private long totalCountNum;
	private double totalCost;
	private double avgCost;
	private String maxCostDay;
	private AccountRecord maxCost;
	private String maxCostInterval;

	public String getMaxCostInterval() {
		return maxCostInterval;
	}

	public void setMaxCostInterval(String maxCostInterval) {
		this.maxCostInterval = maxCostInterval;
	}

	public long getTotalCountNum() {
		return totalCountNum;
	}

	public void setTotalCountNum(long totalCountNum) {
		this.totalCountNum = totalCountNum;
	}

	public double getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(double totalCost) {
		this.totalCost = totalCost;
	}

	public double getAvgCost() {
		return avgCost;
	}

	public void setAvgCost(double avgCost) {
		this.avgCost = avgCost;
	}

	public String getMaxCostDay() {
		return maxCostDay;
	}

	public void setMaxCostDay(String maxCostDay) {
		this.maxCostDay = maxCostDay;
	}

	public AccountRecord getMaxCost() {
		return maxCost;
	}

	public void setMaxCost(AccountRecord maxCost) {
		this.maxCost = maxCost;
	}

}
