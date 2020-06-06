package com.sht.eurasiaring.repository;

import com.sht.eurasiaring.entity.Fans;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author Hongtao Shen
 * @date 2020/6/4 - 21:04
 **/
public interface FansRepository  extends JpaRepository<Fans,Integer>, JpaSpecificationExecutor<Fans> {

    Fans findByUserIdAndFansId(Integer userId, Integer fansId);

    Integer countByFansId(Integer userId);

    Integer countByUserId(Integer userId);

    List<Fans> findByUserId(Integer userId);

    List<Fans> findByFansId(Integer userId);
}
