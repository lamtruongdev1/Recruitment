package com.poly.Recruitment.service;

import com.poly.Recruitment.entity.NguoiTimViec;
import com.poly.Recruitment.repository.NguoiTimViecDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NguoiTimViecService {

    @Autowired
    private NguoiTimViecDAO nguoiTimViecRepository;

    public NguoiTimViec getJobseekerById(Long jobseekerID) {
        return nguoiTimViecRepository.findById(jobseekerID).orElse(null);
    }

    public NguoiTimViec save(NguoiTimViec nguoiTimViec) {
        return nguoiTimViecRepository.save(nguoiTimViec);
    }
    public void deleteById(Long jobseekerID) {
        nguoiTimViecRepository.deleteById(jobseekerID);
    }

    // Additional methods as needed
}
