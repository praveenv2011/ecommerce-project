package com.ecom.cart.feignclient;

import com.ecom.cart.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user",url = "http://localhost:8082")
public interface UserClient {

    @GetMapping("/api/users/{id}")
    UserDTO getUserByid(@PathVariable("id") Long id);

    @PutMapping("/api/users/{id}")
    UserDTO updateUserByid(@RequestBody UserDTO userDTO, @PathVariable("id") Long id);
}
