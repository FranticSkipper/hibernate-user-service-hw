package mate.academy.dao.impl;

import jakarta.persistence.EntityManagerFactory;
import mate.academy.lib.Dao;

@Dao
public class AbstractDao {
    protected EntityManagerFactory factory;

    public AbstractDao(EntityManagerFactory factory) {
        this.factory = factory;
    }
}
