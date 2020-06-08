package com.sht.eurasiaring.controller;

import com.sht.eurasiaring.entity.User;
import com.sht.eurasiaring.service.FansService;
import com.sht.eurasiaring.service.UserService;
import com.sht.eurasiaring.utils.JsonData;
import com.sht.eurasiaring.utils.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
@Api(tags = "用户服务")
public class UserController extends BaseController{
    @Autowired
    private UserService userService;
    @Autowired
    private FansService fansService;

    @PostMapping("login")
    @ApiOperation(value = "登陆", notes = "该接口不需要身份令牌")
    public ResponseEntity<JsonData> login(@RequestBody User user) {
        Map<String, Object> result = userService.login(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(JsonData.buildSuccess(result,"登录成功"));
    }

    @GetMapping("init")
    @ApiOperation(value = "初始化首页面", notes = "该接口不需要身份令牌")
    public ResponseEntity<JsonData> init(){
        return ResponseEntity.ok(JsonData.buildSuccess(userService.init(), "初始化数据"));
    }

    @GetMapping()
    public ResponseEntity<JsonData> getMessage(){
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

    @GetMapping("getToken")
    @ApiOperation(value = "获取一个token，用于测试", notes = "该接口不需要身份令牌")
    public ResponseEntity<String> getToken(@ApiParam("用户id") @RequestParam(name = "id") Integer id){
        User user = new User();
        user.setId(id);
        user.setNickName("Binary");
        user.setOpenid("otwpb5HTpiMlSH7EQ6r5Ezr7nNQw");
        user.setSession_key("aaaaaaaaaa");
        String token ="Bearer " + JwtUtils.geneJsonWebToken(user);
        return ResponseEntity.ok(token);
    }
}
