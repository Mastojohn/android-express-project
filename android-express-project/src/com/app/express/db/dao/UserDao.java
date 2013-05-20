package com.app.express.db.dao;

import java.sql.SQLException;

import com.app.express.db.idao.IDao;
import com.app.express.db.persistence.User;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

/**
 * Data Access Object for ORM Database <user> table.
 * 
 * @author Ambroise
 */
public class UserDao extends BaseDaoImpl<User, Integer> implements IDao<User> {
	/**
	 * Constructor mandatory for ORMLite.
	 * 
	 * @param connectionSource
	 * @throws SQLException
	 */
	public UserDao(ConnectionSource connectionSource) throws SQLException {
		super(connectionSource, User.class);
	}
}
