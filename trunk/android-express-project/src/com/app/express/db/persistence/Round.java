package com.app.express.db.persistence;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.xmlpull.v1.XmlPullParserException;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import com.app.express.db.DatabaseHelper;
import com.app.express.db.dao.RoundDao;
import com.app.express.helper.App;
import com.app.express.helper.StringHelper;
import com.app.express.helper.XmlParser;
import com.google.android.gms.maps.model.LatLng;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.misc.BaseDaoEnabled;
import com.j256.ormlite.table.DatabaseTable;
import com.server.erp.Erp;

/**
 * Round model persistence for ORM using ORMLite. {@link http://ormlite.com/javadoc/ormlite-core/doc-files/ormlite.html#Top}
 * 
 * Cette classe correspond à une tournée au niveau de l'application. La tournée est effectuée par un livreur. Elle contient plusieurs livraisons. (Delivery)
 * 
 * @author Ambroise
 */
@DatabaseTable(tableName = "round", daoClass = RoundDao.class)
public class Round extends BaseDaoEnabled {
	@DatabaseField(generatedId = true, columnName = "round_id", useGetSet = true)
	private Integer roundId;

	@DatabaseField(index = true, canBeNull = false, columnName = "deliverer_id", foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true, maxForeignAutoRefreshLevel = 3, useGetSet = true)
	private Deliverer deliverer;

	@DatabaseField(index = true, canBeNull = false, useGetSet = true)
	private Date day;

	@DatabaseField(columnName = "date_end", useGetSet = true)
	private Date dateEnd;

	@ForeignCollectionField(eager = true, orderColumnName = "priority", maxEagerLevel = 3)
	private ForeignCollection<Delivery> deliveries;

	/**
	 * Constructor empty for ORMLite.
	 */
	public Round() {
	}

	/**
	 * Constructor with DAO.
	 * 
	 * @param dao
	 */
	public Round(Dao dao) {
		this.setDao(dao);
	}

	/**
	 * Constructor.
	 * 
	 * @param delivererId
	 */
	public Round(Deliverer deliverer) {
		this.deliverer = deliverer;
	}

	/**
	 * Constructor with DAO.
	 * 
	 * @param dao
	 * @param delivererId
	 */
	public Round(Dao dao, Deliverer deliverer) {
		this.deliverer = deliverer;

		this.setDao(dao);
	}

	/**
	 * Constructor.
	 * 
	 * @param delivererId
	 * @param day
	 */
	public Round(Deliverer deliverer, Date day) {
		this.deliverer = deliverer;
		this.day = day;
	}

	/**
	 * Constructor with DAO.
	 * 
	 * @param dao
	 * @param delivererId
	 * @param day
	 */
	public Round(Dao dao, Deliverer deliverer, Date day) {
		this.deliverer = deliverer;
		this.day = day;

		this.setDao(dao);
	}

	@Override
	public String toString() {
		return "Round [roundId=" + roundId + ", deliverer=" + deliverer + ", day=" + day + ", dateEnd=" + dateEnd + ", deliveries=" + deliveries + "]";
	}

	public static List<Delivery> getDeliveriesToDeliver(Round round) {
		List<Delivery> deliveries = new ArrayList<Delivery>();
		CloseableIterator<Delivery> typeIterator = round.getDeliveries().closeableIterator();

		try {
			// Foreach deliveries in this round.
			while (typeIterator.hasNext()) {
				Delivery delivery = typeIterator.next();

				// If the delivery isn't already deliver (over).
				if (!delivery.getDeliveryOver()) {
					// Add to the list.
					deliveries.add(delivery);
				}
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

		return deliveries;
	}

	/**
	 * Get the name of the city by Location.
	 * 
	 * @param location
	 * @return String
	 */
	public static String getCityNameByLocation(Location location) {
		String cityName = null;
		Geocoder gcd = new Geocoder(App.context, Locale.getDefault());
		List<Address> addresses;
		try {
			addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
			cityName = addresses.get(0).getLocality();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return cityName;
	}
	
	/**
	 * Get LatLng object from address string.
	 * @param strAddress
	 * @return LatLng|null
	 */
	public static LatLng getLatLngByAddress(String strAddress){
		Geocoder coder = new Geocoder(App.context);
		List<Address> address;

		try {
		    address = coder.getFromLocationName(strAddress, 5);
		    if (address == null) {
		        return null;
		    }
		    
		    Address location = address.get(0);
		    location.getLatitude();
		    location.getLongitude();
		    
		    return new LatLng(location.getLatitude(), location.getLongitude());
		}catch(Exception e){
			Log.w("Round", "Impossible de déterminer la position de l'adresse: "+strAddress, e);
			return null;
		}
	}

	/**
	 * Try to parse the XML file where are stored the data of the round.
	 * Parse only if it's useful.
	 * 
	 * @return boolean
	 */
	public static boolean tryParseXmlFileRound() {
		// Get the round xml file from the ERP. (Simulation)
		StringBuffer roundBuffer = Erp.getRoundsByUser(App.context);
		InputStream roundStream = null;
		XmlParser xmlparser = new XmlParser();

		if (roundBuffer != null) {
			// Rounds are available. Display content.
			roundStream = StringHelper.fromStringBuffer(roundBuffer);

			try {
				try {
					Round round = xmlparser.parse(roundStream);

					// Save the round created as round to do for this day.
					App.setCurrentRound(round);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.e("NextDelivery", "Une exception SQL a été attrapée durant la lecture du fichier XML", e);
				}
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return false;
	}

	/*
	 * ******************** Accessors. **********************
	 */

	public Integer getRoundId() {
		return roundId;
	}

	public void setRoundId(Integer roundId) {
		this.roundId = roundId;
	}

	public Deliverer getDeliverer() {
		return deliverer;
	}

	public void setDeliverer(Deliverer deliverer) {
		this.deliverer = deliverer;
	}

	public Date getDay() {
		return day;
	}

	public void setDay(Date day) {
		this.day = day;
	}

	public Date getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}

	public ForeignCollection<Delivery> getDeliveries() {
		return deliveries;
	}

	public void setDeliveries(ForeignCollection<Delivery> deliveries) {
		this.deliveries = deliveries;
	}

}
