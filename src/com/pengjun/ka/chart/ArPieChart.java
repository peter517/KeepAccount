package com.pengjun.ka.chart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.achartengine.ChartFactory;
import org.achartengine.renderer.DefaultRenderer;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.pengjun.ka.db.model.AccountRecord;
import com.pengjun.ka.utils.ResManageUtils;

public class ArPieChart extends BaseChart {

	private double[] valueArr;
	private String[] typeArr;
	private DefaultRenderer renderer;

	@Override
	public View getView(Context context) {

		View view = ChartFactory.getPieChartView(context,
				buildCategoryDataset("type distribute", typeArr, valueArr), renderer);
		return view;
	}

	@Override
	public void compute(List<AccountRecord> arList) {

		// compute type distribute
		Map<String, Double> map = new HashMap<String, Double>();
		for (AccountRecord ar : arList) {
			Double count = map.get(ar.getTypeName());
			if (count == null) {
				count = 0.0;
			}
			map.put(ar.getTypeName(), ++count);
		}

		int[] colorArr = new int[map.size()];
		typeArr = new String[map.size()];
		valueArr = new double[map.size()];
		int k = 0;
		double total = 0;
		for (Map.Entry<String, Double> entry : map.entrySet()) {
			typeArr[k] = entry.getKey();
			valueArr[k] = entry.getValue();
			colorArr[k] = ResManageUtils.colors[k % ResManageUtils.colors.length];
			total += valueArr[k];
			k++;
		}

		// ratio of each type
		for (int i = 0; i < k; i++) {
			// keep two decimal place
			typeArr[i] += "(" + ((int) (valueArr[i] / total * 10000)) / 100.0 + "%)";
		}

		renderer = buildCategoryRenderer(colorArr);
		renderer.setZoomButtonsVisible(true);
		renderer.setZoomEnabled(true);
		renderer.setChartTitleTextSize(20);
		renderer.setLabelsColor(Color.BLACK);

	}
}
