package com.pengjun.ka.db.model;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class AccountRecord implements Serializable {

	private static final long serialVersionUID = -4580617869739349892L;

	public static final String COL_ID = "id";
	public static final String COL_AMOUNT = "amount";
	public static final String COL_TYPE = "type";
	public static final String COL_DATE = "date";
	public static final String COL_COMMENT = "comment";
	public static final String COL_UPDATETIME = "update_time";

	@DatabaseField(generatedId = true, columnName = "_id")
	int id;

	@DatabaseField(canBeNull = false)
	float amount;

	@DatabaseField(canBeNull = false)
	String type;

	@DatabaseField
	String date;

	@DatabaseField
	String comment;

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

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
