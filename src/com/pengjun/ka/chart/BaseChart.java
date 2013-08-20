package com.pengjun.ka.chart;

import java.util.Date;
import java.util.List;

import org.achartengine.chart.PointStyle;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.MultipleCategorySeries;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.view.View;

import com.pengjun.ka.db.model.AccountRecord;
import com.pengjun.ka.utils.TimeUtils;

public abstract class BaseChart {

	public abstract void compute(List<AccountRecord> arList);

	public abstract View getView(Context context);

	/**
	 * Builds an XY multiple dataset using the provided values.
	 * 
	 * @param titles
	 *            the series titles
	 * @param xValues
	 *            the values for the X axis
	 * @param yValues
	 *            the values for the Y axis
	 * @return the XY multiple dataset
	 */
	protected XYMultipleSeriesDataset buildDataset(String[] titles, List<double[]> xValues,
			List<double[]> yValues) {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		addXYSeries(dataset, titles, xValues, yValues, 0);
		return dataset;
	}

	public void addXYSeries(XYMultipleSeriesDataset dataset, String[] titles, List<double[]> xValues,
			List<double[]> yValues, int scale) {
		int length = titles.length;
		for (int i = 0; i < length; i++) {
			XYSeries series = new XYSeries(titles[i], scale);
			double[] xV = xValues.get(i);
			double[] yV = yValues.get(i);
			int seriesLength = xV.length;
			for (int k = 0; k < seriesLength; k++) {
				series.add(xV[k], yV[k]);
			}
			dataset.addSeries(series);
		}
	}

	/**
	 * Builds an XY multiple series renderer.
	 * 
	 * @param colors
	 *            the series rendering colors
	 * @param styles
	 *            the series point styles
	 * @return the XY multiple series renderers
	 */
	protected XYMultipleSeriesRenderer buildRenderer(int[] colors, PointStyle[] styles) {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		setRenderer(renderer, colors, styles);
		return renderer;
	}

	protected void setRenderer(XYMultipleSeriesRenderer renderer, int[] colors, PointStyle[] styles) {
		renderer.setAxisTitleTextSize(16);
		renderer.setChartTitleTextSize(20);
		renderer.setLabelsTextSize(15);
		renderer.setLegendTextSize(15);
		renderer.setPointSize(5f);
		renderer.setZoomEnabled(true);
		renderer.setMargins(new int[] { 20, 30, 15, 20 });
		int length = colors.length;
		for (int i = 0; i < length; i++) {
			XYSeriesRenderer r = new XYSeriesRenderer();
			r.setColor(colors[i]);
			r.setPointStyle(styles[i]);
			renderer.addSeriesRenderer(r);
		}
	}

	/**
	 * Sets a few of the series renderer settings.
	 * 
	 * @param renderer
	 *            the renderer to set the properties to
	 * @param title
	 *            the chart title
	 * @param xTitle
	 *            the title for the X axis
	 * @param yTitle
	 *            the title for the Y axis
	 * @param xMin
	 *            the minimum value on the X axis
	 * @param xMax
	 *            the maximum value on the X axis
	 * @param yMin
	 *            the minimum value on the Y axis
	 * @param yMax
	 *            the maximum value on the Y axis
	 * @param axesColor
	 *            the axes color
	 * @param labelsColor
	 *            the labels color
	 */
	protected void setChartSettings(XYMultipleSeriesRenderer renderer, String title, String xTitle,
			String yTitle, double xMin, double xMax, double yMin, double yMax, int axesColor, int labelsColor) {
		renderer.setChartTitle(title);
		renderer.setXTitle(xTitle);
		renderer.setYTitle(yTitle);
		renderer.setXAxisMin(xMin);
		renderer.setXAxisMax(xMax);
		renderer.setYAxisMin(yMin);
		renderer.setYAxisMax(yMax);
		renderer.setAxesColor(axesColor);
		renderer.setLabelsColor(labelsColor);
	}

