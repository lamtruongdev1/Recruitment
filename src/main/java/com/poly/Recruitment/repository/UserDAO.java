package com.poly.Recruitment.repository;



import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.poly.Recruitment.entity.User;



@Repository
public interface UserDAO extends JpaRepository<User, Long> {
    Optional<User> findById(Long userId);
    User findByEmail(String email);
    @Query("SELECT COUNT(u) FROM User u")
    Long getTotalUsers();
}
