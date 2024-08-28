package com.poly.Recruitment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.Recruitment.entity.NguoiTimViec;
import com.poly.Recruitment.entity.Review;
import com.poly.Recruitment.entity.TinTuyenDung;
@Repository
public interface ReviewDAO extends JpaRepository<Review, Long> {
	 List<Review> findByJobAndReviewer(TinTuyenDung job, NguoiTimViec reviewer);
	 List<Review> findByJobJobId(Long jobId);

}