	protected void setXStringChartSettings(XYMultipleSeriesRenderer renderer, String title, String xTitle,
			String yTitle, double yMin, double yMax, int axesColor, int labelsColor) {
		renderer.setChartTitle(title);
		renderer.setLabelsColor(Color.YELLOW);
		renderer.setXTitle(xTitle);
		renderer.setXLabelsColor(Color.GREEN);
		renderer.setYTitle(yTitle);
		renderer.setYLabelsColor(0, Color.GREEN);
		renderer.setXLabelsAlign(Align.CENTER);
		renderer.setYLabelsAlign(Align.CENTER);
		renderer.setXLabels(0);
		renderer.setYAxisMin(yMin);
		renderer.setYAxisMax(yMax);
		renderer.setAxesColor(axesColor);
		renderer.setLabelsColor(labelsColor);
	}

	protected XYMultipleSeriesRenderer createXYChartRenderer(String xTitle, String yTitle) {

		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		XYSeriesRenderer r = new XYSeriesRenderer();
		r.setColor(Color.MAGENTA);
		r.setPointStyle(PointStyle.CIRCLE);

		renderer.setYLabels(5);
		renderer.setPointSize(5f);
		renderer.addSeriesRenderer(r);
		renderer.setLabelsColor(Color.YELLOW);
		renderer.setXTitle(xTitle);
		renderer.setXLabelsColor(Color.GREEN);
		renderer.setYTitle(yTitle);
		renderer.setYLabelsColor(0, Color.GREEN);
		renderer.setXLabelsAlign(Align.CENTER);
		renderer.setYLabelsAlign(Align.CENTER);
		renderer.setAxesColor(Color.GRAY);
		renderer.setAxisTitleTextSize(16);
		renderer.setChartTitleTextSize(20);
		renderer.setLabelsTextSize(15);
		renderer.setLegendTextSize(15);
		renderer.setZoomEnabled(true);
		renderer.setZoomButtonsVisible(true);
		((XYSeriesRenderer) renderer.getSeriesRendererAt(0)).setFillPoints(true);
		((XYSeriesRenderer) renderer.getSeriesRendererAt(0)).setLineWidth(1.5f);

		return renderer;
	}

	/**
	 * Builds an XY multiple time dataset using the provided values.
	 * 
	 * @param titles
	 *            the series titles
	 * @param xValues
	 *            the values for the X axis
	 * @param yValues
	 *            the values for the Y axis
	 * @return the XY multiple time dataset
	 */
	protected XYMultipleSeriesDataset buildDateDataset(String[] titles, List<Date[]> xValues,
			List<double[]> yValues) {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		int length = titles.length;
		for (int i = 0; i < length; i++) {
			TimeSeries series = new TimeSeries(titles[i]);
			Date[] xV = xValues.get(i);
			double[] yV = yValues.get(i);
			int seriesLength = xV.length;
			for (int k = 0; k < seriesLength; k++) {
				series.add(xV[k], yV[k]);
			}
			dataset.addSeries(series);
		}
		return dataset;
	}

	protected XYMultipleSeriesDataset buildTimeDataset(String titles, String[] xValues, double[] yValues) {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		TimeSeries series = new TimeSeries(titles);
		int seriesLength = xValues.length;
		for (int k = 0; k < seriesLength; k++) {
			series.add(TimeUtils.string2Date(xValues[k]), yValues[k]);
		}
		dataset.addSeries(series);
		return dataset;
	}

	protected XYMultipleSeriesDataset buildXStringDataset(String titles, double[] yValues) {

		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		int seriesLength = yValues.length;
		XYSeries series = new XYSeries(titles);

		series.add(0, 0);
		int k = 0;
		for (k = 0; k < seriesLength; k++) {
			series.add(k + 1, yValues[k]);

		}
		series.add(k + 1, 0);
		dataset.addSeries(series);
		return dataset;
	}

	protected XYMultipleSeriesDataset buildDataset(String titles, int[] xValues, double[] yValues) {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		XYSeries series = new XYSeries(titles);
		int seriesLength = xValues.length;
		for (int k = 0; k < seriesLength; k++) {
			series.add(xValues[k], yValues[k]);
		}
		dataset.addSeries(series);
		return dataset;
	}

