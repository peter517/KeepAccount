package com.pengjun.ka.db.service;

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.pengjun.ka.activity.KAApplication;
import com.pengjun.ka.db.model.AccountRecord;
import com.pengjun.ka.tools.Constants;

public class AccountRecordService {

	private static AndroidConnectionSource cs = KAApplication
			.getAndroidConnectionSource();

	private static Dao<AccountRecord, Integer> dao = null;

	static {
		try {
			dao = DaoManager.createDao(cs, AccountRecord.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void insert(AccountRecord ar) {
		try {
			// for (int i = 0; i < 20; i++)
			dao.create(ar);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void update(AccountRecord ar) {
		try {
			dao.update(ar);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static boolean ifExist(AccountRecord ar) {
		try {
			if (((List<AccountRecord>) dao.queryForId(ar.getId())).size() == 0) {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	public static void delete(AccountRecord ar) {
		try {
			dao.delete(ar);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static List<AccountRecord> queryAll() {
		try {
			QueryBuilder<AccountRecord, Integer> queryBuilder = dao
					.queryBuilder();
			queryBuilder.orderBy(AccountRecord.COL_UPDATETIME, false);
			return queryBuilder.query();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Constants.DB_SEARCH_LIST_NOT_FOUND;
	}

	public static void deleteAll(List<AccountRecord> arList) {
		try {
			dao.delete(arList);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void deleteByTypeId(Integer typeId) {
		try {
			DeleteBuilder db = dao.deleteBuilder();
			db.where().eq(AccountRecord.COL_TYPE_ID, typeId);
			dao.delete(db.prepare());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static List<AccountRecord> queryLimitRows(int offset, int limtRows) {
		try {
			QueryBuilder<AccountRecord, Integer> queryBuilder = dao
					.queryBuilder();

			queryBuilder.offset(offset).limit(limtRows);
			queryBuilder.orderBy(AccountRecord.COL_UPDATETIME, false);
			List<AccountRecord> tmp = dao.query(queryBuilder.prepare());
			return tmp;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Constants.DB_SEARCH_LIST_NOT_FOUND;
	}
}
