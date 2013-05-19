package com.app.express.db.persistence;

import com.app.express.db.dao.TypeDao;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.misc.BaseDaoEnabled;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Type model persistence for ORM using ORMLite. 
 * {@link http://ormlite.com/javadoc/ormlite-core/doc-files/ormlite.html#Top}
 * 
 * Cette classe correspond à un type, il appartient forcément à une catégorie.
 * 
 * @author Ambroise
 */
@DatabaseTable(tableName = "type", daoClass = TypeDao.class)
public class Type extends BaseDaoEnabled {
	@DatabaseField(generatedId = true, dataType = DataType.INTEGER_OBJ, useGetSet = true)
	private Integer id;

	@DatabaseField(columnName = "type_id", uniqueIndexName = "type_uniq", dataType = DataType.STRING, useGetSet = true)
	private String typeId;

	@DatabaseField(columnName = "category_id", uniqueIndexName = "type_uniq", canBeNull = false, foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true, useGetSet = true)
	private Category category;

	/**
	 * Constructor empty for ORMLite.
	 */
	public Type() {
	}

	/**
	 * Constructor with DAO.
	 * 
	 * @param dao
	 */
	public Type(Dao dao) {
		this.setDao(dao);
	}

	/**
	 * Constructor.
	 * 
	 * @param typeId
	 * @param category
	 */
	public Type(String typeId) {
		this.typeId = typeId;
	}

	/**
	 * Constructor with DAO.
	 * 
	 * @param dao
	 */
	public Type(Dao dao, String typeId) {
		this.typeId = typeId;

		this.setDao(dao);
	}

	/**
	 * Constructor.
	 * 
	 * @param typeId
	 * @param category
	 */
	public Type(String typeId, Category category) {
		this.typeId = typeId;
		this.category = category;
	}

	/**
	 * Constructor with DAO.
	 * 
	 * @param dao
	 * @param category
	 */
	public Type(Dao dao, String typeId, Category category) {
		this.typeId = typeId;
		this.category = category;

		this.setDao(dao);
	}

	@Override
	public String toString() {
		return "Type [id=" + id + ", typeId=" + typeId + ", category=" + category + "]";
	}

	/*
	 * ******************** Accessors. **********************
	 */

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

}
