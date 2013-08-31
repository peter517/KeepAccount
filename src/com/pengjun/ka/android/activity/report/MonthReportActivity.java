package com.pengjun.ka.android.activity.report;

import android.os.AsyncTask;
import android.os.Bundle;

import com.pengjun.ka.R;
import com.pengjun.ka.chart.BaseChart;
import com.pengjun.ka.db.model.WeekOrMonthReport;
import com.pengjun.ka.db.service.MonthReportService;
import com.pengjun.ka.utils.ComponentUtils;
import com.pengjun.ka.utils.KaConstants;

public class MonthReportActivity extends BaseReportActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String topTitle = (String) ComponentUtils.getIntentData(getIntent(), KaConstants.INTENT_TOP_TITLE);
		initLayout(R.layout.week_or_month_report, topTitle);
		showProgress();
		new LoadArChartTask().execute();
	}

	class LoadArChartTask extends AsyncTask<Void, Void, Void> {

		BaseChart typeRadioChart;
		WeekOrMonthReport reportData;

		@Override
		protected Void doInBackground(Void... params) {

			typeRadioChart = createBaseChart(arList);
			reportData = MonthReportService.computeReportData(arList);
			return null;
		}

		@Override
		protected void onPostExecute(Void v) {

			fillBaseReport(reportData, typeRadioChart);
			hideProgress();
			super.onPostExecute(v);
		}
	}
}
