package com.pengjun.ka.db.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import com.pengjun.ka.db.model.AccountRecord;
import com.pengjun.ka.db.model.ArType;
import com.pengjun.ka.utils.Constants;
import com.pengjun.ka.utils.FileUtils;

public class BackupService {

	public static void backup(String dateStr) {
		List<AccountRecord> arList = ArService.queryAllByUpdate();
		List<ArType> arTypeList = ArTypeService.queryAllByUpdate();
		backupAll(dateStr, arList, arTypeList);

	}

	public static void deleteBackup(String dateStr) {
		String backupPath = Constants.BACK_UP_ROOT + File.separator + dateStr;
		FileUtils.deleteFile(new File(backupPath));
	}

	public static void restore(String dateStr) {

		String arFilePath = Constants.BACK_UP_ROOT + File.separator + dateStr + File.separator
				+ Constants.BACK_AR_FILE_NAME;

		List<AccountRecord> arList = null;
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(arFilePath));
			arList = (List<AccountRecord>) in.readObject();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		String arTypeFilePath = Constants.BACK_UP_ROOT + File.separator + dateStr + File.separator
				+ Constants.BACK_AR_TYPE_FILE_NAME;
		List<ArType> arTypeList = null;
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(arTypeFilePath));
			arTypeList = (List<ArType>) in.readObject();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		restoreAll(dateStr, arList, arTypeList);

	}

	private static void restoreAll(String curTimeStr, List<AccountRecord> arList, List<ArType> arTypeList) {
		ArService.reCreateTable(arList);
		ArTypeService.reCreateTable(arTypeList);
	}

	private static void backupAll(String curTimeStr, List<AccountRecord> arList, List<ArType> arTypeList) {
		try {

			String backDir = Constants.BACK_UP_ROOT + File.separator + curTimeStr;
			FileUtils.createDir(backDir);

			String arFilePath = backDir + File.separator + Constants.BACK_AR_FILE_NAME;
			String arTypeFilePath = backDir + File.separator + Constants.BACK_AR_TYPE_FILE_NAME;

			ObjectOutputStream arOut = new ObjectOutputStream(new FileOutputStream(arFilePath));
			arOut.writeObject(arList);
			arOut.close();

			ObjectOutputStream arTypeOut = new ObjectOutputStream(new FileOutputStream(arTypeFilePath));
			arTypeOut.writeObject(arTypeList);
			arTypeOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
