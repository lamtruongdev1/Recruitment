package com.poly.Recruitment.service;

import com.poly.Recruitment.entity.NguoiTimViec;

public interface NguoiTimViecService {
    NguoiTimViec save(NguoiTimViec nguoiTimViec);
    NguoiTimViec getJobseekerById(Long jobseekerID);
}
