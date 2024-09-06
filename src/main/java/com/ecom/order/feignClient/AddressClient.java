package com.ecom.order.feignClient;

import com.ecom.order.dto.AddressDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@FeignClient(name = "user",url = "http://localhost:8082")
public interface AddressClient {

    @GetMapping("/api/users/{userid}/addresses")
    List<AddressDTO> getAddressByUserId(@PathVariable("userid") Long userId);

    @GetMapping("/api/addresses/{addressid}")
    Optional<AddressDTO> getAddressById(@PathVariable("addressid") Long addressId);
}
