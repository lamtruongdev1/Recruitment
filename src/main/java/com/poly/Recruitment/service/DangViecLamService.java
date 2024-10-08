package com.poly.Recruitment.service;

import com.poly.Recruitment.entity.TinTuyenDung;
import com.poly.Recruitment.exception.ResourceNotFoundException;
import com.poly.Recruitment.repository.TinTuyenDungDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class DangViecLamService {

    @Autowired
    private TinTuyenDungDAO tinTuyenDungRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;


    public TinTuyenDung createJobPosting(TinTuyenDung tinTuyenDung) {
        tinTuyenDung.setPostedDate(LocalDate.now());
        return tinTuyenDungRepository.save(tinTuyenDung);
    }

    public TinTuyenDung updateJobPosting(Long id, TinTuyenDung tinTuyenDungDetails) {
        TinTuyenDung tinTuyenDung = tinTuyenDungRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("JobPosting", "id", id));

        tinTuyenDung.setTitle(tinTuyenDungDetails.getTitle());
        tinTuyenDung.setDescription(tinTuyenDungDetails.getDescription());
        tinTuyenDung.setRequirement(tinTuyenDungDetails.getRequirement());
        tinTuyenDung.setBenefits(tinTuyenDungDetails.getBenefits());
        tinTuyenDung.setEndDate(tinTuyenDungDetails.getEndDate());
        tinTuyenDung.setPosition(tinTuyenDungDetails.getPosition());

        return tinTuyenDungRepository.save(tinTuyenDung);
    }
    
    
    
    public TinTuyenDung findById(Long id) {
        return tinTuyenDungRepository.findById(id).orElse(null);
    }

    public void save(TinTuyenDung tinTuyenDung) {
        tinTuyenDungRepository.save(tinTuyenDung);
    }

  
    public void deleteJobPosting(Long id) {
        // Xóa bản ghi
        tinTuyenDungRepository.deleteById(id);
        // Đặt lại AUTO_INCREMENT
        resetAutoIncrement();
    }

    private void resetAutoIncrement() {
        jdbcTemplate.execute("ALTER TABLE tin_tuyen_dung AUTO_INCREMENT = 1");
    }

    public List<TinTuyenDung> getAllJobPostings() {
        return tinTuyenDungRepository.findAll();
    }

    public TinTuyenDung getJobPostingById(Long id) {
        return tinTuyenDungRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("JobPosting", "id", id));
    }
    @Transactional
    public void approveJobPosting(Long id) {
        tinTuyenDungRepository.updateStatus(id, "APPROVED");
    }
    @Transactional
    public void rejectJobPosting(Long id) {
        tinTuyenDungRepository.updateStatus(id, "REJECTED");
    }

}
