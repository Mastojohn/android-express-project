package com.app.express.db.persistence;

import com.app.express.config.Categories;
import com.app.express.db.dao.DeliveryDao;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.misc.BaseDaoEnabled;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "delivery", daoClass = DeliveryDao.class)
public class Category extends BaseDaoEnabled {
	@DatabaseField(generatedId = true, columnName = "delivery_id", dataType = DataType.INTEGER_OBJ)
	private Integer deliveryId;
	
	@DatabaseField(columnName = "round_id", uniqueIndexName = "priority_uniq", dataType = DataType.INTEGER_OBJ, canBeNull = false)
	private Integer roundId;
	
	@DatabaseField(columnName = "sender_id", dataType = DataType.INTEGER_OBJ, canBeNull = false)
	private Integer senderId;

	@DatabaseField(uniqueIndexName = "priority_uniq", dataType = DataType.INTEGER_OBJ, canBeNull = false)
	private Integer priority;
	
	@DatabaseField(columnName = "type_delivery", dataType = DataType.STRING, canBeNull = false, defaultValue = Categories.Types.TypeDelivery.DELIVERY, index = true)
	private String typeDelivery;

	public Category() {
		// ORMLite needs a no-arg constructor
	}

	public Category(Integer roundId, Integer senderId, Integer priority) {
		this.roundId = roundId;
		this.senderId = senderId;
		this.priority = priority;
	}
	
	public Category(Integer deliveryId, Integer roundId, Integer senderId, Integer priority) {
		this.deliveryId = deliveryId;
		this.roundId = roundId;
		this.senderId = senderId;
		this.priority = priority;
	}
	
	public Category(Integer deliveryId, Integer roundId, Integer senderId, Integer priority, String typeDelivery) {
		this.deliveryId = deliveryId;
		this.roundId = roundId;
		this.senderId = senderId;
		this.priority = priority;
		this.typeDelivery = typeDelivery;
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

	public String getTypeDelivery() {
		return typeDelivery;
	}

	public void setTypeDelivery(String typeDelivery) {
		this.typeDelivery = typeDelivery;
	}
}
