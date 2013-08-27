package com.pengjun.ka.db.service;

import java.io.File;
import java.util.List;

import com.pengjun.ka.db.dao.ArDao;
import com.pengjun.ka.db.dao.ArTypeDao;
import com.pengjun.ka.db.model.AccountRecord;
import com.pengjun.ka.db.model.ArType;
import com.pengjun.ka.utils.Constants;
import com.pengjun.ka.utils.FileUtils;

public class BackupService {

	public static void saveBackupByDateStr(String dateStr) {
		List<AccountRecord> arList = ArDao.queryAllByUpdate();
		List<ArType> arTypeList = ArTypeDao.queryAllByUpdate();
		saveBackupAll(dateStr, arList, arTypeList);

	}

	public static void deleteBackupFromDateStr(String dateStr) {
		String backupPath = Constants.BACK_UP_ROOT + File.separator + dateStr;
		FileUtils.deleteFile(new File(backupPath));
	}

	public static void restoreByDateStr(String dateStr) {

		String arFilePath = Constants.BACK_UP_ROOT + File.separator + dateStr + File.separator
				+ Constants.BACK_AR_FILE_NAME;
		String arTypeFilePath = Constants.BACK_UP_ROOT + File.separator + dateStr + File.separator
				+ Constants.BACK_AR_TYPE_FILE_NAME;

		List<AccountRecord> arList = FileUtils.<AccountRecord> readListFromFile(arFilePath);
		List<ArType> arTypeList = FileUtils.<ArType> readListFromFile(arTypeFilePath);

		restoreAll(dateStr, arList, arTypeList);

	}

	private static void restoreAll(String curTimeStr, List<AccountRecord> arList, List<ArType> arTypeList) {
		ArDao.reCreateTable(arList);
		ArTypeDao.reCreateTable(arTypeList);
	}

	private static void saveBackupAll(String curTimeStr, List<AccountRecord> arList, List<ArType> arTypeList) {

		String backDir = Constants.BACK_UP_ROOT + File.separator + curTimeStr;
		FileUtils.createDir(backDir);

		String arFilePath = backDir + File.separator + Constants.BACK_AR_FILE_NAME;
		String arTypeFilePath = backDir + File.separator + Constants.BACK_AR_TYPE_FILE_NAME;

		FileUtils.<AccountRecord> writeListToFile(arList, arFilePath);
		FileUtils.<ArType> writeListToFile(arTypeList, arTypeFilePath);

	}

}
