package com.sht.eurasiaring.dao;

import com.sht.eurasiaring.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserDao extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    public User findByOpenid(String openid);
}
