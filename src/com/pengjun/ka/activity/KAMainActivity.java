package com.pengjun.ka.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pengjun.ka.db.model.AccountRecord;
import com.pengjun.ka.db.service.AccountRecordService;
import com.pengjun.ka.tools.Constants;
import com.pengjun.keepaccounts.R;

public class KAMainActivity extends Activity {

	ListView arListView;
	ImageButton ibAddAccount;
	ProgressBar pbLoad;

	ImageButton ibDetails;
	ImageButton ibStatistics;
	ImageButton ibChart;
	ImageButton ibSettings;

	private AccountListAdapter arAdapter;
	private List<AccountRecord> arList = new ArrayList<AccountRecord>();

	String[] titles = { "title1", "title2", "title3", "title4", "title5",
			"title6", "title7", "title8", "title9", "title10", "title11",
			"title12" };
	String[] texts = { "标题1", "标题2", "标题3", "标题4", "标题1", "标题2", "标题3", "标题4",
			"标题1", "标题2", "标题3", "标题4" };
	int[] resIds = { R.drawable.ic_launcher, R.drawable.ic_launcher,
			R.drawable.ic_launcher, R.drawable.ic_launcher,
			R.drawable.ic_launcher, R.drawable.ic_launcher,
			R.drawable.ic_launcher, R.drawable.ic_launcher,
			R.drawable.ic_launcher, R.drawable.ic_launcher,
			R.drawable.ic_launcher, R.drawable.ic_launcher };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_keep_account);

		// top bar
		ibAddAccount = (ImageButton) findViewById(R.id.ibAddAccount);
		ibAddAccount.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(KAMainActivity.this, AddAccountActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.left_in, R.anim.left_out);

			}
		});

		// main content
		pbLoad = (ProgressBar) this.findViewById(R.id.pbLoad);
		arListView = (ListView) this.findViewById(R.id.lvCostRecord);
		arAdapter = new AccountListAdapter();
		arListView.setAdapter(arAdapter);

		arListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

			}
		});
		arListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {

				// AccountRecord ar = arList.get(position);
				// AccountRecordService.delete(ar);
				return false;
			}
		});

		// bottom bar
		ibDetails = (ImageButton) findViewById(R.id.ibDetails);
		ibDetails.setBackgroundColor(Color.GRAY);
		ibDetails.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ibDetails.setBackgroundColor(Color.GRAY);

				ibStatistics.setBackgroundColor(Color.TRANSPARENT);
				ibChart.setBackgroundColor(Color.TRANSPARENT);
				ibSettings.setBackgroundColor(Color.TRANSPARENT);
			}
		});

		ibStatistics = (ImageButton) findViewById(R.id.ibStatistics);
		ibStatistics.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ibStatistics.setBackgroundColor(Color.GRAY);

				ibDetails.setBackgroundColor(Color.TRANSPARENT);
				ibChart.setBackgroundColor(Color.TRANSPARENT);
				ibSettings.setBackgroundColor(Color.TRANSPARENT);
			}
		});

		ibChart = (ImageButton) findViewById(R.id.ibChart);
		ibChart.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ibChart.setBackgroundColor(Color.GRAY);

				ibDetails.setBackgroundColor(Color.TRANSPARENT);
				ibStatistics.setBackgroundColor(Color.TRANSPARENT);
				ibSettings.setBackgroundColor(Color.TRANSPARENT);
			}
		});

		ibSettings = (ImageButton) findViewById(R.id.ibSettings);
		ibSettings.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ibSettings.setBackgroundColor(Color.GRAY);

				ibDetails.setBackgroundColor(Color.TRANSPARENT);
				ibChart.setBackgroundColor(Color.TRANSPARENT);
				ibStatistics.setBackgroundColor(Color.TRANSPARENT);
			}
		});

	}

	@Override
	protected void onResume() {
		// fill listview
		showProgress();
		new LoadAccountTask().execute();
		super.onResume();
	}

	private void showProgress() {
		pbLoad.setVisibility(View.VISIBLE);
		arListView.setVisibility(View.GONE);
	}

	private void hideProgress() {
		pbLoad.setVisibility(View.GONE);
		arListView.setVisibility(View.VISIBLE);
	}

	class LoadAccountTask extends AsyncTask<AccountRecord, Void, Integer> {

		@Override
		protected Integer doInBackground(AccountRecord... params) {
			arList = AccountRecordService.queryAll();
			if (arList == null) {
				arList = new ArrayList<AccountRecord>();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Integer result) {
			arAdapter.notifyDataSetChanged();
			hideProgress();
			super.onPostExecute(result);
		}
	}

	public class AccountListAdapter extends BaseAdapter {
		LayoutInflater inflater = (LayoutInflater) KAMainActivity.this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		public int getCount() {
			return arList.size();
		}

		public Object getItem(int position) {
			return arList.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			AccountHolder holder = new AccountHolder();
			if (convertView == null) {

				convertView = inflater.inflate(R.layout.account_item, null);

				holder.account = (TextView) convertView
						.findViewById(R.id.tvCost);
				holder.category = (ImageView) convertView
						.findViewById(R.id.imCategory);
				holder.date = (TextView) convertView.findViewById(R.id.tvDate);

				convertView.setTag(holder);
			} else {
				holder = (AccountHolder) convertView.getTag();

			}

			// fill content
			AccountRecord ar = arList.get(position);
			holder.account.setText(String.valueOf(ar.getAmount()));
			holder.category.setImageDrawable(getCategoryImg(ar.getCategory()));
			holder.date.setText(ar.getDate());

			return convertView;
		}

		private Drawable getCategoryImg(String type) {

			if (type.equals(Constants.TYPE_EAT)) {
				return getResources().getDrawable(R.drawable.eat);
			} else if (type.equals(Constants.TYPE_DRESS)) {
				return getResources().getDrawable(R.drawable.dress);
			} else if (type.equals(Constants.TYPE_CAR)) {
				return getResources().getDrawable(R.drawable.car);
			} else if (type.equals(Constants.TYPE_PLAY)) {
				return getResources().getDrawable(R.drawable.play);
			} else if (type.equals(Constants.TYPE_OTHER)) {
				return getResources().getDrawable(R.drawable.other);
			}

			return null;
		}

		private class AccountHolder {
			public TextView account;
			public ImageView category;
			public TextView date;
		}
	}

}
