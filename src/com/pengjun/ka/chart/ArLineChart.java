package com.pengjun.ka.chart;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.achartengine.ChartFactory;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.content.Context;
import android.view.View;

import com.pengjun.ka.db.model.AccountRecord;
import com.pengjun.ka.utils.Constants.ChartType;
import com.pengjun.ka.utils.MathUtils;
import com.pengjun.ka.utils.TimeUtils;

public class ArLineChart extends BaseChart {

	private static final int DISPALY_CHART_VALUE_POINT_MAX_NUM = 15;
	private ChartType chartType;

	public ArLineChart(ChartType chartType) {
		this.chartType = chartType;
	}

	private String titles;
	private XYMultipleSeriesRenderer renderer;
	XYMultipleSeriesDataset dataset;

	@Override
	public void compute(List<AccountRecord> arList) {

		// compute each date account
		Map<Date, Double> map = new TreeMap<Date, Double>();
		Double count = null;
		switch (chartType) {
		case line_day:
			for (AccountRecord ar : arList) {

				Date date = TimeUtils.string2Date(ar.getCreateDate());
				count = map.get(date);
				if (count == null) {
					count = 0.0;
				}
				map.put(date, count + ar.getAccount());
			}
			titles = "每天花费总额曲线图";
			break;
		case line_mouth:
			for (AccountRecord ar : arList) {

				Date yearMonth = TimeUtils.string2YearMonth(TimeUtils.getYearMouthFromDateStr(ar
						.getCreateDate()));
				count = map.get(yearMonth);
				if (count == null) {
					count = 0.0;
				}
				map.put(yearMonth, count + ar.getAccount());
			}
			titles = "每月花费总额曲线图";
			break;
		case line_year:
			for (AccountRecord ar : arList) {

				Date year = TimeUtils.string2Year(TimeUtils.getYearFromDateStr(ar.getCreateDate()));
				count = map.get(year);
				if (count == null) {
					count = 0.0;
				}
				map.put(year, count + ar.getAccount());
			}
			titles = "每年花费总额曲线图";
			break;
		}

		int pointCnt = 0;
		Double maxValue = Double.MIN_VALUE;
		Double minValue = Double.MAX_VALUE;
		Date firstDate = null;
		Date lastDate = null;

		dataset = new XYMultipleSeriesDataset();
		TimeSeries series = new TimeSeries(titles);

		for (Map.Entry<Date, Double> entry : map.entrySet()) {
			if (pointCnt == 0) {
				firstDate = entry.getKey();
			}
			if (pointCnt == map.size() - 1) {
				lastDate = entry.getKey();
			}
			maxValue = Math.max(entry.getValue(), maxValue);
			minValue = Math.min(entry.getValue(), minValue);
			series.add(entry.getKey(), MathUtils.formatDouble(entry.getValue()));
			pointCnt++;
		}
		dataset.addSeries(series);

		renderer = createXYChartRenderer("时间", "金额");

		// fill last invalid data

		switch (chartType) {
		case line_day:
			firstDate.setDate(firstDate.getDate() - 1);
			lastDate.setDate(lastDate.getDate() + 1);
			break;
		case line_mouth:
			firstDate.setMonth(firstDate.getMonth() - 1);
			lastDate.setMonth(lastDate.getMonth() + 1);
			break;
		case line_year:
			firstDate.setYear(firstDate.getYear() - 1);
			lastDate.setYear(lastDate.getYear() + 1);
			break;
		}

		renderer.setXLabels(10);
		renderer.setXAxisMin(firstDate.getTime());
		renderer.setXAxisMax(lastDate.getTime());

		// set point to middle when only one point
		if (pointCnt == 1) {
			renderer.setXAxisMin(firstDate.getTime() / 2);
			renderer.setXAxisMax(lastDate.getTime() * 2);
		}

		renderer.setYAxisMin(minValue * 0.5f);
		renderer.setYAxisMax(maxValue * 1.1f);
		if (pointCnt <= DISPALY_CHART_VALUE_POINT_MAX_NUM) {
			renderer.getSeriesRendererAt(0).setDisplayChartValues(true);
		}

	}

	@Override
	public View getView(Context context) {
		View view = null;
		switch (chartType) {
		case line_day:
			view = ChartFactory.getTimeChartView(context, dataset, renderer, TimeUtils.TO_DATE_FORMT);
			break;
		case line_mouth:
			view = ChartFactory.getTimeChartView(context, dataset, renderer, TimeUtils.TO_YERA_MONTH_FORMT);
			break;
		case line_year:
			view = ChartFactory.getTimeChartView(context, dataset, renderer, TimeUtils.TO_YERA_FORMT);
			break;
		}

		return view;
	}

}
