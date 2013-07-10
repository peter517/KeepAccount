package com.pengjun.ka.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.pengjun.keepaccounts.R;

public class LoadingActivity extends Activity {
	private static final int GO_MAIN = 0;
	private static final int COVER_EXIST_TIME = 0;
	Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			Intent intent = new Intent(LoadingActivity.this,
					KAMainActivity.class);
			startActivity(intent);
			finish();
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		handler.sendEmptyMessageDelayed(GO_MAIN, COVER_EXIST_TIME);
		setContentView(R.layout.loading);

	}

	@Override
	protected void onDestroy() {
		handler.removeMessages(GO_MAIN);
		super.onDestroy();
	}
}
