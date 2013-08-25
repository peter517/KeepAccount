package com.pengjun.ka.activity;

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
import com.pengjun.ka.db.model.MagicBoxData;
import com.pengjun.ka.db.service.ArService;
import com.pengjun.ka.db.service.MagicBoxService;
import com.pengjun.ka.utils.Constants.ChartType;
import com.pengjun.ka.utils.MathUtils;
import com.pengjun.ka.utils.ResourceUtils;

public class MagicBoxActivity extends Activity {

	private LinearLayout llTypeRatioChart;
	private ProgressBar pbLoad;
	private ScrollView svMagicData;

	private MagicBoxData magicBoxData;

	TextView tvTotalCountNum;
	TextView tvTotalCost;
	TextView tvAvgCost;
	TextView tvAvgCostMonth;
	TextView tvMaxCostMonth;
	TextView tvMaxCostInterval;

	TextView tvAccount;
	ImageView ivType;
	TextView tvType;
	TextView tvDate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.magic_box_open);

		llTypeRatioChart = (LinearLayout) findViewById(R.id.llTypeRatioChart);

		tvTotalCountNum = (TextView) findViewById(R.id.tvTotalCountNum);
		tvTotalCost = (TextView) findViewById(R.id.tvTotalCost);
		tvAvgCost = (TextView) findViewById(R.id.tvAvgCost);
		tvAvgCostMonth = (TextView) findViewById(R.id.tvAvgCostMonth);
		tvMaxCostMonth = (TextView) findViewById(R.id.tvMaxCostMonth);
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

			List<AccountRecord> arList = ArService.queryAll();
			typeRadioChart = ChartFactory.createChart(ChartType.Pie);
			typeRadioChart.compute(arList);

			magicBoxData = MagicBoxService.getTotalCountNum(arList);
			return null;
		}

		@Override
		protected void onPostExecute(Void v) {

			typeRadioChart.setZoomEnabled(false);

			llTypeRatioChart.addView(typeRadioChart.getView(MagicBoxActivity.this));

			tvTotalCountNum.setText(String.valueOf(magicBoxData.getTotalCountNum()));
			tvTotalCost.setText(MathUtils.double2String(magicBoxData.getTotalCost()));
			tvAvgCost.setText(String.valueOf(magicBoxData.getAvgCost()));
			tvAvgCostMonth.setText(String.valueOf(magicBoxData.getAvgCostMonth()));
			tvMaxCostMonth.setText(magicBoxData.getMaxCostMonth());
			tvMaxCostInterval.setText(magicBoxData.getMaxCostInterval());

			AccountRecord ar = magicBoxData.getMaxCost();
			tvAccount.setText(String.valueOf(ar.getAccount()));
			ivType.setImageResource(ResourceUtils.getImgResIdByName(ar.getImgResName()));
			tvType.setText(ar.getTypeName());
			tvDate.setText(ar.getCreateDate());

			hideProgress();
			super.onPostExecute(v);
		}
	}
}
