package com.sht.eurasiaring.controller;

import com.sht.eurasiaring.entity.User;
import com.sht.eurasiaring.service.UserService;
import com.sht.eurasiaring.utils.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author Hongtao Shen
 * @date 2020/5/16 - 13:48
 **/
@RestController
@RequestMapping("ring/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("login")
    public ResponseEntity<JsonData> login(@RequestBody User user) {
        Map<String, Object> result = userService.login(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(JsonData.buildSuccess(result,"登录成功"));
    }

    @GetMapping("init")
    public ResponseEntity<JsonData> init(){
        return ResponseEntity.ok(JsonData.buildSuccess(userService.init(), "初始化数据"));
    }
}
