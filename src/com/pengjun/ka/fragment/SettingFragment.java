package com.pengjun.ka.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.pengjun.ka.activity.ManageArTypeActivity;
import com.pengjun.keepaccounts.R;

public class SettingFragment extends Fragment {

	RelativeLayout rlManageType;
	RelativeLayout rlBackup;
	Button btExit;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public static SettingFragment instance = null;

	public static SettingFragment newInstance() {
		if (instance == null) {
			instance = new SettingFragment();
			return instance;
		}
		return instance;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.setting_sys, null);

		rlManageType = (RelativeLayout) view.findViewById(R.id.rlManageType);
		rlManageType.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				rlManageType.setBackgroundResource(R.drawable.text_focused);
				rlBackup.setBackgroundResource(R.drawable.text_normal);

				Intent intent = new Intent();
				intent.setClass(getActivity(), ManageArTypeActivity.class);
				getActivity().startActivity(intent);
				getActivity().overridePendingTransition(R.anim.left_in,
						R.anim.left_out);
			}
		});

		rlBackup = (RelativeLayout) view.findViewById(R.id.rlBackup);
		rlBackup.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				rlBackup.setBackgroundResource(R.drawable.text_focused);
				rlManageType.setBackgroundResource(R.drawable.text_normal);
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
