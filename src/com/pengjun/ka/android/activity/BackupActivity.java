package com.pengjun.ka.android.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pengjun.ka.R;
import com.pengjun.ka.android.fragment.ArFragment;
import com.pengjun.ka.db.service.BackupService;
import com.pengjun.ka.utils.ComponentUtils;
import com.pengjun.ka.utils.Constants;
import com.pengjun.ka.utils.FileUtils;
import com.pengjun.ka.utils.TimeUtils;

public class BackupActivity extends Activity {

	private GridView gvBackup;
	private ImageButton ibAddBackup;
	private ImageButton ibRestore;
	private ProgressDialog pdLoad;

	private List<String> backupDateStrList = new ArrayList<String>();
	private BackupAdapter backupAdapter;

	private final int GRIDVIEW_UNSELECTED = -1;
	private int selectPos = GRIDVIEW_UNSELECTED;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.backup);

		FileUtils.createDir(Constants.BACK_UP_ROOT);
		backupDateStrList = FileUtils.getFileNameList(new File(Constants.BACK_UP_ROOT));

		ibAddBackup = (ImageButton) findViewById(R.id.ibAddBackup);
		ibAddBackup.setOnClickListener(new View.OnClickListener() {
			private String curDateStr;

			@Override
			public void onClick(View v) {

				selectPos = GRIDVIEW_UNSELECTED;

				curDateStr = TimeUtils.getCurDateStr();
				if (backupDateStrList.size() != 0 && backupDateStrList.contains(TimeUtils.getCurDateStr())) {
					AlertDialog.Builder builder = new AlertDialog.Builder(BackupActivity.this);
					builder.setIcon(R.drawable.mark_paste);
					builder.setTitle("替换备份");
					builder.setMessage("确定要替换日期为" + curDateStr + "的备份");
					builder.setPositiveButton("替换", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							showProgress("正在备份……");
							new BackupTask().execute(curDateStr);
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
					return;
				}

				showProgress("正在加载……");
				new BackupTask().execute(curDateStr);
			}

		});

		ibRestore = (ImageButton) findViewById(R.id.ibRestore);
		ibRestore.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if (selectPos == GRIDVIEW_UNSELECTED) {
					ComponentUtils.createInfoDialog(BackupActivity.this, "请选择还原日期").show();
					return;
				}

				showProgress("正在恢复……");
				new RestoreTask().execute(backupDateStrList.get(selectPos));

			}
		});

		gvBackup = (GridView) findViewById(R.id.gvBackup);
		backupAdapter = new BackupAdapter();
		gvBackup.setAdapter(backupAdapter);
		gvBackup.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View selectedView, int position, long id) {

				selectPos = position;

				// make gridview item focused effect
				gvBackup.requestFocusFromTouch();
				selectedView.requestFocusFromTouch();
			}
		});

		gvBackup.setOnItemLongClickListener(new OnItemLongClickListener() {

			private int longClickPos = -1;

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

				longClickPos = position;
				AlertDialog.Builder builder = new AlertDialog.Builder(BackupActivity.this);
				builder.setIcon(R.drawable.mark_delete);
				builder.setTitle("删除备份");
				builder.setMessage("确定要删除日期为" + backupDateStrList.get(longClickPos) + "的备份");
				builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

						BackupService.deleteBackupFromDateStr(backupDateStrList.get(longClickPos));
						BackupActivity.this.backupDateStrList = FileUtils.getFileNameList(new File(
								Constants.BACK_UP_ROOT));
						backupAdapter.notifyDataSetChanged();
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

	}

	private void showProgress(String info) {
		pdLoad = ProgressDialog.show(this, "请稍等", "正在备份……");
		gvBackup.setVisibility(View.GONE);
	}

	private void hideProgress() {
		pdLoad.dismiss();
		gvBackup.setVisibility(View.VISIBLE);
	}

	class BackupTask extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {
			String curDateStr = params[0];
			BackupService.saveBackupByDateStr(curDateStr);
			return null;
		}

		@Override
		protected void onPostExecute(Void v) {

			BackupActivity.this.backupDateStrList = FileUtils
					.getFileNameList(new File(Constants.BACK_UP_ROOT));
			backupAdapter.notifyDataSetChanged();
			hideProgress();

			super.onPostExecute(v);
		}
	}

	class RestoreTask extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {
			String curDateStr = params[0];
			BackupService.restoreByDateStr(curDateStr);
			return null;
		}

		@Override
		protected void onPostExecute(Void v) {

			ComponentUtils.createInfoDialog(BackupActivity.this,
					"数据已还原至日期：" + backupDateStrList.get(selectPos)).show();

			selectPos = GRIDVIEW_UNSELECTED;
			// refresh ArFragment data
			ArFragment.newInstance().refresh();
			hideProgress();
			super.onPostExecute(v);
		}
	}

	private class BackupAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return backupDateStrList.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			BackupHolder holder = new BackupHolder();
			if (convertView == null) {

				convertView = ComponentUtils.getLayoutInflater(BackupActivity.this).inflate(
						R.layout.backup_gridview_item, null);
				holder.tvBackupDate = (TextView) convertView.findViewById(R.id.tvBackupDate);

				convertView.setTag(holder);
			} else {
				holder = (BackupHolder) convertView.getTag();
			}

			holder.tvBackupDate.setText(backupDateStrList.get(position));
			return convertView;

		}

		private class BackupHolder {
			public TextView tvBackupDate;
		}

	}
}
