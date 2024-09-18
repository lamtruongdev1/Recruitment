package com.poly.Recruitment.Nguoitimviec;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poly.Recruitment.dto.CartDTO;
import com.poly.Recruitment.entity.User;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart")
public class CartController {


	   @Autowired
	    private RestTemplate restTemplate;
	
	    
//	@RequestMapping()
//	public String viewCart() {
//		return "NguoiTimViec/cart/cart";
//	}
	
	   @RequestMapping
	   public String viewCart( Model model) {
	
	       return "NguoiTimViec/cart/cart";
	   }

	   
	   @RequestMapping("/delete/{cvId}")
	    public String deleteCvFromCart(@PathVariable Long cvId, HttpSession session, Model model) {
	        // Lấy đối tượng User từ session với khóa "ACCOUNT_SESSION"
	        User user = (User) session.getAttribute("ACCOUNT_SESSION");

	        if (user == null) {
	            model.addAttribute("message", "Bạn cần đăng nhập để thực hiện thao tác này");
	            return "redirect:/login";
	        }

	        String url = "http://localhost:8080/api/cart/remove/" + cvId;

	        try {
	            // Gọi API xóa CV khỏi giỏ hàng
	            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, null, String.class);

	            if (response.getStatusCode().is2xxSuccessful()) {
	                model.addAttribute("message", "CV đã được xóa khỏi giỏ hàng");
	            } else {
	                model.addAttribute("message", "Lỗi: " + response.getBody());
	            }
	        } catch (Exception e) {
	            model.addAttribute("message", "Đã xảy ra lỗi khi xóa CV khỏi giỏ hàng: " + e.getMessage());
	        }

	        return "redirect:/cart"; // Redirect về trang giỏ hàng sau khi xóa
	    }
}