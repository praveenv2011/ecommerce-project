package com.ecom.cart.repository;

import com.ecom.cart.domain.Cart;
import com.ecom.cart.dto.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart,Long> {

    Cart findByUserId(Long id);

    //Cart findByUser(UserDTO user);
}
