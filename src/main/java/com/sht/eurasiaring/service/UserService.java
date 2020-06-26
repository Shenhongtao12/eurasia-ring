package com.sht.eurasiaring.service;

import com.alibaba.fastjson.JSON;
import com.sht.eurasiaring.entity.Post;
import com.sht.eurasiaring.repository.UserRepository;
import com.sht.eurasiaring.entity.Carousel;
import com.sht.eurasiaring.entity.Classify;
import com.sht.eurasiaring.entity.User;
import com.sht.eurasiaring.exception.AllException;
import com.sht.eurasiaring.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Hongtao Shen
 * @date 2020/5/16 - 13:48
 **/
@Service
public class UserService {

    @Value("${WX_DATA.appid}")
    protected String APPID;
    @Value("${WX_DATA.secret}")
    protected String SECRET;
    @Value("${WX_DATA.grant_type}")
    protected String GRANT_TYPE;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClassifyService classifyService;

    @Autowired
    private CarouselService carouselService;

    @Autowired
    private MatterService matterService;
    @Autowired
    private PostService postService;

    @Autowired
    private StringRedisTemplate redisTemplate;


    public Map<String, Object> login(User user) {
        Map<String, Object> map = new HashMap<>();
        String url = "https://api.weixin.qq.com/sns/jscode2session?" +
                "appid=" + APPID +
                "&secret=" + SECRET +
                "&js_code=" + user.getJs_code() +
                "&grant_type=" + GRANT_TYPE;
        if (user.getFlag() == 2){
            url = "https://api.weixin.qq.com/sns/jscode2session?" +
                    "appid=" + "wx99ef068dd517514f" +
                    "&secret=" + "a50dd3326de4f70b26b30bfb1c94cb1c" +
                    "&js_code=" + user.getJs_code() +
                    "&grant_type=" + "authorization_code";
        }
        String data = HttpClientUtils.httpGet(url);
        User user1 = JSON.parseObject(data, User.class);
        if (user1.getErrcode() != null) {
            throw new AllException(-1, "登录失败，请重新登录");
        }
        user1.setNickName(user.getNickName());
        user1.setAvatarUrl(user.getAvatarUrl());
        //查看该用户是否为老用户
        User byOpenid = userRepository.findByOpenid(user1.getOpenid());
        if (byOpenid != null) {
            //老用户就更新信息
            user1.setId(byOpenid.getId());
            user1.setCreateTime(byOpenid.getCreateTime());
        } else {
            //新用户就设置首次登陆时间
            user1.setCreateTime(DateUtils.dateToString());
        }
        userRepository.save(user1);
        user.setId(user1.getId());
        String token = JwtUtils.geneJsonWebToken(user1);
        map.put("token", token);
        map.put("user", user);
        return map;
    }

    public Map<String, Object> init() {
        Map<String, Object> map = new HashMap<>();

        //轮播图数据
        List<Carousel> carouselList = carouselService.findAll();
        map.put("carouselList", carouselList);

        //分类数据
        List<Classify> classifyList = classifyService.findAll();
        map.put("classifyList", classifyList);
        //热门
        map.put("matterList", matterService.init());
        //帖子
        map.put("post", postService.init());
        return map;
    }

    public JsonData getMessage(Integer userId) {

        //获取回复数量
        String reply = redisTemplate.boundValueOps("eurasia_" + userId).get();
        int comNum = reply == null ? 0 : Integer.parseInt(reply);

        //获取关注的数量
        Long fans = redisTemplate.opsForSet().size("eu_fans-" + userId);
        int fansNum = fans == null ? 0 : fans.intValue();

        //获取点赞数量
        String praise = redisTemplate.boundValueOps("eu_praise_" + userId).get();
        int praiseNum = praise == null ? 0 : Integer.parseInt(praise);
        Map<String, Integer> map = new HashMap<>();
        map.put("comment", comNum);
        map.put("fans", fansNum);
        map.put("praise", praiseNum);
        return JsonData.buildSuccess(map, "");
    }

    public User findUserById(Integer id) {
        return userRepository.findById(id).get();
    }
}
