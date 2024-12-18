package com.amrazik.tennisclub.data.repository;

import com.amrazik.tennisclub.data.model.User;
import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Repository;

/**
 * Repository for managing {@link User} entities.
 *
 * This class extends {@link MyAbstractRepository} to provide specific implementations for managing
 * {@link User} entities. It provides standard CRUD operations for {@link User} entities and adds
 * custom functionality for querying users by phone number.
 *
 * The repository is annotated with {@link Repository}, marking it as a Spring Data component
 * that will be managed by Spring's dependency injection framework.
 */
@Repository
public class UserRepository extends MyAbstractRepository<User> {

    /**
     * Constructs a new {@link UserRepository} for managing {@link User} entities.
     */
    public UserRepository() {
        super(User.class);
    }

    /**
     * Finds a {@link User} entity by its phone number.
     *
     * This method queries the database to find a user based on the provided phone number.
     * If no user is found with the specified phone number, it returns {@code null}.
     *
     * @param phoneNumber The phone number of the user to be searched for.
     * @return The {@link User} entity with the specified phone number, or {@code null} if no such user exists.
     */
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
