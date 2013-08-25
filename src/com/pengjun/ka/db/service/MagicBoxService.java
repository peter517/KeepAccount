package com.pengjun.ka.db.service;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.pengjun.ka.db.model.AccountRecord;
import com.pengjun.ka.db.model.MagicBoxData;
import com.pengjun.ka.utils.MathUtils;
import com.pengjun.ka.utils.TimeUtils;

public class MagicBoxService {
	public static MagicBoxData getTotalCountNum(List<AccountRecord> arList) {

		double totalCost = 0;
		Double count = null;
		Map<String, Double> yearMonthMap = new TreeMap<String, Double>();
		Map<String, Double> monthMap = new TreeMap<String, Double>();

		AccountRecord maxCostAr = new AccountRecord();
		maxCostAr.setAccount(Float.MIN_VALUE);
		int monthStart = 0;
		int monthMiddle = 0;
		int monthEnd = 0;
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

			String month = TimeUtils.String2DateStrArr(ar.getCreateDate())[1];
			count = monthMap.get(month);
			if (count == null) {
				count = 0.0;
			}
			monthMap.put(month, count + ar.getAccount());

			String yearMonth = TimeUtils.getYearMouthFromDateStr(ar.getCreateDate());
			count = yearMonthMap.get(yearMonth);
			if (count == null) {
				count = 0.0;
			}
			yearMonthMap.put(yearMonth, count + ar.getAccount());
		}

		MagicBoxData magicBoxData = new MagicBoxData();
		int max = Math.max(monthStart, Math.max(monthMiddle, monthEnd));
		if (max == monthStart) {
			magicBoxData.setMaxCostInterval("上旬");
		} else if (max == monthMiddle) {
			magicBoxData.setMaxCostInterval("中旬");
		} else if (max == monthEnd) {
			magicBoxData.setMaxCostInterval("下旬");
		}

		magicBoxData.setTotalCountNum(arList.size());
		magicBoxData.setTotalCost(MathUtils.formatDouble(totalCost));
		magicBoxData.setAvgCost(MathUtils.formatDouble(totalCost / arList.size()));
		magicBoxData.setAvgCostMonth(MathUtils.formatDouble(totalCost / yearMonthMap.size()));
		magicBoxData.setMaxCost(maxCostAr);

		String maxCostMonthStr = null;
		Double maxCostMonth = Double.MIN_VALUE;
		for (Map.Entry<String, Double> entry : monthMap.entrySet()) {
			if (maxCostMonth < entry.getValue()) {
				maxCostMonth = entry.getValue();
				maxCostMonthStr = entry.getKey();
			}
		}
		magicBoxData.setMaxCostMonth(String.valueOf(Integer.valueOf(maxCostMonthStr)) + "月");

		return magicBoxData;
	}
}
