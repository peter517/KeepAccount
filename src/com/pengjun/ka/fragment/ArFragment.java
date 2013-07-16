package com.pengjun.ka.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.pengjun.ka.activity.AddArActivity;
import com.pengjun.ka.activity.KAMainActivity;
import com.pengjun.ka.db.model.AccountRecord;
import com.pengjun.ka.db.service.AccountRecordService;
import com.pengjun.ka.tools.Constants;
import com.pengjun.ka.tools.MyDebug;
import com.pengjun.keepaccounts.R;

public class ArFragment extends Fragment {

	private ListView lvAr;
	private ProgressBar pbLoad;
	private Button btLoadMore;
	private View loadMoreView;

	private int offset = 0;
	private final int LIMIT_ROW_TOTAL = 30;

	public static String AR_BEAN = "ar_bean";

	private AccountListAdapter arAdapter;
	private List<AccountRecord> arList = new ArrayList<AccountRecord>();

	private static ArFragment instance = null;

	private static final int MSG_LISTVIEW_TO_TOP = 0x01;
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case MSG_LISTVIEW_TO_TOP:
				// if add the new AR, set ListView to the top
				lvAr.setSelectionFromTop(0, 0);
				break;
			default:
				MyDebug.printFromPJ("undefined msg:" + msg.what);
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	public static ArFragment newInstance() {
		if (instance == null) {
			instance = new ArFragment();
		}
		return instance;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// AccountRecordService.deleteAll(AccountRecordService.queryAll());
		View view = inflater.inflate(R.layout.ar_listview, null);

		pbLoad = (ProgressBar) view.findViewById(R.id.pbLoad);

		loadMoreView = inflater.inflate(R.layout.laod_button, null);
		btLoadMore = (Button) loadMoreView.findViewById(R.id.btLoadMore);
		btLoadMore.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				btLoadMore.setVisibility(View.GONE);
				offset = arList.size();
				loadMoreArListView();
			}
		});

		lvAr = (ListView) view.findViewById(R.id.lvAcountRecord);
		lvAr.addFooterView(loadMoreView);
		arAdapter = new AccountListAdapter();
		lvAr.setAdapter(arAdapter);

		lvAr.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// view account record
				Intent intent = new Intent();
				intent.setClass(getActivity(), AddArActivity.class);

				AccountRecord ar = arList.get(position);
				Bundle bundle = new Bundle();
				bundle.putSerializable(AR_BEAN, (Serializable) ar);
				intent.putExtras(bundle);
				getActivity().startActivityForResult(intent,
						KAMainActivity.ADD_AR);
				getActivity().overridePendingTransition(R.anim.left_in,
						R.anim.left_out);

			}
		});
		lvAr.setOnItemLongClickListener(new OnItemLongClickListener() {

			private int selectPos = 0;

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {

				// delete account record
				selectPos = position;
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				builder.setIcon(R.drawable.mark_delete);
				builder.setTitle("删除记账");
				builder.setMessage("确定要删除该次记账？");
				builder.setPositiveButton("删除",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								AccountRecord ar = arList.get(selectPos);
								AccountRecordService.delete(ar);
								updateArListView(false);
							}
						});
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {

							}
						});
				AlertDialog dialog = builder.create();
				dialog.show();

				Button btPositive = dialog
						.getButton(DialogInterface.BUTTON_POSITIVE);
				if (btPositive != null) {
					btPositive.setBackgroundResource(R.drawable.btn_alert);
				}
				Button btNegative = dialog
						.getButton(DialogInterface.BUTTON_NEGATIVE);
				if (btNegative != null) {
					btNegative.setBackgroundResource(R.drawable.btn_normal);
				}

				return false;
			}
		});

		updateArListView(false);
		return view;
	}

	public void recycle() {
		// strange thing: reEnter app after close the app, instance still exist
		instance = null;
	}

	public void setListViewToTop() {
		// no handler could be failed to update UI, especially when data changes
		// delay time is optional
		handler.sendEmptyMessageDelayed(MSG_LISTVIEW_TO_TOP, 100);
	}

	// fill listview
	public void updateArListView(Boolean isSetListViewToTop) {
		showProgress();
		new LoadArTask().execute(isSetListViewToTop);
	}

	public void loadMoreArListView() {
		showProgress();
		new LoadMoreArTask().execute();
	}

	private void showProgress() {
		pbLoad.setVisibility(View.VISIBLE);
		lvAr.setVisibility(View.GONE);
	}

	private void hideProgress() {
		pbLoad.setVisibility(View.GONE);
		lvAr.setVisibility(View.VISIBLE);
	}

	class LoadArTask extends AsyncTask<Boolean, Void, List<AccountRecord>> {

		private boolean isSetListViewToTop = false;

		@Override
		protected List<AccountRecord> doInBackground(Boolean... params) {

			isSetListViewToTop = params[0];
			List<AccountRecord> tempArList = null;

			tempArList = AccountRecordService.queryLimitRows(0,
					Math.max(LIMIT_ROW_TOTAL, arList.size()));
			if (tempArList != null) {
				arList = tempArList;
			}

			return tempArList;
		}

		@Override
		protected void onPostExecute(List<AccountRecord> tempArList) {

			hideProgress();

			if (tempArList == null || tempArList.size() == 0) {
				Toast.makeText(ArFragment.this.getActivity(), "没有数据，请记账",
						Constants.TOAST_EXSIT_TIME).show();
			}

			// rows too little to display load btLoadMore
			if (arList.size() < LIMIT_ROW_TOTAL) {
				btLoadMore.setVisibility(View.GONE);
			} else {
				btLoadMore.setVisibility(View.VISIBLE);
			}

			arAdapter.notifyDataSetChanged();
			if (isSetListViewToTop) {
				ArFragment.this.setListViewToTop();
			}

			super.onPostExecute(tempArList);
		}
	}

	class LoadMoreArTask extends AsyncTask<Void, Void, List<AccountRecord>> {

		@Override
		protected List<AccountRecord> doInBackground(Void... params) {

			List<AccountRecord> tempArList = null;

			tempArList = AccountRecordService.queryLimitRows(offset,
					LIMIT_ROW_TOTAL);
			if (tempArList != null) {
				arList.addAll(tempArList);
			}

			return tempArList;
		}

		@Override
		protected void onPostExecute(List<AccountRecord> tempArList) {

			hideProgress();

			if (tempArList == null || tempArList.size() == 0) {
				Toast.makeText(ArFragment.this.getActivity(), "没有新数据",
						Constants.TOAST_EXSIT_TIME).show();
			}

			arAdapter.notifyDataSetChanged();
			super.onPostExecute(tempArList);
		}
	}

	public class AccountListAdapter extends BaseAdapter {
		LayoutInflater inflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		public int getCount() {
			return arList.size();
		}

		public Object getItem(int position) {
			return arList.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			AccountHolder holder = new AccountHolder();
			if (convertView == null) {

				convertView = inflater.inflate(R.layout.ar_item, null);

				holder.account = (TextView) convertView
						.findViewById(R.id.tvCost);
				holder.type = (ImageView) convertView
						.findViewById(R.id.imCategory);
				holder.date = (TextView) convertView.findViewById(R.id.tvDate);

				convertView.setTag(holder);
			} else {
				holder = (AccountHolder) convertView.getTag();
			}

			// fill content
			AccountRecord ar = arList.get(position);
			holder.account.setText(String.valueOf(ar.getAmount()));
			holder.type.setImageResource(getCategoryImg(ar.getType()));
			holder.date.setText(ar.getDate());

			return convertView;
		}

		private int getCategoryImg(String type) {

			if (type.equals(Constants.TYPE_EAT)) {
				return R.drawable.type_eat;
			} else if (type.equals(Constants.TYPE_DRESS)) {
				return R.drawable.type_dress;
			} else if (type.equals(Constants.TYPE_CAR)) {
				return R.drawable.type_car;
			} else if (type.equals(Constants.TYPE_PLAY)) {
				return R.drawable.type_play;
			} else if (type.equals(Constants.TYPE_OTHER)) {
				return R.drawable.type_other;
			}

			return -1;
		}

		private class AccountHolder {
			public TextView account;
			public ImageView type;
			public TextView date;
		}
	}
}