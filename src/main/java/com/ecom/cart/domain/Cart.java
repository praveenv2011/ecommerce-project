package com.ecom.cart.domain;

import com.ecom.cart.dto.UserDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "cart")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {

    @Column(name = "cart_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    @Column(name = "user_id",nullable = false)
    private Long userId;

    @OneToMany(mappedBy = "cart", cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REMOVE}, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

    @Column(name = "totalprice",nullable = false)
    private Double totalPrice =0.0;

//    public void addCartItems(CartItem cartItem){
//        cartItems.add(cartItem);
//    }
}
