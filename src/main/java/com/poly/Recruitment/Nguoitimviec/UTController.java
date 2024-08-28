package com.poly.Recruitment.Nguoitimviec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
@Controller
public class UTController {





    @Autowired
    private RestTemplate restTemplate;

    
    @RequestMapping("/apply")
    public ModelAndView showApplyForm(@RequestParam(name = "jobId", required = false) Long jobId) {
        ModelAndView modelAndView = new ModelAndView("NguoiTimViec/apply");
        
        // Kiểm tra và thêm jobId vào model nếu có
        if (jobId != null) {
            modelAndView.addObject("jobId", jobId);
        }

        // Nếu cần lấy thêm thông tin từ API hoặc thực hiện xử lý khác
        // String apiUrl = "http://external-api-url/api/jobs/" + jobId;
        // String jobDetails = restTemplate.getForObject(apiUrl, String.class);
        // modelAndView.addObject("jobDetails", jobDetails);

        return modelAndView;
    }
}
    