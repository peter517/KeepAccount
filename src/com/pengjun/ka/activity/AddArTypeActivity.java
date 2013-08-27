package com.pengjun.ka.activity;

import java.lang.reflect.Field;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.pengjun.ka.R;
import com.pengjun.ka.db.model.ArType;
import com.pengjun.ka.db.service.ArTypeService;
import com.pengjun.ka.utils.ComponentUtils;
import com.pengjun.ka.utils.Constants;
import com.pengjun.ka.utils.ResourceUtils;
import com.pengjun.ka.utils.TimeUtils;

public class AddArTypeActivity extends Activity {

	private GridView gvArTypeImg;
	private EditText etArTypeName;

	private ImageButton btSave;
	private ImageButton btCancel;

	private final int GRIDVIEW_UNSELECTED = -1;
	private int selectPos = GRIDVIEW_UNSELECTED;

	private ArrayList<String> resNameList = new ArrayList<String>();
	private ArTypeImgAdapter arTypeImgAdapter;
	private ArType arType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.add_ar_type);

		etArTypeName = (EditText) findViewById(R.id.etArTypeName);
		etArTypeName.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					selectPos = GRIDVIEW_UNSELECTED;
				}
			}
		});

		btSave = (ImageButton) findViewById(R.id.btSave);
		btSave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (etArTypeName.getText().toString().equals("")) {
					ComponentUtils.createInfoDialog(AddArTypeActivity.this, "请输入新建类名称").show();
					return;
				}
				if (selectPos == GRIDVIEW_UNSELECTED) {
					ComponentUtils.createInfoDialog(AddArTypeActivity.this, "请选择图片").show();
					return;
				}

				if (ArTypeService.isTypeNameExsit(etArTypeName.getText().toString())) {
					ComponentUtils.createInfoDialog(AddArTypeActivity.this, "类名称已存在，请重新输入").show();
					return;
				}

				// add or update
				if (arType == null) {
					arType = new ArType();
					arType.setTypeName(etArTypeName.getText().toString());
					arType.setImgResName(resNameList.get(selectPos));
					arType.setCreateDate(TimeUtils.getCurDateStr());
					arType.setUpdateTime(TimeUtils.getCurTimeStr());
					ArTypeService.insert(arType);
				} else {
					arType.setTypeName(etArTypeName.getText().toString());
					arType.setImgResName(resNameList.get(selectPos));
					arType.setUpdateTime(TimeUtils.getCurTimeStr());
					ArTypeService.update(arType);
				}

				setResult(RESULT_OK, null);
				finish();
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

		gvArTypeImg = (GridView) findViewById(R.id.gvTypeImg);
		arTypeImgAdapter = new ArTypeImgAdapter();
		gvArTypeImg.setAdapter(arTypeImgAdapter);
		gvArTypeImg.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View selectedView, int position, long id) {

				selectPos = position;

				// make gridview item focused effect
				gvArTypeImg.requestFocusFromTouch();
				selectedView.requestFocusFromTouch();
			}
		});

		arType = (ArType) getIntent().getSerializableExtra(Constants.INTENT_AR_TYPE_BEAN);
		if (arType != null) {
			etArTypeName.setText(arType.getTypeName());
		}

	}

	private class ArTypeImgAdapter extends BaseAdapter {

		public ArTypeImgAdapter() {

			// get all image from res which name start with type
			Field[] fields = R.drawable.class.getDeclaredFields();
			for (Field field : fields) {
				if (field.getName().startsWith(ResourceUtils.RES_IMAGE_PREFIX)) {
					resNameList.add(field.getName());
				}
			}
		}

		@Override
		public int getCount() {
			return resNameList.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ArTypeHolder holder = new ArTypeHolder();
			if (convertView == null) {

				convertView = ComponentUtils.getLayoutInflater(AddArTypeActivity.this).inflate(
						R.layout.ar_type_gridview_item, null);
				holder.ivArType = (ImageView) convertView.findViewById(R.id.ivArType);

				convertView.setTag(holder);
			} else {
				holder = (ArTypeHolder) convertView.getTag();
			}

			holder.ivArType.setImageResource(ResourceUtils.getImgResIdByResName(resNameList.get(position)));
			return convertView;

		}

		private class ArTypeHolder {
			public ImageView ivArType;
		}

	};

}
