package com.pengjun.ka.db.service;

import java.util.List;

import com.pengjun.ka.db.model.AccountRecord;
import com.pengjun.ka.db.model.WeekOrMonthReport;
import com.pengjun.utils.TimeUtils;

public class MonthReportService extends BaseReportService {

	public static WeekOrMonthReport computeReportData(List<AccountRecord> arList) {

		WeekOrMonthReport monthReport = new WeekOrMonthReport();

		creatBaseReport(monthReport, arList);

		int monthStart = 0;
		int monthMiddle = 0;
		int monthEnd = 0;
		for (AccountRecord ar : arList) {

			int day = Integer.valueOf(TimeUtils.String2DateStrArr(ar.getCreateDate())[2]);
			if (day > 0 && day <= 10) {
				monthStart++;
			} else if (day <= 20 && day > 10) {
				monthMiddle++;
			} else if (day <= 31 && day > 20) {
				monthEnd++;
			}

		}

		int max = Math.max(monthStart, Math.max(monthMiddle, monthEnd));
		if (max == monthStart) {
			monthReport.setMaxCostInterval("上旬");
		} else if (max == monthMiddle) {
			monthReport.setMaxCostInterval("中旬");
		} else if (max == monthEnd) {
			monthReport.setMaxCostInterval("下旬");
		}

		return monthReport;
	}
}
