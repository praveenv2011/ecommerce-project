package com.ecom.user.controller;

import com.ecom.user.dto.AddressDTO;
import com.ecom.user.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping("/users/{userid}/addresses")
    public ResponseEntity<AddressDTO> saveAddress(@PathVariable("userid") Long userid,
                                                  @Valid @RequestBody AddressDTO addressDTO){
          AddressDTO savedAddressDTO = addressService.save(userid, addressDTO);

          return new ResponseEntity<>(savedAddressDTO, HttpStatus.CREATED);
    }

    @GetMapping("/addresses")
    public ResponseEntity<List<AddressDTO>> getAllAddresses(){
        List<AddressDTO> addressDTOList = addressService.getAll();
        return new ResponseEntity<>(addressDTOList, HttpStatus.OK);
    }

    @GetMapping("/addresses/{addressid}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable("addressid") Long addressId){
        AddressDTO addressDTO = addressService.getById(addressId);
        return new ResponseEntity<>(addressDTO, HttpStatus.OK);
    }
    @GetMapping("/users/{userid}/addresses")
    public ResponseEntity<List<AddressDTO>> getAddressesByUserId(@PathVariable("userid") Long userId){
        List<AddressDTO> addressDTOList = addressService.getAllByUserId(userId);
        return new ResponseEntity<>(addressDTOList,HttpStatus.OK);
    }

    @PutMapping("/users/{userid}/addresses/{addressid}")
    public ResponseEntity<AddressDTO> updateAddressById(@PathVariable("userid") Long userId,
                                                        @PathVariable("addressid") Long addressId,
                                                         @RequestBody AddressDTO addressDTO){
        AddressDTO savedAddressDTO = addressService.updateById(userId,addressId,addressDTO);
        return new ResponseEntity<>(savedAddressDTO,HttpStatus.OK);
    }

    @DeleteMapping("/users/{userid}/addresses/{addressid}")
    public ResponseEntity<String> deleteAddressById(@PathVariable("userid") Long userId,
                                                        @PathVariable("addressid") Long addressId){
        String status = addressService.deleteById(userId,addressId);
        return new ResponseEntity<>(status,HttpStatus.OK);
    }
}
