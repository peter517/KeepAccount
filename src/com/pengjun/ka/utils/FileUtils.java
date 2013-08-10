package com.pengjun.ka.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.os.Environment;

public class FileUtils {

	public static boolean createDir(String dir) {
		File file = new File(dir);
		if (!file.exists() && file.mkdir() == false) {
			return false;
		}
		return true;
	}

	public static List<String> getFileNameList(File dir) {

		File[] files = dir.listFiles();
		List<String> fileNameList = new ArrayList<String>();
		for (int i = 0; i < files.length; i++) {
			fileNameList.add(files[i].getName());
		}

		return fileNameList;
	}

	public static boolean saveToSDCard(String filename, String content) {

		String status = Environment.getExternalStorageState();
		if (!status.equals(Environment.MEDIA_MOUNTED)) {
			return false;
		}

		File file = new File(Environment.getExternalStorageDirectory(), filename);
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(file);
			fos.write(content.getBytes());
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;

	}

	public static void deleteFile(File file) {

		if (file.isFile()) {
			file.delete();
			return;
		}

		if (file.isDirectory()) {
			File[] childFiles = file.listFiles();
			if (childFiles == null || childFiles.length == 0) {
				file.delete();
				return;
			}

			for (int i = 0; i < childFiles.length; i++) {
				deleteFile(childFiles[i]);
			}
			file.delete();
		}
	}

	public static boolean copyFile(String srcPath, String dstPath) {

		FileInputStream in = null;
		FileOutputStream out = null;
		try {
			in = new FileInputStream(new File(srcPath));
			out = new FileOutputStream(new File(dstPath));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		byte[] buf = new byte[1024];
		int len;
		try {
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	public static String readFile(String filename) throws Throwable {

		FileInputStream fis = new FileInputStream(filename);

		byte[] buf = new byte[1024];
		int len = 0;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		while ((len = fis.read(buf)) != -1) {
			baos.write(buf, 0, len);
		}
		byte[] data = baos.toByteArray();
		baos.close();
		fis.close();

		return new String(data);
	}

}
