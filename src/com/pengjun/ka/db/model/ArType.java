package com.pengjun.ka.db.model;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.pengjun.ka.db.service.AccountRecordService;

public class ArType implements Serializable {

	private static final long serialVersionUID = -4580617869739349891L;

	public static final String COL_ID = "id";
	public static final String COL_TYPE_NAME = "typeName";
	public static final String COL_UPDATE_TIME = "updateTime";
	public static final String COL_CREATE_DATE = "createDate";

	@DatabaseField(generatedId = true, columnName = "_id")
	int id;

	@DatabaseField(canBeNull = false)
	String typeName;

	@DatabaseField(canBeNull = false)
	int imgResId;

	@DatabaseField(canBeNull = false)
	String createDate;

	@DatabaseField(canBeNull = false)
	String updateTime;

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

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

	public int getArSum(int typeId) {
		return AccountRecordService.queryArSumByTypeId(typeId);
	}

}
