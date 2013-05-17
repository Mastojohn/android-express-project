package com.app.express.dao;

import java.sql.SQLException;

import com.app.express.idao.IAccountDao;
import com.app.express.persistence.Account;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

/** JDBC implementation of the AccountDao interface. */
public class AccountDaoImpl extends BaseDaoImpl<Account, String>
  implements IAccountDao {
    // this constructor must be defined
    public AccountDaoImpl(ConnectionSource connectionSource)
      throws SQLException {
        super(connectionSource, Account.class);
    }
}