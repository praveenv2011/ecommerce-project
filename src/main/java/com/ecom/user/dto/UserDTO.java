package com.ecom.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long userId;

    private String username;

    private String email;

    private String password;

    private long cartId;

    //private List<Address> addresses = new ArrayList<>();


}
