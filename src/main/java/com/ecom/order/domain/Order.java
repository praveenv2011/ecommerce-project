package com.ecom.order.domain;

import com.ecom.order.dto.AddressDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderid")
    private Long orderId;


    @Column(name="user_id",nullable = false)
    private Long userId;

    @OneToMany(mappedBy = "order",cascade = {CascadeType.PERSIST,CascadeType.MERGE},orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Column(name = "address_id")
    private Long addressId;

    private LocalDate orderDate;

    @OneToOne
    @JoinColumn(name = "payment")
    private Payment payment;

    @Column(name = "totalprice")
    private Double totalPrice;

    @Column(name="orderstatus")
    private String orderStatus;


}
