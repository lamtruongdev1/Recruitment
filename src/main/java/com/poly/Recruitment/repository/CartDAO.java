package com.poly.Recruitment.repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.poly.Recruitment.entity.CV;
import com.poly.Recruitment.entity.Cart;
import com.poly.Recruitment.entity.User;

@Repository
public interface CartDAO extends JpaRepository<Cart, Long> {
    
    // Find cart by User object
    Cart findByUser(User user);
    Cart findByUser_UserID(Long userId);
    @Query("SELECT COUNT(cv.idCV) FROM Cart c JOIN c.cvs cv WHERE c.user.id = :userId")
    Long countCVsByUserId(@Param("userId") Long userId);
    
    
    // Find cart by userId
    @Query("SELECT c FROM Cart c WHERE c.user.id = :userId")
    Cart findByUserId(@Param("userId") Long userId);
    
    // Find carts containing a specific CV
    List<Cart> findByCvs(CV cv);
    
    // Get all carts with CVs
    @Query(value = "SELECT c.cart_id AS cartId, " +
                   "u.user_id AS userId, " +
                   "cv.idcv AS id, " +
                   "cv.name, " +
                   "cv.description, " +
                   "cv.fileurl AS fileUrl, " +
                   "cv.price " +
                   "FROM Cart c " +
                   "JOIN [User] u ON c.user_id = u.user_id " +
                   "JOIN cart_cv cc ON c.cart_id = cc.cart_id " +
                   "JOIN CV cv ON cc.cv_id = cv.idcv", 
           nativeQuery = true)
    List<Object[]> findAllCartsWithCvs();
   
}
