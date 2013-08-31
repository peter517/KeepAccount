package com.pengjun.ka.android.activity.report;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.pengjun.ka.R;
import com.pengjun.ka.android.service.ReportNotificationService;
import com.pengjun.ka.chart.BaseChart;
import com.pengjun.ka.chart.ChartFactory;
import com.pengjun.ka.db.model.AccountRecord;
import com.pengjun.ka.db.model.BaseReport;
import com.pengjun.ka.utils.KaConstants.ChartType;
import com.pengjun.ka.utils.NumberUtils;
import com.pengjun.ka.utils.ResourceUtils;

public class BaseReportActivity extends Activity {

	protected LinearLayout llTypeRatioChart;
	private ProgressBar pbLoad;
	private ScrollView svMagicData;

	private TextView tvTopTitle;

	private TextView tvTotalCountNum;
	private TextView tvTotalCost;
	private TextView tvAvgCost;
	private TextView tvMaxCostDay;
	private TextView tvMaxCostInterval;
	private TextView tvAvgCostPerDay;
	private TextView tvCostKeyWord;

	private TextView tvAccount;
	private ImageView ivType;
	private TextView tvType;
	private TextView tvDate;

	protected List<AccountRecord> arList = new ArrayList<AccountRecord>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

	}

	protected void initLayout(int layoutId, String topTitle) {

		setContentView(layoutId);
		// pass value directly in order to avoid arList is too large
		arList = ReportNotificationService.arList;

		tvTopTitle = (TextView) findViewById(R.id.tvTopTitle);

		tvTopTitle.setText(topTitle);

		llTypeRatioChart = (LinearLayout) findViewById(R.id.llTypeRatioChart);

		tvTotalCountNum = (TextView) findViewById(R.id.tvTotalCountNum);
		tvTotalCost = (TextView) findViewById(R.id.tvTotalCost);
		tvAvgCost = (TextView) findViewById(R.id.tvAvgCost);
		tvAvgCostPerDay = (TextView) findViewById(R.id.tvAvgCostPerDay);
		tvMaxCostDay = (TextView) findViewById(R.id.tvMaxCostDay);
		tvMaxCostInterval = (TextView) findViewById(R.id.tvMaxCostInterval);
		tvCostKeyWord = (TextView) findViewById(R.id.tvCostKeyWord);

		tvAccount = (TextView) findViewById(R.id.tvCost);
		ivType = (ImageView) findViewById(R.id.ivType);
		tvDate = (TextView) findViewById(R.id.tvDate);
		tvType = (TextView) findViewById(R.id.tvType);

		pbLoad = (ProgressBar) findViewById(R.id.pbLoad);
		svMagicData = (ScrollView) findViewById(R.id.svMagicData);

	}

	protected void showProgress() {
		pbLoad.setVisibility(View.VISIBLE);
		svMagicData.setVisibility(View.GONE);
	}

	protected void hideProgress() {
		pbLoad.setVisibility(View.GONE);
		svMagicData.setVisibility(View.VISIBLE);
	}

	protected BaseChart createBaseChart(List<AccountRecord> arList) {

		BaseChart typeRadioChart = ChartFactory.createChart(ChartType.Pie);
		typeRadioChart.compute(arList);
		typeRadioChart.setZoomEnabled(false);

		return typeRadioChart;
	}

	protected void fillBaseReport(BaseReport baseReport, BaseChart typeRadioChart) {

		llTypeRatioChart.addView(typeRadioChart.getView(this));

		tvTotalCountNum.setText(String.valueOf(baseReport.getTotalCountNum()));
		tvTotalCost.setText(NumberUtils.double2String(baseReport.getTotalCost()));
		tvAvgCost.setText(String.valueOf(baseReport.getAvgCost()));
		tvAvgCostPerDay.setText(String.valueOf(baseReport.getAvgCostPerDay()));
		tvMaxCostDay.setText(baseReport.getMaxCostDay());
		tvMaxCostInterval.setText(baseReport.getMaxCostInterval());
		if (baseReport.getCostKeyWord() != null) {
			tvCostKeyWord.setText(baseReport.getCostKeyWord());
		} else {
			tvCostKeyWord.setText("无");
		}

		AccountRecord ar = baseReport.getMaxCost();
		tvAccount.setText(String.valueOf(ar.getAccount()));
		ivType.setImageResource(ResourceUtils.getImgResIdByResName(ar.getImgResName()));
		tvType.setText(ar.getTypeName());
		tvDate.setText(ar.getCreateDate());
	}

}
