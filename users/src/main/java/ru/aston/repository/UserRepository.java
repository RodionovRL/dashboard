package ru.aston.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.aston.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
