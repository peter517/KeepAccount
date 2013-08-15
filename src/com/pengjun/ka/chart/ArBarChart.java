package com.pengjun.ka.chart;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.view.View;

import com.pengjun.ka.db.model.AccountRecord;

public class ArBarChart extends AbstractChart {

	@Override
	public View getView(Context context, List<AccountRecord> arList, CallBack cb) {
		String[] titles = new String[] { "2008", "2007" };
		List<double[]> values = new ArrayList<double[]>();
		values.add(new double[] { 14230, 12300, 14240, 15244, 15900, 19200, 22030, 21200, 19500, 15500,
				12600, 14000 });
		values.add(new double[] { 5230, 7300, 9240, 10540, 7900, 9200, 12030, 11200, 9500, 10500, 11600,
				13500 });
		int[] colors = new int[] { Color.BLUE, Color.CYAN };
		XYMultipleSeriesRenderer renderer = buildBarRenderer(colors);
		setChartSettings(renderer, "Monthly sales in the last 2 years", "Month", "Units sold", 0.5, 12.5, 0,
				24000, Color.GRAY, Color.LTGRAY);
		renderer.getSeriesRendererAt(0).setDisplayChartValues(true);
		renderer.getSeriesRendererAt(1).setDisplayChartValues(true);
		renderer.setXLabels(12);
		renderer.setYLabels(10);
		renderer.setXLabelsAlign(Align.LEFT);
		renderer.setYLabelsAlign(Align.LEFT);
		renderer.setPanEnabled(true, false);
		// renderer.setZoomEnabled(false);
		renderer.setZoomRate(1.1f);
		renderer.setBarSpacing(0.5f);
		View view = ChartFactory.getBarChartView(context, buildBarDataset(titles, values), renderer,
				Type.STACKED);
		return view;
	}

}
