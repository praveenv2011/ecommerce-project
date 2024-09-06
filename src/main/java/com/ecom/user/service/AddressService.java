package com.ecom.user.service;

import com.ecom.user.domain.Address;
import com.ecom.user.domain.User;
import com.ecom.user.dto.AddressDTO;
import com.ecom.user.exception.ResourceNotFoundException;
import com.ecom.user.repository.AddressRepository;
import com.ecom.user.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    public AddressDTO save(Long userId, AddressDTO addressDTO) {
        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("user","userid",userId));

        Address address = modelMapper.map(addressDTO,Address.class);

        address.setUser(user);
        Address savedAddress = addressRepository.save(address);

//        adding this user to address base
        user.addAddress(address);
        userRepository.save(user);

        return modelMapper.map(savedAddress,AddressDTO.class);
    }

    public List<AddressDTO> getAll() {

        List<Address> addressList = addressRepository.findAll();
        List<AddressDTO> addressDTOList = addressList.stream().map(address -> modelMapper.map(address,AddressDTO.class)).toList();
       return addressDTOList;
    }

    public AddressDTO getById(Long addressId) {

        Address address = addressRepository.findById(addressId).orElseThrow(()-> new ResourceNotFoundException("addressId","addressId",addressId));
        return modelMapper.map(address,AddressDTO.class);
    }

    public List<AddressDTO> getAllByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("user","userid",userId));

        List<Address> addressList = user.getAddresses();

        List<AddressDTO> addressDTOList = addressList.stream().map(address -> modelMapper.map(address,AddressDTO.class)).toList();

        return addressDTOList;
    }

    public AddressDTO updateById(Long userId, Long addressId,AddressDTO addressDTO) {
        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("user","userid",userId));

        Address address = addressRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("address","addressid",addressId));

        address.setUser(user);
        address.setCountry(addressDTO.getCountry());
        address.setState(addressDTO.getState());
        address.setCity(addressDTO.getCity());
        address.setPincode(addressDTO.getPincode());
        address.setBuildingName(addressDTO.getBuildingName());
        address.setStreet(addressDTO.getStreet());

        Address updatedAddress = addressRepository.save(address);

        //REMOVING THE EXISITNG ADDRESS AND ADDING THE UPDATED ADDRESS
        user.getAddresses().removeIf(address1 -> address1.getAddressId().equals(addressId));
        user.getAddresses().add(updatedAddress);
        userRepository.save(user);

        return modelMapper.map(address, AddressDTO.class);
        
    }

    public String deleteById(Long userId, Long addressId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("user","userid",userId));

        Address address = addressRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("address","addressid",addressId));

        //REMOVING THAT ADDRESS FROM THE USER
        user.getAddresses().removeIf(address1 -> address1.getAddressId().equals(addressId));
        userRepository.save(user);

        addressRepository.deleteById(addressId);

        return "address removed successfully from the user with id"+" "+addressId;
    }
}
