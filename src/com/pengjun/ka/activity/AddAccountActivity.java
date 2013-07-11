package com.pengjun.ka.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.pengjun.ka.db.model.AccountRecord;
import com.pengjun.ka.db.service.AccountRecordService;
import com.pengjun.ka.tools.Constants;
import com.pengjun.ka.tools.Util;
import com.pengjun.keepaccounts.R;

public class AddAccountActivity extends Activity {

	private EditText etAccount = null;
	private Spinner spCategory = null;
	private DatePicker dpDate = null;
	private EditText etComment = null;

	private ImageButton btSave = null;
	private ImageButton btCancel = null;

	private AccountRecord ar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_add_account);

		etAccount = (EditText) findViewById(R.id.etAccount);

		spCategory = (Spinner) findViewById(R.id.spCategory);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, Constants.CATEGORY_ARR);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spCategory.setAdapter(adapter);

		dpDate = (DatePicker) findViewById(R.id.dpDate);
		etComment = (EditText) findViewById(R.id.etComment);

		btSave = (ImageButton) findViewById(R.id.btSave);
		btSave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!etAccount.getText().toString().equals("")) {

					// add or update
					if (ar == null) {
						ar = new AccountRecord();
						getArFromView(ar);
						AccountRecordService.insert(ar);
					} else {
						getArFromView(ar);
						AccountRecordService.update(ar);
					}
					finish();
				}
			}

		});

		btCancel = (ImageButton) findViewById(R.id.btCancel);
		btCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		ar = (AccountRecord) getIntent().getSerializableExtra(
				KAMainActivity.AR_BEAN);
		if (ar != null) {
			putArToView(ar);
		}
	}

	private void putArToView(AccountRecord ar) {
		etAccount.setText(String.valueOf((ar.getAmount())));
		spCategory
				.setSelection(Constants.getPosByCategroyStr(ar.getCategory()));
		etComment.setText(String.valueOf((ar.getComment())));

		String[] date = Util.String2DateArr(ar);
		dpDate.updateDate(Integer.valueOf(date[0]),
				Integer.valueOf(date[1]) - 1, Integer.valueOf(date[2]));
	}

	private void getArFromView(AccountRecord ar) {
		ar.setAmount(Float.valueOf(etAccount.getText().toString()));
		ar.setCategory(spCategory.getSelectedItem().toString());
		ar.setDate(Util.DatePicker2FormatStr(dpDate));
		ar.setComment(etComment.getText().toString());
		ar.setUpdateTime(Util.getCurTimeStr());
	}
}
