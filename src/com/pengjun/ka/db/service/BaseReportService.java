package com.pengjun.ka.db.service;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import com.pengjun.ka.db.model.AccountRecord;
import com.pengjun.ka.db.model.BaseReport;
import com.pengjun.ka.utils.MyDebug;
import com.pengjun.ka.utils.NumberUtils;
import com.pengjun.ka.utils.TimeUtils;

public class BaseReportService {

	public static String computeMaxCntKeywordStr(List<String> commentStringList) {
		Map<String, Integer> keyword2CntMap = new HashMap<String, Integer>();
		Integer cnt = 0;
		for (String commentStr : commentStringList) {

			TimeUtils.startTiming();
			List<String> segmentList = getSegmentationList(commentStr);
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
		Map<String, Double> day2AccountMap = new HashMap<String, Double>();
		Map<String, Double> date2AccountMap = new HashMap<String, Double>();
		for (AccountRecord ar : arList) {

			totalCost += ar.getAccount();
			if (ar.getAccount() > maxCostAr.getAccount()) {
				maxCostAr = ar;
			}

			if (!ar.getComment().equals("")) {
				commentStringList.add(ar.getComment());
			}

			String month = TimeUtils.String2DateStrArr(ar.getCreateDate())[2];
			account = day2AccountMap.get(month);
			if (account == null) {
				account = 0.0;
			}
			day2AccountMap.put(month, account + ar.getAccount());

			if (date2AccountMap.get(ar.getCreateDate()) == null) {
				date2AccountMap.put(ar.getCreateDate(), 0.0);
			}

		}

		baseReport.setTotalCountNum(arList.size());
		baseReport.setTotalCost(NumberUtils.formatDouble(totalCost));
		baseReport.setAvgCost(NumberUtils.formatDouble(totalCost / arList.size()));
		baseReport.setMaxCost(maxCostAr);
		baseReport.setCostKeyWord(computeMaxCntKeywordStr(commentStringList));
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

	public static void initSegmentationTool() {
		getSegmentationList("");
	}

	public static List<String> getSegmentationList(String sentence) {

		List<String> sgmList = new ArrayList<String>();

		// max word-length segmentation
		IKSegmenter ik = new IKSegmenter(new StringReader(sentence), true);
		Lexeme lexeme = null;
		try {
			while ((lexeme = ik.next()) != null) {
				sgmList.add(lexeme.getLexemeText());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sgmList;
	}
}
