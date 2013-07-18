package com.pengjun.ka.db.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.pengjun.ka.activity.KAApplication;
import com.pengjun.ka.db.model.ArType;
import com.pengjun.ka.tools.Constants;
import com.pengjun.ka.tools.Util;

public class ArTypeService {

	private static AndroidConnectionSource cs = KAApplication
			.getAndroidConnectionSource();

	private static Dao<ArType, Integer> dao = null;

	static {
		try {
			dao = DaoManager.createDao(cs, ArType.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void initTable() {

		for (int i = 0; i < Constants.TYPE_STR_ARR.length; i++) {
			ArType arType = new ArType();
			arType.setTypeName(Constants.TYPE_STR_ARR[i]);
			arType.setUpdateTime(Util.getCurTimeStr());
			arType.setImgResId(Constants.TYPE_IMAGE_RES_ID_ARR[i]);
			try {
				dao.create(arType);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	public static List<ArType> queryAll() {
		try {
			return dao.queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Constants.DB_SEARCH_LIST_NOT_FOUND;
	}

	public static List<String> queryAllArTypeName() {
		try {
			List<ArType> arTypeList = dao.queryForAll();
			List<String> typeNameList = new ArrayList<String>();

			for (ArType arType : arTypeList) {
				typeNameList.add(arType.getTypeName());
			}

			return typeNameList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Constants.DB_SEARCH_LIST_NOT_FOUND;
	}

	public static String getArTpyeById(int id) {
		try {
			ArType arType = dao.queryForId(id);
			return arType.getTypeName();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Constants.DB_SEARCH_STRING_NOT_FOUND;
	}

	public static int getIdByArTpye(String arTpye) {
		try {
			List<ArType> arTypeList = dao.queryForEq(ArType.COL_TYPE_NAME,
					arTpye);
			if (arTypeList.size() == 1) {
				return arTypeList.get(0).getId();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Constants.DB_SEARCH_INT_NOT_FOUND;
	}

	public static boolean ifArTypeExist(String arTpye) {
		try {
			List<ArType> arTypeList = dao.queryForEq(ArType.COL_TYPE_NAME,
					arTpye);
			if (arTypeList.size() == 1) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static int getImgResIdById(int id) {
		try {
			ArType arType = dao.queryForId(id);
			return arType.getImgResId();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Constants.DB_SEARCH_INT_NOT_FOUND;
	}

	public static void insert(ArType arType) {
		try {
			dao.create(arType);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
