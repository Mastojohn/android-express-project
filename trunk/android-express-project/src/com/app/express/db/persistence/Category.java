package com.app.express.db.persistence;

import com.app.express.db.dao.CategoryDao;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
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
	@DatabaseField(id = true, columnName = "category_id", dataType = DataType.STRING, useGetSet = true)
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
	 *  @param categoryId
	 */
	public Category(Dao dao, String categoryId) {
		this.categoryId = categoryId;

		this.setDao(dao);
	}

	@Override
	public String toString() {
		return "Category [categoryId=" + categoryId + ", types=" + types + "]";
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
