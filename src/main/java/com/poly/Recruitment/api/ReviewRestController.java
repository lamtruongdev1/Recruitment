package com.poly.Recruitment.api;


import com.poly.Recruitment.dto.ReviewRequest;
import com.poly.Recruitment.entity.NguoiTimViec;
import com.poly.Recruitment.entity.Review;
import com.poly.Recruitment.entity.TinTuyenDung;
import com.poly.Recruitment.repository.NguoiTimViecDAO;
import com.poly.Recruitment.repository.ReviewDAO;
import com.poly.Recruitment.repository.TinTuyenDungDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
@CrossOrigin("*")
@RestController
@RequestMapping("/api/reviews")
public class ReviewRestController {
   

    @Autowired
    private TinTuyenDungDAO tinTuyenDungDAO;

    @Autowired
    private NguoiTimViecDAO nguoiTimViecDAO;

    @Autowired
    private ReviewDAO reviewDAO; // Sử dụng ReviewDAO thay vì ReviewRepository

 
    @GetMapping("/jobseekers/search")
    public List<NguoiTimViec> searchJobseekersBySkill(@RequestParam String skill) {
        return nguoiTimViecDAO.searchBySkill(skill);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addReview(@RequestBody ReviewRequest reviewRequest) {
        try {
            // Tìm Job
            TinTuyenDung job = tinTuyenDungDAO.findById(reviewRequest.getJobId())
                    .orElseThrow(() -> new RuntimeException("Job not found"));

            // Tìm Reviewer
            NguoiTimViec reviewer = nguoiTimViecDAO.findById(reviewRequest.getReviewerId())
                    .orElseThrow(() -> new RuntimeException("Reviewer not found"));

            // Tạo Review và lưu vào cơ sở dữ liệu
            Review review = new Review();
            review.setJob(job);
            review.setReviewer(reviewer);
            review.setRating(reviewRequest.getRating());
            review.setComment(reviewRequest.getComment());

            reviewDAO.save(review);
            return ResponseEntity.ok("Review added successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error occurred while adding review: " + e.getMessage());
        }
    }

 
    // Lấy tất cả đánh giá
    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews() {
        try {
            List<Review> reviews = reviewDAO.findAll();
            return new ResponseEntity<>(reviews, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // Lấy đánh giá theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable("id") Long id) {
        Optional<Review> review = reviewDAO.findById(id);
        return review.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    
    // Cập nhật đánh giá
    @PutMapping("/{id}")
    public ResponseEntity<Review> updateReview(
            @PathVariable("id") Long id, @RequestBody Review review) {
        if (!reviewDAO.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        review.setReviewID(id);
        try {
            Review updatedReview = reviewDAO.save(review);
            return new ResponseEntity<>(updatedReview, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // Xóa đánh giá
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteReview(@PathVariable("id") Long id) {
        try {
            reviewDAO.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    
    @GetMapping("/jobseeker/{jobseekerID}")
    public ResponseEntity<NguoiTimViec> getJobseekerById(@PathVariable Long jobseekerID) {
        NguoiTimViec jobseeker = nguoiTimViecDAO.findById(jobseekerID)
            .orElseThrow(() -> new RuntimeException("Jobseeker not found"));
        return ResponseEntity.ok(jobseeker);
    }
    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<Review>> getReviewsByJob(@PathVariable Long jobId) {
        try {
            List<Review> reviews = reviewDAO.findByJobJobId(jobId);
            return new ResponseEntity<>(reviews, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/jobseekers")
    public List<NguoiTimViec> getAllJobseekers() {
        return nguoiTimViecDAO.findAll();
    }
}
