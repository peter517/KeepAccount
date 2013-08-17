package com.pengjun.ka.chart;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.pengjun.ka.db.model.AccountRecord;
import com.pengjun.ka.utils.MathUtils;

public class ArBarChart extends BaseChart {

	private String titles;
	private XYMultipleSeriesRenderer renderer;
	private XYMultipleSeriesDataset dataset;

	@Override
	public void compute(List<AccountRecord> arList) {

		titles = "各类型消费总数";

		// compute each date account
		Map<String, Double> map = new TreeMap<String, Double>();
		for (AccountRecord ar : arList) {
			Double count = map.get(ar.getTypeName());
			if (count == null) {
				count = 0.0;
			}
			map.put(ar.getTypeName(), count + ar.getAccount());
		}

		renderer = createXYChartRenderer("种类", "金额");

		dataset = new XYMultipleSeriesDataset();
		XYSeries series = new XYSeries(titles);

		// fill first invalid data
		series.add(0, 0);
		renderer.addXTextLabel(0, "");

		int k = 0;
		Double maxValue = Double.MIN_VALUE;
		Double minValue = Double.MAX_VALUE;
		for (Map.Entry<String, Double> entry : map.entrySet()) {
			maxValue = Math.max(entry.getValue(), maxValue);
			minValue = Math.max(entry.getValue(), minValue);
			renderer.addTextLabel(k + 1, entry.getKey());
			series.add(k + 1, MathUtils.formatDouble(entry.getValue()));
			k++;
		}

		// fill last invalid data
		series.add(k + 1, 0);
		renderer.addXTextLabel(k + 1, "");

		dataset.addSeries(series);

		renderer.setYAxisMin(minValue);
		renderer.setYAxisMax(maxValue * 1.1f);
		renderer.setXLabels(0);
		renderer.getSeriesRendererAt(0).setDisplayChartValues(true);
		renderer.setBarSpacing(2.0f);
		renderer.setShowGrid(true);
		renderer.setGridColor(Color.BLUE);

	}

	@Override
	public View getView(Context context) {
		View view = ChartFactory.getBarChartView(context, dataset, renderer, Type.DEFAULT);
		return view;
	}
}
