package com.pengjun.keepaccounts.activity;

import java.util.ArrayList;
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
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.pengjun.keepaccounts.R;
import com.pengjun.keepaccounts.model.AccountBean;

public class KeepAccountActivity extends Activity {

	ListView accountListView;
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
				intent.setClass(KeepAccountActivity.this,
						AddAccountActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.left_in, R.anim.left_out);
			}
		});

		// fill listview
		List<AccountBean> accountList = new ArrayList<AccountBean>();
		for (int i = 0; i < titles.length; i++) {
			AccountBean account = new AccountBean(titles[i], texts[i],
					resIds[i]);
			accountList.add(account);
		}
		accountListView = (ListView) this.findViewById(R.id.lvCostRecord);
		accountListView.setAdapter(new AccountListAdapter(accountList));
	}

	public class AccountListAdapter extends BaseAdapter {
		List<AccountBean> accountList = null;
		LayoutInflater inflater = (LayoutInflater) KeepAccountActivity.this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		public AccountListAdapter(List<AccountBean> accountList) {
			this.accountList = accountList;
		}

		public int getCount() {
			return accountList.size();
		}

		public Object getItem(int position) {
			return accountList.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			AccountHolder holder = new AccountHolder();
			if (convertView == null) {

				convertView = inflater.inflate(R.layout.account_item, null);

				holder.cost = (TextView) convertView.findViewById(R.id.tvCost);
				holder.date = (TextView) convertView.findViewById(R.id.tvDate);
				holder.category = (ImageView) convertView
						.findViewById(R.id.imCategory);

				convertView.setTag(holder);
			} else {
				holder = (AccountHolder) convertView.getTag();

			}

			// fill content
			AccountBean account = accountList.get(position);
			holder.cost.setText(account.getCost());
			holder.date.setText(account.getDate());
			holder.category.setImageResource(account.getCategory());

			return convertView;
		}

		private class AccountHolder {
			public TextView date;
			public TextView cost;
			public ImageView category;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.keep_account, menu);
		return true;
	}

}
