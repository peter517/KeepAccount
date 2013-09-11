package com.pengjun.ka.android.activity.report;

import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import com.pengjun.ka.R;
import com.pengjun.ka.chart.KaBaseChart;
import com.pengjun.ka.db.dao.ArDao;
import com.pengjun.ka.db.model.AccountRecord;
import com.pengjun.ka.db.model.MagicBoxData;
import com.pengjun.ka.db.service.MagicBoxService;

public class MagicBoxActivity extends BaseReportActivity {

	private TextView tvAvgCostMonth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initLayout(R.layout.magic_box_open, getResources().getString(R.string.magicBox));
		tvAvgCostMonth = (TextView) findViewById(R.id.tvAvgCostMonth);
		showProgress();
		new LoadArChartTask().execute();

	}

	class LoadArChartTask extends AsyncTask<Void, Void, Void> {

		KaBaseChart typeRadioChart;
		MagicBoxData magicBoxData;

		@Override
		protected Void doInBackground(Void... params) {

			List<AccountRecord> arList = ArDao.getSingleInstance().queryAll();
			typeRadioChart = createBaseChart(arList);
			magicBoxData = MagicBoxService.computeMagicBoxData(arList);
			return null;
		}

		@Override
		protected void onPostExecute(Void v) {

			fillBaseReport(magicBoxData, typeRadioChart);
			tvAvgCostMonth.setText(String.valueOf(magicBoxData.getAvgCostMonth()));
			hideProgress();
			super.onPostExecute(v);
		}
	}
}
