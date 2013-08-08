package com.pengjun.ka.fragment;

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
import com.pengjun.ka.db.model.AccountRecord;
import com.pengjun.ka.db.model.ArSearchCondition;
import com.pengjun.ka.db.service.ArService;
import com.pengjun.ka.utils.Constants;
import com.pengjun.ka.utils.MyDebug;
import com.pengjun.ka.utils.ResManageUtils;
import com.pengjun.keepaccounts.R;

public class ArSearchResultFragment extends Fragment {

	private ListView lvAr;
	private ProgressBar pbLoad;
	private Button btLoadMore;
	private View loadMoreView;

	private int offset = 0;
	private final int LIMIT_ROW_TOTAL = 30;

	private AccountListAdapter arAdapter;
	private List<AccountRecord> arList = new ArrayList<AccountRecord>();

	private static ArSearchResultFragment instance = null;

	ArSearchCondition arSC;

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

	public static ArSearchResultFragment newInstance(ArSearchCondition arSC) {
		if (instance == null) {
			instance = new ArSearchResultFragment();

		}
		if (arSC != null) {
			instance.arSC = arSC;
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
				bundle.putSerializable(Constants.INTENT_AR_BEAN, ar);
				bundle.putSerializable(Constants.INTENT_DISABLE_AR_TYPE_MANAGE,
						true);
				intent.putExtras(bundle);
				getActivity().startActivityForResult(intent,
						Constants.CB_ADD_AR);
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
								ArService.delete(ar);
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

				// modified dialog button
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

				return true;
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
	public void updateArListView(Boolean isListViewDataChange) {
		showProgress();
		new LoadArTask().execute(isListViewDataChange);
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

		private boolean isListViewDataChange = false;

		@Override
		protected List<AccountRecord> doInBackground(Boolean... params) {

			isListViewDataChange = params[0];
			List<AccountRecord> tempArList = ArService.queryAr(arSC, 0,
					Math.max(LIMIT_ROW_TOTAL, arList.size()));
			if (tempArList != null) {
				arList = tempArList;
			}

			return tempArList;
		}

		@Override
		protected void onPostExecute(List<AccountRecord> tempArList) {

			hideProgress();

			// rows too little to display load btLoadMore
			if (arList.size() < LIMIT_ROW_TOTAL) {
				btLoadMore.setVisibility(View.GONE);
			} else {
				btLoadMore.setVisibility(View.VISIBLE);
			}

			if (tempArList == null || tempArList.size() == 0) {
				return;
			}

			arAdapter.notifyDataSetChanged();
			if (isListViewDataChange) {
				ArSearchResultFragment.this.setListViewToTop();
			}

			super.onPostExecute(tempArList);
		}
	}

	class LoadMoreArTask extends AsyncTask<Void, Void, List<AccountRecord>> {

		@Override
		protected List<AccountRecord> doInBackground(Void... params) {

			List<AccountRecord> tempArList = null;

			tempArList = ArService.queryAr(arSC, offset, LIMIT_ROW_TOTAL);
			if (tempArList != null) {
				arList.addAll(tempArList);
			}

			return tempArList;
		}

		@Override
		protected void onPostExecute(List<AccountRecord> tempArList) {

			hideProgress();

			if (tempArList == null || tempArList.size() == 0) {
				Toast.makeText(ArSearchResultFragment.this.getActivity(),
						"没有新数据", Constants.TOAST_EXSIT_TIME).show();
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

				convertView = inflater.inflate(R.layout.ar_listview_item, null);

				holder.account = (TextView) convertView
						.findViewById(R.id.tvCost);
				holder.ivType = (ImageView) convertView
						.findViewById(R.id.ivType);
				holder.date = (TextView) convertView.findViewById(R.id.tvDate);
				holder.tvType = (TextView) convertView
						.findViewById(R.id.tvType);

				convertView.setTag(holder);
			} else {
				holder = (AccountHolder) convertView.getTag();
			}

			// fill content
			AccountRecord ar = arList.get(position);
			holder.account.setText(String.valueOf(ar.getAccount()));
			holder.ivType.setImageResource(ResManageUtils.getImgResIdByName(ar
					.getImgResName()));
			holder.tvType.setText(ar.getTypeName());
			holder.date.setText(ar.getCreateDate());

			return convertView;
		}

		private class AccountHolder {
			public TextView account;
			public ImageView ivType;
			public TextView tvType;
			public TextView date;
		}
	}
}
