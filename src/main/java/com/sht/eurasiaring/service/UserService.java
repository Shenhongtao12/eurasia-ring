package com.sht.eurasiaring.service;

import com.alibaba.fastjson.JSON;
import com.sht.eurasiaring.dao.UserDao;
import com.sht.eurasiaring.entity.Carousel;
import com.sht.eurasiaring.entity.Classify;
import com.sht.eurasiaring.entity.User;
import com.sht.eurasiaring.exception.AllException;
import com.sht.eurasiaring.utils.DateUtils;
import com.sht.eurasiaring.utils.HttpClientUtils;
import com.sht.eurasiaring.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private UserDao userDao;

    @Autowired
    private ClassifyService classifyService;

    @Autowired
    private CarouselService carouselService;

    @Autowired
    private MatterService matterService;


    public Map<String, Object> login(User user) {
        Map<String, Object> map = new HashMap<>();
        String url = "https://api.weixin.qq.com/sns/jscode2session?" +
                "appid="+ APPID +
                "&secret="+ SECRET +
                "&js_code="+ user.getJs_code() +
                "&grant_type="+ GRANT_TYPE;
        String data = HttpClientUtils.httpGet(url);
        User user1 = JSON.parseObject(data, User.class);
        if (user1.getErrcode() != null) {
            throw new AllException(-1, "登录失败，请重新登录");
        }
        user1.setNickName(user.getNickName());
        user1.setAvatarUrl(user.getAvatarUrl());
        //查看该用户是否为老用户
        User byOpenid = userDao.findByOpenid(user1.getOpenid());
        if (byOpenid != null){
            //老用户就更新信息
            user1.setId(byOpenid.getId());
            user1.setCreateTime(byOpenid.getCreateTime());
        }else {
            //新用户就设置首次登陆时间
            user1.setCreateTime(DateUtils.dateToString());
        }
        userDao.save(user1);
        String token = JwtUtils.geneJsonWebToken(user1);
        map.put("token", token);
        map.put("user", user);
        return  map;
    }

    public Map<String, Object> init(){
        Map<String, Object> map = new HashMap<>();

        //轮播图数据
        List<Carousel> carouselList = carouselService.findAll();
        map.put("carouselList", carouselList);

        //分类数据
        List<Classify> classifyList = classifyService.findAll();
        map.put("classifyList", classifyList);
        //热门
        map.put("matterList", matterService.init());
        return map;
    }
}
