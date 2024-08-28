package com.poly.Recruitment.api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.poly.Recruitment.entity.DonUngTuyen;
import com.poly.Recruitment.entity.NguoiTimViec;
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
    private TinTuyenDungDAO tinTuyenDungDAO; // Thay thế JobRepository bằng TinTuyenDungDAO

    @Autowired
    private DonUngTuyenDAO donUngTuyenDAO; // DAO để lưu đơn ứng tuyển
    
    @GetMapping("/all")
    public List<DonUngTuyen> getAllDonUngTuyen() {
        return donUngTuyenDAO.findAll();
    }

    private static final String UPLOAD_DIR = "static/uploads/"; // Thay đổi nếu cần
    
    
   
    @PostMapping("/apply-job")
    public ResponseEntity<String> applyForJob(
            @RequestParam("jobId") Long jobId,
            @RequestParam("cvFile") MultipartFile file,
            @RequestParam("phone") String phone,
            @RequestParam("name") String name,
            HttpSession session) {
        
        // Lấy đối tượng User từ session với khóa "ACCOUNT_SESSION"
        User user = (User) session.getAttribute("ACCOUNT_SESSION");
        
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bạn cần đăng nhập để ứng tuyển");
        }

        // Kiểm tra xem công việc có tồn tại không
        Optional<TinTuyenDung> jobOpt = tinTuyenDungDAO.findById(jobId);
        if (!jobOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Công việc không tồn tại");
        }

        TinTuyenDung job = jobOpt.get();
        
        // Tạo đối tượng Đơn Ứng Tuyển
        DonUngTuyen donUngTuyen = new DonUngTuyen();
        donUngTuyen.setJob(job);
        donUngTuyen.setName(name);
        donUngTuyen.setPhone(phone);
        donUngTuyen.setStatus("Đang xử lý");
        
        // Xử lý file CV
        if (file != null && !file.isEmpty()) {
            try {
                Path uploadPath = Paths.get(UPLOAD_DIR + file.getOriginalFilename());
                Files.createDirectories(uploadPath.getParent());
                Files.copy(file.getInputStream(), uploadPath, StandardCopyOption.REPLACE_EXISTING);
                donUngTuyen.setCvURL(file.getOriginalFilename());
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi tải lên file CV");
            }
        } else {
            return ResponseEntity.badRequest().body("Chưa chọn file CV");
        }

        // Lưu đơn ứng tuyển vào cơ sở dữ liệu
        donUngTuyenDAO.save(donUngTuyen);

        return ResponseEntity.ok("Đơn ứng tuyển đã được gửi thành công");
        
    }
}