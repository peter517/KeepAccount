package com.pengjun.ka.db.model;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.pengjun.ka.db.service.ArTypeService;

@DatabaseTable
public class AccountRecord implements Serializable {

	private static final long serialVersionUID = -4580617869739349892L;

	public static final String COL_ID = "id";
	public static final String COL_ACOUNT = "account";
	public static final String COL_TYPE_ID = "typeId";
	public static final String COL_CREATE_DATE = "createDate";
	public static final String COL_COMMENT = "comment";
	public static final String COL_UPDATE_TIME = "updateTime";

	@DatabaseField(generatedId = true, columnName = "_id")
	int id;

	@DatabaseField(canBeNull = false)
	float account;

	@DatabaseField(canBeNull = false)
	int typeId;

	@DatabaseField(canBeNull = false)
	String createDate;

	@DatabaseField
	String comment;

	@DatabaseField(canBeNull = false)
	String updateTime;

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
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

	public float getAccount() {
		return account;
	}

	public void setAccount(float acount) {
		this.account = acount;
	}

	public String getType() {
		return ArTypeService.getArTpyeById(typeId);
	}

	public void setType(String arTpye) {
		typeId = ArTypeService.getIdByArTpye(arTpye);
	}

	public int getImgResId() {
		return ArTypeService.getImgResIdById(typeId);
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
