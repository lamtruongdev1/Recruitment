package com.poly.Recruitment.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.poly.Recruitment.entity.CV;
import com.poly.Recruitment.entity.Cart;
import com.poly.Recruitment.entity.User;
import com.poly.Recruitment.repository.CVDAO;
import com.poly.Recruitment.repository.CartDAO;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class CartService {

    @Autowired
    private CVDAO cvRepository;

    @Autowired
    private CartDAO cartRepository;

    public ResponseEntity<String> addCvToCart(Long cvId, User user) {
        // Kiểm tra sự tồn tại của CV
        Optional<CV> cvOpt = cvRepository.findById(cvId);
        if (cvOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("CV không tồn tại");
        }

        CV cv = cvOpt.get();

        // Tìm hoặc tạo giỏ hàng cho người dùng
        Cart cart = cartRepository.findByUser(user);
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cart = cartRepository.save(cart);
        }

        // Thêm CV vào giỏ hàng và lưu giỏ hàng
        cart.getCvs().add(cv);
        cartRepository.save(cart);

        // Logging và phản hồi
        System.out.println("Added CV with ID " + cvId + " to cart for user with ID " + user.getUserID());
        return ResponseEntity.ok("CV đã được thêm vào giỏ hàng");
    }
    
    public void removeCvFromAllCarts(Long cvId) {
        // Tìm CV theo ID
        Optional<CV> cvOpt = cvRepository.findById(cvId);
        if (!cvOpt.isPresent()) {
            throw new RuntimeException("CV không tồn tại");
        }

        CV cv = cvOpt.get();
        // Tìm tất cả các giỏ hàng chứa CV
        List<Cart> cartsWithCv = cartRepository.findByCvs(cv);

        // Xóa CV khỏi tất cả các giỏ hàng
        for (Cart cart : cartsWithCv) {
            cart.getCvs().remove(cv);
            cartRepository.save(cart);
        }
    }
    

}
