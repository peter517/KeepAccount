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

/**
 * time-line of money
 * 
 * @author pengjun
 * 
 */
public class ArLineChart extends BaseChart {

	private static final int DISPALY_CHART_VALUE_POINT_MAX_NUM = 15;
	private ChartType chartType;

	public ArLineChart(ChartType chartType) {
		this.chartType = chartType;
	}

	private String title;
	private XYMultipleSeriesRenderer renderer;
	XYMultipleSeriesDataset dataset;

	@Override
	public void setZoomEnabled(boolean isZoomEnabled) {
		if (renderer != null) {
			renderer.setZoomEnabled(isZoomEnabled);
		}

	}

	@Override
	public void compute(List<AccountRecord> arList) {

		// compute each date account
		Map<Date, Double> map = new TreeMap<Date, Double>();
		Double account = null;
		switch (chartType) {
		case LineDay:
			for (AccountRecord ar : arList) {

				Date date = TimeUtils.string2Date(ar.getCreateDate());
				account = map.get(date);
				if (account == null) {
					account = 0.0;
				}
				map.put(date, account + ar.getAccount());
			}
			title = "每天花费总额曲线图";
			break;
		case LineMonth:
			for (AccountRecord ar : arList) {

				Date yearMonth = TimeUtils.string2YearMonthDate(TimeUtils.String2YearMonthDateStr(ar
						.getCreateDate()));
				account = map.get(yearMonth);
				if (account == null) {
					account = 0.0;
				}
				map.put(yearMonth, account + ar.getAccount());
			}
			title = "每月花费总额曲线图";
			break;
		case LineYear:
			for (AccountRecord ar : arList) {

				Date year = TimeUtils.string2YearDate(TimeUtils.String2YearDateStr(ar.getCreateDate()));
				account = map.get(year);
				if (account == null) {
					account = 0.0;
				}
				map.put(year, account + ar.getAccount());
			}
			title = "每年花费总额曲线图";
			break;
		}

		int pointCnt = 0;
		Double maxValue = Double.MIN_VALUE;
		Double minValue = Double.MAX_VALUE;
		Date firstDate = null;
		Date lastDate = null;

		dataset = new XYMultipleSeriesDataset();
		TimeSeries series = new TimeSeries(title);

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
		case LineDay:
			firstDate.setDate(firstDate.getDate() - 1);
			lastDate.setDate(lastDate.getDate() + 1);
			break;
		case LineMonth:
			firstDate.setMonth(firstDate.getMonth() - 1);
			lastDate.setMonth(lastDate.getMonth() + 1);
			break;
		case LineYear:
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
		case LineDay:
			view = ChartFactory.getTimeChartView(context, dataset, renderer, TimeUtils.DATE_FORMT);
			break;
		case LineMonth:
			view = ChartFactory.getTimeChartView(context, dataset, renderer, TimeUtils.YERA_MONTH_FORMT);
			break;
		case LineYear:
			view = ChartFactory.getTimeChartView(context, dataset, renderer, TimeUtils.YERA_FORMT);
			break;
		}

		return view;
	}

}
