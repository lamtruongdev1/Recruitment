package com.poly.Recruitment.service;

import com.poly.Recruitment.entity.CV;
import com.poly.Recruitment.repository.CVDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CVService {

    @Autowired
    private CVDAO cvRepository;

    private final Path fileStorageLocation = Paths.get("src/main/resources/static/uploads").toAbsolutePath().normalize();

    public List<CV> getAllCvs() {
        return cvRepository.findAll();
    }

    public CV getCvById(Long id) {
        return cvRepository.findById(id).orElse(null);
    }

    public String storeFile(MultipartFile file) throws IOException {
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path targetLocation = fileStorageLocation.resolve(fileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        return fileName; // Return the file name or URL
    }

    public ResponseEntity<Resource> getCvFile(String filename) {
        try {
            Path filePath = fileStorageLocation.resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .header(HttpHeaders.CONTENT_TYPE, "application/pdf")
                        .body(resource);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            e.printStackTrace(); // Print the stack trace to help with debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public void saveCV(CV cv) {
        cvRepository.save(cv);
    }
    public void deleteCV(Long idCV) throws Exception {
        try {
            if (cvRepository.existsById(idCV)) {
                cvRepository.deleteById(idCV);
            } else {
                throw new NoSuchElementException("CV not found");
            }
        } catch (Exception e) {
            throw new Exception("Failed to delete CV: " + e.getMessage());
        }
    }

    public void updateCV(Long id, String name, String description, BigDecimal price) {
        CV cv = cvRepository.findById(id).orElseThrow(() -> new NoSuchElementException("CV not found"));
        cv.setName(name);
        cv.setDescription(description);
        cv.setPrice(price);
        cvRepository.save(cv);
    }
    public CV findCVById(Long id) {
        Optional<CV> optionalCV = cvRepository.findById(id);
        if (optionalCV.isPresent()) {
            return optionalCV.get();
        } else {
            throw new NoSuchElementException("CV with id " + id + " not found");
        }
    }


}

