package com.pengjun.ka.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.pengjun.ka.chart.AbstractChart;
import com.pengjun.ka.chart.ArBarChart;
import com.pengjun.ka.chart.ArLineChart;
import com.pengjun.ka.chart.ArPieChart;
import com.pengjun.ka.db.model.AccountRecord;
import com.pengjun.ka.utils.Constants;
import com.pengjun.ka.utils.Constants.ChartType;
import com.pengjun.ka.utils.MyDebug;
import com.pengjun.keepaccounts.R;

public class ArChartDisplayActivity extends Activity implements AbstractChart.CallBack {

	private ProgressBar pbLoad;
	private RelativeLayout rlChart;
	private ChartType chartType;
	private List<AccountRecord> arList = new ArrayList<AccountRecord>();

	private static final int MSG_CREATE_CHART_FINISHED = 0x01;
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case MSG_CREATE_CHART_FINISHED:
				rlChart.addView((View) msg.obj);
				break;
			default:
				MyDebug.printFromPJ("undefined msg:" + msg.what);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);

		arList = (List<AccountRecord>) getIntent().getExtras().getSerializable(Constants.INTENT_AR_LIST);
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

	class LoadArChartTask extends AsyncTask<Void, Void, View> {

		@Override
		protected View doInBackground(Void... params) {

			View view = null;
			switch (chartType) {
			case bar:
				view = (new ArBarChart().getView(ArChartDisplayActivity.this, arList, null));
				break;
			case pie:
				view = (new ArPieChart().getView(ArChartDisplayActivity.this, arList, null));
				break;
			case line:
				view = (new ArLineChart().getView(ArChartDisplayActivity.this, arList, null));
				break;
			}
			return view;

		}

		@Override
		protected void onPostExecute(View v) {
			Message msg = new Message();
			msg.obj = v;
			msg.what = MSG_CREATE_CHART_FINISHED;
			handler.sendMessage(msg);
			hideProgress();
			super.onPostExecute(v);
		}
	}

}
