package com.app.express.db.dao;

import java.sql.SQLException;

import com.app.express.db.idao.IDeliveryDao;
import com.app.express.db.persistence.Delivery;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

public class DeliveryDao extends BaseDaoImpl<Delivery, String> implements IDeliveryDao {
	// This constructor must be defined.
	public DeliveryDao(ConnectionSource connectionSource) throws SQLException {
		super(connectionSource, Delivery.class);
	}
}
