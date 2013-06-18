package com.pengjun.ka.db;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.pengjun.ka.db.model.AccountRecord;

public class DbHelper extends OrmLiteSqliteOpenHelper {

	public static final String DBNAME = "pengjun.db";
	public static final int DBVERSION = 0x01;

	@SuppressWarnings("rawtypes")
	public static final Class[] DATACLASSES = { AccountRecord.class };

	public DbHelper(Context context) {
		super(context, DBNAME, null, DBVERSION);
	}

	public static AndroidConnectionSource androidConnectionSource;

	public static AndroidConnectionSource getAndroidConnectionSource(
			Context context) {
		if (androidConnectionSource == null) {
			androidConnectionSource = new AndroidConnectionSource(new DbHelper(
					context));
		}
		return androidConnectionSource;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(SQLiteDatabase database,
			ConnectionSource connectionSource) {
		try {
			for (int i = 0; i < DATACLASSES.length; i++) {
				TableUtils.createTableIfNotExists(connectionSource,
						DATACLASSES[i]);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onUpgrade(SQLiteDatabase database,
			ConnectionSource connectionSource, int oldVersion, int newVersion) {

		for (int i = 0; i < DATACLASSES.length; i++) {
			try {
				TableUtils.dropTable(connectionSource, DATACLASSES[i], false);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		onCreate(database, connectionSource);
	}

}
