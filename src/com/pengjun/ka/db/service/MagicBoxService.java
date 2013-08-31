package com.pengjun.ka.db.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pengjun.ka.db.model.AccountRecord;
import com.pengjun.ka.db.model.MagicBoxData;
import com.pengjun.ka.utils.NumberUtils;
import com.pengjun.ka.utils.TimeUtils;

public class MagicBoxService extends BaseReportService {

	public static MagicBoxData computeMagicBoxData(List<AccountRecord> arList) {

		MagicBoxData magicBoxData = new MagicBoxData();
		creatBaseReport(magicBoxData, arList);

		Map<String, Double> yearMonth2AccountMap = new HashMap<String, Double>();
		Map<String, Double> month2AccountMap = new HashMap<String, Double>();

		Double account = null;
		double totalCost = 0;
		for (AccountRecord ar : arList) {

			totalCost += ar.getAccount();

			String month = TimeUtils.String2DateStrArr(ar.getCreateDate())[1];
			account = month2AccountMap.get(month);
			if (account == null) {
				account = 0.0;
			}
			month2AccountMap.put(month, account + ar.getAccount());

			String yearMonth = TimeUtils.String2MonthYearStr(ar.getCreateDate());
			account = yearMonth2AccountMap.get(yearMonth);
			if (account == null) {
				account = 0.0;
			}
			yearMonth2AccountMap.put(yearMonth, account + ar.getAccount());

		}

		magicBoxData.setAvgCostMonth(NumberUtils.formatDouble(totalCost / yearMonth2AccountMap.size()));
		magicBoxData.setMaxCostInterval(computeMaxCostMonthStr(month2AccountMap));

		return magicBoxData;
	}

}
