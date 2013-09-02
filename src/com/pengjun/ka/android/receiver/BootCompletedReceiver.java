package com.pengjun.ka.android.receiver;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.pengjun.ka.android.service.ReportNotificationService;

public class BootCompletedReceiver extends BroadcastReceiver {

	private ReportNotificationService reportService;
	private ServiceConnection serviceConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName name, IBinder service) {
			reportService = ((ReportNotificationService.ReportServiceBinder) service).getService();
		}

		public void onServiceDisconnected(ComponentName name) {
			reportService = null;
		}
	};

	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			Intent newIntent = new Intent(context, ReportNotificationService.class);
			newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startService(newIntent);
		}
	}
}