package com.pengjun.ka.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.pengjun.ka.R;
import com.pengjun.ka.utils.Constants;

public class LoadingActivity extends Activity {

	private static final int GO_MAIN = 0;
	private static final int COVER_EXIST_TIME = 3000;

	Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			Intent intent = new Intent(LoadingActivity.this, KaMainActivity.class);
			startActivity(intent);
			finish();
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (Constants.IS_DEBUG) {
			handler.sendEmptyMessageDelayed(GO_MAIN, 0);
		} else {
			handler.sendEmptyMessageDelayed(GO_MAIN, COVER_EXIST_TIME);
		}

		setContentView(R.layout.enter_cover);

	}

	@Override
	protected void onDestroy() {
		handler.removeMessages(GO_MAIN);
		super.onDestroy();
	}
}
