package com.app.express.db.dao;

import java.sql.SQLException;

import com.app.express.db.idao.IDao;
import com.app.express.db.persistence.Delivery;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

/**
 * Data Access Object for ORM Database <delivery> table.
 * 
 * @author Ambroise
 */
public class DeliveryDao extends BaseDaoImpl<Delivery, Integer> implements IDao<Delivery> {
	/**
	 * Constructor mandatory for ORMLite.
	 * 
	 * @param connectionSource
	 * @throws SQLException
	 */
	public DeliveryDao(ConnectionSource connectionSource) throws SQLException {
		super(connectionSource, Delivery.class);
	}
}
