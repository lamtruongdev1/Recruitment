package com.poly.Recruitment.Nhatuyendung;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.poly.Recruitment.entity.DonUngTuyen;
import com.poly.Recruitment.entity.TinTuyenDung;

import java.util.List;

@Controller
public class DonUngTuyenController {

    private final RestTemplate restTemplate;

    public DonUngTuyenController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    @RequestMapping("/DonUngTuyen")
    public String viewAllApplications(Model model) {
        try {
            // Call API to fetch job applications
            ResponseEntity<List<DonUngTuyen>> response = restTemplate.exchange(
                    "http://localhost:8080/api/applications/all",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<DonUngTuyen>>() {}
            );

            List<DonUngTuyen> UT = response.getBody();

            if (UT != null) {
                // Add list of applications to model
                model.addAttribute("ut", UT);
            } else {
                model.addAttribute("ut", List.of());
            }
        } catch (Exception e) {
            // Handle errors when calling the API
            System.err.println("Error fetching applications: " + e.getMessage());
            model.addAttribute("error", "Unable to fetch applications.");
        }

        return "don-ung-tuyen"; // The name of the Thymeleaf template to display the data
    }
}
