package com.poly.Recruitment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.poly.Recruitment.entity.CV;

@Repository
public interface CVDAO extends JpaRepository<CV, Long> {
	
}