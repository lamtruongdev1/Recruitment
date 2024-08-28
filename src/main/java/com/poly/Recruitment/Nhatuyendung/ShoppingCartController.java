package com.poly.Recruitment.Nhatuyendung;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ShoppingCartController {

    @RequestMapping("/cart/view")
    public String viewCart() {
        return "cart_nhatuyendung/cart";  // Trỏ đến cart.html trong thư mục cart_nhatuyendung
    }

    @RequestMapping("/products")
    public String viewProducts() {
        return "cart_nhatuyendung/products";  // Trỏ đến products.html trong thư mục cart_nhatuyendung
    }
}