package com.poly.Recruitment.api;

import com.poly.Recruitment.entity.User;
import com.poly.Recruitment.repository.UserDAO;
import com.poly.Recruitment.service.UserService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.poly.Recruitment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin/users")
public class AdminRestController {

	 @Autowired
	    private UserDAO userRepository;
	 
	    @Autowired
	    private UserService userService;
	 // Get all users
	    @GetMapping
	    public List<User> getAllUsers() {
	        return userRepository.findAll();
	    }

	    // Search users by name
	    @GetMapping("/search")
	    public List<User> searchUsers(@RequestParam String name) {
	        return userRepository.findByNameContaining(name);
	    }

	    // Delete a user
	    @DeleteMapping("/{id}")
	    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
	        Optional<User> user = userRepository.findById(id);
	        if (user.isPresent()) {
	            userRepository.deleteById(id);
	            return ResponseEntity.ok("User deleted successfully");
	        } else {
	            return ResponseEntity.status(404).body("User not found");
	        }
	    }

	    // Update user role
	    @PutMapping("/{id}/role")
	    public ResponseEntity<String> updateUserRole(@PathVariable Long id, @RequestParam String role) {
	        Optional<User> optionalUser = userRepository.findById(id);
	        if (optionalUser.isPresent()) {
	            User user = optionalUser.get();
	            user.setRole(role);
	            userRepository.save(user);
	            return ResponseEntity.ok("User role updated successfully");
	        } else {
	            return ResponseEntity.status(404).body("User not found");
	        }
	    }

	    // Get a user by ID
	    @GetMapping("/{id}")
	    public ResponseEntity<User> getUserById(@PathVariable Long id) {
	        Optional<User> user = userRepository.findById(id);
	        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(404).build());
	    }
	    
	    

	    @PutMapping("/{userId}/set-to-faq")
	    public ResponseEntity<String> setRoleToFAQ(@PathVariable Long userId) {
	        try {
	            // Tìm người dùng theo ID
	            User user = userRepository.findById(userId)
	                    .orElseThrow(() -> new RuntimeException("User not found"));

	            // Cập nhật vai trò thành "FAQ"
	            user.setRole("FAQ");
	            userRepository.save(user);

	            return ResponseEntity.ok("User role updated to FAQ");
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating user role");
	        }
	    }

	    @GetMapping("/export")
	    public ResponseEntity<InputStreamResource> exportUsersToExcel() {
	        try {
	            ByteArrayInputStream in = userService.exportUsersToExcel();

	            HttpHeaders headers = new HttpHeaders();
	            headers.add("Content-Disposition", "attachment; filename=users.xlsx");

	            return ResponseEntity
	                    .ok()
	                    .headers(headers)
	                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
	                    .body(new InputStreamResource(in));

	        } catch (IOException e) {
	            return ResponseEntity
	                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
	                    .build();
	        }
	    }
	    
	    
}
