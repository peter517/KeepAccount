package com.pengjun.ka.db.model;

import com.j256.ormlite.field.DatabaseField;

public class ArType {

	public static final String COL_ID = "id";
	public static final String COL_TYPE_NAME = "type_name";
	public static final String COL_UPDATETIME = "update_time";

	@DatabaseField(generatedId = true, columnName = "_id")
	int id;

	@DatabaseField(canBeNull = false)
	float typeName;

	@DatabaseField
	String updateTime;

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float getTypeName() {
		return typeName;
	}

	public void setTypeName(float typeName) {
		this.typeName = typeName;
	}

}
