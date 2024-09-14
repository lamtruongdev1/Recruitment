package com.poly.Recruitment.api;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.poly.Recruitment.dto.CartDTO;
import com.poly.Recruitment.dto.CvDto;
import com.poly.Recruitment.entity.CV;
import com.poly.Recruitment.entity.Cart;
import com.poly.Recruitment.entity.User;
import com.poly.Recruitment.repository.CVDAO;
import com.poly.Recruitment.repository.CartDAO;
import com.poly.Recruitment.repository.UserDAO;
import com.poly.Recruitment.service.CartService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/cart")
public class CartRestController {

    @Autowired
    private CartDAO cartRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private CVDAO cvRepository;

    @Autowired
    private UserDAO userRepository;
    
    @GetMapping("/total-cv")
    public ResponseEntity<Integer> getTotalCVInCart() {
        int totalCV = cartRepository.countTotalCVInCart();
        return ResponseEntity.ok(totalCV);
    }
    
    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestParam Long cvId, HttpSession session) {
        // Lấy đối tượng User từ session với khóa "ACCOUNT_SESSION"
        User user = (User) session.getAttribute("ACCOUNT_SESSION");
        
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bạn cần đăng nhập để thêm CV vào giỏ hàng");
        }

        Long userId = user.getUserID();

        // Kiểm tra xem CV có tồn tại không
        Optional<CV> cvOpt = cvRepository.findById(cvId);
        if (!cvOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("CV không tồn tại");
        }

        CV cv = cvOpt.get();

        // Tìm giỏ hàng cho người dùng, nếu không có thì tạo mới
        Cart cart = cartRepository.findByUser(user);
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cartRepository.save(cart);
        }

        // Kiểm tra xem CV đã có trong giỏ hàng chưa
        if (cart.getCvs().contains(cv)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("CV đã có trong giỏ hàng");
        }

        // Thêm CV vào giỏ hàng
        cart.getCvs().add(cv);
        cartRepository.save(cart);

        return ResponseEntity.ok("CV đã được thêm vào giỏ hàng");
    }
    
    @GetMapping
    public ResponseEntity<List<CartDTO>> getAllCarts() {
        List<Object[]> results = cartRepository.findAllCartsWithCvs();

        if (results.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        // Group results by cartId
        Map<Long, List<Object[]>> groupedResults = results.stream()
            .collect(Collectors.groupingBy(result -> (Long) result[0]));

        // Convert to CartDTO list
        List<CartDTO> cartDTOs = groupedResults.entrySet().stream().map(entry -> {
            Long cartId = entry.getKey();
            List<Object[]> items = entry.getValue();
            Long userId = (Long) items.get(0)[1];

            List<CvDto> cvDTOs = items.stream().map(item -> new CvDto(
                (Long) item[2],
                (String) item[3],
                (String) item[4],
                (String) item[5],
                (BigDecimal) item[6]  // Đảm bảo rằng đây là giá trị price
            )).collect(Collectors.toList());

            return new CartDTO(cartId, userId, cvDTOs);
        }).collect(Collectors.toList());

        return ResponseEntity.ok(cartDTOs);

}
    
    @DeleteMapping("/remove/{cvId}")
    public ResponseEntity<String> removeCvFromAllCarts(@PathVariable Long cvId) {
        // Kiểm tra xem CV có tồn tại không
        Optional<CV> cvOpt = cvRepository.findById(cvId);
        if (!cvOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("CV không tồn tại");
        }

        // Xóa CV khỏi tất cả các giỏ hàng mà nó thuộc về
        cartService.removeCvFromAllCarts(cvId);

        return ResponseEntity.ok("CV đã được xóa khỏi tất cả các giỏ hàng");
    }
 
}

