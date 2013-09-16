package com.pengjun.ka.db.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.pengjun.ka.android.activity.KaApplication;
import com.pengjun.ka.db.model.AccountRecord;
import com.pengjun.ka.db.model.BaseReport;
import com.pengjun.utils.CollectionUtils;
import com.pengjun.utils.IKAnalyzerUtils;
import com.pengjun.utils.NumberUtils;
import com.pengjun.utils.TimeUtils;

public class BaseReportService {

	public static String computeMaxCntKeywordStr(List<String> commentStringList) {

		CollectionUtils.CountIntegerMap keyword2CntMap = new CollectionUtils.CountIntegerMap();
		for (String commentStr : commentStringList) {
			List<String> segmentList = IKAnalyzerUtils.getSegmentationList(KaApplication.instance(),
					commentStr);
			for (String segmentStr : segmentList) {
				keyword2CntMap.count(segmentStr, 1);
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

		return maxCntKeywordStr;
	}

	public static String computeMaxCostMonthStr(Map<String, Double> month2AccountMap) {
		String maxCostMonthStr = null;
		Double maxCostMonth = Double.MIN_VALUE;
		for (Map.Entry<String, Double> entry : month2AccountMap.entrySet()) {
			if (maxCostMonth < entry.getValue()) {
				maxCostMonth = entry.getValue();
				maxCostMonthStr = entry.getKey();
			}
		}

		return String.valueOf(Integer.valueOf(maxCostMonthStr)) + "月";
	}

	public static void creatBaseReport(BaseReport baseReport, List<AccountRecord> arList) {

		AccountRecord maxCostAr = new AccountRecord();
		maxCostAr.setAccount(Float.MIN_VALUE);
		double totalCost = 0;
		List<String> commentStringList = new ArrayList<String>();
		Double account = null;
		CollectionUtils.CountDoubleMap day2AccountMap = new CollectionUtils.CountDoubleMap();
		CollectionUtils.CountDoubleMap date2AccountMap = new CollectionUtils.CountDoubleMap();

		for (AccountRecord ar : arList) {

			account = (double) ar.getAccount();
			totalCost += account;

			if (account > maxCostAr.getAccount()) {
				maxCostAr = ar;
			}

			if (!ar.getComment().equals("")) {
				commentStringList.add(ar.getComment());
			}

			String month = TimeUtils.String2DateStrArr(ar.getCreateDate())[2];
			day2AccountMap.count(month, account);
			date2AccountMap.count(ar.getCreateDate(), 0.0);

		}

		baseReport.setTotalCountNum(arList.size());
		baseReport.setTotalCost(NumberUtils.formatDouble(totalCost));
		baseReport.setAvgCost(NumberUtils.formatDouble(totalCost / arList.size()));
		baseReport.setMaxCost(maxCostAr);
		// need too many memory , stop it
		// baseReport.setCostKeyWord(computeMaxCntKeywordStr(commentStringList));
		baseReport.setAvgCostPerDay(NumberUtils.formatDouble(totalCost / date2AccountMap.size()));

		String maxCostDayStr = null;
		Double maxCostDay = Double.MIN_VALUE;
		for (Map.Entry<String, Double> entry : day2AccountMap.entrySet()) {
			if (maxCostDay < entry.getValue()) {
				maxCostDay = entry.getValue();
				maxCostDayStr = entry.getKey();
			}
		}
		baseReport.setMaxCostDay(maxCostDayStr);

	}
}
