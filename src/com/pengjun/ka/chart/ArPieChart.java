package com.pengjun.ka.chart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;

import android.content.Context;
import android.view.View;

import com.pengjun.ka.db.model.AccountRecord;
import com.pengjun.ka.utils.MathUtils;
import com.pengjun.ka.utils.MyDebug;
import com.pengjun.ka.utils.ResManageUtils;

public class ArPieChart extends BaseChart {

	private DefaultRenderer renderer;
	private CategorySeries series;

	@Override
	public void compute(List<AccountRecord> arList) {

		// compute type distribute
		Map<String, Double> map = new HashMap<String, Double>();
		Double count = null;
		for (AccountRecord ar : arList) {
			count = map.get(ar.getTypeName());
			if (count == null) {
				count = 0.0;
			}
			map.put(ar.getTypeName(), ++count);
		}
		series = new CategorySeries("");

		int[] colorArr = new int[map.size()];
		int k = 0;
		double total = 0;

		for (Map.Entry<String, Double> entry : map.entrySet()) {
			total += entry.getValue();
		}
		for (Map.Entry<String, Double> entry : map.entrySet()) {
			colorArr[k] = ResManageUtils.colors[k % ResManageUtils.colors.length];
			// ratio of each type , keep two decimal place
			double ratio = MathUtils.formatDouble(entry.getValue() * 100 / total);
			if (ratio != 0) {
				series.add(entry.getKey() + "(" + ratio + "%)", entry.getValue());
			} else {
				series.add(entry.getKey() + "(近似0.0%)", entry.getValue());
			}

			k++;
		}

		renderer = createCategoryRenderer("各类花费总额分布比例", colorArr);
	}

	@Override
	public View getView(Context context) {
		long time = System.currentTimeMillis();
		View view = ChartFactory.getPieChartView(context, series, renderer);
		MyDebug.printFromPJ("getPieChartView " + (System.currentTimeMillis() - time));
		return view;
	}

}
