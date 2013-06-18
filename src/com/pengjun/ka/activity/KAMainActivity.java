package com.pengjun.ka.activity;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.pengjun.ka.db.model.AccountRecord;
import com.pengjun.ka.db.service.AccountRecordService;
import com.pengjun.keepaccounts.R;

public class KAMainActivity extends Activity {

	ListView arListView;
	ImageButton ibAddAccount;
	ImageButton ibAddAccount1;
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
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_keep_account);

		// add new count
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

		// fill listview
		List<AccountRecord> accountList = AccountRecordService.queryAll();
		arListView = (ListView) this.findViewById(R.id.lvCostRecord);
		arListView.setAdapter(new AccountListAdapter(accountList));
		arListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

			}
		});
	}

	public class AccountListAdapter extends BaseAdapter {
		List<AccountRecord> arList = null;
		LayoutInflater inflater = (LayoutInflater) KAMainActivity.this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		public AccountListAdapter(List<AccountRecord> arList) {
			this.arList = arList;
		}

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
			AccountRecord account = arList.get(position);
			holder.account.setText(String.valueOf(account.getAmount()));
			holder.category.setImageResource(R.id.imCategory);
			holder.date.setText(account.getDate());

			return convertView;
		}

		private class AccountHolder {
			public TextView account;
			public ImageView category;
			public TextView date;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.keep_account, menu);
		return true;
	}

}
