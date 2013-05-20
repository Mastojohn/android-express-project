package com.app.express.db.persistence;

import java.util.Date;

import com.app.express.config.Categories;
import com.app.express.db.dao.PacketDao;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.misc.BaseDaoEnabled;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Packet model persistence for ORM using ORMLite. {@link http://ormlite.com/javadoc/ormlite-core/doc-files/ormlite.html#Top}
 * 
 * Cette classe correspond à un colis au niveau de l'application. Elle appartient à une livraison.
 * 
 * @author Ambroise
 */
@DatabaseTable(tableName = "packet", daoClass = PacketDao.class)
public class Packet extends BaseDaoEnabled {
	@DatabaseField(generatedId = true, columnName = "packet_id", useGetSet = true)
	private Integer packetId;

	@DatabaseField(index = true, columnName = "delivery_id", canBeNull = false, foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true, maxForeignAutoRefreshLevel = 3, useGetSet = true)
	private Delivery delivery;

	@DatabaseField(canBeNull = false, useGetSet = true)
	private String barcode;

	@DatabaseField(canBeNull = false, useGetSet = true)
	private String size;

	@DatabaseField(index = true, useGetSet = true)
	private Double weight;

	@DatabaseField(index = true, columnName = "delivery_attempted", defaultValue = "0", useGetSet = true)
	private Boolean deliveryAttempted = false;

	@DatabaseField(index = true, columnName = "delivered_state", defaultValue = Categories.Types.type_delivery_state.PENDING, canBeNull = false, useGetSet = true)
	private String deliveredState = Categories.Types.type_delivery_state.PENDING;

	@DatabaseField(useGetSet = true)
	private String description;

	@DatabaseField(columnName = "date_delivery", useGetSet = true)
	private Date dateDelivery;

	/**
	 * Constructor empty for ORMLite.
	 */
	public Packet() {
	}

	/**
	 * Constructor with DAO.
	 * 
	 * @param dao
	 */
	public Packet(Dao dao) {
		this.setDao(dao);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param delivery
	 * @param barcode
	 * @param size
	 * @param weight
	 */
	public Packet(Delivery delivery, String barcode, String size, Double weight) {
		this.delivery = delivery;
		this.barcode = barcode;
		this.size = size;
		this.weight = weight;
	}
	
	/**
	 * Constructor with DAO.
	 * 
	 * @param dao
	 * @param delivery
	 * @param barcode
	 * @param size
	 * @param weight
	 */
	public Packet(Dao dao, Delivery delivery, String barcode, String size, Double weight) {
		this.delivery = delivery;
		this.barcode = barcode;
		this.size = size;
		this.weight = weight;
		
		this.setDao(dao);
	}

	@Override
	public String toString() {
		return "Packet [packetId=" + packetId + ", delivery=" + delivery + ", barcode=" + barcode + ", size=" + size + ", weight=" + weight + ", deliveryAttempted=" + deliveryAttempted + ", deliveredState=" + deliveredState + ", description=" + description + ", dateDelivery=" + dateDelivery + "]";
	}

	/*
	 * ******************** Accessors. **********************
	 */

	public Integer getPacketId() {
		return packetId;
	}

	public void setPacketId(Integer packetId) {
		this.packetId = packetId;
	}

	public Delivery getDelivery() {
		return delivery;
	}

	public void setDelivery(Delivery delivery) {
		this.delivery = delivery;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Boolean getDeliveryAttempted() {
		return deliveryAttempted;
	}

	public void setDeliveryAttempted(Boolean deliveryAttempted) {
		this.deliveryAttempted = deliveryAttempted;
	}

	public String getDeliveredState() {
		return deliveredState;
	}

	public void setDeliveredState(String deliveredState) {
		this.deliveredState = deliveredState;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDateDelivery() {
		return dateDelivery;
	}

	public void setDateDelivery(Date dateDelivery) {
		this.dateDelivery = dateDelivery;
	}

}
