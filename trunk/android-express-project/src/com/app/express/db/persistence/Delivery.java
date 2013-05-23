package com.app.express.db.persistence;

import java.sql.SQLException;
import java.util.Date;

import android.util.Log;

import com.app.express.config.Categories;
import com.app.express.db.DatabaseHelper;
import com.app.express.db.dao.DeliveryDao;
import com.app.express.helper.App;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.misc.BaseDaoEnabled;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Delivery model persistence for ORM using ORMLite. {@link http://ormlite.com/javadoc/ormlite-core/doc-files/ormlite.html#Top}
 * 
 * Cette classe correspond à une Livraison au niveau de l'application, soit une destination où se rendre parmi toutes celles de la tournée. Elle peut inclure
 * plusieurs paquets à livrer à un seul destinataire.
 * 
 * @author Ambroise
 */
@DatabaseTable(tableName = "delivery", daoClass = DeliveryDao.class)
public class Delivery extends BaseDaoEnabled {
	@DatabaseField(generatedId = true, columnName = "delivery_id", useGetSet = true)
	private Integer deliveryId;
	
	@DatabaseField(columnName = "id", uniqueIndexName = "priority_uniq", useGetSet = true)
	private String id;

	@DatabaseField(columnName = "round_id", uniqueIndexName = "priority_uniq", canBeNull = false, foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true, maxForeignAutoRefreshLevel = 3, useGetSet = true)
	private Round round;

	@DatabaseField(index = true, columnName = "type_delivery", canBeNull = false, defaultValue = Categories.Types.type_delivery.DELIVERY, useGetSet = true)
	private String typeDelivery = Categories.Types.type_delivery.DELIVERY;

	@DatabaseField(uniqueIndexName = "priority_uniq", canBeNull = false, useGetSet = true)
	private Integer priority;

	@DatabaseField(index = true, columnName = "delivery_over", defaultValue = "0", useGetSet = true)
	private Boolean deliveryOver = false;

	@DatabaseField(index = true, columnName = "receiver_available", defaultValue = "0", useGetSet = true)
	private Boolean receiverAvailable = false;

	@DatabaseField(useGetSet = true)
	private String signature;

	@DatabaseField(columnName = "date_over", useGetSet = true)
	private Date dateOver;

	@DatabaseField(useGetSet = true)
	private String latitude;

	@DatabaseField(useGetSet = true)
	private String longitude;
	
	@ForeignCollectionField(eager = true, orderColumnName = "name", maxEagerLevel = 3)
	private ForeignCollection<User> users;
	
	@ForeignCollectionField(eager = true)
	private ForeignCollection<Packet> packets;

	private User sender;

	private User receiver;

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
	 * @param id
	 * @param round
	 * @param priority
	 */
	public Delivery(String id, Round round, Integer priority) {
		this.id = id;
		this.round = round;
		this.priority = priority;
	}
	
	/**
	 * Constructor.
	 * 
	 * @param roundId
	 * @param priority
	 */
	public Delivery(Round roundId, Integer priority) {
		this.round = roundId;
		this.priority = priority;
	}

	/**
	 * Constructor with DAO.
	 * 
	 * @param dao
	 * @param roundId
	 * @param priority
	 */
	public Delivery(Dao dao, Round roundId, Integer priority) {
		this.round = roundId;
		this.priority = priority;

		this.setDao(dao);
	}

	/**
	 * Constructor.
	 * 
	 * @param roundId
	 * @param priority
	 */
	public Delivery(Round roundId, String id, Integer priority) {
		this.round = roundId;
		this.id = id;
		this.priority = priority;
	}
	
	/**
	 * Constructor.
	 * 
	 * @param roundId
	 * @param priority
	 */
	public Delivery(Dao dao, Round roundId, String id, Integer priority) {
		this.round = roundId;
		this.id = id;
		this.priority = priority;

		this.setDao(dao);
	}

	/**
	 * Constructor.
	 * 
	 * @param roundId
	 * @param priority
	 * @param typeDelivery
	 */
	public Delivery(Round roundId, Integer priority, String typeDelivery) {
		this.round = roundId;
		this.priority = priority;
		this.typeDelivery = typeDelivery;
	}

	/**
	 * Constructor with DAO.
	 * 
	 * @param dao
	 * @param roundId
	 * @param priority
	 * @param typeDelivery
	 */
	public Delivery(Dao dao, Round roundId, Integer priority, String typeDelivery) {
		this.round = roundId;
		this.priority = priority;
		this.typeDelivery = typeDelivery;

		this.setDao(dao);
	}

	/**
	 * Constructor.
	 * 
	 * @param round
	 * @param users
	 * @param typeDelivery
	 * @param priority
	 */
	public Delivery(Round round, ForeignCollection<User> users, String typeDelivery, Integer priority) {
		this.round = round;
		this.users = users;
		this.typeDelivery = typeDelivery;
		this.priority = priority;
	}

	/**
	 * Constructor with DAO.
	 * 
	 * @param dao
	 * @param round
	 * @param users
	 * @param typeDelivery
	 * @param priority
	 */
	public Delivery(Dao dao, Round round, ForeignCollection<User> users, String typeDelivery, Integer priority) {
		this.round = round;
		this.users = users;
		this.typeDelivery = typeDelivery;
		this.priority = priority;

		this.setDao(dao);
	}

	@Override
	public String toString() {
		return "Delivery [deliveryId=" + deliveryId + ", id=" + id + ", round="
				+ round + ", typeDelivery=" + typeDelivery + ", priority="
				+ priority + ", deliveryOver=" + deliveryOver
				+ ", receiverAvailable=" + receiverAvailable + ", signature="
				+ signature + ", dateOver=" + dateOver + ", latitude="
				+ latitude + ", longitude=" + longitude + ", users=" + users
				+ ", packets=" + packets + ", sender=" + sender + ", receiver="
				+ receiver + "]";
	}
	
	public Integer countPacketToScan(){
		CloseableIterator<Packet> packetIterator = this.getPackets().closeableIterator();

		Integer count = 0;

		try {
			// Foreach types in this category.
			while (packetIterator.hasNext()) {
				Packet packet = packetIterator.next();
				if(!packet.getPacketScanned()){
					count++;
				}
			}
		} finally {
			// Always close the iterator, else the connection from the database
			// isn't destroyed.
			try {
				packetIterator.close();
			} catch (SQLException e) {
				Log.e(Delivery.class.getName(), e.getMessage(), e);
			}
		}
		
		return count;
	}

	/**
	 * Get user of the delivery by type.
	 * 
	 * @param type - Must be is a valid type in Categories.Types.type_user.
	 * 
	 * @return User
	 */
	private User _getUserByType(String type) {
		User user = null;
		
		if (receiver == null) {
			if (users != null) {
				CloseableIterator<User> userIterator = this.getUsers().closeableIterator();
				try {

					while (userIterator.hasNext()) {
						User userToMatch = userIterator.next();
						String typeUser = userToMatch.getTypeUser();
						if (typeUser.equals(type)) {
							user = userToMatch;
							break;
						}
					}
				} finally {
					// Always close the iterator, else the connection from the database isn't destroyed.
					try {
						userIterator.close();
					} catch (SQLException e) {
						Log.e(Delivery.class.getName(), "Impossible de récupérer le destinataire de la livraison:" + this.toString(), e);
					}
				}
			}
		}
		
		return user;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Round getRoundId() {
		return round;
	}

	public void setRoundId(Round roundId) {
		this.round = roundId;
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

	public Round getRound() {
		return round;
	}

	public void setRound(Round round) {
		this.round = round;
	}

	public Boolean getDeliveryOver() {
		return deliveryOver;
	}

	public void setDeliveryOver(Boolean deliveryOver) {
		this.deliveryOver = deliveryOver;
	}

	public Boolean getReceiverAvailable() {
		return receiverAvailable;
	}

	public void setReceiverAvailable(Boolean receiverAvailable) {
		this.receiverAvailable = receiverAvailable;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public Date getDateOver() {
		return dateOver;
	}

	public void setDateOver(Date dateOver) {
		this.dateOver = dateOver;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public ForeignCollection<User> getUsers() {
		return users;
	}

	public void setUsers(ForeignCollection<User> users) {
		this.users = users;
	}

	public ForeignCollection<Packet> getPackets() {
		return packets;
	}

	public void setPackets(ForeignCollection<Packet> packets) {
		this.packets = packets;
	}

	public User getSender() {
		this.sender = _getUserByType(Categories.Types.type_user.SENDER);
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public User getReceiver() {
		this.receiver = _getUserByType(Categories.Types.type_user.RECEIVER);
		return receiver;
	}

	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}
}
