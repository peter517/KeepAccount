package com.pengjun.ka.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.pengjun.keepaccounts.R;

public class ResManageUtils {

	// res
	public static final String RES_IMAGE_PREFIX = "type";

	public static Map<String, Integer> resName2Id = new HashMap<String, Integer>();

	static {
		Field[] fields = R.drawable.class.getDeclaredFields();
		for (Field field : fields) {
			// get all image from res which name start with type
			if (field.getName().startsWith(RES_IMAGE_PREFIX)) {
				try {
					resName2Id.put(field.getName(),
							field.getInt(R.drawable.class));
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
}
