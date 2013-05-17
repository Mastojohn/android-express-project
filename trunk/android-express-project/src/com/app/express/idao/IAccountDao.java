package com.app.express.idao;

import com.app.express.persistence.Account;
import com.j256.ormlite.dao.Dao;

/** Account DAO which has a String id (Account.name) */
public interface IAccountDao extends Dao<Account, String> {
    // empty wrapper, you can add additional DAO methods here
}