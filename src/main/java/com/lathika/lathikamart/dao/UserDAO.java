package com.lathika.lathikamart.dao;

import com.lathika.lathikamart.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for User persistence operations.
 */
public interface UserDAO {
    User create(User user);
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    List<User> findAll();
    boolean update(User user);
    boolean delete(Long id);
}
