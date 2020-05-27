package com.sht.eurasiaring.repository;

import com.sht.eurasiaring.entity.Praise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Hongtao Shen
 * @date 2020/5/24 - 19:59
 **/
public interface PraiseRepository extends JpaRepository<Praise, Integer> {

    Praise findPraiseByTypeAndTypeIdAndUserId(String type, Integer typeId, Integer userId);

    List<Praise> findByTypeUserId(Integer userId);
}
