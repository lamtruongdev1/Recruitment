package com.poly.Recruitment.Nhatuyendung;

import java.util.Collections;
import java.util.List;

import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.batch.BatchProperties.Job;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.poly.Recruitment.dto.MailModel;
import com.poly.Recruitment.dto.ReviewRequest;
import com.poly.Recruitment.entity.NguoiTimViec;
import com.poly.Recruitment.entity.Review;
import com.poly.Recruitment.entity.TinTuyenDung;
import com.poly.Recruitment.repository.NguoiTimViecDAO;
import com.poly.Recruitment.repository.ReviewDAO;
import com.poly.Recruitment.repository.TinTuyenDungDAO;
import com.poly.Recruitment.service.MailService;

import ch.qos.logback.classic.Logger;
import jakarta.mail.MessagingException;

import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private TinTuyenDungDAO tinTuyenDungRepository; // Thay vì tiêm entity, tiêm repository

    @Autowired
    private NguoiTimViecDAO nguoiTimViecRepository;

    @Autowired
    private ReviewDAO reviewRepository;
    @Autowired
    private MailService mailService;
    @PostMapping("/send-email")
    public String sendEmail(
    		
            @RequestParam String email, 
            @RequestParam String subject, 
            @RequestParam String message, 
            RedirectAttributes redirectAttributes,
            Model model) {
        try {
            // Prepare MailModel
            MailModel mailModel = MailModel.builder()
                    .to(email)
                    .subject(subject)
                    .content(message)
                    .from("travelbee@gmail.com") // Set your default sender email
                    .build();
            
            // Send email
            mailService.send(mailModel);
            redirectAttributes.addFlashAttribute("success", "Email sent successfully!");
            try {
                ResponseEntity<List<NguoiTimViec>> response = restTemplate.exchange(
                        "http://localhost:8080/api/reviews/jobseekers",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<NguoiTimViec>>() {}
                );
                List<NguoiTimViec> jobseekers = response.getBody();
                model.addAttribute("jobseekers", jobseekers);
            } catch (Exception e) {
                model.addAttribute("error", "Unable to fetch job seekers.");
            }
        } catch (MessagingException e) {
            redirectAttributes.addFlashAttribute("error", "Failed to send email.");
        }
        return "review/jobseekerList";
    }

    @RequestMapping("/jobseekers")
    public String getAllJobseekers(Model model) {
        try {
            ResponseEntity<List<NguoiTimViec>> response = restTemplate.exchange(
                    "http://localhost:8080/api/reviews/jobseekers",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<NguoiTimViec>>() {}
            );
            List<NguoiTimViec> jobseekers = response.getBody();
            model.addAttribute("jobseekers", jobseekers);
        } catch (Exception e) {
            model.addAttribute("error", "Unable to fetch job seekers.");
        }
        return "review/jobseekerList";
    }
    @RequestMapping("/jobseekers/{jobseekerID}")
    public String getJobseekerDetails(@PathVariable Long jobseekerID, Model model) {
        try {
            String jobseekerUrl = "http://localhost:8080/api/reviews/jobseeker/" + jobseekerID;
            ResponseEntity<NguoiTimViec> jobseekerResponse = restTemplate.getForEntity(jobseekerUrl, NguoiTimViec.class);

            if (jobseekerResponse.getStatusCode().is2xxSuccessful()) {
                NguoiTimViec jobseeker = jobseekerResponse.getBody();
                if (jobseeker != null) {
                    model.addAttribute("jobseeker", jobseeker);
                    model.addAttribute("reviews", jobseeker.getReviews());
                    model.addAttribute("jobId", jobseeker.getJobseekerID()); // Hoặc ID công việc liên quan

                    return "review/jobseekerDetails";
                } else {
                    model.addAttribute("error", "Jobseeker not found");
                    return "error";
                }
            } else {
                model.addAttribute("error", "Failed to fetch jobseeker details");
                return "error";
            }
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred: " + e.getMessage());
            return "error";
        }
    }




    
    @PostMapping("/add")
    public String submitReview(
        @RequestParam("reviewerId") Long reviewerId,
        @RequestParam("jobseekerID") Long jobseekerID,
        @RequestParam("jobId") Long jobId,
        @RequestParam("rating") Integer rating,
        @RequestParam("comment") String comment,
        Model model) {

        try {
            ReviewRequest reviewRequest = new ReviewRequest(reviewerId, jobseekerID, jobId, rating, comment);
            String apiUrl = "http://localhost:8080/api/reviews/add";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<ReviewRequest> requestEntity = new HttpEntity<>(reviewRequest, headers);
            ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);
            
            if (response.getStatusCode() == HttpStatus.OK) {
                model.addAttribute("message", "Review added successfully!");
            } else {
                model.addAttribute("message", "Failed to add review.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("message", "Error occurred while adding review.");
        }
        return "redirect:/reviews/jobseekers/" + jobseekerID;
    }



}
