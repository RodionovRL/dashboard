package ru.aston.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.aston.model.User;

import java.util.List;

/**
 * This interface extends JpaRepository to gain access to basic CRUD operations for users.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds users in the database based on list of IDs.
     * @param userIds The list of user IDs to fetch.
     * @return A list of users matching the specified IDs.
     */
    List<User> findByIdIn(List<Long> userIds);
}