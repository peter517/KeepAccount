package com.pengjun.ka.utils;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.StatFs;

import com.pengjun.ka.R;

public class ResourceUtils {

	public static int[] colors = new int[] { Color.BLUE, Color.GREEN, Color.MAGENTA, Color.CYAN, Color.BLACK,
			Color.DKGRAY, Color.GRAY, Color.LTGRAY, Color.RED, Color.WHITE };
	// res
	public static final String RES_IMAGE_PREFIX = "type";

	public static Map<String, Integer> resName2Id = new HashMap<String, Integer>();

	static {
		Field[] fields = R.drawable.class.getDeclaredFields();
		for (Field field : fields) {
			// get all image from res which name start with type
			if (field.getName().startsWith(RES_IMAGE_PREFIX)) {
				try {
					resName2Id.put(field.getName(), field.getInt(R.drawable.class));
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static int getImgResIdByName(String imgResName) {
		return resName2Id.get(imgResName);
	}

	public static boolean CheckNetwork(Context context, boolean isNotify) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();

		if (activeNetInfo != null) {
			int netType = activeNetInfo.getType();
			switch (netType) {
			case 0:
			case 1:
			case 9:
			case 6:
			case 7:
				return true;
			default:
				return false;
			}
		}
		return false;
	}

	public static boolean checkStorageSpace(Context context) {
		StatFs stat = new StatFs(Environment.getDataDirectory().getAbsolutePath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();

		if (blockSize * availableBlocks / 1024 / 1024 >= 128) {
			return true;
		}

		return false;
	}

	public static String getLocalIpAddress() {
		String localIPs = "";
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en
					.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr
						.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						localIPs = inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
		}
		return localIPs;
	}

	public static boolean hasExternalStorage() {
		String state = android.os.Environment.getExternalStorageState();
		if (android.os.Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}
}
