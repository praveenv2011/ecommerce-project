package com.ecom.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {

    private  Long cartItemId;

    private Long cartId;

    private Long prouctId;

    private Integer quantity;

    private double discount;

    private double specialPrice;

}
