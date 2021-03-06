package com.pengjun.ka.db.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.table.TableUtils;
import com.pengjun.db.orm.BaseDao;
import com.pengjun.ka.android.activity.KaApplication;
import com.pengjun.ka.db.model.AccountRecord;
import com.pengjun.ka.db.model.ArSearchCondition;

public class ArDao extends BaseDao<AccountRecord> {

	private ArDao(AndroidConnectionSource cs, Class<AccountRecord> modelClass) {
		super(cs, modelClass);
	}

	private static ArDao arDao = null;

	public static ArDao getSingleInstance() {
		if (arDao == null) {
			arDao = new ArDao(KaApplication.getAndroidConnectionSource(),
					AccountRecord.class);
		}
		return arDao;
	}

	public boolean ifExist(AccountRecord ar) {
		try {
			if (((List<AccountRecord>) dao.queryForId(ar.getId())).size() == 0) {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	public void reCreateTable(List<AccountRecord> arList) {

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

	public List<AccountRecord> queryAllByUpdate() {
		try {
			QueryBuilder<AccountRecord, Integer> queryBuilder = dao
					.queryBuilder();
			queryBuilder.orderBy(AccountRecord.COL_UPDATE_TIME, false);
			return queryBuilder.query();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return BaseDao.DB_SEARCH_LIST_NOT_FOUND;
	}

	public void deleteAll(List<AccountRecord> arList) {
		try {
			dao.delete(arList);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void deleteByTypeId(Integer typeId) {
		try {
			DeleteBuilder db = dao.deleteBuilder();
			db.where().eq(AccountRecord.COL_TYPE_ID, typeId);
			dao.delete(db.prepare());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int queryArSumByTypeId(int id) {
		try {
			return dao.queryForEq(AccountRecord.COL_TYPE_ID, id).size();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return BaseDao.DB_SEARCH_INT_NOT_FOUND;
	}

	public List<AccountRecord> queryLimitRows(int offset, int limtRows) {
		try {
			QueryBuilder<AccountRecord, Integer> queryBuilder = dao
					.queryBuilder();

			queryBuilder.offset(offset).limit(limtRows);
			queryBuilder.orderBy(AccountRecord.COL_UPDATE_TIME, false);
			return dao.query(queryBuilder.prepare());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return BaseDao.DB_SEARCH_LIST_NOT_FOUND;
	}

	public List<String> queryAllComments() {
		try {

			QueryBuilder<AccountRecord, Integer> queryBuilder = dao
					.queryBuilder();
			Where<AccountRecord, Integer> where = queryBuilder.where();
			where.ne(AccountRecord.COL_COMMENT, "");
			List<AccountRecord> arList = dao.query(queryBuilder.prepare());

			List<String> typeNameList = new ArrayList<String>();
			for (AccountRecord ar : arList) {
				typeNameList.add(ar.getComment());
			}

			return typeNameList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return BaseDao.DB_SEARCH_LIST_NOT_FOUND;
	}

	public List<AccountRecord> queryAr(ArSearchCondition arSC, int offset,
			int limtRows) {

		if (arSC == null) {
			return null;
		}

		try {
			QueryBuilder<AccountRecord, Integer> queryBuilder = dao
					.queryBuilder();

			Where<AccountRecord, Integer> where = queryBuilder.where();
			where.isNotNull(AccountRecord.COL_ACOUNT);

			// account
			if (arSC.getStartAccount() != null
					&& !arSC.getStartAccount().equals("")) {
				where.and();
				where.ge(AccountRecord.COL_ACOUNT,
						Float.valueOf(arSC.getStartAccount()));

			}
			if (arSC.getEndAccount() != null
					&& !arSC.getEndAccount().equals("")) {
				where.and();
				where.le(AccountRecord.COL_ACOUNT,
						Float.valueOf(arSC.getEndAccount()));
			}

			// type
			if (arSC.getType() != null && !arSC.getType().equals("")) {
				where.and();
				where.eq(AccountRecord.COL_TYPE_ID, ArTypeDao
						.getSingleInstance().getIdByArTpye(arSC.getType()));
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

			if (limtRows >= 0) {
				queryBuilder.offset(offset).limit(limtRows);
				queryBuilder.orderBy(AccountRecord.COL_UPDATE_TIME, false);
			}

			return dao.query(queryBuilder.prepare());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return BaseDao.DB_SEARCH_LIST_NOT_FOUND;
	}
}
