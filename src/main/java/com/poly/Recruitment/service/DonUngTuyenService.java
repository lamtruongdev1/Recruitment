package com.poly.Recruitment.service;

import com.poly.Recruitment.dto.DonUngTuyenDTO;
import com.poly.Recruitment.entity.DonUngTuyen;
import com.poly.Recruitment.entity.NguoiTimViec;
import com.poly.Recruitment.entity.TinTuyenDung;
import com.poly.Recruitment.repository.DonUngTuyenDAO;

import com.poly.Recruitment.repository.NguoiTimViecDAO;
import com.poly.Recruitment.repository.TinTuyenDungDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DonUngTuyenService {

    @Autowired
    private DonUngTuyenDAO donUngTuyenRepository;

    public void save(DonUngTuyen donUngTuyen) {
        donUngTuyenRepository.save(donUngTuyen);
    }
}