package com.pengjun.ka.test;

import com.pengjun.ka.db.model.AccountRecord;
import com.pengjun.ka.db.model.ArType;
import com.pengjun.ka.utils.KaConstants.InitArType;
import com.pengjun.ka.utils.NumberUtils;
import com.pengjun.ka.utils.TimeUtils;

public class DataCreater {

	public static AccountRecord getRandomAr() {
		AccountRecord ar = new AccountRecord();
		ar.setAccount(NumberUtils.formatFloat(TimeUtils.getRandomFloat() * 10000));
		ar.setTypeName(InitArType.values()[TimeUtils.getRandomInt(5)].getTypeName());
		ar.setCreateDate(TimeUtils.getRandomDateStr());
		ar.setUpdateTime(TimeUtils.getRandomTimeStr());
		ar.setComment("");
		return ar;
	}

	public static ArType getRandomArType() {
		ArType arArType = new ArType();
		arArType.setImgResName("type_car");
		arArType.setTypeName(InitArType.values()[TimeUtils.getRandomInt(5)].getTypeName());
		arArType.setCreateDate(TimeUtils.getRandomDateStr());
		arArType.setUpdateTime(TimeUtils.getRandomTimeStr());
		return arArType;
	}
}
