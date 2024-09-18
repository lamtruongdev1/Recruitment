package com.poly.Recruitment.Nguoitimviec;



import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;



@Controller
public class Checkout {


    
	@RequestMapping("/checkout")
	public String about() {
		return "NguoiTimViec/checkout/checkout";
	}
    

}
