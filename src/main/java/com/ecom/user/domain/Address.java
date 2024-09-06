package com.ecom.user.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="Address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "addressid")
    private Long addressId;

    @NotBlank
    @Size(min = 3)
    @Column(name = "street")
    private String street;

    @NotBlank
    @Size(min = 3)
    @Column(name = "buildingname")
    private String buildingName;

    @NotBlank
    @Size(min = 3)
    @Column(name = "city")
    private String city;

    @NotBlank
    @Size(min = 3)
    @Column(name = "state")
    private String state;

    @NotBlank
    @Size(min = 3)
    @Column(name = "country")
    private String country;

    @NotBlank
    @Size(min = 6)
    @Column(name = "pincode")
    private String pincode;


    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

}
