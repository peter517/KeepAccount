package com.pengjun.ka.db.service;

import java.util.List;

import com.pengjun.ka.db.model.AccountRecord;
import com.pengjun.ka.db.model.MagicBoxData;
import com.pengjun.ka.utils.CollectionUtils;
import com.pengjun.ka.utils.NumberUtils;
import com.pengjun.ka.utils.TimeUtils;

public class MagicBoxService extends BaseReportService {

	public static MagicBoxData computeMagicBoxData(List<AccountRecord> arList) {

		MagicBoxData magicBoxData = new MagicBoxData();
		creatBaseReport(magicBoxData, arList);

		CollectionUtils.CountDoubleMap yearMonth2AccountMap = new CollectionUtils.CountDoubleMap();
		CollectionUtils.CountDoubleMap month2AccountMap = new CollectionUtils.CountDoubleMap();

		Double account = null;
		double totalCost = 0;
		for (AccountRecord ar : arList) {

			account = (double) ar.getAccount();
			totalCost += account;

			String month = TimeUtils.String2DateStrArr(ar.getCreateDate())[1];
			month2AccountMap.count(month, account);

			String yearMonth = TimeUtils.String2MonthYearStr(ar.getCreateDate());
			yearMonth2AccountMap.count(yearMonth, account);

		}

		magicBoxData.setAvgCostMonth(NumberUtils.formatDouble(totalCost / yearMonth2AccountMap.size()));
		magicBoxData.setMaxCostInterval(computeMaxCostMonthStr(month2AccountMap));

		return magicBoxData;
	}

}
