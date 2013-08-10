package com.pengjun.ka.db.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.table.TableUtils;
import com.pengjun.ka.activity.KaApplication;
import com.pengjun.ka.db.model.AccountRecord;
import com.pengjun.ka.db.model.ArType;
import com.pengjun.ka.utils.Constants;
import com.pengjun.ka.utils.Utils;

public class ArTypeService {

	private static AndroidConnectionSource cs = KaApplication.getAndroidConnectionSource();

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
			arType.setUpdateTime(Utils.getCurTimeStr());
			arType.setCreateDate(Utils.getCurDateStr());
			arType.setImgResName(Constants.TYPE_IMAGE_RES_ID_ARR[i]);
			try {
				dao.create(arType);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	public static List<ArType> queryAllByUpdate() {
		try {
			QueryBuilder<ArType, Integer> queryBuilder = dao.queryBuilder();
			queryBuilder.orderBy(AccountRecord.COL_UPDATE_TIME, false);
			return queryBuilder.query();
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

	public static String getArTpyeNameById(int id) {
		try {
			ArType arType = dao.queryForId(id);
			return arType.getTypeName();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Constants.DB_SEARCH_STRING_NOT_FOUND;
	}

	public static String getImgResNameById(int id) {
		try {
			ArType arType = dao.queryForId(id);
			return arType.getImgResName();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Constants.DB_SEARCH_STRING_NOT_FOUND;
	}

	public static int getIdByArTpye(String arTpye) {
		try {
			List<ArType> arTypeList = dao.queryForEq(ArType.COL_TYPE_NAME, arTpye);
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
			List<ArType> arTypeList = dao.queryForEq(ArType.COL_TYPE_NAME, arTpye);
			if (arTypeList.size() == 1) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void insert(ArType arType) {
		try {
			dao.create(arType);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static boolean isTypeNameExsit(String typeName) {
		List<ArType> arTypeList = queryArTypeByTypeName(typeName);
		if (arTypeList == null || arTypeList.size() == 0) {
			return false;
		}
		return true;
	}

	public static List<ArType> queryArTypeByTypeName(String typeName) {
		try {
			return dao.queryForEq(ArType.COL_TYPE_NAME, typeName);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void update(ArType arType) {
		try {
			dao.update(arType);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void delete(ArType arType) {
		try {
			dao.delete(arType);
			ArService.deleteByTypeId(arType.getId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void reCreateTable(List<ArType> arTypeList) {

		try {
			TableUtils.dropTable(cs, ArType.class, false);
			TableUtils.createTable(cs, ArType.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		for (int i = arTypeList.size() - 1; i >= 0; i--) {
			insert(arTypeList.get(i));
		}
	}

}
