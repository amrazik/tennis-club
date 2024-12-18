package com.amrazik.tennisclub.data.repository;

import com.amrazik.tennisclub.data.model.User;
import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository extends MyAbstractRepository<User> {
    public UserRepository() {
        super(User.class);
    }

    public User findByNumber(String phoneNumber) {
        try {
            return entityManager.createQuery("SELECT u FROM User u WHERE u.phoneNumber = :phoneNumber", User.class)
                    .setParameter("phoneNumber", phoneNumber)
                    .getSingleResult();
        }
        catch (NoResultException e) {
            return null;
        }
    }
}
