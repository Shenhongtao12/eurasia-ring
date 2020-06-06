package com.sht.eurasiaring.service;


import com.sht.eurasiaring.entity.Fans;
import com.sht.eurasiaring.entity.Post;
import com.sht.eurasiaring.repository.FansRepository;
import com.sht.eurasiaring.utils.DateUtils;
import com.sht.eurasiaring.utils.JsonData;
import com.sht.eurasiaring.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class FansService {
    @Autowired
    private FansRepository fansRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate redisTemplate;

    //既可以收藏也可以取消收藏
    public JsonData save(Fans fans) {
        Fans fansRest = fansRepository.findByUserIdAndFansId(fans.getUserId(), fans.getFansId());
        if (fansRest != null) {
            fansRepository.deleteById(fansRest.getId());
            if (redisTemplate.hasKey("eu_fans-" + fans.getUserId()) && redisTemplate.opsForSet().isMember("eu_fans-" + fans.getUserId(), String.valueOf(fans.getFansId()))){
                redisTemplate.opsForSet().remove("eu_fans-" + fans.getUserId(), String.valueOf(fans.getFansId()));
            }
            return JsonData.buildSuccess("取消收藏成功");
        } else {
            fans.setCreateTime(DateUtils.dateToString());
            fansRepository.save(fans);
            redisTemplate.opsForSet().add("eu_fans-" + fans.getUserId(), String.valueOf(fans.getFansId()));
        }
        return JsonData.buildSuccess("收藏成功");
    }

    public JsonData delete(Integer id) {
        fansRepository.deleteById(id);
        return JsonData.buildSuccess("取消成功");
    }

    //userId：我的粉丝     fansId：我的关注
    public PageResult<Fans> findFansToUser(Integer userId, Integer fansId, Integer page, Integer rows) {
        Specification<Fans> spec = new Specification<Fans>() {
            @Override
            public Predicate toPredicate(Root<Fans> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                //根据属性名获取查询对象的属性
                //Path<Reply> path = root.get("nameId");
                //相当于 where receiverName = "Veggie", CriteriaBuilder接口中还有很多查询条件，建议看源码
                //Predicate equal = criteriaBuilder.equal(path, userId);
                if(userId != null){
                    list.add(criteriaBuilder.equal(root.get("userId"), userId));
                }
                if(fansId != null){
                    list.add(criteriaBuilder.equal(root.get("fansId"), fansId));
                }
                return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
            }
        };
        Page<Fans> fansPage = fansRepository.findAll(spec, PageRequest.of(page, rows));
        for (Fans fans : fansPage) {
            if (userId != null) {
                fans.setUser(this.userService.findUserById(fans.getFansId()));
            }
            fans.setUser(this.userService.findUserById(fans.getUserId()));
        }

        return new PageResult<>(fansPage.getTotalElements(), fansPage.getTotalPages(), fansPage.getContent());
    }

    public boolean checkFans(Integer userId, Integer toUserId) {
        return fansRepository.findByUserIdAndFansId(toUserId, userId) != null;
    }

    public Integer countNum(String type, Integer UserId){
        if ("fans".equals(type)){
            return fansRepository.countByFansId(UserId);
        }else {
            return fansRepository.countByUserId(UserId);
        }
    }

    public List<Fans> findByUserId(Integer userId) {
        return fansRepository.findByUserId(userId);
    }
    public List<Fans> findByFansId(Integer userId){
        return fansRepository.findByFansId(userId);
    }
}
