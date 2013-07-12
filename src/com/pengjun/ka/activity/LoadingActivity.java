package com.pengjun.ka.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.pengjun.keepaccounts.R;

public class LoadingActivity extends Activity {

	private static final int GO_MAIN = 0;
	private static final int COVER_EXIST_TIME = 2000;

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
		handler.sendEmptyMessageDelayed(GO_MAIN, COVER_EXIST_TIME);
		setContentView(R.layout.enter_cover);

	}

	@Override
	protected void onDestroy() {
		handler.removeMessages(GO_MAIN);
		super.onDestroy();
	}
}
