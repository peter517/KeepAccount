package com.pengjun.ka.db.service;

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.pengjun.ka.activity.KAApplication;
import com.pengjun.ka.db.model.AccountRecord;

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
			dao.create(ar);
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
		return null;
	}

}
