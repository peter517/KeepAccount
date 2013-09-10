package com.pengjun.ka.db.service;

import java.io.File;
import java.util.List;

import com.pengjun.ka.db.dao.ArDao;
import com.pengjun.ka.db.dao.ArTypeDao;
import com.pengjun.ka.db.model.AccountRecord;
import com.pengjun.ka.db.model.ArType;
import com.pengjun.ka.utils.FileUtils;
import com.pengjun.ka.utils.KaConstants;
import com.pengjun.ka.utils.AndroidLoggerUtils;

public class BackupService {

	public static void saveBackupByDateStr(String dateStr) {
		List<AccountRecord> arList = ArDao.queryAllByUpdate();
		List<ArType> arTypeList = ArTypeDao.queryAllByUpdate();
		saveBackupAll(dateStr, arList, arTypeList);
		AndroidLoggerUtils.dbLogger.info("saveBackupAll " + dateStr);
	}

	public static void deleteBackupFromDateStr(String dateStr) {
		String backupPath = KaConstants.BACK_UP_ROOT + File.separator + dateStr;
		FileUtils.deleteFile(new File(backupPath));
		AndroidLoggerUtils.dbLogger.info("deleteBackup " + dateStr);
	}

	public static void restoreByDateStr(String dateStr) {

		String arFilePath = KaConstants.BACK_UP_ROOT + File.separator + dateStr + File.separator
				+ KaConstants.BACK_AR_FILE_NAME;
		String arTypeFilePath = KaConstants.BACK_UP_ROOT + File.separator + dateStr + File.separator
				+ KaConstants.BACK_AR_TYPE_FILE_NAME;

		List<AccountRecord> arList = FileUtils.<AccountRecord> readListFromFile(arFilePath);
		List<ArType> arTypeList = FileUtils.<ArType> readListFromFile(arTypeFilePath);

		restoreAll(dateStr, arList, arTypeList);
		AndroidLoggerUtils.dbLogger.info("restoreAll " + dateStr);

	}

	private static void restoreAll(String curTimeStr, List<AccountRecord> arList, List<ArType> arTypeList) {
		ArDao.reCreateTable(arList);
		ArTypeDao.reCreateTable(arTypeList);
	}

	private static void saveBackupAll(String curTimeStr, List<AccountRecord> arList, List<ArType> arTypeList) {

		String backDir = KaConstants.BACK_UP_ROOT + File.separator + curTimeStr;
		FileUtils.createDir(backDir);

		String arFilePath = backDir + File.separator + KaConstants.BACK_AR_FILE_NAME;
		String arTypeFilePath = backDir + File.separator + KaConstants.BACK_AR_TYPE_FILE_NAME;

		FileUtils.<AccountRecord> writeListToFile(arList, arFilePath);
		FileUtils.<ArType> writeListToFile(arTypeList, arTypeFilePath);

	}

}
