package com.pengjun.ka.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.widget.DatePicker;

import com.pengjun.ka.R;

public class ComponentUtils {

	public static LayoutInflater inflater = null;

	public static Dialog createInfoDialog(Context context, String info) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(R.drawable.title_warning);
		builder.setTitle("信息");
		builder.setMessage(info);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});
		return builder.create();
	}

	public static String DatePicker2FormatStr(DatePicker dp) {
		return String
				.format(TimeUtils.DATE_STRING_FORMT, dp.getYear(), dp.getMonth() + 1, dp.getDayOfMonth());
	}

	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static LayoutInflater getLayoutInflater(Context context) {
		if (inflater == null) {
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		return inflater;
	}

}
