package com.app.express.db;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.app.express.config.Categories;
import com.app.express.db.dao.CategoryDao;
import com.app.express.db.dao.DelivererDao;
import com.app.express.db.dao.DeliveryDao;
import com.app.express.db.dao.TypeDao;
import com.app.express.db.persistence.Category;
import com.app.express.db.persistence.Deliverer;
import com.app.express.db.persistence.Delivery;
import com.app.express.db.persistence.Type;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * Database helper which creates and upgrades the database and provides the DAOs
 * for the app.
 * 
 * @author Ambroise
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
	/**
	 * Name of the database stored on the device.
	 */
	private static final String DATABASE_NAME = "express.db";

	/**
	 * Version of the database. Must be changed when models persistence are
	 * changed for regenerated all tables.
	 */
	private static final int DATABASE_VERSION = 22;

	/**
	 * DAO for deliverer instance.
	 */
	private Dao<Deliverer, Integer> delivererDao;

	/**
	 * DAO for delivery instance.
	 */
	private Dao<Delivery, Integer> deliveryDao;

	/**
	 * DAO for category instance.
	 */
	private Dao<Category, Integer> categoryDao;

	/**
	 * DAO for type instance.
	 */
	private Dao<Type, Integer> typeDao;

	/**
	 * Constructor.
	 * 
	 * @param context
	 */
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/**
	 * Event called on create. Generate all tables who are not already generated
	 * before. Generate defaults values to insert to the DB.
	 * 
	 * @param sqliteDatabase
	 * @param connectionSource
	 */
	@Override
	public void onCreate(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource) {

		// Instantiate all DAO.
		try {
			this.delivererDao = new DelivererDao(connectionSource);
			this.deliveryDao = new DeliveryDao(connectionSource);
			this.categoryDao = new CategoryDao(connectionSource);
			this.typeDao = new TypeDao(connectionSource);

		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Impossible d'instancier les DAO."
					+ e.getMessage(), e);
		}

		// Create all tables.
		try {
			TableUtils.createTable(connectionSource, Deliverer.class);
			TableUtils.createTable(connectionSource, Delivery.class);
			TableUtils.createTable(connectionSource, Category.class);
			TableUtils.createTable(connectionSource, Type.class);

		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Impossible de créer les tables de la BDD."
					+ e.getMessage(), e);
		}

		// Create default values.
		try {
			Category catDelivery = new Category(categoryDao, Categories.Types.type_delivery.class
					.getSimpleName());
			categoryDao.assignEmptyForeignCollection(catDelivery, "types");

			// Add first type.
			catDelivery.getTypes()
					.add(new Type(Categories.Types.type_delivery.DELIVERY));

			// Add second type.
			catDelivery.getTypes()
					.add(new Type(Categories.Types.type_delivery.RECOVERY));

			// Create the category.
			int catDeliveryId = catDelivery.create();

			// Log message.
			Log.i(DatabaseHelper.class.getName(), "Catégorie créée: "
					+ catDelivery.toString());

			// Display all categories & types created.
			List<Category> categories = categoryDao.queryForAll();

			CloseableIterator<Category> categoryIterator = categoryDao
					.closeableIterator();
			try {
				// For each category.
				while (categoryIterator.hasNext()) {
					Category category = categoryIterator.next();

					// Display the category name.
					Log.i(DatabaseHelper.class.getName(), "\n=============\nContenu de la catégorie : "
							+ category.getCategoryId());

					CloseableIterator<Type> typeIterator = category.getTypes()
							.closeableIterator();
					
					try {
						// Foreach types in this category.
						while (typeIterator.hasNext()) {
							Type type = typeIterator.next();

							// Display the type name.
							Log.i(DatabaseHelper.class.getName(), "===> "
									+ type.getTypeId());
						}
					} finally {
						// Always close the iterator, else the connection from the database isn't destroyed.
						typeIterator.close();
					}
				}
				// End.
				Log.i(DatabaseHelper.class.getName(), "\n=============\n");

			} finally {
				// Always close the iterator, else the connection from the database isn't destroyed.
				categoryIterator.close();
			}

		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Impossible de créer les valeurs par défaut de la BDD."
					+ e.getMessage(), e);
		}

		// ---------------------------- SAMPLES SOURCE CODE
		// ---------------------------

		// ADD DELIVERER with intern DAO.
		// try {
		// Delivery delivery = new Delivery(getHelper().getDeliveryDao(), 2, 1,
		// 3);
		// delivery.create();
		// Toast.makeText(this,
		// "ID généré : "+Integer.toString(delivery.getDeliveryId()),
		// Toast.LENGTH_LONG).show();
		// } catch(SQLException e){
		// Log.e(NextDelivery.class.getName(), "Erreur SQL."+e.getMessage(), e);
		// Toast.makeText(this, "Erreur SQL : "+e.getMessage(),
		// Toast.LENGTH_LONG).show();
		// } catch(Exception e){
		// Log.e(NextDelivery.class.getName(), "Erreur."+e.getMessage(), e);
		// Toast.makeText(this, "Erreur : "+e.getMessage(),
		// Toast.LENGTH_LONG).show();
		// }

		// Add DELIVERER with extern DAO.
		// Delivery deliverer = new Delivery(1, 1, 2);
		// Dao<Delivery, Integer> dao;
		// try {
		// dao = getHelper().getDeliveryDao();
		// dao.createIfNotExists(deliverer);
		//
		// List<Delivery> deliverer2 = dao.queryForAll();
		// Toast.makeText(this, "Taille : "+Integer.toString(deliverer2.size()),
		// Toast.LENGTH_LONG).show();
		//
		// } catch(SQLException e){
		// Log.e(NextDelivery.class.getName(), "Erreur SQL."+e.getMessage(), e);
		// Toast.makeText(this, "Erreur SQL : "+e.getMessage(),
		// Toast.LENGTH_LONG).show();
		// } catch(Exception e){
		// Log.e(NextDelivery.class.getName(), "Erreur."+e.getMessage(), e);
		// Toast.makeText(this, "Erreur : "+e.getMessage(),
		// Toast.LENGTH_LONG).show();
		// }

		// GET DELIVERER BY ID
		// try {
		// Dao<Deliverer, Integer> dao = getHelper().getDelivererDao();
		// Deliverer deliverer2 = dao.queryForId(789);
		// Toast.makeText(this,
		// "ID: "+Integer.toString(deliverer2.getDelivererId()),
		// Toast.LENGTH_LONG).show();
		// } catch (SQLException e) {
		// throw new RuntimeException(e);
		// }catch (Exception e) {
		// Toast.makeText(this, "Impossible de récupérer l'utilisateur 789.",
		// Toast.LENGTH_LONG)
		// .show();
		// }

		// Create and get all deliveries.
		// Delivery delivery = new Delivery(1, 1, 1);
		// Dao<Delivery, Integer> dao;
		// try {
		// dao = getHelper().getDeliveryDao();
		// dao.createIfNotExists(delivery);
		// List<Delivery> deliverer2 = dao.queryForAll();
		// Toast.makeText(this, "Taille : "+Integer.toString(deliverer2.size()),
		// Toast.LENGTH_LONG).show();
		//
		// } catch (Exception e) {
		//
		// }

	}

	/**
	 * Event called on update. When the database already exists but the
	 * DATABASE_VERSION was upgraded, this method is called automatically for
	 * delete all tables/datas from the DB. It's will regenerate all the
	 * database by call to onCreate() method.
	 * 
	 * @param sqliteDatabase
	 * @param connectionSource
	 * @param oldVer
	 * @param newVer
	 */
	@Override
	public void onUpgrade(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource, int oldVer, int newVer) {
		try {
			TableUtils.dropTable(connectionSource, Deliverer.class, true);
			TableUtils.dropTable(connectionSource, Delivery.class, true);
			TableUtils.dropTable(connectionSource, Category.class, true);
			TableUtils.dropTable(connectionSource, Type.class, true);
			onCreate(sqliteDatabase, connectionSource);
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Impossible de passer de la version "
					+ oldVer + " à " + newVer + "." + e.getMessage(), e);
		}
	}

	/**
	 * Helper for get a DelivererDao instance object.
	 * 
	 * @return {@link DelivererDao}
	 */
	public Dao<Deliverer, Integer> getDelivererDao() {
		if (delivererDao == null) {
			try {
				delivererDao = getDao(Deliverer.class);
			} catch (SQLException e) {
				Log.e(DatabaseHelper.class.getName(), "Impossible d'instancier un DelivererDao.\n"
						+ e.getMessage(), e);
			}
		}
		return delivererDao;
	}

	/**
	 * Helper for get a DeliveryDao instance object.
	 * 
	 * @return {@link DeliveryDao}
	 */
	public Dao<Delivery, Integer> getDeliveryDao() {
		if (deliveryDao == null) {
			try {
				deliveryDao = getDao(Delivery.class);
			} catch (SQLException e) {
				Log.e(DatabaseHelper.class.getName(), "Impossible d'instancier un DeliveryDao.\n"
						+ e.getMessage(), e);
			}
		}
		return deliveryDao;
	}

	/**
	 * Helper for get a CategoryDao instance object.
	 * 
	 * @return {@link CategoryDao}
	 */
	public Dao<Category, Integer> getCategoryDao() {
		if (categoryDao == null) {
			try {
				categoryDao = getDao(Category.class);
			} catch (SQLException e) {
				Log.e(DatabaseHelper.class.getName(), "Impossible d'instancier un CategoryDao.\n"
						+ e.getMessage(), e);
			}
		}
		return categoryDao;
	}

	/**
	 * Helper for get a TypeDao instance object.
	 * 
	 * @return {@link TypeDao}
	 */
	public Dao<Type, Integer> getTypeDao() {
		if (typeDao == null) {
			try {
				typeDao = getDao(Type.class);
			} catch (SQLException e) {
				Log.e(DatabaseHelper.class.getName(), "Impossible d'instancier un TypeDao.\n"
						+ e.getMessage(), e);
			}
		}
		return typeDao;
	}
}
