package com.pengjun.ka.android.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.pengjun.ka.R;
import com.pengjun.ka.chart.BaseChart;
import com.pengjun.ka.chart.ChartFactory;
import com.pengjun.ka.db.model.AccountRecord;
import com.pengjun.ka.db.model.ReportData;
import com.pengjun.ka.db.service.ReportNotificationService;
import com.pengjun.ka.utils.Constants.ChartType;
import com.pengjun.ka.utils.NumberUtils;
import com.pengjun.ka.utils.ResourceUtils;

public class ReportActivity extends Activity {

	private LinearLayout llTypeRatioChart;
	private ProgressBar pbLoad;
	private ScrollView svMagicData;

	private ReportData reportData;

	private TextView tvTotalCountNum;
	private TextView tvTotalCost;
	private TextView tvAvgCost;
	private TextView tvMaxCostDay;
	private TextView tvMaxCostInterval;

	private TextView tvAccount;
	private ImageView ivType;
	private TextView tvType;
	private TextView tvDate;

	private List<AccountRecord> arList = new ArrayList<AccountRecord>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.report);

		arList = ReportNotificationService.arList;

		llTypeRatioChart = (LinearLayout) findViewById(R.id.llTypeRatioChart);

		tvTotalCountNum = (TextView) findViewById(R.id.tvTotalCountNum);
		tvTotalCost = (TextView) findViewById(R.id.tvTotalCost);
		tvAvgCost = (TextView) findViewById(R.id.tvAvgCost);
		tvMaxCostDay = (TextView) findViewById(R.id.tvMaxCostDay);
		tvMaxCostInterval = (TextView) findViewById(R.id.tvMaxCostInterval);

		tvAccount = (TextView) findViewById(R.id.tvCost);
		ivType = (ImageView) findViewById(R.id.ivType);
		tvDate = (TextView) findViewById(R.id.tvDate);
		tvType = (TextView) findViewById(R.id.tvType);

		pbLoad = (ProgressBar) findViewById(R.id.pbLoad);
		svMagicData = (ScrollView) findViewById(R.id.svMagicData);

		showProgress();
		new LoadArChartTask().execute();
	}

	private void showProgress() {
		pbLoad.setVisibility(View.VISIBLE);
		svMagicData.setVisibility(View.GONE);
	}

	private void hideProgress() {
		pbLoad.setVisibility(View.GONE);
		svMagicData.setVisibility(View.VISIBLE);
	}

	class LoadArChartTask extends AsyncTask<Void, Void, Void> {

		BaseChart typeRadioChart;

		@Override
		protected Void doInBackground(Void... params) {

			typeRadioChart = ChartFactory.createChart(ChartType.Pie);
			typeRadioChart.compute(arList);

			reportData = ReportNotificationService.computeMagicBoxData(arList);
			return null;
		}

		@Override
		protected void onPostExecute(Void v) {

			typeRadioChart.setZoomEnabled(false);

			llTypeRatioChart.addView(typeRadioChart.getView(ReportActivity.this));

			tvTotalCountNum.setText(String.valueOf(reportData.getTotalCountNum()));
			tvTotalCost.setText(NumberUtils.double2String(reportData.getTotalCost()));
			tvAvgCost.setText(String.valueOf(reportData.getAvgCost()));
			tvMaxCostDay.setText(reportData.getMaxCostDay());
			tvMaxCostInterval.setText(reportData.getMaxCostInterval());

			AccountRecord ar = reportData.getMaxCost();
			tvAccount.setText(String.valueOf(ar.getAccount()));
			ivType.setImageResource(ResourceUtils.getImgResIdByResName(ar.getImgResName()));
			tvType.setText(ar.getTypeName());
			tvDate.setText(ar.getCreateDate());

			hideProgress();
			super.onPostExecute(v);
		}
	}
}
