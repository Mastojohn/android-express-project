package com.app.express.db.persistence;

import com.app.express.db.dao.DelivererDao;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "deliverer", daoClass = DelivererDao.class)
public class Deliverer {
	@DatabaseField(generatedId = true, columnName = "deliverer_id", dataType = DataType.INTEGER_OBJ)
	private Integer delivererId;

	@DatabaseField(canBeNull = false, dataType = DataType.STRING)
	private String name;

	@DatabaseField(index = true, dataType = DataType.STRING)
	private String email;

	public Deliverer() {
		// ORMLite needs a no-arg constructor
	}

	public Deliverer(String name, String email) {
		this.name = name;
		this.email = email;
	}

	public Deliverer(Integer delivererId, String name, String email) {
		this.delivererId = delivererId;
		this.name = name;
		this.email = email;
	}

	public Integer getDelivererId() {
		return delivererId;
	}

	public void setDelivererId(Integer delivererId) {
		this.delivererId = delivererId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
