package com.app.express.db.dao;

import java.sql.SQLException;

import com.app.express.db.idao.IDao;
import com.app.express.db.persistence.Deliverer;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

/**
 * Data Access Object for ORM Database <deliverer> table.
 * 
 * @author Ambroise
 */
public class DelivererDao extends BaseDaoImpl<Deliverer, Integer> implements IDao<Deliverer> {
	/**
	 * Constructor mandatory for ORMLite.
	 * 
	 * @param connectionSource
	 * @throws SQLException
	 */
	public DelivererDao(ConnectionSource connectionSource) throws SQLException {
		super(connectionSource, Deliverer.class);
	}
}
