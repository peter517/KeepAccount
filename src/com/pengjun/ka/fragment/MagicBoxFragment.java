package com.pengjun.ka.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pengjun.ka.component.GalleryFlow;
import com.pengjun.keepaccounts.R;

public class MagicBoxFragment extends Fragment {

	GalleryFlow gfChart;

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

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.ar_chart, null);
		return view;
	}
}
