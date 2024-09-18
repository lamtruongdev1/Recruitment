package com.poly.Recruitment.service;

import com.poly.Recruitment.entity.User;
import com.poly.Recruitment.repository.UserDAO;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;


@Service
public interface UserService {
	
    User save(User user);
    User update(User user);
    void deleteById(Long id);
    Optional<User> findById(Long id);
    User findByEmail(String email);
    List<User> findAll();
    ByteArrayInputStream exportUsersToExcel() throws IOException;  // Thêm phương thức xuất Excel

}
