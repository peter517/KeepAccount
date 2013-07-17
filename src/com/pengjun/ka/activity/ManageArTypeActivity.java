package com.pengjun.ka.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.pengjun.ka.db.model.ArType;
import com.pengjun.ka.db.service.ArTypeService;
import com.pengjun.ka.tools.Constants;
import com.pengjun.ka.tools.MyDebug;
import com.pengjun.keepaccounts.R;

public class ManageArTypeActivity extends Activity {

	private ListView lvArType;
	private ProgressBar pbLoad;
	private ImageButton ibAddTpye;

	private ArTypeAdapter arAdapter;

	private List<ArType> arTypeList = new ArrayList<ArType>();

	private static final int MSG_LISTVIEW_TO_TOP = 0x01;
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case MSG_LISTVIEW_TO_TOP:
				// if add the new AR, set ListView to the top
				lvArType.setSelectionFromTop(0, 0);
				break;
			default:
				MyDebug.printFromPJ("undefined msg:" + msg.what);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.manage_type);

		ibAddTpye = (ImageButton) findViewById(R.id.ibAddTpye);
		ibAddTpye.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent();
				intent.setClass(ManageArTypeActivity.this,
						AddArTypeActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable(Constants.INTENT_AR_TYPE_BEAN,
						(Serializable) arTypeList);
				intent.putExtras(bundle);
				startActivityForResult(intent, Constants.CB_ADD_AR_TYPE);
				overridePendingTransition(R.anim.left_in, R.anim.left_out);
			}
		});

		lvArType = (ListView) findViewById(R.id.lvArType);
		arAdapter = new ArTypeAdapter();
		lvArType.setAdapter(arAdapter);

		pbLoad = (ProgressBar) findViewById(R.id.pbLoad);

		updateArTypeLv(false);
	}

	// public void recycle() {
	// // strange thing: reEnter app after close the app, instance still exist
	// instance = null;
	// }
	//
	public void setListViewToTop() {
		// no handler could be failed to update UI, especially when data changes
		// delay time is optional
		handler.sendEmptyMessageDelayed(MSG_LISTVIEW_TO_TOP, 100);
	}

	// fill listview
	public void updateArTypeLv(Boolean isListViewDataChange) {
		showProgress();
		new LoadArTypeTask().execute(isListViewDataChange);
	}

	private void showProgress() {
		pbLoad.setVisibility(View.VISIBLE);
		lvArType.setVisibility(View.GONE);
	}

	private void hideProgress() {
		pbLoad.setVisibility(View.GONE);
		lvArType.setVisibility(View.VISIBLE);
	}

	class LoadArTypeTask extends AsyncTask<Boolean, Void, List<ArType>> {

		private boolean isListViewDataChange = false;

		@Override
		protected List<ArType> doInBackground(Boolean... params) {

			isListViewDataChange = params[0];
			List<ArType> tempArList = null;

			tempArList = ArTypeService.queryAll();
			if (tempArList != null) {
				arTypeList = tempArList;
			}

			return tempArList;
		}

		@Override
		protected void onPostExecute(List<ArType> tempArList) {

			hideProgress();

			if (tempArList == null || tempArList.size() == 0) {
				Toast.makeText(ManageArTypeActivity.this, "没有分类，请新建",
						Constants.TOAST_EXSIT_TIME).show();
				return;
			}

			arAdapter.notifyDataSetChanged();
			if (isListViewDataChange) {
				ManageArTypeActivity.this.setListViewToTop();
			}

			super.onPostExecute(tempArList);
		}
	}

	public class ArTypeAdapter extends BaseAdapter {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		public int getCount() {
			return arTypeList.size();
		}

		public Object getItem(int position) {
			return arTypeList.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			AccountHolder holder = new AccountHolder();
			if (convertView == null) {

				convertView = inflater.inflate(R.layout.ar_type_listview_item,
						null);

				holder.recordSum = (TextView) convertView
						.findViewById(R.id.tvRecordSum);
				holder.ivType = (ImageView) convertView
						.findViewById(R.id.ivType);
				holder.updateTime = (TextView) convertView
						.findViewById(R.id.tvUpdateTime);
				holder.tvType = (TextView) convertView
						.findViewById(R.id.tvType);

				convertView.setTag(holder);
			} else {
				holder = (AccountHolder) convertView.getTag();
			}

			// fill content
			ArType arType = arTypeList.get(position);
			holder.recordSum.setText(String.valueOf(arType.getId()));
			holder.ivType.setImageResource(arType.getImgResId());
			holder.tvType.setText(arType.getTypeName());
			holder.updateTime.setText(arType.getUpdateTime());

			return convertView;
		}

		private class AccountHolder {
			public TextView recordSum;
			public ImageView ivType;
			public TextView tvType;
			public TextView updateTime;
		}
	}
}
