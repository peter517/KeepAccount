package com.pengjun.ka.db.service;

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.table.TableUtils;
import com.pengjun.ka.activity.KaApplication;
import com.pengjun.ka.db.model.AccountRecord;
import com.pengjun.ka.db.model.ArSearchCondition;
import com.pengjun.ka.utils.Constants;

public class ArService {

	private static AndroidConnectionSource cs = KaApplication.getAndroidConnectionSource();

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
			// for (int i = 0; i < 100; i++)
			// dao.create(DataCreater.getRandomAr());
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

	public static void reCreateTable(List<AccountRecord> arList) {

		try {
			TableUtils.dropTable(cs, AccountRecord.class, false);
			TableUtils.createTable(cs, AccountRecord.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		for (int i = arList.size() - 1; i >= 0; i--) {
			arList.get(i).setId(-1);
			insert(arList.get(i));
		}
	}

	public static List<AccountRecord> queryAllByUpdate() {
		try {
			QueryBuilder<AccountRecord, Integer> queryBuilder = dao.queryBuilder();
			queryBuilder.orderBy(AccountRecord.COL_UPDATE_TIME, false);
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

	public static int queryArSumByTypeId(int id) {
		try {
			return dao.queryForEq(AccountRecord.COL_TYPE_ID, id).size();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Constants.DB_SEARCH_INT_NOT_FOUND;
	}

	public static List<AccountRecord> queryLimitRows(int offset, int limtRows) {
		try {
			QueryBuilder<AccountRecord, Integer> queryBuilder = dao.queryBuilder();

			queryBuilder.offset(offset).limit(limtRows);
			queryBuilder.orderBy(AccountRecord.COL_UPDATE_TIME, false);
			return dao.query(queryBuilder.prepare());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Constants.DB_SEARCH_LIST_NOT_FOUND;
	}

	public static List<AccountRecord> queryAr(ArSearchCondition arSC, int offset, int limtRows) {

		if (arSC == null) {
			return null;
		}

		try {
			QueryBuilder<AccountRecord, Integer> queryBuilder = dao.queryBuilder();

			Where<AccountRecord, Integer> where = queryBuilder.where();
			where.isNotNull(AccountRecord.COL_ACOUNT);

			// account
			if (arSC.getStartAccount() != null && !arSC.getStartAccount().equals("")) {
				where.and();
				where.ge(AccountRecord.COL_ACOUNT, Float.valueOf(arSC.getStartAccount()));

			}
			if (arSC.getEndAccount() != null && !arSC.getEndAccount().equals("")) {
				where.and();
				where.le(AccountRecord.COL_ACOUNT, Float.valueOf(arSC.getEndAccount()));
			}

			// type
			if (arSC.getType() != null && !arSC.getType().equals("")) {
				where.and();
				where.eq(AccountRecord.COL_TYPE_ID, ArTypeService.getIdByArTpye(arSC.getType()));
			}

			// date
			if (arSC.getStartDate() != null && !arSC.getStartDate().equals("")) {
				where.and();
				where.ge(AccountRecord.COL_CREATE_DATE, arSC.getStartDate());
			}
			if (arSC.getEndDate() != null && !arSC.getEndDate().equals("")) {
				where.and();
				where.le(AccountRecord.COL_CREATE_DATE, arSC.getEndDate());
			}

			if (limtRows != -1) {
				queryBuilder.offset(offset).limit(limtRows);
			}
			queryBuilder.orderBy(AccountRecord.COL_UPDATE_TIME, false);

			return dao.query(queryBuilder.prepare());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Constants.DB_SEARCH_LIST_NOT_FOUND;
	}
}
