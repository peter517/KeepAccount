package com.pengjun.ka.chart;

import java.util.List;
import java.util.Map;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;

import android.content.Context;
import android.view.View;

import com.pengjun.ka.db.model.AccountRecord;
import com.pengjun.utils.CollectionUtils;
import com.pengjun.utils.NumberUtils;
import com.pengjun.utils.ResourceUtils;

/**
 * ratio of each type cost
 * 
 * @author pengjun
 * 
 */
public class ArPieChart extends KaBaseChart {

	private DefaultRenderer renderer;
	private CategorySeries series;
	private CollectionUtils.CountDoubleMap name2CostMap;
	private GraphicalView graphicalView;
	private double costTotal = 0;
	private boolean isShowPercent = false;

	@Override
	public void setZoomEnabled(boolean isZoomEnabled) {
		if (renderer != null) {
			renderer.setZoomEnabled(isZoomEnabled);
			renderer.setZoomButtonsVisible(isZoomEnabled);
		}

	}

	@Override
	public void compute(List<AccountRecord> arList) {

		// compute type distribute
		name2CostMap = new CollectionUtils.CountDoubleMap();
		for (AccountRecord ar : arList) {
			name2CostMap.count(ar.getTypeName(), (double) ar.getAccount());
		}

		int[] colorArr = new int[name2CostMap.size()];
		int k = 0;
		for (Map.Entry<String, Double> entry : name2CostMap.entrySet()) {
			colorArr[k] = ResourceUtils.COLOR_ARR[k % ResourceUtils.COLOR_ARR.length];
			costTotal += entry.getValue();
			k++;
		}

		renderer = createCategoryRenderer("各类花费总额分布比例", colorArr);

		series = new CategorySeries("");

		changeData(isShowPercent);

	}

	private void changeData(boolean isShowPercent) {

		series.clear();
		if (isShowPercent) {
			for (Map.Entry<String, Double> entry : name2CostMap.entrySet()) {
				series.add(entry.getKey() + "(" + NumberUtils.formatDouble(entry.getValue()) + ")",
						entry.getValue());
			}
		} else {
			for (Map.Entry<String, Double> entry : name2CostMap.entrySet()) {
				// ratio of each type , keep two decimal place
				double ratio = NumberUtils.formatDouble(entry.getValue() * 100 / costTotal);
				if (ratio != 0) {
					series.add(entry.getKey() + "(" + ratio + "%)", entry.getValue());
				} else {
					series.add(entry.getKey() + "(近似0.0%)", entry.getValue());
				}
			}
		}
	}

	@Override
	public View getView(Context context) {
		graphicalView = ChartFactory.getPieChartView(context, series, renderer);
		graphicalView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				isShowPercent = !isShowPercent;
				changeData(isShowPercent);
				graphicalView.repaint();
			}
		});
		return graphicalView;
	}
}
