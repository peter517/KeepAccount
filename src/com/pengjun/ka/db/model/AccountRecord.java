package com.pengjun.ka.db.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.pengjun.ka.db.dao.ArTypeDao;

@DatabaseTable
public class AccountRecord implements Serializable {

	private static final long serialVersionUID = -4580617869739349892L;

	private static Map<Integer, String> id2TypeMap = new HashMap<Integer, String>();

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

	public String getTypeName() {
		// typeId is increasing and unique
		// even typeName is deleted, the typeId could not reused
		String typeName = id2TypeMap.get(typeId);
		if (typeName == null) {
			typeName = ArTypeDao.getArTpyeNameById(typeId);
			id2TypeMap.put(typeId, typeName);
		}
		return typeName;
	}

	public void setTypeName(String arTpye) {
		typeId = ArTypeDao.getIdByArTpye(arTpye);
	}

	public String getImgResName() {
		return ArTypeDao.getImgResNameById(typeId);
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
