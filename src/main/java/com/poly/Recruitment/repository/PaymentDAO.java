package com.poly.Recruitment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poly.Recruitment.entity.Payment;

public interface PaymentDAO extends JpaRepository<Payment, Long>{

}
