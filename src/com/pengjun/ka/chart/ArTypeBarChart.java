package com.pengjun.ka.chart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.pengjun.ka.db.model.AccountRecord;
import com.pengjun.ka.utils.NumberUtils;

/**
 * count of each type cost
 * 
 * @author pengjun
 * 
 */
public class ArTypeBarChart extends BaseChart {

	private String title;
	private XYMultipleSeriesRenderer renderer;
	private XYMultipleSeriesDataset dataset;

	@Override
	public void setZoomEnabled(boolean isZoomEnabled) {
		if (renderer != null) {
			renderer.setZoomEnabled(isZoomEnabled);
		}

	}

	@Override
	public void compute(List<AccountRecord> arList) {

		title = "各类型花费总额";

		// compute each date account
		Map<String, Double> map = new HashMap<String, Double>();
		Double account = null;
		for (AccountRecord ar : arList) {
			account = map.get(ar.getTypeName());
			if (account == null) {
				account = 0.0;
			}
			map.put(ar.getTypeName(), account + ar.getAccount());
		}

		renderer = createXYChartRenderer("种类", "金额");

		dataset = new XYMultipleSeriesDataset();
		XYSeries series = new XYSeries(title);

		// fill first invalid data
		series.add(0, 0);
		renderer.addXTextLabel(0, "");
		int pointCnt = 0;
		Double maxValue = Double.MIN_VALUE;
		Double minValue = Double.MAX_VALUE;
		for (Map.Entry<String, Double> entry : map.entrySet()) {
			maxValue = Math.max(entry.getValue(), maxValue);
			minValue = Math.min(entry.getValue(), minValue);
			renderer.addTextLabel(pointCnt + 1, entry.getKey());
			series.add(pointCnt + 1, NumberUtils.formatDouble(entry.getValue()));
			pointCnt++;
		}

		// fill last invalid data
		series.add(pointCnt + 1, 0);
		renderer.addXTextLabel(pointCnt + 1, "");

		dataset.addSeries(series);

		renderer.setYAxisMin(minValue * 0.5f);
		renderer.setYAxisMax(maxValue * 1.1f);
		renderer.setXLabels(0);
		renderer.setBarSpacing(2.0f);
		renderer.setShowGrid(true);
		renderer.setGridColor(Color.BLUE);
		renderer.getSeriesRendererAt(0).setDisplayChartValues(true);

	}

	@Override
	public View getView(Context context) {
		View view = ChartFactory.getBarChartView(context, dataset, renderer, Type.DEFAULT);
		return view;
	}
}
