package com.poly.Recruitment.controller;

import com.poly.Recruitment.dto.MessageResponse;
import com.poly.Recruitment.entity.NhaTuyenDung;
import com.poly.Recruitment.entity.NguoiTimViec;
import com.poly.Recruitment.entity.User;
import com.poly.Recruitment.service.MailService;
import com.poly.Recruitment.service.NguoiTimViecService;
import com.poly.Recruitment.service.NhaTuyenDungService;
import com.poly.Recruitment.service.UserService;
import com.poly.Recruitment.util.Common;
import com.poly.Recruitment.util.Keywords;
import com.poly.Recruitment.util.RoleEnum;
import com.poly.Recruitment.util.SessionUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {

	private final UserService userService;
	private final PasswordEncoder passwordEncoder;
	private final NhaTuyenDungService nhaTuyenDungService;
	private final NguoiTimViecService nguoiTimViecService;
	private final MailService mailService;
	private final SessionUtil sessionUtil;

	private final String uploadDir = "src/main/resources/static/images/";

	@GetMapping("/login/form")
	public String formLogin() {
		return "login";
	}

	@GetMapping("/register/form")
	public String formRegister() {
		return "register";
	}

	@GetMapping("/register-company")
	public String formRegisterCompany() {
		return "register-company";
	}

	@GetMapping("/login/success")
	public String successLogin() {
	    // Lấy thông tin người dùng từ SecurityContext
	    Authentication authenticationManager = SecurityContextHolder.getContext().getAuthentication();
	    UserDetails user = (UserDetails) authenticationManager.getPrincipal();

	    // Lưu thông tin người dùng vào session
	    User loggedInUser = userService.findByEmail(user.getUsername());
	    sessionUtil.setSessionId(Keywords.ACCOUNT_SESSION.name(), loggedInUser);
	    sessionUtil.setSessionId("user_id", loggedInUser.getUserID()); // Lưu user_id vào session
	    sessionUtil.setSessionId("userSessionEmail", user.getUsername());

	    // Xác định vai trò của người dùng
	    String role = user.getAuthorities().iterator().next().getAuthority();

	    // Chuyển hướng dựa trên vai trò của người dùng
	    if (role.equals(RoleEnum.USER.toString())) {
	        return "redirect:/nguoitimviec";
	    } else if (role.equals(RoleEnum.COMPANY.toString())) {
	        return "redirect:/index";
	    } else if (role.equals(RoleEnum.ADMIN.toString())) {
	        return "redirect:/admin/dashboard";
	    } else if (role.equals(RoleEnum.FAQ.toString())) {
	        return "redirect:/auth/error/disabled-account";
	    } else {
	        return "redirect:/login";
	    }
	}


	@GetMapping("/error/disabled-account")
	public String disabledAccount() {
		return "/error/disabled-account";
	}

	@GetMapping("/login/failure")
	public String failureLogin(Model model) {
		model.addAttribute("errors", "Đăng Nhập Thất Bại");
		return "login";
	}

	@GetMapping("/quanlytaikhoan/form")
	public String formAccountManagementPage(Model model) {
		return "accountmanager";
	}

	@PostMapping("/register/user")
	@ResponseBody
	public ResponseEntity<MessageResponse> registerUser(@RequestParam("name") String name,
			@RequestParam("email") String email, @RequestParam("phone") String phone,
			@RequestParam("skill") String skill, @RequestParam("address") String address,
			@RequestParam("password") String password, @RequestParam("confirmPassword") String confirmPassword,
			@RequestParam("avatarFile") MultipartFile avatarFile) {

		if (password == null || password.isEmpty() || !password.equals(confirmPassword)) {
			return new ResponseEntity<>(
					MessageResponse.builder().message("Password cannot be null, empty or passwords do not match")
							.code(HttpStatus.BAD_REQUEST.value()).build(),
					HttpStatus.BAD_REQUEST);
		}

		// Validate email existence
		if (checkEmail(email)) {
			return new ResponseEntity<>(
					MessageResponse.builder().message("Email Existed").code(HttpStatus.BAD_REQUEST.value()).build(),
					HttpStatus.BAD_REQUEST);
		}

		// Handle avatar file upload
		String avatarFileName = null;
		if (avatarFile != null && !avatarFile.isEmpty()) {
			try {
				avatarFileName = avatarFile.getOriginalFilename();
				Path path = Paths.get(uploadDir, avatarFileName);
				Files.write(path, avatarFile.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
				return new ResponseEntity<>(
						MessageResponse.builder().message("File upload failed")
								.code(HttpStatus.INTERNAL_SERVER_ERROR.value()).build(),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		// Save user
		User user = userService.save(User.builder().email(email).password(passwordEncoder.encode(password))
				.role(RoleEnum.USER.toString()).phone(phone).address(address).name(name).photo(avatarFileName).build());

		// Save job seeker information
		NguoiTimViec nguoiTimViec = new NguoiTimViec();
		nguoiTimViec.setUser(user);
		nguoiTimViec.setAvatar(avatarFileName);
		nguoiTimViec.setSkill(skill);
		nguoiTimViecService.save(nguoiTimViec);

		return new ResponseEntity<>(MessageResponse.builder().message("Register User Success")
				.code(HttpStatus.OK.value()).data(nguoiTimViec).build(), HttpStatus.OK);
	}

	@PostMapping("/register/company")
	@ResponseBody
	public ResponseEntity<MessageResponse> registerCompany(@ModelAttribute NhaTuyenDung nhaTuyenDung,
			@RequestParam("companyLogo") MultipartFile companyLogo, Errors errors) {

		// Validate input
		if (errors.hasErrors()) {
			return new ResponseEntity<>(MessageResponse.builder().message("Register Company Fail")
					.code(HttpStatus.BAD_REQUEST.value()).build(), HttpStatus.BAD_REQUEST);
		}

		// Check email
		if (checkEmail(nhaTuyenDung.getCompanyEmail())) {
			return new ResponseEntity<>(
					MessageResponse.builder().message("Email Existed").code(HttpStatus.BAD_REQUEST.value()).build(),
					HttpStatus.BAD_REQUEST);
		}

		// Handle company logo file upload
		String logoFileName = null;
		if (companyLogo != null && !companyLogo.isEmpty()) {
			try {
				logoFileName = companyLogo.getOriginalFilename();
				Path path = Paths.get(uploadDir + logoFileName);
				Files.write(path, companyLogo.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
				return new ResponseEntity<>(
						MessageResponse.builder().message("File upload failed")
								.code(HttpStatus.INTERNAL_SERVER_ERROR.value()).build(),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		// Save user with photo
		User user = userService.save(User.builder().email(nhaTuyenDung.getCompanyEmail())
				.password(passwordEncoder.encode(nhaTuyenDung.getPassword())).role(RoleEnum.COMPANY.toString())
				.phone(nhaTuyenDung.getCompanyPhone()).address(nhaTuyenDung.getCompanyAddress())
				.name(nhaTuyenDung.getCompanyName()).photo(logoFileName) // Save logo filename as photo
				.build());

		// Save company with logo filename
		nhaTuyenDungService.save(NhaTuyenDung.builder().companyName(nhaTuyenDung.getCompanyName())
				.companyAddress(nhaTuyenDung.getCompanyAddress()).companyEmail(nhaTuyenDung.getCompanyEmail())
				.companyWebsite(nhaTuyenDung.getCompanyWebsite()).companyPhone(nhaTuyenDung.getCompanyPhone())
				.logoFileName(logoFileName) // Add logo filename to the entity
				.user(user).build());

		return new ResponseEntity<>(MessageResponse.builder().message("Register Company Success")
				.code(HttpStatus.OK.value()).data(nhaTuyenDung).build(), HttpStatus.OK);
	}

	@GetMapping("/reset-password")
	public String resetPasswordForm() {
		return "reset-password.html";
	}

	@PostMapping("/reset-password/{email}")
	@ResponseBody
	public ResponseEntity<MessageResponse> resetPassword(@PathVariable String email) {
		if (email.isEmpty()) {
			return new ResponseEntity<>(MessageResponse.builder().message("Email không hợp lệ!")
					.code(HttpStatus.BAD_REQUEST.value()).build(), HttpStatus.BAD_REQUEST);
		}
		try {
			String passRandom = Common.randomCodeMail();
			User account = userService.findByEmail(email);
			if (account == null) {
				return new ResponseEntity<>(MessageResponse.builder().message("Email này chưa đăng ký tài khoản!")
						.code(HttpStatus.BAD_REQUEST.value()).build(), HttpStatus.BAD_REQUEST);
			}
			account.setPassword(passwordEncoder.encode(passRandom));
			userService.update(account);
			mailService.send(email.trim(), "Mail xác thực tài khoản từ Recruitment",
					"<div style='width:80%; margin:0 auto;text-align: center;'>\n"
							+ "    <h1 style='color:#080202;'>Website Recruitment</h1>\n"
							+ "    <p>Dùng mã này để xác minh địa chỉ email của bạn trên Recruitment </p>\n"
							+ "    <h5>Mật khẩu mới</h5>" + "<h2 style='color: #116D6E;'>" + passRandom + "</h2>"
							+ "      <br>"
							+ "    <p style='font-size: 15px;font-weight: 200;'>Tin nhắn này được gửi tới bạn theo yêu cầu của Recruitment.\n"
							+ "      recruitment@gmail.com.vn © 2024 All rights reserved. Privacy Policy|T&C|System Status</p>\n"
							+ "  </div>");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					MessageResponse.builder().message("Gửi mail thất bại, kiểm tra email có còn hoạt động không!")
							.code(HttpStatus.BAD_REQUEST.value()).build(),
					HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(MessageResponse.builder().message("Vui lòng kiểm tra email để xem mật khẩu mới!")
				.code(HttpStatus.OK.value()).build(), HttpStatus.OK);
	}

	@GetMapping("/logout/success")
	public String logoutSuccess(HttpServletRequest request) {
	    HttpSession session = request.getSession(false);
	    if (session != null) {
	        session.removeAttribute("cart"); // Xóa giỏ hàng khỏi session
	        session.invalidate(); // Xóa toàn bộ session
	    }
	    return "redirect:/auth/login/form";
	}

	@GetMapping("/access-denied")
	@ResponseBody
	public String accessDenied() {
		return "redirect:/auth/login/form";
	}

	@GetMapping("/change-password/form")
	public String changePassword() {
		return "change-password";
	}

	@PostMapping("/change-password")
	public String handleChangePassword(@RequestParam String password, @RequestParam("new-password") String newPass,
			Model model) {
		String email = (String) sessionUtil.getSessionId("userSessionEmail");
		User user = userService.findByEmail(email);
		if (user == null) {
			model.addAttribute("errors", "Email không tồn tại!");
			return "accountmanager";
		}

		if (!passwordEncoder.matches(password, user.getPassword())) {
			model.addAttribute("errors", "Mật khẩu cũ không đúng!");
			return "accountmanager";
		}

		user.setPassword(passwordEncoder.encode(newPass));
		userService.update(user);
		model.addAttribute("success", "Đổi mật khẩu thành công!");
		return "redirect:/index";
	}

	@PostMapping("/upload")
	public String uploadFile(@RequestParam("file") MultipartFile file, Model model) {
		try {
			// Kiểm tra loại file
			String contentType = file.getContentType();
			if (!contentType.startsWith("image/")) {
				model.addAttribute("error", "Chỉ cho phép tải lên file ảnh.");
				return "accountmanager";
			}

			// Kiểm tra kích thước file
			long maxFileSize = 5 * 1024 * 1024; // 5MB
			if (file.getSize() > maxFileSize) {
				model.addAttribute("error", "Kích thước file vượt quá giới hạn cho phép 5MB.");
				return "accountmanager";
			}

			// Tạo thư mục nếu chưa tồn tại
			Path directoryPath = Paths.get(uploadDir);
			if (!Files.exists(directoryPath)) {
				Files.createDirectories(directoryPath);
			}

			// Lưu file
			String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
			Path path = Paths.get(uploadDir + fileName);
			Files.write(path, file.getBytes());

			model.addAttribute("success", "Tải lên thành công: " + fileName);
		} catch (IOException e) {
			model.addAttribute("error", "Tải lên thất bại: " + e.getMessage());
		}
		return "accountmanager";
	}

	private boolean checkEmail(String mail) {
		return userService.findByEmail(mail) != null;
	}

	@GetMapping("/profile/photo/{userID}")
	public ResponseEntity<byte[]> getProfilePhoto(@PathVariable Long userID) {
		Optional<User> optionalUser = userService.findById(userID);
		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			String photoFileName = user.getPhoto(); // Lấy tên file từ thuộc tính photo
			if (photoFileName != null && !photoFileName.isEmpty()) {
				try {
					Path path = Paths.get(uploadDir + photoFileName); // Đường dẫn tới file ảnh
					if (Files.exists(path)) {
						byte[] photoBytes = Files.readAllBytes(path);
						HttpHeaders headers = new HttpHeaders();
						// Xác định loại MIME dựa trên phần mở rộng file
						String mimeType = Files.probeContentType(path);
						headers.setContentType(mimeType != null ? MediaType.parseMediaType(mimeType)
								: MediaType.APPLICATION_OCTET_STREAM);
						return new ResponseEntity<>(photoBytes, headers, HttpStatus.OK);
					} else {
						return new ResponseEntity<>(HttpStatus.NOT_FOUND);
					}
				} catch (IOException e) {
					e.printStackTrace();
					return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@GetMapping("/company/logo/{employerID}")
	public ResponseEntity<byte[]> getCompanyLogo(@PathVariable Long employerID) {
		Optional<NhaTuyenDung> optionalCompany = nhaTuyenDungService.findById(employerID);
		if (optionalCompany.isPresent()) {
			NhaTuyenDung company = optionalCompany.get();
			String logoFileName = company.getLogoFileName(); // Lấy tên file từ thuộc tính logoFileName
			if (logoFileName != null && !logoFileName.isEmpty()) {
				Path logoPath = Paths.get(uploadDir + logoFileName);
				try {
					if (Files.exists(logoPath)) {
						byte[] imageBytes = Files.readAllBytes(logoPath);
						HttpHeaders headers = new HttpHeaders();
						// Xác định loại MIME dựa trên phần mở rộng file
						String mimeType = Files.probeContentType(logoPath);
						headers.setContentType(mimeType != null ? MediaType.parseMediaType(mimeType)
								: MediaType.APPLICATION_OCTET_STREAM);
						return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
					} else {
						return new ResponseEntity<>(HttpStatus.NOT_FOUND);
					}
				} catch (IOException e) {
					e.printStackTrace();
					return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

}
