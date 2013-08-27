package com.pengjun.ka.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
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

	public static void createNotification(Context context, String notificationTitle,
			String notificationCotent, PendingIntent pendingIntent) {

		Notification notification = new Notification(R.drawable.report, notificationTitle,
				System.currentTimeMillis());

		notification.flags |= Notification.FLAG_NO_CLEAR;
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		notification.tickerText = notificationCotent;
		notification.defaults = Notification.DEFAULT_SOUND;
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		notification.setLatestEventInfo(context, notificationTitle, notificationCotent, pendingIntent);

		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(R.string.app_name, notification);
	}

}
