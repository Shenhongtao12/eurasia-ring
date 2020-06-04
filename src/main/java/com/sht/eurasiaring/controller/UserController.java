package com.sht.eurasiaring.controller;

import com.sht.eurasiaring.entity.User;
import com.sht.eurasiaring.service.FansService;
import com.sht.eurasiaring.service.UserService;
import com.sht.eurasiaring.utils.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Hongtao Shen
 * @date 2020/5/16 - 13:48
 **/
@RestController
@RequestMapping("ring/user")
public class UserController extends BaseController{
    @Autowired
    private UserService userService;
    @Autowired
    private FansService fansService;

    @PostMapping("login")
    public ResponseEntity<JsonData> login(@RequestBody User user) {
        Map<String, Object> result = userService.login(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(JsonData.buildSuccess(result,"登录成功"));
    }

    @GetMapping("init")
    public ResponseEntity<JsonData> init(){
        return ResponseEntity.ok(JsonData.buildSuccess(userService.init(), "初始化数据"));
    }

    @GetMapping()
    public ResponseEntity<Integer> getMessage(){
        return ResponseEntity.ok(userService.getMessage(userId));
    }

    @GetMapping("{id}")
    public ResponseEntity<JsonData> findById(@PathVariable Integer id){
        Map<String, Object> fans = new HashMap<>();
        //我关注的数量
        int Num1 = fansService.countNum("fans", userId);
        //粉丝的数量
        int Num2 = fansService.countNum("attention", userId);
        fans.put("fans", Num2);
        fans.put("attention", Num1);
        fans.put("user", userService.findUserById(id));
        return ResponseEntity.ok(JsonData.buildSuccess(fans, ""));
    }
}
