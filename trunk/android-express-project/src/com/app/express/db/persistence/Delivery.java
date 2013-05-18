package com.app.express.db.persistence;

import com.app.express.db.dao.DeliveryDao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "delivery", daoClass = DeliveryDao.class)
public class Delivery {
	@DatabaseField(id = true)
	private Integer deliveryId;
	
	@DatabaseField
	private Integer roundId;
	
	@DatabaseField
	private Integer senderId;

	@DatabaseField
	private Integer priority;

	public Delivery() {
		// ORMLite needs a no-arg constructor
	}

	public Delivery(Integer roundId, Integer senderId, Integer priority) {
		this.roundId = roundId;
		this.senderId = senderId;
		this.priority = priority;
	}
	
	public Delivery(Integer deliveryId, Integer roundId, Integer senderId, Integer priority) {
		this.deliveryId = deliveryId;
		this.roundId = roundId;
		this.senderId = senderId;
		this.priority = priority;
	}

	public Integer getDeliveryId() {
		return deliveryId;
	}

	public void setDeliveryId(Integer deliveryId) {
		this.deliveryId = deliveryId;
	}

	public Integer getRoundId() {
		return roundId;
	}

	public void setRoundId(Integer roundId) {
		this.roundId = roundId;
	}

	public Integer getSenderId() {
		return senderId;
	}

	public void setSenderId(Integer senderId) {
		this.senderId = senderId;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}	
}
