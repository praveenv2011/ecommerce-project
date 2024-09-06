package com.ecom.order.dto;

import com.ecom.order.domain.Payment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    private Long orderId;
    private Long userId;
    private List<OrderItemDTO> orderItems = new ArrayList<>();
    private String address;
    private LocalDate orderDate;
    private Payment payment;
    private Double totalPrice;
    private String orderStatus;
}
