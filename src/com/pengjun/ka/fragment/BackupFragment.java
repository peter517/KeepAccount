package com.pengjun.ka.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

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
		return view;
	}

}
