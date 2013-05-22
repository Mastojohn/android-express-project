package com.app.express.db.persistence;

import java.util.Date;

import com.app.express.db.dao.RoundDao;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.misc.BaseDaoEnabled;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Round model persistence for ORM using ORMLite. 
 * {@link http://ormlite.com/javadoc/ormlite-core/doc-files/ormlite.html#Top}
 * 
 * Cette classe correspond à une tournée au niveau de l'application. La tournée
 * est effectuée par un livreur. Elle contient plusieurs livraisons. (Delivery)
 * 
 * @author Ambroise
 */
@DatabaseTable(tableName = "round", daoClass = RoundDao.class)
public class Round extends BaseDaoEnabled {
	@DatabaseField(generatedId = true, columnName = "round_id", useGetSet = true)
	private Integer roundId;

	@DatabaseField(index = true, canBeNull = false, columnName = "deliverer_id", foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true, maxForeignAutoRefreshLevel = 3, useGetSet = true)
	private Deliverer deliverer;

	@DatabaseField(index = true, canBeNull = false, useGetSet = true)
	private Date day;

	@DatabaseField(columnName = "date_end", useGetSet = true)
	private Date dateEnd;

	@ForeignCollectionField(eager = true, orderColumnName = "priority", maxEagerLevel = 3)
	private ForeignCollection<Delivery> deliveries;

	/**
	 * Constructor empty for ORMLite.
	 */
	public Round() {
	}

	/**
	 * Constructor with DAO.
	 * 
	 * @param dao
	 */
	public Round(Dao dao) {
		this.setDao(dao);
	}

	/**
	 * Constructor.
	 * 
	 * @param delivererId
	 */
	public Round(Deliverer deliverer) {
		this.deliverer = deliverer;
	}

	/**
	 * Constructor with DAO.
	 * 
	 * @param dao
	 * @param delivererId
	 */
	public Round(Dao dao, Deliverer deliverer) {
		this.deliverer = deliverer;

		this.setDao(dao);
	}

	/**
	 * Constructor.
	 * 
	 * @param delivererId
	 * @param day
	 */
	public Round(Deliverer deliverer, Date day) {
		this.deliverer = deliverer;
		this.day = day;
	}

	/**
	 * Constructor with DAO.
	 * 
	 * @param dao
	 * @param delivererId
	 * @param day
	 */
	public Round(Dao dao, Deliverer deliverer, Date day) {
		this.deliverer = deliverer;
		this.day = day;

		this.setDao(dao);
	}

	@Override
	public String toString() {
		return "Round [roundId=" + roundId + ", deliverer=" + deliverer
				+ ", day=" + day + ", dateEnd=" + dateEnd + ", deliveries="
				+ deliveries + "]";
	}

	/*
	 * ******************** Accessors. **********************
	 */

	public Integer getRoundId() {
		return roundId;
	}

	public void setRoundId(Integer roundId) {
		this.roundId = roundId;
	}

	public Deliverer getDeliverer() {
		return deliverer;
	}

	public void setDeliverer(Deliverer deliverer) {
		this.deliverer = deliverer;
	}

	public Date getDay() {
		return day;
	}

	public void setDay(Date day) {
		this.day = day;
	}

	public Date getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}

	public ForeignCollection<Delivery> getDeliveries() {
		return deliveries;
	}

	public void setDeliveries(ForeignCollection<Delivery> deliveries) {
		this.deliveries = deliveries;
	}

}
