package com.poly.Recruitment.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
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
}
