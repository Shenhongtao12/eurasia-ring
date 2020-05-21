package com.sht.eurasiaring.repository;

import com.sht.eurasiaring.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {

    public User findByOpenid(String openid);
}
