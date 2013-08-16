package com.pengjun.ka.test;

import com.pengjun.ka.db.model.AccountRecord;
import com.pengjun.ka.utils.Constants.InitArType;
import com.pengjun.ka.utils.TimeUtils;

public class DataCreater {

	public static AccountRecord getRandomAr() {
		AccountRecord ar = new AccountRecord();
		ar.setAccount(TimeUtils.getRandomFloat() * 10000);
		ar.setTypeName(InitArType.values()[TimeUtils.getRandomInt(5)].getTypeName());
		ar.setCreateDate(TimeUtils.getRandomDateStr());
		ar.setUpdateTime(TimeUtils.getRandomTimeStr());
		return ar;
	}
}
