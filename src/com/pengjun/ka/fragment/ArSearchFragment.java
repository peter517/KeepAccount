package com.pengjun.ka.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.pengjun.ka.db.service.ArTypeService;
import com.pengjun.ka.tools.Util;
import com.pengjun.keepaccounts.R;

public class ArSearchFragment extends Fragment {

	private EditText etStartAccount;
	private EditText etEndAccount;

	private Spinner spArTypeName;

	private Button btStartDate;
	private Button btEndDate;

	private TextView tvStartDate;
	private TextView tvEndDate;

	private Button btArSearch;

	private final static String ALL_AR_TYPE = "全部";

	private static ArSearchFragment instance = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	public static ArSearchFragment newInstance() {
		if (instance == null) {
			instance = new ArSearchFragment();
		}
		return instance;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.ar_search, null);

		etStartAccount = (EditText) view.findViewById(R.id.etStartAccount);
		etStartAccount.setText("0.0");
		etEndAccount = (EditText) view.findViewById(R.id.etEndAccount);
		etEndAccount.setText("0.0");

		tvStartDate = (TextView) view.findViewById(R.id.tvStartDate);
		tvStartDate.setText(Util.getCurDateStr());
		tvEndDate = (TextView) view.findViewById(R.id.tvEndDate);
		tvEndDate.setText(Util.getCurDateStr());

		btStartDate = (Button) view.findViewById(R.id.btStartDate);
		btStartDate.setOnClickListener(new View.OnClickListener() {

			OnDateSetListener dateSetListener = new OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker view, int year,
						int monthOfYear, int dayOfMonth) {
					String setDateStr = String.format(Util.DATE_FORMT, year,
							monthOfYear + 1, dayOfMonth);
					tvStartDate.setText(setDateStr);
				}
			};

			@Override
			public void onClick(View v) {

				final Calendar mCalendar = Calendar.getInstance();
				long time = System.currentTimeMillis();
				mCalendar.setTimeInMillis(time);
				int year = mCalendar.get(Calendar.YEAR);
				int mouth = mCalendar.get(Calendar.MONTH);
				int day = mCalendar.get(Calendar.DAY_OF_MONTH);

				DatePickerDialog datePickerDialog = new DatePickerDialog(
						getActivity(), dateSetListener, year, mouth, day);

				datePickerDialog.show();
			}
		});

		btEndDate = (Button) view.findViewById(R.id.btEndDate);
		btEndDate.setOnClickListener(new View.OnClickListener() {

			OnDateSetListener dateSetListener = new OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker view, int year,
						int monthOfYear, int dayOfMonth) {
					String setDateStr = String.format(Util.DATE_FORMT, year,
							monthOfYear + 1, dayOfMonth);
					tvEndDate.setText(setDateStr);
				}
			};

			@Override
			public void onClick(View v) {

				final Calendar mCalendar = Calendar.getInstance();
				long time = System.currentTimeMillis();
				mCalendar.setTimeInMillis(time);
				int year = mCalendar.get(Calendar.YEAR);
				int mouth = mCalendar.get(Calendar.MONTH);
				int day = mCalendar.get(Calendar.DAY_OF_MONTH);

				DatePickerDialog datePickerDialog = new DatePickerDialog(
						getActivity(), dateSetListener, year, mouth, day);

				datePickerDialog.show();
			}
		});

		List<String> arTypeNameList = new ArrayList<String>();
		arTypeNameList.add(ALL_AR_TYPE);
		arTypeNameList.addAll(ArTypeService.queryAllArTypeName());

		spArTypeName = (Spinner) view.findViewById(R.id.spArTypeName);
		ArrayAdapter<String> arTypeNameAdapter = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_item,
				arTypeNameList);
		arTypeNameAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spArTypeName.setAdapter(arTypeNameAdapter);

		return view;
	}
}
