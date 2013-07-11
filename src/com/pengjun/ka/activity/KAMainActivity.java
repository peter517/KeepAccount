package com.pengjun.ka.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pengjun.ka.fragment.ArFragment;
import com.pengjun.ka.fragment.BackupFragment;
import com.pengjun.keepaccounts.R;

public class KAMainActivity extends FragmentActivity {

	ImageButton ibAddAccount;

	ImageButton ibDetails;
	ImageButton ibStatistics;
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

		// top bar
		createTopBar();

		// main content
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.replace(R.id.mainConent, ArFragment.newInstance());
		ft.commit();

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

			}
		});
		tvTopTitle = (TextView) findViewById(R.id.tvTopTitle);
	}

	private void createBottomBar() {

		ibDetails = (ImageButton) findViewById(R.id.ibDetails);
		ibDetails.setBackgroundColor(Color.GRAY);
		ibDetails.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ibDetails.setBackgroundColor(Color.GRAY);

				ibStatistics.setBackgroundColor(Color.TRANSPARENT);
				ibChart.setBackgroundColor(Color.TRANSPARENT);
				ibSettings.setBackgroundColor(Color.TRANSPARENT);

				FragmentTransaction ft = getSupportFragmentManager()
						.beginTransaction();
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				ft.replace(R.id.mainConent, ArFragment.newInstance());
				ft.commit();

				ibAddAccount.setVisibility(View.VISIBLE);
				tvTopTitle.setText(R.string.recentArs);
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

				FragmentTransaction ft = getSupportFragmentManager()
						.beginTransaction();
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				ft.replace(R.id.mainConent, BackupFragment.newInstance());
				ft.commit();

				ibAddAccount.setVisibility(View.GONE);
				tvTopTitle.setText(R.string.backUp);

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

	// the same as backing home operation
	@Override
	public void onBackPressed() {
		// Intent homeIntent = new Intent(Intent.ACTION_MAIN);
		// homeIntent.addCategory(Intent.CATEGORY_HOME);
		// startActivity(homeIntent);
		finish();
	}

}
