package com.poly.Recruitment.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/dashboard")
    public String dashboard() {
        return "admin/ThongKe";
    }
 
    @GetMapping("/upload")
    public String CVMau() {
        return "admin/CV-Mau/upload-form";
    }
 
}

