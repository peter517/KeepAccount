package com.pengjun.ka.db.model;

public class MagicBoxData {

	private long totalCountNum;
	private double totalCost;
	private double avgCost;
	private double avgCostMonth;
	private String maxCostMonth;
	private AccountRecord maxCost;
	private String maxCostInterval;

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

	public double getAvgCostMonth() {
		return avgCostMonth;
	}

	public void setAvgCostMonth(double avgCostMonth) {
		this.avgCostMonth = avgCostMonth;
	}

	public String getMaxCostMonth() {
		return maxCostMonth;
	}

	public void setMaxCostMonth(String maxCostMonth) {
		this.maxCostMonth = maxCostMonth;
	}

	public AccountRecord getMaxCost() {
		return maxCost;
	}

	public void setMaxCost(AccountRecord maxCost) {
		this.maxCost = maxCost;
	}

	public String getMaxCostInterval() {
		return maxCostInterval;
	}

	public void setMaxCostInterval(String maxCostInterval) {
		this.maxCostInterval = maxCostInterval;
	}

}
