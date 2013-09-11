package com.pengjun.ka.android.activity;

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

import com.pengjun.ka.R;
import com.pengjun.ka.db.dao.ArDao;
import com.pengjun.ka.db.dao.ArTypeDao;
import com.pengjun.ka.db.model.AccountRecord;
import com.pengjun.ka.utils.KaConstants;
import com.pengjun.utils.ComponentUtils;
import com.pengjun.utils.NumberUtils;
import com.pengjun.utils.TimeUtils;

public class AddArActivity extends Activity {

	private static final int MAX_COUNT_PER_AR = 10000000;
	private EditText etAccount = null;
	private Spinner spArTypeName = null;
	private DatePicker dpCreateDate = null;
	private EditText etComment = null;
	private Button btManageArType = null;

	private ImageButton btSave = null;
	private ImageButton btCancel = null;

	private AccountRecord ar;

	private List<String> arTypeNameList = new ArrayList<String>();
	private ArrayAdapter<String> arTypeNameAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.add_ar);

		arTypeNameList = ArTypeDao.getSingleInstance().queryAllArTypeName();

		etAccount = (EditText) findViewById(R.id.etAccount);

		spArTypeName = (Spinner) findViewById(R.id.spArTypeName);
		updataSpTypeAdapter();

		dpCreateDate = (DatePicker) findViewById(R.id.dpCreateDate);
		etComment = (EditText) findViewById(R.id.etComment);

		ar = (AccountRecord) getIntent().getSerializableExtra(KaConstants.INTENT_AR_BEAN);
		if (ar != null) {
			putArToView(ar);
		}

		btManageArType = (Button) findViewById(R.id.btManageArType);
		btManageArType.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(AddArActivity.this, ManageArTypeActivity.class);
				startActivityForResult(intent, KaConstants.CB_ADD_AR_TYPE_NAME);
				overridePendingTransition(R.anim.left_in, R.anim.left_out);
			}
		});

		if ((Boolean) getIntent().getSerializableExtra(KaConstants.INTENT_DISABLE_AR_TYPE_MANAGE) != null) {
			btManageArType.setVisibility(View.GONE);
		}

		btSave = (ImageButton) findViewById(R.id.btSave);
		btSave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!etAccount.getText().toString().equals("")) {

					Float account = Float.valueOf(etAccount.getText().toString());
					if (account.isInfinite() || account.isNaN()) {
						etAccount.setText("");
						ComponentUtils.createInfoDialog(AddArActivity.this, "输入金额无效",
								R.drawable.title_warning).show();
						return;
					}
					if (account > MAX_COUNT_PER_AR) {
						etAccount.setText("");
						ComponentUtils.createInfoDialog(AddArActivity.this, "单笔记账金额不能超过1千万",
								R.drawable.title_warning).show();
						return;
					}

					// add or update
					if (ar == null) {
						ar = new AccountRecord();
						getArFromView(ar);
						ArDao.getSingleInstance().insert(ar);
					} else {
						getArFromView(ar);
						ArDao.getSingleInstance().update(ar);
					}

					setResult(RESULT_OK, null);
					finish();
				} else {
					ComponentUtils.createInfoDialog(AddArActivity.this, "请输入金额", R.drawable.title_warning)
							.show();
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
		if (requestCode == KaConstants.CB_ADD_AR_TYPE_NAME) {
			if (resultCode == RESULT_OK) {
				ArrayList<String> arTypeNameList = data.getExtras().getStringArrayList(
						KaConstants.INTENT_AR_TYPE_NAME_LIST_BEAN);
				this.arTypeNameList = arTypeNameList;
				updataSpTypeAdapter();
				arTypeNameAdapter.notifyDataSetChanged();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void updataSpTypeAdapter() {
		arTypeNameAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				arTypeNameList);
		arTypeNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spArTypeName.setAdapter(arTypeNameAdapter);
	}

	private void putArToView(AccountRecord ar) {
		etAccount.setText(String.valueOf((ar.getAccount())));

		spArTypeName.setSelection(arTypeNameList.indexOf(ar.getTypeName()));
		etComment.setText(String.valueOf((ar.getComment())));

		String[] date = TimeUtils.String2DateStrArr(ar.getCreateDate());
		dpCreateDate.updateDate(Integer.valueOf(date[0]), Integer.valueOf(date[1]) - 1,
				Integer.valueOf(date[2]));
	}

	private void getArFromView(AccountRecord ar) {
		ar.setAccount(NumberUtils.formatFloat(Float.valueOf(etAccount.getText().toString())));
		ar.setTypeName(spArTypeName.getSelectedItem().toString());
		ar.setCreateDate(ComponentUtils.DatePicker2FormatStr(dpCreateDate));
		ar.setComment(etComment.getText().toString());
		ar.setUpdateTime(TimeUtils.getCurTimeStr());
	}
}
