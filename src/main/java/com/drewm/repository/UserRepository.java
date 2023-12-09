package com.drewm.repository;

import com.drewm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findUserByUsername(String username);
    boolean existsByUsername(String username);

    @Query(value = "SELECT * FROM _user u WHERE (LOWER(u.display_name) LIKE LOWER(CONCAT('%', :query, '%'))) OR (LOWER(u.username) LIKE LOWER(CONCAT('%', :query, '%')))", nativeQuery = true)
    List<User> searchUsers(String query);
}
