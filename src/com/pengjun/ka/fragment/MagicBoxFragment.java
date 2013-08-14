package com.pengjun.ka.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pengjun.ka.component.GalleryFlow;
import com.pengjun.ka.jni.MagicBox;
import com.pengjun.keepaccounts.R;

public class MagicBoxFragment extends Fragment {

	GalleryFlow gfChart;
	TextView tvTilte;
	TextView tvTilte1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

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

		tvTilte = (TextView) view.findViewById(R.id.tvTilte);
		tvTilte1 = (TextView) view.findViewById(R.id.tvTilte1);
		long time = System.currentTimeMillis();
		String.valueOf(MagicBox.add(0, 100));
		tvTilte.setText(String.valueOf(System.currentTimeMillis() - time));

		time = System.currentTimeMillis();
		int c = 0;
		for (int i = 0; i < 100; i++) {
			c = i + c;
		}
		tvTilte1.setText(String.valueOf(System.currentTimeMillis() - time));
		return view;
	}
}
