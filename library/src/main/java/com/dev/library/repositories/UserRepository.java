package com.dev.library.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.library.model.entity.User;
import java.util.List;


public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findByEmail(String email);
}
