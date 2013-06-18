package com.pengjun.ka.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.pengjun.ka.db.model.AccountRecord;
import com.pengjun.ka.db.service.AccountRecordService;
import com.pengjun.ka.tools.MyDebug;
import com.pengjun.keepaccounts.R;

public class AddAccountActivity extends Activity {

	private EditText etAccount = null;
	private EditText etCategory = null;
	private EditText etDate = null;
	private EditText etComment = null;

	private Button btSave = null;
	private Button btCancel = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		MyDebug.printFromPJ("onCreate");

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_add_account);

		etAccount = (EditText) findViewById(R.id.etAccount);
		etCategory = (EditText) findViewById(R.id.etCategory);
		etDate = (EditText) findViewById(R.id.etDate);
		etComment = (EditText) findViewById(R.id.etComment);

		btSave = (Button) findViewById(R.id.btSave);
		btSave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!etAccount.getText().toString().equals("")) {
					AccountRecord ar = new AccountRecord();
					ar.setAmount(Float.valueOf(etAccount.getText().toString()));
					ar.setCategory(etCategory.getText().toString());
					ar.setDate(etDate.getText().toString());
					ar.setComment(etComment.getText().toString());
					AccountRecordService.insert(ar);
					finish();
				}
			}
		});

		btCancel = (Button) findViewById(R.id.btCancel);
		btCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
