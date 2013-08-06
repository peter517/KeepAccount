package com.pengjun.ka.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.pengjun.ka.db.model.AccountRecord;
import com.pengjun.ka.db.service.ArService;
import com.pengjun.ka.db.service.ArTypeService;
import com.pengjun.ka.tools.Constants;
import com.pengjun.ka.tools.Util;
import com.pengjun.keepaccounts.R;

public class AddArActivity extends Activity {

	private EditText etAccount = null;
	private Spinner spArTypeName = null;
	private DatePicker dpCreateDate = null;
	private EditText etComment = null;
	private Button btManageArType = null;

	private ImageButton btSave = null;
	private ImageButton btCancel = null;

	private AccountRecord ar;

	List<String> arTypeNameList = new ArrayList<String>();
	ArrayAdapter<String> arTypeNameAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.add_ar);

		arTypeNameList = ArTypeService.queryAllArTypeName();

		etAccount = (EditText) findViewById(R.id.etAccount);

		spArTypeName = (Spinner) findViewById(R.id.spArTypeName);
		updataSpTypeAdapter();

		dpCreateDate = (DatePicker) findViewById(R.id.dpCreateDate);
		etComment = (EditText) findViewById(R.id.etComment);

		ar = (AccountRecord) getIntent().getSerializableExtra(
				Constants.INTENT_AR_BEAN);
		if (ar != null) {
			putArToView(ar);
		}

		btManageArType = (Button) findViewById(R.id.btManageArType);
		btManageArType.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(AddArActivity.this, ManageArTypeActivity.class);
				startActivityForResult(intent, Constants.CB_ADD_AR_TYPE_NAME);
				overridePendingTransition(R.anim.left_in, R.anim.left_out);
			}
		});

		if ((Boolean) getIntent().getSerializableExtra(
				Constants.INTENT_DISABLE_AR_TYPE_MANAGE) != null) {
			btManageArType.setVisibility(View.GONE);
		}

		btSave = (ImageButton) findViewById(R.id.btSave);
		btSave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!etAccount.getText().toString().equals("")) {

					// add or update
					if (ar == null) {
						ar = new AccountRecord();
						getArFromView(ar);
						ArService.insert(ar);
					} else {
						getArFromView(ar);
						ArService.update(ar);
					}

					setResult(RESULT_OK, null);
					finish();
				} else {
					Util.createAlertDialog(AddArActivity.this, "请输入金额").show();
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

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == Constants.CB_ADD_AR_TYPE_NAME) {
			if (resultCode == RESULT_OK) {
				ArrayList<String> arTypeNameList = data.getExtras()
						.getStringArrayList(
								Constants.INTENT_AR_TYPE_NAME_LIST_BEAN);
				this.arTypeNameList = arTypeNameList;
				updataSpTypeAdapter();
				arTypeNameAdapter.notifyDataSetChanged();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void updataSpTypeAdapter() {
		arTypeNameAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, arTypeNameList);
		arTypeNameAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spArTypeName.setAdapter(arTypeNameAdapter);
	}

	private void putArToView(AccountRecord ar) {
		etAccount.setText(String.valueOf((ar.getAccount())));
		spArTypeName.setSelection(Util.getPosFromList(arTypeNameList,
				ar.getType()));
		etComment.setText(String.valueOf((ar.getComment())));

		String[] date = Util.String2DateArr(ar.getCreateDate());
		dpCreateDate.updateDate(Integer.valueOf(date[0]),
				Integer.valueOf(date[1]) - 1, Integer.valueOf(date[2]));
	}

	private void getArFromView(AccountRecord ar) {
		ar.setAccount(Float.valueOf(etAccount.getText().toString()));
		ar.setType(spArTypeName.getSelectedItem().toString());
		ar.setCreateDate(Util.DatePicker2FormatStr(dpCreateDate));
		ar.setComment(etComment.getText().toString());
		ar.setUpdateTime(Util.getCurTimeStr());
	}
}
