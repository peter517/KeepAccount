package com.pengjun.ka.db.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pengjun.ka.db.model.AccountRecord;
import com.pengjun.ka.db.model.MagicBoxData;
import com.pengjun.ka.utils.MyDebug;
import com.pengjun.ka.utils.NumberUtils;
import com.pengjun.ka.utils.StringUtils;
import com.pengjun.ka.utils.TimeUtils;

public class MagicBoxService {

	public static MagicBoxData computeMagicBoxData(List<AccountRecord> arList) {

		double totalCost = 0;

		Map<String, Double> yearMonth2AccountMap = new HashMap<String, Double>();
		Map<String, Double> month2AccountMap = new HashMap<String, Double>();

		AccountRecord maxCostAr = new AccountRecord();
		maxCostAr.setAccount(Float.MIN_VALUE);
		int monthStart = 0;
		int monthMiddle = 0;
		int monthEnd = 0;
		Double account = null;

		List<String> commentStringList = new ArrayList<String>();
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
			account = month2AccountMap.get(month);
			if (account == null) {
				account = 0.0;
			}
			month2AccountMap.put(month, account + ar.getAccount());

			String yearMonth = TimeUtils.String2YearMonthDateStr(ar.getCreateDate());
			account = yearMonth2AccountMap.get(yearMonth);
			if (account == null) {
				account = 0.0;
			}
			yearMonth2AccountMap.put(yearMonth, account + ar.getAccount());

			if (!ar.getComment().equals("")) {
				commentStringList.add(ar.getComment());
			}

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
		magicBoxData.setTotalCost(NumberUtils.formatDouble(totalCost));
		magicBoxData.setAvgCost(NumberUtils.formatDouble(totalCost / arList.size()));
		magicBoxData.setAvgCostMonth(NumberUtils.formatDouble(totalCost / yearMonth2AccountMap.size()));
		magicBoxData.setMaxCost(maxCostAr);

		// maxCostMonth
		String maxCostMonthStr = null;
		Double maxCostMonth = Double.MIN_VALUE;
		for (Map.Entry<String, Double> entry : month2AccountMap.entrySet()) {
			if (maxCostMonth < entry.getValue()) {
				maxCostMonth = entry.getValue();
				maxCostMonthStr = entry.getKey();
			}
		}
		magicBoxData.setMaxCostMonth(String.valueOf(Integer.valueOf(maxCostMonthStr)) + "月");

		// maxCntKeyword
		Map<String, Integer> keyword2CntMap = new HashMap<String, Integer>();
		Integer cnt = 0;
		for (String commentStr : commentStringList) {

			TimeUtils.startTiming();
			List<String> segmentList = StringUtils.getSegmentationList(commentStr);
			MyDebug.printFromPJ("time " + TimeUtils.stopTiming());
			for (String segmentStr : segmentList) {
				cnt = keyword2CntMap.get(segmentStr);
				if (cnt == null) {
					cnt = 0;
				}
				keyword2CntMap.put(segmentStr, ++cnt);
			}
		}

		String maxCntKeywordStr = null;
		Integer maxCntKeyword = Integer.MIN_VALUE;
		for (Map.Entry<String, Integer> entry : keyword2CntMap.entrySet()) {

			if (entry.getValue() > maxCntKeyword) {
				maxCntKeyword = entry.getValue();
				maxCntKeywordStr = entry.getKey();
			}
		}

		magicBoxData.setCostKeyWord(maxCntKeywordStr);

		return magicBoxData;
	}
}
