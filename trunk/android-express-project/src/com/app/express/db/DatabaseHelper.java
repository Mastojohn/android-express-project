package com.app.express.db;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.app.express.activity.NextDelivery;
import com.app.express.config.Categories;
import com.app.express.db.dao.*;
import com.app.express.db.persistence.*;
import com.app.express.helper.App;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * Database helper which creates and upgrades the database and provides the DAOs for the app.
 * 
 * @author Ambroise
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
	/**
	 * Name of the database stored on the device.
	 */
	private static final String DATABASE_NAME = "express.db";

	/**
	 * Version of the database. Must be changed when models persistence are changed for regenerated all tables.
	 */
	private static final int DATABASE_VERSION = 55;

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
	 * DAO for type instance.
	 */
	private Dao<Round, Integer> roundDao;

	/**
	 * DAO for type instance.
	 */
	private Dao<User, Integer> userDao;
	
	/**
	 * DAO for type instance.
	 */
	private Dao<Packet, Integer> packetDao;

	/**
	 * Constructor.
	 * 
	 * @param context
	 */
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/**
	 * Event called on create. Generate all tables who are not already generated before. Generate defaults values to insert to the DB.
	 * 
	 * @param sqliteDatabase
	 * @param connectionSource
	 */
	@Override
	public void onCreate(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource) {

		// Instantiate all DAO.
		try {
			this.delivererDao = new DelivererDao(connectionSource);
			this.roundDao = new RoundDao(connectionSource);
			this.deliveryDao = new DeliveryDao(connectionSource);
			this.categoryDao = new CategoryDao(connectionSource);
			this.typeDao = new TypeDao(connectionSource);
			this.userDao = new UserDao(connectionSource);
			this.packetDao = new PacketDao(connectionSource);

		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Impossible d'instancier les DAO." + e.getMessage(), e);
		}

		// Create all tables.
		try {
			TableUtils.createTable(connectionSource, Deliverer.class);
			TableUtils.createTable(connectionSource, Round.class);
			TableUtils.createTable(connectionSource, Delivery.class);
			TableUtils.createTable(connectionSource, Category.class);
			TableUtils.createTable(connectionSource, Type.class);
			TableUtils.createTable(connectionSource, User.class);
			TableUtils.createTable(connectionSource, Packet.class);

		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Impossible de créer les tables de la BDD." + e.getMessage(), e);
		}

		// Generate all default categories and types.
		this.generateCategories();

		// Display categories and types generated.
		displayCategories();

		// ---------------------------- SAMPLES SOURCE CODE ---------------------------

		// 2.0

		// Create deliverer.
		// Dao<Deliverer, Integer> delivererDao = App.dbHelper.getDelivererDao();
		// Deliverer deliverer = new Deliverer(delivererDao, "Ambroise", "amb@test.fr");
		// deliverer.create();
		// deliverer.refresh();
		// Log.i(NextDelivery.class.getName(), "Livreur ajouté: " + deliverer.toString());
		//
		// // Create round.
		// Dao<Round, Integer> roundDao = App.dbHelper.getRoundDao();
		// Round round = new Round(roundDao, deliverer, new Date());
		// round.create();
		// round.refresh();
		// Log.i(NextDelivery.class.getName(), "Tournée ajoutée: " + round.toString());
		//
		// // Create delivery.
		// Dao<Delivery, Integer> deliveryDao = App.dbHelper.getDeliveryDao();
		// Delivery delivery = new Delivery(deliveryDao, round, 1);
		// delivery.create();
		// delivery.refresh();
		// Log.i(NextDelivery.class.getName(), "Livraison ajoutée: " + delivery.toString());
		//
		// // Create receiver.
		// Dao<User, Integer> receiverDao = App.dbHelper.getUserDao();
		// User receiver = new User(receiverDao, delivery, "Destinataire mathieu", Categories.Types.type_user.RECEIVER, "240 b xxx", "", "13100", "Aix", "");
		// receiver.create();
		// receiver.refresh();
		// Log.i(NextDelivery.class.getName(), "Utilisateur ajouté: " + receiver.toString());
		//
		// // Create sender.
		// Dao<User, Integer> senderDao = App.dbHelper.getUserDao();
		// User sender = new User(senderDao, delivery, "Expéditeur paul", Categories.Types.type_user.SENDER, "", "", "", "", "");
		// sender.create();
		// sender.refresh();
		// Log.i(NextDelivery.class.getName(), "Utilisateur ajouté: " + sender.toString());
		//
		// // Create packet.
		// Dao<Packet, Integer> packetDao = App.dbHelper.getPacketDao();
		// Packet packet = new Packet(packetDao, delivery, "", "", 515789423.68785531561);
		// packet.create();
		// packet.refresh();
		// Log.i(NextDelivery.class.getName(), "Colis ajouté: " + packet.toString());
		//
		// // Refresh the local delivery for add users.
		// delivery.refresh();
		// Log.i(NextDelivery.class.getName(), "Utilisateurs de la livraison: " + delivery.toString());

		// 1.0 - DEPRECATED

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
	 * Event called on update. When the database already exists but the DATABASE_VERSION was upgraded, this method is called automatically for delete all
	 * tables/datas from the DB. It's will regenerate all the database by call to onCreate() method.
	 * 
	 * @param sqliteDatabase
	 * @param connectionSource
	 * @param oldVer
	 * @param newVer
	 */
	@Override
	public void onUpgrade(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource, int oldVer, int newVer) {
		try {
			TableUtils.dropTable(connectionSource, User.class, true);
			TableUtils.dropTable(connectionSource, Packet.class, true);
			TableUtils.dropTable(connectionSource, Delivery.class, true);
			TableUtils.dropTable(connectionSource, Round.class, true);
			TableUtils.dropTable(connectionSource, Deliverer.class, true);
			TableUtils.dropTable(connectionSource, Type.class, true);
			TableUtils.dropTable(connectionSource, Category.class, true);

			onCreate(sqliteDatabase, connectionSource);

		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Impossible de passer de la version " + oldVer + " à " + newVer + "." + e.getMessage(), e);
		}
	}

	/**
	 * Display all categories from the database.
	 */
	public void displayCategories() {
		try {
			// Display all categories & types created.
			List<Category> categories = categoryDao.queryForAll();

			CloseableIterator<Category> categoryIterator = categoryDao.closeableIterator();
			try {
				// For each category.
				while (categoryIterator.hasNext()) {
					Category category = categoryIterator.next();

					// Display the category name.
					Log.i(DatabaseHelper.class.getName(), "\n=============\nContenu de la catégorie : " + category.getCategoryId());

					CloseableIterator<Type> typeIterator = category.getTypes().closeableIterator();

					try {
						// Foreach types in this category.
						while (typeIterator.hasNext()) {
							Type type = typeIterator.next();

							// Display the type name.
							Log.i(DatabaseHelper.class.getName(), "===> " + type.getTypeId());
						}
					} finally {
						// Always close the iterator, else the connection from
						// the database isn't destroyed.
						typeIterator.close();
					}
				}
				// End.
				Log.i(DatabaseHelper.class.getName(), "\n=============\n");

			} finally {
				// Always close the iterator, else the connection from the
				// database isn't destroyed.
				categoryIterator.close();
			}

		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Impossible de récupérer les catégories et types de la BDD." + e.getMessage(), e);
		}
	}

	/**
	 * generate all categories and types defined into config.Categories.
	 */
	private void generateCategories() {
		try {
			// Generate type_delivery category.
			Category type_delivery = new Category(categoryDao, Categories.Types.type_delivery.class.getSimpleName());
			categoryDao.assignEmptyForeignCollection(type_delivery, "types");

			// Add first type.
			type_delivery.getTypes().add(new Type(Categories.Types.type_delivery.DELIVERY));

			// Add second type.
			type_delivery.getTypes().add(new Type(Categories.Types.type_delivery.RECOVERY));

			// Create the category.
			type_delivery.create();

			// Log message.
			Log.i(DatabaseHelper.class.getName(), "Catégorie créée: " + type_delivery.toString());

			// ---------------

			// Generate type_delivery_state category.
			Category type_delivery_state = new Category(categoryDao, Categories.Types.type_delivery_state.class.getSimpleName());
			categoryDao.assignEmptyForeignCollection(type_delivery_state, "types");

			// Add first type.
			type_delivery_state.getTypes().add(new Type(Categories.Types.type_delivery_state.DELIVERED));

			// Add second type.
			type_delivery_state.getTypes().add(new Type(Categories.Types.type_delivery_state.FORGOTTEN));

			// Add third type.
			type_delivery_state.getTypes().add(new Type(Categories.Types.type_delivery_state.PENDING));

			// Add fourth type.
			type_delivery_state.getTypes().add(new Type(Categories.Types.type_delivery_state.REFUSED));

			// Create the category.
			type_delivery_state.create();

			// Log message.
			Log.i(DatabaseHelper.class.getName(), "Catégorie créée: " + type_delivery_state.toString());

			// ---------------

			// Generate type_delivery category.
			Category type_user = new Category(categoryDao, Categories.Types.type_user.class.getSimpleName());
			categoryDao.assignEmptyForeignCollection(type_user, "types");

			// Add first type.
			type_user.getTypes().add(new Type(Categories.Types.type_user.RECEIVER));

			// Add second type.
			type_user.getTypes().add(new Type(Categories.Types.type_user.SENDER));

			// Create the category.
			type_user.create();

			// Log message.
			Log.i(DatabaseHelper.class.getName(), "Catégorie créée: " + type_user.toString());

			// ---------------

		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Impossible de créer les valeurs par défaut de la BDD." + e.getMessage(), e);
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
				Log.e(DatabaseHelper.class.getName(), "Impossible d'instancier un DelivererDao.\n" + e.getMessage(), e);
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
				Log.e(DatabaseHelper.class.getName(), "Impossible d'instancier un DeliveryDao.\n" + e.getMessage(), e);
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
				Log.e(DatabaseHelper.class.getName(), "Impossible d'instancier un CategoryDao.\n" + e.getMessage(), e);
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
				Log.e(DatabaseHelper.class.getName(), "Impossible d'instancier un TypeDao.\n" + e.getMessage(), e);
			}
		}
		return typeDao;
	}

	/**
	 * Helper for get a RoundDao instance object.
	 * 
	 * @return {@link RoundDao}
	 */
	public Dao<Round, Integer> getRoundDao() {
		if (roundDao == null) {
			try {
				roundDao = getDao(Round.class);
			} catch (SQLException e) {
				Log.e(DatabaseHelper.class.getName(), "Impossible d'instancier un RoundDao.\n" + e.getMessage(), e);
			}
		}
		return roundDao;
	}

	/**
	 * Helper for get a UserDao instance object.
	 * 
	 * @return {@link UserDao}
	 */
	public Dao<User, Integer> getUserDao() {
		if (userDao == null) {
			try {
				userDao = getDao(User.class);
			} catch (SQLException e) {
				Log.e(DatabaseHelper.class.getName(), "Impossible d'instancier un UserDao.\n" + e.getMessage(), e);
			}
		}
		return userDao;
	}
	
	/**
	 * Helper for get a PacketDao instance object.
	 * 
	 * @return {@link PacketDao}
	 */
	public Dao<Packet, Integer> getPacketDao() {
		if (packetDao == null) {
			try {
				packetDao = getDao(Packet.class);
			} catch (SQLException e) {
				Log.e(DatabaseHelper.class.getName(), "Impossible d'instancier un PacketDao.\n" + e.getMessage(), e);
			}
		}
		return packetDao;
	}
}
