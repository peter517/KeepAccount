package com.pengjun.ka.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pengjun.ka.fragment.ArFragment;
import com.pengjun.ka.fragment.BackupFragment;
import com.pengjun.ka.fragment.FragmentDirector;
import com.pengjun.keepaccounts.R;

public class KAMainActivity extends FragmentActivity {

	ImageButton ibAddAccount;
	ImageButton ibSetListViewToTop;

	ImageButton ibHome;
	ImageButton ibSearch;
	ImageButton ibChart;
	ImageButton ibSettings;

	TextView tvTopTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.keep_account);

		// Toast.makeText(this, "123", 5000).show();
		// top bar
		createTopBar();

		// main content
		FragmentDirector.replaceFragment(this, R.id.mainConent,
				ArFragment.newInstance());

		// bottom bar
		createBottomBar();

	}

	private void createTopBar() {

		ibAddAccount = (ImageButton) findViewById(R.id.ibAddAccount);
		ibAddAccount.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// add new account record
				Intent intent = new Intent();
				intent.setClass(KAMainActivity.this, AddAccountActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.left_in, R.anim.left_out);
				ArFragment.newInstance().setListViewToTop();
			}
		});

		tvTopTitle = (TextView) findViewById(R.id.tvTopTitle);

		ibSetListViewToTop = (ImageButton) findViewById(R.id.ibSetListViewToTop);
		ibSetListViewToTop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ArFragment.newInstance().setListViewToTop();
			}
		});
	}

	private void createBottomBar() {

		ibHome = (ImageButton) findViewById(R.id.ibHome);
		ibHome.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					FragmentDirector.replaceFragment(KAMainActivity.this,
							R.id.mainConent, ArFragment.newInstance());

					ibAddAccount.setVisibility(View.VISIBLE);
					ibSetListViewToTop.setVisibility(View.VISIBLE);
					tvTopTitle.setText(R.string.recentArs);
				}
			}
		});

		ibSearch = (ImageButton) findViewById(R.id.ibSearch);
		ibSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {

				}
			}
		});

		ibChart = (ImageButton) findViewById(R.id.ibChart);
		ibChart.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					FragmentDirector.replaceFragment(KAMainActivity.this,
							R.id.mainConent, BackupFragment.newInstance());

					ibAddAccount.setVisibility(View.GONE);
					ibSetListViewToTop.setVisibility(View.GONE);
					tvTopTitle.setText(R.string.backUp);
				}

			}
		});

		ibChart.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});

		ibSettings = (ImageButton) findViewById(R.id.ibSettings);
		ibSettings.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {

				}
			}
		});
	}

	// the same as backing home operation
	@Override
	public void onBackPressed() {
		// Intent homeIntent = new Intent(Intent.ACTION_MAIN);
		// homeIntent.addCategory(Intent.CATEGORY_HOME);
		// startActivity(homeIntent);
		finish();
	}

	@Override
	protected void onDestroy() {

		ArFragment.newInstance().recycle();
		super.onDestroy();
	}

}
