package com.pengjun.ka.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.pengjun.ka.R;
import com.pengjun.ka.component.GalleryFlow;
import com.pengjun.ka.net.KaClient;

public class MagicBoxFragment extends Fragment {

	GalleryFlow gfChart;
	Button btOpenMagicBox;

	// ImageView ivMagicBox;
	// TextView tvTilte;
	// TextView tvTilte1;

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
		btOpenMagicBox = (Button) view.findViewById(R.id.btOpenMagicBox);

		KaClient.getInstance().connect();
		btOpenMagicBox.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				KaClient.getInstance().sendData();
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