	/**
	 * Builds a category series using the provided values.
	 * 
	 * @param titles
	 *            the series titles
	 * @param values
	 *            the values
	 * @return the category series
	 */
	protected CategorySeries buildCategoryDataset(String title, String[] items, double[] values) {
		CategorySeries series = new CategorySeries(title);
		int k = 0;
		for (double value : values) {
			series.add(items[k++], value);
		}

		return series;
	}

	/**
	 * Builds a multiple category series using the provided values.
	 * 
	 * @param titles
	 *            the series titles
	 * @param values
	 *            the values
	 * @return the category series
	 */
	protected MultipleCategorySeries buildMultipleCategoryDataset(String title, List<String[]> titles,
			List<double[]> values) {
		MultipleCategorySeries series = new MultipleCategorySeries(title);
		int k = 0;
		for (double[] value : values) {
			series.add(2007 + k + "", titles.get(k), value);
			k++;
		}
		return series;
	}

	/**
	 * Builds a category renderer to use the provided colors.
	 * 
	 * @param colors
	 *            the colors
	 * @return the category renderer
	 */
	protected DefaultRenderer buildCategoryRenderer(int[] colors) {
		DefaultRenderer renderer = new DefaultRenderer();
		renderer.setLabelsTextSize(15);
		renderer.setLegendTextSize(15);
		renderer.setMargins(new int[] { 20, 30, 15, 0 });
		for (int color : colors) {
			SimpleSeriesRenderer r = new SimpleSeriesRenderer();
			r.setColor(color);
			renderer.addSeriesRenderer(r);
		}
		return renderer;
	}

	protected DefaultRenderer createCategoryRenderer(String title, int[] colors) {
		DefaultRenderer renderer = new DefaultRenderer();
		renderer.setChartTitle(title);
		renderer.setLabelsTextSize(15);
		renderer.setLegendTextSize(15);
		renderer.setZoomButtonsVisible(true);
		renderer.setZoomEnabled(true);
		renderer.setChartTitleTextSize(20);
		renderer.setLabelsColor(Color.BLACK);
		renderer.setMargins(new int[] { 20, 30, 15, 0 });
		for (int color : colors) {
			SimpleSeriesRenderer r = new SimpleSeriesRenderer();
			r.setColor(color);
			renderer.addSeriesRenderer(r);
		}
		return renderer;
	}

	/**
	 * Builds a bar multiple series dataset using the provided values.
	 * 
	 * @param titles
	 *            the series titles
	 * @param values
	 *            the values
	 * @return the XY multiple bar dataset
	 */
	protected XYMultipleSeriesDataset buildBarDataset(String[] titles, List<double[]> values) {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		int length = titles.length;
		for (int i = 0; i < length; i++) {
			CategorySeries series = new CategorySeries(titles[i]);
			double[] v = values.get(i);
			int seriesLength = v.length;
			for (int k = 0; k < seriesLength; k++) {
				series.add(v[k]);
			}
			dataset.addSeries(series.toXYSeries());
		}
		return dataset;
	}

	protected XYMultipleSeriesDataset buildBarDataset(String titles, double[] values) {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		CategorySeries series = new CategorySeries(titles);
		int seriesLength = values.length;
		for (int k = 0; k < seriesLength; k++) {
			series.add(values[k]);
		}
		dataset.addSeries(series.toXYSeries());
		return dataset;
	}

	/**
	 * Builds a bar multiple series renderer to use the provided colors.
	 * 
	 * @param colors
	 *            the series renderers colors
	 * @return the bar multiple series renderer
	 */
	protected XYMultipleSeriesRenderer buildBarRenderer(int[] colors) {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		renderer.setAxisTitleTextSize(16);
		renderer.setChartTitleTextSize(20);
		renderer.setLabelsTextSize(15);
		renderer.setLegendTextSize(15);
		int length = colors.length;
		for (int i = 0; i < length; i++) {
			XYSeriesRenderer r = new XYSeriesRenderer();
			r.setColor(colors[i]);
			renderer.addSeriesRenderer(r);
		}
		return renderer;
	}

}
