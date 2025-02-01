package com.backend.app.dao;

import com.backend.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByRole(String role);
    List<User> findAllByOrderByAgeAsc();
    List<User> findAllByOrderByAgeDesc();
    Optional<User> findBySsn(String ssn);
}

