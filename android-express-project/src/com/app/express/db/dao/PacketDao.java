package com.app.express.db.dao;

import java.sql.SQLException;

import com.app.express.db.idao.IDao;
import com.app.express.db.persistence.Delivery;
import com.app.express.db.persistence.Packet;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

/**
 * Data Access Object for ORM Database <packet> table.
 * 
 * @author Ambroise
 */
public class PacketDao extends BaseDaoImpl<Packet, Integer> implements IDao<Packet> {
	/**
	 * Constructor mandatory for ORMLite.
	 * 
	 * @param connectionSource
	 * @throws SQLException
	 */
	public PacketDao(ConnectionSource connectionSource) throws SQLException {
		super(connectionSource, Packet.class);
	}
}
