package com.pengjun.ka.android.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pengjun.android.utils.ComponentUtils;
import com.pengjun.android.utils.DebugUtils;
import com.pengjun.ka.R;
import com.pengjun.ka.db.dao.ArTypeDao;
import com.pengjun.ka.db.model.ArType;
import com.pengjun.ka.utils.KaConstants;

public class ManageArTypeActivity extends Activity {

	private ListView lvArType;
	private ProgressBar pbLoad;
	private ImageButton ibAddArTpye;

	private ArTypeAdapter arTypeAdapter;
	private List<ArType> arTypeList = new ArrayList<ArType>();

	private static final int MSG_LISTVIEW_TO_TOP = 0x01;
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case MSG_LISTVIEW_TO_TOP:
				// if add or update the new ArType, set ListView to the top
				lvArType.setSelectionFromTop(0, 0);
				break;
			default:
				DebugUtils.printFromPJ("undefined msg:" + msg.what);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.manage_type);

		ibAddArTpye = (ImageButton) findViewById(R.id.ibAddTpye);
		ibAddArTpye.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent();
				intent.setClass(ManageArTypeActivity.this, AddArTypeActivity.class);
				startActivityForResult(intent, KaConstants.CB_ADD_AR_TYPE);
				overridePendingTransition(R.anim.left_in, R.anim.left_out);
			}
		});

		lvArType = (ListView) findViewById(R.id.lvArType);
		arTypeAdapter = new ArTypeAdapter();
		lvArType.setAdapter(arTypeAdapter);

		lvArType.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				// view account record
				Intent intent = new Intent();
				intent.setClass(ManageArTypeActivity.this, AddArTypeActivity.class);

				ArType arType = arTypeList.get(position);
				Bundle bundle = new Bundle();
				bundle.putSerializable(KaConstants.INTENT_AR_TYPE_BEAN, arType);
				intent.putExtras(bundle);
				startActivityForResult(intent, KaConstants.CB_ADD_AR_TYPE);
				overridePendingTransition(R.anim.left_in, R.anim.left_out);

			}
		});

		lvArType.setOnItemLongClickListener(new OnItemLongClickListener() {

			private int selectPos = 0;

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

				if (arTypeList.size() == 1) {
					ComponentUtils.createInfoDialog(ManageArTypeActivity.this, "至少要有一个分类",
							R.drawable.title_warning).show();
					return false;
				}

				selectPos = position;
				AlertDialog.Builder builder = new AlertDialog.Builder(ManageArTypeActivity.this);
				builder.setIcon(R.drawable.mark_delete);
				builder.setTitle("删除记账");
				builder.setMessage("确定要删除该分类？");
				builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						ArType ar = arTypeList.get(selectPos);
						ArTypeDao.getSingleInstance().delete(ar);
						updateArTypeListAsync(false);
					}
				});
				builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

					}
				});
				AlertDialog dialog = builder.create();
				dialog.show();

				// modified dialog button
				Button btPositive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
				if (btPositive != null) {
					btPositive.setBackgroundResource(R.drawable.btn_alert);
				}
				Button btNegative = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
				if (btNegative != null) {
					btNegative.setBackgroundResource(R.drawable.btn_pressed);
				}

				return true;
			}
		});

		pbLoad = (ProgressBar) findViewById(R.id.pbLoad);

		updateArTypeListAsync(false);
	}

	@Override
	public void onBackPressed() {

		ArrayList<String> arTypeNameList = new ArrayList<String>();
		for (ArType arType : arTypeList) {
			arTypeNameList.add(arType.getTypeName());
		}

		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putStringArrayList(KaConstants.INTENT_AR_TYPE_NAME_LIST_BEAN, arTypeNameList);
		intent.putExtras(bundle);

		setResult(RESULT_OK, intent);

		finish();
	}

	public void setListViewToTop() {
		// no handler could be failed to update UI, especially when data changes
		// delay time is optional
		handler.sendEmptyMessageDelayed(MSG_LISTVIEW_TO_TOP, 100);
	}

	// fill listview
	public void updateArTypeListAsync(Boolean isSetListViewToTop) {
		showProgress();
		new LoadArTypeTask().execute(isSetListViewToTop);
	}

	public void updateArTypeListSync() {

		arTypeList = ArTypeDao.getSingleInstance().queryAllByUpdate();

		arTypeAdapter.notifyDataSetChanged();
		ManageArTypeActivity.this.setListViewToTop();
	}

	private void showProgress() {
		pbLoad.setVisibility(View.VISIBLE);
		lvArType.setVisibility(View.GONE);
	}

	private void hideProgress() {
		pbLoad.setVisibility(View.GONE);
		lvArType.setVisibility(View.VISIBLE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == KaConstants.CB_ADD_AR_TYPE) {
			if (resultCode == RESULT_OK) {
				updateArTypeListSync();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	class LoadArTypeTask extends AsyncTask<Boolean, Void, List<ArType>> {

		private boolean isSetListViewToTop = false;

		@Override
		protected List<ArType> doInBackground(Boolean... params) {

			isSetListViewToTop = params[0];
			List<ArType> tempArList = null;

			tempArList = ArTypeDao.getSingleInstance().queryAllByUpdate();
			if (tempArList != null) {
				arTypeList = tempArList;
			}

			return tempArList;
		}

		@Override
		protected void onPostExecute(List<ArType> tempArList) {

			hideProgress();

			arTypeAdapter.notifyDataSetChanged();
			if (isSetListViewToTop) {
				ManageArTypeActivity.this.setListViewToTop();
			}

			super.onPostExecute(tempArList);
		}
	}

	public class ArTypeAdapter extends BaseAdapter {

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

				convertView = ComponentUtils.getLayoutInflater(ManageArTypeActivity.this).inflate(
						R.layout.ar_type_listview_item, null);

				holder.ivType = (ImageView) convertView.findViewById(R.id.ivType);
				holder.createDate = (TextView) convertView.findViewById(R.id.tvCreateTime);
				holder.tvType = (TextView) convertView.findViewById(R.id.tvType);

				convertView.setTag(holder);
			} else {
				holder = (AccountHolder) convertView.getTag();
			}

			ArType arType = arTypeList.get(position);
			holder.ivType.setImageResource(KaConstants.getImgResIdByResName(arType.getImgResName()));
			holder.tvType.setText(arType.getTypeName());
			holder.createDate.setText(arType.getCreateDate());

			return convertView;
		}

		private class AccountHolder {
			public ImageView ivType;
			public TextView tvType;
			public TextView createDate;
		}
	}
}
