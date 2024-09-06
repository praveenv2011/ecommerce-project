package com.ecom.cart.controller;

import com.ecom.cart.dto.CartDTO;
import com.ecom.cart.dto.CartItemDTO;
import com.ecom.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/users/{userId}/carts/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> addProductToCart(@PathVariable(name="userId") Long userId,
                                                    @PathVariable(name="productId") Long productId,
                                                    @PathVariable(name="quantity") Integer quantity){

        CartDTO cartDTO = cartService.saveProduct(userId,productId,quantity);

        return new ResponseEntity<>(cartDTO, HttpStatus.CREATED);
    }

    @GetMapping("/users/{userid}/cart")
    public ResponseEntity<CartDTO> getCartByUserId(@PathVariable("userid") Long userId){

        CartDTO cartDTO = cartService.getById(userId);
        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }

    @PutMapping("/users/{userid}/carts/products/{productid}/{quantity}")
    public ResponseEntity<CartDTO> updateCartProductByProductId(@PathVariable("userid") Long userid,
                                                                @PathVariable("productid") Long productid,
                                                                @PathVariable("quantity") String quantity){
        CartDTO cartDTO = cartService.updateCartProductQuantity(userid,productid,
                 quantity.equalsIgnoreCase("reduce")?-1:1);

        return new ResponseEntity<>(cartDTO,HttpStatus.OK);
    }

    @DeleteMapping("/users/{userid}/carts/products/{productid}")
    public ResponseEntity<String> deleteProductByProductId(@PathVariable("userid") Long userid,
                                                            @PathVariable("productid") Long productid){

        String update = cartService.deleteProduct(userid, productid);

        return new ResponseEntity<>(update,HttpStatus.OK);
    }

    //for the sake of if product is updated or deleted in the database all carts that have that product should be modified so this happpens in product service
    @GetMapping("/carts")
        public ResponseEntity<List<CartDTO>> getAllCarts(){
            List<CartDTO> cartDTOS = cartService.getAll();

            return ResponseEntity.ok().body(cartDTOS);
        }

}
