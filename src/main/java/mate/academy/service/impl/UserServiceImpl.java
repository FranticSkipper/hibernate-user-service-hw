package mate.academy.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import java.util.Optional;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Service;
import mate.academy.model.User;
import mate.academy.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    private final EntityManagerFactory factory;

    public UserServiceImpl(EntityManagerFactory factory) {
        this.factory = factory;
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

            throw new DataProcessingException("Can't add new user " + user, e);
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try (EntityManager entityManager = this.factory.createEntityManager()) {
            return Optional.ofNullable(entityManager.find(User.class, email));
        } catch (Exception e) {
            throw new DataProcessingException("Can't find user with email " + email, e);
        }
    }
}
