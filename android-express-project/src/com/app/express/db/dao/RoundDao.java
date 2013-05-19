package com.app.express.db.dao;

import java.sql.SQLException;

import com.app.express.db.idao.IDao;
import com.app.express.db.persistence.Deliverer;
import com.app.express.db.persistence.Round;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

/**
 * Data Access Object for ORM Database <round> table.
 * 
 * @author Ambroise
 */
public class RoundDao extends BaseDaoImpl<Round, Integer> implements IDao<Round> {
	/**
	 * Constructor mandatory for ORMLite.
	 * 
	 * @param connectionSource
	 * @throws SQLException
	 */
	public RoundDao(ConnectionSource connectionSource) throws SQLException {
		super(connectionSource, Round.class);
	}
}
