package com.pengjun.ka.utils;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

import com.pengjun.keepaccounts.R;
import com.pengjun.keepaccounts.R.drawable;

public class ComponentUtils {

	public static Dialog createAlertDialog(Context context, String warnInfo) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(R.drawable.title_warning);
		builder.setTitle("警告");
		builder.setMessage(warnInfo);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});
		return builder.create();
	}

}
