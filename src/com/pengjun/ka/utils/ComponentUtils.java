package com.pengjun.ka.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.DatePicker;

import com.pengjun.ka.R;

public class ComponentUtils {

	public static Dialog createAlertDialog(Context context, String warnInfo) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(R.drawable.title_warning);
		builder.setTitle("信息");
		builder.setMessage(warnInfo);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});
		return builder.create();
	}

	public static String DatePicker2FormatStr(DatePicker dp) {
		return String.format(TimeUtils.DATE_FORMT, dp.getYear(), dp.getMonth() + 1, dp.getDayOfMonth());
	}

}
