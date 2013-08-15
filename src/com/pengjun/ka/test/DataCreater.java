package com.pengjun.ka.test;

import com.pengjun.ka.db.model.AccountRecord;
import com.pengjun.ka.utils.Constants;
import com.pengjun.ka.utils.TimeUtils;

public class DataCreater {

	public static AccountRecord getRandomAr() {
		AccountRecord ar = new AccountRecord();
		ar.setAccount(TimeUtils.getRandomFloat() * 10000);
		ar.setTypeName(Constants.TYPE_STR_ARR[TimeUtils.getRandomInt(5)]);
		ar.setCreateDate(TimeUtils.getRandomDateStr());
		ar.setUpdateTime(TimeUtils.getRandomTimeStr());
		return ar;
	}
}
