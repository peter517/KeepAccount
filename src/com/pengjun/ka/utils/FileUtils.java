package com.pengjun.ka.utils;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Environment;

public class FileUtils {

	public static boolean isUrl(String path) {
		if (path.startsWith("file://")) {
			return false;
		}

		if (path.contains("://")) {
			return true;
		}
		return false;
	}

	public static void atomicCopyFile(File from, File to) {
		FileInputStream in = null;
		FileOutputStream out = null;
		File tmp = null;
		try {
			tmp = new File(to.getPath() + ".tmp");
			in = new FileInputStream(from);
			out = new FileOutputStream(tmp);
			in.getChannel().transferTo(0, from.length(), out.getChannel());
			out.close();
			if (!tmp.renameTo(to)) {
				throw new IOException("Failed to rename " + tmp + " to " + to);
			}
		} catch (IOException x) {
			close(out);
			deleteFile(to);
		} finally {
			close(in);
			close(out);
			deleteFile(tmp);
		}
	}

	public static void close(Closeable closeable) {
		try {
			if (closeable != null) {
				closeable.close();
			}
		} catch (Throwable x) {
			x.printStackTrace();
		}
	}

	public static String getFileBaseName(String name) {
		int index = name.lastIndexOf('.');
		return index == -1 ? name : name.substring(0, index);
	}

	public static String getFileExtension(String name) {
		int index = name.lastIndexOf('.');
		return index == -1 ? "" : name.substring(index + 1).toLowerCase();
	}

	public static void printCurAppDir(Context context) {
		Context cont = context.getApplicationContext();
		MyDebug.printFromPJ("cont.getCacheDir() = " + cont.getCacheDir());
		MyDebug.printFromPJ("cont.getDatabasePath = " + cont.getDatabasePath("temp"));
		MyDebug.printFromPJ("cont.getFilesDir() = " + cont.getFilesDir());
	}

	public static <T> void writeListToFile(List<T> list, String filename) {
		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(new FileOutputStream(filename));
			out.writeObject(list);
			close(out);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			close(out);
		}

	}

	public static <T> List<T> readListFromFile(String filename) {
		List<T> list = null;
		ObjectInputStream in = null;
		try {
			in = new ObjectInputStream(new FileInputStream(filename));
			list = (List<T>) in.readObject();
		} catch (OptionalDataException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			close(in);
		}
		return list;
	}

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
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
			out.write(content.getBytes());

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(out);
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

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(in);
			close(out);
		}

		return true;
	}

	public static String readFile(String filename) {

		FileInputStream in = null;
		byte[] data = null;
		ByteArrayOutputStream out = null;

		try {
			in = new FileInputStream(filename);
			byte[] buf = new byte[1024];
			int len = 0;
			out = new ByteArrayOutputStream();
			while ((len = in.read(buf)) != -1) {
				out.write(buf, 0, len);
			}
			data = out.toByteArray();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close(in);
			close(out);
		}

		return new String(data);
	}
}
