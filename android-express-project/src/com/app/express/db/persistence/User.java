package com.app.express.db.persistence;

import com.app.express.config.Categories;
import com.app.express.db.dao.UserDao;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.misc.BaseDaoEnabled;
import com.j256.ormlite.table.DatabaseTable;

/**
 * User model persistence for ORM using ORMLite. {@link http://ormlite.com/javadoc/ormlite-core/doc-files/ormlite.html#Top}
 * 
 * Cette classe correspond à un Expéditeur ou un Destinataire au niveau de l'application.
 * 
 * @author Ambroise
 */
@DatabaseTable(tableName = "user", daoClass = UserDao.class)
public class User extends BaseDaoEnabled {
	@DatabaseField(generatedId = true, columnName = "user_id", useGetSet = true)
	private Integer userId;

	@DatabaseField(index = true, canBeNull = false, columnName = "delivery_id", foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true, maxForeignAutoRefreshLevel = 3, useGetSet = true)
	private Delivery delivery;

	@DatabaseField(canBeNull = false, useGetSet = true)
	private String name;

	@DatabaseField(index = true, canBeNull = false, columnName = "type_user", defaultValue = Categories.Types.type_user.SENDER, useGetSet = true)
	private String typeUser = Categories.Types.type_user.SENDER;

	@DatabaseField(canBeNull = false, useGetSet = true)
	private String address;

	@DatabaseField(columnName = "address_description", useGetSet = true)
	private String addressDescription;

	@DatabaseField(canBeNull = false, columnName = "zip_code", useGetSet = true)
	private String zipCode;

	@DatabaseField(canBeNull = false, useGetSet = true)
	private String city;

	@DatabaseField(useGetSet = true)
	private String phone;

	/**
	 * Constructor empty for ORMLite.
	 */
	public User() {
	}

	/**
	 * Constructor with DAO.
	 * 
	 * @param dao
	 */
	public User(Dao dao) {
		this.setDao(dao);
	}

	/**
	 * Constructor.
	 * 
	 * @param delivery
	 * @param name
	 * @param typeUser
	 * @param address
	 * @param addressDescription
	 * @param zipCode
	 * @param city
	 * @param phone
	 */
	public User(Delivery delivery, String name, String typeUser, String address, String addressDescription, String zipCode, String city, String phone) {
		this.delivery = delivery;
		this.name = name;
		this.typeUser = typeUser;
		this.address = address;
		this.addressDescription = addressDescription;
		this.zipCode = zipCode;
		this.city = city;
		this.phone = phone;
	}
	
	/**
	 * Constructor with DAO.
	 * 
	 * @param dao
	 * @param delivery
	 * @param name
	 * @param typeUser
	 * @param address
	 * @param addressDescription
	 * @param zipCode
	 * @param city
	 * @param phone
	 */
	public User(Dao dao, Delivery delivery, String name, String typeUser, String address, String addressDescription, String zipCode, String city, String phone) {
		this.delivery = delivery;
		this.name = name;
		this.typeUser = typeUser;
		this.address = address;
		this.addressDescription = addressDescription;
		this.zipCode = zipCode;
		this.city = city;
		this.phone = phone;

		this.setDao(dao);
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", delivery=" + delivery + ", name=" + name + ", typeUser=" + typeUser + ", address=" + address + ", addressDescription=" + addressDescription + ", zipCode=" + zipCode + ", city=" + city + ", phone=" + phone + "]";
	}

	/*
	 * ******************** Accessors. **********************
	 */

	public Integer getDelivererId() {
		return userId;
	}

	public void setDelivererId(Integer delivererId) {
		this.userId = delivererId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return phone;
	}

	public void setEmail(String email) {
		this.phone = email;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Delivery getDelivery() {
		return delivery;
	}

	public void setDelivery(Delivery delivery) {
		this.delivery = delivery;
	}

	public String getTypeUser() {
		return typeUser;
	}

	public void setTypeUser(String typeUser) {
		this.typeUser = typeUser;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddressDescription() {
		return addressDescription;
	}

	public void setAddressDescription(String addressDescription) {
		this.addressDescription = addressDescription;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
}
