package com.poly.Recruitment.api;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.Recruitment.dto.CvDto;
import com.poly.Recruitment.service.CVService;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/cvs")
public class CVRestController {

	@Autowired
	private CVService cvService;

    @Value("${file.upload-dir}") 
    private String uploadDir;

    @GetMapping
    public List<CvDto> getAllCvs() {
        return cvService.getAllCvs().stream().map(cv -> {
            String fileUrl = "/api/files/cv/" + cv.getFileURL();
            return new CvDto(cv.getIdCV(), cv.getName(), cv.getDescription(), fileUrl, cv.getPrice());
        }).collect(Collectors.toList());
    }
    
}
