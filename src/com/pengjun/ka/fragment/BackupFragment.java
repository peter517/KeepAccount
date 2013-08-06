package com.pengjun.ka.fragment;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.pengjun.ka.db.model.AccountRecord;
import com.pengjun.ka.db.service.ArService;
import com.pengjun.ka.tools.FileUtils;
import com.pengjun.ka.tools.Util;
import com.pengjun.keepaccounts.R;

public class BackupFragment extends Fragment {

	ImageButton ibBackup;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public static BackupFragment instance = null;

	public static BackupFragment newInstance() {
		if (instance == null) {
			instance = new BackupFragment();
			return instance;
		}
		return instance;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.backup_data, null);
		ibBackup = (ImageButton) view.findViewById(R.id.ibBackup);
		ibBackup.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				List<AccountRecord> arList = ArService.queryAll();
				if (arList.size() != 0) {
					FileUtils.backupAr(arList);
				}
				Util.createAlertDialog(getActivity(), "备份成功").show();
			}
		});

		ibBackup.setOnLongClickListener(new View.OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				List<AccountRecord> arList = FileUtils.restoreAr();
				ArService.reCreateTable(arList);
				Util.createAlertDialog(getActivity(), "还原成功").show();
				return true;
			}
		});
		return view;
	}
}
