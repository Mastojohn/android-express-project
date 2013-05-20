package com.app.express.db.persistence;

import java.sql.SQLException;

import android.util.Log;

import com.app.express.db.DatabaseHelper;
import com.app.express.db.dao.CategoryDao;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.misc.BaseDaoEnabled;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Category model persistence for ORM using ORMLite. 
 * {@link http://ormlite.com/javadoc/ormlite-core/doc-files/ormlite.html#Top}
 * 
 * Cette classe correspond à une catégorie, elle contient plusieurs types.
 * 
 * @author Ambroise
 */
@DatabaseTable(tableName = "category", daoClass = CategoryDao.class)
public class Category extends BaseDaoEnabled {
	@DatabaseField(id = true, columnName = "category_id", useGetSet = true)
	private String categoryId;

	@ForeignCollectionField(eager = true, orderColumnName = "type_id")
	private ForeignCollection<Type> types;

	/**
	 * Constructor empty for ORMLite.
	 */
	public Category() {
	}

	/**
	 * Constructor with DAO.
	 * 
	 * @param dao
	 */
	public Category(Dao dao) {
		this.setDao(dao);
	}

	/**
	 * Constructor.
	 * 
	 * @param categoryId
	 */
	public Category(String categoryId) {
		this.categoryId = categoryId;
	}

	/**
	 * Constructor with DAO.
	 * 
	 * @param dao
	 * @param categoryId
	 */
	public Category(Dao dao, String categoryId) {
		this.categoryId = categoryId;

		this.setDao(dao);
	}

	@Override
	public String toString() {
		CloseableIterator<Type> typeIterator = this.getTypes().closeableIterator();

		String typesReadables = "";

		try {
			// Foreach types in this category.
			while (typeIterator.hasNext()) {
				typesReadables += typesReadables.length() > 0 ? ", " : "";
				Type type = typeIterator.next();
				typesReadables += type.getTypeId();
			}
		} finally {
			// Always close the iterator, else the connection from the database
			// isn't destroyed.
			try {
				typeIterator.close();
			} catch (SQLException e) {
				Log.e(DatabaseHelper.class.getName(), e.getMessage(), e);
			}
		}

		return "Category [categoryId=" + categoryId + ", types=" + typesReadables + "]";
	}

	/*
	 * ******************** Accessors. **********************
	 */

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public ForeignCollection<Type> getTypes() {
		return types;
	}

	public void setTypes(ForeignCollection<Type> types) {
		this.types = types;
	}

}
