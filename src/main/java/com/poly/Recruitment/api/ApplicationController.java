package com.poly.Recruitment.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.poly.Recruitment.entity.DonUngTuyen;
import com.poly.Recruitment.entity.TinTuyenDung;
import com.poly.Recruitment.entity.User;
import com.poly.Recruitment.repository.DonUngTuyenDAO;
import com.poly.Recruitment.repository.TinTuyenDungDAO;

import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    @Autowired
    private TinTuyenDungDAO tinTuyenDungDAO;

    @Autowired
    private DonUngTuyenDAO donUngTuyenDAO;

    // Define the directory to store files
    private final Path fileStorageLocation = Paths.get("src/main/resources/static/uploads").toAbsolutePath().normalize();

    public ApplicationController() {
        try {
            Files.createDirectories(fileStorageLocation); // Ensure the directory exists
        } catch (IOException ex) {
            throw new RuntimeException("Could not create the directory to upload files.", ex);
        }
    }

    // Method to store files
    public String storeFile(MultipartFile file) throws IOException {
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path targetLocation = fileStorageLocation.resolve(fileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        return fileName; // Return the file name or URL
    }

    // API to get all job applications
    @GetMapping("/all")
    public List<DonUngTuyen> getAllDonUngTuyen() {
        return donUngTuyenDAO.findAll();
    }

    // API to apply for a job
    @PostMapping("/apply-job")
    public ResponseEntity<String> applyForJob(
            @RequestParam("jobId") Long jobId,
            @RequestParam("cvFile") MultipartFile file,
            @RequestParam("phone") String phone,
            @RequestParam("name") String name,
            @RequestParam("address") String address,
            HttpSession session) {

        // Check if user is logged in
        User user = (User) session.getAttribute("ACCOUNT_SESSION");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bạn cần đăng nhập để ứng tuyển");
        }

        // Check if job exists
        Optional<TinTuyenDung> jobOpt = tinTuyenDungDAO.findById(jobId);
        if (!jobOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Công việc không tồn tại");
        }

        TinTuyenDung job = jobOpt.get();

        // Create a new application
        DonUngTuyen donUngTuyen = new DonUngTuyen();
        donUngTuyen.setJob(job);
        donUngTuyen.setName(name);
        donUngTuyen.setPhone(phone);
        donUngTuyen.setAddress(address);
        donUngTuyen.setStatus("Đang xử lý");

        // Handle CV file upload
        if (file != null && !file.isEmpty()) {
            try {
                String fileName = storeFile(file); // Use the storeFile method
                donUngTuyen.setCvURL(fileName);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi tải lên file CV");
            }
        } else {
            return ResponseEntity.badRequest().body("Chưa chọn file CV");
        }

        // Save the application in the database
        donUngTuyenDAO.save(donUngTuyen);

        return ResponseEntity.ok("Đơn ứng tuyển đã được gửi thành công");
    }
}
