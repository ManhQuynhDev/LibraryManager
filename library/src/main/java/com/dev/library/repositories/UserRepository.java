package com.dev.library.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dev.library.model.entity.User;
import java.util.List;


public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.id = :id AND u.delFlag = 0")
    public User findUserById(@Param("id") Integer id);
}
