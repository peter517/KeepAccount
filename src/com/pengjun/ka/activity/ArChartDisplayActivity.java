package com.pengjun.ka.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.pengjun.ka.R;
import com.pengjun.ka.chart.BaseChart;
import com.pengjun.ka.chart.ChartFactory;
import com.pengjun.ka.db.model.AccountRecord;
import com.pengjun.ka.utils.Constants;
import com.pengjun.ka.utils.Constants.ChartType;

public class ArChartDisplayActivity extends Activity {

	private ProgressBar pbLoad;
	private RelativeLayout rlChart;
	private ChartType chartType;
	private List<AccountRecord> arList = new ArrayList<AccountRecord>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// arList maybe too large to transfers by Intent
		arList = ArChartActivity.arList;

		chartType = (ChartType) getIntent().getExtras().getSerializable(Constants.INTENT_AR_CHART_TYPE);

		setContentView(R.layout.ar_chart_display);

		pbLoad = (ProgressBar) findViewById(R.id.pbLoad);
		rlChart = (RelativeLayout) findViewById(R.id.rlChart);

		showProgress();
		new LoadArChartTask().execute();
	}

	private void showProgress() {
		pbLoad.setVisibility(View.VISIBLE);
		rlChart.setVisibility(View.GONE);
	}

	private void hideProgress() {
		pbLoad.setVisibility(View.GONE);
		rlChart.setVisibility(View.VISIBLE);
	}

	class LoadArChartTask extends AsyncTask<Void, Void, Void> {

		BaseChart baseChart;

		@Override
		protected Void doInBackground(Void... params) {

			baseChart = ChartFactory.createChart(chartType);
			baseChart.compute(arList);
			return null;
		}

		@Override
		protected void onPostExecute(Void v) {
			// must done by main thread
			rlChart.addView(baseChart.getView(ArChartDisplayActivity.this));

			hideProgress();
			super.onPostExecute(v);
		}
	}

}
