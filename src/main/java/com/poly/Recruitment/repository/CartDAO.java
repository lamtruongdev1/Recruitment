package com.poly.Recruitment.repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.poly.Recruitment.entity.CV;
import com.poly.Recruitment.entity.Cart;
import com.poly.Recruitment.entity.User;

import jakarta.transaction.Transactional;


@Repository
public interface CartDAO extends JpaRepository<Cart, Long> {
	 Cart findByUser(User user);
	 @Query("SELECT c FROM Cart c WHERE c.user.id = :userId")
	 Cart findByUserId(@Param("userId") Long userId);
	 @Query(value = "SELECT c.cart_id AS cartId, " +
             "u.user_id AS userId, " +
             "cv.idcv AS id, " +
             "cv.name, " +
             "cv.description, " +
             "cv.fileurl AS fileUrl, " +
             "cv.price " +  // Thêm trường price vào câu truy vấn
             "FROM Cart c " +
             "JOIN [User] u ON c.user_id = u.user_id " +
             "JOIN cart_cv cc ON c.cart_id = cc.cart_id " +
             "JOIN CV cv ON cc.cv_id = cv.idcv", 
     nativeQuery = true)
List<Object[]> findAllCartsWithCvs();

List<Cart> findByCvs(CV cv);

}