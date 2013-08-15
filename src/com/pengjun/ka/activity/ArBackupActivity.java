package com.pengjun.ka.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pengjun.ka.db.service.BackupService;
import com.pengjun.ka.fragment.ArFragment;
import com.pengjun.ka.utils.ComponentUtils;
import com.pengjun.ka.utils.Constants;
import com.pengjun.ka.utils.FileUtils;
import com.pengjun.ka.utils.TimeUtils;
import com.pengjun.keepaccounts.R;

public class ArBackupActivity extends Activity {

	private GridView gvBackup;
	private ImageButton ibAddBackup;
	private ImageButton ibRestore;
	private ProgressBar pbLoad;

	private List<String> backupDateStrList = new ArrayList<String>();
	private BackupAdapter backupAdapter;

	private final int GV_UNSELECTED = -1;
	private int selectPos = GV_UNSELECTED;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.backup);

		FileUtils.createDir(Constants.BACK_UP_ROOT);
		backupDateStrList = FileUtils.getFileNameList(new File(Constants.BACK_UP_ROOT));

		pbLoad = (ProgressBar) findViewById(R.id.pbLoad);

		ibAddBackup = (ImageButton) findViewById(R.id.ibAddBackup);
		ibAddBackup.setOnClickListener(new View.OnClickListener() {
			private String curDateStr;

			@Override
			public void onClick(View v) {

				selectPos = GV_UNSELECTED;

				curDateStr = TimeUtils.getCurDateStr();
				if (backupDateStrList.size() != 0 && backupDateStrList.contains(TimeUtils.getCurDateStr())) {
					AlertDialog.Builder builder = new AlertDialog.Builder(ArBackupActivity.this);
					builder.setIcon(R.drawable.mark_paste);
					builder.setTitle("替换备份");
					builder.setMessage("确定要替换日期为" + curDateStr + "的备份");
					builder.setPositiveButton("替换", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							showProgress();
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
						btNegative.setBackgroundResource(R.drawable.btn_normal);
					}
				} else {
					showProgress();
					new BackupTask().execute(curDateStr);
				}
			}

		});

		ibRestore = (ImageButton) findViewById(R.id.ibRestore);
		ibRestore.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if (selectPos == GV_UNSELECTED) {
					ComponentUtils.createAlertDialog(ArBackupActivity.this, "请选择还原日期").show();
					return;
				}

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
				gvBackup.requestFocusFromTouch();
				selectedView.requestFocusFromTouch();
			}
		});

		gvBackup.setOnItemLongClickListener(new OnItemLongClickListener() {

			private int longClickPos = -1;

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

				longClickPos = position;
				AlertDialog.Builder builder = new AlertDialog.Builder(ArBackupActivity.this);
				builder.setIcon(R.drawable.mark_delete);
				builder.setTitle("删除备份");
				builder.setMessage("确定要删除日期为" + backupDateStrList.get(longClickPos) + "的备份");
				builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

						BackupService.deleteBackup(backupDateStrList.get(longClickPos));
						ArBackupActivity.this.backupDateStrList = FileUtils.getFileNameList(new File(
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
					btNegative.setBackgroundResource(R.drawable.btn_normal);
				}

				return true;
			}
		});

	}

	private void showProgress() {
		pbLoad.setVisibility(View.VISIBLE);
		gvBackup.setVisibility(View.GONE);
	}

	private void hideProgress() {
		pbLoad.setVisibility(View.GONE);
		gvBackup.setVisibility(View.VISIBLE);
	}

	class BackupTask extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {
			String curDateStr = params[0];
			BackupService.backup(curDateStr);
			return null;
		}

		@Override
		protected void onPostExecute(Void v) {

			ArBackupActivity.this.backupDateStrList = FileUtils.getFileNameList(new File(
					Constants.BACK_UP_ROOT));
			backupAdapter.notifyDataSetChanged();
			hideProgress();

			super.onPostExecute(v);
		}
	}

	class RestoreTask extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {
			String curDateStr = params[0];
			BackupService.restore(curDateStr);
			return null;
		}

		@Override
		protected void onPostExecute(Void v) {

			ComponentUtils.createAlertDialog(ArBackupActivity.this, "数据已还原至日期：" + backupDateStrList.get(selectPos))
					.show();

			selectPos = GV_UNSELECTED;
			ArFragment.newInstance().recycle();
			hideProgress();
			super.onPostExecute(v);
		}
	}

	private class BackupAdapter extends BaseAdapter {

		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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

				convertView = inflater.inflate(R.layout.backup_gridview_item, null);
				holder.tvBackupDate = (TextView) convertView.findViewById(R.id.tvBackupDate);

				convertView.setTag(holder);
			} else {
				holder = (BackupHolder) convertView.getTag();
			}

			String backupFileName = backupDateStrList.get(position);
			holder.tvBackupDate.setText(backupFileName);
			return convertView;

		}

		private class BackupHolder {
			public TextView tvBackupDate;
		}

	}
}
