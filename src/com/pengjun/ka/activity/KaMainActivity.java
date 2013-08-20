package com.pengjun.ka.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pengjun.ka.R;
import com.pengjun.ka.db.service.ArTypeService;
import com.pengjun.ka.fragment.ArFragment;
import com.pengjun.ka.fragment.ArSearchFragment;
import com.pengjun.ka.fragment.FragmentDirector;
import com.pengjun.ka.fragment.MagicBoxFragment;
import com.pengjun.ka.fragment.SettingFragment;
import com.pengjun.ka.utils.Constants;

public class KaMainActivity extends FragmentActivity {

	ImageButton ibAddAccount;
	ImageButton ibMenuTopLeft;

	ImageButton ibHome;
	ImageButton ibSearch;
	ImageButton ibMagicbox;
	ImageButton ibSettings;

	TextView tvTopTitle;

	State curState = State.home;

	enum State {
		home, search, magicbox, setting
	}

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
		FragmentDirector.replaceFragment(this, R.id.mainConent, ArFragment.newInstance());

		// bottom bar
		createBottomBar();

		SharedPreferences firstInstall = getSharedPreferences(Constants.SP_TAG_INSTALL, 0);

		// if first start of the app
		if (firstInstall.getString(Constants.SP_KEY_FIRST_START_APP, "").equals("")) {
			firstInstall.edit()
					.putString(Constants.SP_KEY_FIRST_START_APP, Constants.SP_VALUE_FIRST_START_APP).commit();
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

		ibMenuTopLeft = (ImageButton) findViewById(R.id.ibMenuTopLeft);
		ibMenuTopLeft.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (curState) {
				case home:
					ArFragment.newInstance().setListViewToTop();
					break;
				case search:
					ArSearchFragment.newInstance().clearAll();
					break;
				}

			}
		});
	}

	private void stateChange(State state, Fragment fragment) {
		switch (state) {
		case home:
			ibSearch.setBackgroundResource(R.drawable.search_normal);

			ibAddAccount.setVisibility(View.VISIBLE);
			ibMenuTopLeft.setBackgroundResource(R.drawable.menu_btn_to_top);
			ibMenuTopLeft.setVisibility(View.VISIBLE);
			tvTopTitle.setText(R.string.recentArs);
			break;
		case search:
			ibSearch.setBackgroundResource(R.drawable.search_focused);
			ibAddAccount.setVisibility(View.GONE);
			ibMenuTopLeft.setVisibility(View.VISIBLE);
			ibMenuTopLeft.setBackgroundResource(R.drawable.menu_btn_clear);
			tvTopTitle.setText(R.string.searchAr);
			break;
		case magicbox:
			ibSearch.setBackgroundResource(R.drawable.search_normal);

			ibAddAccount.setVisibility(View.GONE);
			ibMenuTopLeft.setVisibility(View.GONE);
			tvTopTitle.setText(R.string.magicBox);
			break;
		case setting:

			ibSearch.setBackgroundResource(R.drawable.search_normal);
			tvTopTitle.setText(R.string.systemSetting);
			break;
		}
		FragmentDirector.replaceFragment(KaMainActivity.this, R.id.mainConent, fragment);

	}

	private void createBottomBar() {

		ibHome = (ImageButton) findViewById(R.id.ibHome);
		ibHome.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					curState = State.home;
					stateChange(curState, ArFragment.newInstance());
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
					curState = State.search;
					stateChange(curState, ArSearchFragment.newInstance());
				}
			}
		});

		ibMagicbox = (ImageButton) findViewById(R.id.ibMagicbox);
		ibMagicbox.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					curState = State.magicbox;
					stateChange(curState, MagicBoxFragment.newInstance());
				}

			}
		});

		ibSettings = (ImageButton) findViewById(R.id.ibSettings);
		ibSettings.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					curState = State.setting;
					stateChange(curState, SettingFragment.newInstance());

				}
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == Constants.CB_ADD_AR) {
			if (resultCode == RESULT_OK) {
				ArFragment.newInstance().updateArListViewSync();
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
