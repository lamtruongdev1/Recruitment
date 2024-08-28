package com.poly.Recruitment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.poly.Recruitment.entity.NguoiTimViec;

import java.util.List;

public interface NguoiTimViecDAO extends JpaRepository<NguoiTimViec, Long> {

    @Query(value = "SELECT * FROM nguoi_tim_viec WHERE LOWER(skill) LIKE LOWER(CONCAT('%', :skill, '%'))", nativeQuery = true)
    List<NguoiTimViec> searchBySkill(@Param("skill") String skill);
}
