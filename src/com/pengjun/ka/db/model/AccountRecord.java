package com.pengjun.ka.db.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class AccountRecord {

	public static final String COL_AMOUNT = "amount";
	public static final String COL_CATEGORY = "category";
	public static final String COL_DATE = "date";
	public static final String COL_COMMENT = "comment";

	@DatabaseField(generatedId = true, columnName = "_id")
	int id;

	@DatabaseField(canBeNull = false)
	float amount;

	@DatabaseField(canBeNull = false)
	String category;

	@DatabaseField
	String date;

	@DatabaseField
	String comment;

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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
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
