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
import android.widget.Toast;

import com.pengjun.ka.db.model.AccountRecord;
import com.pengjun.ka.db.service.AccountRecordService;
import com.pengjun.ka.fragment.ArFragment;
import com.pengjun.ka.tools.Constants;
import com.pengjun.ka.tools.Util;
import com.pengjun.keepaccounts.R;

public class AddArActivity extends Activity {

	private EditText etAccount = null;
	private Spinner spType = null;
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
		setContentView(R.layout.add_ar);

		etAccount = (EditText) findViewById(R.id.etAccount);

		spType = (Spinner) findViewById(R.id.spType);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, Constants.TYPE_STR_ARR);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spType.setAdapter(adapter);

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

					setResult(RESULT_OK, null);
					finish();
				} else {
					Toast.makeText(AddArActivity.this, "请输入金额", 3000).show();
				}
			}

		});

		btCancel = (ImageButton) findViewById(R.id.btCancel);
		btCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED, null);
				finish();

			}
		});

		ar = (AccountRecord) getIntent().getSerializableExtra(
				ArFragment.AR_BEAN);
		if (ar != null) {
			putArToView(ar);
		}
	}

	private void putArToView(AccountRecord ar) {
		etAccount.setText(String.valueOf((ar.getAcount())));
		spType.setSelection(Constants.getPosByCategroyStr(ar.getType()));
		etComment.setText(String.valueOf((ar.getComment())));

		String[] date = Util.String2DateArr(ar);
		dpDate.updateDate(Integer.valueOf(date[0]),
				Integer.valueOf(date[1]) - 1, Integer.valueOf(date[2]));
	}

	private void getArFromView(AccountRecord ar) {
		ar.setAcount(Float.valueOf(etAccount.getText().toString()));
		ar.setType(spType.getSelectedItem().toString());
		ar.setDate(Util.DatePicker2FormatStr(dpDate));
		ar.setComment(etComment.getText().toString());
		ar.setUpdateTime(Util.getCurTimeStr());
	}
}
