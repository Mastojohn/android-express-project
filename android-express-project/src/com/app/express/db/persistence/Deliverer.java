package com.app.express.db.persistence;

import com.app.express.db.dao.DelivererDao;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.misc.BaseDaoEnabled;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Deliverer model persistence for ORM using ORMLite.
 * {@link http://ormlite.com/javadoc/ormlite-core/doc-files/ormlite.html#Top} 
 * 
 * Cette classe correspond à un Livreur au niveau de l'application.
 * Le livreur est la personne chargée de la tournée.
 * 
 * @author Ambroise
 */
@DatabaseTable(tableName = "deliverer", daoClass = DelivererDao.class)
public class Deliverer extends BaseDaoEnabled {
	@DatabaseField(generatedId = true, columnName = "deliverer_id",
			dataType = DataType.INTEGER_OBJ, useGetSet = true)
	private Integer delivererId;

	@DatabaseField(canBeNull = false, dataType = DataType.STRING, useGetSet = true)
	private String name;

	@DatabaseField(index = true, dataType = DataType.STRING, useGetSet = true)
	private String email;

	/**
	 * Constructor empty for ORMLite.
	 */
	public Deliverer() {
	}

	/**
	 * Constructor with DAO.
	 * 
	 * @param dao
	 */
	public Deliverer(Dao dao) {
		this.setDao(dao);
	}

	/**
	 * Constructor.
	 * 
	 * @param name
	 * @param email
	 */
	public Deliverer(String name, String email) {
		this.name = name;
		this.email = email;
	}

	/**
	 * Constructor with DAO.
	 * 
	 * @param dao
	 * @param name
	 * @param email
	 */
	public Deliverer(Dao dao, String name, String email) {
		this.name = name;
		this.email = email;

		this.setDao(dao);
	}

	/**
	 * Constructor.
	 * 
	 * @param delivererId
	 * @param name
	 * @param email
	 */
	public Deliverer(Integer delivererId, String name, String email) {
		this.delivererId = delivererId;
		this.name = name;
		this.email = email;
	}

	/**
	 * Constructor with DAO.
	 * 
	 * @param dao
	 * @param delivererId
	 * @param name
	 * @param email
	 */
	public Deliverer(Dao dao, Integer delivererId, String name, String email) {
		this.delivererId = delivererId;
		this.name = name;
		this.email = email;

		this.setDao(dao);
	}

	@Override
	public String toString() {
		return "Deliverer [delivererId=" + delivererId + ", name=" + name
				+ ", email=" + email + "]";
	}

	/*
	 * ******************** Accessors. **********************
	 */

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
