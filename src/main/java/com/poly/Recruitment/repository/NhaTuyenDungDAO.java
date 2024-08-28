package com.poly.Recruitment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.Recruitment.entity.NhaTuyenDung;
@Repository
public interface NhaTuyenDungDAO extends JpaRepository<NhaTuyenDung, Long> {}