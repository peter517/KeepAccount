package com.pengjun.ka.android.activity;

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
import com.pengjun.ka.db.dao.ArDao;
import com.pengjun.ka.db.model.AccountRecord;
import com.pengjun.ka.db.model.MagicBoxData;
import com.pengjun.ka.db.service.MagicBoxService;
import com.pengjun.ka.utils.Constants.ChartType;
import com.pengjun.ka.utils.NumberUtils;
import com.pengjun.ka.utils.ResourceUtils;

public class MagicBoxActivity extends Activity {

	private LinearLayout llTypeRatioChart;
	private ProgressBar pbLoad;
	private ScrollView svMagicData;

	private MagicBoxData magicBoxData;

	private TextView tvTotalCountNum;
	private TextView tvTotalCost;
	private TextView tvAvgCost;
	private TextView tvAvgCostMonth;
	private TextView tvMaxCostMonth;
	private TextView tvMaxCostInterval;
	private TextView tvCostKeyWord;

	private TextView tvAccount;
	private ImageView ivType;
	private TextView tvType;
	private TextView tvDate;

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
		tvCostKeyWord = (TextView) findViewById(R.id.tvCostKeyWord);

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

			List<AccountRecord> arList = ArDao.queryAll();
			typeRadioChart = ChartFactory.createChart(ChartType.Pie);
			typeRadioChart.compute(arList);

			magicBoxData = MagicBoxService.computeMagicBoxData(arList);
			return null;
		}

		@Override
		protected void onPostExecute(Void v) {

			typeRadioChart.setZoomEnabled(false);

			llTypeRatioChart.addView(typeRadioChart.getView(MagicBoxActivity.this));

			tvTotalCountNum.setText(String.valueOf(magicBoxData.getTotalCountNum()));
			tvTotalCost.setText(NumberUtils.double2String(magicBoxData.getTotalCost()));
			tvAvgCost.setText(String.valueOf(magicBoxData.getAvgCost()));
			tvAvgCostMonth.setText(String.valueOf(magicBoxData.getAvgCostMonth()));
			tvMaxCostMonth.setText(magicBoxData.getMaxCostMonth());
			tvMaxCostInterval.setText(magicBoxData.getMaxCostInterval());
			if (magicBoxData.getCostKeyWord() != null) {
				tvCostKeyWord.setText(magicBoxData.getCostKeyWord());
			} else {
				tvCostKeyWord.setText("无");
			}

			AccountRecord ar = magicBoxData.getMaxCost();
			tvAccount.setText(String.valueOf(ar.getAccount()));
			ivType.setImageResource(ResourceUtils.getImgResIdByResName(ar.getImgResName()));
			tvType.setText(ar.getTypeName());
			tvDate.setText(ar.getCreateDate());

			hideProgress();
			super.onPostExecute(v);
		}
	}
}
