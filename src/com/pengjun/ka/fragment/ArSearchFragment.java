package com.pengjun.ka.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
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

import com.pengjun.ka.activity.ArSearchResultActivity;
import com.pengjun.ka.db.model.ArSearchCondition;
import com.pengjun.ka.db.service.ArTypeService;
import com.pengjun.ka.utils.ComponentUtils;
import com.pengjun.ka.utils.Constants;
import com.pengjun.ka.utils.TimeUtils;
import com.pengjun.keepaccounts.R;

public class ArSearchFragment extends Fragment {

	private EditText etStartAccount;
	private EditText etEndAccount;

	private Spinner spArTypeName;

	private Button btStartDate;
	private Button btEndDate;

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

	public boolean isTvAccountFocous() {
		return etStartAccount.isFocused() || etEndAccount.isFocused();
	}

	public static ArSearchFragment newInstance() {
		if (instance == null) {
			instance = new ArSearchFragment();
		}
		return instance;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.ar_search, null);

		etStartAccount = (EditText) view.findViewById(R.id.etStartAccount);
		etEndAccount = (EditText) view.findViewById(R.id.etEndAccount);

		List<String> arTypeNameList = new ArrayList<String>();
		arTypeNameList.add(ALL_AR_TYPE);
		arTypeNameList.addAll(ArTypeService.queryAllArTypeName());

		spArTypeName = (Spinner) view.findViewById(R.id.spArTypeName);
		ArrayAdapter<String> arTypeNameAdapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, arTypeNameList);
		arTypeNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spArTypeName.setAdapter(arTypeNameAdapter);

		btStartDate = (Button) view.findViewById(R.id.btStartDate);
		btStartDate.setOnClickListener(new View.OnClickListener() {

			OnDateSetListener dateSetListener = new OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					String setDateStr = String
							.format(TimeUtils.DATE_FORMT, year, monthOfYear + 1, dayOfMonth);
					btStartDate.setText(setDateStr);
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

				DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), dateSetListener,
						year, mouth, day);

				datePickerDialog.show();
			}
		});

		btEndDate = (Button) view.findViewById(R.id.btEndDate);
		btEndDate.setOnClickListener(new View.OnClickListener() {

			OnDateSetListener dateSetListener = new OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					String setDateStr = String
							.format(TimeUtils.DATE_FORMT, year, monthOfYear + 1, dayOfMonth);
					btEndDate.setText(setDateStr);
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

				DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), dateSetListener,
						year, mouth, day);

				datePickerDialog.show();
			}
		});

		btArSearch = (Button) view.findViewById(R.id.btArSearch);
		btArSearch.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// legal input certify

				boolean isAccountLegal = true;

				// both not empty
				if (!etStartAccount.getText().toString().equals("")
						&& !etEndAccount.getText().toString().equals("")) {
					isAccountLegal = Float.valueOf(etStartAccount.getText().toString()) <= Float
							.valueOf(etEndAccount.getText().toString());
				}

				boolean isDateLegal = true;
				if (!btStartDate.getText().toString().equals("")
						&& !btEndDate.getText().toString().equals("")) {
					isDateLegal = btStartDate.getText().toString().compareTo(btEndDate.getText().toString()) <= 0;
				}

				if (!isAccountLegal) {
					ComponentUtils.createAlertDialog(getActivity(), "金额应该右边大于或等于左边").show();
					return;
				}

				if (!isDateLegal) {
					ComponentUtils.createAlertDialog(getActivity(), "日期应该右边大于或等于左边").show();
					return;
				}

				Intent intent = new Intent();
				intent.setClass(getActivity(), ArSearchResultActivity.class);

				ArSearchCondition arSC = new ArSearchCondition();
				arSC.setStartAccount(etStartAccount.getText().toString());
				arSC.setEndAccount(etEndAccount.getText().toString());
				arSC.setStartDate(btStartDate.getText().toString());
				arSC.setEndDate(btEndDate.getText().toString());

				if (!spArTypeName.getSelectedItem().toString().equals(ALL_AR_TYPE)) {
					arSC.setType(spArTypeName.getSelectedItem().toString());
				}

				Bundle bundle = new Bundle();
				bundle.putSerializable(Constants.INTENT_AR_SEARCH_CONDITION, arSC);
				intent.putExtras(bundle);
				startActivityForResult(intent, Constants.CB_ADD_AR);
				getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);
			}
		});
		return view;
	}
}
