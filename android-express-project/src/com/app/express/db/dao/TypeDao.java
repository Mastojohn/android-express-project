package com.app.express.db.dao;

import java.sql.SQLException;

import com.app.express.db.idao.IDao;
import com.app.express.db.persistence.Delivery;
import com.app.express.db.persistence.Type;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

/**
 * Data Access Object for ORM Database <type> table.
 * 
 * @author Ambroise
 */
public class TypeDao extends BaseDaoImpl<Type, Integer> implements IDao<Type> {
	/**
	 * Constructor mandatory for ORMLite.
	 * 
	 * @param connectionSource
	 * @throws SQLException
	 */
	public TypeDao(ConnectionSource connectionSource) throws SQLException {
		super(connectionSource, Type.class);
	}
}
