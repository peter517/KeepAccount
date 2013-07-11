package com.pengjun.ka.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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

import com.pengjun.ka.activity.AddAccountActivity;
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
	private int selectPos = 0;

	private final int LIMIT_ROW_TOTAL = 50;

	public static String AR_BEAN = "ar_bean";

	private AccountListAdapter arAdapter;
	private List<AccountRecord> arList = new ArrayList<AccountRecord>();

	private static final int MSG_LISTVIEW_TO_TOP = 0x01;
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case MSG_LISTVIEW_TO_TOP:
				// if add the new AR, set ListView to the top
				if (selectPos == 0) {
					lvAr.setSelectionFromTop(0, 0);
				} else {
					selectPos = 0;
				}
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

		// fill listview
		updateArListView(false);
		super.onResume();
	}

	@Override
	public void onDestroy() {

		MyDebug.printFromPJ("onDestroy");
		// strange thing: reEnter app after close the app, arList and offset
		// still exist
		offset = 0;
		arList.clear();
		super.onDestroy();
	}

	public static ArFragment instance = null;

	public static ArFragment newInstance() {
		if (instance == null) {
			instance = new ArFragment();
			return instance;
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
				updateArListView(true);
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
				selectPos = position;
				Intent intent = new Intent();
				intent.setClass(getActivity(), AddAccountActivity.class);

				AccountRecord ar = arList.get(selectPos);
				Bundle bundle = new Bundle();
				bundle.putSerializable(AR_BEAN, (Serializable) ar);
				intent.putExtras(bundle);
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.left_in,
						R.anim.left_out);

			}
		});
		lvAr.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {

				// delete account record
				selectPos = position;
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				builder.setIcon(R.drawable.delete);
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
				builder.show();
				return false;
			}
		});

		return view;
	}

	private void updateArListView(Boolean isLoadMore) {
		showProgress();
		new LoadArTask().execute(isLoadMore);
	}

	private void showProgress() {
		pbLoad.setVisibility(View.VISIBLE);
		lvAr.setVisibility(View.GONE);
	}

	private void hideProgress() {
		pbLoad.setVisibility(View.GONE);
		lvAr.setVisibility(View.VISIBLE);
	}

	class LoadArTask extends AsyncTask<Boolean, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Boolean... params) {

			Boolean isLoadMore = params[0];
			List<AccountRecord> tempArList = null;

			if (isLoadMore) {
				tempArList = AccountRecordService.queryLimitRows(offset,
						LIMIT_ROW_TOTAL);
				if (tempArList != null) {
					arList.addAll(tempArList);
					selectPos = arList.size() - 1;
				}
			} else {
				tempArList = AccountRecordService.queryLimitRows(0,
						Math.max(LIMIT_ROW_TOTAL, arList.size()));
				if (tempArList != null) {
					arList = tempArList;
				}
			}

			return isLoadMore;
		}

		@Override
		protected void onPostExecute(Boolean isLoadMore) {

			hideProgress();

			// rows too little to display load btLoadMore
			if (arList.size() < LIMIT_ROW_TOTAL) {
				btLoadMore.setVisibility(View.GONE);
			} else {
				btLoadMore.setVisibility(View.VISIBLE);
			}

			arAdapter.notifyDataSetChanged();

			// no handler could be failed to update UI, delaytime 20 is optional
			handler.sendEmptyMessageDelayed(MSG_LISTVIEW_TO_TOP, 20);
			super.onPostExecute(isLoadMore);
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
				holder.category = (ImageView) convertView
						.findViewById(R.id.imCategory);
				holder.date = (TextView) convertView.findViewById(R.id.tvDate);

				convertView.setTag(holder);
			} else {
				holder = (AccountHolder) convertView.getTag();

			}

			// fill content
			AccountRecord ar = arList.get(position);
			holder.account.setText(String.valueOf(ar.getAmount()));
			holder.category.setImageDrawable(getCategoryImg(ar.getCategory()));
			holder.date.setText(ar.getDate());

			return convertView;
		}

		private Drawable getCategoryImg(String type) {

			if (type.equals(Constants.TYPE_EAT)) {
				return getResources().getDrawable(R.drawable.eat);
			} else if (type.equals(Constants.TYPE_DRESS)) {
				return getResources().getDrawable(R.drawable.dress);
			} else if (type.equals(Constants.TYPE_CAR)) {
				return getResources().getDrawable(R.drawable.car);
			} else if (type.equals(Constants.TYPE_PLAY)) {
				return getResources().getDrawable(R.drawable.play);
			} else if (type.equals(Constants.TYPE_OTHER)) {
				return getResources().getDrawable(R.drawable.other);
			}

			return null;
		}

		private class AccountHolder {
			public TextView account;
			public ImageView category;
			public TextView date;
		}
	}
}
