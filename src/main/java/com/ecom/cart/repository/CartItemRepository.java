package com.ecom.cart.repository;

import com.ecom.cart.domain.Cart;
import com.ecom.cart.domain.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {


   // @Query("DELETE FROM CartItem ci WHERE ci.productId= ?1 AND ci.cart = ?2")
    void deleteByProductIdAndCart(Long productId,Cart cart);

    //@Query("SELECT ci from CartItem ci WHERE ci.productId= ?1 AND ci.cart = ?2")
    CartItem findByProductIdAndCart(Long productid, Cart cart);

    CartItem findByCart(Cart cart);
}
