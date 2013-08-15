package com.pengjun.ka.chart;

import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.renderer.DefaultRenderer;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.pengjun.ka.db.model.AccountRecord;

public class ArPieChart extends AbstractChart {

	@Override
	public View getView(Context context, List<AccountRecord> arList, CallBack cb) {
		double[] values = new double[] { 12, 14, 11, 10, 19 };
		int[] colors = new int[] { Color.BLUE, Color.GREEN, Color.MAGENTA, Color.YELLOW, Color.CYAN };
		DefaultRenderer renderer = buildCategoryRenderer(colors);
		renderer.setZoomButtonsVisible(true);
		renderer.setZoomEnabled(true);
		renderer.setChartTitleTextSize(20);

		View view = ChartFactory.getPieChartView(context, buildCategoryDataset("Project budget", values),
				renderer);
		return view;
	}

}
