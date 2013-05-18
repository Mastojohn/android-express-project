package com.app.express.db.dao;

import java.sql.SQLException;

import com.app.express.db.idao.IDao;
import com.app.express.db.persistence.Deliverer;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

public class DelivererDao extends BaseDaoImpl<Deliverer, String> implements IDao<Deliverer> {
	// This constructor must be defined.
	public DelivererDao(ConnectionSource connectionSource) throws SQLException {
		super(connectionSource, Deliverer.class);
	}
}
