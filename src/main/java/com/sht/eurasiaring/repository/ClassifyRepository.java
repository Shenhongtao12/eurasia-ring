package com.sht.eurasiaring.repository;

import com.sht.eurasiaring.entity.Classify;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author Hongtao Shen
 * @date 2020/5/16 - 17:33
 **/
public interface ClassifyRepository extends JpaRepository<Classify, Long>, JpaSpecificationExecutor<Classify> {
}
