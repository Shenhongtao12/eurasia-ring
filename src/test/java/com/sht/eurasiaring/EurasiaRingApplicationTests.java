package com.sht.eurasiaring;

import com.sht.eurasiaring.entity.Matter;
import com.sht.eurasiaring.repository.ClassifyRepository;
import com.sht.eurasiaring.entity.Classify;
import com.sht.eurasiaring.entity.Post;
import com.sht.eurasiaring.entity.User;
import com.sht.eurasiaring.service.MatterService;
import com.sht.eurasiaring.service.PostService;
import com.sht.eurasiaring.service.UserService;
import com.sht.eurasiaring.utils.PageResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;
import java.util.Optional;

@SpringBootTest
class EurasiaRingApplicationTests {
    @Autowired
    private UserService userService;

    @Autowired
    ClassifyRepository classifyRepository;

    @Autowired
    private MatterService matterService;

    @Autowired
    private PostService postService;

    @Test
    void contextLoads() {
        User user = new User();
        user.setNickName("111");
        user.setJs_code("0712yyRe1rxfZw0SA0Ue1h9GRe12yyRy");
        Map<String, Object> login = userService.login(user);
        System.out.println(login.get("token")); //{"session_key":"B116ftinrQYUVCWd+8DwuA==","openid":"otwpb5FYv-S_hVT9GQfeDyjRYIC4"}
    }


    @Test
    void test1(){
        Optional<Classify> byId = classifyRepository.findById(1);
        System.out.println(byId);
        System.out.println(byId.get());
        System.out.println(byId.isPresent());
    }

    @Test
    void test2(){
        System.out.println(postService.findUserIdByPostId(4));
    }


}
