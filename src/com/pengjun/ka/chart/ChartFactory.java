package com.pengjun.ka.chart;

import com.pengjun.ka.utils.KaConstants.ChartType;

public class ChartFactory {

	public static KaBaseChart createChart(ChartType chartType) {

		switch (chartType) {
		case Bar:
			return new ArBarChart();
		case Pie:
			return new ArPieChart();
		case LineDay:
			return new ArLineChart(ChartType.LineDay);
		case LineMonth:
			return new ArLineChart(ChartType.LineMonth);
		case LineYear:
			return new ArLineChart(ChartType.LineYear);
		}
		return null;
	}
}
