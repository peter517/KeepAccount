package com.pengjun.ka.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pengjun.ka.db.model.ArSearchCondition;
import com.pengjun.ka.fragment.ArFragment;
import com.pengjun.ka.fragment.ArSearchResultFragment;
import com.pengjun.ka.fragment.FragmentDirector;
import com.pengjun.ka.utils.Constants;
import com.pengjun.keepaccounts.R;

public class ArSearchResultActivity extends FragmentActivity {

	TextView tvTopTitle;
	ImageButton ibChart;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.ar_search_result);

		tvTopTitle = (TextView) findViewById(R.id.tvTopTitle);
		ibChart = (ImageButton) findViewById(R.id.ibChart);
		ibChart.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(ArSearchResultActivity.this,
						ArChartActivity.class);

				Bundle bundle = new Bundle();
				bundle.putSerializable(Constants.INTENT_AR_LIST, null);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

		ArSearchCondition arSC = (ArSearchCondition) getIntent().getExtras()
				.getSerializable(Constants.INTENT_AR_SEARCH_CONDITION);

		// main content
		FragmentDirector.replaceFragment(this, R.id.mainConent,
				ArSearchResultFragment.newInstance(arSC));

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == Constants.CB_ADD_AR) {
			if (resultCode == RESULT_OK) {
				ArSearchResultFragment.newInstance(null).updateArListView(true);
			} else {
				ArSearchResultFragment.newInstance(null)
						.updateArListView(false);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
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
