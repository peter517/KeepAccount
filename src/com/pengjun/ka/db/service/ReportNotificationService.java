package com.pengjun.ka.db.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.pengjun.ka.R;
import com.pengjun.ka.android.activity.ReportActivity;
import com.pengjun.ka.db.dao.ArDao;
import com.pengjun.ka.db.model.AccountRecord;
import com.pengjun.ka.db.model.ArSearchCondition;
import com.pengjun.ka.db.model.ReportData;
import com.pengjun.ka.utils.ComponentUtils;
import com.pengjun.ka.utils.NumberUtils;
import com.pengjun.ka.utils.TimeUtils;

public class ReportNotificationService {
	public static List<AccountRecord> arList = new ArrayList<AccountRecord>();
	private static String notificationTitle = "KeepAccount";
	private static String notificationCotent = "月 报表";

	public static void startReportNotification(Context context) {

		// the frist day of month to create the last month report
		ArSearchCondition arSC = new ArSearchCondition();

		arSC.setStartDate(TimeUtils.getLastMonthOfToDayStr());
		arSC.setEndDate(TimeUtils.getLastDayStr());
		arList = ArDao.queryAr(arSC, 0, -1);

		if (arList.size() == 0) {
			return;
		}

		notificationCotent = TimeUtils.getLastMonthStr() + notificationCotent;

		Intent intent = new Intent(context, ReportActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, R.string.app_name, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		ComponentUtils.createNotification(context, notificationTitle, notificationCotent, pendingIntent);
	}

	public static ReportData computeMagicBoxData(List<AccountRecord> arList) {

		double totalCost = 0;

		Map<String, Double> day2AccountMap = new HashMap<String, Double>();

		AccountRecord maxCostAr = new AccountRecord();
		maxCostAr.setAccount(Float.MIN_VALUE);
		int monthStart = 0;
		int monthMiddle = 0;
		int monthEnd = 0;
		Double account = null;
		for (AccountRecord ar : arList) {

			totalCost += ar.getAccount();
			if (ar.getAccount() > maxCostAr.getAccount()) {
				maxCostAr = ar;
			}

			int day = Integer.valueOf(TimeUtils.String2DateStrArr(ar.getCreateDate())[2]);
			if (day > 0 && day <= 10) {
				monthStart++;
			} else if (day <= 20 && day > 10) {
				monthMiddle++;
			} else if (day <= 31 && day > 20) {
				monthEnd++;
			}

			String month = TimeUtils.String2DateStrArr(ar.getCreateDate())[2];
			account = day2AccountMap.get(month);
			if (account == null) {
				account = 0.0;
			}
			day2AccountMap.put(month, account + ar.getAccount());

		}

		ReportData reportData = new ReportData();
		int max = Math.max(monthStart, Math.max(monthMiddle, monthEnd));
		if (max == monthStart) {
			reportData.setMaxCostInterval("上旬");
		} else if (max == monthMiddle) {
			reportData.setMaxCostInterval("中旬");
		} else if (max == monthEnd) {
			reportData.setMaxCostInterval("下旬");
		}

		reportData.setTotalCountNum(arList.size());
		reportData.setTotalCost(NumberUtils.formatDouble(totalCost));
		reportData.setAvgCost(NumberUtils.formatDouble(totalCost / arList.size()));
		reportData.setMaxCost(maxCostAr);

		String maxCostDayStr = null;
		Double maxCostDay = Double.MIN_VALUE;
		for (Map.Entry<String, Double> entry : day2AccountMap.entrySet()) {
			if (maxCostDay < entry.getValue()) {
				maxCostDay = entry.getValue();
				maxCostDayStr = entry.getKey();
			}
		}
		reportData.setMaxCostDay(maxCostDayStr);

		return reportData;
	}
}
