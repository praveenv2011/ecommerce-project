package com.ecom.order.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "paymentid")
    private Long paymentId;

    @JsonIgnore
    @OneToOne(mappedBy = "payment", cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    private Order order;

    @NotBlank
    @Column(name="paymentmethod")
    private String paymentMethod;

    @Column(name = "pg_payment_id")
    private String pgPaymentId;

    @Column(name = "pg_status")
    private String pgStatus;

    @Column(name = "pg_response_message")
    private String pgResponseMessage;

    @Column(name="pg_name")
    private String pgName;

    public Payment(String paymentMethod, String pgPaymentId, String pgStatus, String pgResponseMessage, String pgName) {
        this.paymentMethod = paymentMethod;
        this.pgPaymentId = pgPaymentId;
        this.pgStatus = pgStatus;
        this.pgResponseMessage = pgResponseMessage;
        this.pgName=pgName;
    }
}
