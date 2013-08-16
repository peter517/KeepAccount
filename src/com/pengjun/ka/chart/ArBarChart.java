package com.pengjun.ka.chart;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.achartengine.ChartFactory;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.view.View;

import com.pengjun.ka.db.model.AccountRecord;
import com.pengjun.ka.utils.TimeUtils;

public class ArBarChart extends BaseChart {

	private String titles;
	private XYMultipleSeriesRenderer renderer;
	private double[] valueArr;
	String[] dateStrArr;

	@Override
	public void compute(List<AccountRecord> arList) {

		titles = "消费趋势图";

		// compute each date account
		Map<String, Double> map = new TreeMap<String, Double>();
		for (AccountRecord ar : arList) {
			Double count = map.get(ar.getCreateDate());
			if (count == null) {
				count = 0.0;
			}
			map.put(ar.getCreateDate(), count + ar.getAccount());
		}

		valueArr = new double[map.size()];
		dateStrArr = new String[map.size()];
		int k = 0;
		Double maxValue = Double.MIN_VALUE;
		Double minValue = Double.MAX_VALUE;
		String firstDate = null;
		String lastDate = null;
		for (Map.Entry<String, Double> entry : map.entrySet()) {
			if (k == 0) {
				firstDate = entry.getKey();
			}
			if (k == map.size() - 1) {
				lastDate = entry.getKey();
			}
			dateStrArr[k] = entry.getKey();
			valueArr[k] = entry.getValue();
			maxValue = Math.max(valueArr[k], maxValue);
			minValue = Math.max(valueArr[k], minValue);
			k++;
		}

		int[] colors = new int[] { Color.BLUE };
		renderer = buildBarRenderer(colors);
		setChartSettings(renderer, "消费趋势图", "时间", "金额", TimeUtils.string2Date(firstDate).getTime(), TimeUtils
				.string2Date(lastDate).getTime(), minValue, maxValue, Color.GRAY, Color.BLACK);
		renderer.getSeriesRendererAt(0).setDisplayChartValues(true);
		renderer.setXLabels(12);
		renderer.setYLabels(10);
		renderer.setXLabelsAlign(Align.LEFT);
		renderer.setYLabelsAlign(Align.LEFT);
		renderer.setPanEnabled(true, false);
		renderer.setZoomEnabled(true);
		renderer.setZoomRate(1.1f);
		renderer.setBarSpacing(0.5f);
	}

	@Override
	public View getView(Context context) {

		View view = ChartFactory.getTimeChartView(context, buildDateDataset(titles, dateStrArr, valueArr),
				renderer, "MM/dd/yyyy");
		return view;
	}
}
