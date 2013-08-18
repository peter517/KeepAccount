package com.pengjun.ka.chart;

import com.pengjun.ka.utils.Constants.ChartType;

public class ChartFactory {

	public static BaseChart createChart(ChartType chartType) {

		switch (chartType) {
		case bar:
			return new ArBarChart();
		case pie:
			return new ArPieChart();
		case line_day:
			return new ArLineChart(ChartType.line_day);
		case line_mouth:
			return new ArLineChart(ChartType.line_mouth);
		case line_year:
			return new ArLineChart(ChartType.line_year);
		}
		return null;
	}
}
