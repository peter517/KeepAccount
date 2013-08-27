package com.pengjun.ka.android.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.pengjun.ka.R;
import com.pengjun.ka.android.component.GalleryFlow;
import com.pengjun.ka.db.service.ReportNotificationService;
import com.pengjun.ka.net.KaClient;

public class MagicBoxFragment extends Fragment {

	GalleryFlow gfChart;
	Button btOpenMagicBox;

	public static MagicBoxFragment instance = null;

	public static MagicBoxFragment newInstance() {
		if (instance == null) {
			instance = new MagicBoxFragment();
			return instance;
		}
		return instance;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.magic_box, null);
		btOpenMagicBox = (Button) view.findViewById(R.id.btOpenMagicBox);
		btOpenMagicBox.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				ReportNotificationService.startReportNotification(getActivity());
				// if (ArFragment.newInstance().hasEnoughArData() == false) {
				// Toast.makeText(getActivity(), "没有充足的记账数据，无法打开魔方",
				// Constants.TOAST_EXSIT_TIME).show();
				// return;
				// }
				// Intent intent = new Intent();
				// intent.setClass(getActivity(), MagicBoxActivity.class);
				// startActivity(intent);
				// getActivity().overridePendingTransition(R.anim.left_in,
				// R.anim.left_out);

			}
		});
		return view;
	}

	@Override
	public void onDestroy() {
		KaClient.getInstance().disConnect();
		super.onDestroy();
	}

}
