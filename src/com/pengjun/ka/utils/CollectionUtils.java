package com.pengjun.ka.utils;

import java.util.List;

public class CollectionUtils {

	public static int getPosFromList(List<String> strList, String str) {

		for (int i = 0; i < strList.size(); i++) {
			if (str.equals(strList.get(i))) {
				return i;
			}
		}
		return 0;
	}

}
