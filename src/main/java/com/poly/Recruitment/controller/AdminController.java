package com.poly.Recruitment.controller;


import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.poly.Recruitment.repository.TinTuyenDungDAO;
import com.poly.Recruitment.service.DangViecLamService;


@Controller
//@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private DangViecLamService dangViecLamService;
	
	@Autowired
	private TinTuyenDungDAO tinTuyenDungDAO;
	
    @GetMapping("/dashboard")
    public String dashboard() {
        return "admin/ThongKe";
    }
 
    @GetMapping("/upload")
    public String CVMau() {
        return "admin/CV-Mau/upload-form";
 
    
    }
    
    @GetMapping("/postmanager")
    public String PostManager(Model model) {
    	model.addAttribute("posts", tinTuyenDungDAO.findAll());
        return "admin/PostMangement";
    }
    
    

    @PostMapping("/approve-job-posting/{id}")
    public String approveJobPosting(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            dangViecLamService.approveJobPosting(id);
            redirectAttributes.addFlashAttribute("message", "Successfully approved the job posting.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Failed to approve the job posting.");
            System.err.println("Error approving job posting: " + e.getMessage());
        }
        return "redirect:/admin/postmanager";
    }

    @PostMapping("/reject-job-posting/{id}")
    public String rejectJobPosting(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            dangViecLamService.rejectJobPosting(id);
            redirectAttributes.addFlashAttribute("message", "Successfully rejected the job posting.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Failed to reject the job posting.");
            System.err.println("Error rejecting job posting: " + e.getMessage());
        }
        return "redirect:/admin/postmanager";
    }
 
 
}

