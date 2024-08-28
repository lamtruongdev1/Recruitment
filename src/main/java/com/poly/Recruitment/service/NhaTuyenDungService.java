package com.poly.Recruitment.service;

import java.util.Optional;

import com.poly.Recruitment.entity.NhaTuyenDung;

public interface NhaTuyenDungService {
    NhaTuyenDung save(NhaTuyenDung nhaTuyenDung);
    Optional<NhaTuyenDung> findById(Long id);
}
