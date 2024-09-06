package com.ecom.order.controller;


import com.ecom.order.dto.OrderDTO;
import com.ecom.order.dto.OrderRequestDTO;
import com.ecom.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/users/{userid}/orders/payments/{paymentMethod}")
    public ResponseEntity<OrderDTO> orderProducts(@PathVariable("userid") Long userid,
                                                  @PathVariable("paymentMethod") String paymentMethod,
                                                  @RequestBody OrderRequestDTO orderRequestDTO){
        OrderDTO order = orderService.placeOrder(
                userid,
                orderRequestDTO.getAddressId(),
                paymentMethod,
                orderRequestDTO.getPgName(),
                orderRequestDTO.getPgPaymentId(),
                orderRequestDTO.getPgStatus(),
                orderRequestDTO.getPgResponseMessage());

        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @GetMapping("/users/{id}/orders")
    public ResponseEntity<List<OrderDTO>> getOrdersByUserId(@PathVariable("id") Long userid) {

        List<OrderDTO> orders = orderService.getByUserId(userid);

        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
}
