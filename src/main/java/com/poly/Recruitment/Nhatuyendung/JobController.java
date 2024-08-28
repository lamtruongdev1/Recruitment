package com.poly.Recruitment.Nhatuyendung;

import com.poly.Recruitment.entity.TinTuyenDung;
import com.poly.Recruitment.repository.TinTuyenDungDAO;
import com.poly.Recruitment.service.DangViecLamService;
import com.poly.Recruitment.service.DateUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class JobController {
	 @Autowired
	    private DangViecLamService tinTuyenDungService;
	    @Autowired
	    private TinTuyenDungDAO tinTuyenDungDAO;
	
	    @Autowired
	    private RestTemplate restTemplate;
	
	    
	    @RequestMapping("/about1")
		public String about() {
			return "about1";
		}
	    @RequestMapping("/tuyendung")
			public String tuyendung() {
				return "all-applications";
			}
	    @RequestMapping("/index")
	    public String getAllJobPostings(Model model, @ModelAttribute("jobPost") TinTuyenDung jobPost) {
	    	 loadAll(model);
	         return "crud/CRUDTinTuyenDung";
	    }
	 
	

	    @PostMapping("/job-postings/create")
	    public String createJobPosting(@ModelAttribute TinTuyenDung tinTuyenDung) {
	        try {
	            restTemplate.postForEntity("http://localhost:8080/api/tin-tuyen-dung", tinTuyenDung, TinTuyenDung.class);
	        } catch (Exception e) {
	            System.err.println("Error creating job posting: " + e.getMessage());
	            return "error";
	        }
	        return "redirect:/index";
	    }
	    
	    @PostMapping("/job-postings/update/{id}")
	    public String updateJobPosting(@PathVariable("id") Long id, @ModelAttribute TinTuyenDung tinTuyenDung) {
	        try {
	            tinTuyenDung.setJobId(id);  // Ensure the ID is set in the request body
	            ResponseEntity<TinTuyenDung> response = restTemplate.exchange(
	                "http://localhost:8080/api/tin-tuyen-dung/" + id,
	                HttpMethod.PUT,
	                new HttpEntity<>(tinTuyenDung),
	                TinTuyenDung.class
	            );
	            if (response.getStatusCode() == HttpStatus.OK) {
	                return "redirect:/index";
	            } else {
	                System.err.println("Error updating job posting: " + response.getStatusCode());
	                return "error";
	            }
	        } catch (Exception e) {
	            System.err.println("Error updating job posting: " + e.getMessage());
	            return "error";
	        }
	    }



	    
	    //edit
	    @RequestMapping("/job-postings/edit/{id}")
	    public String editJobPosting(@PathVariable("id") Long id, Model model) {
	        try {
	            ResponseEntity<TinTuyenDung> response = restTemplate.exchange(
	                    "http://localhost:8080/api/tin-tuyen-dung/" + id,
	                    HttpMethod.GET,
	                    null,
	                    TinTuyenDung.class
	                    
	            );
	
	            TinTuyenDung jobPost = response.getBody();
	
	            if (jobPost != null) {
	                model.addAttribute("jobPost", jobPost);
	            } else {
	                model.addAttribute("error", "Job posting not found.");
	            }
	        } catch (Exception e) {
	            model.addAttribute("error", "Unable to fetch job posting.");
	        }
	
	        loadAll(model);
	        System.out.println("Received ID: " + id);
	        return "crud/CRUDTinTuyenDung";
	    }
	
	    void loadAll(Model model) {
	        try {
	            // Gọi API để lấy danh sách tin tuyển dụng
	            ResponseEntity<List<TinTuyenDung>> response = restTemplate.exchange(
	                    "http://localhost:8080/api/tin-tuyen-dung",
	                    HttpMethod.GET,
	                    null,
	                    new ParameterizedTypeReference<List<TinTuyenDung>>() {}
	            );
	
	            List<TinTuyenDung> jobPostings = response.getBody();
	
	            // Định dạng ngày tháng trước khi gửi tới view
	            for (TinTuyenDung jobPosting : jobPostings) {
	                String formattedPostedDate = DateUtils.formatToCustomFormat(jobPosting.getPostedDate());
	                String formattedEndDate = DateUtils.formatToCustomFormat(jobPosting.getEndDate());
	
	                // Thêm ngày tháng đã định dạng vào model
	                model.addAttribute("formattedPostedDate" + jobPosting.getJobId(), formattedPostedDate);
	                model.addAttribute("formattedEndDate" + jobPosting.getJobId(), formattedEndDate);
	            }
	
	            // Thêm danh sách tin tuyển dụng vào model
	            model.addAttribute("jobPosts", jobPostings);
	        } catch (Exception e) {
	            // Xử lý lỗi khi gọi API
	            System.err.println("Error fetching job postings: " + e.getMessage());
	            model.addAttribute("error", "Unable to fetch job postings.");
	        }
	    }
		// delete
		@RequestMapping("/job-postings/delete/{id}")
		public String deleteJobPosting(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
		    try {
		        restTemplate.delete("http://localhost:8080/api/tin-tuyen-dung/" + id);
		        redirectAttributes.addFlashAttribute("message", "Xoá tin tuyển dụng thành công!");
		    } catch (HttpClientErrorException ex) {
		        if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
		            redirectAttributes.addFlashAttribute("message", "Không tìm thấy tin tuyển dụng để xoá!");
		        } else {
		            redirectAttributes.addFlashAttribute("message", "Có lỗi xảy ra khi xoá tin tuyển dụng!");
		        }
		    }
		    return "redirect:/index";
		}
	
	
	}
