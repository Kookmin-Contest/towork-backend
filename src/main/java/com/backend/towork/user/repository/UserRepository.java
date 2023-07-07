package com.backend.towork.user.repository;

import com.backend.towork.user.entity.User;

import org.springframework.data.repository.Repository;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends Repository<User, Long> {

    void save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    List<User> findAll();

}
