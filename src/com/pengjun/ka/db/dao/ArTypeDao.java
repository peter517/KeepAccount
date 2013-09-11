package com.pengjun.ka.db.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.table.TableUtils;
import com.pengjun.db.BaseDao;
import com.pengjun.ka.android.activity.KaApplication;
import com.pengjun.ka.db.model.ArType;
import com.pengjun.ka.utils.KaConstants.InitArType;
import com.pengjun.utils.TimeUtils;

public class ArTypeDao extends BaseDao<ArType> {

	private ArTypeDao(AndroidConnectionSource cs, Class<ArType> modelClass) {
		super(cs, modelClass);
	}

	private static ArTypeDao arTypeDao = null;

	public static ArTypeDao getSingleInstance() {
		if (arTypeDao == null) {
			arTypeDao = new ArTypeDao(KaApplication.getAndroidConnectionSource(), ArType.class);
		}
		return arTypeDao;
	}

	public void initTable() {

		for (int i = 0; i < InitArType.values().length; i++) {
			ArType arType = new ArType();
			arType.setTypeName(InitArType.values()[i].getTypeName());
			arType.setUpdateTime(TimeUtils.getCurTimeStr());
			arType.setCreateDate(TimeUtils.getCurDateStr());
			arType.setImgResName(InitArType.values()[i].toString());
			try {
				dao.create(arType);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	public List<ArType> queryAllByUpdate() {
		try {
			QueryBuilder<ArType, Integer> queryBuilder = dao.queryBuilder();
			queryBuilder.orderBy(ArType.COL_UPDATE_TIME, false);
			return queryBuilder.query();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return BaseDao.DB_SEARCH_LIST_NOT_FOUND;
	}

	public List<String> queryAllArTypeName() {
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
		return BaseDao.DB_SEARCH_LIST_NOT_FOUND;
	}

	public String getArTpyeNameById(int id) {
		try {
			ArType arType = dao.queryForId(id);
			return arType.getTypeName();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return BaseDao.DB_SEARCH_STRING_NOT_FOUND;
	}

	public String getImgResNameById(int id) {
		try {
			ArType arType = dao.queryForId(id);
			return arType.getImgResName();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return BaseDao.DB_SEARCH_STRING_NOT_FOUND;
	}

	public int getIdByArTpye(String arTpye) {
		try {
			List<ArType> arTypeList = dao.queryForEq(ArType.COL_TYPE_NAME, arTpye);
			if (arTypeList.size() == 1) {
				return arTypeList.get(0).getId();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return BaseDao.DB_SEARCH_INT_NOT_FOUND;
	}

	public boolean ifArTypeExist(String arTpye) {
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

	public boolean isTypeNameExsit(String typeName) {
		List<ArType> arTypeList = queryArTypeByTypeName(typeName);
		if (arTypeList == null || arTypeList.size() == 0) {
			return false;
		}
		return true;
	}

	public List<ArType> queryArTypeByTypeName(String typeName) {
		try {
			return dao.queryForEq(ArType.COL_TYPE_NAME, typeName);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void reCreateTable(List<ArType> arTypeList) {

		try {
			TableUtils.dropTable(cs, ArType.class, false);
			TableUtils.createTable(cs, ArType.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		for (int i = arTypeList.size() - 1; i >= 0; i--) {
			arTypeList.get(i).setId(-1);
			insert(arTypeList.get(i));
		}
	}

}
