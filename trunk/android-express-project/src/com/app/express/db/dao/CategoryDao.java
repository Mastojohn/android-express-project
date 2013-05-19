package com.app.express.db.dao;

import java.sql.SQLException;

import com.app.express.db.idao.IDao;
import com.app.express.db.persistence.Category;
import com.app.express.db.persistence.Delivery;
import com.app.express.db.persistence.Type;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

/**
 * Data Access Object for ORM Database <category> table.
 * 
 * @author Ambroise
 */
public class CategoryDao extends BaseDaoImpl<Category, Integer> implements IDao<Category> {
	/**
	 * Constructor mandatory for ORMLite.
	 * 
	 * @param connectionSource
	 * @throws SQLException
	 */
	public CategoryDao(ConnectionSource connectionSource) throws SQLException {
		super(connectionSource, Category.class);
	}
}
