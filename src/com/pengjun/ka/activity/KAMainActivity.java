package com.pengjun.ka.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pengjun.ka.db.service.ArTypeService;
import com.pengjun.ka.fragment.ArChartFragment;
import com.pengjun.ka.fragment.ArFragment;
import com.pengjun.ka.fragment.ArSearchFragment;
import com.pengjun.ka.fragment.FragmentDirector;
import com.pengjun.ka.fragment.SettingFragment;
import com.pengjun.ka.utils.Constants;
import com.pengjun.keepaccounts.R;

public class KaMainActivity extends FragmentActivity {

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

		SharedPreferences firstInstall = getSharedPreferences(
				Constants.SP_TAG_INSTALL, 0);

		// if first start of the app
		if (firstInstall.getString(Constants.SP_KEY_FIRST_START_APP, "")
				.equals("")) {
			firstInstall
					.edit()
					.putString(Constants.SP_KEY_FIRST_START_APP,
							Constants.SP_VALUE_FIRST_START_APP).commit();
			ArTypeService.initTable();
		}

	}

	private void createTopBar() {

		ibAddAccount = (ImageButton) findViewById(R.id.ibAddAccount);
		ibAddAccount.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// add new account record
				Intent intent = new Intent();
				intent.setClass(KaMainActivity.this, AddArActivity.class);
				startActivityForResult(intent, Constants.CB_ADD_AR);
				overridePendingTransition(R.anim.left_in, R.anim.left_out);

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
					FragmentDirector.replaceFragment(KaMainActivity.this,
							R.id.mainConent, ArFragment.newInstance());

					ibSearch.setBackgroundResource(R.drawable.search_normal);

					ibAddAccount.setVisibility(View.VISIBLE);
					ibSetListViewToTop.setVisibility(View.VISIBLE);
					tvTopTitle.setText(R.string.recentArs);
				}
			}
		});

		// the text in searchFragment could be focused
		// so ibSearch icon change process dependently
		ibSearch = (ImageButton) findViewById(R.id.ibSearch);
		ibSearch.setBackgroundResource(R.drawable.search_normal);
		ibSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					FragmentDirector.replaceFragment(KaMainActivity.this,
							R.id.mainConent, ArSearchFragment.newInstance());

					ibSearch.setBackgroundResource(R.drawable.search_focused);

					ibAddAccount.setVisibility(View.GONE);
					ibSetListViewToTop.setVisibility(View.GONE);
					tvTopTitle.setText(R.string.searchAr);

				} else {

				}
			}
		});

		ibChart = (ImageButton) findViewById(R.id.ibChart);
		ibChart.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					FragmentDirector.replaceFragment(KaMainActivity.this,
							R.id.mainConent, ArChartFragment.newInstance());

					ibSearch.setBackgroundResource(R.drawable.search_normal);

					ibAddAccount.setVisibility(View.GONE);
					ibSetListViewToTop.setVisibility(View.GONE);
					tvTopTitle.setText(R.string.staticChart);
				}

			}
		});

		ibSettings = (ImageButton) findViewById(R.id.ibSettings);
		ibSettings.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {

					ibSearch.setBackgroundResource(R.drawable.search_normal);
					tvTopTitle.setText(R.string.systemSetting);
					FragmentDirector.replaceFragment(KaMainActivity.this,
							R.id.mainConent, SettingFragment.newInstance());

				}
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == Constants.CB_ADD_AR) {
			if (resultCode == RESULT_OK) {
				ArFragment.newInstance().updateArListView(true);
			} else {
				ArFragment.newInstance().updateArListView(false);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	// the same as backing home operation
	@Override
	public void onBackPressed() {
		Intent homeIntent = new Intent(Intent.ACTION_MAIN);
		homeIntent.addCategory(Intent.CATEGORY_HOME);
		startActivity(homeIntent);
		// finish();
	}

	@Override
	protected void onDestroy() {

		ArFragment.newInstance().recycle();
		super.onDestroy();
	}

}
