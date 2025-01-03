package com.dev.library.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dev.library.model.entity.User;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findByEmail(String email);

    List<User> findByPhoneNumber(String phoneNumber);

    @Query("SELECT u FROM User u WHERE u.id = :id AND u.delflag = 0")
    public User findUserById(@Param("id") Integer userId);

    @Query("SELECT u FROM User u WHERE LOWER(u.fullname) LIKE LOWER(CONCAT('%', :fullname, '%')) AND u.delflag = 0")
    public Page<User> searchUserByName(@Param("fullname") String fullname, Pageable pageable);

    @Query("SELECT u FROM User u WHERE LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%')) AND u.delflag = 0")
    public Page<User> searchUserByEmail(@Param("email") String email, Pageable pageable);
}
