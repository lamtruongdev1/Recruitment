package com.poly.Recruitment.service.serviceImplement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.poly.Recruitment.entity.NguoiTimViec;
import com.poly.Recruitment.repository.NguoiTimViecDAO;
import com.poly.Recruitment.service.NguoiTimViecService;

@Service
public class NguoiTimViecServiceImpl implements NguoiTimViecService {

    @Autowired
    private NguoiTimViecDAO nguoiTimViecRepository;

    @Override
    public NguoiTimViec save(NguoiTimViec nguoiTimViec) {
        return nguoiTimViecRepository.save(nguoiTimViec);
    }

    @Override
    public NguoiTimViec getJobseekerById(Long jobseekerID) {
        return nguoiTimViecRepository.findById(jobseekerID).orElse(null);
    }
}
