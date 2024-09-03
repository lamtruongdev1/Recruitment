package com.poly.Recruitment.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.poly.Recruitment.entity.CV;
import com.poly.Recruitment.repository.CVDAO;
import com.poly.Recruitment.repository.UserDAO;
import com.poly.Recruitment.service.CVService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
@CrossOrigin("http://localhost:8080/")
@RestController
@RequestMapping("/admin/cv")
public class AdminCVController {

    @Autowired
    private CVService cvService;
    @Autowired
    private CVDAO cvRepository;
    
    @Autowired
    private UserDAO userDAO;
    
    @GetMapping("/total")
    public ResponseEntity<Long> getTotalUsers() {
        long totalUsers = userDAO.getTotalUsers(); // Lấy tổng số người dùng từ service
        return ResponseEntity.ok(totalUsers);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadCV(@RequestParam("file") MultipartFile file,
                                           @RequestParam("name") String name,
                                           @RequestParam("description") String description,
                                           @RequestParam("price") BigDecimal price) {
        try {
            // Save the file and get the URL
            String fileURL = cvService.storeFile(file);

            // Create and save the CV entity
            CV cv = new CV();
            cv.setName(name);
            cv.setDescription(description);
            cv.setPrice(price);
            cv.setFileURL(fileURL);

            cvService.saveCV(cv);

            return ResponseEntity.ok("CV uploaded successfully: " + fileURL);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading CV: " + e.getMessage());
        }
    }
    
    
    @GetMapping("/list")
    public ResponseEntity<List<CV>> listCVs() {
        try {
            List<CV> cvs = cvService.getAllCvs();
            return ResponseEntity.ok(cvs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    @DeleteMapping("/delete/{idCV}")
    public ResponseEntity<String> deleteCV(@PathVariable("idCV") Long idCV) {
        try {
            System.out.println("Attempting to delete CV with ID: " + idCV);
            cvService.deleteCV(idCV);
            return ResponseEntity.ok("CV deleted successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete CV");
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateCV(@PathVariable("id") Long id,
                                            @RequestParam("name") String name,
                                            @RequestParam("description") String description,
                                            @RequestParam("price") BigDecimal price) {
        try {
            cvService.updateCV(id, name, description, price);
            return ResponseEntity.ok("CV updated successfully!");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("CV not found.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error updating CV.");
        }
    }





}