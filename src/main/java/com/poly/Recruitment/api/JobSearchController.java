package com.poly.Recruitment.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.poly.Recruitment.repository.TinTuyenDungDAO;
import com.poly.Recruitment.entity.TinTuyenDung;
@CrossOrigin("*")
@RestController
public class JobSearchController {

    @Autowired
    private TinTuyenDungDAO tinTuyenDungDAO;

    @GetMapping("/api/jobs/search")
    public List<TinTuyenDung> searchJobs(@RequestParam("keyword") String keyword) {
        return tinTuyenDungDAO.searchJobsByTitle(keyword);
    }
}