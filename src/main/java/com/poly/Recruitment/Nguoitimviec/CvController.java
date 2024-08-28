package com.poly.Recruitment.Nguoitimviec;

import com.poly.Recruitment.entity.CV;
import com.poly.Recruitment.entity.User;
import com.poly.Recruitment.service.DonUngTuyenService;
import com.poly.Recruitment.service.CVService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/cv")
public class CvController {

    @Autowired
    private CVService cvService;
    @Autowired
    private DonUngTuyenService applicationService; // Service để xử lý logic ứng tuyển

    @GetMapping
    public String getAllCVs(Model model) {
        List<CV> cvs = cvService.getAllCvs();
        model.addAttribute("cvs", cvs);
        return "NguoiTimViec/cv/CV"; // Đường dẫn đến Thymeleaf template
    }

}
