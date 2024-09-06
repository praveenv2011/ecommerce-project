package com.ecom.user.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.action.internal.OrphanRemovalAction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userid")
    private Long userId;

    @NotBlank
    @Size(min = 3, max = 20, message = "Username should be between 3 and 20 characters")
    @Column(name = "username")
    private String username;

    @NotBlank
    @Email
    @Column(name = "email")
    private String email;

    @NotBlank
    @Size(min = 8, max = 16)
    @Column(name = "password")
    private String password;

    @OneToMany( mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE,CascadeType.REMOVE},orphanRemoval = true)
    private List<Address> addresses = new ArrayList<>();


    @Column(name = "cart_id")
    private long cartId;

    public User() {
    }

    public User(List<Address> addressIds, long cartId, String email, String password, Long userId, String username) {
        this.addresses = addressIds;
        this.cartId = cartId;
        this.email = email;
        this.password = password;
        this.userId = userId;
        this.username = username;
    }


    public @NotBlank @Email String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank @Email String email) {
        this.email = email;
    }

    public @NotBlank @Size(min = 8, max = 16) String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank @Size(min = 8, max = 16) String password) {
        this.password = password;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public @NotBlank @Size(max = 20) String getUsername() {
        return username;
    }

    public void setUsername(@NotBlank @Size(max = 20) String username) {
        this.username = username;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public long getCartId() {
        return cartId;
    }

    public void setCartId(long cartId) {
        this.cartId = cartId;
    }

    public void addAddress(Address address){
        addresses.add(address);
    }
}
