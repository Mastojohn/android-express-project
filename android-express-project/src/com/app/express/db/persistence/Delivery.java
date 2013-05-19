package com.app.express.db.persistence;

import com.app.express.config.Categories;
import com.app.express.db.dao.DeliveryDao;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.misc.BaseDaoEnabled;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Delivery model persistence for ORM using ORMLite.
 * {@link http://ormlite.com/javadoc/ormlite-core/doc-files/ormlite.html#Top} 
 * 
 * Cette classe correspond à une Livraison au niveau de l'application, soit une destination où se rendre parmi toutes celles de la tournée.
 * Elle peut inclure plusieurs paquets à livrer à un seul destinataire.
 * 
 * @author Ambroise
 */
@DatabaseTable(tableName = "delivery", daoClass = DeliveryDao.class)
public class Delivery extends BaseDaoEnabled {
	@DatabaseField(generatedId = true, columnName = "delivery_id", dataType = DataType.INTEGER_OBJ, useGetSet = true)
	private Integer deliveryId;
	
	@DatabaseField(columnName = "round_id", uniqueIndexName = "priority_uniq", dataType = DataType.INTEGER_OBJ, canBeNull = false, useGetSet = true)
	private Integer roundId;
	
	@DatabaseField(columnName = "sender_id", dataType = DataType.INTEGER_OBJ, canBeNull = false, useGetSet = true)
	private Integer senderId;

	@DatabaseField(uniqueIndexName = "priority_uniq", dataType = DataType.INTEGER_OBJ, canBeNull = false, useGetSet = true)
	private Integer priority;
	
	@DatabaseField(columnName = "type_delivery", dataType = DataType.STRING, canBeNull = false, defaultValue = Categories.Types.type_delivery.DELIVERY, index = true, useGetSet = true)
	private String typeDelivery;

	/**
	 * Constructor empty for ORMLite.
	 */
	public Delivery() {
	}

	/**
	 * Constructor with DAO.
	 * 
	 * @param dao
	 */
	public Delivery(Dao dao) {
		this.setDao(dao);
	}

	/**
	 * Constructor.
	 * 
	 * @param roundId
	 * @param senderId
	 * @param priority
	 */
	public Delivery(Integer roundId, Integer senderId, Integer priority) {
		this.roundId = roundId;
		this.senderId = senderId;
		this.priority = priority;
	}
	
	/**
	 * Constructor with DAO.
	 * 
	 * @param dao
	 * @param roundId
	 * @param senderId
	 * @param priority
	 */
	public Delivery(Dao dao, Integer roundId, Integer senderId, Integer priority) {
		this.roundId = roundId;
		this.senderId = senderId;
		this.priority = priority;
		
		this.setDao(dao);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param roundId
	 * @param senderId
	 * @param priority
	 * @param typeDelivery
	 */
	public Delivery(Integer roundId, Integer senderId, Integer priority, String typeDelivery) {
		this.roundId = roundId;
		this.senderId = senderId;
		this.priority = priority;
		this.typeDelivery = typeDelivery;
	}
	
	/**
	 * Constructor with DAO.
	 * 
	 * @param dao
	 * @param roundId
	 * @param senderId
	 * @param priority
	 * @param typeDelivery
	 */
	public Delivery(Dao dao, Integer roundId, Integer senderId, Integer priority, String typeDelivery) {
		this.roundId = roundId;
		this.senderId = senderId;
		this.priority = priority;
		this.typeDelivery = typeDelivery;
		
		this.setDao(dao);
	}

	@Override
	public String toString() {
		return "Delivery [deliveryId=" + deliveryId + ", roundId=" + roundId
				+ ", senderId=" + senderId + ", priority=" + priority
				+ ", typeDelivery=" + typeDelivery + "]";
	}

	/*
	 * ******************** Accessors. **********************
	 */
	
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
