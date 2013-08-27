package com.pengjun.ka.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pengjun.ka.R;
import com.pengjun.ka.component.GalleryFlow;
import com.pengjun.ka.db.model.AccountRecord;
import com.pengjun.ka.db.service.ArService;
import com.pengjun.ka.fragment.ArSearchResultFragment;
import com.pengjun.ka.utils.ComponentUtils;
import com.pengjun.ka.utils.Constants;
import com.pengjun.ka.utils.Constants.ChartType;
import com.pengjun.ka.utils.ImageUtils;

public class ArChartActivity extends Activity {

	private GalleryFlow gfChart;
	private ProgressBar pbLoad;
	private TextView tvTilte;

	// static to make a cache of the data
	public static List<AccountRecord> arList = new ArrayList<AccountRecord>();
	private static List<Bitmap> chartBmpList = new ArrayList<Bitmap>();

	private final static Integer[] chartResIdArr = { R.drawable.chart_columnar, R.drawable.chart_pie,
			R.drawable.chart_line_day, R.drawable.chart_line_mouth, R.drawable.chart_line_year };

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ar_chart);

		if (chartBmpList != null) {
			for (int i = 0; i < chartResIdArr.length; i++) {
				chartBmpList.add(ImageUtils.createReflectedImage(
						BitmapFactory.decodeResource(getResources(), chartResIdArr[i]), 0.5f));
			}
		}

		gfChart = (GalleryFlow) findViewById(R.id.gfChart);
		pbLoad = (ProgressBar) findViewById(R.id.pbLoad);
		tvTilte = (TextView) findViewById(R.id.tvTilte);

		gfChart.setFadingEdgeLength(0);
		gfChart.setSpacing(-25); // 图片之间的间距
		gfChart.setAdapter(new GalleryFlowAdapter());

		gfChart.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				Intent intent = new Intent(ArChartActivity.this, ArChartDisplayActivity.class);
				Bundle bundle = new Bundle();
				switch (position) {
				case 0:
					bundle.putSerializable(Constants.INTENT_AR_CHART_TYPE, ChartType.Bar);
					break;
				case 1:
					bundle.putSerializable(Constants.INTENT_AR_CHART_TYPE, ChartType.Pie);
					break;
				case 2:
					bundle.putSerializable(Constants.INTENT_AR_CHART_TYPE, ChartType.LineDay);
					break;
				case 3:
					bundle.putSerializable(Constants.INTENT_AR_CHART_TYPE, ChartType.LineMonth);
					break;
				case 4:
					bundle.putSerializable(Constants.INTENT_AR_CHART_TYPE, ChartType.LineYear);
					break;
				}
				intent.putExtras(bundle);
				startActivity(intent);
			}

		});
		gfChart.setSelection(1);

		if (arList.size() == 0) {
			showProgress();
			new LoadArTask().execute();
		}

	}

	private void showProgress() {
		pbLoad.setVisibility(View.VISIBLE);
		gfChart.setVisibility(View.GONE);
		tvTilte.setVisibility(View.GONE);
	}

	private void hideProgress() {
		pbLoad.setVisibility(View.GONE);
		gfChart.setVisibility(View.VISIBLE);
		tvTilte.setVisibility(View.VISIBLE);
	}

	public static void resetArList() {
		arList = new ArrayList<AccountRecord>();
	}

	class LoadArTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// the data in ArSearchResult is searched by offset and limit
			// so need to research
			arList = ArService.queryAr(ArSearchResultFragment.getArSearchCondition(), 0, -1);
			return null;
		}

		@Override
		protected void onPostExecute(Void v) {
			hideProgress();
			super.onPostExecute(v);
		}
	}

	class GalleryFlowAdapter extends BaseAdapter {

		public int getCount() {
			return chartBmpList.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			AccountHolder holder = new AccountHolder();
			if (convertView == null) {

				convertView = ComponentUtils.getLayoutInflater(ArChartActivity.this).inflate(
						R.layout.ar_chart_gallery_item, null);

				holder.ivChart = (ImageView) convertView.findViewById(R.id.ivChart);
				holder.ivChart.setLayoutParams(new GalleryFlow.LayoutParams(ComponentUtils.dip2px(
						ArChartActivity.this, 128), ComponentUtils.dip2px(ArChartActivity.this, 128)));
				convertView.setTag(holder);
			} else {
				holder = (AccountHolder) convertView.getTag();
			}

			holder.ivChart.setImageBitmap(chartBmpList.get(position));

			return convertView;
		}

		private class AccountHolder {
			public ImageView ivChart;
		}
	}
}
