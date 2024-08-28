package com.poly.Recruitment.Nguoitimviec;

import com.poly.Recruitment.entity.TinTuyenDung;
import com.poly.Recruitment.service.DateUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    @Autowired
    private RestTemplate restTemplate;

    
	@RequestMapping("/about")
	public String about() {
		return "NguoiTimViec/about";
	}
    
    @RequestMapping("/nguoitimviec")
    public String getAllJobPostings(Model model) {
        return getAllJobs(model);
    }

    @GetMapping("/jobs") // Đảm bảo đường dẫn là /jobs
    public String getAllJobs(Model model) {
        try {
            // Gọi API để lấy danh sách tin tuyển dụng
            ResponseEntity<List<TinTuyenDung>> response = restTemplate.exchange(
                    "http://localhost:8080/api/tin-tuyen-dung",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<TinTuyenDung>>() {}
            );

            List<TinTuyenDung> jobPostings = response.getBody();

            // Tạo danh sách các thuộc tính đã định dạng để thêm vào model
            Map<String, Object> formattedDates = new HashMap<>();
            for (TinTuyenDung jobPosting : jobPostings) {
                formattedDates.put("formattedPostedDate_" + jobPosting.getJobId(), DateUtils.formatToCustomFormat(jobPosting.getPostedDate()));
                formattedDates.put("formattedEndDate_" + jobPosting.getJobId(), DateUtils.formatToCustomFormat(jobPosting.getEndDate()));
            }

            // Thêm danh sách tin tuyển dụng vào model
            model.addAttribute("jobPostings", jobPostings);

            // Thêm các thuộc tính đã định dạng vào model
            model.addAllAttributes(formattedDates);

            return "NguoiTimViec/searchViecLam/TimViec"; // Tên của view HTML (tệp TimViec.html)
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Unable to load job postings");
            return "error"; // Tên của view lỗi (thay đổi tùy theo cấu trúc của bạn)
        }
    }
}
