package com.ecom.user.repository;

import com.ecom.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {


    //Optional<User> findByUserName(String username);

}
