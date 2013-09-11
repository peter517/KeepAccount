package com.pengjun.ka.chart;

import java.util.List;
import java.util.Map;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.pengjun.ka.db.model.AccountRecord;
import com.pengjun.utils.CollectionUtils;

/**
 * number of each cost interval
 * 
 * @author pengjun
 * 
 */
public class ArBarChart extends KaBaseChart {

	private String[] MONEY_RANGE_ARR = { "100以下", "101-500", "501-1000", "1001-2000", "2000以上" };
	private String title = "各区间花费数";
	private XYMultipleSeriesRenderer renderer;
	private XYMultipleSeriesDataset dataset;
	private GraphicalView graphicalView;

	@Override
	public void compute(List<AccountRecord> arList) {

		// compute each date account

		CollectionUtils.CountIntegerMap map = new CollectionUtils.CountIntegerMap();
		String range = null;
		for (AccountRecord ar : arList) {
			if (ar.getAccount() <= 100) {
				range = MONEY_RANGE_ARR[0];
			} else if (ar.getAccount() > 100 && ar.getAccount() <= 500) {
				range = MONEY_RANGE_ARR[1];
			} else if (ar.getAccount() > 500 && ar.getAccount() <= 1000) {
				range = MONEY_RANGE_ARR[2];
			} else if (ar.getAccount() > 1000 && ar.getAccount() <= 2000) {
				range = MONEY_RANGE_ARR[3];
			} else if (ar.getAccount() > 2000) {
				range = MONEY_RANGE_ARR[4];
			}
			map.count(range, 1);
		}

		renderer = createXYChartRenderer("金额区间", "数量");

		dataset = new XYMultipleSeriesDataset();
		XYSeries series = new XYSeries(title);

		// fill first invalid data
		series.add(0, 0);
		renderer.addXTextLabel(0, "");

		int pointCnt = 0;
		Integer maxValue = Integer.MIN_VALUE;
		Integer minValue = Integer.MAX_VALUE;
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			maxValue = Math.max(entry.getValue(), maxValue);
			minValue = Math.min(entry.getValue(), minValue);
			renderer.addTextLabel(pointCnt + 1, entry.getKey());
			series.add(pointCnt + 1, entry.getValue());
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
		graphicalView = ChartFactory.getBarChartView(context, dataset, renderer, Type.DEFAULT);
		return graphicalView;
	}

	@Override
	public void setZoomEnabled(boolean isZoomEnabled) {
		if (renderer != null) {
			renderer.setZoomEnabled(isZoomEnabled);
			renderer.setZoomButtonsVisible(isZoomEnabled);
		}

	}
}
