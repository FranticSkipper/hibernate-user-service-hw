package mate.academy.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import java.util.Optional;
import mate.academy.dao.UserDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Dao;
import mate.academy.model.User;

@Dao
public class UserDaoImpl extends AbstractDao implements UserDao {
    public UserDaoImpl(EntityManagerFactory factory) {
        super(factory);
    }

    @Override
    public User add(User user) {
        EntityTransaction entityTransaction = null;

        try (EntityManager entityManager = this.factory.createEntityManager()) {
            entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();
            entityManager.persist(user);
            entityTransaction.commit();
            return user;
        } catch (Exception e) {
            if (entityTransaction != null && entityTransaction.isActive()) {
                entityTransaction.rollback();
            }

            throw new DataProcessingException("Can't create user " + user, e);
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try (EntityManager entityManager = this.factory.createEntityManager()) {
            return entityManager.createQuery("SELECT u "
                                    + "FROM User u "
                                    + "WHERE email = :email",
                            User.class)
                    .setParameter("email", email)
                    .getResultList()
                    .stream()
                    .findFirst();
        } catch (Exception e) {
            throw new DataProcessingException("Can't find user by email " + email, e);
        }
    }
}
