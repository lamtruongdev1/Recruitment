package com.poly.Recruitment.api;

import com.poly.Recruitment.entity.TinTuyenDung;
import com.poly.Recruitment.repository.TinTuyenDungDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin("*")
@RestController
@RequestMapping("/api/tin-tuyen-dung")
public class DangViecLamRestController {

    @Autowired
    private TinTuyenDungDAO tinTuyenDungRepository;

    @PostMapping
    public ResponseEntity<TinTuyenDung> createJobPosting(@RequestBody TinTuyenDung tinTuyenDung) {
        TinTuyenDung createdJobPosting = tinTuyenDungRepository.save(tinTuyenDung);
        return ResponseEntity.ok(createdJobPosting);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TinTuyenDung> updateJobPosting(@PathVariable("id") Long id, @RequestBody TinTuyenDung tinTuyenDung) {
        if (tinTuyenDungRepository.findById(id).isPresent()) {
            tinTuyenDung.setJobId(id);
            TinTuyenDung updatedJobPost = tinTuyenDungRepository.save(tinTuyenDung);
            return new ResponseEntity<>(updatedJobPost, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobPosting(@PathVariable Long id) {
        if (!tinTuyenDungRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        tinTuyenDungRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TinTuyenDung> getJobPostingById(@PathVariable Long id) {
        return tinTuyenDungRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<TinTuyenDung>> getAllJobPostApproved() {
        List<TinTuyenDung> jobPostings = tinTuyenDungRepository.findAllApprovePost();
        return ResponseEntity.ok(jobPostings);
    }
    
    @GetMapping("/getALL")
    public ResponseEntity<List<TinTuyenDung>> getAllJobPosting() {
        List<TinTuyenDung> jobPostings = tinTuyenDungRepository.findAll();
        return ResponseEntity.ok(jobPostings);
    }


    // Lấy danh sách ngành nghề
    @GetMapping("/industries")
    public ResponseEntity<List<String>> getAllIndustries() {
    	List<String> industries = List.of(
    		    "Công nghệ thông tin",
    		    "Tài chính",
    		    "Y tế",
    		    "Giáo dục",
    		    "Xây dựng",
    		    "Cơ khí",
    		    "Điện tử",
    		    "Hóa học",
    		    "Dịch vụ khách hàng",
    		    "Quản lý dự án",
    		    "Marketing",
    		    "Bán hàng",
    		    "Logistics",
    		    "Nhà hàng và khách sạn",
    		    "Nông nghiệp",
    		    "Môi trường",
    		    "Vận tải",
    		    "Du lịch",
    		    "Nghệ thuật",
    		    "Giải trí",
    		    "Kinh doanh",
    		    "Nhân sự",
    		    "Pháp lý",
    		    "Khoa học nghiên cứu",
    		    "Chăm sóc sức khỏe",
    		    "Bảo mật thông tin",
    		    "Thiết kế đồ họa",
    		    "Truyền thông",
    		    "Tài nguyên nước",
    		    "Công nghiệp chế biến",
    		    "Kỹ thuật",
    		    "Thể thao",
    		    "Thời trang",
    		    "Nội thất",
    		    "Xã hội và cộng đồng"
    		);

        return ResponseEntity.ok(industries);
    }
}