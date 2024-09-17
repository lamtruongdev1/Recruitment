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
import com.poly.Recruitment.repository.UserDAO;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class CartService {

    @Autowired
    private CVDAO cvRepository;

    @Autowired
    private CartDAO cartRepository;
    @Autowired
    private UserDAO userRepository; // Inject UserDAO

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
    public Long getTotalCV(Long userId) {
        // Lấy tất cả các CV trong giỏ hàng của người dùng và đếm số lượng
        Cart cart = cartRepository.findByUserId(userId);
        if (cart != null) {
            return (long) cart.getCvs().size();
        }
        return 0L;
    }
    
    
    
    
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUser_UserID(userId); // Lấy giỏ hàng của user từ CartDAO
    }
    public List<Cart> getCartsByUserId(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new EntityNotFoundException("User not found with ID: " + userId);
        }

        User user = userOpt.get();
        Cart cart = cartRepository.findByUser(user);
        return (cart != null) ? List.of(cart) : List.of(); // Return as a list or adjust logic as needed
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