package com.pengjun.ka.chart;

import com.pengjun.ka.utils.Constants.ChartType;

public class ChartFactory {

	public static BaseChart createChart(ChartType chartType) {

		switch (chartType) {
		case bar:
			return new ArBarChart();
		case pie:
			return new ArPieChart();
		case line:
			return new ArLineChart();
		}
		return null;
	}
}
