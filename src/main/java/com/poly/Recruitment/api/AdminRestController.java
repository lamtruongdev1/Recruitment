package com.poly.Recruitment.api;

import com.poly.Recruitment.entity.User;
import com.poly.Recruitment.repository.CVDAO;
import com.poly.Recruitment.repository.UserDAO;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminRestController {

	 @Autowired
	    private UserDAO userRepository;

    @GetMapping("/users")
    public List<User> listUsers() {
        return userRepository.findAll();
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }
}
