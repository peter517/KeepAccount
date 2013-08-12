package com.pengjun.ka.test;

import java.util.Random;

import com.pengjun.ka.db.model.AccountRecord;
import com.pengjun.ka.utils.Constants;
import com.pengjun.ka.utils.Utils;

public class DataCreater {

	private static Random random = new Random();
	static {
		random.setSeed(System.currentTimeMillis());
	}

	public static AccountRecord getRandomAr() {
		AccountRecord ar = new AccountRecord();
		ar.setAccount(random.nextFloat() * 10000);
		ar.setTypeName(Constants.TYPE_STR_ARR[random.nextInt(5)]);
		ar.setCreateDate(Utils.getRandomDateStr(2000 + random.nextInt(13), 1 + random.nextInt(12),
				1 + random.nextInt(30)));
		ar.setUpdateTime(Utils.getRandomTimeStr(2000 + random.nextInt(13), 1 + random.nextInt(12),
				1 + random.nextInt(30), 1 + random.nextInt(24), 1 + random.nextInt(60),
				1 + random.nextInt(60)));
		return ar;
	}
}
