package com.ecom.user.service;

import com.ecom.user.domain.User;
import com.ecom.user.dto.UserDTO;
import com.ecom.user.exception.ApiException;
import com.ecom.user.exception.ResourceNotFoundException;
import com.ecom.user.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    public UserDTO save(UserDTO userDTO) {

        List<User> userList = userRepository.findAll();
        boolean flag = userList.stream().anyMatch((user)->user.getUsername().equals(userDTO.getUsername()));
        if(flag){
            throw new  ApiException("username is already exists");
        }
        User user = modelMapper.map(userDTO,User.class);
        User savedUser = userRepository.save(user);

        return modelMapper.map(savedUser,UserDTO.class);
    }

    public UserDTO updateById(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("User","Userid",id));

        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setCartId(userDTO.getCartId());

        return modelMapper.map(userRepository.save(user),UserDTO.class);
    }

    public UserDTO getById(Long id) {
        User user = userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("User","Userid",id));
        return modelMapper.map(user,UserDTO.class);
    }

    public void deleteById(Long id) {
        User user = userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("User","Userid",id));
        userRepository.delete(user);
    }
}
