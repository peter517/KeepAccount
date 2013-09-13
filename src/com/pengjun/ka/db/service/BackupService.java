package com.pengjun.ka.db.service;

import java.io.File;
import java.util.List;

import com.pengjun.ka.db.dao.ArDao;
import com.pengjun.ka.db.dao.ArTypeDao;
import com.pengjun.ka.db.model.AccountRecord;
import com.pengjun.ka.db.model.ArType;
import com.pengjun.ka.utils.KaConstants;
import com.pengjun.utils.FileUtils;

public class BackupService {

	public static void saveBackupByDateStr(String dateStr) {
		List<AccountRecord> arList = ArDao.getSingleInstance().queryAllByUpdate();
		List<ArType> arTypeList = ArTypeDao.getSingleInstance().queryAllByUpdate();
		saveBackupAll(dateStr, arList, arTypeList);
		KaConstants.dbLogger.info("saveBackupAll " + dateStr);
	}

	public static void deleteBackupFromDateStr(String dateStr) {
		String backupPath = KaConstants.BACKUP_ROOT + File.separator + dateStr;
		FileUtils.deleteFile(new File(backupPath));
		KaConstants.dbLogger.info("deleteBackup " + dateStr);
	}

	public static void restoreByDateStr(String dateStr) {

		List<AccountRecord> arList = FileUtils.<AccountRecord> readListFromFile(getBackupPathByDateStr(
				dateStr, AccountRecord.class.getSimpleName()));
		List<ArType> arTypeList = FileUtils.<ArType> readListFromFile(getBackupPathByDateStr(dateStr,
				ArType.class.getSimpleName()));

		restoreAll(dateStr, arList, arTypeList);
		KaConstants.dbLogger.info("restoreAll " + dateStr);

	}

	private static void restoreAll(String curTimeStr, List<AccountRecord> arList, List<ArType> arTypeList) {
		ArDao.getSingleInstance().reCreateTable(arList);
		ArTypeDao.getSingleInstance().reCreateTable(arTypeList);
	}

	private static void saveBackupAll(String curTimeStr, List<AccountRecord> arList, List<ArType> arTypeList) {

		FileUtils.createDirIfNotExist(KaConstants.BACKUP_ROOT + File.separator + curTimeStr);

		FileUtils.<AccountRecord> writeListToFile(arList,
				getBackupPathByDateStr(curTimeStr, AccountRecord.class.getSimpleName()));
		FileUtils.<ArType> writeListToFile(arTypeList,
				getBackupPathByDateStr(curTimeStr, ArType.class.getSimpleName()));

	}

	private static String getBackupPathByDateStr(String dataStr, String className) {
		return KaConstants.BACKUP_ROOT + File.separator + dataStr + File.separator + className
				+ KaConstants.BACKUP_FILE_POSTFIX;
	}

}
