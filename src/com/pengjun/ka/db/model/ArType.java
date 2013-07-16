package com.pengjun.ka.db.model;

import com.j256.ormlite.field.DatabaseField;

public class ArType {

	public static final String COL_ID = "id";
	public static final String COL_TYPE_NAME = "typeName";
	public static final String COL_UPDATETIME = "updateTime";

	@DatabaseField(generatedId = true, columnName = "_id")
	int id;

	@DatabaseField(canBeNull = false)
	String typeName;

	@DatabaseField(canBeNull = false)
	int imgResId;

	@DatabaseField(canBeNull = false)
	String updateTime;

	public int getImgResId() {
		return imgResId;
	}

	public void setImgResId(int imgResId) {
		this.imgResId = imgResId;
	}

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

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

}
