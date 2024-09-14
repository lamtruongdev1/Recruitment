package com.poly.Recruitment.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.poly.Recruitment.entity.TinTuyenDung;

@Repository
public interface TinTuyenDungDAO extends JpaRepository<TinTuyenDung, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE TinTuyenDung t SET t.title = ?1 WHERE t.jobId = ?2")
    void updateTitleByJobId(String title, Long jobId);

    @Query("SELECT t FROM TinTuyenDung t WHERE LOWER(t.title) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<TinTuyenDung> searchJobsByTitle(String keyword);

    TinTuyenDung findByTitle(String title);
    
    @Query(value = "SELECT * FROM tin_tuyen_dung where status = 'APPROVED'", nativeQuery = true)
    List<TinTuyenDung> findAllApprovePost();  
    
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE tin_tuyen_dung SET status = :status WHERE job_id = :jobId")
    void updateStatus(@Param("jobId") Long jobId, @Param("status") String status);
    
    
    @Query("SELECT j FROM TinTuyenDung j WHERE j.title LIKE %:keyword% AND j.status = :status")
    List<TinTuyenDung> searchJobsByTitleAndStatus(@Param("keyword") String keyword, @Param("status") String status);

    
    @Query("SELECT COUNT(t) FROM TinTuyenDung t WHERE t.status = 'APPROVED'")
    Long countApprovedJobs();

    @Query("SELECT COUNT(t) FROM TinTuyenDung t WHERE t.status = 'PENDING'")
    Long countPendingJobs();

}
