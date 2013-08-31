package com.pengjun.ka.db.model;

public class BaseReport {

	private long totalCountNum;
	private double totalCost;
	private double avgCost;

	private String maxCostDay;
	private double avgCostPerDay;
	private AccountRecord maxCost;
	private String costKeyWord;

	private String maxCostInterval;

	public String getCostKeyWord() {
		return costKeyWord;
	}

	public void setCostKeyWord(String costKeyWord) {
		this.costKeyWord = costKeyWord;
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

	public double getAvgCostPerDay() {
		return avgCostPerDay;
	}

	public void setAvgCostPerDay(double avgCostPerDay) {
		this.avgCostPerDay = avgCostPerDay;
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
