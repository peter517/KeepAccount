package com.pengjun.ka.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pengjun.ka.R;
import com.pengjun.ka.android.fragment.ArSearchResultFragment;
import com.pengjun.ka.android.fragment.FragmentDirector;
import com.pengjun.ka.db.model.ArSearchCondition;
import com.pengjun.ka.utils.ComponentUtils;
import com.pengjun.ka.utils.Constants;

public class ArSearchResultActivity extends FragmentActivity {

	private TextView tvTopTitle;
	private ImageButton ibChart;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ar_search_result);

		tvTopTitle = (TextView) findViewById(R.id.tvTopTitle);
		ibChart = (ImageButton) findViewById(R.id.ibChart);
		ibChart.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if (!ArSearchResultFragment.newInstance(null).hasSearchResult()) {
					ComponentUtils.createInfoDialog(ArSearchResultActivity.this, "没有查询数据，不能生成图表").show();
					return;
				}
				Intent intent = new Intent();
				intent.setClass(ArSearchResultActivity.this, ArChartActivity.class);

				startActivity(intent);
			}
		});

		ArSearchCondition arSC = (ArSearchCondition) getIntent().getExtras().getSerializable(
				Constants.INTENT_AR_SEARCH_CONDITION);

		// main content
		FragmentDirector.replaceFragment(this, R.id.mainConent, ArSearchResultFragment.newInstance(arSC));

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == Constants.CB_ADD_AR) {
			if (resultCode == RESULT_OK) {
				ArSearchResultFragment.newInstance(null).updateArListViewSync();
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

		super.onDestroy();
	}

}
