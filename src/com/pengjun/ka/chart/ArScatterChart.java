package com.pengjun.ka.chart;

import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.pengjun.ka.db.model.AccountRecord;
import com.pengjun.ka.utils.TimeUtils;

public class ArScatterChart extends BaseChart {

	private String titles;
	private XYMultipleSeriesRenderer renderer;
	private double[] valueArr;
	private int[] dayArr;

	@Override
	public void setZoomEnabled(boolean isZoomEnabled) {
		if (renderer != null) {
			renderer.setZoomEnabled(isZoomEnabled);
		}

	}

	@Override
	public void compute(List<AccountRecord> arList) {

		titles = "每月消费分布图";

		Double maxValue = Double.MIN_VALUE;
		Double minValue = Double.MAX_VALUE;
		valueArr = new double[arList.size()];
		dayArr = new int[arList.size()];
		int k = 0;
		for (AccountRecord ar : arList) {
			valueArr[k] = ar.getAccount();
			dayArr[k] = TimeUtils.string2Date(ar.getCreateDate()).getDate();
			maxValue = Math.max(ar.getAccount(), maxValue);
			minValue = Math.max(ar.getAccount(), minValue);
			k++;
		}

		int[] colors = new int[] { Color.GREEN };
		PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE };
		renderer = buildRenderer(colors, styles);
		renderer.setXLabels(10);
		renderer.setYLabels(10);
		((XYSeriesRenderer) renderer.getSeriesRendererAt(0)).setFillPoints(true);
		setChartSettings(renderer, "每月消费分布图", "日", "金额", 1, 31, minValue, maxValue, Color.GRAY, Color.BLACK);

	}

	@Override
	public View getView(Context context) {
		View view = ChartFactory.getScatterChartView(context, buildDataset(titles, dayArr, valueArr),
				renderer);
		return view;
	}
}
