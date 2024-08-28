package com.poly.Recruitment.repository;

import com.poly.Recruitment.entity.DonUngTuyen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DonUngTuyenDAO extends JpaRepository<DonUngTuyen, Long> {
    List<DonUngTuyen> findByJob_JobId(Long jobId);
    List<DonUngTuyen> findByJobseeker_JobseekerID(Long jobseekerID);

}
