package com.poly.Recruitment.Nhatuyendung;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.poly.Recruitment.entity.TinTuyenDung;
import com.poly.Recruitment.repository.TinTuyenDungDAO;
import com.poly.Recruitment.service.DangViecLamService;
import com.poly.Recruitment.service.DateUtils;

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
		loadIndustries(model);
		loadProvinces(model);

		return "crud/CRUDTinTuyenDung";
	}

	private void loadIndustries(Model model) {
		try {
			ResponseEntity<List<String>> response = restTemplate.exchange(
					"http://localhost:8080/api/tin-tuyen-dung/industries", HttpMethod.GET, null,
					new ParameterizedTypeReference<List<String>>() {
					});

			List<String> industries = response.getBody();
			if (industries != null) {
				model.addAttribute("industries", industries);
			} else {
				model.addAttribute("error", "No industries found.");
			}
		} catch (Exception e) {
			System.err.println("Error fetching industries: " + e.getMessage());
			model.addAttribute("error", "Unable to fetch industries.");
		}
	}

	private void loadProvinces(Model model) {
		try {
			String url = "https://provinces.open-api.vn/api/"; // Thay bằng URL thực tế của API
			ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(url, HttpMethod.GET, null,
					new ParameterizedTypeReference<List<Map<String, Object>>>() {
					});

			List<Map<String, Object>> provinceMaps = response.getBody();
			List<String> provinceNames = new ArrayList<>();

			for (Map<String, Object> provinceMap : provinceMaps) {
				provinceNames.add((String) provinceMap.get("name"));
			}

			model.addAttribute("provinces", provinceNames);
		} catch (Exception e) {
			System.err.println("Error fetching provinces: " + e.getMessage());
			model.addAttribute("error", "Unable to fetch provinces.");
		}
	}

	@PostMapping("/job-postings/create")
	public String createJobPosting(
	        @RequestParam(name = "title") String title,
	        @RequestParam(name = "description") String description,
	        @RequestParam(name = "position") String position,
	        @RequestParam(name = "requirement") String requirement,
	        @RequestParam(name = "benefits") String benefits,
	        @RequestParam(name = "postedDate") LocalDate postedDate,
	        @RequestParam(name = "endDate") LocalDate endDate,
	        @RequestParam(name = "postedBy") String postedBy,
	        @RequestParam(name = "image", required = false) MultipartFile image,
	        @RequestParam(name = "salary") Double salary,
	        @RequestParam(name = "province") String province,
	        @RequestParam(name = "industry") String industry,
	        RedirectAttributes redirectAttributes) {
	    
	    // Tạo đối tượng TinTuyenDung
	    TinTuyenDung jobPost = new TinTuyenDung();

	    // Thiết lập các giá trị cho jobPost
	    jobPost.setTitle(title);
	    jobPost.setDescription(description);
	    jobPost.setPosition(position);
	    jobPost.setRequirement(requirement);
	    jobPost.setBenefits(benefits);
	    jobPost.setPostedDate(postedDate);
	    jobPost.setEndDate(endDate);
	    jobPost.setPostedBy(postedBy);
	    jobPost.setSalary(salary);
	    jobPost.setProvince(province);
	    jobPost.setIndustry(industry);
	    jobPost.setStatus("pending"); // Đặt trạng thái mặc định là "pending"

	    // Đường dẫn tới thư mục lưu trữ ảnh
	    String uploadDir = "src/main/resources/static/images/";

	    // Xử lý việc upload file ảnh
	    if (image != null && !image.isEmpty()) {
	        try {
	            // Kiểm tra và tạo thư mục nếu chưa tồn tại
	            Path uploadPath = Paths.get(uploadDir);
	            if (!Files.exists(uploadPath)) {
	                Files.createDirectories(uploadPath);
	            }

	            // Lấy tên gốc của file ảnh và tạo tên file duy nhất để lưu trữ
	            String originalFilename = image.getOriginalFilename();
	            String uniqueFilename = UUID.randomUUID().toString() + "_" + originalFilename;
	            Path filePath = uploadPath.resolve(uniqueFilename);

	            // Lưu file vào thư mục đã chỉ định
	            Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

	            // Kiểm tra xem tệp đã được lưu thành công hay chưa
	            if (Files.exists(filePath)) {
	                System.out.println("Tệp đã được lưu tại: " + filePath.toString());
	            } else {
	                System.err.println("Không thể lưu tệp!");
	                redirectAttributes.addFlashAttribute("message", "Không thể lưu tệp!");
	                return "redirect:/error";
	            }

	            // Lưu tên file duy nhất vào cơ sở dữ liệu
	            jobPost.setImage(uniqueFilename); 

	        } catch (IOException e) {
	            System.err.println("Error saving image file: " + e.getMessage());
	            redirectAttributes.addFlashAttribute("message", "Error saving image file: " + e.getMessage());
	            return "redirect:/error";
	        }
	    }

	    // Lưu jobPost vào cơ sở dữ liệu
	    try {
	        // Giả sử bạn có phương thức save trong DAO để lưu jobPost
	        tinTuyenDungDAO.save(jobPost);
	    } catch (Exception e) {
	        System.err.println("Error saving job post: " + e.getMessage());
	        redirectAttributes.addFlashAttribute("message", "Error saving job post: " + e.getMessage());
	        return "redirect:/error";
	    }

	    // Thêm thông báo thành công
	    redirectAttributes.addFlashAttribute("message", "Job posting created successfully!");

	    // Chuyển hướng về trang job postings
	    return "redirect:/index";
	}




	@PostMapping("/job-postings/update/{id}")
	public String updateJobPosting(
	        @PathVariable("id") Long id,
	        @RequestParam(name = "title") String title,
	        @RequestParam(name = "description") String description,
	        @RequestParam(name = "position") String position,
	        @RequestParam(name = "requirement") String requirement,
	        @RequestParam(name = "benefits") String benefits,
	        @RequestParam(name = "postedDate") LocalDate postedDate,
	        @RequestParam(name = "endDate") LocalDate endDate,
	        @RequestParam(name = "postedBy") String postedBy,
	        @RequestParam(name = "image", required = false) MultipartFile image,
	        @RequestParam(name = "salary") Double salary,
	        @RequestParam(name = "province") String province,
	        @RequestParam(name = "industry") String industry) {
	    
	    // Retrieve the existing job post by ID
	    TinTuyenDung jobPost = tinTuyenDungDAO.findById(id)
	        .orElseThrow(() -> new RuntimeException("Job post not found"));

	    // Set the updated values to the jobPost
	    jobPost.setTitle(title);
	    jobPost.setDescription(description);
	    jobPost.setPosition(position);
	    jobPost.setRequirement(requirement);
	    jobPost.setBenefits(benefits);
	    jobPost.setPostedDate(postedDate);
	    jobPost.setEndDate(endDate);
	    jobPost.setPostedBy(postedBy);
	    jobPost.setSalary(salary);
	    jobPost.setProvince(province);
	    jobPost.setIndustry(industry);
	    jobPost.setStatus("pending");

	    // Handle file upload
	    if (image != null && !image.isEmpty()) {
	        try {
	            String uploadDir = "src/main/resources/static/images/";
	            File uploadDirFile = new File(uploadDir);
	            if (!uploadDirFile.exists()) {
	                uploadDirFile.mkdirs();
	            }
	            
	            // Xóa file ảnh cũ nếu có ảnh mới được cập nhật
	            if (jobPost.getImage() != null && !jobPost.getImage().isEmpty()) {
	                Path oldFilePath = Paths.get(uploadDir, jobPost.getImage());
	                Files.deleteIfExists(oldFilePath);
	            }

	            // Lưu file ảnh mới
	            String originalFilename = image.getOriginalFilename();
	            String uniqueFilename = UUID.randomUUID().toString() + "_" + originalFilename;
	            Path filePath = Paths.get(uploadDir, uniqueFilename);
	            Files.write(filePath, image.getBytes());
	            jobPost.setImage(uniqueFilename);
	        } catch (IOException e) {
	            System.err.println("Error saving image file: " + e.getMessage());
	            return "error";
	        }
	    }

	    // Update the job post in the database
	    try {
	        tinTuyenDungDAO.save(jobPost);
	    } catch (Exception e) {
	        System.err.println("Error updating job posting: " + e.getMessage());
	        return "error";
	    }

	    // Redirect to the index page after successful update
	    return "redirect:/index";
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
		loadIndustries(model);
		loadProvinces(model);
        System.out.println("Received ID: " + id);
        return "crud/CRUDTinTuyenDung";
    }

    void loadAll(Model model) {
        try {
            // Gọi API để lấy danh sách tin tuyển dụng
            ResponseEntity<List<TinTuyenDung>> response = restTemplate.exchange(
                    "http://localhost:8080/api/tin-tuyen-dung/getALL",
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