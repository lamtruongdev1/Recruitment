package com.poly.Recruitment.service;

import com.poly.Recruitment.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User save(User user);
    User update(User user);
    void deleteById(Long id);
    Optional<User> findById(Long id);
    User findByEmail(String email);
    List<User> findAll();

}
