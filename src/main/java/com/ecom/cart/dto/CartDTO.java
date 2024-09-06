package com.ecom.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {

    private Long cartId;

    private Long userId;

    //private List<CartItemDTO> cartItemList;

    private List<ProductDTO> productinforamtion = new ArrayList<>();

    private Double totalPrice;

    public void addProducts(ProductDTO productDTO){
        productinforamtion.add(productDTO);
    }
}
