package com.pengjun.ka.android.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.pengjun.ka.R;
import com.pengjun.ka.android.activity.BackupActivity;
import com.pengjun.ka.android.activity.ManageArTypeActivity;
import com.pengjun.utils.ComponentUtils;
import com.pengjun.utils.ResourceUtils;

public class SettingFragment extends Fragment {

	RelativeLayout rlManageType;
	RelativeLayout rlBackup;
	Button btExit;

	public static SettingFragment instance = null;

	public static SettingFragment newInstance() {
		if (instance == null) {
			instance = new SettingFragment();
			return instance;
		}
		return instance;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.setting_sys, null);

		rlManageType = (RelativeLayout) view.findViewById(R.id.rlManageType);
		rlManageType.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// could not use xml to control background change
				rlManageType.setBackgroundResource(R.drawable.text_focused);
				rlBackup.setBackgroundResource(R.drawable.text_normal);

				Intent intent = new Intent();
				intent.setClass(getActivity(), ManageArTypeActivity.class);
				getActivity().startActivity(intent);
				getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);
			}
		});

		rlBackup = (RelativeLayout) view.findViewById(R.id.rlBackup);
		rlBackup.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (ResourceUtils.hasExternalStorage() == false) {
					ComponentUtils.createInfoDialog(getActivity(), "请插入内存卡！", R.drawable.title_warning)
							.show();
					return;
				}

				rlBackup.setBackgroundResource(R.drawable.text_focused);
				rlManageType.setBackgroundResource(R.drawable.text_normal);
				Intent intent = new Intent();
				intent.setClass(getActivity(), BackupActivity.class);
				getActivity().startActivity(intent);
				getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);
			}
		});

		btExit = (Button) view.findViewById(R.id.btExit);
		btExit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				getActivity().finish();
			}
		});
		return view;
	}
}
